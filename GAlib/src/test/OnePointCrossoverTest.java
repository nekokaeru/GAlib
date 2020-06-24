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

/*
 * Created on 2005/07/14
 * 
 */
package test;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import operator.OnePointCrossover;
import population.Individual;
import population.Population;
import util.MyRandom;
import util.NStatistics;

/**
 * @author mori
 * @version 1.0
 */
public class OnePointCrossoverTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(OnePointCrossoverTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
	}

	/**
	 * Constructor for OnePointCrossoverTest.
	 * 
	 * @param arg0
	 */
	public OnePointCrossoverTest(String arg0) {
		super(arg0);
	}

	public void testCrossover() {
		OnePointCrossover opx = new OnePointCrossover();
		opx.setCrossoverRate(1);
		Number[] p1_c = new Number[10];
		Number[] p2_c = new Number[10];
		int index0, index1, maxi = 3000;
		int[] sum = new int[p1_c.length];
		Arrays.fill(sum, 0);
		Individual p1 = new Individual();
		Individual p2 = new Individual();
		// all 0 �� all 1 �̌̂���_��������D
		for (int i = 0; i < maxi; i++) {
			Arrays.fill(p1_c, 0);
			Arrays.fill(p2_c, 1);
			p1.setChromosome(p1_c);
			p2.setChromosome(p2_c);
			opx.crossover(p1, p2); //����
			//��_�����͔j��I�D(p1, p2 �𒼐ڏ���������)
			ArrayList<Number> chrom1 = new ArrayList<Number>();
			ArrayList<Number> chrom2 = new ArrayList<Number>();
			for (int j = 0; j < p1.size(); j++) {
				//�o�����q�̈�`�q�^�����ꂼ�� ArrayList �ɓ����D
				chrom1.add(p1.getGeneAt(j)); 
				chrom2.add(p2.getGeneAt(j));
			}
			for (int j = 0; j < chrom1.size(); j++) {
				sum[j] += (Integer)chrom1.get(j);
			}
			//p1 �̌㔼���؂ꂽ�̂́C0�݂̂������񑱂��C���̌�1�݂̂������Ă���͂��D
			index0 = chrom1.lastIndexOf(chrom1.get(0));
			index1 = chrom1.indexOf(chrom1.get(chrom1.size() - 1));
			assertEquals(index0 + 1, index1);
			//p2 �̌㔼���؂ꂽ�̂́C1�݂̂������񑱂��C���̌�0�݂̂������Ă���͂��D			
			index0 = chrom2.lastIndexOf(chrom2.get(0));
			index1 = chrom2.indexOf(chrom2.get(chrom2.size() - 1));
			assertEquals(index0 + 1, index1);
		}
		// p1 �̌㔼���؂ꂽ�q�̍��[�͕K��0�C�E�[�͕K��1�D��_�����͕K��������؂�D
		assertTrue(sum[0] == 0);
		assertTrue(sum[sum.length - 1] == maxi);
		for (int i = 1; i < sum.length - 1; i++) {
			double p = (1.0 / (p1.size() - 1)) * i; // ��`�q�� i �� 1 �������m���D
			assertTrue(NStatistics.binomialTest(maxi,sum[i],p,TestParameters.SIGMA_MARGIN));
		}
	}

	public void testApply() {
		Population pop = new Population();
		OnePointCrossover ox = new OnePointCrossover();
		Number[] c1 = { 0, 0, 0 };
		Number[] c2 = { 1, 1, 1 };
		Individual i1 = new Individual(c1);
		Individual i2 = new Individual(c2);
		ArrayList<Individual> list = new ArrayList<Individual>();
		list.add(i1);
		list.add(i2);
		pop.setIndivList(list);
		int maxi = 3000;
		int sum = 0;
		for (double crate = 0; crate <= 1; crate += 0.2) {
			ox.setCrossoverRate(crate);
			sum = 0;
			for (int i = 0; i < maxi; i++) {
				Population tmp = pop.clone();
				ox.apply(tmp);
				// shuffle ���Ă���̂�, �����Ȃ̂� 0 �� 1 ���킩��Ȃ��D
				if (tmp.getIndividualAt(0).equals(
						pop.getIndividualAt(0))
						|| tmp.getIndividualAt(0).equals(
								pop.getIndividualAt(1))) {
					// �������N���Ă��Ȃ���ΐ��F�̂ɕω��Ȃ��D
					assertTrue(tmp.getIndividualAt(1).equals(
							pop.getIndividualAt(1))
							|| tmp.getIndividualAt(1).equals(
									pop.getIndividualAt(0)));
				} else {
					// �������N���Ă���ΐ��F�̂��ω��D
					assertFalse(tmp.getIndividualAt(1).equals(
							pop.getIndividualAt(1)));
					assertFalse(tmp.getIndividualAt(1).equals(
							pop.getIndividualAt(0)));
					sum++; // �������N�����񐔁D
				}
			}
			assertTrue(NStatistics.binomialTest(maxi,sum,ox.getCrossoverRate(),TestParameters.SIGMA_MARGIN));			
		}
	}

	public void testGetName(){
		OnePointCrossover ox = new OnePointCrossover();
		assertEquals("OnePointCrossover",ox.getName());
	}
	/**
	 * �N���X OnePointCrossover �� setParameter(Object ... params) �̃e�X�g�D
	 * @see OnePointCrossover#setParameter
	 **/

	public void testSetParameter() {
		OnePointCrossover target = new OnePointCrossover();

		//TODO �e�X�g�L�q�D�ȉ��͎����l����p�����[�^�Ƃ���ꍇ�̗�D
		//���l�������̏ꍇ�� 1. ���l�C 2. ������C 3. �z���n�����ꍇ�̃e�X�g������
		target.setParameter(1.0);
		assertEquals(1.0,target.getCrossoverRate(), 0.0);
		target.setParameter("2");
		assertEquals(2.0,target.getCrossoverRate(), 0.0);
		Object[] tmpParam = { new Double(3) };
		target.setParameter(tmpParam);
		assertEquals(3.0,target.getCrossoverRate(), 0.0);

		//TODO ��O�̔����e�X�g�D
		//�����̐������������Ɨ�O�����D�p�����[�^��
		try {
			target.setParameter("1", "2");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		//�����̓��e�����������Ɨ�O�����D������̓��e
		try {
			target.setParameter("num");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	/**
	 * @see operator.AbstractCrossover#getParameterInfo()
	 */
	public void testGetParameterInfo(){
		OnePointCrossover target = new OnePointCrossover();
		target.setCrossoverRate(1.2);
		assertEquals(target.getParameterInfo(),"CROSSOVER_RATE:1.2");
	}
	
	/**
	 * @see operator.AbstractCrossover#toString()
	 */
	public void testToString(){
		OnePointCrossover target = new OnePointCrossover();
		// �������� 1.2 �ɐݒ�
		target.setCrossoverRate(1.2);
		assertEquals("OnePointCrossover{CROSSOVER_RATE:1.2}", target.toString());
	}
}
