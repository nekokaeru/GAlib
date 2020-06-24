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
 * ��ʓI�Ȑ��`�X�P�[�����O�ϊ�.
 * @author mori
 * @version 1.0
 */
public class LinearScalingTransform implements IArrayTransform {
	/**
	 * �ϊ���̍ő�l
	 */
	private double topValue_;

	/**
	 * �ϊ���̍ŏ��l
	 */
	private double bottomValue_;

	/**
	 * �f�t�H���g�R���X�g���N�^ �ϊ���̍ő�l3����эŏ��l1�ŏ���������.
	 */
	public LinearScalingTransform() {
		this(3, 1);
	}

	/**
	 * �ϊ���̍ő�l����эŏ��l�ŏ���������.
	 * @param top �ϊ���̍ő�l
	 * @param bottom �ϊ���̍ŏ��l
	 */
	public LinearScalingTransform(double top, double bottom) {
		topValue_ = top;
		bottomValue_ = bottom;
	}

	/**
	 * �z��̒l����`�X�P�[�����O����.
	 * @param array �I���W�i���̐��l�z��
	 * @return result �ϊ���̔z��
	 */
	public double[] transform(double[] array) {
		// array ���ő� topValue, �ŏ� bottomValue �̐��`�X�P�[�����O��K�p�D
		if (array == null) {
			return null;
		}
		if (array.length == 0 || array.length == 1) {
			return (double[]) array.clone();
		}
		double[] result = new double[array.length];
		double maxFitness = -Double.MAX_VALUE, minFitness = Double.MAX_VALUE;
		// �ő�l�ƍŏ��l�𒲂ׂ�
		for (int i = 0; i < array.length; i++) {
			if (array[i] > maxFitness) {
				maxFitness = array[i];
			}
			if (array[i] < minFitness) {
				minFitness = array[i];
			}
		}
		if (maxFitness == minFitness) {
			return (double[]) array.clone();
		}
		for (int i = 0; i < array.length; i++) {
			result[i] = (topValue_ - bottomValue_) / (maxFitness - minFitness)
					* array[i]
					+ (bottomValue_ * maxFitness - topValue_ * minFitness)
					/ (maxFitness - minFitness);
		}
		return result;
	}

	/**
	 * �ϊ���̍ŏ��l��Ԃ�.
	 * @return �ϊ���̍ŏ��l
	 */
	public final double getBottomValue() {
		return bottomValue_;
	}

	/**
	 * �ϊ���̍ŏ��l��ݒ�.
	 * @param bottomValue �ϊ���̍ŏ��l
	 */
	public final void setBottomValue(double bottomValue) {
		bottomValue_ = bottomValue;
	}

	/**
	 * �ϊ���̍ő�l��Ԃ�.
	 * @return �ϊ���̍ő�l
	 */
	public final double getTopValue() {
		return topValue_;
	}

	/**
	 * �ϊ���̍ő�l��ݒ�.
	 * @param topValue �ϊ���̍ő�l
	 */
	public final void setTopValue(double topValue) {
		topValue_ = topValue;
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "LinearScalingTransform";
	}

	/**
	 * �p�����[�^�̏���Ԃ��D ��FTOP_VALUE:300,BOTTOM_VALUE:10
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "TOP_VALUE:" + getTopValue() + ", BOTTOM_VALUE:"
				+ getBottomValue();
	}

	/**
	 * @see problem.function.IFunction#setParameter(java.lang.Object...)
	 * @param params �p�����[�^�̔z��D���v�f���ϊ���̍ő�l�C���v�f���ϊ���̍ŏ��l�D
	 *        ���l�ȊO���w�肳��Ă�����C�v�f��2�łȂ��ꍇ�ɂ͗�O�����D
	 */
	public void setParameter(Object... params) {
		try {
			if (params.length != 2) {
				throw new Exception(
						"params length must be 2!");
			}
			// �ϊ���̍ő�l��ݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) {
				setTopValue(((Number) (params[0])).doubleValue());
			} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
				setTopValue(Double.parseDouble(params[0].toString()));
			}
			// �ؕАݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[1] instanceof Number) {
				setBottomValue(((Number) (params[1])).doubleValue());
			} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
				setBottomValue(Double.parseDouble(params[1].toString()));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	public static void main(String[] args) {
		LinearScalingTransform ls = new LinearScalingTransform();
		System.out.println(ls.getParameterInfo());
	}
}
