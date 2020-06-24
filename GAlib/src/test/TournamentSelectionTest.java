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

package test;

import gabuilder.GAParameters;

import java.util.Arrays;
import junit.framework.TestCase;
import operator.AbstractSelection;
import operator.TournamentSelection;
import population.Individual;
import population.Population;
import util.MyRandom;
import util.NStatistics;
/**
 * @author mori
 * @version 1.0
 */

public class TournamentSelectionTest extends TestCase {
	/**
	 */
	TournamentSelection ts_;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TournamentSelectionTest.class);
	}

	public TournamentSelectionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
		ts_ = new TournamentSelection();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
	}

	/*
	 * Test method for 'operator.TournamentSelection.select(double[])'
	 */
	public void testSelect() {
		// i �Ԗڂ̏��ʂ� data.length-i;
		double[] data = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] result = new int[data.length];
		double sum = 0;
		int maxi = 3000;
		for (int i = 0; i < data.length; i++) {
			sum += (data[i] + 1); // ���ʂ� 1�`11�܂�
			result[i] = 0;
		}
		// �g�[�i�����g�T�C�Y��1�̏ꍇ. ���ׂĂ������ɑI�΂��D
		ts_.setTournamentSize(1);
		int[] indexArray;
		for (int i = 0; i < maxi; i++) {
			indexArray = ts_.select(data);
			for (int j = 0; j < indexArray.length; j++) {
				result[indexArray[j]]++;
			}
		}

		for (int i = 0; i < result.length; i++) {
			double p = 1.0 / data.length; // i �Ԗڂ��I�΂��m���D
			// ��x��select �� data.length �̂��I�΂��̂ŁC�S����maxi*data.length �I�΂��D
			NStatistics.binomialTest(maxi * data.length, result[i], p,
					TestParameters.SIGMA_MARGIN);
		}
		// �g�[�i�����g�T�C�Y�� 2 �̏ꍇ.
		ts_.setTournamentSize(2);
		Arrays.fill(result, 0);
		for (int i = 0; i < maxi; i++) {
			indexArray = ts_.select(data);
			for (int j = 0; j < indexArray.length; j++) {
				result[indexArray[j]]++;
			}
		}
		assertEquals(0, result[0]);
		for (int i = 0; i < result.length; i++) {
			/**
			 * �����Nr �����̃g�[�i�����g�ɎQ���ł���m�� 2/N �����Nr ���g�[�i�����g�ɎQ�������ꍇ�ɑI�΂��m��
			 * (N-r)/(N-1) <- ���肪������艺�Ȃ�悢�D �����Nr �����̃g�[�i�����g�őI�΂��m��
			 * {(N-r)/(N-1)}*{2/N}
			 */
			double p = data[i] / (data.length - 1) * (2.0 / data.length); // data[i]=N-r
			NStatistics.binomialTest(maxi * data.length, result[i], p,
					TestParameters.SIGMA_MARGIN);
		}
		// �g�[�i�����g�T�C�Y�� Nt �̂Ƃ��̓����N�� N-Nt+2 �ȉ��̌̂͑I�΂�Ȃ��D
		maxi = 100;
		// tsize 1,2 �͎�����.
		for (int tsize = 3; tsize <= data.length; tsize++) {
			Arrays.fill(result, 0);
			ts_.setTournamentSize(tsize);
			for (int i = 0; i < maxi; i++) {
				indexArray = ts_.select(data);
				for (int j = 0; j < indexArray.length; j++) {
					result[indexArray[j]]++;
				}
			}
			// ���ʌ̂��I�΂�Ă��Ȃ����Ƃ̃e�X�g
			// data �͏��������Ȃ̂ŁC�O���� tsize-1 ��(index 0~tsize-2 �܂�)���I�΂�Ȃ��D
			for (int i = 0; i < tsize - 1; i++) {
				assertEquals(0, result[i]);
			}
		}
	}

	/*
	 * Test method for 'operator.TournamentSelection.init(List)'
	 */
	public void testInit() {
		// �f�t�H���g�l�͂R
		assertEquals(GAParameters.DEFAULT_TOURNAMENT_SIZE, ts_.getTournamentSize());
		ts_.setParameter("12");
		assertEquals(12, ts_.getTournamentSize());
		ts_.setParameter(1);
		assertEquals(1, ts_.getTournamentSize());
		try {
			ts_.setParameter("a");
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
		try {
			ts_.setParameter(-1);
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
		try {
			ts_.setParameter(0);
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}

	}

	/*
	 * Test method for 'operator.TournamentSelection.getName()'
	 */
	public void testGetName() {
		assertEquals("TournamentSelection", ts_.getName());
	}

	/*
	 * Test method for 'operator.TournamentSelection.getTournamentSize()' Test
	 * method for 'operator.TournamentSelection.setTournamentSize(int)'
	 */
	public void testGetAndSetTournamentSize() {
		int size = 123;
		ts_.setTournamentSize(size);
		assertEquals(size, ts_.getTournamentSize());
	}

	// �K���x�̍X�V�t���O���K�؂ɍX�V����Ă��邩�̃e�X�g
	public void testIsChanged() {
		Population pop = new Population(100, 20);// 100 ��
		for (Individual indiv : pop.getIndivList()) {
			assertTrue(indiv.isChanged());
		}
		ts_.apply(pop);
		// �I�����ɂ͂��ׂĂ̓K���x���v�Z�����D�I���ł͐V�����̂͂ł��Ȃ��D
		for (Individual indiv : pop.getIndivList()) {
			assertFalse(indiv.isChanged());
		}
	}

	/*
	 * Test method for 'operator.TournamentSelection.localTest()'
	 */
	public void testLocalTest() {
		try {
			ts_.localTest();
		} catch (Exception e) {
			e.printStackTrace();
			fail("TournamentSelection.localTest() was failed!");
		}
	}

	/**
	 * �N���X TournamentSelection �� setParameter(Object ... params) �̃e�X�g�D
	 * 
	 * @see TournamentSelection#setParameter
	 */

	public void testSetParameter() {
		TournamentSelection target = new TournamentSelection();

		// �e�X�g�L�q�D�ȉ��͎����l����p�����[�^�Ƃ���ꍇ�̗�D
		// ���l�������̏ꍇ�� 1. ���l�C 2. ������C 3. �z���n�����ꍇ�̃e�X�g������
		target.setParameter(2);
		assertEquals(2, target.getTournamentSize());
		target.setParameter("2");
		assertEquals(2, target.getTournamentSize());
		Object[] tmpParam = { new Integer(2) };
		target.setParameter(tmpParam);
		assertEquals(2, target.getTournamentSize());

		// ��O�̔����e�X�g�D
		// �����̐������������Ɨ�O�����D�p�����[�^��
		try {
			target.setParameter("1", "2");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// �����̓��e�����������Ɨ�O�����D������̓��e
		try {
			target.setParameter("num");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * @see TournamentSelection#getParameterInfo()
	 */
	public void testGetParameterInfo(){
		// �g�[�i�����g�T�C�Y�� 7 �ɐݒ�
		ts_.setTournamentSize(7);
		assertEquals("TOURNAMENT_SIZE:7", ts_.getParameterInfo());
	}
	
	/**
	 * @see AbstractSelection#toString()
	 */
	public void testToString(){
		// �g�[�i�����g�T�C�Y�� 7 �ɐݒ�
		ts_.setTournamentSize(7);
		assertEquals("TournamentSelection{TOURNAMENT_SIZE:7}", ts_.toString());
	}
}
