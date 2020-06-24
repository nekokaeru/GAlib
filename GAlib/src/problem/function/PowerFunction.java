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
 * �ׂ���֐���\���N���X.
 * y = x^exponent
 * exponent �͎w����
 * @author mori
 * @version 1.0
 */

public class PowerFunction implements IFunction {
	/**
	 * �w���D
	 */
	private double exponent_ = 2;

	/**
	 * ���͒l x �ɂ���ϊ����{���ďo�͂���֐��̖{�́D
	 * @param value �֐��ւ̓��͒l
	 * @return �ׂ���X�P�[�����O�̌���
	 */
	public double function(double value) {
		return Math.pow(value, exponent_);
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "Power";
	}

	/**
	 * �����Ȃ��R���X�g���N�^ �Ȃɂ����Ȃ�.
	 */
	public PowerFunction() {
	}

	/**
	 * �w�����ŏ�����.
	 * @param exponent �w����
	 */
	public PowerFunction(double exponent) {
		setExponent(exponent);
	}

	/**
	 * �w������Ԃ�.
	 * @return �w����
	 */
	public final double getExponent() {
		return exponent_;
	}

	/**
	 * �w������ݒ�.
	 * @param exponent �w����
	 */
	public final void setExponent(double exponent) {
		exponent_ = exponent;
	}

	/**
	 * @see problem.function.IFunction#setParameter(Object[])
	 * @param params �p�����[�^�̔z��DObject �̔z��ł��邱�Ƃɒ��ӁD���v�f���w���D
	 *        ���l�ȊO���w�肳��Ă�����C�v�f��1�łȂ��ꍇ�ɂ͗�O��f���D
	 */
	public void setParameter(Object... params) {
		try {
			// �p�����[�^�̗v�f����1�łȂ��Ƃ�
			if (params.length != 1) {
				throw new Exception("params length of PowerFunction must be 1!");
			}
			// �w���ݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) { // ���l�N���X�̏ꍇ
				setExponent(((Number) params[0]).doubleValue()); // �w���ݒ�

			} else { // ������̏ꍇ
				double x = Double.parseDouble(params[0].toString()); // �w���ݒ�
				setExponent(x);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		// y = x ^ 3
		PowerFunction f = new PowerFunction();
		f.setParameter("3");
		// 27.0 ���\�������.
		System.out.println(f.function(3));

	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "EXPONENT:" + String.valueOf(getExponent());
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
