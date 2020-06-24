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
 * 線形変換関数を表すクラス.
 * y = gradient * x + intercept
 * gradient は傾き, intercept は切片
 * @author mori
 * @version 1.0
 */

public class LinearFunction implements IFunction {
	/**
	 * 傾き.
	 */
	private double gradient_;

	/**
	 * 切片．
	 */
	private double intercept_;

	/**
	 * 引数なしコンストラクタ. 傾き 3, 切片 1 で初期化
	 */
	public LinearFunction() {
		this(3, 1);
	}

	/**
	 * 傾きと切片で初期化
	 * @param a
	 * @param b
	 */
	public LinearFunction(double a, double b) {
		gradient_ = a;
		intercept_ = b;
	}

	/**
	 * 可変長引数を利用した配列で初期化．単に setParameter を呼ぶだけ．
	 * @param params パラメータ配列
	 */
	public LinearFunction(Object... params) {
		setParameter(params);
	}

	/**
	 * @param value 関数への入力値
	 * @return 線形スケーリングの結果
	 */
	public double function(double value) {
		return getGradient() * value + getIntercept();
	}

	/**
	 * @see problem.function.IFunction#setParameter(java.lang.Object...)
	 * @param params パラメータの配列．第一要素が傾き，第二要素が切片．数値以外が指定されていたり，要素が2つでない場合には例外発生．
	 */
	public void setParameter(Object... params) {
		try {
			if (params.length != 2) {
				throw new Exception(
						"params length of LinearFunction must be 2!");
			}
			// 傾き設定． 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) {
				setGradient(((Number) (params[0])).doubleValue());
			} else { // 数値系クラス以外の場合には文字列と判断して設定．
				setGradient(Double.parseDouble(params[0].toString()));
			}
			// 切片設定． 文字列の場合と数値の場合で場合分け
			if (params[1] instanceof Number) {
				setIntercept(((Number) (params[1])).doubleValue());
			} else { // 数値系クラス以外の場合には文字列と判断して設定．
				setIntercept(Double.parseDouble(params[1].toString()));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "Linear";
	}

	/**
	 * 傾きを返す.
	 * @return 傾き
	 */
	public final double getGradient() {
		return gradient_;
	}

	/**
	 * 傾きを設定.
	 * @param gradient 傾き
	 */
	public final void setGradient(double gradient) {
		gradient_ = gradient;
	}

	/**
	 * 切片を返す.
	 * @return 切片
	 */
	public final double getIntercept() {
		return intercept_;
	}

	/**
	 * 切片を設定.
	 * @param intercept 切片
	 */
	public final void setIntercept(double intercept) {
		intercept_ = intercept;
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		// y=3x+1
		LinearFunction f = new LinearFunction(3, 1);
		// 7.0が表示される
		System.out.println(f.function(2));
	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "GRADIENT:" + String.valueOf(getGradient()) + ",INTERCEPT:"
				+ String.valueOf(getIntercept());
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
