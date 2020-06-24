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
 * �̌Q�N���X
 * Created on 2005/07/16
 * 
 */
package population;

import gabuilder.GAParameters;

import java.util.ArrayList;

/**
 * �̌Q�N���X.
 * @author mori
 * @version 1.0
 */
public class Population implements Cloneable {
	/**
	 * �̌Q
	 */
	private ArrayList<Individual> indivList_ = new ArrayList<Individual>();

	/**
	 * �����Ȃ��̃R���X�g���N�^�D
	 */
	public Population() {
		// �����Ȃ��̏ꍇ�͌̐�100�C��`�q��10�ŏ������D
		init(GAParameters.DEFAULT_POPULATION_SIZE,
				GAParameters.DEFAULT_CHROMOSOME_LENGTH);
	}

	/**
	 * �̐��ƈ�`�q���ŏ���������R���X�g���N�^
	 * @param popsize �̐�
	 * @param clength ��`�q��
	 */
	public Population(int popsize, int clength) {
		init(popsize, clength);
	}

	/**
	 * indivList �ɂ�鏉����������R���X�g���N�^
	 * @param indivList �̌Q�� ArrayList
	 */
	public Population(ArrayList<Individual> indivList) {
		setIndivList(indivList);
	}

	/**
	 * �������֐�
	 * @param popsize �̐�
	 * @param clength ��`�q��
	 */
	public void init(int popsize, int clength) {
		indivList_.clear();
		for (int i = 0; i < popsize; i++) {
			indivList_.add(new Individual(clength));
		}
	}

	/**
	 * �[���R�s�[�����邽�߂ɁCclone ���I�[�o�[���C�h�D JDK5.0 �̋��ϖ߂�l�𗘗p�D
	 * @return �[���R�s�[���ꂽ Population
	 */
	@Override
	public Population clone() {
		try {
			Population pop = (Population) super.clone();
			ArrayList<Individual> indivList = new ArrayList<Individual>();
			for (Individual indiv : indivList_) {
				// Individual �N���X�� clone �͐[���R�s�[�ł͂Ȃ���
				// ��`�q�� Integer ���̕s�σI�u�W�F�N�g�ł���Ζ��Ȃ��D
				indivList.add(indiv.clone());
			}
			pop.setIndivList(indivList);
			return pop;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e.getMessage());
		}
	}

	/**
	 * �̐���Ԃ�.
	 * @return �̐�
	 */
	public final int getPopulationSize() {
		return indivList_.size();
	}

	/**
	 * �̌Q��Ԃ�.
	 * @return �̌Q
	 */
	public final ArrayList<Individual> getIndivList() {
		return indivList_;
	}

	/**
	 * ��̂�Ԃ��D
	 * @param index �̌Q�̂���̂��w���C���f�b�N�X
	 */
	public final Individual getIndividualAt(int index) {
		return indivList_.get(index);
	}

	/**
	 * �������̂��ߐ[���R�s�[�͂��Ȃ��D
	 * @param pop �̌Q
	 */
	public final void setIndivList(ArrayList<Individual> pop) {
		indivList_ = pop;
	}

	/**
	 * ��̂��㏑��.
	 * @param index �̌Q�̂Ƃ����
	 * @param indiv �㏑�������
	 */
	public final void setIndividualAt(int index, Individual indiv) {
		indivList_.set(index, indiv);
	}

	/**
	 * ���ׂĂ̌̂̐��F�̂� String �ɕϊ����ĕԂ��D
	 * @return ���ׂĂ̌̂̐��F�̂̕�����
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Individual indiv : indivList_) {
			sb.append(indiv.toString());
			sb.append("\n");

		}
		return sb.toString();
	}

	/**
	 * �̌Q�S�̂̕ύX�t���O���Z�b�g����D
	 * @param b �Z�b�g����^�U�l
	 */
	public void setAllChanged(boolean b) {
		for (Individual indiv : indivList_) {
			indiv.setChanged(b);
		}
	}

	/**
	 * ���s��D
	 * @param args
	 */
	public static void main(String[] args) {
		Population pop = new Population();
		System.err.println(pop);
	}
}
