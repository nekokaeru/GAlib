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

import java.util.ArrayList;

import junit.framework.TestCase;
import population.Individual;
import population.Population;
import util.MyRandom;

/**
 * @author mori
 * @version 1.0
 */

public class PopulationTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PopulationTest.class);
	}

	public PopulationTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
	}

	/*
	 * �����l�ŏ���������Ă��邩�H
	 * Test method for 'population.Population.Population()'
	 */
	public void testPopulation() {
		Population pop = new Population();
		assertEquals(GAParameters.DEFAULT_POPULATION_SIZE,pop.getPopulationSize());
		ArrayList<Individual> list = pop.getIndivList();
		assertEquals(GAParameters.DEFAULT_POPULATION_SIZE,list.size());
		for (Individual indiv : list) {
			assertEquals(GAParameters.DEFAULT_CHROMOSOME_LENGTH,indiv.size());
		}
	}

	/*
	 * �����l�ȊO�ŏ���������邩�H
	 * Test method for 'population.Population.Population(int, int)'
	 */
	public void testPopulationIntInt() {
		Population pop = new Population(12,14);
		assertEquals(12,pop.getPopulationSize());
		ArrayList<Individual> list = pop.getIndivList();
		assertEquals(12,list.size());
		for (Individual indiv : list) {
			assertEquals(14,indiv.size());
		}
	}
	
	/*
	 * ArrayList �ŏ������ł��邩�H 
	 * Test method for 'population.Population.Population(ArrayList<Individual>)'
	 */
	public void testPopulationArrayListOfIndividual() {
		Individual i1 = new Individual("010");
		Individual i2 = new Individual("000");
		Individual i3 = new Individual("111");
		ArrayList<Individual> list = new ArrayList<Individual>();
		list.add(i1); list.add(i2); list.add(i3);
		//�󂢃R�s�[�������Ȃ��D
		Population pop = new Population(list);
		assertEquals(3,pop.getPopulationSize());
		assertEquals(i1,pop.getIndividualAt(0));
		assertEquals(i2,pop.getIndividualAt(1));
		assertEquals(i3,pop.getIndividualAt(2));
		i1.setGeneAt(1,0); //i1="000" �ɂȂ�D
		//�󂢃R�s�[�Ȃ̂ŁCpop �̒��g���ς��D
		assertTrue(pop.getIndividualAt(0).equals(i1));
		//i1 �� i2 �͈�`�q�^���������D
		assertTrue(pop.getIndividualAt(1).equals(i1));
	}
	
	/*
	 * init �ŏ������ł��邩?
	 * Test method for 'population.Population.init(int, int)'
	 */
	public void testInit() {
		Population pop = new Population(0,0); //�����
		assertEquals(0,pop.getPopulationSize());
		ArrayList<Individual> list = pop.getIndivList();
		assertEquals(0,list.size());
		for (Individual indiv : list) {
			assertEquals(0,indiv.size());
		}		
		pop.init(5,7); //5�́C7��`�q��
		assertEquals(5,pop.getPopulationSize());
		list = pop.getIndivList();
		assertEquals(5,list.size());
		for (Individual indiv : list) {
			assertEquals(7,indiv.size());
		}		
	}

	/*
	 * �[���R�s�[
	 * Test method for 'population.Population.clone()'
	 */
	public void testClone() {
		Population pop = new Population(8,5);
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,1); // pop �̑S�̂̈�`�q�� 1 ��
			}
		}
		Population pop2 = pop.clone();
		assertEquals(8,pop.getPopulationSize());
		assertEquals(8,pop2.getPopulationSize());
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,2); // pop �̑S�̂̈�`�q�� 2 ��
				                      // pop2 �͉e�����󂯂Ă͂��߁D
			}
		}		
		for (int i = 0; i < 8; i++) {
			Individual i1 = pop.getIndivList().get(i);
			Individual i2 = pop2.getIndivList().get(i);
			for (int j = 0; j < 5; j++) {
				assertEquals(5,i1.size());
				assertEquals(5,i2.size());
				assertEquals(2,i1.getGeneAt(j)); //pop �� 2
				assertEquals(1,i2.getGeneAt(j));   //pop2 �� 1

			}
		}
	}


	/*
	 * �Q�Ƃ����ڕԂ���D
	 */
	public void testGetIndivList() {
		Population pop = new Population(7,6);
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,1); // pop �̑S�̂̈�`�q�� 1 ��
			}
		}
		Population pop2 = new Population(0,0); //�����
		assertEquals(pop.getPopulationSize(),7);
		assertEquals(pop2.getPopulationSize(),0);
		// ���ۂɂ͉��L�̏����������邱�Ƃ͂قƂ�ǂȂ��D�[���R�s�[�� clone ���g���ׂ��D
		pop2.setIndivList(pop.getIndivList()); //�Q�Ƃ����ړn��D
		assertEquals(pop2.getPopulationSize(),7);
		for (int i = 0; i < 7; i++) {
			Individual i1 = pop.getIndivList().get(i);
			Individual i2 = pop2.getIndivList().get(i);
			for (int j = 0; j < 5; j++) {
				assertEquals(i1.size(),6);
				assertEquals(i2.size(),6);
				assertEquals(i1.getGeneAt(j),1); //pop �� 1
				assertEquals(i2.getGeneAt(j),1);   //pop2 �� 1

			}
		}		
		
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,2); // pop �̑S�̂̈�`�q�� 2 ��
				                      // pop2 ���e�����󂯂�D
			}
		}		
		for (int i = 0; i < 7; i++) {
			Individual i1 = pop.getIndivList().get(i);
			Individual i2 = pop2.getIndivList().get(i);
			for (int j = 0; j < 5; j++) {
				assertEquals(i1.size(),6);
				assertEquals(i2.size(),6);
				assertEquals(i1.getGeneAt(j),2); //pop �� 2
				assertEquals(i2.getGeneAt(j),2);   //pop2 �� 2

			}
		}
	}

	/*
	 * ���ڎQ�Ƃ��Ԃ�D
	 */
	public void testGetIndividualAt() {
		Population pop = new Population(20,10);
		Individual indiv = pop.getIndividualAt(3);
		for (int i = 0; i < indiv.size(); i++) {
			indiv.setGeneAt(i,i); //pop �̒��g���ς���Ă���͂��D 
		}
		// i1, i2 �͓�������
		Individual i1 = pop.getIndividualAt(3);
		Individual i2 = pop.getIndivList().get(3);
		assertEquals(i1.size(),10);
		assertEquals(i2.size(),10);		
		for (int i = 0; i < i1.size(); i++) {
//			 ��̕ύX�Ɠ������ʂɂȂ�͂�
			assertEquals(i1.getGeneAt(i),i); 
			assertEquals(i2.getGeneAt(i),i);
		}
	}

	/*
	 * �󂢃R�s�[�ŃZ�b�g
	 */
	public void testSetIndivList() {
		ArrayList<Individual> list = new ArrayList<Individual>();
		Individual i1 = new Individual(2); //��`�q��2 
		Individual i2 = new Individual(2);
		// i1={4,5} i2={10,20}
		i1.setGeneAt(0,4);
		i1.setGeneAt(1,5);
		i2.setGeneAt(0,10);
		i2.setGeneAt(1,20);		
		list.add(i1);
		list.add(i2);
		Population pop = new Population();
		pop.setIndivList(list);
		assertEquals(pop.getPopulationSize(),2); //2��
		 //�Ⴄ�����������D		
		assertEquals(pop.getIndividualAt(0).getGeneAt(0),4);
		assertEquals(pop.getIndividualAt(0).getChromosome()[1],5);
		assertEquals(pop.getIndivList().get(1).getChromosome()[0],10);
		assertEquals(pop.getIndivList().get(1).getGeneAt(1),20); //�Ⴄ�����D
		//�Q�Ƃ��n���Ă��邱�Ƃ̊m�F�D
		i1.setGeneAt(1,0);
		assertEquals(pop.getIndividualAt(0).getGeneAt(1),0);
	}

	/*
	 * �󂢃R�s�[�ŃZ�b�g
	 */
	public void testSetIndividualAt() {
		Population pop = new Population(100,100);
		Individual indiv = new Individual(100);
		for (int i = 0; i < indiv.size(); i++) {
			indiv.setGeneAt(i,7);
		}
		//��L�̌̂�37�ԖڂɃZ�b�g
		pop.setIndividualAt(37,indiv);
		Individual i2 = pop.getIndividualAt(37);
		for (int i = 0; i < i2.size(); i++) {
			assertEquals(i2.getGeneAt(i),7);
		}
		//�Q�Ƃ��n���Ă��邱�Ƃ̃`�F�b�N�D
		indiv.setGeneAt(3,8); //�Z�b�g�����̂�3�Ԗڂ̈�`�q����8�ɂ���D
		assertEquals(pop.getIndivList().get(37).getGeneAt(3),8);
	}

	/*
	 * Test method for 'population.Population.toString()'
	 */
	public void testToString() {
		Individual i1 = new Individual(2); //��`�q��2 
		Individual i2 = new Individual(2);
		i1.setGeneAt(0,1);
		i1.setGeneAt(1,2);
		i2.setGeneAt(0,3);
		i2.setGeneAt(1,4);		
		Population pop = new Population(2,0); //2��
		pop.setIndividualAt(0,i1);
		pop.setIndividualAt(1,i2);
		assertEquals(pop.toString(),"12\n34\n");
	}
}
