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

import operator.transform.IArrayTransform;
import util.MyRandom;

/**
 * ルーレット選択.
 * @author mori
 * @version 1.0
 */
public class RouletteSelection extends AbstractSelection {

	/**
	 * 選択
	 * @param allFitness 全個体の適応度情報
	 * @return 選ばれた個体の index
	 */
	@Override
	public int[] select(double[] allFitness) {
		double sum = 0;
		int[] indexArray = new int[allFitness.length];
		for (int i = 0; i < allFitness.length; i++) {
			sum += allFitness[i];
		}
		for (int i = 0; i < allFitness.length; i++) {
			// ルーレットの針を回す.
			double needle = MyRandom.getInstance().nextDouble() * sum;
			int index = 0;
			// 針が指している場所を調べる．
			while (needle - allFitness[index] > 0) {
				needle -= allFitness[index];
				index++;
			}
			// 選ばれた配列の添え字を保存.
			indexArray[i] = index;
		}
		return indexArray;
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "RouletteSelection";
	}

	/**
	 * 適応度変換関数のset
	 * @param transform 適応度変換関数
	 */
	public void addTransform(IArrayTransform transform) {
		transformList_.add(transform);
	}

	/**
	 * パラメータ情報を返す.
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return super.getParameterInfo();
	}
}
