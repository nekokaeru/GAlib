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
 * Created on 2005/05/11
 * GABase �p�����[�^�p���[�e�B���e�B�N���X�D
 */
package gabuilder;

import java.util.HashMap;
import java.util.HashSet;

/**
 * GABase �p�����[�^�p�N���X�D �g�p���� new ���邱�ƁD
 * @author mori
 * @version 1.0
 */
public class GAParameters {
	/** �̐��̏����l */
	public static final int DEFAULT_POPULATION_SIZE = 100;

	/** ���㐔�̏����l */
	public static final int DEFAULT_GENERATION_SIZE = 100;

	/** ��`�q���̏����l */
	public static final int DEFAULT_CHROMOSOME_LENGTH = 10;

	/** �ˑR�ψٗ��̏����l */
	public static final double DEFAULT_MUTATION_RATE = 0.01;

	/** �������̏����l */
	public static final double DEFAULT_CROSSOVER_RATE = 0.6;

	/** �g�[�i�����g�T�C�Y�̏����l */
	public static final int DEFAULT_TOURNAMENT_SIZE = 3;

	/** TDGA���x�̏����l */
	public static final double DEFAULT_TEMPERATURE = 1;

	/** ���̏����l */
	public static final String DEFAULT_PROBLEM = "problem.BitCountProblem";

	/** �I���̏����l */
	public static final String DEFAULT_SELECTION = "operator.RouletteSelection";

	/** �����̏����l */
	public static final String DEFAULT_CROSSOVER = "operator.OnePointCrossover";

	/** �ˑR�ψق̏����l */
	public static final String DEFAULT_MUTATION = "operator.BitMutation";

	/** �r���[���̏����l */
	public static final String DEFAULT_VIEWER = "viewer.TextViewer";

	/** �\�����x���̏����l */
	public static final int DEFAULT_PRINT_LEVEL = 1;

	/** �������t���O�̏����l */
	public static final boolean DEFAULT_IS_TURBO = false;

	/** �̃������t���O�̏����l */
	public static final boolean DEFAULT_IS_MEMORY = false;

	/** �V�[�h�̏����l */
	public static final String DEFAULT_SEED = null;


	/** HashMap �̃L�[:  �I�� */
	public static final String SELECTION = "SELECTION";

	/** HashMap �̃L�[:  ���� */
	public static final String CROSSOVER = "CROSSOVER";

	/** HashMap �̃L�[:  �ˑR�ψ� */
	public static final String MUTATION = "MUTATION";

	/** HashMap �̃L�[:  �I���p�����[�^ */
	public static final String S_PARAM = "S_PARAM";

	/** HashMap �̃L�[:  �����p�����[�^ */
	public static final String C_PARAM = "C_PARAM";

	/** HashMap �̃L�[:  �ˑR�ψكp�����[�^ */
	public static final String M_PARAM = "M_PARAM";

	/** HashMap �̃L�[:  �̐� */
	public static final String POPULATION_SIZE = "POPULATION_SIZE";

	/** HashMap �̃L�[:  �ړI�֐��l�Ŏg���ϊ��֐� */
	public static final String FUNCTION = "FUNCTION";

	/** HashMap �̃L�[:  �ړI�֐��l�Ŏg���ϊ��֐��p�����[�^ */
	public static final String FUNCTION_PARAMETER = "FUNCTION_PARAMETER";

	/** HashMap �̃L�[:  �I���Ŏg���X�P�[�����O */
	public static final String TRANSFORM = "TRANSFORM";

	/** HashMap �̃L�[:  �I���Ŏg���X�P�[�����O�p�����[�^ */
	public static final String TRANSFORM_PARAMETER = "TRANSFORM_PARAMETER";

	/** HashMap �̃L�[:  ��� */
	public static final String PROBLEM = "PROBLEM";

	/** HashMap �̃L�[:  ���p�����[�^ */
	public static final String PROBLEM_PARAMETER = "PROBLEM_PARAMETER";

	/** HashMap �̃L�[:  ���㐔 */
	public static final String GENERATION_SIZE = "GENERATION_SIZE";

	/** HashMap �̃L�[:  ��`�q�� */
	public static final String CHROMOSOME_LENGTH = "CHROMOSOME_LENGTH";

	/** HashMap �̃L�[:  �v�����g���x�� */
	public static final String PRINT_LEVEL = "PRINT_LEVEL";

	/** HashMap �̃L�[:  �o�̓t�@�C���� */
	public static final String OUTPUT_FILE = "OUTPUT_FILE";

	/** HashMap �̃L�[:  �G���[�g��`�̗L�� */
	public static final String IS_ELITISM = "IS_ELITISM";

	/** HashMap �̃L�[:  �r���[�� */
	public static final String VIEWER = "VIEWER"; // viewer.TextViewer

	/** HashMap �̃L�[:  �������t���O */
	public static final String IS_TURBO = "IS_TURBO";

	/** HashMap �̃L�[:  �̃������t���O */
	public static final String IS_MEMORY = "IS_MEMORY";

	/** HashMap �̃L�[:  �����̃V�[�h */
	public static final String SEED = "SEED";

	/**
	 * �p�����[�^�ۑ��p�n�b�V���}�b�v 
	 */
	private HashMap<String, String> parametersMap_;

	/**
	 * �w��\�ȃI�v�V�����W��
	 */
	private HashSet<String> regalOption_;

	/**
	 * �p�����[�^�ۑ��p�n�b�V���}�b�v��Ԃ�.
	 * @return �p�����[�^�ۑ��p�̃n�b�V���}�b�v
	 */
	public final HashMap<String, String> getParametersMap() {
		if (parametersMap_ == null) {
			// parametersMap_�̏�����
			parametersMap_ = new HashMap<String, String>();
			parametersMap_.put(SEED, DEFAULT_SEED); // �V�[�h
			parametersMap_.put(SELECTION, DEFAULT_SELECTION);
			parametersMap_.put(CROSSOVER, DEFAULT_CROSSOVER);
			parametersMap_.put(MUTATION, DEFAULT_MUTATION);
			parametersMap_.put(POPULATION_SIZE, Integer
					.toString(DEFAULT_POPULATION_SIZE));
			parametersMap_.put(FUNCTION, null);// �ړI�֐��Ŏg���ϊ��֐� IProblem �Őݒ�->problem.function
			parametersMap_.put(FUNCTION_PARAMETER, null);// �ړI�֐��Ŏg���ϊ��̃p�����[�^
			parametersMap_.put(TRANSFORM, null);// �I���Ŏg���X�P�[�����O ->operator.transform
			parametersMap_.put(TRANSFORM_PARAMETER, null);// �I���Ŏg���X�P�[�����O�̃p�����[�^
			parametersMap_.put(PROBLEM, DEFAULT_PROBLEM);// ���
			parametersMap_.put(PROBLEM_PARAMETER, null);// ���p�����[�^
			parametersMap_.put(GENERATION_SIZE, Integer
					.toString(DEFAULT_GENERATION_SIZE));// ���㐔
			parametersMap_.put(CHROMOSOME_LENGTH, Integer
					.toString(DEFAULT_CHROMOSOME_LENGTH));// ��`�q��
			parametersMap_
					.put(PRINT_LEVEL, String.valueOf(DEFAULT_PRINT_LEVEL));// �v�����g���x��
			parametersMap_.put(OUTPUT_FILE, "STDOUT");// �o�̓t�@�C��. STDOUT �͕W���o��, STDERR �͕W���G���[�o��
			parametersMap_.put(IS_ELITISM, "true");// �G���[�g
			parametersMap_.put(VIEWER, DEFAULT_VIEWER);// �r���[��
			parametersMap_.put(IS_TURBO, String.valueOf(DEFAULT_IS_TURBO));// �������t���O
			parametersMap_.put(IS_MEMORY, String.valueOf(DEFAULT_IS_MEMORY));// �̃������t���O
		}
		return parametersMap_;
	}

	/**
	 * �I�v�V������Ԃ�.
	 * @return �I�v�V����
	 */
	public final HashSet<String> getRegalOption() {
		if (regalOption_ == null) {
			regalOption_ = new HashSet<String>();
			// �w���v�\��
			regalOption_.add("-h");
			regalOption_.add("-help");
			regalOption_.add("-S"); // �I���w��
			regalOption_.add("-C"); // �����w��
			regalOption_.add("-M"); // �ˑR�ψَw��
			regalOption_.add("-Sparam");// �I���p�����[�^
			regalOption_.add("-Cparam");// �����p�����[�^
			regalOption_.add("-Mparam");// �ˑR�ψكp�����[�^
			regalOption_.add("-scaling1");// �ړI�֐��Ŏg���ϊ��֐� -> problem.function
			regalOption_.add("-scaling1param");// �ړI�֐��Ŏg���ϊ��֐��p�����[�^
			regalOption_.add("-scaling2");// �I���Ŏg���X�P�[�����O -> operator.transform
			regalOption_.add("-scaling2param");// �I���Ŏg���X�P�[�����O�p�����[�^
			regalOption_.add("-P");// ���
			regalOption_.add("-Pparam");// ���p�����[�^
			regalOption_.add("-popsize");// �̐�
			regalOption_.add("-gsize");// ���㐔
			regalOption_.add("-clength");// ��`�q��
			regalOption_.add("-printlevel");// �v�����g���x��
			regalOption_.add("-output");// �o�̓t�@�C��
			regalOption_.add("-elitism");// �G���[�g��`
			regalOption_.add("-viewer");// �r���[��
			regalOption_.add("-turbo");// �������t���O
			regalOption_.add("-memory");// �̃������t���O
			regalOption_.add("-seed");// �V�[�h
		}
		return regalOption_;
	}
}
