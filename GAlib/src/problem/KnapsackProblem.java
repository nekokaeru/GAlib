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

import java.util.Arrays;

/**
 * �i�b�v�T�b�N���̃N���X�D 30�ו��C100�ו������O�ɐݒ肷��D �V�������́Cvalue_, weight_, limitWeight_,
 * optimum ��ݒ肵�Ďg�p����D
 * @author mori
 * @version 1.0
 */
public class KnapsackProblem extends AbstractProblem {
	/**
	 * �T���v���p30�ו��i�b�v�T�b�N���̉��l
	 */
	private final double[] value30_ = { 23, 45, 15, 87, 24, 47, 63, 85, 26, 45,
			37, 57, 88, 53, 87, 77, 17, 38, 78, 21, 25, 19, 61, 89, 7, 5, 39,
			58, 18, 48 };

	/**
	 * �T���v���p30�ו��i�b�v�T�b�N���̏d��
	 */
	private final double[] weight30_ = { 57, 40, 29, 57, 90, 1, 18, 95, 1, 5,
			18, 6, 14, 57, 21, 78, 10, 22, 31, 25, 1, 67, 88, 3, 87, 33, 44,
			61, 27, 10 };

	/**
	 * �T���v���p30�ו��i�b�v�T�b�N���̐����d��
	 */
	private final double limitWeight30_ = 548.0;

	/**
	 * �T���v���p30�ו��i�b�v�T�b�N���̍œK��
	 */
	private double optimum30_ = 1136.0;

	/**
	 * �T���v���p100�ו��i�b�v�T�b�N���̉��l
	 */
	private final double[] value100_ = { 68, 134, 44, 260, 70, 139, 188, 254,
			78, 134, 110, 169, 264, 157, 261, 231, 50, 114, 233, 62, 75, 55,
			183, 266, 21, 13, 116, 172, 52, 142, 259, 41, 136, 101, 227, 71,
			61, 264, 134, 204, 27, 286, 97, 223, 185, 160, 136, 151, 55, 40,
			112, 283, 236, 116, 174, 225, 235, 44, 68, 179, 29, 95, 239, 137,
			196, 274, 287, 180, 202, 292, 15, 276, 127, 280, 284, 146, 197,
			178, 185, 129, 190, 87, 59, 98, 262, 36, 6, 226, 30, 27, 64, 27,
			111, 96, 217, 74, 27, 282, 127, 46 };

	/**
	 * �T���v���p100�ו��i�b�v�T�b�N���̏d��
	 */
	private final double[] weight100_ = { 169, 118, 86, 170, 269, 2, 53, 284,
			3, 13, 53, 18, 41, 171, 63, 232, 29, 64, 93, 74, 2, 200, 263, 9,
			260, 99, 131, 181, 80, 28, 13, 73, 8, 154, 69, 162, 2, 126, 244,
			229, 155, 216, 143, 157, 148, 64, 143, 201, 272, 251, 281, 111,
			173, 246, 70, 19, 201, 211, 31, 266, 252, 39, 165, 245, 293, 177,
			8, 257, 223, 296, 76, 217, 249, 19, 194, 159, 13, 240, 136, 14, 80,
			103, 176, 267, 260, 90, 277, 11, 133, 205, 14, 172, 260, 204, 295,
			225, 106, 161, 86, 80 };

	/**
	 * �T���v���p100�ו��i�b�v�T�b�N���̐����d��
	 */
	private final double limitWeight100_ = 7087.0;

	/**
	 * �T���v���p100�ו��i�b�v�T�b�N���̍œK��
	 */
	private double optimum100_ = 11878.0;

	/**
	 * �ו��̉��l
	 */
	private double[] value_ = null;

	/**
	 * �ו��̏d��
	 */
	private double[] weight_ = null;

	/**
	 * �����d��
	 */
	private double limitWeight_ = Double.NaN;;

	/**
	 * �œK�l
	 */
	private double optimum_ = Double.NaN;

