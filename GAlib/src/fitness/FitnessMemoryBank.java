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

import java.util.ArrayList;
import java.util.HashMap;
import population.Individual;

/**
 * �̂̓K���x����ۑ�����N���X.
 * @author mori
 * @version 1.0
 */
public class FitnessMemoryBank implements IFitnessMemoryBank {
	/**
	 * �̃I�u�W�F�N�g�ƓK���x���L������D �̃I�u�W�F�N�g���L�[�ɁC�K���x��l�ɂ��Ă���D
	 */
	private HashMap<Individual, Double> memory_ = new HashMap<Individual, Double>();

	/**
	 * memory_ �� key �ƂȂ�̗̂����D�L�������������߂邽�߁D
	 */
	private ArrayList<Individual> history_ = new ArrayList<Individual>();

	/**
	 * �L������̐��D���̏ꍇ�͐����Ȃ��D
	 */
	private long maxMemorySize_ = -1;

	/**
	 * ���������ɊY���̂����݂���΂��̌̂̓K���x��Ԃ�. �Ȃ���� null ���Ԃ�D
	 * @param indiv ��
	 * @return �K���x
	 */
	public Double getFitness(Individual indiv) {
		return memory_.get(indiv);
	}

	/**
	 * ���������ɊY���̂����݂��邩�𒲂ׂ�.
	 * @param indiv ��
	 * @return true: ���݂��� false: ���݂��Ȃ�
	 */
	public boolean containsIndividual(Individual indiv) {
		return memory_.containsKey(indiv);
	}

	/**
	 * �������Ɍ̂�ǉ�����. ���������ɓ���̂����݂���ꍇ�͒ǉ����Ȃ�. <br>
	 * ���������̌̐����K��̃T�C�Y�𒴂���ꍇ��, �ł��Â��̂��폜���Ă���ǉ�����.
	 * @param indiv ��
	 * @param fitness �K���x
	 * @return true: �ǉ����������� false: ���łɓ����̂��������ɑ��݂��Ă���
	 */
	public boolean put(Individual indiv, Double fitness) {
		if (memory_.containsKey(indiv)) {
			// ���łɑ��݂��Ă���ꍇ�D TODO ���I���ȂǓK���x���ω�����ꍇ�ɂ͕ύX���K�v�D
			return false;
		}
		// �L���Ɨ������X�V
		// �K���C�[���R�s�[���L�[�ɂ��čX�V����D�������Ȃ��ƃL�[���ǂ����ŏ���ɕς���Ă��܂��댯������D
		Individual keyIndiv = indiv.clone();
		memory_.put(keyIndiv, fitness);
		history_.add(keyIndiv);
		// �T�C�Y���߂̏ꍇ�̏���
		if ((maxMemorySize_ > 0) && (memory_.size() > maxMemorySize_)) {
			remove(history_.get(0)); // ��ԌÂ����̂��폜�D
		}
		return true;
	}

	/**
	 * ����������Ώی̂��폜. (�����������ɍ폜����.)
	 * @param indiv ��
	 * @return true: �폜���� false: �������ɑΏی̂����݂��Ȃ�
	 */
	public boolean remove(Individual indiv) {
		if (memory_.containsKey(indiv)) {
			// ���݂��Ă���ꍇ�͋L���Ɨ�������폜�D
			memory_.remove(indiv);
			history_.remove(indiv);
			return true;
		}
		// ���݂��Ă��Ȃ������ꍇ
		return false;
	}

	/**
	 * �������̃T�C�Y��Ԃ�.
	 * @return �������̃T�C�Y
	 */
	public long size() {
		assert (memory_.size() == history_.size());
		return memory_.size();
	}

	/**
	 * �������Ɨ���������������.
	 */
	public void clear() {
		memory_.clear();
		history_.clear();
	}

	/**
	 * ������(HashMap)�̕�����\����Ԃ�.
	 * @return �������̕�����\��
	 */
	public String toString() {
		return memory_.toString();
	}

	/**
	 * �ݒ肳��Ă��郁�����̏���̃T�C�Y��Ԃ�.
	 * @return maxMemorySize �������̏���̃T�C�Y
	 */
	public long getMaxMemorySize() {
		return maxMemorySize_;
	}

	/**
	 * �������̏���̃T�C�Y��ݒ肷��. ������^�����ꍇ�̓������̃T�C�Y�𐧌����Ȃ�.
	 * @param maxMemorySize �������̏���̃T�C�Y
	 */
	public void setMaxMemorySize(long maxMemorySize) {
		maxMemorySize_ = maxMemorySize;
		if (maxMemorySize_ < 0) { // ���̏ꍇ�͏���Ȃ��D
			return;
		}
		if (history_.size() > maxMemorySize) {
			int currentSize = history_.size();
			for (int i = 0; i < currentSize - maxMemorySize; i++) {
				memory_.remove(history_.get(0));// �L�����璴�ߕ��̌Â��̂��폜
				history_.remove(0);// ����������폜
			}
		}
	}

	/**
	 * ���s��D
	 * @param args
	 */
	public static void main(String[] args) {
		FitnessMemoryBank map = new FitnessMemoryBank();
		Number[] i1 = { 1, 0, 1 };
		Number[] i2 = { 1, 0, 1 };
		Individual indiv1 = new Individual(i1);
		Individual indiv2 = new Individual(i2);
		indiv1.fitness();
		indiv2.fitness();
		map.put(indiv1, indiv1.fitness());
		System.err.println(map);
		indiv1.setGeneAt(0, 3);
		System.err.println(map);
	}
}
