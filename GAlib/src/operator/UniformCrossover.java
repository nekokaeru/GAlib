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
 * 一様交叉実装
 * Created on 2005/07/13
 * 
 */
package operator;

import java.util.Arrays;

import population.Individual;
import util.LocalTestException;
import util.MyRandom;
import util.NStatistics;

/**
 * 一様交叉．
 * @author mori
 * @version 1.0
 */
public class UniformCrossover extends AbstractCrossover {
	/**
	 * オペレータ名を返す．
	 * @return name オペレータ名"UniformCrossover"
	 */
	public String getName() {
		return "UniformCrossover";
	}

	/**
	 * 各個体に一様交叉を適用. p1, p2 について破壊的な演算子． スーパークラスの apply で呼ばれる．<br>
	 * マスクはランダムに決められ，必ず交叉する． すべて 1 もしくはすべて 0 を防ぐために強制的に<br>
	 * 0 と １ をひとつ入れる． マスクが 1 のとき，遺伝子を交換する．
	 * @param p1 親1，p2 親2
	 */
	@Override
	public void crossover(Individual p1, Individual p2,
			Individual... individuals) {
		// マスク用
		int size = p1.size();
		byte[] mask = makeMask(size);
		for (int i = 0; i < p1.size(); i++) {
			// マスクが1であれば遺伝子を交換する．
			if (mask[i] == 1) {
				Number tmp = p1.getGeneAt(i);
				p1.setGeneAt(i, p2.getGeneAt(i));
				p2.setGeneAt(i, tmp);
			}
		}
	}

	/**
	 * マスクを返す．
	 * @param size マスクの長さ
	 * @return マスク
	 */
	private byte[] makeMask(int size) {
		byte[] mask = new byte[size];
		for (int i = 0; i < mask.length; i++) {
			mask[i] = (byte) MyRandom.getInstance().nextInt(2);
		}
		// all 0 や all 1 を除くため，強制的に 0 と １ をひとつ入れる．
		int index0 = MyRandom.getInstance().nextInt(size);
		int index1 = index0 + 1 + MyRandom.getInstance().nextInt(size - 1);
		mask[index0] = 0;
		mask[index1 % size] = 1;
		return mask;
	}

	/**
	 * UniformCrossover の localTest() を実行する．
	 * @param args
	 */
	public static void main(String[] args) {
		UniformCrossover x = new UniformCrossover();
		try {
			x.localTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * マスクの生成をテストする．<br>
	 * すべて 1 もしくはすべて 0 になっていないか， 1 と 0 が確率的に均等になっているかをテストする．
	 * @throws LocalTestException
	 */

	public void localTest() throws LocalTestException {
		// makeMask
		int size = 3, testNum = 2500;
		byte[] all0 = new byte[size], all1 = new byte[size];
		Arrays.fill(all0, (byte) 0);
		Arrays.fill(all1, (byte) 1);
		int[] sum = new int[size];
		byte[] mask;
		for (int i = 0; i < testNum; i++) {
			mask = makeMask(size);
			// 配列の内容による比較は Arrays.equals を用いる．
			if (Arrays.equals(mask, all0) || Arrays.equals(mask, all1)) {
				throw new LocalTestException();
			}
			for (int j = 0; j < mask.length; j++) {
				sum[j] += mask[j];
			}
		}
		for (int i = 0; i < sum.length; i++) {
			// 結果が4σに入っていればよい．
			if (!NStatistics.binomialTest(testNum, sum[i], 0.5, 4)) {
				throw new LocalTestException();
			}
		}
	}
}
