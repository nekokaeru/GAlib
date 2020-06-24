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
 * 
 */
package test;

import junit.framework.TestCase;
import operator.Elitism;
import population.Individual;
import population.Population;
import fitness.FitnessManager;

/**
 * @author mori
 * @version 1.0
 */
public class ElitismTest extends TestCase {
	/**
	 */
	private Elitism elitism_;
	public static void main(String[] args) {
		junit.textui.TestRunner.run(ElitismTest.class);
	}

	/**
	 * Constructor for ElitismTest.
	 * @param name
	 */
	public ElitismTest(String name) {
		super(name);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		elitism_=new Elitism();
		FitnessManager.reset();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		FitnessManager.reset();
	}

	/*
	 * Test method for 'operator.Elitism.apply(Population)'
	 */
	public void testApply() {
		Population pop = new Population(100,10); //100�́C10��`�q��
		for (Individual indiv : pop.getIndivList()) {
			indiv.setGeneAt(0,0);//���ׂĂ̑���`�q����0�ɂ���D�œK���� all 1.
		}
		FitnessManager.getFitnessArray(pop.getIndivList());//���ׂĂ̓K���x���v�Z�D
		//�n�߂̈�`�q�� 0 �Ȃ̂ŕK���G���[�g�̓K���x��10�ȉ��D
		assertTrue(FitnessManager.getElite().fitness()<10);
		Individual opt = new Individual("1111111111"); //�œK���D
		pop.setIndividualAt(0,opt); //�œK�̂��Z�b�g�D
		FitnessManager.getFitnessArray(pop.getIndivList());//���ׂĂ̓K���x���v�Z�D
		assertEquals(10.0,FitnessManager.getElite().fitness(),0);//�G���[�g�̓K���x�͓��R10
		pop.getIndividualAt(0).setGeneAt(0,0); //�œK�����󂷁D
		assertEquals(10.0,FitnessManager.getElite().fitness(),0);//�G���[�g�̓K���x��10�̂܂�		
		double[] allFitness = FitnessManager.getFitnessArray(pop.getIndivList());//���ׂĂ̓K���x���v�Z�D
		for (int i = 0; i < allFitness.length; i++) {
			assertTrue(allFitness[i]<10); //�̌Q���ɂ͍œK���͂Ȃ��D
		}
		elitism_.apply(pop);
		//100�̂Ȃ̂� 99 �͍Ō��index
		assertEquals(10,pop.getIndividualAt(99).fitness(),0);
		
	}

	/*
	 * Test method for 'operator.Elitism.getName()'
	 */
	public void testGetName() {
		assertEquals("Elitism",elitism_.getName());
	}
	
	/**
	 * �N���X Elitism �� setParameter(Object ... params) �̃e�X�g�D
	 * @see Elitism#setParameter
	 **/

	public void testSetParameter() {
		Elitism target = new Elitism();
		target.setParameter(); //�p�����[�^�����D
		target.setParameter((Object[])null); //�p�����[�^ null		
		Object p[] = new Object[0]; //�p�����[�^ ��z��
		target.setParameter(p); 				
		//�����̐������������Ɨ�O��f���D��
		try {
			target.setParameter(3);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * @see Elitism#getParameterInfo()
	 */
	public void testGetParameterInfo(){
		// �G���[�g��`���Ȃ��ɐݒ�
		elitism_.setElitism(false);
		assertEquals("IS_ELITISM:false", elitism_.getParameterInfo());
	}
	
	public void testToString(){
		// �G���[�g��`���Ȃ��ɐݒ�
		elitism_.setElitism(false);
		assertEquals("Elitism{IS_ELITISM:false}", elitism_.toString());
	}

}
