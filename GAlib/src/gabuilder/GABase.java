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

import operator.BitMutation;
import operator.Elitism;
import operator.IOperator;
import operator.OnePointCrossover;
import operator.TournamentSelection;
import population.Population;
import problem.BitCountProblem;
import viewer.IViewer;
import viewer.TextViewer;

/**
 * GABase �̊�{�N���X�D�̌Q�ɉ��Z�q���J��Ԃ��K�p����D<br>
 * ����, �I��, �ˑR�ψق�K�p ���邱�Ƃ͑O��Ƃ��Ȃ��D�C�ӂ̉��Z�q���K�p�\�D
 * @author mori
 * @version 1.0
 */

public class GABase {
	/**
	 * �I�y���[�^���i�[���郊�X�g�D���̃��X�g���̃I�y���[�^����x�K�p������Ԃ��ꐢ��ƂȂ�D
	 */
	private ArrayList<IOperator> operatorList_;

	/**
	 * �̌Q�D
	 */
	private Population population_;

	/**
	 * GA�p�����[�^
	 */
	private GAParameters param_;

	/**
	 * ���݂̐��㐔�D�����̌Q�͐��� 0 �Ƃ��C���Z�q�����ׂēK�p������1���₷�D
	 */
	private long currentGeneration_ = 0;

	/**
	 * ���㐔�D
	 */
	private long generationSize_ = GAParameters.DEFAULT_GENERATION_SIZE;

	/**
	 * ���Ɏ��s���鉉�Z�q�� operatorList_ �ɂ�����ʒu
	 */
	private int nextIndex_ = 0;

	/**
	 * ���ゲ�Ƃ̕\���r���[�����X�g
	 */
	private ArrayList<IViewer> generationViewerList_;

	/**
	 * ���Z�q���Ƃ̕\���r���[�����X�g
	 */
	private ArrayList<IViewer> operationViewerList_;

	/**
	 * �����Ȃ��R���X�g���N�^
	 */
	public GABase() {
		operatorList_ = new ArrayList<IOperator>();
	}

	/**
	 * �Ƃ肠���������������ꍇ�p�� SGA �������֐��D�O������̃p�����[�^�ύX�͂ł��Ȃ��D<br>
	 * �����܂ł��T���v���Ȃ̂ŁCIGABuilder �� Director ���g�����Ƃ�����D
	 * @see Director
	 * @see IGABuilder
	 * @see DefaultGABuilder
	 * @see TDGABuilder
	 */
	public void sampleInit() {
		// �r�b�g�J�E���g���
		FitnessManager.setProblem(new BitCountProblem());
		// �̌Q�̐ݒ�. 100 �� ��`�q�� 50
		setPopulation(new Population(100, 50));
		// ��`���Z�q�̐ݒ�
		ArrayList<IOperator> list = new ArrayList<IOperator>();
		// �����͌�����0�D6�̈�_����
		OnePointCrossover c = new OnePointCrossover();
		c.setParameter(0.6);
		// �ˑR�ψٗ�0�D02
		BitMutation m = new BitMutation();
		m.setParameter(0.02);
		// �I���̓g�[�i�����g�T�C�Y 3 �̃g�[�i�����g�I���D
		TournamentSelection s = new TournamentSelection();
		s.setParameter(3);
		list.add(c);
		list.add(m);
		list.add(s);
		// �G���[�g�ۑ�����
		list.add(new Elitism());
		setOperatorList(list);
		// ���㐔 100
		setGenerationSize(100);
		// �r���[���̐ݒ�D�\�����x��2�� TextViewer�D�W���o�͂ɕ\���D
		TextViewer v = new TextViewer();
		v.setPrintLevel(2);
		addGenerationViewer(v);
	}

	/**
	 * �J�n���Ɉ�x�����s���鏈�����L�q�D
	 */
	public void begin() {
		for (IViewer viewer : getGenerationViewerList()) {
			viewer.begin();
		}
	}

	/**
	 * �I�����Ɉ�x�����s���鏈�����L�q�D
	 */
	public void end() {
		for (IViewer viewer : getGenerationViewerList()) {
			viewer.end();
		}
	}

	/**
	 * GABase �̃��C�����[�v. ���㐔�� nextGeneration ���ĂԁD
	 * @see #nextGeneration()
	 */
	public void start() {
		begin();
		for (int i = 0; i < getGenerationSize(); i++) {
			nextGeneration();
		}
		end();
	}

	/**
	 * GABase �̐�����ЂƂi�߂�DoperatorList_ �̓r���Ŏ~�܂��Ă���ꍇ�͓r�����烊�X�g�̍Ō�܂Ői�߂�D
	 */
	public void nextGeneration() {
		int startIndex = getNextIndex();
		for (int i = startIndex; i < getOperatorList().size(); i++) {
			// null �̓G���[�D �P�����Z�q�� Dummy ���g���D
			nextOperation();
		}
		// ����Ɋւ���r���[���̍X�V
		for (IViewer viewer : getGenerationViewerList()) {
			// �����t�����Ƃ��ĕ\��
			viewer.update("generation:" + getCurrentGeneration() + " ");
		}
	}

