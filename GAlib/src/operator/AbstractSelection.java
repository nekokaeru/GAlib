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

import java.util.ArrayList;
import java.util.Arrays;

import operator.transform.IArrayTransform;
import population.Individual;
import population.Population;
import fitness.FitnessManager;

/**
 * �I���̒��ۃN���X�D�T�u�N���X�� select ����������D
 * @author mori
 * @version 1.0
 */
public abstract class AbstractSelection implements IOperator {
	/**
	 * �K���x�z��̕ϊ��֐����i�[���郊�X�g
	 */
	protected ArrayList<IArrayTransform> transformList_ = new ArrayList<IArrayTransform>();

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^�Ȃ��̏ꍇ�������D<br>
	 * �p�����[�^������ꍇ�̓T�u�N���X�ŃI�[�o�[���C�h�D
	 * @param params �p�����[�^�̔z��
	 * @see operator.IOperator#apply(Population)
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
	 * �̌Q�ɑI����K�p�D
	 * @param pop �̌Q
	 * @see #select(double[])
	 */
	public void apply(Population pop) {
		// �K���x�̏�����
		double[] allFitness = FitnessManager
				.getFitnessArray(pop.getIndivList());

		// �̌Q�̔z��ɃX�P�[�����O���̕ϊ���K�p����D
		// �K���x�������N�ɂ���̂������D
		for (IArrayTransform tr : transformList_) {
			allFitness = tr.transform(allFitness);
		}
		// �K���x���Ɋ�Â����I���̌��ʂ��̔ԍ��Ŏ���D
		int[] indexArray = select(allFitness);
		ArrayList<Individual> result = new ArrayList<Individual>();
		for (int i = 0; i < indexArray.length; i++) {
			result.add(pop.getIndividualAt(indexArray[i]).clone());
		}
		pop.setIndivList(result);
	}

	/**
	 * ��̓I�ȑI����e����ۃN���X�Ŏ���
	 * @param allFitness �S�̂̓K���x���
	 * @return �I�΂ꂽ�̂� index
	 */
	public abstract int[] select(double[] allFitness);

	/**
	 * �K���x�ϊ��֐��̒ǉ�
	 * @param transform
	 */
	public void addTransform(IArrayTransform transform) {
		transformList_.add(transform);
	}

	/**
	 * �K���x�ϊ��֐����X�g��������
	 */
	public void clearTransformList() {
		transformList_.clear();
	}

	/**
	 * �p�����[�^�̏���Ԃ�. ��: TOURNAMENT_SIZE:3
	 * @see operator.IOperator#getParameterInfo()
	 */
	public String getParameterInfo() {
		if (transformList_.size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (IArrayTransform transform : transformList_) {
			result.append(new String(transform.getName() + "{"
					+ transform.getParameterInfo() + "},"));
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
