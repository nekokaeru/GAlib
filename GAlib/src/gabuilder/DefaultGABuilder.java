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

package gabuilder;

import fitness.FitnessManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import operator.AbstractCrossover;
import operator.AbstractMutation;
import operator.AbstractSelection;
import operator.Elitism;
import operator.IOperator;
import operator.OperatorFactory;
import operator.transform.IArrayTransform;
import operator.transform.TransformFactory;
import population.Population;
import problem.IProblem;
import problem.ProblemFactory;
import problem.function.FunctionFactory;
import problem.function.IFunction;
import util.LocalTestException;
import util.MyRandom;
import viewer.IViewer;
import viewer.TextViewer;
import viewer.ViewerFactory;
/**
 * �f�t�H���g�� GA �r���_�DSGA ���쐬����D
 * @author mori
 * @version 1.0
 */
public class DefaultGABuilder implements IGABuilder {
	/**
	 * �쐬���� GABase
	 */
	protected GABase ga_;

	/**
	 * GA�p�����[�^
	 */
	protected GAParameters params_;

	/**
	 * ��������GA��Ԃ�.
	 * @return GABase
	 */
	public GABase buildGA() {
		HashMap<String, String> map = params_.getParametersMap();
		// �V�[�h�̐ݒ�́C�ŗD��ł���D
		setSeed(map);
		ga_ = new GABase();
		// ���̐ݒ�
		setProblem(map);
		// �̌Q�̐ݒ�
		setPopulation(map);
		// ��`���Z�q�̐ݒ�
		ArrayList<IOperator> list = new ArrayList<IOperator>();
		list.add(makeCrossover(map));
		list.add(makeMutation(map));
		list.add(makeSelection(map));
		// �G���[�g�ۑ��t���O���ݒ肳��Ă���΃G���[�g�ۑ��ǉ��D
		if (Boolean.valueOf(map.get(GAParameters.IS_ELITISM))) {
			list.add(new Elitism());
		}
		ga_.setOperatorList(list);
		// ���㐔�̐ݒ�
		setGenerationSize(map);
		// Viewer �̐ݒ�D
		ga_.addGenerationViewer(makeViewer(map));
		// �������t���O�ƌ̃������t���O�̐ݒ�
		setTurboAndMemoryFlag(map);
		return ga_;
	}

	/**
	 * �V�[�h�̐ݒ�D
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 * @return �V�[�h���ݒ�ł������H
	 */
	protected boolean setSeed(HashMap<String, String> map) {
		boolean result = false;
		if (map.get(GAParameters.SEED) != null) {
			long seed = Long.parseLong(map.get(GAParameters.SEED));
			result = MyRandom.setSeed(seed);
		}
		return result;
	}

	/**
	 * ���ݒ�
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 */
	protected void setProblem(HashMap<String, String> map) {
		IProblem problem = ProblemFactory.createProblem(map
				.get(GAParameters.PROBLEM));
		if (map.get(GAParameters.PROBLEM_PARAMETER) != null) {
			// �����̃p�����[�^�� : �Ŏd�؂���D
			Object[] params = map.get(GAParameters.PROBLEM_PARAMETER)
					.split(":");
			problem.setParameter(params);
		}
		// Function �̐ݒ�D�ŏ�����聨�ő剻���Ȃǂ̕ϊ�������D
		if (map.get(GAParameters.FUNCTION) != null) {
			String functionName = map.get(GAParameters.FUNCTION);
			// ���ŗp����֐������D
			IFunction function = FunctionFactory.createFunction(functionName);
			// Function �p�̃p�����[�^�ݒ�
			if (map.get(GAParameters.FUNCTION_PARAMETER) != null) {
				// �����̃p�����[�^�� : �Ŏd�؂���D
				Object[] params = map.get(GAParameters.FUNCTION_PARAMETER)
						.split(":");
				function.setParameter(params);
			}
			// ���� function ��ǉ�
			problem.addFunction(function);
		}
		FitnessManager.setProblem(problem);
	}

	/**
	 * �̌Q�̐ݒ�
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 */
	protected void setPopulation(HashMap<String, String> map) {
		int popSize = Integer.parseInt(map.get(GAParameters.POPULATION_SIZE));
		int chromosomeLength = Integer.parseInt(map
				.get(GAParameters.CHROMOSOME_LENGTH));
		Population pop = new Population(popSize, chromosomeLength);
		ga_.setPopulation(pop);
	}

