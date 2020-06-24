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

/**
 * 遺伝子型を 2 進数として適応度を算出する問題．例 0001 : 適応度1 0011 : 適応度3 1111 : 適応度15
 * @author mori
 * @version 1.0
 */
public class BinaryNumberProblem extends AbstractProblem {
	/**
	 * 目的関数値を計算して返す．
	 * @param array 遺伝子型情報
	 * @return 2進数を10進数に変えた数
	 * @see problem.AbstractProblem#getObjectiveFunctionValue(Number[])
	 */
	public double getObjectiveFunctionValue(Number[] array) {
		double result = 0;
		for (int i = 0; i < array.length; i++) {
			// 0, 1 以外の値の場合は例外発生．
			if (array[i].intValue() != 0 && array[i].intValue() != 1) {
				throw new IllegalArgumentException(array[i] + " != 0 and != 1!");
			}
			result += array[i].doubleValue()
					* Math.pow(2, array.length - i - 1);
		}
		return result;
	}

	/**
	 * 問題の名前を返す．
	 * @return 問題の名前
	 * @see problem.AbstractProblem#getName()
	 */
	public String getName() {
		return "BinaryNumber";
	}

	/**
	 * 可変長引数を利用した配列で初期化． パラメータなし．
	 * @param params パラメータの配列
	 */
	public void setParameter(Object... params) {
		// パラメータ無し
		if (params == null) {
			return;
		}
		try {
			// 長さ0の配列は認める．
			if (params.length != 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return getFunctionInfo();
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		BinaryNumberProblem bnp = new BinaryNumberProblem();
		Number[] a = { 1, 0, 1, 0 };
		// 10.0 が表示される．
		System.out.println(bnp.getObjectiveFunctionValue(a));
	}

}
