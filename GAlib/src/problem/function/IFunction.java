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

/**
 * 適応度変換時の関数を表すインターフェース.
 * @author mori
 * @version 1.0
 */
public interface IFunction {
	/**
	 * 入力値 x にある変換を施して出力する関数の本体．
	 * @param x 入力値
	 * @return 出力値
	 */
	public double function(double x);

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName();

	/**
	 * パラメータの設定関数．
	 * @param params パラメータ列．可変長パラメータを利用．
	 */
	public void setParameter(Object... params);

	/**
	 * パラメータの情報を返す．例： EXPONENT:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo();
}
