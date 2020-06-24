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
 * いろいろな統計量を計算するための Utility Class
 */
package util;

/**
 * 統計用クラス.
 * @author mori
 * @version 1.0
 */
public final class NStatistics {
	/**
	 * 期待値．
	 * @param p 確率
	 * @param N 総回数
	 * @return 期待値
	 */
	public static double E(double p, long N) {
		if (p < 0 || p > 1 || N < 0) {
			throw new IllegalArgumentException();
		}
		return p * N;
	}

	/**
	 * 2項分布に従う場合の分散．
	 * @param p
	 * @param N
	 * @return 分散
	 */
	public static double V(double p, long N) {
		if (p < 0 || p > 1 || N < 0) {
			throw new IllegalArgumentException();
		}
		return N * p * (1 - p);
	}

	/**
	 * 2項分布に従う場合の標準偏差．
	 * @param p
	 * @param N
	 * @return 標準偏差
	 */
	public static double SD(double p, long N) {
		if (p < 0 || p > 1 || N < 0) {
			throw new IllegalArgumentException();
		}
		return Math.sqrt(N * p * (1 - p));
	}

	/**
	 * 2項実験の結果テスト．確率 p で起きる事象を totalNum 回繰り返した結果，実際は resultNum 回
	 * その事象が起きた．これが統計的に正しいかを調べる．具体的には結果が正規分布で近似できること
	 * を利用して，期待値と実際の回数の誤差が n*σ に入っているかを返す．<br> 
	 * |p*totalNum - resultNum| < n*√(totalNum*p*(1-p)) となる確率は 
	 * n=1 -> 0.6827, n=2->0.9545, n=3 ->0.9973, n=4 -> 0.9999 となる．普通は n=4 で十分．
	 * @param totalNum 実験の総回数
	 * @param resultNum ある事象の起きた回数
	 * @param p ある事象の起きる確率
	 * @param n 結果が n*σ に入っているかを調べる
	 * @return 条件を満たせば真を返す
	 */
	public static boolean binomialTest(long totalNum, long resultNum, double p,
			double n) {
		if (p < 0 || p > 1 || totalNum < 0 || resultNum < 0 || n < 0) {
			throw new IllegalArgumentException();
		}
		double E = totalNum * p;
		double sig = Math.sqrt(totalNum * p * (1 - p));
		return Math.abs(E - resultNum) <= n * sig;
	}
}
