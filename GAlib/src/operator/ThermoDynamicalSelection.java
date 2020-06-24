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

import fitness.FitnessManager;
import gabuilder.GAParameters;

import java.util.ArrayList;
import java.util.Arrays;

import population.Individual;
import population.Population;
import util.LocalTestException;

/**
 * �M�͊w�I�I��.
 * @author mori
 * @version 1.0
 */

public class ThermoDynamicalSelection extends AbstractSelection {
	/**
	 * ���x
	 */
	private double temperature_ = GAParameters.DEFAULT_TEMPERATURE;

	/**
	 * �I��p�ɑ�����{�����ꎞ�I�̌Q�D
	 */
	private ArrayList<Individual> currentPop_;

	/**
	 * ������̌̐��D �����Ă���̐��̔���
	 */
	private int nextPopSize_ = -1;

	/**
	 * �v�Z�r���̊e��`�q�̐���ۑ��D ����̓o�C�i���̂݁D
	 */
	private int[][] numOfGene_;

	/**
	 * ���R���� Log ��ۑ�����D �v�Z�ʌy���̂��߁D
	 */
	private double[] logValue_ = null;

	/**
	 * ���k���s�����̃t���O
	 */
	private boolean isPackIndivList_ = false;

	/**
	 * �f�t�H���g�R���X�g���N�^
	 */
	public ThermoDynamicalSelection() {
		super();
	}

	/**
	 * �̌Q�ɑI����K�p����.
	 * @param pop �̌Q
	 */
	@Override
	public void apply(Population pop) {
		Individual elite = FitnessManager.getElite();
		if (elite != null) {
			pop.getIndivList().add(elite); // �G���[�g��������D
		}
		// �d������̂������Č̌Q�����k����D���x���Ⴏ��Ό��ʑ�D
		if (isPackIndivList_) {
			packIndivList(pop.getIndivList());
		}
		currentPop_ = pop.getIndivList(); // select �Ŏg����悤�ɎQ�Ƃ�ۑ��D
		// �����ŁCselect ���Ă΂��DcurrentPop_ ��ێ����Ă���̂ŁC�̂̈�`�q�^���킩��D
		// �G���g���s�[���v�Z����Ƃ��ɕK�v�D
		super.apply(pop);
	}

	/**
	 * TDGA �ł͒ʏ�� SGA ��2�{�̌̐��𔼕��ɂ���D
	 * @param allFitness �S�̂̓K���x���
	 * @return �I�΂ꂽ�̂� index
	 */
	@Override
	public int[] select(double[] allFitness) {
		// �I�΂ꂽ�̂� index ��Ԃ��D
		int[] indexArray = new int[getNextPopSize()]; // ������͓����Ă����̂̔���
		// ��`�q�̐��̏�����
		Arrays.fill(numOfGene_[0], 0);
		Arrays.fill(numOfGene_[1], 0);
		// ���R�G�l���M�[�ŏ��̌̂� index
		int minIndex = -1;
		double F, Ebar = 0, Hj = 0; // F = Ebar - Hj*T
		// Fmin: �ŏ��̎��R�G�l���M�[�l�ۑ��p Esum: �G�l���M�[�̑��a�ۑ��p
		double Fmin, Esum = 0;
		for (int i = 0; i < getNextPopSize(); i++) { // ������̌̐����I��
			Fmin = Double.POSITIVE_INFINITY; // �ŏ����Ȃ̂ōŏ��́�
			minIndex = -1; // ����͕s�v�D�G���[�`�F�b�N�̂���.
			/*
			 * ���݂̌̂̒��Ŏ��R�G�l���M�[���ŏ�������̂�T���DcurrentPop_ �̓G���[�g��ǉ����ꂽ�̂�
			 * ���k����Ă���\��������̂ŃT�C�Y�͕s���D�I������̂� index ���K�v�Ȃ̂Ŋg��for�͎g��Ȃ��D
			 */
			for (int j = 0; j < currentPop_.size(); j++) {
				// Hj ��j �Ԗڂ����������̃G���g���s�[. ���ł� i �̂����܂��Ă���D
				// ���ꂩ��I�Ԍ̂� i+1 �̔Ԗ�
				Hj = entropy(i + 1, currentPop_.get(j));
				// ��j�̃G�l���M�[�� -1�~�K���x
				Ebar = (Esum + (-allFitness[j])) / (i + 1);// Ebar �͕��σG�l���M�[
				// Esum �͍��܂ł̘a
				F = Ebar - Hj * getTemperature();// ���R�G�l���M�[ F ���v�Z����.
				if (F < Fmin) { // ���݂̍ŏ��l��莩�R�G�l���M�[�����������
					Fmin = F;
					minIndex = j; // �ŏ����R�G�l���M�[�̂�index��ۑ��D
				}
			}
			indexArray[i] = minIndex; // �ŏ��̂� index ���L�^
			Esum += -allFitness[minIndex]; // �G�l���M�[�̍X�V
			updateNumOfGene(currentPop_.get(minIndex));
		}
		return indexArray;
	}

