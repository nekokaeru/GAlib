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
 * �������t�]����֐���\���N���X.
 * y = -x
 * @author mori
 * @version 1.0
 */

public class NegativeFunction implements IFunction {

	/**
	 * ���͒l value �ɂ���ϊ����{���ďo�͂���֐��̖{�́D
	 * @param value ����
	 * @return ���͂̕������t�]���ĕԂ�
	 */
	public double function(double value) {
		return -value;
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "Negative";
	}

	/**
	 * @see problem.function.IFunction#setParameter(java.lang.Object...)
	 * @param params �p�����[�^�̔z��D NegativeFunction �ł͉����p�����[�^�͂Ȃ��D
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
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		NegativeFunction f = new NegativeFunction();
		// String[] p = {"2","3a","4"};
		Object[] p = new Object[0];
		f.setParameter(p);
		// -3.0 ���\�������.
		System.out.println(f.function(3));

	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "";
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
