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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import util.LocalTestException;

/**
 * Builder�p�^�[���� Director.
 * @author mori
 * @version 1.0
 */
public class Director {
	/**
	 * �r���_
	 */
	private IGABuilder builder_;

	/**
	 * GAParameters �ւ̎Q��
	 */
	private GAParameters gaParams_;

	/**
	 * �r���_�ŏ�����
	 * @param builder �r���_
	 */
	public Director(IGABuilder builder) {
		builder_ = builder;
	}

	/**
	 * �r���_�Ɋe�p�����[�^��n��.
	 */
	public void constract() {
		builder_.setGAParameters(getGAParams());
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		// �܂��r���_��錾�D
		IGABuilder builder = new DefaultGABuilder();
		// �r���_�������Ƃ��ăf�B���N�^���������D
		Director d = new Director(builder);
		// �p�����[�^���Z�b�g�D
		d.setParameter(args);
		// �ȉ������s����� builder ���K�؂ɐݒ肳���D
		d.constract();
		// �r���_�̍���� GA �����炤�D
		GABase ga=builder.buildGA();
		try {
			ga.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * �p�����[�^�ݒ�p�������� GAParameters �� parametersMap �����D
	 * @param params �p�����[�^���X�g�D-{�L��} �l �̌`�Őݒ肷��D
	 */
	public void setParameter(String[] params) {
		// null �̏ꍇ�͉����Ȃ��ƍl����D
		if (params == null) {
			return;
		}
		// �܂��C-h �� -help ���������ꍇ�ɂ� printHelp() ���Ă�ŏI���D
		if (Arrays.binarySearch(params, "-h") >= 0
				|| Arrays.binarySearch(params, "-help") >= 0) {
			printHelp();
			System.exit(-1);
		}

		try {
			// �p�����[�^������ł���΃t�@�C���ɂ��ݒ�Ƃ݂Ȃ��D
			if (params.length == 1) {
				setGAParamsByCSVFile(params[0]);
				return;
			}
			// �p�����[�^������ȊO�Ŋ�ł���΃G���[�D
			if (params.length % 2 != 0) {
				throw new Exception("�����̐��������ł͂���܂���D -h �Ńw���v���\������܂��D");
			}
			// params ���� 2 �����o���Cresult �ɓ���Ă����D
			for (int i = 0; i < params.length; i += 2) {
				// �n�C�t���t���̕����D
				String hyphenPart = params[i];
				String val = params[i + 1];
				if (hyphenPart.charAt(0) != '-') {
					printHelp();
					throw new Exception(hyphenPart
							+ " �Ƀn�C�t�����t���Ă��܂���. -h �Ńw���v���\������܂��D");
				}
				if (!getGAParams().getRegalOption().contains(hyphenPart)) {
					printHelp();
					throw new Exception(hyphenPart
							+ " �͗L���ȃI�v�V�����ł͂���܂���. -h �Ńw���v���\������܂��D");
				}
				addParameter(hyphenPart, val); // �Z�b�g�Œǉ�
			}
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * �p�����[�^�ݒ�� List �Ŏ󂯎��ꍇ�̃��\�b�h�D<br>
	 * �P�� List ��z�񉻂��� setParameter �ɓn�������D
	 * @param params �p���[���^���X�g�D
	 */
	public void setParameter(List<String> params) {
		String[] array = new String[params.size()];
		params.toArray(array);
		// �z��ł̃p�����[�^�ݒ�֐����ĂԁD
		setParameter(array);
	}

	/**
	 * �p�����[�^�̒ǉ�.
	 * @param hyphenPart �L�[
	 * @param val �l
	 */
	private void addParameter(String hyphenPart, String val) {
		// �p�����[�^�}�b�v
		HashMap<String, String> map = getGAParams().getParametersMap();
		// �I��
		if (hyphenPart.equals("-S")) {
			map.put(GAParameters.SELECTION, val);
			// ����
		} else if (hyphenPart.equals("-C")) {
			map.put(GAParameters.CROSSOVER, val);
			// �ˑR�ψ�
		} else if (hyphenPart.equals("-M")) {
			map.put(GAParameters.MUTATION, val);
			// �I���p�����[�^
		} else if (hyphenPart.equals("-Sparam")) {
			map.put(GAParameters.S_PARAM, val);
			// �����p�����[�^
		} else if (hyphenPart.equals("-Cparam")) {
			map.put(GAParameters.C_PARAM, val);
			// �ˑR�ψكp�����[�^
		} else if (hyphenPart.equals("-Mparam")) {
			map.put(GAParameters.M_PARAM, val);
			// �ړI�֐��Ŏg���ϊ��֐�
		} else if (hyphenPart.equals("-scaling1")) {
			map.put(GAParameters.FUNCTION, val);
			// �ړI�֐��Ŏg���ϊ��֐��p�����[�^
		} else if (hyphenPart.equals("-scaling1param")) {
			map.put(GAParameters.FUNCTION_PARAMETER, val);
			// �I���Ŏg���X�P�[�����O
		} else if (hyphenPart.equals("-scaling2")) {
			map.put(GAParameters.TRANSFORM, val);
			// �I���Ŏg���X�P�[�����O�p�����[�^
		} else if (hyphenPart.equals("-scaling2param")) {
			map.put(GAParameters.TRANSFORM_PARAMETER, val);
			// ���
		} else if (hyphenPart.equals("-P")) {
			map.put(GAParameters.PROBLEM, val);
			// ���p�����[�^
		} else if (hyphenPart.equals("-Pparam")) {
			map.put(GAParameters.PROBLEM_PARAMETER, val);
			// �̐�
		} else if (hyphenPart.equals("-popsize")) {
			map.put(GAParameters.POPULATION_SIZE, val);
			// ���㐔
		} else if (hyphenPart.equals("-gsize")) {
			map.put(GAParameters.GENERATION_SIZE, val);
			// ��`�q��
		} else if (hyphenPart.equals("-clength")) {
			map.put(GAParameters.CHROMOSOME_LENGTH, val);
			// �v�����g���x��
		} else if (hyphenPart.equals("-printlevel")) {
			map.put(GAParameters.PRINT_LEVEL, val);
			// �o�̓t�@�C��
		} else if (hyphenPart.equals("-output")) {
			map.put(GAParameters.OUTPUT_FILE, val);
			// �G���[�g�̗L��
		} else if (hyphenPart.equals("-elitism")) {
			map.put(GAParameters.IS_ELITISM, val);
			// �r���[��
		} else if (hyphenPart.equals("-viewer")) {
			map.put(GAParameters.VIEWER, val);
			// �������t���O
		} else if (hyphenPart.equals("-turbo")) {
			map.put(GAParameters.IS_TURBO, val);
			// �̃������t���O
		} else if (hyphenPart.equals("-memory")) {
			map.put(GAParameters.IS_MEMORY, val);
		} else if (hyphenPart.equals("-seed")) {
			map.put(GAParameters.SEED, val);
		} else {
			throw new IllegalArgumentException(hyphenPart + " is wrong!");
		}
	}

	/**
	 * �w���v��\��
	 */
	public void printHelp() {
		System.err.println("�g�p�\�ȃI�v�V�����͈ȉ��̒ʂ�ł�:");
		System.err.println("  -help / -h	�w���v�\��");
		System.err.println("  -S		�I���w�� ��: operator.TournamentSelection");
		System.err.println("  -C		�����w�� ��: operator.UniformCrossover");
		System.err.println("  -M		�ˑR�ψَw�� ��: operator.BitMutation");
		System.err.println("  -Sparam	�I���p�����[�^ ��: 3�C2:4 (�����̃p�����[�^��:�ŋ�؂�)");
		System.err.println("  -Cparam	�����p�����[�^ ��: 0.6 (�����̃p�����[�^��:�ŋ�؂�)");
		System.err.println("  -Mparam	�ˑR�ψكp�����[�^ ��: 0.01 (�����̃p�����[�^��:�ŋ�؂�)");
		System.err
				.println("  -scaling1	�ړI�֐��Ŏg���ϊ��֐� ��: problem.function.LinearFunction");
		System.err
				.println("  -scaling2	�I���Ŏg���X�P�[�����O ��: operator.transform.RankTransform");
		System.err.println("  -P		��� ��: problem.KnapsackProblem");
		System.err.println("  -Pparam	���p�����[�^ ��: 10�C20:3 (�����̃p�����[�^��:�ŋ�؂�)");
		System.err.println("  -popsize	�̐� ��: 100");
		System.err.println("  -gsize	���㐔 ��: 200");
		System.err.println("  -clength	��`�q�� ��: 30");
		System.err.println("  -printlevel	�v�����g���x�� ��: 1");
		System.err
				.println("  -output	�o�͐� ��: log.txt, stderr(�W���o��)�Cstderr(�W���G���[�o��)");
		System.err.println("  -elitism	�G���[�g��` ��: true/false");
		System.err.println("  -viewer	�r���[�� ��: viewer.TextViewer");
		System.err.println("  -turbo	�������t���O ��: true/false");
		System.err.println("  -memory	�̃������t���O ��: true/false");
		System.err.println("  -seed	�V�[�h ��: 0 (�w�肵�Ȃ���΃V�X�e���������g�p)");
	}

	/**
	 * �eGA�p�����[�^��Ԃ�.
	 * @return GA�p�����[�^
	 */
	public GAParameters getGAParams() {
		if (gaParams_ == null) {
			gaParams_ = new GAParameters();
		}
		return gaParams_;
	}

	/**
	 * CSV�t�@�C���ɂ�鏉����
	 * @param filename CSV�t�@�C����
	 */
	public void setGAParamsByCSVFile(String filename) {
		String line;
		HashMap<String, String> map = getGAParams().getParametersMap();
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
				// ��s�X�L�b�v
				if (line.matches("^\\s*$")) {
					continue;
				}
				// ������n�܂�R�����g�s����
				if (line.matches("^#.*")) {
					continue;
				}
				// CSV��O��ɂ��Ă���̂� , ���Ȃ��s�͖���
				if (!line.matches(".*,.*")) {
					continue;
				}
				// �󔒏���
				line = line.replaceAll("\\s", "");
				String[] data = line.split(",");
				// key,val �̌`���ɂȂ��Ă��Ȃ�����
				if (data.length != 2) {
					continue;
				}
				map.put(data[0], data[1]);
			}
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void localTest() throws LocalTestException {
		IGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		// Director()�̃e�X�g
		if (d.builder_ != builder) {
			throw new LocalTestException(" Director constractor error! ");
		}
		// setParameter(String[])�̃e�X�g
		IGABuilder builder2 = new DefaultGABuilder();
		Director d2 = new Director(builder2);
		try{
			// null ��n���̂� OK.
			d2.setParameter((String[]) null);
		} catch (Exception e) {
			throw new LocalTestException(e.getMessage());			
		}
		String[] param = { "", "" };
		try {
			// �󕶎���̓_���D
			d2.setParameter(param);
			// ��O���������Ă��Ȃ��Ƃ�������			
			throw new LocalTestException();
		} catch (Exception e) {

		}
		param = new String[] { "-P" };
		try {
			// �s���S�Ȃ��̂��_���D
			d2.setParameter(param);
			// ��O���������Ă��Ȃ��Ƃ�������
			throw new LocalTestException();
		} catch (Exception e) {

		}
		param = new String[] { "-P", "problem.BitCountProblem" };
		try{
			// ����� OK.
			d2.setParameter(param);
			d2.constract();
			builder2.buildGA();
		}catch(Exception e){
			throw new LocalTestException(e.getMessage());			
		}
		// getGAParams()�̃e�X�g
		Director d3 = new Director(new DefaultGABuilder());
		if (d3.getGAParams() == null) {
			throw new LocalTestException();
		}
	}
}