	/**
	 * �K�؂Ƀp�����[�^���ݒ肳�ꂽ����������ĕԂ��D
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 * @return crossover
	 */
	protected AbstractCrossover makeCrossover(HashMap<String, String> map) {
		// �����ݒ�D �����ȊO���ݒ肳��Ă���C�L���X�g�Ɏ��s����Ɨ�O�����D
		AbstractCrossover crossover = (AbstractCrossover) OperatorFactory
				.createOperator(map.get(GAParameters.CROSSOVER));
		// GAParameters.C_PARAM �ɂ������p�����[�^�ݒ�
		if (map.get(GAParameters.C_PARAM) != null) {
			Object[] params = map.get(GAParameters.C_PARAM).split(":");
			crossover.setParameter(params);
		}
		return crossover;
	}

	/**
	 * �K�؂Ƀp�����[�^���ݒ肳�ꂽ�ˑR�ψق�����ĕԂ��D
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 * @return mutation
	 */
	protected AbstractMutation makeMutation(HashMap<String, String> map) {
		// �ˑR�ψِݒ�D�ˑR�ψوȊO���ݒ肳��Ă���C�L���X�g�Ɏ��s����Ɨ�O�����D
		AbstractMutation mutation = (AbstractMutation) OperatorFactory
				.createOperator(map.get(GAParameters.MUTATION));
		// GAParameters.M_PARAM �ɂ��ˑR�ψكp�����[�^�ݒ�
		if (map.get(GAParameters.M_PARAM) != null) {
			Object[] params = map.get(GAParameters.M_PARAM).split(":");
			mutation.setParameter(params);
		}
		return mutation;
	}

	/**
	 * �K�؂Ƀp�����[�^���ݒ肳�ꂽ�I��������ĕԂ��D�X�P�[�����O�֐����ݒ肳���D
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 * @return selection
	 */

	protected AbstractSelection makeSelection(HashMap<String, String> map) {
		// �I��ݒ�D�I���ȊO���ݒ肳��Ă���C�L���X�g�Ɏ��s����Ɨ�O�����D
		AbstractSelection selection = (AbstractSelection) OperatorFactory
				.createOperator(map.get(GAParameters.SELECTION));
		// DefaultGABuilder �ł��M�͊w�I�I�����g�����Ƃ��\�Ƃ��邽�߂̏���
		if(selection.getName().equals("ThermoDynamicalSelection")){
			ArrayList<String> paramsList = new ArrayList<String>();
			// ThermoDynamicalSelection �ł͌̐��ƈ�`�q����K���w�肷��K�v�L��D
			paramsList.add(map.get(GAParameters.POPULATION_SIZE));
			paramsList.add(map.get(GAParameters.CHROMOSOME_LENGTH));
			// S_PARAM �ɂ��I���p�����[�^�ݒ�. S_PARAM �͉��x�݂̂̏ꍇ�Ɖ��x�ƌ̌Q���k�t���O�̏ꍇ������D
			// ���k�t���O�͏ȗ��D��: -Sparam 3 (���x 3�C�t���O false) -Sparam 3:true (���x 3�C�t���O true) 
			if (map.get(GAParameters.S_PARAM) != null) {
				for (String object : map.get(GAParameters.S_PARAM).split(":")) {
					paramsList.add(object);
				}				
			}
			selection.setParameter(paramsList.toArray());
		}else{ // �ʏ�̑I���D
			// S_PARAM �ɂ��I���p�����[�^�ݒ�
			if (map.get(GAParameters.S_PARAM) != null) {
				Object[] params = map.get(GAParameters.S_PARAM).split(":");
				selection.setParameter(params);
			}
		}
		
		// �I�����̃X�P�[�����O�̐ݒ�
		if (map.get(GAParameters.TRANSFORM) != null) {
			String transformName = map.get(GAParameters.TRANSFORM);
			// �I���ŗp����X�P�[�����O�����D
			IArrayTransform transform = TransformFactory
					.createTransform(transformName);
			// IArrayTransform �p�̃p�����[�^�ݒ�
			if (map.get(GAParameters.TRANSFORM_PARAMETER) != null) {
				// �����̃p�����[�^�� : �Ŏd�؂���D
				Object[] params = map.get(GAParameters.TRANSFORM_PARAMETER)
						.split(":");
				transform.setParameter(params);
			}
			// �I���� transform ��ǉ�
			selection.addTransform(transform);
		}
		return selection;
	}

	/**
	 * ���㐔�ݒ�
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 */
	protected void setGenerationSize(HashMap<String, String> map) {
		int gsize = Integer.parseInt(map.get(GAParameters.GENERATION_SIZE));
		ga_.setGenerationSize(gsize);
	}

