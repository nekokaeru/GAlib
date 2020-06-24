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
 * 突然変異クラス
 * Created on 2005/06/27
 */
package operator;

import population.Individual;
import util.MyRandom;

/**
 * 遺伝子座ごとに突然変異を適応するクラス．<br>
 * {0,1} バイナリ列専用突然変異演算子．
 * @author mori
 * @version 1.0
 */
public class BitMutation extends AbstractMutation {

	/**
	 * 各個体の遺伝子座ごとに突然変異を適用. indiv について破壊的な演算子． スーパークラスの apply で呼ばれる．
	 * @param indiv 個体
	 */
	@Override
	public void mutation(Individual indiv) {
		for (int i = 0; i < indiv.size(); i++) {
			// 各ビットに突然変異率で突然変異を適用
			if (MyRandom.getInstance().nextDouble() < getMutationRate()) {
				if ((Integer) indiv.getGeneAt(i) == 0) {
					indiv.setGeneAt(i, 1);
				} else if ((Integer) indiv.getGeneAt(i) == 1) {
					indiv.setGeneAt(i, 0);
				} else { // 対立遺伝子 0, 1 のみに対応
					throw new IllegalArgumentException(getName()
							+ " Illegal gene type:" + indiv.getGeneAt(i));
				}
			}
		}
	}

	/**
	 * オペレータ名を返す
	 * @return name オペレータ名"BitMutation"
	 */
	public String getName() {
		return "BitMutation";
	}
}
