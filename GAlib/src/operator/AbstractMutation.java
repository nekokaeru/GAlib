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

/*
 * 突然変異の抽象クラス
 * Created on 2005/06/27
 */
package operator;

import gabuilder.GAParameters;

import java.util.Arrays;

import population.Individual;
import population.Population;

/**
 * 突然変異の抽象クラス．サブクラスで mutation を実装する． 
 * @author mori
 * @version 1.0
 */
public abstract class AbstractMutation implements IOperator {

	/**
	 * 突然変異率. 初期値は ga.GAParameters の値.
	 */
	private double mutationRate_ = GAParameters.DEFAULT_MUTATION_RATE;

	/**
	 * 可変長引数を利用した配列で初期化． パラメータ数は 1 で第一要素が突然変異率．
	 * @param params パラメータの配列
	 */
	public void setParameter(Object... params) {
		try {
			// パラメータの要素数が正しくないとき
			if (params.length != 1) {
				throw new Exception("params length must be 1!");
			}
			// 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) { // 数値クラスの場合
				double x = ((Number) params[0]).doubleValue(); // 数値化
				setMutationRate(x); // 突然変異率設定
			} else { // 文字列の場合
				double x = Double.parseDouble(params[0].toString()); // 数値化
				setMutationRate(x); // 突然変異率設定
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * 個体群に突然変異を適用．
	 * @param pop 個体群
	 * @see #mutation(Individual)
	 */
	public void apply(Population pop) {
		for (Individual indiv : pop.getIndivList()) {
			mutation(indiv);
		}
	}

	/**
	 * 各個体への具体的な突然変異の適用. indiv について破壊的な演算子．apply で呼ばれる.
	 * @param indiv 個体
	 * @see #apply(Population)
	 */
	public abstract void mutation(Individual indiv);

	/**
	 * 突然変異率を返す．
	 * @return mutationRate 突然変異率
	 */
	public final double getMutationRate() {
		return mutationRate_;
	}

	/**
	 * 突然変異率を設定する．
	 * @param mutationRate 突然変異率
	 */
	public final void setMutationRate(double mutationRate) {
		mutationRate_ = mutationRate;
	}

	/**
	 * パラメータの情報を返す．例： MUTATION_RATE:0.1
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "MUTATION_RATE:" + String.valueOf(getMutationRate());
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
