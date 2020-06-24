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
 * �V�O���C�h�^�̕ϊ�������֐�.
 * y=1/(1+exp(ax)) �� a (�w����)�������p�����[�^.
 * @author mori and takeuchi
 * @version 1.0
 */
public class SigmoidFunction implements IFunction {

	/**
	 * y=1/(1+exp(ax)) �� �w���� a �����l��1.0
	 */
	private double alpha_ = 1.0;

	/**
	 * �����Ȃ��R���X�g���N�^
	 */
	public SigmoidFunction() {
	}

	/**
	 * �R���X�g���N�^�ŏ�����
	 * @param alpha �w�����ɑ��
	 */
	public SigmoidFunction(double alpha) {
		alpha_ = alpha;
	}

	/**
	 * ���͒l x �ɂ���ϊ����{���ďo�͂���֐��̖{�́D
	 * @param value �֐��ւ̓��͒l
	 * @return �V�O���C�h�֐��ŕϊ���������
	 */
	public double function(double value) {
		return 1.0 / (1.0 + Math.exp(getAlpha() * value));
	}

	/**
	 * �p�����[�^�̐ݒ�֐� ��1�v�f��alpha�ɐݒ�. �v�f�������łȂ��ꍇ�܂��͗v�f����1�łȂ��ꍇ�͗�O���͂�.
	 * @param params �p�����[�^�̔z��D
	 */
	public void setParameter(Object... params) {
		try {
			if (params.length != 1) {
				// ��O�𓊂���
				throw new Exception(
						"params length of SigmoidFunction must be 1!");
			}
			// ������Ɛ����̏ꍇ�ŏꍇ�킯
			if (params[0] instanceof Number) {
				setAlpha(((Number) (params[0])).doubleValue());
			} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
				setAlpha(Double.parseDouble(params[0].toString()));
			}
		} catch (Exception e) {
			// ��O�𓊂���
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "Sigmoid";
	}

	/**
	 * �w������Ԃ�
	 * @return alpha
	 */
	public final double getAlpha() {
		return alpha_;
	}

	/**
	 * �w�����̐ݒ�
	 * @param alpha �p�����[�^
	 */
	public final void setAlpha(double alpha) {
		alpha_ = alpha;
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		// y=1/(1+exp(-x))
		SigmoidFunction f = new SigmoidFunction(-1.0);
		// 1.0 ���\�������
		System.out.println(f.function(Double.POSITIVE_INFINITY));
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "ALPHA:" + String.valueOf(getAlpha());
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

}
