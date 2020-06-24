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

import java.util.Arrays;

/**
 * ���F�̂̈�`�q 1 �̑�����K���x�Ƃ��ĎZ�o������D�� 0001 : �K���x1 0011 : �K���x2 1111 : �K���x4
 * @author mori
 * @version 1.0
 */
public class BitCountProblem extends AbstractProblem {
	/**
	 * �ړI�֐��l���v�Z���ĕԂ��D
	 * @param array ��`�q�^���
	 * @return ��`�q 1 �̑��a
	 */
	public double getObjectiveFunctionValue(Number[] array) {
		double sum = 0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].intValue() == 1) {
				sum++;
			}
			// TODO 01 �ȊO�̎��ɗ�O��f�����邩�H
		}
		return sum;
	}

	/**
	 * ���̖��O��Ԃ��D
	 * @return ���̖��O
	 */
	public String getName() {
		return "BitCount";
	}

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^�Ȃ��D
	 * @param params �p�����[�^�̔z��
	 */
	public void setParameter(Object... params) {
		// �p�����[�^����
		if (params == null) {
			return;
		}
		try {
			// ����0�̔z��͔F�߂�D
			if (params.length != 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return getFunctionInfo();
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		Number[] array = { 1, 0, 1, 0 };
		BitCountProblem bp = new BitCountProblem();
		// 2.0 ���\�������D
		System.out.println(bp.getObjectiveFunctionValue(array));
		System.out.println(bp.getFitness(array));
		Number[] array2 = { 1, 2, 3 };
		// 1.0 ���\�������D
		System.out.println(bp.getFitness(array2));

	}

}
