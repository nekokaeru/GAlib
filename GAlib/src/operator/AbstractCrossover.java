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
 * Created on 2005/07/13
 * 
 */
package operator;

import gabuilder.GAParameters;

import java.util.Arrays;
import java.util.Collections;

import population.Individual;
import population.Population;
import util.MyRandom;

/**
 * 交叉の抽象クラス．サブクラスで crossover を継承する．
 * @author mori
 * @version 1.0
 */
public abstract class AbstractCrossover implements IOperator {
	/**
	 * 交叉率. 初期値は ga.GAParameters の値
	 */
	private double crossoverRate_ = GAParameters.DEFAULT_CROSSOVER_RATE;

	/**
	 * 可変長引数を利用した配列で初期化． パラメータ数は 1 で第一要素が交叉率．
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
				setCrossoverRate(x); // 交叉率設定

			} else { // 文字列の場合
				double x = Double.parseDouble(params[0].toString()); // 数値化
				setCrossoverRate(x); // 交叉率設定
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * 個体群に交叉を適用． ランダムにペアリングするため個体群の個体の順番をバラバラにする．<br>
	 * 乱数を指定しないと再現性が失われる．<br>
	 * 個体の数が奇数のとき，最後の個体は変更されない．
	 * @param pop 個体群
	 * @see #crossover(Individual, Individual, Individual[])
	 */
	public void apply(Population pop) {
		// ランダムにペアリングするため順番をバラバラにする．乱数を指定しないと再現性が失われる．
		Collections.shuffle(pop.getIndivList(), MyRandom.getInstance());
		// 奇数のときは最後の個体は交叉に参加しない．
		for (int i = 0; i < pop.getIndivList().size() / 2; i++) {
			if (MyRandom.getInstance().nextDouble() < getCrossoverRate()) {
				crossover(pop.getIndividualAt(2 * i), pop
						.getIndividualAt(2 * i + 1));
			}
		}
	}

	/**
	 * 各個体への具体的な交叉の適用. 2個体→2個体用だが可変長引数を利用して複数の親にも対応．<br>
	 * p1, p2 について破壊的な演算子．apply から呼ばれる．<br>
	 * @param p1 個体
	 * @param p2 個体
	 * @param individuals 複数の個体
	 * @see #apply(Population)
	 */
	public abstract void crossover(Individual p1, Individual p2,
			Individual... individuals);

	/**
	 * 交叉率を返す．
	 * @return crossoverRate 交叉率
	 */
	public final double getCrossoverRate() {
		return crossoverRate_;
	}

	/**
	 * 交叉率を設定する．
	 * @param crossoverRate 交叉率
	 */
	public final void setCrossoverRate(double crossoverRate) {
		crossoverRate_ = crossoverRate;
	}

	/**
	 * パラメータの情報を返す．例： CROSSOVER_RATE:0.6
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "CROSSOVER_RATE:" + String.valueOf(getCrossoverRate());
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
