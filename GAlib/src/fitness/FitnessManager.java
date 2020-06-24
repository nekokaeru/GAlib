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

package fitness;

import gabuilder.GAParameters;

import java.util.ArrayList;

import population.Individual;
import problem.BitCountProblem;
import problem.IProblem;
import util.MyRandom;

/**
 * �K���x���Ǘ����郆�[�e�B���e�B�N���X.
 * @author mori
 * @version 1.0
 */
public class FitnessManager {
	/**
	 * �G���[�g�̕ۑ��p���X�g�D�K���x���v�Z�������ɍX�V�����D
	 */
	private static ArrayList<Individual> eliteList_;

	/**
	 * �G���[�g�̂̓K���x�D�G���[�g�̂��ǂ������m�F���邽�߂Ɏg�p�D
	 */
	private static double eliteFitness_ = Double.NEGATIVE_INFINITY;

	/**
	 * ���I�u�W�F�N�g�D�K���x���肩��v�Z����D
	 */
	private static IProblem problem_;

	/**
	 * �K���x�v�Z�̍����������邩�̃t���O�D(�������t���O)
	 */
	private static boolean isTurbo_ = GAParameters.DEFAULT_IS_TURBO;

	/**
	 * ���K���x�]����
	 */
	private static long totalEvalNum_ = 0;

	/**
	 * ��`�q�^�ƓK���x�̋L���N���X�D
	 */
	private static IFitnessMemoryBank memoryBank_;

	/**
	 * �̂̓K���x�����L�����邩�̃t���O�D(�̃������t���O)
	 */
	private static boolean isMemory_ = GAParameters.DEFAULT_IS_MEMORY;

	/**
	 * �f�t�H���g�R���X�g���N�^�D Utility Class �Ȃ̂ŃR���X�g���N�^�� private
	 */
	private FitnessManager() {
	}

	/**
	 * �̌Q�̂��ׂĂ̌̂̓K���x�� double �̔z��Ɋi�[���ĕԂ��D
	 * @param pop �̌Q
	 * @return �̌Q�ɂ����邷�ׂĂ̌̂̓K���x�̔z��
	 */
	public static double[] getFitnessArray(ArrayList<Individual> pop) {
		// �K���x�̏�����
		double[] allFitness = new double[pop.size()];
		for (int i = 0; i < pop.size(); i++) {
			double f = pop.get(i).fitness();
			allFitness[i] = f;
		}
		return allFitness;
	}

	/**
	 * �K���x���v�Z����D getChromosome �͐��F�̂̕ύX���֎~���邽�߂ɔz��� clone �� �Ԃ����C<br>
	 * ���̃��X������邽�߂� Individual �� fitness �Œ��ڐ��F�̂�n���Ă���D
	 * @param chromosome ���F��
	 * @return chromosome �̓K���x
	 * @see Individual#fitness()
	 */
	public static double getFitness(Number[] chromosome) {
		totalEvalNum_++; // �K���x�����v�Z�D
		return getProblem().getFitness(chromosome);
	}

	/**
	 * �K���x���v�Z����D ��{�I�ɂ� getFitness �Ɠ��������C�L�^�Ɏc��Ȃ��D <br>
	 * ��̓I�ɂ͓K���x�]���񐔂͑������C�G���[�g���X�V���Ȃ��D<br>
	 * �r���̃��O�ȂǒT���Ɋ֌W�̂Ȃ��K���x�v�Z�̂��߂ɂ���D chromosome �𒼐ړn���D
	 * @param chromosome ���F��
	 * @return chromosome �̓K���x
	 * @see Individual#fitness()
	 * @see FitnessManager#getFitness(Number[])
	 */
	public static double getFitnessWithoutRecord(Number[] chromosome) {
		return getProblem().getFitness(chromosome);
	}

	/**
	 * �G���[�g�̃��X�g���X�V����֐��D
	 * @param indiv ������
	 * @param f �����̂̓K���x
	 * @return �X�V���ꂽ��
	 * @see Individual#fitness()
	 */
	public static boolean updateElite(Individual indiv, double f) {
		// �K���x���������ꍇ�D
		if (f == eliteFitness_) {
			setElite(indiv);
			return true;
		} else if (f > eliteFitness_) {
			getEliteList().clear();
			setElite(indiv);
			eliteFitness_ = f;
			return true;
		}

		return false;
	}

	/**
	 * �G���[�g�̂��擾����֐�. �����̃G���[�g�����݂���ꍇ�ɂ́C�����_���ɂЂƂI�ԁD<br>
	 * �G���[�g�̂̃N���[�����Ԃ�. �G���[�g�̂����݂��Ȃ��ꍇ�� null ���Ԃ�.
	 * @return �G���[�g��
	 * @see Individual#fitness()
	 */
	public static Individual getElite() {
		int size = getEliteList().size();
		// �G���[�g��������̏ꍇ�D
		if (size == 1) {
			return getEliteList().get(0).clone();
			// �������݂���ꍇ�D
		} else if (size > 1) {
			int num = MyRandom.getInstance().nextInt(size);
			return getEliteList().get(num).clone();
			// �G���[�g�̃��X�g����̏ꍇ�D
		} else {
			return null;
		}
	}