	/**
	 * �f�t�H���g�R���X�g���N�^
	 */
	public KnapsackProblem() {
		// �����Ȃ��̏ꍇ�ɂ�30�ו��i�b�v�T�b�N���
		value_ = value30_;
		weight_ = weight30_;
		limitWeight_ = limitWeight30_;
		optimum_ = optimum30_;
	}

	/**
	 * ��������R���X�g���N�^ num: �ו����D30, 100 �̂����ꂩ�D
	 * @param itemNum �ŉו��������߂Ė���������
	 */
	public KnapsackProblem(Object itemNum) { // 30, 100, 200 �̂����ꂩ���w��D
		setParameter(itemNum);
	}

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^���� 1 �ő��v�f���ו����D
	 * @param params �p�����[�^�̔z��
	 */
	public void setParameter(Object... params) {
		int num = 0;
		try {
			// �p�����[�^�̗v�f�����������Ȃ��Ƃ�
			if (params.length != 1) {
				throw new Exception("params length of PowerFunction must be 1!");
			}
			// ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) { // ���l�N���X�̏ꍇ
				num = ((Number) params[0]).intValue(); // ���l��
			} else { // ������̏ꍇ
				num = Integer.parseInt(params[0].toString()); // ���l��
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
		// num �ɉ����ď�����
		switch (num) {
		case 30:
			// 30�ו��ŏ�����
			value_ = value30_;
			weight_ = weight30_;
			limitWeight_ = limitWeight30_;
			optimum_ = optimum30_;
			break;
		case 100:
			// 100�ו��ŏ�����
			value_ = value100_;
			weight_ = weight100_;
			limitWeight_ = limitWeight100_;
			optimum_ = optimum100_;
			break;
		default:
			// ��O�𔭐��D
			throw new IllegalArgumentException("itemNum " + num
					+ " is not 30 or 100!!!!!!!");
		}
	}

	/**
	 * �ړI�֐��l���v�Z���ĕԂ����\�b�h
	 * @param array ��`�q�^���D��`�q 1 �ŉו���I�ԁD
	 * @return �i�b�v�T�b�N���̖ړI�֐��l
	 * @see problem.AbstractProblem#getObjectiveFunctionValue(Number[])
	 */
	@Override
	public double getObjectiveFunctionValue(Number[] array) {
		double result = 0.0;
		double weight = 0.0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].intValue() == 1) {
				result += value_[i];
				weight += weight_[i];
			}
		}
		// �����d�ʂ𒴂����Ƃ��͕]���� 0 �ɂ���
		if (weight > limitWeight_) {
			result = 0.0;
		}
		return result;
	}

	/**
	 * ���̖��O��Ԃ�
	 * @return ���O��Ԃ�
	 * @see problem.AbstractProblem#getName()
	 */
	@Override
	public String getName() {
		return "Knapsack";
	}

	/**
	 * �����d�ʂ�Ԃ��D
	 * @return �����d��
	 */
	public final double getLimitWeight() {
		return limitWeight_;
	}

	/**
	 * �œK���͊O���I�ɗ^����K�v����D ���̃N���X�ł͍œK����ۏ؂��Ă��Ȃ�
	 * @return �œK��
	 */
	public final double getOptimum() {
		return optimum_;
	}

	/**
	 * �ו��̉��l�̔z���Ԃ��D ���l���O���ŕύX����Ȃ��悤�ɃN���[����Ԃ��D
	 * @return �ו��̉��l�̔z��
	 */
	public final double[] getValue() {
		return value_.clone();
	}

	/**
	 * �ו��̏d���̔z���Ԃ��D ���l���O���ŕύX����Ȃ��悤�ɃN���[����Ԃ��D
	 * @return �ו��̏d���̔z��
	 */
	public final double[] getWeight() {
		return weight_.clone();
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("itemNum:" + getWeight().length);
		if (getFunctionInfo().length() == 0) {
			return sb.toString();
		}
		return sb.toString() + "," + getFunctionInfo();
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		Number[] array = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		KnapsackProblem npsk = new KnapsackProblem();
		// 0.0 ���\�������D
		System.out.println(npsk.getFitness(array));
		// toString �ɂ�� Knapsack{itemNum:30} ���\�������D
		System.out.println(npsk);
	}

}
