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

import fitness.FitnessManager;
import gabuilder.GAParameters;

import java.util.ArrayList;

import junit.framework.TestCase;
import operator.ThermoDynamicalSelection;
import population.Individual;
import population.Population;
import problem.function.IFunction;
import problem.function.LinearFunction;
import util.MyRandom;

/**
 * @author mori
 * @version 1.0
 */
public class ThermoDynamicalSelectionTest extends TestCase {
	/**
	 */
	private ThermoDynamicalSelection td_;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(ThermoDynamicalSelectionTest.class);
	}

	public ThermoDynamicalSelectionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
		td_ = new ThermoDynamicalSelection();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
	}

	public void testThermoDynamicalSelection() {
		assertEquals(td_.getTemperature(), GAParameters.DEFAULT_TEMPERATURE);
	}

	/*
	 * Test method for 'operator.ThermoDynamicalSelection.apply(Population)'
	 */
	public void testApply() {
		td_.setParameter("4", "6");
		td_.setTemperature(1);
		Number[] i1 = { 1, 0, 1, 1, 1, 1 };
		Number[] i2 = { 0, 1, 0, 1, 0, 0 };
		Number[] i3 = { 0, 1, 0, 1, 1, 1 };
		Number[] i4 = { 1, 0, 1, 1, 0, 0 };
		Number[] i5 = { 0, 1, 0, 1, 1, 0 };
		Number[] i6 = { 1, 0, 0, 1, 1, 0 };
		Number[] i7 = { 1, 0, 1, 1, 1, 1 };
		Number[] i8 = { 1, 1, 1, 1, 0, 0 };
		Number[] i9 = { 1, 1, 1, 1, 1, 1 };
		ArrayList<Individual> indivList = new ArrayList<Individual>();
		indivList.add(new Individual(i1));
		indivList.add(new Individual(i2));
		indivList.add(new Individual(i3));
		indivList.add(new Individual(i4));
		indivList.add(new Individual(i5));
		indivList.add(new Individual(i6));
		indivList.add(new Individual(i7));
		indivList.add(new Individual(i8));
		indivList.add(new Individual(i9));
		Population pop = new Population();
		pop.setIndivList(new ArrayList<Individual>(indivList));
		td_.apply(pop);
		assertTrue(pop.getIndividualAt(0).equals(indivList.get(8)));
		assertTrue(pop.getIndividualAt(1).equals(indivList.get(1)));
		assertTrue(pop.getIndividualAt(2).equals(indivList.get(0)));
		assertTrue(pop.getIndividualAt(3).equals(indivList.get(8)));
		// all 1 �̌̂̓K���x�� 6
		assertEquals(6.0, pop.getIndividualAt(0).fitness(), 0.0);
		// ���`�X�P�[�����O�����Ă��C���ʂ͓����D
		IFunction f = new LinearFunction();
		f.setParameter(1, -3.5);
		FitnessManager.getProblem().addFunction(f);
		pop.setIndivList(new ArrayList<Individual>(indivList));		
		td_.apply(pop);
		assertTrue(pop.getIndividualAt(0).equals(indivList.get(8)));
		assertTrue(pop.getIndividualAt(1).equals(indivList.get(1)));
		assertTrue(pop.getIndividualAt(2).equals(indivList.get(0)));
		assertTrue(pop.getIndividualAt(3).equals(indivList.get(8)));
		// all 1 �̌̂̓K���x�� 6*1-3.5=2.5
		assertEquals(2.5, pop.getIndividualAt(0).fitness(), 0.0);
		FitnessManager.reset();
	}

	/*
	 * Test method for 'operator.ThermoDynamicalSelection.init(ArrayList)'
	 */
	public void testInit() {
		td_.setParameter(100, 30); // (�̐�, ��`�q��)
		// �ȉ���͌̐��Ɋ֘A
		assertEquals(td_.getLogValue(100), Math.log(100), 0);
		assertEquals(td_.getNextPopSize(), 100);
		// ������ł����邩�D
		td_.setParameter("43", "2");
		assertEquals(td_.getLogValue(43), Math.log(43), 0);
		assertEquals(td_.getNextPopSize(), 43);
	}

	/*
	 * Test method for 'operator.ThermoDynamicalSelection.getName()'
	 */
	public void testGetName() {
		assertEquals(td_.getName(), "ThermoDynamicalSelection");
	}

	/*
	 * Test method for 'operator.ThermoDynamicalSelection.getLogValue(int)'
	 */
	public void testGetLogValue() {
		td_.setParameter(20, 10); // (�̐�, ��`�q��)
		// �̐����� Log �l���Z�b�g�����D
		assertEquals(td_.getLogValue(20), Math.log(20), 0);
		// 0 ��Log�l�͗�O�I�� 0 �Ƃ��Ă���D x*log(x) �����g��Ȃ����ߖ��Ȃ��D
		assertEquals(td_.getLogValue(0), 0, 0);
	}

	/*
	 * Test method for 'operator.ThermoDynamicalSelection.getNextPopSize()'
	 */
	public void testGetNextPopSize() {
		td_.setParameter(8, 30); // (�̐�, ��`�q��)
		assertEquals(td_.getNextPopSize(), 8);
	}

	/*
	 * Test method for
	 * 'operator.ThermoDynamicalSelection.packIndivList(ArrayList<Individual>)'
	 */
	public void testPackIndivList() {
		Number[] i1 = { 1, 0, 1 };
		Number[] i2 = { 1, 1, 1 };
		Number[] i3 = { 1, 0, 1 };
		ArrayList<Individual> list = new ArrayList<Individual>();
		for (int i = 0; i < 3; i++) {
			list.add(new Individual(i1));
			list.add(new Individual(i2));
			list.add(new Individual(i3));
		}
		assertEquals(list.size(), 9);
		td_.packIndivList(list); // �d���̂���菜��. ����2�̂������Ȃ��D
		assertEquals(list.size(), 2);
		Individual indiv1 = new Individual(i1);
		Individual indiv2 = new Individual(i2);
		assertTrue(indiv1.equals(list.get(0)));
		assertTrue(indiv2.equals(list.get(1)));
	}

	/*
	 * Test method for 'operator.ThermoDynamicalSelection.getTemperature()'
	 */
	public void testGetTemperature() {
		double t = MyRandom.getInstance().nextDouble();
		td_.setTemperature(t);
		assertEquals(td_.getTemperature(), t, 0);
		td_.setParameter("10", "5");// ���̏ꍇ�͉��x�ύX�Ȃ��D
		assertEquals(td_.getTemperature(), t, 0);
		td_.setParameter("10", "5", "1.234"); // ��3�v�f������Ɖ��x�ɂȂ�
		assertEquals(td_.getTemperature(), 1.234, 0);
	}

	// �K���x�̍X�V�t���O���K�؂ɍX�V����Ă��邩�̃e�X�g
	public void testIsChanged() {
		Population pop = new Population(100, 20);// 100 ��
		td_.setParameter("100", "20");
		for (Individual indiv : pop.getIndivList()) {
			assertTrue(indiv.isChanged());
		}
		td_.apply(pop);
		// �I�����ɂ͂��ׂĂ̓K���x���v�Z�����D�I���ł͐V�����̂͂ł��Ȃ��D
		for (Individual indiv : pop.getIndivList()) {
			assertFalse(indiv.isChanged());
		}
	}

	public void testLocalTest() {
		try {
			td_.localTest();
		} catch (Exception e) {
			e.printStackTrace();
			fail("ThermoDynamicalSelection.localTest() was failed!");
		}
	}

	/**
	 * �N���X ThermoDynamicalSelection �� setParameter(Object ... params) �̃e�X�g�D
	 * 
	 * @see ThermoDynamicalSelection#setParameter
	 */

	public void testSetParameter() {
		ThermoDynamicalSelection target = new ThermoDynamicalSelection();

		// ���l�������̏ꍇ�� 1. ���l�C 2. ������C 3. �z���n�����ꍇ�̃e�X�g������
		target.setParameter(100, 10);
		assertEquals(target.getNextPopSize(), 100);
		// ���x�̃f�t�H���g�͂P
		assertEquals(target.getTemperature(), 1.0, 0.0);
		// isPackIndivList �̏����l�� false
		assertFalse(target.isPackIndivList());
		target.setParameter(100, 10, 36);
		assertEquals(target.getTemperature(), 36.0, 0.0);
		target.setParameter(100, 10, 111.1, "true");
		assertTrue(target.isPackIndivList());
		assertEquals(target.getTemperature(), 111.1, 0.0);
		target.setParameter(100, 10, "-1", false);
		assertFalse(target.isPackIndivList());
		assertEquals(target.getTemperature(), -1, 0.0);
		target.setParameter("100", "10");
		assertEquals(target.getNextPopSize(), 100);
		Object[] tmpParam = { new Integer(100), new Integer(10) };
		target.setParameter(tmpParam);
		assertEquals(target.getNextPopSize(), 100);
		// ��O�̔����e�X�g�D
		// �����̐������������Ɨ�O�����D�p�����[�^��(���Ȃ���)
		try {
			target.setParameter("1");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// �����̐������������Ɨ�O�����D�p�����[�^��(������)
		try {
			target.setParameter(1, 2, 3, true, 0);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// �����̓��e�����������Ɨ�O�����D������̓��e
		try {
			target.setParameter("100�D1", "30");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// �����̓��e�����������Ɨ�O�����D��3����
		try {
			target.setParameter(10, 5, "0a");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// �����̓��e�����������Ɨ�O�����D��4������ null �ȊO�ł͂Ȃ�ł��悢�D(false �ɐݒ肳���)
		try {
			target.setParameter(10, 5, 3, null);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * @see operator.ThermoDynamicalSelection#getParameterInfo()
	 */
	public void testGetParameterInfo() {
		// ���x�� 36.0 �ɐݒ�. isPackIndivList �̓f�t�H���g false
		td_.setTemperature(36.0);
		assertEquals("TEMPERATURE:36.0,isPackIndivList:false", td_
				.getParameterInfo());
		// isPackIndivList �� true �ɐݒ�
		td_.setPackIndivList(true);
		assertEquals("TEMPERATURE:36.0,isPackIndivList:true", td_
				.getParameterInfo());

	}

	/**
	 * @see operator.AbstractSelection#toString()
	 */
	public void testToString() {
		// ���x�� 36.0 �ɐݒ�
		td_.setTemperature(36.0);
		assertEquals(
				"ThermoDynamicalSelection{TEMPERATURE:36.0,isPackIndivList:false}",
				td_.toString());
	}

	public void testSetPackIndivList() {
		// �n�߂� False
		assertFalse(td_.isPackIndivList());
		td_.setPackIndivList(true);
		// True �ɂȂ����D
		assertTrue(td_.isPackIndivList());
	}

}
