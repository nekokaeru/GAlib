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

import problem.IProblem;
import problem.ProblemFactory;
import problem.function.FunctionFactory;
import problem.function.IFunction;

/**
 * ���ƕϊ��֐��̎g�����̗�D���̓r�b�g�J�E���g�ƃo�C�i���ϊ��Ƃ��C�v���O�����ւ̈�����
 * �ϊ��֐����w�肷��D���̂Ƃ��C�p�b�P�[�W��(problem.function.)�͎w�肵�Ȃ��Ă悢�D
 * �N���X���̌�ɁF�ŋ�؂��ăp�����[�^���w��ł���D��F PowerFunction:2 �p�����[�^����
 * NegativeFunction�F �Ȃ� PowerFunction�F 1 (�w��) LinearFunction�F 2 (�X���C�ؕ�)
 * �ł���D java Main PowerFunction:2 NegativeFunction
 * �Ƃ���ƁC�ړI�֐��l��2�悵�ĕ����𔽓]����ϊ����s���D
 * @author mori
 * @version 1.0
 */
public class ProblemSample {
	public static void main(String[] args) {
		// ���ۃN���X�Ƃ��Ė����w��D
		IProblem bitcount = ProblemFactory
				.createProblem("problem.BitCountProblem");
		IProblem binary = ProblemFactory
				.createProblem("problem.BinaryNumberProblem");
		// 3�̂���Ȃ�̌Q
		Number[][] pop = { { 0, 1, 0, 1, 0 }, { 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1 } }; // 2�����z��
		// �ϊ��֐��̃p�b�P�[�W
		String prefix = "problem.function.";
		// �v���O���������Ŏw�肳�ꂽ�N���X��ϊ��֐��Ƃ��Ēǉ����Ă����D
		for (String s : args) {
			IFunction f;
			if (s.indexOf(':') > 0) { // �p�����[�^�̎w�肠��
				String name = s.split(":")[0]; // �f���~�^�F�ŕ��������Ƃ��̐擪���N���X��
				f = FunctionFactory.createFunction(prefix + name);
				// �擪�̃N���X������菜��
				String paramsName = s.replaceFirst("^" + name + ":*", "");
				Object[] params = new Object[0];
				if (!paramsName.equals("")) { // �󕶎���̏ꍇ�̓p�����[�^����
					params = paramsName.split(":");
				}
				f.setParameter(params);
			} else { // �p�����[�^�̎w��Ȃ�
				f = FunctionFactory.createFunction(prefix + s);
			}
			bitcount.addFunction(f);
			binary.addFunction(f);
		}
		for (Number[] genotype : pop) {
			// ��`�q�^�\��
			System.out.println("genotype->" + Arrays.toString(genotype));
			// �r�b�g�J�E���g���̏ꍇ�̓K���x f �ƌ��̖ړI�֐��l g ��\��
			System.out.println("bitcount:" + "f="
					+ bitcount.getFitness(genotype) + " g="
					+ bitcount.getObjectiveFunctionValue(genotype));
			// �o�C�i���ϊ����̏ꍇ�̓K���x f �ƌ��̖ړI�֐��l g ��\��
			System.out.println("binary:  " + "f=" + binary.getFitness(genotype)
					+ " g=" + binary.getObjectiveFunctionValue(genotype) + "\n");
		}
	}

}
