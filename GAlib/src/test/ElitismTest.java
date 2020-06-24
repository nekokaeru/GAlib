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
		Population pop = new Population(100,10); //100個体，10遺伝子座
		for (Individual indiv : pop.getIndivList()) {
			indiv.setGeneAt(0,0);//すべての第一遺伝子座を0にする．最適解は all 1.
		}
		FitnessManager.getFitnessArray(pop.getIndivList());//すべての適応度を計算．
		//始めの遺伝子が 0 なので必ずエリートの適応度は10以下．
		assertTrue(FitnessManager.getElite().fitness()<10);
		Individual opt = new Individual("1111111111"); //最適解．
		pop.setIndividualAt(0,opt); //最適個体をセット．
		FitnessManager.getFitnessArray(pop.getIndivList());//すべての適応度を計算．
		assertEquals(10.0,FitnessManager.getElite().fitness(),0);//エリートの適応度は当然10
		pop.getIndividualAt(0).setGeneAt(0,0); //最適解を壊す．
		assertEquals(10.0,FitnessManager.getElite().fitness(),0);//エリートの適応度は10のまま		
		double[] allFitness = FitnessManager.getFitnessArray(pop.getIndivList());//すべての適応度を計算．
		for (int i = 0; i < allFitness.length; i++) {
			assertTrue(allFitness[i]<10); //個体群中には最適解はない．
		}
		elitism_.apply(pop);
		//100個体なので 99 は最後のindex
		assertEquals(10,pop.getIndividualAt(99).fitness(),0);
		
	}

	/*
	 * Test method for 'operator.Elitism.getName()'
	 */
	public void testGetName() {
		assertEquals("Elitism",elitism_.getName());
	}
	
	/**
	 * クラス Elitism の setParameter(Object ... params) のテスト．
	 * @see Elitism#setParameter
	 **/

	public void testSetParameter() {
		Elitism target = new Elitism();
		target.setParameter(); //パラメータ無し．
		target.setParameter((Object[])null); //パラメータ null		
		Object p[] = new Object[0]; //パラメータ 空配列
		target.setParameter(p); 				
		//引数の数がおかしいと例外を吐く．数
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
		// エリート主義をなしに設定
		elitism_.setElitism(false);
		assertEquals("IS_ELITISM:false", elitism_.getParameterInfo());
	}
	
	public void testToString(){
		// エリート主義をなしに設定
		elitism_.setElitism(false);
		assertEquals("Elitism{IS_ELITISM:false}", elitism_.toString());
	}

}
