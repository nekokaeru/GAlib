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

package operator;

import fitness.FitnessManager;
import gabuilder.GAParameters;

import java.util.ArrayList;
import java.util.Arrays;

import population.Individual;
import population.Population;
import util.LocalTestException;

/**
 * 熱力学的選択.
 * @author mori
 * @version 1.0
 */

public class ThermoDynamicalSelection extends AbstractSelection {
	/**
	 * 温度
	 */
	private double temperature_ = GAParameters.DEFAULT_TEMPERATURE;

	/**
	 * 選択用に操作を施した一時的個体群．
	 */
	private ArrayList<Individual> currentPop_;

	/**
	 * 次世代の個体数． 入ってくる個体数の半分
	 */
	private int nextPopSize_ = -1;

	/**
	 * 計算途中の各遺伝子の数を保存． 現状はバイナリのみ．
	 */
	private int[][] numOfGene_;

	/**
	 * 自然数の Log を保存する． 計算量軽減のため．
	 */
	private double[] logValue_ = null;

	/**
	 * 圧縮を行うかのフラグ
	 */
	private boolean isPackIndivList_ = false;

	/**
	 * デフォルトコンストラクタ
	 */
	public ThermoDynamicalSelection() {
		super();
	}

	/**
	 * 個体群に選択を適用する.
	 * @param pop 個体群
	 */
	@Override
	public void apply(Population pop) {
		Individual elite = FitnessManager.getElite();
		if (elite != null) {
			pop.getIndivList().add(elite); // エリートを加える．
		}
		// 重複する個体を除いて個体群を圧縮する．温度が低ければ効果大．
		if (isPackIndivList_) {
			packIndivList(pop.getIndivList());
		}
		currentPop_ = pop.getIndivList(); // select で使えるように参照を保存．
		// ここで，select が呼ばれる．currentPop_ を保持しているので，個体の遺伝子型がわかる．
		// エントロピーを計算するときに必要．
		super.apply(pop);
	}

	/**
	 * TDGA では通常の SGA の2倍の個体数を半分にする．
	 * @param allFitness 全個体の適応度情報
	 * @return 選ばれた個体の index
	 */
	@Override
	public int[] select(double[] allFitness) {
		// 選ばれた個体の index を返す．
		int[] indexArray = new int[getNextPopSize()]; // 次世代は入ってきた個体の半数
		// 遺伝子の数の初期化
		Arrays.fill(numOfGene_[0], 0);
		Arrays.fill(numOfGene_[1], 0);
		// 自由エネルギー最小の個体の index
		int minIndex = -1;
		double F, Ebar = 0, Hj = 0; // F = Ebar - Hj*T
		// Fmin: 最小の自由エネルギー値保存用 Esum: エネルギーの総和保存用
		double Fmin, Esum = 0;
		for (int i = 0; i < getNextPopSize(); i++) { // 次世代の個体数分選択
			Fmin = Double.POSITIVE_INFINITY; // 最小化なので最初は∞
			minIndex = -1; // これは不要．エラーチェックのため.
			/*
			 * 現在の個体の中で自由エネルギーを最小化する個体を探す．currentPop_ はエリートを追加されたのち
			 * 圧縮されている可能性があるのでサイズは不明．選択する個体の index が必要なので拡張forは使わない．
			 */
			for (int j = 0; j < currentPop_.size(); j++) {
				// Hj はj 番目が入った時のエントロピー. すでに i 個体が決まっている．
				// これから選ぶ個体は i+1 個体番目
				Hj = entropy(i + 1, currentPop_.get(j));
				// 個体jのエネルギーは -1×適応度
				Ebar = (Esum + (-allFitness[j])) / (i + 1);// Ebar は平均エネルギー
				// Esum は今までの和
				F = Ebar - Hj * getTemperature();// 自由エネルギー F を計算する.
				if (F < Fmin) { // 現在の最小値より自由エネルギーが小さければ
					Fmin = F;
					minIndex = j; // 最小自由エネルギー個体のindexを保存．
				}
			}
			indexArray[i] = minIndex; // 最小個体の index を記録
			Esum += -allFitness[minIndex]; // エネルギーの更新
			updateNumOfGene(currentPop_.get(minIndex));
		}
		return indexArray;
	}