	/**
	 * �r���[���̐ݒ�
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 */
	protected IViewer makeViewer(HashMap<String, String> map) {
		IViewer v = ViewerFactory.createViewer(map.get(GAParameters.VIEWER));
		int printLevel = Integer.parseInt(map.get(GAParameters.PRINT_LEVEL));
		v.setPrintLevel(printLevel);
		if (map.get(GAParameters.OUTPUT_FILE).equalsIgnoreCase("stdout")) {
			v.setFilename("STDOUT");
		} else if (map.get(GAParameters.OUTPUT_FILE).equalsIgnoreCase("stderr")) {
			v.setFilename("STDERR");
		} else {
			v.setFilename(map.get(GAParameters.OUTPUT_FILE));
		}
		return v;
	}

	/**
	 * �������t���O�ƌ̃������t���O�̐ݒ�
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 */
	protected void setTurboAndMemoryFlag(HashMap<String, String> map) {
		boolean isTurbo = Boolean.parseBoolean(map.get(GAParameters.IS_TURBO));
		boolean isMemory = Boolean
				.parseBoolean(map.get(GAParameters.IS_MEMORY));
		FitnessManager.setTurbo(isTurbo);
		FitnessManager.setMemory(isMemory);
	}

	/**
	 * GA�p�����[�^��ݒ�D�V�[�h�̐ݒ���s���D
	 * @param params GA�p�����[�^
	 */
	public void setGAParameters(GAParameters params) {
		params_ = params;
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		long t1, t2;
		t1 = System.currentTimeMillis();
		DefaultGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		d.setParameter(args);
		d.constract();
		GABase ga = builder.buildGA();
		ga.start();
		t2 = System.currentTimeMillis();
		System.out.println("time:" + (t2 - t1));
		try {
			builder.localTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���[�J���e�X�g
	 * @throws LocalTestException
	 */
	public void localTest() throws LocalTestException {
		GAParameters params = new GAParameters();
		HashMap<String, String> map = params.getParametersMap();
		ga_ = new GABase();

		// setSeed �̃e�X�g�D
		MyRandom.reset();
		map.put(GAParameters.SEED, "3");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 3) {
			throw new LocalTestException("seed != 3");
		}
		// �V�[�h�� getInstance() ����܂ł͕ύX�D
		map.put(GAParameters.SEED, "8");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 8) {
			throw new LocalTestException("seed != 8");
		}
		// �V�[�h��8�Ŋm��D
		MyRandom.getInstance();
		map.put(GAParameters.SEED, "4");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 8) {
			throw new LocalTestException("seed != 8");
		}
		// setProblem �̃e�X�g
		map.put(GAParameters.PROBLEM, "problem.KnapsackProblem");
		map.put(GAParameters.PROBLEM_PARAMETER, "100");
		map.put(GAParameters.FUNCTION, "problem.function.LinearFunction");
		map.put(GAParameters.FUNCTION_PARAMETER, "-2:1.23");
		setProblem(map);
		if (!FitnessManager.getProblem().toString().equals(
				"Knapsack{itemNum:100,Linear{GRADIENT:-2.0,INTERCEPT:1.23}}")) {
			throw new LocalTestException(FitnessManager.getProblem().toString()
					+ " is wrong!");
		}
		Number[] indiv = new Number[100];
		Arrays.fill(indiv, 0);
		indiv[0] = 1;
		// 100�ו��i�b�v�T�b�N���̎n�߂̉ו��̉��l��68
		if (FitnessManager.getProblem().getObjectiveFunctionValue(indiv) != 68) {
			throw new LocalTestException(FitnessManager.getProblem()
					.getObjectiveFunctionValue(indiv)
					+ "!=68");
		}
		// ���`�X�P�[�����O��̒l
		if (FitnessManager.getProblem().getFitness(indiv) != 68 * (-2.0) + 1.23) {
			throw new LocalTestException(FitnessManager.getProblem()
					.getFitness(indiv)
					+ "!=68*(-2.0)+1.23=-134.77");
		}

		// setPopulation �̃e�X�g
		map.put(GAParameters.POPULATION_SIZE, "13"); // �̐�13
		map.put(GAParameters.CHROMOSOME_LENGTH, "17"); // ��`�q��17
		setPopulation(map);
		// �̐�
		if (ga_.getPopulation().getPopulationSize() != 13) {
			throw new LocalTestException(ga_.getPopulation()
					.getPopulationSize()
					+ "!= 13");
		}
		// ��`�q��
		if (ga_.getPopulation().getIndividualAt(0).size() != 17) {
			throw new LocalTestException(ga_.getPopulation().getIndividualAt(0)
					.size()
					+ "!= 17");
		}

		// makeCrossover �̃e�X�g
		map.put(GAParameters.CROSSOVER, "operator.UniformCrossover"); // ��l����
		map.put(GAParameters.C_PARAM, "0.99"); // ������0.99
		AbstractCrossover crossover = makeCrossover(map);
		if (!crossover.toString().equals(
				"UniformCrossover{CROSSOVER_RATE:0.99}")) {
			throw new LocalTestException(crossover.toString() + " is wrong!");
		}

		// makeMutation �̃e�X�g
		map.put(GAParameters.MUTATION, "operator.BitMutation"); // �r�b�g�ˑR�ψ�
		map.put(GAParameters.M_PARAM, "0.98"); // �ˑR�ψٗ�0.98
		AbstractMutation mutation = makeMutation(map);
		if (!mutation.toString().equals("BitMutation{MUTATION_RATE:0.98}")) {
			throw new LocalTestException(mutation.toString() + " is wrong!");
		}

		// makeSelection �̃e�X�g
		map.put(GAParameters.SELECTION, "operator.TournamentSelection"); // �g�[�i�����g�I��
		map.put(GAParameters.S_PARAM, "7"); // �g�[�i�����g�T�C�Y 7
		map.put(GAParameters.TRANSFORM,
				"operator.transform.LinearScalingTransform"); // �g�[�i�����g�I��
		map.put(GAParameters.TRANSFORM_PARAMETER, "2.1:-3.2"); // �g�[�i�����g�T�C�Y 7
		AbstractSelection selection = makeSelection(map);
		if (!selection
				.toString()
				.equals(
						"TournamentSelection{TOURNAMENT_SIZE:7,LinearScalingTransform{TOP_VALUE:2.1, BOTTOM_VALUE:-3.2}}")) {
			throw new LocalTestException(selection.toString() + " is wrong!");
		}

		// setGenerationSize �̃e�X�g
		map.put(GAParameters.GENERATION_SIZE, "123"); // ���㐔123
		setGenerationSize(map);
		if (ga_.getGenerationSize() != 123) {
			throw new LocalTestException(ga_.getGenerationSize() + "!=123");
		}
		// makeViewer �̃e�X�g
		map.put(GAParameters.OUTPUT_FILE, ""); // �󕶎���͕W���o��
		TextViewer v = (TextViewer) makeViewer(map);
		if (v.getPrintLevel() != GAParameters.DEFAULT_PRINT_LEVEL) {
			throw new LocalTestException(v.getPrintLevel() + "!="
					+ GAParameters.DEFAULT_PRINT_LEVEL);
		}
		if (v.getOut() != System.out) {
			throw new LocalTestException("v.getOut() != System.out");
		}
		if (!v.getFilename().equals("")) {
			throw new LocalTestException(v.getFilename() + "!= \"\"");
		}

		map.put(GAParameters.OUTPUT_FILE, "STDERR"); // �W���G���[�o��
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.err) {
			throw new LocalTestException("v.getOut() != System.err");
		}
		if (!v.getFilename().equals("STDERR")) {
			throw new LocalTestException(v.getFilename() + "!= STDERR");
		}

