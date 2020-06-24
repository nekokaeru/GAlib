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
 * べき乗関数を表すクラス.
 * y = x^exponent
 * exponent は指数部
 * @author mori
 * @version 1.0
 */

public class PowerFunction implements IFunction {
	/**
	 * 指数．
	 */
	private double exponent_ = 2;

	/**
	 * 入力値 x にある変換を施して出力する関数の本体．
	 * @param value 関数への入力値
	 * @return べき乗スケーリングの結果
	 */
	public double function(double value) {
		return Math.pow(value, exponent_);
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "Power";
	}

	/**
	 * 引数なしコンストラクタ なにもしない.
	 */
	public PowerFunction() {
	}

	/**
	 * 指数部で初期化.
	 * @param exponent 指数部
	 */
	public PowerFunction(double exponent) {
		setExponent(exponent);
	}

	/**
	 * 指数部を返す.
	 * @return 指数部
	 */
	public final double getExponent() {
		return exponent_;
	}

	/**
	 * 指数部を設定.
	 * @param exponent 指数部
	 */
	public final void setExponent(double exponent) {
		exponent_ = exponent;
	}

	/**
	 * @see problem.function.IFunction#setParameter(Object[])
	 * @param params パラメータの配列．Object の配列であることに注意．第一要素が指数．
	 *        数値以外が指定されていたり，要素が1つでない場合には例外を吐く．
	 */
	public void setParameter(Object... params) {
		try {
			// パラメータの要素数が1個でないとき
			if (params.length != 1) {
				throw new Exception("params length of PowerFunction must be 1!");
			}
			// 指数設定． 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) { // 数値クラスの場合
				setExponent(((Number) params[0]).doubleValue()); // 指数設定

			} else { // 文字列の場合
				double x = Double.parseDouble(params[0].toString()); // 指数設定
				setExponent(x);
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
		// y = x ^ 3
		PowerFunction f = new PowerFunction();
		f.setParameter("3");
		// 27.0 が表示される.
		System.out.println(f.function(3));

	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "EXPONENT:" + String.valueOf(getExponent());
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
