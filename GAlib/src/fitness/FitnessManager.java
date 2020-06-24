/*
 * Copyright (c) 2007 Naoki Mori
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fitness;

import gabuilder.GAParameters;

import java.util.ArrayList;

import population.Individual;
import problem.BitCountProblem;
import problem.IProblem;
import util.MyRandom;

/**
 * 適応度を管理するユーティリティクラス.
 * @author mori
 * @version 1.0
 */
public class FitnessManager {
	/**
	 * エリート個体保存用リスト．適応度を計算した時に更新される．
	 */
	private static ArrayList<Individual> eliteList_;

	/**
	 * エリート個体の適応度．エリート個体かどうかを確認するために使用．
	 */
	private static double eliteFitness_ = Double.NEGATIVE_INFINITY;

	/**
	 * 問題オブジェクト．適応度を問題から計算する．
	 */
	private static IProblem problem_;

	/**
	 * 適応度計算の高速化をするかのフラグ．(高速化フラグ)
	 */
	private static boolean isTurbo_ = GAParameters.DEFAULT_IS_TURBO;

	/**
	 * 総適応度評価回数
	 */
	private static long totalEvalNum_ = 0;

	/**
	 * 遺伝子型と適応度の記憶クラス．
	 */
	private static IFitnessMemoryBank memoryBank_;

	/**
	 * 個体の適応度情報を記憶するかのフラグ．(個体メモリフラグ)
	 */
	private static boolean isMemory_ = GAParameters.DEFAULT_IS_MEMORY;

	/**
	 * デフォルトコンストラクタ． Utility Class なのでコンストラクタを private
	 */
	private FitnessManager() {
	}

	/**
	 * 個体群のすべての個体の適応度を double の配列に格納して返す．
	 * @param pop 個体群
	 * @return 個体群におけるすべての個体の適応度の配列
	 */
	public static double[] getFitnessArray(ArrayList<Individual> pop) {
		// 適応度の初期化
		double[] allFitness = new double[pop.size()];
		for (int i = 0; i < pop.size(); i++) {
			double f = pop.get(i).fitness();
			allFitness[i] = f;
		}
		return allFitness;
	}

	/**
	 * 適応度を計算する． getChromosome は染色体の変更を禁止するために配列の clone を 返すが，<br>
	 * このロスを避けるために Individual の fitness で直接染色体を渡している．
	 * @param chromosome 染色体
	 * @return chromosome の適応度
	 * @see Individual#fitness()
	 */
	public static double getFitness(Number[] chromosome) {
		totalEvalNum_++; // 適応度を一回計算．
		return getProblem().getFitness(chromosome);
	}

	/**
	 * 適応度を計算する． 基本的には getFitness と同じだが，記録に残らない． <br>
	 * 具体的には適応度評価回数は増えず，エリートも更新しない．<br>
	 * 途中のログなど探索に関係のない適応度計算のためにある． chromosome を直接渡す．
	 * @param chromosome 染色体
	 * @return chromosome の適応度
	 * @see Individual#fitness()
	 * @see FitnessManager#getFitness(Number[])
	 */
	public static double getFitnessWithoutRecord(Number[] chromosome) {
		return getProblem().getFitness(chromosome);
	}

	/**
	 * エリート個体リストを更新する関数．
	 * @param indiv 検査個体
	 * @param f 検査個体の適応度
	 * @return 更新されたか
	 * @see Individual#fitness()
	 */
	public static boolean updateElite(Individual indiv, double f) {
		// 適応度が等しい場合．
		if (f == eliteFitness_) {
			setElite(indiv);
			return true;
		} else if (f > eliteFitness_) {
			getEliteList().clear();
			setElite(indiv);
			eliteFitness_ = f;
			return true;
		}

		return false;
	}

	/**
	 * エリート個体を取得する関数. 複数のエリートが存在する場合には，ランダムにひとつ選ぶ．<br>
	 * エリート個体のクローンが返る. エリート個体が存在しない場合は null が返る.
	 * @return エリート個体
	 * @see Individual#fitness()
	 */
	public static Individual getElite() {
		int size = getEliteList().size();
		// エリートが一つだけの場合．
		if (size == 1) {
			return getEliteList().get(0).clone();
			// 複数存在する場合．
		} else if (size > 1) {
			int num = MyRandom.getInstance().nextInt(size);
			return getEliteList().get(num).clone();
			// エリート個体リストが空の場合．
		} else {
			return null;
		}
	}

