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

import gabuilder.GAParameters;

import java.util.ArrayList;
import java.util.Arrays;

import util.LocalTestException;
import util.MyRandom;
import util.NStatistics;

/**
 * �g�[�i�����g�I��.
 * @author mori
 * @version 1.0
 */
public class TournamentSelection extends AbstractSelection {
	/**
	 * �g�[�i�����g�T�C�Y
	 */
	private int tournamentSize_ = GAParameters.DEFAULT_TOURNAMENT_SIZE;

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^���� 1 �ő��v�f���g�[�i�����g�T�C�Y�D
	 * @param params �p�����[�^�̔z��
	 */
	@Override
	public void setParameter(Object... params) {
		try {
			// �p�����[�^�̗v�f�����������Ȃ��Ƃ�
			if (params.length != 1) {
				throw new Exception(
						"params length of TournamentSelection must be 1!");
			}
			// ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) { // ���l�N���X�̏ꍇ
				int x = ((Number) params[0]).intValue(); // ���l��
				setTournamentSize(x); // �g�[�i�����g�T�C�Y�̐ݒ�

			} else { // ������̏ꍇ
				int x = Integer.parseInt(params[0].toString()); // ���l��
				setTournamentSize(x); // �g�[�i�����g�T�C�Y�̐ݒ�
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * �I��
	 * @param allFitness �S�̂̓K���x���
	 * @return �I�΂ꂽ�̂� index
	 */
	@Override
	public int[] select(double[] allFitness) {
		int[] indexArray = new int[allFitness.length];
		int[] numArray;
		// �K���x���������ꍇ�̃^�C�u���[�N�p�D
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		for (int i = 0; i < allFitness.length; i++) {
			// �����_���Ƀg�[�i�����g�T�C�Y���̐�����I�ԁD
			numArray = makeNumbers(getTournamentSize(), allFitness.length);
			// numArray �̒����� allFitness �̒l���ő�� index �𒲂ׂ�D
			double max = Double.NEGATIVE_INFINITY;
			buffer.clear();
			for (int j = 0; j < numArray.length; j++) {
				if (allFitness[numArray[j]] > max) {
					buffer.clear();
					buffer.add(numArray[j]);
					max = allFitness[numArray[j]];
				} else if (allFitness[numArray[j]] == max) {
					// �������ꍇ�ɂ̓^�C�u���[�N�p�ɕێ����ďI���D
					buffer.add(numArray[j]);
				}
			}
			// �^�C�u���[�N�D
			int selectNum = MyRandom.getInstance().nextInt(buffer.size());
			indexArray[i] = buffer.get(selectNum);
		}
		return indexArray;
	}

	/**
	 * 0�`maxSize-1 �̒�����d�����Ȃ� size �̐�����I��.
	 * @param size �I�Ԑ����̌�
	 * @param maxSize �����̑S��
	 * @return �I�񂾐����̑g
	 */
	private int[] makeNumbers(int size, int maxSize) {
		// �I�Ԍ����S�����傫���������O
		if (size > maxSize) {
			throw new IllegalArgumentException(size + ">" + maxSize);
		}
		/**
		 * �I�Ԑ����S�̂̔����𒴂����ꍇ�ɂ́C�I�΂�Ȃ�����I�񂾕��������D <br>
		 * ���ɁCsize �� maxSize ���߂��ꍇ�ɂ͌��ʂ��傫���D<br>
		 * isNormal �� true: selectSize == size �𕁒ʂɑI�ԁD isNormal �� false:
		 * �I�΂�Ȃ����̌̂� selectSize �I��� maxSize-selectSize ��Ԃ��D
		 */
		boolean isNormal = true;
		// �I�΂��̐��D
		int selectSize = size;
		if (2 * size > maxSize) {
			isNormal = false;
			// ���̏ꍇ�� selectSize �͑I�΂�Ȃ��ق��̌̐��ł���D
			selectSize = maxSize - size;
		}
		int[] numArray = new int[selectSize];
		// ��v���Ȃ��ԍ��ŏ�����
		Arrays.fill(numArray, Integer.MIN_VALUE);
		for (int i = 0; i < selectSize; i++) {
			// ���ɑI�΂ꂽ�ԍ����H
			boolean isNewValue = false;
			int num = -1;
			while (!isNewValue) {
				MyRandom.getInstance().nextInt(maxSize);
				num = MyRandom.getInstance().nextInt(maxSize);
				isNewValue = true;
				for (int j = 0; j < numArray.length; j++) {
					if (num == numArray[j]) {
						// �ЂƂł����ɑI�΂ꂽ�����ƈ�v����΂�蒼���D
						isNewValue = false;
						break;
					}
				}
			}
			numArray[i] = num;
		}
		if (isNormal) {
			// ���ʂɑI�΂ꂽ������Ԃ��D�قƂ�ǂ̏ꍇ�͂����炪���s�����D
			return numArray;
		} else {
			/**
			 * �I�΂�Ȃ��ԍ���T�����ꍇ�ɂ́C�c��������Ԃ��D�����]���ȏ������K�v�����C<br>
			 * GA �ł̓g�[�i�����g�T�C�Y�͈ꌅ�C�̐��� 100 �ȏ�̏ꍇ�� �����̂ŁC<br>
			 * ���������s����邱�Ƃ͂قƂ�ǂȂ��͂�.
			 */
			boolean[] isSelected = new boolean[maxSize];
			Arrays.fill(isSelected, true);
			// size == maxSize �̂Ƃ��� numArray.length == 0 �Ȃ̂Ŏ��̃��[�v�ɓ���Ȃ��D
			for (int i = 0; i < numArray.length; i++) {
				// �I�΂ꂽ�ԍ��� index �� false �ɂ���D
				isSelected[numArray[i]] = false;
			}
			int[] result = new int[size];
			int index = 0; // result �p��index
			for (int i = 0; i < maxSize; i++) {
				if (isSelected[i]) { // �������C�c�������ł����result �ɒǉ��D
					result[index++] = i;
				}
			}
			// �c��̕���Ԃ��D
			return result;
		}
	}

	/**
	 * ���O��Ԃ�.
	 * @return name ���O
	 */
	public String getName() {
		return "TournamentSelection";
	}

	/**
	 * �g�[�i�����g�T�C�Y��Ԃ�.
	 * @return �g�[�i�����g�T�C�Y
	 */
	public final int getTournamentSize() {
		return tournamentSize_;
	}

	/**
	 * �g�[�i�����g�T�C�Y�̐ݒ�.
	 * @param size �g�[�i�����g�T�C�Y
	 */
	public final void setTournamentSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size: " + size + "<" + 0);
		}
		tournamentSize_ = size;
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		TournamentSelection ts = new TournamentSelection();
		int i = 0;
		try {
			for (i = 0; i < 10000; i++) {
				if (i % 200 == 0)
					System.out.println(i);
				ts.localTest();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.err.println(i);
		}
	}

	/**
	 * private �� protected �̃e�X�g�֐��D
	 * @throws LocalTestException
	 */
	public void localTest() throws LocalTestException {
		int size = 20;
		int[] result = null;
		// �S���I�ԏꍇ�ɂ́C����������(0,1,2...)�ɓ���D
		result = makeNumbers(size, size);
		for (int i = 0; i < result.length; i++) {
			if (result[i] != i) {
				throw new LocalTestException();
			}
		}

		for (int i = 0; i < size; i++) {
			result = makeNumbers(i, size);
			if (result.length != i) {
				throw new LocalTestException(i + "!=" + result.length);
			}
		}
		int maxi = 5000;
		// 3 ��I�ԏꍇ�̓��v�I�e�X�g
		int[] sum = new int[size];
		Arrays.fill(sum, 0);
		int selectNum = 3;
		for (int i = 0; i < maxi; i++) {
			result = makeNumbers(selectNum, size);
			for (int j = 0; j < result.length; j++) {
				sum[result[j]]++;
			}
		}
		for (int i = 0; i < sum.length; i++) {
			if (NStatistics.binomialTest(maxi, sum[i], (double) selectNum
					/ size, 5) == false) {
				throw new LocalTestException(maxi * 0.1 + " " + sum[i]
						+ " seed:" + MyRandom.getSeed());
			}
		}
		// 17 ��I�ԏꍇ�̓��v�I�e�X�g
		Arrays.fill(sum, 0);
		selectNum = 17;
		for (int i = 0; i < maxi; i++) {
			result = makeNumbers(selectNum, size);
			for (int j = 0; j < result.length; j++) {
				sum[result[j]]++;
			}
		}
		for (int i = 0; i < sum.length; i++) {
			if (NStatistics.binomialTest(maxi, sum[i], (double) selectNum
					/ size, 5) == false) {
				System.err.println(i + " " + sum[i]);
				throw new LocalTestException(maxi * 0.9 + " " + sum[i]
						+ " seed:" + MyRandom.getSeed());
			}
		}

	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		String info = "TOURNAMENT_SIZE:" + String.valueOf(getTournamentSize());
		String s = super.getParameterInfo();
		if (s.equals("")) {
			return info;
		} else {
			return info + "," + s;
		}
	}
}
