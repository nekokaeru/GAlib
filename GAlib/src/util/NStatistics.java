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

/**
 * ���낢��ȓ��v�ʂ��v�Z���邽�߂� Utility Class
 */
package util;

/**
 * ���v�p�N���X.
 * @author mori
 * @version 1.0
 */
public final class NStatistics {
	/**
	 * ���Ғl�D
	 * @param p �m��
	 * @param N ����
	 * @return ���Ғl
	 */
	public static double E(double p, long N) {
		if (p < 0 || p > 1 || N < 0) {
			throw new IllegalArgumentException();
		}
		return p * N;
	}

	/**
	 * 2�����z�ɏ]���ꍇ�̕��U�D
	 * @param p
	 * @param N
	 * @return ���U
	 */
	public static double V(double p, long N) {
		if (p < 0 || p > 1 || N < 0) {
			throw new IllegalArgumentException();
		}
		return N * p * (1 - p);
	}

	/**
	 * 2�����z�ɏ]���ꍇ�̕W���΍��D
	 * @param p
	 * @param N
	 * @return �W���΍�
	 */
	public static double SD(double p, long N) {
		if (p < 0 || p > 1 || N < 0) {
			throw new IllegalArgumentException();
		}
		return Math.sqrt(N * p * (1 - p));
	}

	/**
	 * 2�������̌��ʃe�X�g�D�m�� p �ŋN���鎖�ۂ� totalNum ��J��Ԃ������ʁC���ۂ� resultNum ��
	 * ���̎��ۂ��N�����D���ꂪ���v�I�ɐ��������𒲂ׂ�D��̓I�ɂ͌��ʂ����K���z�ŋߎ��ł��邱��
	 * �𗘗p���āC���Ғl�Ǝ��ۂ̉񐔂̌덷�� n*�� �ɓ����Ă��邩��Ԃ��D<br> 
	 * |p*totalNum - resultNum| < n*��(totalNum*p*(1-p)) �ƂȂ�m���� 
	 * n=1 -> 0.6827, n=2->0.9545, n=3 ->0.9973, n=4 -> 0.9999 �ƂȂ�D���ʂ� n=4 �ŏ\���D
	 * @param totalNum �����̑���
	 * @param resultNum ���鎖�ۂ̋N������
	 * @param p ���鎖�ۂ̋N����m��
	 * @param n ���ʂ� n*�� �ɓ����Ă��邩�𒲂ׂ�
	 * @return �����𖞂����ΐ^��Ԃ�
	 */
	public static boolean binomialTest(long totalNum, long resultNum, double p,
			double n) {
		if (p < 0 || p > 1 || totalNum < 0 || resultNum < 0 || n < 0) {
			throw new IllegalArgumentException();
		}
		double E = totalNum * p;
		double sig = Math.sqrt(totalNum * p * (1 - p));
		return Math.abs(E - resultNum) <= n * sig;
	}
}
