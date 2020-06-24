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

import java.util.HashMap;

import population.Individual;
import population.Population;
import population.PopulationAnalyzer;
import problem.BitCountProblem;
import problem.IProblem;
import problem.function.PowerFunction;
import util.MyRandom;
import fitness.FitnessManager;
/**
 * Population �N���X�̎g�p��.
 * @author mori
 * @version 1.0
 */

public class PopulationSample {
	public static void main(String[] args) {
		// �V�[�h�Œ�
		MyRandom.setSeed(0);
		// ��`�q�� 3 �� 4 �̂���Ȃ�̌Q���쐬
		Population pop = new Population(4, 3);
		// �r�b�g�J�E���g�����쐬
		IProblem problem = new BitCountProblem();
		// 2�悷��悤�ɂׂ���ϊ���ݒ�
		problem.addFunction(new PowerFunction(2));
		// ����ݒ�
		FitnessManager.setProblem(problem);
		for (Individual individual : pop.getIndivList()) {
			// �S�̂̈�`�q�^�ƓK���x��\��
			System.out.println(individual + " " + individual.fitness());
		}
		// �K���x���\��
		HashMap result = PopulationAnalyzer.fitnessInfo(pop);
		System.out.println("Fitness info max:" + result.get("max") 
				+ " mean:" + result.get("mean") + " min:" + result.get("min"));
		// ��`�q�^�̎�ޕ\��
		System.out.println("genotype num:" + PopulationAnalyzer.genotypeNum(pop));
		// �G���g���s�[�\��
		System.out.println("entropy:" + PopulationAnalyzer.entropy(pop));
	}

}
