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

import java.util.Arrays;

import population.Individual;
import population.Population;
import fitness.FitnessManager;

/**
 * �G���[�g�ۑ��헪�N���X�D
 * @author mori
 * @version 1.0
 */

public class Elitism implements IOperator {
	/**
	 * �G���[�g��`�̗L��
	 */
	private boolean isElitism_ = true;

	/**
	 * �ϒ������𗘗p�����z��ŏ������D���̃N���X�̓p�����[�^�Ȃ��D
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
	 * �G���[�g�̂��̌Q�ɖ߂��D�߂����͌̌Q�̖����ɋ����㏑���D
	 * @param pop �̌Q
	 */
	public void apply(Population pop) {
		if (!isElitism()) {
			return;
		}
		Individual elite = FitnessManager.getElite();
		// null �̏ꍇ�̓G���[�g���Z�b�g����Ă��Ȃ��D
		if (elite != null) {
			// �����Ɍ��݂̃G���[�g�������㏑���D
			int index = pop.getPopulationSize() - 1;
			pop.setIndividualAt(index, elite);
		}
	}

	/**
	 * �I�y���[�^����Ԃ��D
	 * @return �I�y���[�^�� "Elitism"
	 */
	public String getName() {
		return "Elitism";
	}

	/**
	 * �G���[�g��`�̗L����Ԃ�.
	 * @return true:�G���[�g��`����, false:�G���[�g��`�Ȃ�
	 */
	public final boolean isElitism() {
		return isElitism_;
	}

	/**
	 * �G���[�g��`�̗L����ݒ�
	 * @param isElitism true:�G���[�g��`����, false:�G���[�g��`�Ȃ�
	 */
	public final void setElitism(boolean isElitism) {
		isElitism_ = isElitism;
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F IS_ELITISM:true
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "IS_ELITISM:" + String.valueOf(isElitism());
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
