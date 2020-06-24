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

/**
 * @author mori
 * @version 0.1 05/09/13
 */
package population;

import java.util.Arrays;

import util.MyRandom;
import fitness.ChangedFlagAndMemoryFitness;
import fitness.FitnessManager;
import fitness.IFitness;
import gabuilder.GAParameters;

/**
 * 個体クラス．以下染色体と遺伝子型は同じ意味で用いている．
 * @author mori
 * @version 1.0
 */
public class Individual implements Cloneable {
	/**
	 * 染色体．高速化のために配列を使う．
	 */
	private Number[] chromosome_;

	/**
	 * 変更があったかのフラグ
	 */
	private boolean isChanged_ = true;

	/**
	 * 以前の適応度
	 */
	private double previousFitness_ = Double.NaN;

	/**
	 * 拡張された適応度計算クラス
	 */
	private static IFitness fitnessCalculator_;

	/**
	 * 遺伝子型表示時の遺伝子間の区切り文字
	 */
	private static String separator_ = "";

	/**
	 * 引数なしコンストラクタ
	 */
	public Individual() {

		chromosome_ = new Number[GAParameters.DEFAULT_CHROMOSOME_LENGTH];
		init();
	}

	/**
	 * 引数つきコンストラクタ
	 * @param size 遺伝子長
	 */
	public Individual(int size) {
		chromosome_ = new Number[size];
		init();
	}

	/**
	 * 染色体で初期化. 染色体自体は浅いコピーになっているが Integer, Double などの不変オブジェクトの配列の場合は問題なし.
	 * Number を継承した自作のクラスを用いる場合は不変オブジェクトである必要がある．
	 * @param chromosome 染色体
	 */
	public Individual(Number[] chromosome) {
		setChromosome(chromosome);
	}

	/**
	 * 染色体(文字列)で初期化 "10010" など. 数値以外を含んでいる場合には例外発生． TODO
	 * 今は遺伝子が一桁しか駄目．必要があればセパレータを設定したバージョンを作る．
	 * @param chromStr 遺伝子の文字列表現
	 */
	public Individual(String chromStr) {
		// String の matches は完全一致． \\D だけだと一文字の場合のみなので駄目．
		if (chromStr.matches(".*\\D.*")) {
			// 数字しか許さない．
			throw new IllegalArgumentException(
					"A gene must be numerical value!");
		}
		String[] array = chromStr.split("\\B");// 非単語境界で分割
		Number[] chromosome = new Number[array.length];
		for (int i = 0; i < array.length; i++) {
			chromosome[i] = Integer.parseInt(array[i]);
		}
		chromosome_ = chromosome;
	}

	/**
	 * コピーコンストラクタ
	 * @param indiv コピー対象個体
	 */
	public Individual(Individual indiv) {
		chromosome_ = new Number[indiv.chromosome_.length];
		System.arraycopy(indiv.chromosome_, 0, chromosome_, 0,
				chromosome_.length);
		// 適応度の更新情報も受け継ぐ
		setChanged(indiv.isChanged());
		setPreviousFitness(indiv.getPreviousFitness());
	}

	/**
	 * ランダムに染色体を初期化．現在は対立遺伝子 {0,1} のみに対応． TODO 対立遺伝子が3以上の場合の実装．
	 */
	public void init() {
		for (int i = 0; i < chromosome_.length; i++) {
			chromosome_[i] = MyRandom.getInstance().nextInt(2);
		}
	}

