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

package problem;

import java.util.Arrays;
import java.util.Random;

import util.MyRandom;

/**
 * Nk ���.
 * @author mori
 * @version 1.0
 */
public class NkProblem extends AbstractProblem {
	/**
	 * �����V�[�h.
	 */
	private long initSeed_ = 0;

	/**
	 * �����I�u�W�F�N�g.
	 */
	private Random rand_ = new Random(0);

	/**
	 * �V�[�h�����p�D Java �̗����͐����������C�אڂ����V�[�h�̋��������Ă��܂����߁D<br>
	 * TODO �����Z���k�c�C�X�^�[�������I
	 */
	private Random seedGenerator_ = new Random(0);

	// private SecureRandom rand_ = new SecureRandom();

	/**
	 * Nk �� k. �����l 0
	 */
	private int k_ = 0;

	/**
	 * Nk �� N. �����l 10
	 */
	private int N_ = 10;

	/**
	 * �����Ȃ��R���X�g���N�^
	 */
	public NkProblem() {
	}

	/**
	 * Nk �� N �� k �ŏ�����.
	 * @param N Nk��N
	 * @param k Nk��k
	 */
	public NkProblem(int N, int k) {
		setParameter(N, k, initSeed_);
	}

	/**
	 * Nk �� N �� k ����� �����̃V�[�h�ŏ�����.
	 * @param N Nk��N
	 * @param k Nk��k
	 * @param seed �����̃V�[�h
	 */
	public NkProblem(int N, int k, long seed) {
		setParameter(N, k, seed);
	}

	/**
	 * �ړI�֐��l���v�Z���ĕԂ��D
	 * @param array ��`�q�^���
	 * @return Nk���̒l
	 */
	public double getObjectiveFunctionValue(Number[] array) {
		return getTotalValue(array, getK()) / getN(); // �T�C�Y�Ŋ���D
	}

	/**
	 * ���O��Ԃ�.
	 * @return ���O
	 */
	public String getName() {
		return "Nk";
	}

	/**
	 * array �ɉ����������K���x��Ԃ��D
	 * @param array
	 * @return �����K���x
	 */
	protected double getSubValue(Number[] array, int location) {
		/*
		 * ������ƕ�����̈ʒu�ɑΉ����ĈقȂ�V�[�h�����܂�D �����ӁI hashCode
		 * �͊��S�ł͂Ȃ��D�قȂ�z��ł������n�b�V���l�������Ƃ�����D
		 */
		long seed = initSeed_ + Arrays.hashCode(array) + location;// �V�[�h�̍X�V
		seedGenerator_.setSeed(seed);
		// Java �̗����͐������ǂ��Ȃ��̂ň��ڂ͎̂Ă�D
		seedGenerator_.nextLong();
		rand_.setSeed(seedGenerator_.nextLong());
		// Java �̗����͐������ǂ��Ȃ��̂ň��ڂ͎̂Ă�D
		rand_.nextDouble();
		return rand_.nextDouble();
	}

	/**
	 * array �� Nk ���ɂ��]���l���v�Z����D�����͗אڂ̂݁D<br>
	 * TODO �����_���R�l�N�V�����̍쐬�D
	 * @param array �ΏۋL����
	 * @param k Nk �� k
	 * @return �]���l
	 */
	protected double getTotalValue(Number[] array, int k) {
		// Nk �ł� k < N �ł���D
		if (k >= array.length) {
			throw new IllegalArgumentException(k + ">=" + array.length);
		}
		// Nk �ł� k < N �ł���D
		if (getN() != array.length) {
			throw new IllegalArgumentException(getN() + "!=" + array.length);
		}

		// ������ۑ��p�D
		Number[] sub = new Number[k + 1];
		double sum = 0; // �]���l
		int N = array.length;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j <= k; j++) {
				sub[j] = array[(i + j) % N];
			}
			sum += getSubValue(sub, i);
		}
		return sum;
	}

	/**
	 * �����̏����V�[�h��Ԃ�.
	 * @return �����V�[�h
	 */
	public long getInitSeed() {
		return initSeed_;
	}

	/**
	 * �����̏����V�[�h��ݒ�.
	 * @param initSeed �����V�[�h
	 */
	public void setInitSeed(long initSeed) {
		initSeed_ = initSeed;
	}

	/**
	 * Nk �� k ��Ԃ�.
	 * @return k
	 */
	public int getK() {
		return k_;
	}

	/**
	 * Nk �� k ��ݒ�.
	 * @param k Nk��k
	 */
	public void setK(int k) {
		k_ = k;
	}

	/**
	 * Nk �� N ��Ԃ�.
	 * @return N
	 */
	public int getN() {
		return N_;
	}

	/**
	 * Nk �� N ��ݒ�.
	 * @param n Nk��N
	 */
	public void setN(int n) {
		N_ = n;
	}

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("N:" + getN() + ",k:" + getK() + ",seed:" + getInitSeed());
		if (getFunctionInfo().length() == 0) {
			return sb.toString();
		}
		return sb.toString() + "," + getFunctionInfo();
	}

	/**
	 * @param params �p�����[�^�̔z��D������ł��邱�Ƃɒ��ӁD���v�f��N�C���v�f��k ��O�v�f������Η����V�[�h�D
	 *        ���l�ȊO���w�肳��Ă�����C�v�f��2�łȂ��ꍇ�ɂ͗�O�����D
	 */
	public void setParameter(Object... params) {
		int N = 0, k = 0;
		long seed = 0;
		try {
			if (params.length != 2 && params.length != 3) {
				throw new Exception(
						"params length of NkProblem must be 2 or 3!");
			}
			// N �ݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[0] instanceof Number) {
				N = ((Number) (params[0])).intValue();
			} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
				N = Integer.parseInt(params[0].toString());
			}
			// k �ݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params[1] instanceof Number) {
				k = ((Number) (params[1])).intValue();
			} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
				k = Integer.parseInt(params[1].toString());
			}
			// �p�����[�^����3�ł���΃V�[�h�ݒ�D ������̏ꍇ�Ɛ��l�̏ꍇ�ŏꍇ����
			if (params.length == 3) {
				if (params[2] instanceof Number) {
					seed = ((Number) (params[2])).longValue();
				} else { // ���l�n�N���X�ȊO�̏ꍇ�ɂ͕�����Ɣ��f���Đݒ�D
					seed = Long.parseLong(params[2].toString());
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!\n"
					+ e.getMessage());
		}
		if (k >= N) {
			throw new IllegalArgumentException(k + ">=" + N);
		}
		if (k >= N) {
			throw new IllegalArgumentException(k + ">=" + N);
		}
		if (N < 0) {
			throw new IllegalArgumentException("N=" + N + " < 0");
		}
		if (k < 0) {
			throw new IllegalArgumentException("k=" + k + " < 0");
		}
		setN(N);
		setK(k);
		setInitSeed(seed);
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		MyRandom.setSeed(0);
		Number[] array = { 1, 0, 1, 0 };
		IProblem nk = new NkProblem();
		nk.setParameter(4, 2); // N=4, k=2 Nk���ł́C��`�q��=N �̕K�v����
		System.out.println(nk.getFitness(array));
		// toString �ɂ�� Nk{N:4,k:2,seed:0} ���\�������D
		System.out.println(nk);
	}

}