	/**
	 * �̌Q�Ɏ��̈�`���Z�q��K�p����D
	 */
	public void nextOperation() {
		// ���̈�`���Z�q��K�p�D
		getNextOperator().apply(population_);
		/**
		 * ����\���Dn����̓r���̏ꍇ�́C����� n-1 ���ゾ���C���ł� n ����ɓ˓����Ă���ƍl����̂� <br>
		 * currentGeneration �� 1 �𑫂��D n ����ɓK�p�\��̉��Z�q���� M �̏ꍇ�� 
		 * generation: n operator: 1/M <br>
		 * �ȂǂƂ��āC���Ԗڂ̉��Z�q��K�p�����킩��悤�ɂ���Dn ���オ���x �I������̂� <br>
		 * generation: n operator: M/M �̂Ƃ��ƂȂ�D<br>
		 */
		long generation = getCurrentGeneration() + 1;
		int opSize = getOperatorList().size();
		// ���Z�q�Ɋւ���r���[���̍X�V
		for (IViewer viewer : getOperationViewerList()) {
			viewer.update("generation:" + generation + " operator:"
					+ getNextIndex() + "/" + opSize + " ");
		}
		// operator_ �̌��݈ʒu�� �i�߂�D
		if (getNextIndex() == getOperatorList().size() - 1) {
			// operatorList_ �̍Ō�̉��Z�q�̏ꍇ�� nextIndex_ �� 0 �ɂ��Đ������i�߂�D
			setNextIndex(0);
			setCurrentGeneration(getCurrentGeneration() + 1);
		} else {
			// �r���̏ꍇ�͒P�� nextIndex_ ����i�߂�D
			setNextIndex(getNextIndex() + 1);
		}
	}

	/**
	 * ���ɓK�p�����`���Z�q��Ԃ��D�I�y���[�^���󂾂ƃG���[�ɂȂ�D <br>
	 * �������Ȃ��ꍇ��\����������΁CDummy���Z�q�����Ă����D
	 * @return ���ɓK�p�����`���Z�q�D
	 */
	public IOperator getNextOperator() {
		return getOperatorList().get(getNextIndex());
	}

	/**
	 * �I�y���[�^���X�g��Ԃ��D �Q�Ƃ𒼐ڕԂ��D
	 * @return �I�y���[�^���X�g
	 */
	public ArrayList<IOperator> getOperatorList() {
		return operatorList_;
	}

	/**
	 * �I�y���[�^���X�g��ݒ肷��D
	 * @param operators �I�y���[�^���X�g
	 */
	public void setOperatorList(ArrayList<IOperator> operators) {
		operatorList_ = operators;
	}

	/**
	 * ���Z�q���X�g���ŁC���Z�q1�Ɖ��Z�q2�̈ʒu����������D
	 * @param index1 ���Z�q1�̈ʒu
	 * @param index2 ���Z�q2�̈ʒu
	 */
	public void swapOperatorPosition(int index1, int index2) {
		IOperator op1 = getOperatorList().get(index1);
		IOperator op2 = getOperatorList().get(index2);
		// op1 �� op2 �̈ʒu������
		getOperatorList().set(index1, op2);
		getOperatorList().set(index2, op1);

	}

	/**
	 * �̌Q��Ԃ��D �Q�Ƃ𒼐ڕԂ��D
	 * @return �̌Q
	 */
	public Population getPopulation() {
		return population_;
	}

	/**
	 * �̌Q��ݒ肷��D
	 * @param pop �̌Q
	 */
	public void setPopulation(Population pop) {
		population_ = pop;
	}

	/**
	 * �p�����[�^��Ԃ�.
	 * @return �p�����[�^
	 */
	public GAParameters getGAParameter() {
		return param_;
	}

	/**
	 * �p�����[�^�̐ݒ�. 
	 * @param param �p�����[�^
	 */
	public void setGAParameter(GAParameters param) {
		param_ = param;
	}

	/**
	 * ���݂̐��㐔��Ԃ�.
	 * @return ���݂̐��㐔
	 */
	public long getCurrentGeneration() {
		return currentGeneration_;
	}

	/**
	 * ���݂̐��㐔��ݒ�.
	 * @param currentGeneration ���݂̐��㐔
	 */
	public void setCurrentGeneration(long currentGeneration) {
		currentGeneration_ = currentGeneration;
	}

	/**
	 * ���Ɏ��s���鉉�Z�q�� operatorList_ �ɂ�����ʒu��Ԃ�.
	 * @return ���Ɏ��s���鉉�Z�q�� operatorList_ �ɂ�����ʒu
	 */
	public int getNextIndex() {
		return nextIndex_;
	}

