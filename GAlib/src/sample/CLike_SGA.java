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

package sample;

import java.util.Arrays;
import java.util.Random;

/**
 * Java ���S�җp�P��GA�N���X.
 * �I�u�W�F�N�g�w���I�ȗv�f�͈�؂Ȃ� C ���ꃌ�x���̎����ł���D
 * @author mori and takeuchi 
 * @version 1.0
 */
public class CLike_SGA {
	/** ��`�q�� */
	int LENGTH = 10;

	/** �̐� */
	int POPSIZE = 10;

	/** �ˑR�ψٗ� */
	double MRATE = 0.1;

	/** ������ */
	double CRATE = 0.6;

	/** ���㐔 */
	int GENERATION_SIZE = 100;

	/** �G���[�g�� */
	int[] elite_;

	/** �̌Q */
	int[][] pop_ = new int[POPSIZE][LENGTH];

	/** ���� */
	Random rand_ = new Random();

	/** �f�t�H���g�R���X�g���N�^ */
	public CLike_SGA() {
		// �̌Q��������
		for (int i = 0; i < POPSIZE; i++) {
			for (int j = 0; j < LENGTH; j++) {
				pop_[i][j] = rand_.nextInt(2);
			}
		}
	}

	/** �̌Q��\�� */
	public void printPop() {
		for (int i = 0; i < POPSIZE; i++) {
			System.out.println(Arrays.toString(pop_[i]) + " fitness:"
					+ fitness(pop_[i]));
		}
	}

	/** �G���[�g�̂�ۑ� */
	public void setElite() {
		int bestFitness = fitness(pop_[0]); // �擪�̂̓K���x�ŏ�����
		int bestNum = 0; // �擪�̂����̃G���[�g�Ƃ���
		for (int i = 1; i < POPSIZE; i++) {
			int f = fitness(pop_[i]);
			if (f > bestFitness) { // ���ǂ��̂�����΂������ۑ�
				bestFitness = f;
				bestNum = i;
			}
		}
		System.out.println(bestNum);
		elite_ = pop_[bestNum].clone(); // �R�s�[��ۑ�
	}

	/** �T�����X�^�[�g */
	public void start() {
		setElite();
		selection();
		crossover();
		mutation();
		returnElite();
	}

	/**
	 * �̌Q�Ɉ�_������K�p�D
	 */
	public void crossover() {
		// �I����ɌĂ΂�邱�Ƃ�O��ɂ��Ă���̂ŃV���b�t���Ȃ�
		for (int i = 0; i < POPSIZE / 2; i++) {
			/*
			 * TODO �� pop_[2*i] �� pop_[2*i+1] �Ɍ�����K�p�D pop_ �̒��g�𒼐ڏ���������D
			 */
			if (rand_.nextDouble() < CRATE) {
				// �����_
				int point = rand_.nextInt(LENGTH);
				for (int j = point; j < LENGTH; j++) {
					int tmp = pop_[2 * i][j];
					pop_[2 * i][j] = pop_[2 * i + 1][j];
					pop_[2 * i + 1][j] = tmp;
				}
			}
		}
	}

	/**
	 * �̌Q�ɓˑR�ψق�K�p�D
	 */
	public void mutation() {
		for (int i = 0; i < POPSIZE; i++) {
			/*
			 * TODO �� pop_[i]�ɓˑR�ψق�K�p�D pop_ �̒��g�𒼐ڏ���������D
			 */
			for (int j = 0; j < LENGTH; j++) {
				if (rand_.nextDouble() < MRATE) {
					// ��`�q��1�̏ꍇ0��, 0�̏ꍇ1�Ɋ�����.
					// ����ȊO�̐��̎��͗�O����.
					try {
						switch (pop_[i][j]) {
						case 0:
							pop_[i][j] = 1;
							break;
						case 1:
							pop_[i][j] = 0;
							break;
						default:
							throw new IllegalArgumentException();
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Gene must be used 0 or 1.");
					}
				}
			}
		}
	}

	/**
	 * �̌Q�ɓK���x���I��(���[���b�g�I��)��K�p�D
	 */
	public void selection() {
		int[][] tmp = new int[POPSIZE][LENGTH];
		/*
		 * TODO pop_ ���烋�[���b�g���񂵂Ĉ�̂�I�� tmp[i] �ɒǉ��D ���̏ꍇ�C�I�񂾌̂̃R�s�[��n������. pop_[x]
		 * ���I�΂ꂽ�ꍇ�ɂ� <br>
		 * tmp[i]=pop_[x] <br>
		 * �ł͂Ȃ��C 
		 * tmp[i]=pop_[x].clone() <br>
		 * �Ƃ���Dpop_[x]�̓v���~�e�B�u�^�̔z��Ȃ̂ł���� OK.
		 */
		double sum = 0;
		int[] all_fitness = new int[POPSIZE];
		for (int i = 0; i < POPSIZE; i++) {
			all_fitness[i] = fitness(pop_[i]);
			sum += all_fitness[i];
		}
		for (int i = 0; i < POPSIZE; i++) {
			// ���[���b�g�̐j����.
			double needle = rand_.nextDouble() * sum;
			int index = 0;
			// �j���w���ꏊ��T��
			while (needle - all_fitness[index] > 0) {
				needle -= all_fitness[index];
				index++;
			}
			// �I�񂾌̂�ۑ�
			tmp[i] = pop_[index].clone();
		}
		// �̌Q�̍X�V
		pop_ = tmp;
	}

	/**
	 * �̌Q�ɃG���[�g�̂�߂��ׂ� �G���[�g�̂ƓK���x�̍ł��Ⴂ�̂Ƃ����ւ���.
	 */
	public void returnElite() {
		// �K���x�̍ł��Ⴂ�̂�T��
		int worstFitness = fitness(pop_[0]); // �擪�̂̓K���x�ŏ�����
		int worstNum = 0; // �擪�̂����Ƃ���
		for (int i = 1; i < POPSIZE; i++) {
			int f = fitness(pop_[i]);
			if (f < worstFitness) { // ��舫���̂�����΂������ۑ�
				worstFitness = f;
				worstNum = i;
			}
		}
		pop_[worstNum] = elite_.clone(); // �G���[�g�ɒu��������
	}

	/**
	 * �̂̓K���x��Ԃ��D�r�b�g�J�E���g�D
	 * @param indiv ��
	 * @return �K���x
	 */
	public int fitness(int[] indiv) {
		int sum = 0;
		for (int i = 0; i < indiv.length; i++) {
			if (indiv[i] == 1) {
				sum += 1;
			}
		}
		return sum;
	}

	public static void main(String[] args) {
		CLike_SGA ga = new CLike_SGA();
		for (int i = 0; i < ga.GENERATION_SIZE; i++) {
			ga.start();
			System.out.println(Arrays.toString(ga.elite_));
		}
		ga.printPop();
	}

}
