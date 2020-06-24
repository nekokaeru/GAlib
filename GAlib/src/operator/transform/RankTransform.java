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

package operator.transform;

import java.util.Arrays;

/**
 * ���l�z��������N�ɕϊ�.
 * @author mori
 * @version 1.0
 */
public class RankTransform implements IArrayTransform {
	/**
	 * �z��̒l�������N�ɕϊ�����.
	 * @param array �I���W�i���̐��l�z��
	 * @return result �ϊ���̔z��
	 */
	public double[] transform(double[] array) {
		if (array == null) {
			return null;
		}
		if (array.length == 0 || array.length == 1) {
			return (double[]) array.clone();
		}
		double[] result = new double[array.length];
		double[] rank = getRankArray(array);
		for (int i = 0; i < rank.length; i++) {
			result[i] = rank[i];
		}
		return result;
	}

	/**
	 * �����N�����߂�.
	 * @param array
	 * @return ����
	 */
	public double[] getRankArray(double[] array) {
		if (array == null || array.length == 0) {
			return null;
		}
		double[] result = new double[array.length];
		if (array.length == 1) {
			result[0] = 1;
			return result;
		}
		for (int i = 0; i < result.length; i++) {
			result[i] = 1; // �n�߂͂��ׂẴ����N��1�ɂ���D
		}
		for (int i = 0; i < array.length; i++) {
			for (int j = i + 1; j < array.length; j++) {
				// �������ق��̃����N�𑝂₷�D
				if (array[i] > array[j]) {
					result[j]++;
				} else if (array[i] < array[j]) {
					result[i]++;
				}
			}
		}
		return result;
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "RankTransform";
	}

	/**
	 * �p�����[�^����Ԃ�. �p�����[�^�͂Ȃ��̂ŋ󕶎����Ԃ�.
	 * @return �p�����[�^���̕�����
	 */
	public String getParameterInfo() {
		return "";
	}

	/**
	 * ������
	 * @return ������\��
	 */
	@Override
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

	/**
	 * @param params �p�����[�^�̔z��D RankTransform �ł͉����p�����[�^�͂Ȃ��D
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
}
