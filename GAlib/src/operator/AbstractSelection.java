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

package operator;

import java.util.ArrayList;
import java.util.Arrays;

import operator.transform.IArrayTransform;
import population.Individual;
import population.Population;
import fitness.FitnessManager;

/**
 * 選択の抽象クラス．サブクラスで select を実装する．
 * @author mori
 * @version 1.0
 */
public abstract class AbstractSelection implements IOperator {
	/**
	 * 適応度配列の変換関数を格納するリスト
	 */
	protected ArrayList<IArrayTransform> transformList_ = new ArrayList<IArrayTransform>();

	/**
	 * 可変長引数を利用した配列で初期化． パラメータなしの場合を実装．<br>
	 * パラメータがある場合はサブクラスでオーバーライド．
	 * @param params パラメータの配列
	 * @see operator.IOperator#apply(Population)
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
	 * 個体群に選択を適用．
	 * @param pop 個体群
	 * @see #select(double[])
	 */
	public void apply(Population pop) {
		// 適応度の初期化
		double[] allFitness = FitnessManager
				.getFitnessArray(pop.getIndivList());

		// 個体群の配列にスケーリング等の変換を適用する．
		// 適応度をランクにするのもここ．
		for (IArrayTransform tr : transformList_) {
			allFitness = tr.transform(allFitness);
		}
		// 適応度情報に基づいた選択の結果を個体番号で受取る．
		int[] indexArray = select(allFitness);
		ArrayList<Individual> result = new ArrayList<Individual>();
		for (int i = 0; i < indexArray.length; i++) {
			result.add(pop.getIndividualAt(indexArray[i]).clone());
		}
		pop.setIndivList(result);
	}

	/**
	 * 具体的な選択内容を具象クラスで実装
	 * @param allFitness 全個体の適応度情報
	 * @return 選ばれた個体の index
	 */
	public abstract int[] select(double[] allFitness);

	/**
	 * 適応度変換関数の追加
	 * @param transform
	 */
	public void addTransform(IArrayTransform transform) {
		transformList_.add(transform);
	}

	/**
	 * 適応度変換関数リストを初期化
	 */
	public void clearTransformList() {
		transformList_.clear();
	}

	/**
	 * パラメータの情報を返す. 例: TOURNAMENT_SIZE:3
	 * @see operator.IOperator#getParameterInfo()
	 */
	public String getParameterInfo() {
		if (transformList_.size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (IArrayTransform transform : transformList_) {
			result.append(new String(transform.getName() + "{"
					+ transform.getParameterInfo() + "},"));
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
