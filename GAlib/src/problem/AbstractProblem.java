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

import java.util.ArrayList;

import problem.function.IFunction;

/**
 * 問題の抽象クラス．実際の問題は AbstractProblem を継承し抽象メソッドを実装する．
 * @author mori
 * @version 1.0
 */

public abstract class AbstractProblem implements IProblem {
	/**
	 * 適応度を変換する関数を保持するリスト．個体情報を使わないスケーリングはこれで実現する．
	 */
	protected ArrayList<IFunction> functionList_ = new ArrayList<IFunction>();

	/**
	 * 目的関数値を返す抽象メソッド． 継承先で具体的に実装する．
	 * @param array 遺伝子型を表す Number 配列
	 * @return 目的関数値
	 * @see problem.IProblem#getObjectiveFunctionValue(Number[])
	 */
	public abstract double getObjectiveFunctionValue(Number[] array);

	/**
	 * 名前を返す．
	 * @return 名前
	 * @see problem.IProblem#getName()
	 */
	public abstract String getName();

	/**
	 * @param params 可変長パラメータによるパラメータセット
	 * @see problem.IProblem#setParameter(Object[])
	 */
	public abstract void setParameter(Object... params);

	/**
	 * パラメータの情報を返す．例： N:3
	 * @return パラメータの情報文字列
	 * @see problem.IProblem#getParameterInfo()
	 */
	public abstract String getParameterInfo();

	/**
	 * 関数をリストに追加．
	 * @param f 問題で用いる変換を表す関数．
	 * @see problem.IProblem#addFunction(problem.function.IFunction)
	 */
	public void addFunction(IFunction f) {
		functionList_.add(f);
	}

	/**
	 * 関数リストをクリア．
	 * @see problem.IProblem#clearFunctionList()
	 */
	public void clearFunctionList() {
		functionList_.clear();
	}

	/**
	 * 目的関数値に変換関数を適用して適応度を返す． この適応度は大きな値の方が優れているようにする．
	 * @param array 遺伝子配列
	 * @return fit 適応度
	 * @see problem.IProblem#getFitness(Number[])
	 */
	public double getFitness(Number[] array) {
		double fit = getObjectiveFunctionValue(array);
		for (IFunction f : functionList_) {
			fit = f.function(fit);
		}
		return fit;
	}

	/**
	 * getParameterInfo で呼ぶために，変換関数の情報を文字列で返す．
	 * @return 変換関数の情報
	 */
	protected String getFunctionInfo() {
		if (functionList_.size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (IFunction function : functionList_) {
			result.append(new String(function.getName() + "{"
					+ function.getParameterInfo() + "},"));
		}
		// 最後の , を消す
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
