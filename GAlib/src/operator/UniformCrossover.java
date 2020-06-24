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
 * ��l��������
 * Created on 2005/07/13
 * 
 */
package operator;

import java.util.Arrays;

import population.Individual;
import util.LocalTestException;
import util.MyRandom;
import util.NStatistics;

/**
 * ��l�����D
 * @author mori
 * @version 1.0
 */
public class UniformCrossover extends AbstractCrossover {
	/**
	 * �I�y���[�^����Ԃ��D
	 * @return name �I�y���[�^��"UniformCrossover"
	 */
	public String getName() {
		return "UniformCrossover";
	}

	/**
	 * �e�̂Ɉ�l������K�p. p1, p2 �ɂ��Ĕj��I�ȉ��Z�q�D �X�[�p�[�N���X�� apply �ŌĂ΂��D<br>
	 * �}�X�N�̓����_���Ɍ��߂��C�K����������D ���ׂ� 1 �������͂��ׂ� 0 ��h�����߂ɋ����I��<br>
	 * 0 �� �P ���ЂƂ����D �}�X�N�� 1 �̂Ƃ��C��`�q����������D
	 * @param p1 �e1�Cp2 �e2
	 */
	@Override
	public void crossover(Individual p1, Individual p2,
			Individual... individuals) {
		// �}�X�N�p
		int size = p1.size();
		byte[] mask = makeMask(size);
		for (int i = 0; i < p1.size(); i++) {
			// �}�X�N��1�ł���Έ�`�q����������D
			if (mask[i] == 1) {
				Number tmp = p1.getGeneAt(i);
				p1.setGeneAt(i, p2.getGeneAt(i));
				p2.setGeneAt(i, tmp);
			}
		}
	}

	/**
	 * �}�X�N��Ԃ��D
	 * @param size �}�X�N�̒���
	 * @return �}�X�N
	 */
	private byte[] makeMask(int size) {
		byte[] mask = new byte[size];
		for (int i = 0; i < mask.length; i++) {
			mask[i] = (byte) MyRandom.getInstance().nextInt(2);
		}
		// all 0 �� all 1 ���������߁C�����I�� 0 �� �P ���ЂƂ����D
		int index0 = MyRandom.getInstance().nextInt(size);
		int index1 = index0 + 1 + MyRandom.getInstance().nextInt(size - 1);
		mask[index0] = 0;
		mask[index1 % size] = 1;
		return mask;
	}

	/**
	 * UniformCrossover �� localTest() �����s����D
	 * @param args
	 */
	public static void main(String[] args) {
		UniformCrossover x = new UniformCrossover();
		try {
			x.localTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �}�X�N�̐������e�X�g����D<br>
	 * ���ׂ� 1 �������͂��ׂ� 0 �ɂȂ��Ă��Ȃ����C 1 �� 0 ���m���I�ɋϓ��ɂȂ��Ă��邩���e�X�g����D
	 * @throws LocalTestException
	 */

	public void localTest() throws LocalTestException {
		// makeMask
		int size = 3, testNum = 2500;
		byte[] all0 = new byte[size], all1 = new byte[size];
		Arrays.fill(all0, (byte) 0);
		Arrays.fill(all1, (byte) 1);
		int[] sum = new int[size];
		byte[] mask;
		for (int i = 0; i < testNum; i++) {
			mask = makeMask(size);
			// �z��̓��e�ɂ���r�� Arrays.equals ��p����D
			if (Arrays.equals(mask, all0) || Arrays.equals(mask, all1)) {
				throw new LocalTestException();
			}
			for (int j = 0; j < mask.length; j++) {
				sum[j] += mask[j];
			}
		}
		for (int i = 0; i < sum.length; i++) {
			// ���ʂ�4�Ђɓ����Ă���΂悢�D
			if (!NStatistics.binomialTest(testNum, sum[i], 0.5, 4)) {
				throw new LocalTestException();
			}
		}
	}
}
