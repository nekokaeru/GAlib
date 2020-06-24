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

/*
 * �ˑR�ψق̒��ۃN���X
 * Created on 2005/06/27
 */
package operator;

import gabuilder.GAParameters;

import java.util.Arrays;

import population.Individual;
import population.Population;

/**
 * �ˑR�ψق̒��ۃN���X�D�T�u�N���X�� mutation ����������D 
 * @author mori
 * @version 1.0
 */
public abstract class AbstractMutation implements IOperator {

	/**
	 * �ˑR�ψٗ�. �����l�� ga.GAParameters �̒l.
	 */
	private double mutationRate_ = GAParameters.DEFAULT_MUTATION_RATE;

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^���� 1 �ő��v�f���ˑR�ψٗ��D
	 * @param params �p�����[�^�̔z��
	 */
	public void setParameter(Object... params) {
		try {
			// �p�����[�^�̗v�f�����������Ȃ��Ƃ�
			if (params.length != 1) {
				throw new Exception("params length must be 1!");
			}
			// ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) { // ���l�N���X�̏ꍇ
				double x = ((Number) params[0]).doubleValue(); // ���l��
				setMutationRate(x); // �ˑR�ψٗ��ݒ�
			} else { // ������̏ꍇ
				double x = Double.parseDouble(params[0].toString()); // ���l��
				setMutationRate(x); // �ˑR�ψٗ��ݒ�
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * �̌Q�ɓˑR�ψق�K�p�D
	 * @param pop �̌Q
	 * @see #mutation(Individual)
	 */
	public void apply(Population pop) {
		for (Individual indiv : pop.getIndivList()) {
			mutation(indiv);
		}
	}

	/**
	 * �e�̂ւ̋�̓I�ȓˑR�ψق̓K�p. indiv �ɂ��Ĕj��I�ȉ��Z�q�Dapply �ŌĂ΂��.
	 * @param indiv ��
	 * @see #apply(Population)
	 */
	public abstract void mutation(Individual indiv);

	/**
	 * �ˑR�ψٗ���Ԃ��D
	 * @return mutationRate �ˑR�ψٗ�
	 */
	public final double getMutationRate() {
		return mutationRate_;
	}

	/**
	 * �ˑR�ψٗ���ݒ肷��D
	 * @param mutationRate �ˑR�ψٗ�
	 */
	public final void setMutationRate(double mutationRate) {
		mutationRate_ = mutationRate;
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F MUTATION_RATE:0.1
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "MUTATION_RATE:" + String.valueOf(getMutationRate());
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
