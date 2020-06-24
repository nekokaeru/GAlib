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
 * �ˑR�ψكN���X
 * Created on 2005/06/27
 */
package operator;

import population.Individual;
import util.MyRandom;

/**
 * ��`�q�����ƂɓˑR�ψق�K������N���X�D<br>
 * {0,1} �o�C�i�����p�ˑR�ψى��Z�q�D
 * @author mori
 * @version 1.0
 */
public class BitMutation extends AbstractMutation {

	/**
	 * �e�̂̈�`�q�����ƂɓˑR�ψق�K�p. indiv �ɂ��Ĕj��I�ȉ��Z�q�D �X�[�p�[�N���X�� apply �ŌĂ΂��D
	 * @param indiv ��
	 */
	@Override
	public void mutation(Individual indiv) {
		for (int i = 0; i < indiv.size(); i++) {
			// �e�r�b�g�ɓˑR�ψٗ��œˑR�ψق�K�p
			if (MyRandom.getInstance().nextDouble() < getMutationRate()) {
				if ((Integer) indiv.getGeneAt(i) == 0) {
					indiv.setGeneAt(i, 1);
				} else if ((Integer) indiv.getGeneAt(i) == 1) {
					indiv.setGeneAt(i, 0);
				} else { // �Η���`�q 0, 1 �݂̂ɑΉ�
					throw new IllegalArgumentException(getName()
							+ " Illegal gene type:" + indiv.getGeneAt(i));
				}
			}
		}
	}

	/**
	 * �I�y���[�^����Ԃ�
	 * @return name �I�y���[�^��"BitMutation"
	 */
	public String getName() {
		return "BitMutation";
	}
}