	/**
	 * 遺伝子座の遺伝子 1,0 の数を計算する.
	 * @param indiv 個体
	 */
	private void updateNumOfGene(Individual indiv) {
		for (int i = 0; i < indiv.size(); i++) {
			if ((Integer) indiv.getGeneAt(i) == 0) {
				numOfGene_[0][i]++;
			} else if ((Integer) indiv.getGeneAt(i) == 1) {
				numOfGene_[1][i]++;
			} else {
				// 現在は遺伝子 0,1 のみに対応
				throw new IllegalArgumentException("The gene "
						+ indiv.getGeneAt(i) + " is invalid!");
			}
		}

	}

	/**
	 * エントロピーを計算する.
	 * @param targetNum 何個体目を選択しているか
	 * @param candidate エントロピーを計算する候補個体
	 * @return エントロピー
	 */
	protected double entropy(int targetNum, Individual candidate) {
		double H1 = 0, Hall = 0; // H1：1ビット毎のエントロピー， H：全エントロピー
		// 全ビットを調べる
		for (int i = 0; i < candidate.size(); i++) {
			// if (candidate.getGeneAt(i).intValue() == 1) { // そのビットが1の時
			if ((Integer) candidate.getGeneAt(i) == 1) { // そのビットが1の時
				H1 = -(numOfGene_[0][i] * getLogValue(numOfGene_[0][i]) + (numOfGene_[1][i] + 1)
						* getLogValue(numOfGene_[1][i] + 1))
						/ targetNum + getLogValue(targetNum);
				// } else if (candidate.getGeneAt(i).intValue() == 0) {//
				// そのビットが0 の時
			} else if ((Integer) candidate.getGeneAt(i) == 0) {// そのビットが0 の時
				H1 = -(numOfGene_[1][i] * getLogValue(numOfGene_[1][i]) + (numOfGene_[0][i] + 1)
						* getLogValue(numOfGene_[0][i] + 1))
						/ targetNum + getLogValue(targetNum);
			} else { // 現在は遺伝子 0,1 のみに対応
				throw new IllegalArgumentException("The gene "
						+ candidate.getGeneAt(i) + " is invalid!");
			}
			Hall += H1; // i 番目のエントロピーを足す.
		}
		return Hall; // すべての遺伝子座の和をエントロピーとして返す．
	}

