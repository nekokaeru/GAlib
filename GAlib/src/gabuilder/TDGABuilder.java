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
import java.util.HashMap;

import operator.AbstractCrossover;
import operator.AbstractMutation;
import operator.AbstractSelection;
import operator.Dummy;
import operator.IOperator;
import operator.Merge;
import operator.ThermoDynamicalSelection;
import operator.transform.IArrayTransform;
import operator.transform.TransformFactory;
import util.LocalTestException;
import util.MyRandom;
import viewer.TextViewer;
/**
 * TDGA ���쐬���� GA �r���_�D
 * @author mori
 * @version 1.0
 */
public class TDGABuilder extends DefaultGABuilder {
	/**
	 * ��������TDGA��Ԃ�.
	 * @return GABase
	 */
	@Override
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
		Merge merge = new Merge();
		merge.getOperators().add(new Dummy());
		merge.getOperators().add(makeCrossover(map));
		list.add(merge);
		list.add(makeMutation(map));
		list.add(makeSelection(map));
		// �G���[�g�ۑ��͐ݒ肷��K�v�Ȃ��D
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
	 * �K�؂Ƀp�����[�^���ݒ肳�ꂽ�I��������ĕԂ��D�X�P�[�����O�֐����ݒ肳���D
	 * @param map �p�����[�^�ۑ��p�n�b�V���}�b�v
	 * @return selection
	 */
	@Override
	protected AbstractSelection makeSelection(HashMap<String, String> map) {
		// �I��ݒ�DTDGA �͕K���M�͊w�I�I���D
		AbstractSelection selection = new ThermoDynamicalSelection();
		// TDGA �ł͌̐��ƈ�`�q���𖾎��I�ɐݒ肷��K�v������D
		ArrayList<String> paramsList = new ArrayList<String>();
		paramsList.add(map.get(GAParameters.POPULATION_SIZE));
		paramsList.add(map.get(GAParameters.CHROMOSOME_LENGTH));
		// S_PARAM �ɂ��I���p�����[�^�ݒ�. S_PARAM �͉��x�݂̂̏ꍇ�Ɖ��x�ƌ̌Q���k�t���O�̏ꍇ������D
		// ���k�t���O�͏ȗ��D��: -Sparam 3 (���x 3�C�t���O false) -Sparam 3:true (���x 3�C�t���O true) 
		if (map.get(GAParameters.S_PARAM) != null) {
			for (String object : map.get(GAParameters.S_PARAM).split(":")) {
				paramsList.add(object);
			}
		}
		// �쐬�������X�g��z�񉻂��ēn���D
		selection.setParameter(paramsList.toArray());
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
	 * ���s��
	 */
	public static void main(String[] args) {
		long t1, t2;
		t1 = System.currentTimeMillis();
		TDGABuilder builder = new TDGABuilder();
		Director d = new Director(builder);
		d.setParameter(args);
		d.constract();
		GABase ga = builder.buildGA();
		ga.addOperationViewer(new TextViewer());
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
	@Override
	public void localTest() throws LocalTestException {
		GAParameters params = new GAParameters();
		HashMap<String, String> map = params.getParametersMap();
		ga_ = new GABase();

		// setSeed �̃e�X�g
		MyRandom.reset();
		map.put(GAParameters.SEED, "-1");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != -1) {
			throw new LocalTestException("seed != -1");
		}
		// �V�[�h�� getInstance() ����܂ł͕ύX��
		map.put(GAParameters.SEED, "100");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 100) {
			throw new LocalTestException("seed != 100");
		}
		// �V�[�h��100�Ŋm��D
		MyRandom.getInstance();
		map.put(GAParameters.SEED, "100");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 100) {
			throw new LocalTestException("seed != 100");
		}
		// setProblem �̃e�X�g. �f�t�H���g�̃r�b�g�J�E���g���Ă΂��D
		setProblem(map);
		if (!FitnessManager.getProblem().toString().equals("BitCount{}")) {
			throw new LocalTestException(FitnessManager.getProblem().toString()
					+ " is wrong!");
		}
		// setPopulation �̃e�X�g
		map.put(GAParameters.POPULATION_SIZE, "14"); // �̐�14
		map.put(GAParameters.CHROMOSOME_LENGTH, "12"); // ��`�q��12
		setPopulation(map);
		// �̐�
		if (ga_.getPopulation().getPopulationSize() != 14) {
			throw new LocalTestException(ga_.getPopulation()
					.getPopulationSize()
					+ "!= 14");
		}
		// ��`�q��
		if (ga_.getPopulation().getIndividualAt(0).size() != 12) {
			throw new LocalTestException(ga_.getPopulation().getIndividualAt(0)
					.size()
					+ "!= 12");
		}

		// makeCrossover �̃e�X�g
		map.put(GAParameters.CROSSOVER, "operator.OnePointCrossover"); // ��l����
		map.put(GAParameters.C_PARAM, "0.1"); // ������0.1
		AbstractCrossover crossover = makeCrossover(map);
		if (!crossover.toString().equals(
				"OnePointCrossover{CROSSOVER_RATE:0.1}")) {
			throw new LocalTestException(crossover.toString() + " is wrong!");
		}

		// makeMutation �̃e�X�g
		map.put(GAParameters.MUTATION, "operator.BitMutation"); // �r�b�g�ˑR�ψ�
		map.put(GAParameters.M_PARAM, "0.89"); // �ˑR�ψٗ�0.98
		AbstractMutation mutation = makeMutation(map);
		if (!mutation.toString().equals("BitMutation{MUTATION_RATE:0.89}")) {
			throw new LocalTestException(mutation.toString() + " is wrong!");
		}

		// makeSelection �̃e�X�g. �K���M�͊w�I�I�����ݒ肳���D
		map.put(GAParameters.SELECTION, "operator.TournamentSelection"); // �g�[�i�����g�I��
		map.put(GAParameters.S_PARAM, "0.01"); // ���x
		AbstractSelection selection = makeSelection(map);
		if (!selection.toString().equals(
				"ThermoDynamicalSelection{TEMPERATURE:0.01,isPackIndivList:false}")) {
			throw new LocalTestException(selection.toString() + " is wrong!");
		}
		
		map.put(GAParameters.SELECTION, "operator.TournamentSelection"); // �g�[�i�����g�I��
		map.put(GAParameters.S_PARAM, "0.1:true"); // ���x�ƌ̈��k�t���O
		selection = makeSelection(map);
		if (!selection.toString().equals(
				"ThermoDynamicalSelection{TEMPERATURE:0.1,isPackIndivList:true}")) {
			throw new LocalTestException(selection.toString() + " is wrong!");
		}
		

		// setGenerationSize �̃e�X�g
		map.put(GAParameters.GENERATION_SIZE, "111"); // ���㐔111
		setGenerationSize(map);
		if (ga_.getGenerationSize() != 111) {
			throw new LocalTestException(ga_.getGenerationSize() + "!=111");
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
		map.put(GAParameters.OUTPUT_FILE, "stdout"); // �W���o��		
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.out) {
			throw new LocalTestException("v.getOut() != System.out");
		}
		if (!v.getFilename().equals("STDOUT")) {
			throw new LocalTestException(v.getFilename() + "!= STDOUT");
		}
		map.put(GAParameters.OUTPUT_FILE, "sTDERR"); // �W���G���[�o��		
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.err) {
			throw new LocalTestException("v.getOut() != System.err");
		}
		if (!v.getFilename().equals("STDERR")) {
			throw new LocalTestException(v.getFilename() + "!= STDERR");
		}		

		map.put(GAParameters.OUTPUT_FILE, "__test2__.dat");
		map.put(GAParameters.PRINT_LEVEL, "-1");
		v = (TextViewer) makeViewer(map);
		if (v.getPrintLevel() != -1) {
			throw new LocalTestException(v.getPrintLevel() + "!=-1");
		}
		if (!v.getFilename().equals("__test2__.dat")) {
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
		if (!buildGA().getOperatorNameList().toString().equals(
				"[Merge, BitMutation, ThermoDynamicalSelection]")) {
			throw new LocalTestException(ga_.getOperatorNameList()
					+ "is wrong!");
		}

		if (!ga_.getOperatorList().get(0).toString().equals(
				"Merge{Dummy{},OnePointCrossover{CROSSOVER_RATE:0.1}}")) {
			throw new LocalTestException(ga_.getOperatorList().get(0)
					+ "is wrong!");
		}
	}
}