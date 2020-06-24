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

import problem.function.IFunction;

/**
 * 問題クラスのインターフェース.
 * @author mori
 * @version 1.0
 */
public interface IProblem {

	/**
	 * 目的関数値を返す抽象メソッド． 継承先で具体的に実装する．
	 * @param array 遺伝子型を表す Number 配列
	 * @return 目的関数値
	 */
	public double getObjectiveFunctionValue(Number[] array);

	/**
	 * 名前を返す．
	 * @return 名前
	 */
	public abstract String getName();

	/**
	 * @param params 可変長パラメータによるパラメータセット
	 */
	public void setParameter(Object... params);

	/**
	 * パラメータの情報を返す．例： N:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo();

	/**
	 * 関数をリストに追加．
	 * @param f 問題で用いる変換を表す関数．
	 */
	public void addFunction(IFunction f);

	/**
	 * 関数リストを初期化．
	 */
	public void clearFunctionList();

	/**
	 * 目的関数値に関数変換を適用して適応度を返す． この適応度は大きな値の方が優れているようにする．
	 * @param array 遺伝子配列
	 * @return fit 適応度
	 */
	public double getFitness(Number[] array);

}