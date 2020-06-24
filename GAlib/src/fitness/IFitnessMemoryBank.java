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
 * �̂̏���ێ�����@�\�̃C���^�[�t�F�[�X.
 * �I����������O���f�[�^�x�[�X�𗘗p.
 * @author mori
 * @version 1.0
 */
public interface IFitnessMemoryBank {
	/**
	 * �̂̓K���x�f�[�^���擾����D �L���ɂȂ��̂������ꍇ�� null ���Ԃ�D
	 * @param indiv �Ώی�
	 * @return �K���x
	 */
	public Double getFitness(Individual indiv);

	/**
	 * �����̂��L������Ă��邩�𒲂ׂ�D
	 * @param indiv ������
	 * @return true: ���݂��� false: ���݂��Ȃ�
	 */
	public boolean containsIndividual(Individual indiv);

	/**
	 * �̂ƓK���x�̑g��ǉ�����D
	 * @param indiv ��, fitness indiv�̓K���x
	 * @return �ǉ����ꂽ���̃u�[���l true: �ǉ����ꂽ false: ���łɑ��݂��Ă���
	 */
	public boolean put(Individual indiv, Double fitness);

	/**
	 * �̂̏����폜����D
	 * @param indiv ��
	 * @return �폜���ꂽ���̃u�[���l true: �폜���ꂽ false: ���݂��Ȃ�����
	 */
	public boolean remove(Individual indiv);

	/**
	 * �L�����Ă���̐��D
	 * @return �L�����Ă���̐�
	 */
	public long size();

	/**
	 * �S���̏���
	 */
	public void clear();
}
