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
 * ���[���b�g�I��.
 * @author mori
 * @version 1.0
 */
public class RouletteSelection extends AbstractSelection {

	/**
	 * �I��
	 * @param allFitness �S�̂̓K���x���
	 * @return �I�΂ꂽ�̂� index
	 */
	@Override
	public int[] select(double[] allFitness) {
		double sum = 0;
		int[] indexArray = new int[allFitness.length];
		for (int i = 0; i < allFitness.length; i++) {
			sum += allFitness[i];
		}
		for (int i = 0; i < allFitness.length; i++) {
			// ���[���b�g�̐j����.
			double needle = MyRandom.getInstance().nextDouble() * sum;
			int index = 0;
			// �j���w���Ă���ꏊ�𒲂ׂ�D
			while (needle - allFitness[index] > 0) {
				needle -= allFitness[index];
				index++;
			}
			// �I�΂ꂽ�z��̓Y������ۑ�.
			indexArray[i] = index;
		}
		return indexArray;
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "RouletteSelection";
	}

	/**
	 * �K���x�ϊ��֐���set
	 * @param transform �K���x�ϊ��֐�
	 */
	public void addTransform(IArrayTransform transform) {
		transformList_.add(transform);
	}

	/**
	 * �p�����[�^����Ԃ�.
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return super.getParameterInfo();
	}
}
