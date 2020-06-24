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

import java.util.ArrayList;

import operator.EvaluateFitness;

import population.Individual;
import population.Population;
import fitness.FitnessManager;
import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */

public class EvaluateFitnessTest extends TestCase {

	EvaluateFitness operator;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(EvaluateFitnessTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		operator = new EvaluateFitness();
		FitnessManager.reset();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		FitnessManager.reset();
	}

	/**
	 * @see operator.EvaluateFitness#setParameter(Object[])
	 */
	public void testSetParameter() {
		/*
		 * �p�����[�^�Ȃ�, �������� null, ��z��ȊO�͗�O����.
		 */
		operator.setParameter(); // �p�����[�^�����D
		operator.setParameter((Object[]) null); // �p�����[�^ null
		Object p[] = new Object[0]; // �p�����[�^ ��z��
		operator.setParameter(p);
		// �����̐������������Ɨ�O��f���D��
		try {
			operator.setParameter(3);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * @see operator.EvaluateFitness#apply(Population)
	 */
	public void testApply() {
		Individual indiv1 = new Individual("000");
		Individual indiv2 = new Individual("001");
		ArrayList<Individual> indivList = new ArrayList<Individual>();
		indivList.add(indiv1);
		indivList.add(indiv2);
		Population pop = new Population(indivList);
		/*
		 * �K�p�O��Ō̌Q���ω����Ȃ����Ƃ��e�X�g.
		 */
		operator.apply(pop);
		assertEquals("000", pop.getIndividualAt(0).toString());
		assertEquals("001", pop.getIndividualAt(1).toString());
		/*
		 * �K�p�O��œK���x�]���񐔂������邱�Ƃ��e�X�g.
		 */
		long before = FitnessManager.getTotalEvalNum();
		operator.apply(pop);
		// �K���x�]���񐔂͌̕�2������.
		assertEquals(before + 2, FitnessManager.getTotalEvalNum());
		/*
		 * �K�p�O��ŃG���[�g���X�V����邱�Ƃ��e�X�g.
		 */
		// ���̎��_�ł̃G���[�g��"001"
		assertTrue(indiv2.equals(FitnessManager.getElite()));
		indiv1.setGeneAt(0, 1);
		indiv1.setGeneAt(1, 1); // indiv1 <- "110"
		operator.apply(pop);
		// �����ŃG���[�g��"110"�ɍX�V�����
		assertTrue(indiv1.equals(FitnessManager.getElite()));
	}

	/**
	 * @see operator.EvaluateFitness#getName()
	 */
	public void testGetName() {
		assertEquals("EvaluateFitness", operator.getName());
	}

	/**
	 * @see operator.EvaluateFitness#getParameterInfo()
	 */
	public void testGetParameterInfo() {
		// �p�����[�^�͂Ȃ��̂ŋ󕶎��񂪕Ԃ��Ă���.
		assertEquals("", operator.getParameterInfo());
	}

	/**
	 * @see operator.EvaluateFitness#toString()
	 */
	public void testToString() {
		assertEquals("EvaluateFitness{}", operator.toString());
	}

}