		map.put(GAParameters.OUTPUT_FILE, "STDOUT"); // �W���o��
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.out) {
			throw new LocalTestException("v.getOut() != System.out");
		}
		if (!v.getFilename().equals("STDOUT")) {
			throw new LocalTestException(v.getFilename() + "!= STDOUT");
		}

		map.put(GAParameters.OUTPUT_FILE, "__test__.dat");
		map.put(GAParameters.PRINT_LEVEL, "3");
		v = (TextViewer) makeViewer(map);
		if (v.getPrintLevel() != 3) {
			throw new LocalTestException(v.getPrintLevel() + "!=3");
		}
		if (!v.getFilename().equals("__test__.dat")) {
			throw new LocalTestException(v.getFilename() + "!= __test__.dat");
		}
		// setTurboAndMemoryFlag �̃e�X�g
		map.put(GAParameters.IS_TURBO, "true");
		map.put(GAParameters.IS_MEMORY, "true");
		setTurboAndMemoryFlag(map);
		if (!FitnessManager.isTurbo()) {
			throw new LocalTestException("FitnessManager.isTurbo() != true");
		}
		if (!FitnessManager.isMemory()) {
			throw new LocalTestException("FitnessManager.isMemory() != true");
		}

		// getGA �̃e�X�g�DlocalTest �̕K�v�͂Ȃ����܊p�Ȃ̂�...
		setGAParameters(params);
		if (!buildGA()
				.getOperatorNameList()
				.toString()
				.equals(
						"[UniformCrossover, BitMutation, TournamentSelection, Elitism]")) {
			throw new LocalTestException(ga_.getOperatorNameList()
					+ "is wrong!");
		}
	}
}