	/**
	 * �G���[�g�̂�ۑ��������X�g�̐[���R�s�[��Ԃ��D
	 * @return �G���[�g�̂�v�f�Ɏ����X�g
	 */
	public static ArrayList<Individual> getCopyOfEliteList() {
		ArrayList<Individual> deepCopy = new ArrayList<Individual>();
		for (Individual individual : getEliteList()) {
			deepCopy.add(individual.clone());
		}
		return deepCopy;
	}

	/**
	 * �̂��G���[�g�̂Ƃ��ēo�^����.
	 * @param elite �o�^�Ώی�
	 */
	private static void setElite(Individual elite) {
		// ���� elite ���܂��ۑ�����Ă��Ȃ����
		if (!getEliteList().contains(elite)) {
			getEliteList().add(elite.clone());
		}
	}

	/**
	 * �G���[�g�̂̓K���x���擾����.
	 * @return �G���[�g�̂̓K���x
	 */
	public static double getEliteFitness() {
		return eliteFitness_;
	}

	/**
	 * FitnessManager �ɐݒ肳��Ă�������擾����֐�. ��肪�ݒ肳��Ă��Ȃ� (null) �ꍇ��,
	 * �f�t�H���g�̃r�b�g�J�E���g����Ԃ�.
	 * @return �ݒ肳��Ă�����
	 */
	public static IProblem getProblem() {
		if (problem_ == null) {
			// �f�t�H���g�̓r�b�g�J�E���g
			problem_ = new BitCountProblem();
		}
		return problem_;
	}

	/**
	 * �K���x���v�Z���邽�߂̖���ݒ肷��D
	 * @param problem �ݒ肷����D
	 */
	public static void setProblem(IProblem problem) {
		problem_ = problem;
	}

	/**
	 * �������t���O��Ԃ�. ���̐���ȑO�ɓK���x���v�Z����Ă���̂ɂ��Ă�, �K���x�̍Čv�Z���s��Ȃ���������@.
	 * @return �������t���O�̏��. true: ���������ݒ肳��Ă��� false: ���������ݒ肳��Ă��Ȃ�
	 */
	public static boolean isTurbo() {
		return isTurbo_;
	}

	/**
	 * �������t���O�̐ݒ���s��.
	 * @param isTurbo �������t���O�̏�� true: ��������ݒ肷�� false: ��������ݒ肵�Ȃ�
	 */
	public static void setTurbo(boolean isTurbo) {
		FitnessManager.isTurbo_ = isTurbo;
	}

	/**
	 * �����_�̓K���x�]���񐔂��擾����.
	 * @return �K���x�]����
	 */
	public static long getTotalEvalNum() {
		return totalEvalNum_;
	}

	/**
	 * �e���������������D<br>
	 * �G���[�g�́C�ő�K���x�C�K���x�]���񐔁C���C(�ݒ肳��Ă����)�L����� �������t���O�C�̃������t���O
	 */
	public static void reset() {
		getEliteList().clear();
		eliteFitness_ = Double.NEGATIVE_INFINITY;
		totalEvalNum_ = 0;
		problem_ = null;
		setTurbo(false);
		setMemory(false);
	}

	/**
	 * �̃������t���O��Ԃ�.
	 * @return �̃������t���O true: �L�����𗘗p���Ă��� false: �L�����@�𗘗p���Ă��Ȃ�
	 */
	public static boolean isMemory() {
		return isMemory_;
	}

	/**
	 * �̃������t���O�̐ݒ���s��.
	 * @param isMemory �̃������t���O true: �L�����𗘗p���� false: �L�����𗘗p���Ȃ�
	 */
	public static void setMemory(boolean isMemory) {
		// �L������߂��ꍇ�ɂ͋L���̈�����
		if (isMemory == false) {
			memoryBank_ = null;
		}
		FitnessManager.isMemory_ = isMemory;
	}

	/**
	 * �L���N���X���擾����. �L����� (�̃�����) �����݂��Ȃ����, �V���ɋ�̋L�����̃I�u�W�F�N�g�𐶐����Ԃ�.
	 * @return �L���N���X
	 */
	public static IFitnessMemoryBank getMemoryBank() {
		if (memoryBank_ == null) {
			// �f�t�H���g
			memoryBank_ = new FitnessMemoryBank();
		}
		return memoryBank_;
	}

	/**
	 * �L�������Z�b�g����.
	 * @param memoryBank �L�����
	 */
	public static void setMemoryBank(IFitnessMemoryBank memoryBank) {
		memoryBank_ = memoryBank;
	}

	/**
	 * �G���[�g�̂�ۑ��������X�g��Ԃ�.
	 * @return �G���[�g�̂�v�f�Ɏ����X�g
	 */
	private static ArrayList<Individual> getEliteList() {
		if (eliteList_ == null) {
			eliteList_ = new ArrayList<Individual>();
		}
		return eliteList_;
	}
}