	/**
	 * エリート個体を保存したリストの深いコピーを返す．
	 * @return エリート個体を要素に持つリスト
	 */
	public static ArrayList<Individual> getCopyOfEliteList() {
		ArrayList<Individual> deepCopy = new ArrayList<Individual>();
		for (Individual individual : getEliteList()) {
			deepCopy.add(individual.clone());
		}
		return deepCopy;
	}

	/**
	 * 個体をエリート個体として登録する.
	 * @param elite 登録対象個体
	 */
	private static void setElite(Individual elite) {
		// この elite がまだ保存されていなければ
		if (!getEliteList().contains(elite)) {
			getEliteList().add(elite.clone());
		}
	}

	/**
	 * エリート個体の適応度を取得する.
	 * @return エリート個体の適応度
	 */
	public static double getEliteFitness() {
		return eliteFitness_;
	}

	/**
	 * FitnessManager に設定されている問題を取得する関数. 問題が設定されていない (null) 場合は,
	 * デフォルトのビットカウント問題を返す.
	 * @return 設定されている問題
	 */
	public static IProblem getProblem() {
		if (problem_ == null) {
			// デフォルトはビットカウント
			problem_ = new BitCountProblem();
		}
		return problem_;
	}

	/**
	 * 適応度を計算するための問題を設定する．
	 * @param problem 設定する問題．
	 */
	public static void setProblem(IProblem problem) {
		problem_ = problem;
	}

	/**
	 * 高速化フラグを返す. 今の世代以前に適応度を計算されている個体については, 適応度の再計算を行わない高速化手法.
	 * @return 高速化フラグの状態. true: 高速化が設定されている false: 高速化が設定されていない
	 */
	public static boolean isTurbo() {
		return isTurbo_;
	}

	/**
	 * 高速化フラグの設定を行う.
	 * @param isTurbo 高速化フラグの状態 true: 高速化を設定する false: 高速化を設定しない
	 */
	public static void setTurbo(boolean isTurbo) {
		FitnessManager.isTurbo_ = isTurbo;
	}

	/**
	 * 現時点の適応度評価回数を取得する.
	 * @return 適応度評価回数
	 */
	public static long getTotalEvalNum() {
		return totalEvalNum_;
	}

	/**
	 * 各種情報を初期化する．<br>
	 * エリート個体，最大適応度，適応度評価回数，問題，(設定されていれば)記憶情報 高速化フラグ，個体メモリフラグ
	 */
	public static void reset() {
		getEliteList().clear();
		eliteFitness_ = Double.NEGATIVE_INFINITY;
		totalEvalNum_ = 0;
		problem_ = null;
		setTurbo(false);
		setMemory(false);
	}

	/**
	 * 個体メモリフラグを返す.
	 * @return 個体メモリフラグ true: 記憶情報を利用している false: 記憶方法を利用していない
	 */
	public static boolean isMemory() {
		return isMemory_;
	}

	/**
	 * 個体メモリフラグの設定を行う.
	 * @param isMemory 個体メモリフラグ true: 記憶情報を利用する false: 記憶情報を利用しない
	 */
	public static void setMemory(boolean isMemory) {
		// 記憶をやめた場合には記憶領域を解放
		if (isMemory == false) {
			memoryBank_ = null;
		}
		FitnessManager.isMemory_ = isMemory;
	}

	/**
	 * 記憶クラスを取得する. 記憶情報 (個体メモリ) が存在しなければ, 新たに空の記憶情報のオブジェクトを生成し返す.
	 * @return 記憶クラス
	 */
	public static IFitnessMemoryBank getMemoryBank() {
		if (memoryBank_ == null) {
			// デフォルト
			memoryBank_ = new FitnessMemoryBank();
		}
		return memoryBank_;
	}

	/**
	 * 記憶情報をセットする.
	 * @param memoryBank 記憶情報
	 */
	public static void setMemoryBank(IFitnessMemoryBank memoryBank) {
		memoryBank_ = memoryBank;
	}

	/**
	 * エリート個体を保存したリストを返す.
	 * @return エリート個体を要素に持つリスト
	 */
	private static ArrayList<Individual> getEliteList() {
		if (eliteList_ == null) {
			eliteList_ = new ArrayList<Individual>();
		}
		return eliteList_;
	}
}
