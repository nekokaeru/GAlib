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
import fitness.FitnessMemoryBank;
import junit.framework.TestCase;
import population.Individual;
import util.MyRandom;
/**
 * @author mori
 * @version 1.0
 */

public class FitnessMemoryBankTest extends TestCase {
	/**
	 */
	private FitnessMemoryBank memory_;
	public static void main(String[] args) {
		junit.textui.TestRunner.run(FitnessMemoryBankTest.class);
	}

	public FitnessMemoryBankTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		memory_ = new FitnessMemoryBank();
		FitnessManager.reset();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		memory_.clear();
	}

	/*
	 * Test method for 'operator.FitnessMemoryBank.getFitness(Individual)'
	 */
	public void testGetFitness() {
		Individual indiv = new Individual("10101");
		assertFalse(memory_.containsIndividual(indiv));
		assertEquals(3.0,indiv.fitness(),0);//�K���x3
		//�f�t�H���g�͓K���x���v�Z���Ă��o�^����Ȃ��D
		assertFalse(memory_.containsIndividual(indiv));
		
		//�o�^����
		memory_.put(indiv,indiv.fitness());
		assertTrue(memory_.containsIndividual(indiv));
		//�[���R�s�[���o�^����邱�ƂɂȂ�D
		assertTrue(memory_.containsIndividual(indiv.clone()));
		//�K���x�͓������D
		assertEquals(3.0,memory_.getFitness(indiv),0);
		//�[���R�s�[���L�[�ɂ��Ă����ʂ͓����D
		assertEquals(3.0,memory_.getFitness(indiv.clone()),0);
		
		indiv.setGeneAt(0,0);//��`�q�^��ύX�D 00101
		//HashMap �̃L�[�ɐ[���R�s�[���n���Ă��邱�Ƃ̃e�X�g
		assertFalse(memory_.containsIndividual(indiv));
		indiv.setGeneAt(0,1);
		assertTrue(memory_.containsIndividual(indiv));
		//�K���x���������Ă���`�q�^���قȂ�Γo�^�͂���Ă��Ȃ��D
		Individual indiv2 = new Individual("11100");
		assertEquals(3.0,indiv2.fitness(),0);//�K���x3
		assertFalse(memory_.containsIndividual(indiv2));
	}

	/*
	 * Test method for 'operator.FitnessMemoryBank.containsIndividual(Individual)'
	 */
	public void testContainsIndividual() {
		Individual indiv = new Individual();
		assertFalse(memory_.containsIndividual(indiv));
		memory_.put(indiv,0.0);
		assertTrue(memory_.containsIndividual(indiv));
		assertTrue(memory_.containsIndividual(indiv.clone()));
	}

	/*
	 * Test method for 'operator.FitnessMemoryBank.put(Individual, Double)'
	 */
	public void testPut() {
		Individual indiv = new Individual();
		memory_.put(indiv,indiv.fitness());
		assertEquals(memory_.getFitness(indiv),indiv.fitness());
		assertEquals(memory_.getFitness(indiv.clone()),indiv.fitness());
	}

	/*
	 * Test method for 'operator.FitnessMemoryBank.remove(Individual)'
	 */
	public void testRemove() {
		Individual indiv1 = new Individual("00");
		Individual indiv2 = new Individual("01");
		memory_.put(indiv1,indiv1.fitness());
		assertEquals(1,memory_.size());
		assertFalse(memory_.put(indiv1,indiv1.fitness())); //��������
		assertEquals(1,memory_.size());		
		memory_.put(indiv2,indiv2.fitness());
		assertEquals(2,memory_.size());		
		memory_.remove(indiv1); //�ЂƂ�remove
		assertEquals(1,memory_.size());
		assertFalse(memory_.remove(indiv1)); //�Ȃ����̂�remove���Ă��T�C�Y�͕s�ρD
		assertEquals(1,memory_.size());		
		memory_.remove(indiv2.clone()); //�[���R�s�[�ł��폜�\�D
		assertEquals(0,memory_.size());			
	}

	/*
	 * Test method for 'operator.FitnessMemoryBank.size()'
	 */
	public void testSize() {
		Individual indiv1 = new Individual("000");
		Individual indiv2 = new Individual("010");
		Individual indiv3 = new Individual("010");
		assertEquals(0,memory_.size());			
		memory_.put(indiv1,indiv1.fitness());
		assertEquals(1,memory_.size());			
		memory_.put(indiv2,indiv1.fitness());
		assertEquals(2,memory_.size());			
		memory_.put(indiv3,indiv1.fitness());
		assertEquals(2,memory_.size()); //�������̂ł͑����Ȃ��D			
	}

	/*
	 * Test method for 'operator.FitnessMemoryBank.clear()'
	 */
	public void testClear() {
		Individual indiv=new Individual();
		memory_.put(indiv,indiv.fitness());
		assertEquals(memory_.getFitness(indiv),indiv.fitness());
		assertEquals(1,memory_.size());
		memory_.clear();
		assertEquals(null,memory_.getFitness(indiv));
		assertEquals(0,memory_.size());
	}

	/*
	 * Test method for 'operator.FitnessMemoryBank.getMaxMemorySize()'
	 */
	public void testGetMaxMemorySize() {
		//�n�߂͏���Ȃ��D
		assertEquals(-1,memory_.getMaxMemorySize());
		Individual indiv1 = new Individual("00");
		Individual indiv2 = new Individual("01");
		Individual indiv3 = new Individual("10");
		Individual indiv4 = new Individual("11");
		memory_.put(indiv1,indiv1.fitness());
		memory_.put(indiv2,indiv1.fitness());
		memory_.put(indiv3,indiv1.fitness());
		memory_.put(indiv4,indiv1.fitness());
		assertEquals(4,memory_.size());
		//�����3�̂ɂ���
		memory_.setMaxMemorySize(3);
		assertEquals(3,memory_.getMaxMemorySize());
		//��ԌÂ��̂����ꂽ�D
		assertFalse(memory_.containsIndividual(indiv1));
		//�����1�̂ɂ���
		memory_.setMaxMemorySize(1);		
		assertEquals(1,memory_.getMaxMemorySize());
		assertTrue(memory_.containsIndividual(indiv4)); //�ŐV�̌݂̂̂��c��D
		//������P�Ȃ̂Œǉ�����ƑO�̂��̂�������D
		memory_.put(indiv1,indiv1.fitness());
		assertEquals(1,memory_.getMaxMemorySize());
		assertFalse(memory_.containsIndividual(indiv4)); //�����ꂽ�D		
		assertTrue(memory_.containsIndividual(indiv1)); //�ŐV�̌̂̂ݎc��D
		
		memory_.clear();
		memory_.setMaxMemorySize(100);
		for (int i = 0; i < 200; i++) {
			//��`�q�^���d�Ȃ�Ȃ��̂�ǉ�
			memory_.put(new Individual(String.valueOf(i)),0.0); //�K���x�͂ǂ��ł������D
			if(i < 100){
				assertEquals(i+1,memory_.size());
				assertEquals(100,memory_.getMaxMemorySize());
			}else{
				assertEquals(100,memory_.size());
				assertEquals(100,memory_.getMaxMemorySize());
			}
		}
		for (int i = 0; i < 200; i++) {
			//���łɁC��̃��[�v�ł��ׂĒǉ�����Ă���D
			memory_.put(new Individual(String.valueOf(i)),0.0); //�K���x�͂ǂ��ł������D
			assertEquals(100,memory_.size()); //����͉z���Ȃ��D
			assertEquals(100,memory_.getMaxMemorySize());
		}		
		memory_.setMaxMemorySize(-1);//������
		assertEquals(100,memory_.size()); //�T�C�Y�͕s��
	}

}
