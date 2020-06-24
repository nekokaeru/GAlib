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

import problem.function.IFunction;

/**
 * ���N���X�̃C���^�[�t�F�[�X.
 * @author mori
 * @version 1.0
 */
public interface IProblem {

	/**
	 * �ړI�֐��l��Ԃ����ۃ��\�b�h�D �p����ŋ�̓I�Ɏ�������D
	 * @param array ��`�q�^��\�� Number �z��
	 * @return �ړI�֐��l
	 */
	public double getObjectiveFunctionValue(Number[] array);

	/**
	 * ���O��Ԃ��D
	 * @return ���O
	 */
	public abstract String getName();

	/**
	 * @param params �ϒ��p�����[�^�ɂ��p�����[�^�Z�b�g
	 */
	public void setParameter(Object... params);

	/**
	 * �p�����[�^�̏���Ԃ��D��F N:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo();

	/**
	 * �֐������X�g�ɒǉ��D
	 * @param f ���ŗp����ϊ���\���֐��D
	 */
	public void addFunction(IFunction f);

	/**
	 * �֐����X�g���������D
	 */
	public void clearFunctionList();

	/**
	 * �ړI�֐��l�Ɋ֐��ϊ���K�p���ēK���x��Ԃ��D ���̓K���x�͑傫�Ȓl�̕����D��Ă���悤�ɂ���D
	 * @param array ��`�q�z��
	 * @return fit �K���x
	 */
	public double getFitness(Number[] array);

}