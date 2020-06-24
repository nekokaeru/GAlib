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

package problem.function;

import java.util.Arrays;

/**
 * ���`�ϊ��֐���\���N���X.
 * y = gradient * x + intercept
 * gradient �͌X��, intercept �͐ؕ�
 * @author mori
 * @version 1.0
 */

public class LinearFunction implements IFunction {
	/**
	 * �X��.
	 */
	private double gradient_;

	/**
	 * �ؕЁD
	 */
	private double intercept_;

	/**
	 * �����Ȃ��R���X�g���N�^. �X�� 3, �ؕ� 1 �ŏ�����
	 */
	public LinearFunction() {
		this(3, 1);
	}

	/**
	 * �X���ƐؕЂŏ�����
	 * @param a
	 * @param b
	 */
	public LinearFunction(double a, double b) {
		gradient_ = a;
		intercept_ = b;
	}

	/**
	 * �ϒ������𗘗p�����z��ŏ������D�P�� setParameter ���ĂԂ����D
	 * @param params �p�����[�^�z��
	 */
	public LinearFunction(Object... params) {
		setParameter(params);
	}

	/**
	 * @param value �֐��ւ̓��͒l
	 * @return ���`�X�P�[�����O�̌���
	 */
	public double function(double value) {
		return getGradient() * value + getIntercept();
	}

	/**
	 * @see problem.function.IFunction#setParameter(java.lang.Object...)
	 * @param params �p�����[�^�̔z��D���v�f���X���C���v�f���ؕЁD���l�ȊO���w�肳��Ă�����C�v�f��2�łȂ��ꍇ�ɂ͗�O�����D
	 */
	public void setParameter(Object... params) {
		try {
			if (params.length != 2) {
				throw new Exception(
						"params length of LinearFunction must be 2!");
			}
			// �X���ݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) {
				setGradient(((Number) (params[0])).doubleValue());
			} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
				setGradient(Double.parseDouble(params[0].toString()));
			}
			// �ؕАݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[1] instanceof Number) {
				setIntercept(((Number) (params[1])).doubleValue());
			} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
				setIntercept(Double.parseDouble(params[1].toString()));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "Linear";
	}

	/**
	 * �X����Ԃ�.
	 * @return �X��
	 */
	public final double getGradient() {
		return gradient_;
	}

	/**
	 * �X����ݒ�.
	 * @param gradient �X��
	 */
	public final void setGradient(double gradient) {
		gradient_ = gradient;
	}

	/**
	 * �ؕЂ�Ԃ�.
	 * @return �ؕ�
	 */
	public final double getIntercept() {
		return intercept_;
	}

	/**
	 * �ؕЂ�ݒ�.
	 * @param intercept �ؕ�
	 */
	public final void setIntercept(double intercept) {
		intercept_ = intercept;
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		// y=3x+1
		LinearFunction f = new LinearFunction(3, 1);
		// 7.0���\�������
		System.out.println(f.function(2));
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "GRADIENT:" + String.valueOf(getGradient()) + ",INTERCEPT:"
				+ String.valueOf(getIntercept());
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
