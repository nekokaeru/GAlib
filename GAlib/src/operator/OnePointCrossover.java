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
 * 一点交叉実装
 * Created on 2005/07/13
 * 
 */
package operator;

import population.Individual;
import util.MyRandom;

/**
 * 一点交叉．
 * @author mori
 * @version 1.0
 */
public class OnePointCrossover extends AbstractCrossover {
	/**
	 * 各個体に一点交叉を適用. p1, p2 について破壊的な演算子． スーパークラスの apply で呼ばれる．
	 * @param p1 親1，p2 親2
	 */
	@Override	
	public void crossover(Individual p1, Individual p2,
			Individual... individuals) {
		// 必ず切る．
		int cutPoint = MyRandom.getInstance().nextInt(p1.size() - 1);
		for (int i = 0; i < p1.size(); i++) {
			if (i > cutPoint) {
				Number tmp;
				tmp = p1.getGeneAt(i);
				p1.setGeneAt(i, p2.getGeneAt(i));
				p2.setGeneAt(i, tmp);
			}
		}
	}

	/**
	 * オペレータ名を返す．
	 * @return name オペレータ名"OnePointCrossover"
	 */
	public String getName() {
		return "OnePointCrossover";
	}
}
