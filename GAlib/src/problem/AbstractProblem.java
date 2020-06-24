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

import java.util.ArrayList;

import problem.function.IFunction;

/**
 * ���̒��ۃN���X�D���ۂ̖��� AbstractProblem ���p�������ۃ��\�b�h����������D
 * @author mori
 * @version 1.0
 */

public abstract class AbstractProblem implements IProblem {
	/**
	 * �K���x��ϊ�����֐���ێ����郊�X�g�D�̏����g��Ȃ��X�P�[�����O�͂���Ŏ�������D
	 */
	protected ArrayList<IFunction> functionList_ = new ArrayList<IFunction>();

	/**
	 * �ړI�֐��l��Ԃ����ۃ��\�b�h�D �p����ŋ�̓I�Ɏ�������D
	 * @param array ��`�q�^��\�� Number �z��
	 * @return �ړI�֐��l
	 * @see problem.IProblem#getObjectiveFunctionValue(Number[])
	 */
	public abstract double getObjectiveFunctionValue(Number[] array);

	/**
	 * ���O��Ԃ��D
	 * @return ���O
	 * @see problem.IProblem#getName()
	 */
	public abstract String getName();

	/**
	 * @param params �ϒ��p�����[�^�ɂ��p�����[�^�Z�b�g
	 * @see problem.IProblem#setParameter(Object[])
	 */
	public abstract void setParameter(Object... params);

	/**
	 * �p�����[�^�̏���Ԃ��D��F N:3
	 * @return �p�����[�^�̏�񕶎���
	 * @see problem.IProblem#getParameterInfo()
	 */
	public abstract String getParameterInfo();

	/**
	 * �֐������X�g�ɒǉ��D
	 * @param f ���ŗp����ϊ���\���֐��D
	 * @see problem.IProblem#addFunction(problem.function.IFunction)
	 */
	public void addFunction(IFunction f) {
		functionList_.add(f);
	}

	/**
	 * �֐����X�g���N���A�D
	 * @see problem.IProblem#clearFunctionList()
	 */
	public void clearFunctionList() {
		functionList_.clear();
	}

	/**
	 * �ړI�֐��l�ɕϊ��֐���K�p���ēK���x��Ԃ��D ���̓K���x�͑傫�Ȓl�̕����D��Ă���悤�ɂ���D
	 * @param array ��`�q�z��
	 * @return fit �K���x
	 * @see problem.IProblem#getFitness(Number[])
	 */
	public double getFitness(Number[] array) {
		double fit = getObjectiveFunctionValue(array);
		for (IFunction f : functionList_) {
			fit = f.function(fit);
		}
		return fit;
	}

	/**
	 * getParameterInfo �ŌĂԂ��߂ɁC�ϊ��֐��̏��𕶎���ŕԂ��D
	 * @return �ϊ��֐��̏��
	 */
	protected String getFunctionInfo() {
		if (functionList_.size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (IFunction function : functionList_) {
			result.append(new String(function.getName() + "{"
					+ function.getParameterInfo() + "},"));
		}
		// �Ō�� , ������
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
