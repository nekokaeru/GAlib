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

package problem;

import java.util.Arrays;
import java.util.Random;

import util.MyRandom;

/**
 * Nk 問題.
 * @author mori
 * @version 1.0
 */
public class NkProblem extends AbstractProblem {
	/**
	 * 初期シード.
	 */
	private long initSeed_ = 0;

	/**
	 * 乱数オブジェクト.
	 */
	private Random rand_ = new Random(0);

	/**
	 * シード発生用． Java の乱数は性質が悪く，隣接したシードの挙動が似てしまうため．<br>
	 * TODO メルセンヌツイスターを書く！
	 */
	private Random seedGenerator_ = new Random(0);

	// private SecureRandom rand_ = new SecureRandom();

	/**
	 * Nk の k. 初期値 0
	 */
	private int k_ = 0;

	/**
	 * Nk の N. 初期値 10
	 */
	private int N_ = 10;

	/**
	 * 引数なしコンストラクタ
	 */
	public NkProblem() {
	}

	/**
	 * Nk の N と k で初期化.
	 * @param N NkのN
	 * @param k Nkのk
	 */
	public NkProblem(int N, int k) {
		setParameter(N, k, initSeed_);
	}

	/**
	 * Nk の N と k および 乱数のシードで初期化.
	 * @param N NkのN
	 * @param k Nkのk
	 * @param seed 乱数のシード
	 */
	public NkProblem(int N, int k, long seed) {
		setParameter(N, k, seed);
	}

	/**
	 * 目的関数値を計算して返す．
	 * @param array 遺伝子型情報
	 * @return Nk問題の値
	 */
	public double getObjectiveFunctionValue(Number[] array) {
		return getTotalValue(array, getK()) / getN(); // サイズで割る．
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "Nk";
	}

	/**
	 * array に応じた部分適応度を返す．
	 * @param array
	 * @return 部分適応度
	 */
	protected double getSubValue(Number[] array, int location) {
		/*
		 * 部分列と部分列の位置に対応して異なるシードが決まる． ※注意！ hashCode
		 * は完全ではない．異なる配列でも同じハッシュ値を持つこともある．
		 */
		long seed = initSeed_ + Arrays.hashCode(array) + location;// シードの更新
		seedGenerator_.setSeed(seed);
		// Java の乱数は性質が良くないので一回目は捨てる．
		seedGenerator_.nextLong();
		rand_.setSeed(seedGenerator_.nextLong());
		// Java の乱数は性質が良くないので一回目は捨てる．
		rand_.nextDouble();
		return rand_.nextDouble();
	}

	/**
	 * array の Nk 問題による評価値を計算する．結合は隣接のみ．<br>
	 * TODO ランダムコネクションの作成．
	 * @param array 対象記号列
	 * @param k Nk の k
	 * @return 評価値
	 */
	protected double getTotalValue(Number[] array, int k) {
		// Nk では k < N である．
		if (k >= array.length) {
			throw new IllegalArgumentException(k + ">=" + array.length);
		}
		// Nk では k < N である．
		if (getN() != array.length) {
			throw new IllegalArgumentException(getN() + "!=" + array.length);
		}

		// 部分列保存用．
		Number[] sub = new Number[k + 1];
		double sum = 0; // 評価値
		int N = array.length;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j <= k; j++) {
				sub[j] = array[(i + j) % N];
			}
			sum += getSubValue(sub, i);
		}
		return sum;
	}

	/**
	 * 乱数の初期シードを返す.
	 * @return 初期シード
	 */
	public long getInitSeed() {
		return initSeed_;
	}

	/**
	 * 乱数の初期シードを設定.
	 * @param initSeed 初期シード
	 */
	public void setInitSeed(long initSeed) {
		initSeed_ = initSeed;
	}

	/**
	 * Nk の k を返す.
	 * @return k
	 */
	public int getK() {
		return k_;
	}

	/**
	 * Nk の k を設定.
	 * @param k Nkのk
	 */
	public void setK(int k) {
		k_ = k;
	}

	/**
	 * Nk の N を返す.
	 * @return N
	 */
	public int getN() {
		return N_;
	}

	/**
	 * Nk の N を設定.
	 * @param n NkのN
	 */
	public void setN(int n) {
		N_ = n;
	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("N:" + getN() + ",k:" + getK() + ",seed:" + getInitSeed());
		if (getFunctionInfo().length() == 0) {
			return sb.toString();
		}
		return sb.toString() + "," + getFunctionInfo();
	}

	/**
	 * @param params パラメータの配列．文字列であることに注意．第一要素がN，第二要素がk 第三要素があれば乱数シード．
	 *        数値以外が指定されていたり，要素が2つでない場合には例外発生．
	 */
	public void setParameter(Object... params) {
		int N = 0, k = 0;
		long seed = 0;
		try {
			if (params.length != 2 && params.length != 3) {
				throw new Exception(
						"params length of NkProblem must be 2 or 3!");
			}
			// N 設定． 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) {
				N = ((Number) (params[0])).intValue();
			} else { // 数値系クラス以外の場合には文字列と判断して設定．
				N = Integer.parseInt(params[0].toString());
			}
			// k 設定． 文字列の場合と数値の場合で場合分け
			if (params[1] instanceof Number) {
				k = ((Number) (params[1])).intValue();
			} else { // 数値系クラス以外の場合には文字列と判断して設定．
				k = Integer.parseInt(params[1].toString());
			}
			// パラメータ数が3であればシード設定． 文字列の場合と数値の場合で場合分け
			if (params.length == 3) {
				if (params[2] instanceof Number) {
					seed = ((Number) (params[2])).longValue();
				} else { // 数値系クラス以外の場合には文字列と判断して設定．
					seed = Long.parseLong(params[2].toString());
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!\n"
					+ e.getMessage());
		}
		if (k >= N) {
			throw new IllegalArgumentException(k + ">=" + N);
		}
		if (k >= N) {
			throw new IllegalArgumentException(k + ">=" + N);
		}
		if (N < 0) {
			throw new IllegalArgumentException("N=" + N + " < 0");
		}
		if (k < 0) {
			throw new IllegalArgumentException("k=" + k + " < 0");
		}
		setN(N);
		setK(k);
		setInitSeed(seed);
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		MyRandom.setSeed(0);
		Number[] array = { 1, 0, 1, 0 };
		IProblem nk = new NkProblem();
		nk.setParameter(4, 2); // N=4, k=2 Nk問題では，遺伝子長=N の必要あり
		System.out.println(nk.getFitness(array));
		// toString により Nk{N:4,k:2,seed:0} が表示される．
		System.out.println(nk);
	}

}
