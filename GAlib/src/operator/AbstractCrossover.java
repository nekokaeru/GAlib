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
 * Created on 2005/07/13
 * 
 */
package operator;

import gabuilder.GAParameters;

import java.util.Arrays;
import java.util.Collections;

import population.Individual;
import population.Population;
import util.MyRandom;

/**
 * �����̒��ۃN���X�D�T�u�N���X�� crossover ���p������D
 * @author mori
 * @version 1.0
 */
public abstract class AbstractCrossover implements IOperator {
	/**
	 * ������. �����l�� ga.GAParameters �̒l
	 */
	private double crossoverRate_ = GAParameters.DEFAULT_CROSSOVER_RATE;

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^���� 1 �ő��v�f���������D
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
				setCrossoverRate(x); // �������ݒ�

			} else { // ������̏ꍇ
				double x = Double.parseDouble(params[0].toString()); // ���l��
				setCrossoverRate(x); // �������ݒ�
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * �̌Q�Ɍ�����K�p�D �����_���Ƀy�A�����O���邽�ߌ̌Q�̌̂̏��Ԃ��o���o���ɂ���D<br>
	 * �������w�肵�Ȃ��ƍČ�����������D<br>
	 * �̂̐�����̂Ƃ��C�Ō�̌͕̂ύX����Ȃ��D
	 * @param pop �̌Q
	 * @see #crossover(Individual, Individual, Individual[])
	 */
	public void apply(Population pop) {
		// �����_���Ƀy�A�����O���邽�ߏ��Ԃ��o���o���ɂ���D�������w�肵�Ȃ��ƍČ�����������D
		Collections.shuffle(pop.getIndivList(), MyRandom.getInstance());
		// ��̂Ƃ��͍Ō�̌̂͌����ɎQ�����Ȃ��D
		for (int i = 0; i < pop.getIndivList().size() / 2; i++) {
			if (MyRandom.getInstance().nextDouble() < getCrossoverRate()) {
				crossover(pop.getIndividualAt(2 * i), pop
						.getIndividualAt(2 * i + 1));
			}
		}
	}

	/**
	 * �e�̂ւ̋�̓I�Ȍ����̓K�p. 2�́�2�̗p�����ϒ������𗘗p���ĕ����̐e�ɂ��Ή��D<br>
	 * p1, p2 �ɂ��Ĕj��I�ȉ��Z�q�Dapply ����Ă΂��D<br>
	 * @param p1 ��
	 * @param p2 ��
	 * @param individuals �����̌�
	 * @see #apply(Population)
	 */
	public abstract void crossover(Individual p1, Individual p2,
			Individual... individuals);

	/**
	 * ��������Ԃ��D
	 * @return crossoverRate ������
	 */
	public final double getCrossoverRate() {
		return crossoverRate_;
	}

	/**
	 * ��������ݒ肷��D
	 * @param crossoverRate ������
	 */
	public final void setCrossoverRate(double crossoverRate) {
		crossoverRate_ = crossoverRate;
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F CROSSOVER_RATE:0.6
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "CROSSOVER_RATE:" + String.valueOf(getCrossoverRate());
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}
}
