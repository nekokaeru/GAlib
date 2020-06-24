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

package problem.function;

import java.util.Arrays;

/**
 * 符号を逆転する関数を表すクラス.
 * y = -x
 * @author mori
 * @version 1.0
 */

public class NegativeFunction implements IFunction {

	/**
	 * 入力値 value にある変換を施して出力する関数の本体．
	 * @param value 入力
	 * @return 入力の符号を逆転して返す
	 */
	public double function(double value) {
		return -value;
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "Negative";
	}

	/**
	 * @see problem.function.IFunction#setParameter(java.lang.Object...)
	 * @param params パラメータの配列． NegativeFunction では可調整パラメータはない．
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
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		NegativeFunction f = new NegativeFunction();
		// String[] p = {"2","3a","4"};
		Object[] p = new Object[0];
		f.setParameter(p);
		// -3.0 が表示される.
		System.out.println(f.function(3));

	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "";
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