	/**
	 * ���Ɏ��s���鉉�Z�q�� operatorList_ �ɂ�����ʒu��ݒ�. i ���K�؂��̓`�F�b�N���Ȃ��D
	 * @param i ���Ɏ��s���鉉�Z�q�� operatorList_ �ɂ�����ʒu
	 */
	public void setNextIndex(int i) {
		nextIndex_ = i;
	}

	/**
	 * �I�y���[�^����v�f�ɂ����X�g��Ԃ�.
	 * @return �I�y���[�^����v�f�Ɏ����X�g
	 */
	public ArrayList<String> getOperatorNameList() {
		ArrayList<String> result = new ArrayList<String>();
		for (IOperator op : getOperatorList()) {
			result.add(op.getName());
		}
		return result;
	}

	/**
	 * �I�y���[�^�̏ڍׂȏ���Ԃ��D
	 * @return �I�y���[�^���
	 */
	public String getOperatorInfo() {
		StringBuilder sb = new StringBuilder();
		for (IOperator op : getOperatorList()) {
			// op �� null �̏ꍇ���l�����Ė����I�� op.toString() ���Ă΂Ȃ��D
			sb.append(op + "\n");
		}
		return sb.toString();
	}

	/**
	 * ���ゲ�ƂɌĂ΂��r���[���o�^�D�d���o�^�͋����Ȃ��D
	 * @param viewer
	 * @return �o�^�ł�����?
	 */
	public boolean addGenerationViewer(IViewer viewer) {
		// ���ɓ���̎Q�Ƃ�ێ����Ă���ꍇ�ɂ́C�d���o�^�͂��Ȃ��D
		if (getGenerationViewerList().contains(viewer)) {
			return false;
		}
		// viewer �Ɏ�����o�^�D
		viewer.setGA(this);
		// viewer �o�^�D
		getGenerationViewerList().add(viewer);
		return true;
	}

	/**
	 * generationViewerList_ �̃N���A
	 */
	public void clearGenerationViewerList() {
		getGenerationViewerList().clear();
	}

	/**
	 * generationViewerList_ ���̃r���[���̖��O�̃��X�g��Ԃ��D
	 * @return �r���[���̖��O�̃��X�g
	 */
	public ArrayList<String> getGenerationViewerNameList() {
		ArrayList<String> result = new ArrayList<String>();
		for (IViewer v : getGenerationViewerList()) {
			result.add(v.getName());
		}
		return result;
	}

	/**
	 * ���ゲ�Ƃ̃r���[�����X�g��Ԃ�.
	 * @return ���ゲ�Ƃ̃r���[�����X�g
	 */
	private ArrayList<IViewer> getGenerationViewerList() {
		if (generationViewerList_ == null) {
			generationViewerList_ = new ArrayList<IViewer>();
		}
		return generationViewerList_;
	}

	/**
	 * ���Z�q���ƂɌĂ΂��r���[���o�^�D�d���o�^�͋����Ȃ��D
	 * @param viewer
	 * @return �o�^�ł�����?
	 */
	public boolean addOperationViewer(IViewer viewer) {
		// ���ɓ���̎Q�Ƃ�ێ����Ă���ꍇ�ɂ́C�d���o�^�͂��Ȃ��D
		if (getOperationViewerList().contains(viewer)) {
			return false;
		}
		// viewer �Ɏ�����o�^�D
		viewer.setGA(this);
		// viewer �o�^�D
		getOperationViewerList().add(viewer);
		return true;
	}

	/**
	 * operationViewerList_ �̃N���A
	 */
	public void clearOperationViewerList() {
		getOperationViewerList().clear();
	}

	/**
	 * operationViewerList_ ���̃r���[���̖��O�̃��X�g��Ԃ��D
	 * @return �r���[���̖��O�̃��X�g
	 */
	public ArrayList<String> getOperationViewerNameList() {
		ArrayList<String> result = new ArrayList<String>();
		for (IViewer v : getOperationViewerList()) {
			result.add(v.getName());
		}
		return result;
	}

	/**
	 * ���Z�q���Ƃ̃r���[�����X�g��Ԃ�.
	 * @return ���Z�q���Ƃ̃r���[�����X�g
	 */
	private ArrayList<IViewer> getOperationViewerList() {
		if (operationViewerList_ == null) {
			operationViewerList_ = new ArrayList<IViewer>();
		}
		return operationViewerList_;
	}

	/**
	 * ���㐔��Ԃ�.
	 * @return ���㐔
	 */
	public long getGenerationSize() {
		return generationSize_;
	}

	/**
	 * ���㐔�̐ݒ�
	 * @param generationSize ���㐔
	 */
	public void setGenerationSize(long generationSize) {
		generationSize_ = generationSize;
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		GABase ga = new GABase();
		ga.sampleInit();
		ga.start();
	}
}
