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

package operator;

import java.util.ArrayList;
import java.util.Arrays;

import population.Population;

/**
 * �����̉��Z�q�̌��ʂ𕹂��鉉�Z�q.
 * @author mori
 * @version 1.0
 */
public class Merge implements IOperator {
	/**
	 * �I�y���[�^���X�g
	 */
	private ArrayList<IOperator> operators_;

	/**
	 * �f�t�H���g�R���X�g���N�^
	 */
	public Merge() {
		super();
	}

	/**
	 * �ϒ������𗘗p�����z��ŏ������D �̃N���X�̓p�����[�^�Ȃ��D
	 * @param params �p�����[�^�̔z��
	 */
	public void setParameter(Object... params) {
		// �p�����[�^����
		if (params == null) {
			return;
		}
		try {
			// ����0�̔z��͔F�߂�D
			if (params.length != 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * �̌Q�ɓK�p����. <br>
	 * ���Z�q���o�^����Ă��Ȃ� �� �Ȃɂ����Ȃ� <br>
	 * �o�^���Z�q������� �� �o�^���Z�q�� pop �ɓK�p���ďI�� <br>
	 * �o�^���Z�q�� 2 �ȏ� �� �o�^���Z�q�� pop �̐[���R�s�[�ɓK�p�������ʂ����킹�� <br>
	 * @param pop �̌Q
	 */
	public void apply(Population pop) {
		// ���Z�q�������Z�b�g����ĂȂ����
		if (operators_ == null || operators_.size() == 0) {
			return;
		}
		// ���Z�q��������̏ꍇ�D
		if (operators_.size() == 1) {
			operators_.get(0).apply(pop);
		}
		// �����̉��Z�q������ꍇ. �e���Z�q�̓K�p���ʂ�ۑ�����z��D
		Population[] popArray = new Population[operators_.size()];
		popArray[0] = pop; // �擪�̓I���W�i���Dclone ����񌸂炷�D
		for (int i = 1; i < popArray.length; i++) {
			popArray[i] = pop.clone(); // �̌Q�̐[���R�s�[��ۑ��D i=1 ���
		}
		// popArray.length == operators_.size()
		for (int i = 0; i < operators_.size(); i++) {
			IOperator op = operators_.get(i);
			if (op == null) { // null �������Ă����ꍇ�ɂ͌̌Q�ɉ������Ȃ��D
				continue;
			}
			op.apply(popArray[i]); // �e�̂ɉ��Z�q��K�p
		}
		// pop==popArray[0] �ł���D pop �� IndivList �ɃR�s�[�����Z�q��K�p��������ǉ�
		for (int i = 1; i < popArray.length; i++) {
			pop.getIndivList().addAll(popArray[i].getIndivList());
		}
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "Merge";
	}

	/**
	 * �I�y���[�^���X�g��Ԃ�.
	 * @return �I�y���[�^���X�g
	 */
	public final ArrayList<IOperator> getOperators() {
		if (operators_ == null) {
			operators_ = new ArrayList<IOperator>();
		}
		return operators_;
	}

	/**
	 * �I�y���[�^���X�g�̐ݒ�.
	 * @param operators �I�y���[�^���X�g
	 */
	public final void setOperators(ArrayList<IOperator> operators) {
		operators_ = operators;
	}

	/**
	 * �p�����[�^�̏���Ԃ��DMerge �̏ꍇ�͕����̉��Z�q������Ɏ��̂ŁC���ꂼ��̏���\���D<br>
	 * ��FBitMutation{MUTATION_RATE:0.01} OnePointCrossover{CROSSOVER_RATE:0.6}
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		if (getOperators().size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (IOperator op : getOperators()) {
			result.append(op + ",");
		}
		// �Ō�� , ������
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/**
	 * ������.
	 * @return ������\��
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		Merge m = new Merge();
		Merge m2 = new Merge();
		m.getOperators().add(new BitMutation());
		m.getOperators().add(new OnePointCrossover());
		m.getOperators().add(null);
		m2.getOperators().add(new BitMutation());
		m2.getOperators().add(null);
		m2.getOperators().add(new RouletteSelection());
		m.getOperators().add(m2);
		System.out.println(m.getParameterInfo());
		System.out.println(m);
	}

}