	/**
	 * ��`�q���̈�`�q 1,0 �̐����v�Z����.
	 * @param indiv ��
	 */
	private void updateNumOfGene(Individual indiv) {
		for (int i = 0; i < indiv.size(); i++) {
			if ((Integer) indiv.getGeneAt(i) == 0) {
				numOfGene_[0][i]++;
			} else if ((Integer) indiv.getGeneAt(i) == 1) {
				numOfGene_[1][i]++;
			} else {
				// ���݂͈�`�q 0,1 �݂̂ɑΉ�
				throw new IllegalArgumentException("The gene "
						+ indiv.getGeneAt(i) + " is invalid!");
			}
		}

	}

	/**
	 * �G���g���s�[���v�Z����.
	 * @param targetNum ���̖ڂ�I�����Ă��邩
	 * @param candidate �G���g���s�[���v�Z�������
	 * @return �G���g���s�[
	 */
	protected double entropy(int targetNum, Individual candidate) {
		double H1 = 0, Hall = 0; // H1�F1�r�b�g���̃G���g���s�[�C H�F�S�G���g���s�[
		// �S�r�b�g�𒲂ׂ�
		for (int i = 0; i < candidate.size(); i++) {
			// if (candidate.getGeneAt(i).intValue() == 1) { // ���̃r�b�g��1�̎�
			if ((Integer) candidate.getGeneAt(i) == 1) { // ���̃r�b�g��1�̎�
				H1 = -(numOfGene_[0][i] * getLogValue(numOfGene_[0][i]) + (numOfGene_[1][i] + 1)
						* getLogValue(numOfGene_[1][i] + 1))
						/ targetNum + getLogValue(targetNum);
				// } else if (candidate.getGeneAt(i).intValue() == 0) {//
				// ���̃r�b�g��0 �̎�
			} else if ((Integer) candidate.getGeneAt(i) == 0) {// ���̃r�b�g��0 �̎�
				H1 = -(numOfGene_[1][i] * getLogValue(numOfGene_[1][i]) + (numOfGene_[0][i] + 1)
						* getLogValue(numOfGene_[0][i] + 1))
						/ targetNum + getLogValue(targetNum);
			} else { // ���݂͈�`�q 0,1 �݂̂ɑΉ�
				throw new IllegalArgumentException("The gene "
						+ candidate.getGeneAt(i) + " is invalid!");
			}
			Hall += H1; // i �Ԗڂ̃G���g���s�[�𑫂�.
		}
		return Hall; // ���ׂĂ̈�`�q���̘a���G���g���s�[�Ƃ��ĕԂ��D
	}

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �p�����[�^�̑��v�f���̐�, ��2�v�f����`�q���D <br>
	 * �p�����[�^����3�̏ꍇ�͑�3�v�f�����x. �p�����[�^����4�̏ꍇ�͑�4�v�f���̌Q���k�t���O
	 * @param params �p�����[�^�̔z��
	 */
	public void setParameter(Object... params) {
		int popSize = -1, cLength = -1;
		try {
			// ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) { // ���l�N���X�̏ꍇ
				popSize = ((Number) params[0]).intValue(); // ���l�����Č̐��̐ݒ�

			} else { // ������̏ꍇ
				popSize = Integer.parseInt(params[0].toString()); // ���l�����Č̐��̐ݒ�
			}
			// ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[1] instanceof Number) { // ���l�N���X�̏ꍇ
				cLength = ((Number) params[1]).intValue();

			} else { // ������̏ꍇ
				cLength = Integer.parseInt(params[1].toString()); // ���l�����Ĉ�`�q���̐ݒ�
			}
			// �p�����[�^����3�ȏ�Ȃ�3�Ԗڂ͉��x
			if (params.length >= 3) {
				// ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
				if (params[2] instanceof Number) { // ���l�N���X�̏ꍇ
					temperature_ = ((Number) params[2]).doubleValue(); // ���l�����ĉ��x�̐ݒ�
				} else { // ������̏ꍇ
					temperature_ = Double.parseDouble(params[2].toString()); // ���l�����ĉ��x�̐ݒ�
				}
			}
			// �p�����[�^����4�Ȃ�4�Ԗڂ͌̌Q���k�t���O
			if (params.length == 4) {
				// Boolean�̏ꍇ�ƕ�����ŏꍇ����
				if (params[3] instanceof Boolean) { // Boolean�N���X�̏ꍇ
					setPackIndivList((Boolean) params[3]);
				} else { // Boolean�ȊO�̏ꍇ
					// "true" �ȊO�̕�����(�啶���������͖��֌W)�� false �ɂȂ�D
					setPackIndivList(Boolean.parseBoolean(params[3].toString()));
				}
			}
			// �p�����[�^����4�𒴂����ꍇ�͂�������
			if (params.length > 4) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid! ");
		}
		initLog(popSize);
		nextPopSize_ = popSize;
		numOfGene_ = new int[2][];
		numOfGene_[0] = new int[cLength]; // �e��`�q����0�̐���ۑ�
		numOfGene_[1] = new int[cLength]; // �e��`�q����1�̐���ۑ�
	}

	/**
	 * �M�͊w�I�I���ł́C�ό`�ɂ�� 0 �` size(�̐�)�܂ł� log �l�����g��Ȃ��悤�ɂł���D<br>
	 * �����ŁC�v�Z�ʂ̑��� log �l���n�߂Ɉ�񂾂��v�Z���Ă��܂��D
	 * @param size �̐�
	 */
	private void initLog(int size) {
		logValue_ = new double[size + 1];
		// entropy �v�Z���ɂ� x*log(x) �����o�Ă��Ȃ��D x �� 0 �̋Ɍ��ł��̒l�� 0 ��
		// �Ȃ�̂ŁC logValue_[0] = 0 �Ƃ��Ė��Ȃ��D
		logValue_[0] = 0;
		for (int i = 1; i <= size; i++) {
			logValue_[i] = Math.log(i);
		}
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "ThermoDynamicalSelection";
	}

	/**
	 * �ȑO�� initLog() �Ōv�Z���Ă����� log(i) �̒l��Ԃ�.
	 * @param i �l
	 * @return log(i)
	 */
	public double getLogValue(int i) {
		return logValue_[i];
	}

	/**
	 * ������̌̐���Ԃ�.
	 * @return ������̌̐�
	 */
	public int getNextPopSize() {
		if (nextPopSize_ == -1) {
			throw new IllegalArgumentException(
					"Please call init([pop_size,chromosome_length])!");
		}
		return nextPopSize_;
	}

	/**
	 * ����̈�`�q�^�����̂��폜�D �M�͊w�I�I���ł͓���̌̂����������Ă����ʂ͓����D
	 * @param indivList ���k����̂̃��X�g�D
	 */
	public void packIndivList(ArrayList<Individual> indivList) {
		//�󂢃R�s�[�����.
		ArrayList<Individual> copyList = new ArrayList<Individual>(indivList);
		indivList.clear(); // �{�̂̒��g�������D
		boolean isSelected = false;
		for (int i = 0; i < copyList.size(); i++) {
			isSelected = false;
			for (int j = 0; j < indivList.size(); j++) {
				// ������������`�q�^�̌̂������
				if (indivList.get(j).equals(copyList.get(i))) {
					isSelected = true; // ���łɑI�΂�Ă���D
					break;
				}
			}
			if (!isSelected) { // �I�΂�Ă��Ȃ���Βǉ��D
				indivList.add(copyList.get(i));
			}
		}
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		Individual indiv1 = new Individual("000");
		Individual indiv2 = new Individual("111");
		Individual indiv3 = new Individual("100");
		ThermoDynamicalSelection td = new ThermoDynamicalSelection();
		td.setParameter(10, 3);
		System.err.println(td.entropy(1, indiv1));
		td.updateNumOfGene(indiv1);
		System.err.println(td.entropy(2, indiv2) + " " + Math.log(2) * 3);
		td.updateNumOfGene(indiv2);
		double h = -3
				* (Math.log(1. / 3.) * 1. / 3. + Math.log(2. / 3.) * 2. / 3.);
		System.err.println(td.entropy(3, indiv3) + " " + h);
	}

	/**
	 * ���x��Ԃ�.
	 * @return ���x
	 */
	public double getTemperature() {
		return temperature_;
	}

	/**
	 * ���x��ݒ肷��.
	 * @param temperature ���x
	 */
	public void setTemperature(double temperature) {
		temperature_ = temperature;
	}

	/**
	 * ���[�J���e�X�g initLog ���\�b�h�̃e�X�g���s��.
	 * @throws LocalTestException
	 */
	public void localTest() throws LocalTestException {
		// initLog �̃e�X�g
		initLog(100);
		if (logValue_[0] != 0.0) {
			throw new LocalTestException("logValue_[0]!=0!");
		}
		for (int i = 1; i < logValue_.length; i++) {
			if (logValue_[i] != Math.log(i)) {
				throw new LocalTestException("logValue_[" + i + "] is wrong!");
			}
		}
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TEMPERATURE:36.0
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		String info = "TEMPERATURE:" + String.valueOf(getTemperature())+",isPackIndivList:"+String.valueOf(isPackIndivList_);
		String s = super.getParameterInfo();
		if (s.equals("")) {
			return info;
		} else {
			return info + "," + s;
		}
	}

	/**
	 * �̌Q�����k���邩�̃t���O
	 * @return �t���O
	 */
	public boolean isPackIndivList() {
		return isPackIndivList_;
	}

	/**
	 * �̌Q���k�t���O�̐ݒ�
	 * @param isPackIndivList
	 */
	public void setPackIndivList(boolean isPackIndivList) {
		this.isPackIndivList_ = isPackIndivList;
	}
}
