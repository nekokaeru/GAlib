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

import population.Individual;

/**
 * �ύX�t���O�ƃ������������������ꂽ�K���x�N���X.
 * @author mori
 * @version 1.0
 */
public class ChangedFlagAndMemoryFitness implements IFitness {

	/**
	 * Individual �̃C���X�^���X���󂯎��K���x��Ԃ��D �������t���O�������Ă���C�ύX���Ȃ���ΈȑO�̓K���x��Ԃ��D<br>
	 * �L���t���O�������Ă���C���g���L������Ă���΂��̏���Ԃ��D �K���x���v�Z���ꂽ���́C�G���[�g��ۑ�����D <br>
	 * �܂��O�K���x�ƕύX�t���O���X�V����D �܂��Ō�ɋL���t���O�������Ă����ꍇ�ɂ͂��̌̂ƓK���x����ǉ�����D
	 * @param indiv �̃N���X
	 * @see fitness.IFitness#fitness(population.Individual)
	 */
	public double fitness(Individual indiv) {
		// �������t���O�������Ă���C�ύX���Ȃ���ΈȑO�̓K���x��Ԃ��D
		if (FitnessManager.isTurbo() && !indiv.isChanged()) {
			return indiv.getPreviousFitness();
		}
		// �L���t���O�������Ă���C���g���L������Ă���΂��̏���Ԃ��D
		if (FitnessManager.isMemory()
				&& FitnessManager.getMemoryBank().containsIndividual(indiv)) {
			double f = FitnessManager.getMemoryBank().getFitness(indiv);
			indiv.setPreviousFitness(f);
			indiv.setChanged(false);
			return f;
		}
		double f = indiv.simpleFitness();
		// �V���ɓK���x���v�Z�����̂ŁC�O�K���x�ƕύX�t���O���X�V�D
		indiv.setPreviousFitness(f);
		indiv.setChanged(false);
		// �L���t���O�������Ă����ꍇ�ɂ͂��̌̂ƓK���x����ǉ��D
		if (FitnessManager.isMemory()) {
			FitnessManager.getMemoryBank().put(indiv, f);
		}
		return f;
	}

}