	/**
	 * 可変長引数を利用した配列で初期化． パラメータの第一要素が個体数, 第2要素が遺伝子長． <br>
	 * パラメータ数が3の場合は第3要素が温度. パラメータ数が4の場合は第4要素が個体群圧縮フラグ
	 * @param params パラメータの配列
	 */
	public void setParameter(Object... params) {
		int popSize = -1, cLength = -1;
		try {
			// 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) { // 数値クラスの場合
				popSize = ((Number) params[0]).intValue(); // 数値化して個体数の設定

			} else { // 文字列の場合
				popSize = Integer.parseInt(params[0].toString()); // 数値化して個体数の設定
			}
			// 文字列の場合と数値の場合で場合分け
			if (params[1] instanceof Number) { // 数値クラスの場合
				cLength = ((Number) params[1]).intValue();

			} else { // 文字列の場合
				cLength = Integer.parseInt(params[1].toString()); // 数値化して遺伝子長の設定
			}
			// パラメータ数が3以上なら3番目は温度
			if (params.length >= 3) {
				// 文字列の場合と数値の場合で場合分け
				if (params[2] instanceof Number) { // 数値クラスの場合
					temperature_ = ((Number) params[2]).doubleValue(); // 数値化して温度の設定
				} else { // 文字列の場合
					temperature_ = Double.parseDouble(params[2].toString()); // 数値化して温度の設定
				}
			}
			// パラメータ数が4なら4番目は個体群圧縮フラグ
			if (params.length == 4) {
				// Booleanの場合と文字列で場合分け
				if (params[3] instanceof Boolean) { // Booleanクラスの場合
					setPackIndivList((Boolean) params[3]);
				} else { // Boolean以外の場合
					// "true" 以外の文字列(大文字小文字は無関係)は false になる．
					setPackIndivList(Boolean.parseBoolean(params[3].toString()));
				}
			}
			// パラメータ数が4を超えた場合はおかしい
			if (params.length > 4) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid! ");
		}
		initLog(popSize);
		nextPopSize_ = popSize;
		numOfGene_ = new int[2][];
		numOfGene_[0] = new int[cLength]; // 各遺伝子座の0の数を保存
		numOfGene_[1] = new int[cLength]; // 各遺伝子座の1の数を保存
	}

	/**
	 * 熱力学的選択では，変形により 0 ～ size(個体数)までの log 値しか使わないようにできる．<br>
	 * そこで，計算量の多い log 値を始めに一回だけ計算してしまう．
	 * @param size 個体数
	 */
	private void initLog(int size) {
		logValue_ = new double[size + 1];
		// entropy 計算時には x*log(x) しか出てこない． x が 0 の極限でこの値は 0 に
		// なるので， logValue_[0] = 0 として問題ない．
		logValue_[0] = 0;
		for (int i = 1; i <= size; i++) {
			logValue_[i] = Math.log(i);
		}
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "ThermoDynamicalSelection";
	}

	/**
	 * 以前に initLog() で計算しておいた log(i) の値を返す.
	 * @param i 値
	 * @return log(i)
	 */
	public double getLogValue(int i) {
		return logValue_[i];
	}

	/**
	 * 次世代の個体数を返す.
	 * @return 次世代の個体数
	 */
	public int getNextPopSize() {
		if (nextPopSize_ == -1) {
			throw new IllegalArgumentException(
					"Please call init([pop_size,chromosome_length])!");
		}
		return nextPopSize_;
	}

	/**
	 * 同一の遺伝子型を持つ個体を削除． 熱力学的選択では同一の個体が複数あっても結果は同じ．
	 * @param indivList 圧縮する個体のリスト．
	 */
	public void packIndivList(ArrayList<Individual> indivList) {
		//浅いコピーを作る.
		ArrayList<Individual> copyList = new ArrayList<Individual>(indivList);
		indivList.clear(); // 本体の中身を消す．
		boolean isSelected = false;
		for (int i = 0; i < copyList.size(); i++) {
			isSelected = false;
			for (int j = 0; j < indivList.size(); j++) {
				// もしも同じ遺伝子型の個体がいれば
				if (indivList.get(j).equals(copyList.get(i))) {
					isSelected = true; // すでに選ばれている．
					break;
				}
			}
			if (!isSelected) { // 選ばれていなければ追加．
				indivList.add(copyList.get(i));
			}
		}
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		Individual indiv1 = new Individual("000");
		Individual indiv2 = new Individual("111");
		Individual indiv3 = new Individual("100");
		ThermoDynamicalSelection td = new ThermoDynamicalSelection();
		td.setParameter(10, 3);
		System.err.println(td.entropy(1, indiv1));
		td.updateNumOfGene(indiv1);
		System.err.println(td.entropy(2, indiv2) + " " + Math.log(2) * 3);
		td.updateNumOfGene(indiv2);
		double h = -3
				* (Math.log(1. / 3.) * 1. / 3. + Math.log(2. / 3.) * 2. / 3.);
		System.err.println(td.entropy(3, indiv3) + " " + h);
	}

	/**
	 * 温度を返す.
	 * @return 温度
	 */
	public double getTemperature() {
		return temperature_;
	}

	/**
	 * 温度を設定する.
	 * @param temperature 温度
	 */
	public void setTemperature(double temperature) {
		temperature_ = temperature;
	}

	/**
	 * ローカルテスト initLog メソッドのテストを行う.
	 * @throws LocalTestException
	 */
	public void localTest() throws LocalTestException {
		// initLog のテスト
		initLog(100);
		if (logValue_[0] != 0.0) {
			throw new LocalTestException("logValue_[0]!=0!");
		}
		for (int i = 1; i < logValue_.length; i++) {
			if (logValue_[i] != Math.log(i)) {
				throw new LocalTestException("logValue_[" + i + "] is wrong!");
			}
		}
	}

	/**
	 * パラメータの情報を返す．例： TEMPERATURE:36.0
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		String info = "TEMPERATURE:" + String.valueOf(getTemperature())+",isPackIndivList:"+String.valueOf(isPackIndivList_);
		String s = super.getParameterInfo();
		if (s.equals("")) {
			return info;
		} else {
			return info + "," + s;
		}
	}

	/**
	 * 個体群を圧縮するかのフラグ
	 * @return フラグ
	 */
	public boolean isPackIndivList() {
		return isPackIndivList_;
	}

	/**
	 * 個体群圧縮フラグの設定
	 * @param isPackIndivList
	 */
	public void setPackIndivList(boolean isPackIndivList) {
		this.isPackIndivList_ = isPackIndivList;
	}
}