	/**
	 * 深いコピーをするために，clone をオーバーライド． JDK5.0 の共変戻り値を利用． ただし，Number
	 * オブジェクトは浅いコピーになっているが，Integer，Double 等の 不変オブジェクトの場合は問題なし．
	 * @return 深いコピーされた個体
	 */
	@Override
	public Individual clone() {
		try {
			Individual i = (Individual) super.clone();
			Number[] tmp = new Number[size()];
			System.arraycopy(chromosome_, 0, tmp, 0, size());
			i.setChromosome(tmp);
			// 適応度の更新情報も受け継ぐ
			i.setChanged(isChanged());
			i.setPreviousFitness(getPreviousFitness());
			return i;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e.getMessage());
		}
	}

	/**
	 * equals の実装．Javaの鉄則 12 参照が異なっていても遺伝子型の内容が等しければ同値とする．
	 * ただし，継承によってクラスが違う場合には常に偽を返す．なお，同じ 1 でも型が異なれば equals の結果は偽になる．
	 * @return 染色体が同じ内容なら true 違うなら false
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if ((obj != null) && (getClass() == obj.getClass())) {
			return Arrays.equals(chromosome_, ((Individual) obj).chromosome_);
		}
		return false;
	}

	/**
	 * 染色体のハッシュ値を返す． 染色体が同じなら同じ値を返す．
	 * @return 染色体をハッシュ値
	 */
	@Override
	public int hashCode() {
		return Arrays.hashCode(chromosome_);
	}

	/**
	 * 染色体を返す．
	 * @return 染色体
	 */
	public final Number[] getChromosome() {
		return chromosome_.clone();
	}

	/**
	 * 指定した遺伝子座の遺伝子を返す．
	 * @return 指定された遺伝子
	 */
	public final Number getGeneAt(int index) {
		return chromosome_[index];
	}

	/**
	 * 染色体をセットし，変換フラグを true にする．
	 * @param chromosome 染色体
	 */
	public final void setChromosome(Number[] chromosome) {
		// clone など同じ染色体でも更新の必要あり．
		setChanged(true); // 変更があった．
		chromosome_ = new Number[chromosome.length];
		// choromosome_ と chromosome は同じ Number オブジェクトを指すが，
		// Integer 等の不変オブジェクトであれば問題なし．
		System.arraycopy(chromosome, 0, chromosome_, 0, chromosome.length);
	}

	/**
	 * 染色体の index にあたる部分を value に変える． 変えられたときは，変換フラグを true にする．
	 * @param index 遺伝子座
	 * @param value 遺伝子
	 */
	public final void setGeneAt(int index, Number value) {
		if (!chromosome_[index].equals(value)) {
			setChanged(true); // 変更があった．
			chromosome_[index] = value;
		}
	}

	/**
	 * 染色体の遺伝子長を返す．
	 * @return 遺伝子長
	 */
	public final int size() {
		return chromosome_.length;
	}

	/**
	 * 染色体を String に変換して返す．
	 * @return 染色体（文字列）
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < size(); i++) {
			sb.append(chromosome_[i]);
			if (i < size() - 1) {
				sb.append(getSeparator());
			}
		}
		return sb.toString();
	}

	/**
	 * 個体の適応度を返す． 選択時に必要なスケーリングは適用されていない値である． エリートは毎回保存だけはする．<br>
	 * デフォルトは ChangedFlagAndMemoryFitness が fitnessCalculator_ なので
	 * 高速化フラグが設定されている場合は，遺伝的操作を受けていない個体はフラグを利用して previousFitness の値を返す．
	 * 記憶フラグが設定されている場合は，遺伝的操作を受けて MemoryBank に保存されていれば，
	 * FitnessManager.getMemoryBank().getFitness(indiv) で適応度を返す．
	 * @return 適応度
	 * @see FitnessManager#getMemoryBank()
	 */
	public double fitness() {
		double f = getFitnessCalculator().fitness(this); // 適応度を得る．
		FitnessManager.updateElite(this, f);// elite を更新する．
		return f;
	}

	/**
	 * 高速化や記憶メモリ使用しない，シンプルな従来の適応度関数． エリートも保存しない．<br>
	 * ただし，適応度評価回数はカウントされる．
	 * @return 適応度
	 */
	public double simpleFitness() {
		return FitnessManager.getFitness(chromosome_);
	}

	/**
	 * 適応度を計算する． 基本的には simpleFitness と同じだが，適応度評価回数もカウントされない．<br>
	 * 途中のログなど探索に関係のない適応度計算のための補助的なメソッド.
	 * @return 適応度
	 * @see FitnessManager#getFitnessWithoutRecord(Number[])
	 */
	public double fitnessWithoutRecord() {
		return FitnessManager.getFitnessWithoutRecord(chromosome_);
	}

	/**
	 * 生の適応度，つまり目的関数値そのものを返す. 適応度評価回数はカウントされない．
	 * @return 生の適応度(目的関数値)
	 */
	public double rawFitness() {
		return FitnessManager.getProblem().getObjectiveFunctionValue(
				chromosome_);
	}

	/**
	 * 染色体の変換の有無を調べるフラグを返す． 染色体が遺伝的操作を受けた時，true を返す．
	 * @return 染色体の遺伝的操作の有無． 遺伝的操作を受けた時は true を返し，受けていない時は false を返す．
	 */
	public final boolean isChanged() {
		return isChanged_;
	}

	/**
	 * 染色体の変換の有無を調べるフラグをセットする． 染色体が遺伝的操作を受けた時，true になる．
	 * @param isChanged
	 */
	public final void setChanged(boolean isChanged) {
		this.isChanged_ = isChanged;
	}

	/**
	 * 直前の適応度を返す．
	 * @return 前の適応度
	 */
	public final double getPreviousFitness() {
		return previousFitness_;
	}

	/**
	 * 最新の適応度をセットする．
	 * @param previousFitness 前の適応度
	 */
	public final void setPreviousFitness(double previousFitness) {
		previousFitness_ = previousFitness;
	}

	/**
	 * 適応度を計算するインスタンスを返す．
	 * @return 適応度を計算するインスタンス
	 */
	public static IFitness getFitnessCalculator() {
		if (fitnessCalculator_ == null) {
			fitnessCalculator_ = new ChangedFlagAndMemoryFitness();
		}
		return fitnessCalculator_;
	}

	/**
	 * 適応度を計算するインスタンスをセットする.
	 * @param fitnessCalculator 適応度を計算するインスタンス
	 */
	public static void setFitnessCalculator(IFitness fitnessCalculator) {
		fitnessCalculator_ = fitnessCalculator;
	}

	/**
	 * 遺伝子型表示時の区切り文字を得る．
	 * @return 区切り文字
	 */
	public static String getSeparator() {
		return separator_;
	}

	/**
	 * 遺伝子型表示時の区切り文字を設定する．
	 * @param s 区切り文字
	 */
	public static void setSeparator(String s) {
		separator_ = s;
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		Individual indiv = new Individual(new Number[] { 1, 1, 1, 0, 0 });
		// デフォルト問題であるビットカウントで適応度を算出.
		System.out.println(indiv.fitness());
		Individual indiv2 = new Individual(new Number[] { 1, 0, 1 });
		Individual indiv3 = new Individual(new Number[] { 1, 0, 1 });
		Individual indiv4 = new Individual(new Number[] { 1, 0, 1L });
		// indiv2 と indiv3 は同一の染色体を持つため表示は true.
		System.out.println(indiv2.equals(indiv3));
		// indiv2 と indiv4 は最後の遺伝子が Integer と Long で異なるため表示はfalse.
		System.out.println(indiv2.equals(indiv4));
	}
}
