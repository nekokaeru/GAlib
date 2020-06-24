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
 * シグモイド型の変換をする関数.
 * y=1/(1+exp(ax)) で a (指数部)が可調整パラメータ.
 * @author mori and takeuchi
 * @version 1.0
 */
public class SigmoidFunction implements IFunction {

	/**
	 * y=1/(1+exp(ax)) の 指数部 a 初期値は1.0
	 */
	private double alpha_ = 1.0;

	/**
	 * 引数なしコンストラクタ
	 */
	public SigmoidFunction() {
	}

	/**
	 * コンストラクタで初期化
	 * @param alpha 指数部に代入
	 */
	public SigmoidFunction(double alpha) {
		alpha_ = alpha;
	}

	/**
	 * 入力値 x にある変換を施して出力する関数の本体．
	 * @param value 関数への入力値
	 * @return シグモイド関数で変換した結果
	 */
	public double function(double value) {
		return 1.0 / (1.0 + Math.exp(getAlpha() * value));
	}

	/**
	 * パラメータの設定関数 第1要素をalphaに設定. 要素が数字でない場合または要素数が1でない場合は例外をはく.
	 * @param params パラメータの配列．
	 */
	public void setParameter(Object... params) {
		try {
			if (params.length != 1) {
				// 例外を投げる
				throw new Exception(
						"params length of SigmoidFunction must be 1!");
			}
			// 文字列と数字の場合で場合わけ
			if (params[0] instanceof Number) {
				setAlpha(((Number) (params[0])).doubleValue());
			} else { // 数値系クラス以外の場合には文字列と判断して設定．
				setAlpha(Double.parseDouble(params[0].toString()));
			}
		} catch (Exception e) {
			// 例外を投げる
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "Sigmoid";
	}

	/**
	 * 指数部を返す
	 * @return alpha
	 */
	public final double getAlpha() {
		return alpha_;
	}

	/**
	 * 指数部の設定
	 * @param alpha パラメータ
	 */
	public final void setAlpha(double alpha) {
		alpha_ = alpha;
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		// y=1/(1+exp(-x))
		SigmoidFunction f = new SigmoidFunction(-1.0);
		// 1.0 が表示される
		System.out.println(f.function(Double.POSITIVE_INFINITY));
	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "ALPHA:" + String.valueOf(getAlpha());
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

}
