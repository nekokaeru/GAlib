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
/**
 * �̌Q�̃f�[�^��͗p���Z�q�N���X.
 * @author mori
 * @version 1.0
 */
public class DataAnalyzer implements IOperator {

	/**
	 * ���O���x�� �����l��0
	 */
	private int logLevel_ = 0;

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^���� 1 �ő��v�f�����O���x���D
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
				int logLevel = ((Number) params[0]).intValue(); // ���l��
				setLogLevel(logLevel); // ���O���x���ݒ�
			} else { // ������̏ꍇ
				int logLevel = Integer.parseInt(params[0].toString()); // ���l��
				setLogLevel(logLevel); // ���O���x���ݒ�
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * �̌Q�ɓK�p�D
	 * @param pop �̌Q
	 */
	public void apply(Population pop) {
		switch (logLevel_) {
		case 0:
			break;
		case 1:
			printMaxAveMin(pop);
			break;
		case 2:
			printAll(pop);
			break;
		default:
		}
	}

	/**
	 * �̌Q���̌̂����ׂĕ\������.
	 * @param pop �̌Q
	 */
	private void printAll(Population pop) {
		System.out.println(pop);
	}

	/**
	 * �̌Q�̓K���x�̍ő�l����ѕ���, �ŏ��l��\������.
	 * @param pop �̌Q
	 */
	private void printMaxAveMin(Population pop) {
		double f, sum = 0, min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
		for (Individual indiv : pop.getIndivList()) {
			// DataAnalyzer �ɂ���ēK���x�]���֐����ς��Ȃ��悤�� isChanged �t���O��ۑ��D
			boolean b = indiv.isChanged();
			f = indiv.fitness();
			// ���̏�Ԃɖ߂��D
			indiv.setChanged(b);
			if (f < min) {
				min = f;
			}
			if (f > max) {
				max = f;
			}
			sum += f;
		}
		sum /= pop.getPopulationSize();
		System.out.println("max:" + max + " ave:" + sum + " min:" + min);
	}

	/**
	 * �̌Q�̍ő�K���x��Ԃ�.
	 * @param pop �̌Q
	 * @return �ő�K���x
	 */
	public double getMaxFitness(Population pop) {
		double f, max = Double.NEGATIVE_INFINITY;
		for (Individual indiv : pop.getIndivList()) {
			// DataAnalyzer �ɂ���ēK���x�]���֐����ς��Ȃ��悤�� isChanged �t���O��ۑ��D
			boolean b = indiv.isChanged();
			f = indiv.fitness();
			// ���̏�Ԃɖ߂��D
			indiv.setChanged(b);
			if (f > max) {
				max = f;
			}
		}
		return max;
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "DataAnalyzer";
	}

	/**
	 * ���O���x����Ԃ�.
	 * @return ���O���x��
	 */
	public int getLogLevel() {
		return logLevel_;
	}

	/**
	 * ���O���x����ݒ�.
	 * @param logLevel ���O���x��
	 */
	public void setLogLevel(int logLevel) {
		logLevel_ = logLevel;
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��FLOG_LEVEL:0
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		return "LOG_LEVEL:" + getLogLevel();
	}

	/**
	 * ������
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

}
