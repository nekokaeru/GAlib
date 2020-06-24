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

import java.util.Arrays;

import population.Individual;
import population.Population;
import fitness.FitnessManager;

/**
 * エリート保存戦略クラス．
 * @author mori
 * @version 1.0
 */

public class Elitism implements IOperator {
	/**
	 * エリート主義の有無
	 */
	private boolean isElitism_ = true;

	/**
	 * 可変長引数を利用した配列で初期化．このクラスはパラメータなし．
	 * @param params パラメータの配列
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
	 * エリート個体を個体群に戻す．戻し方は個体群の末尾に強制上書き．
	 * @param pop 個体群
	 */
	public void apply(Population pop) {
		if (!isElitism()) {
			return;
		}
		Individual elite = FitnessManager.getElite();
		// null の場合はエリートがセットされていない．
		if (elite != null) {
			// 末尾に現在のエリートを強制上書き．
			int index = pop.getPopulationSize() - 1;
			pop.setIndividualAt(index, elite);
		}
	}

	/**
	 * オペレータ名を返す．
	 * @return オペレータ名 "Elitism"
	 */
	public String getName() {
		return "Elitism";
	}

	/**
	 * エリート主義の有無を返す.
	 * @return true:エリート主義あり, false:エリート主義なし
	 */
	public final boolean isElitism() {
		return isElitism_;
	}

	/**
	 * エリート主義の有無を設定
	 * @param isElitism true:エリート主義あり, false:エリート主義なし
	 */
	public final void setElitism(boolean isElitism) {
		isElitism_ = isElitism;
	}

	/**
	 * パラメータの情報を返す．例： IS_ELITISM:true
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "IS_ELITISM:" + String.valueOf(isElitism());
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
