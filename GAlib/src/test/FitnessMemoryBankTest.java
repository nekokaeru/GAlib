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
		assertEquals(3.0,indiv.fitness(),0);//適応度3
		//デフォルトは適応度を計算しても登録されない．
		assertFalse(memory_.containsIndividual(indiv));
		
		//登録した
		memory_.put(indiv,indiv.fitness());
		assertTrue(memory_.containsIndividual(indiv));
		//深いコピーも登録されることになる．
		assertTrue(memory_.containsIndividual(indiv.clone()));
		//適応度は等しい．
		assertEquals(3.0,memory_.getFitness(indiv),0);
		//深いコピーをキーにしても結果は同じ．
		assertEquals(3.0,memory_.getFitness(indiv.clone()),0);
		
		indiv.setGeneAt(0,0);//遺伝子型を変更． 00101
		//HashMap のキーに深いコピーが渡っていることのテスト
		assertFalse(memory_.containsIndividual(indiv));
		indiv.setGeneAt(0,1);
		assertTrue(memory_.containsIndividual(indiv));
		//適応度が等しくても遺伝子型が異なれば登録はされていない．
		Individual indiv2 = new Individual("11100");
		assertEquals(3.0,indiv2.fitness(),0);//適応度3
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
		assertFalse(memory_.put(indiv1,indiv1.fitness())); //同じもの
		assertEquals(1,memory_.size());		
		memory_.put(indiv2,indiv2.fitness());
		assertEquals(2,memory_.size());		
		memory_.remove(indiv1); //ひとつremove
		assertEquals(1,memory_.size());
		assertFalse(memory_.remove(indiv1)); //ないものをremoveしてもサイズは不変．
		assertEquals(1,memory_.size());		
		memory_.remove(indiv2.clone()); //深いコピーでも削除可能．
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
		assertEquals(2,memory_.size()); //同じものでは増えない．			
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
		//始めは上限なし．
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
		//上限を3個体にする
		memory_.setMaxMemorySize(3);
		assertEquals(3,memory_.getMaxMemorySize());
		//一番古い個体が削られた．
		assertFalse(memory_.containsIndividual(indiv1));
		//上限を1個体にする
		memory_.setMaxMemorySize(1);		
		assertEquals(1,memory_.getMaxMemorySize());
		assertTrue(memory_.containsIndividual(indiv4)); //最新の個体のみが残る．
		//上限が１なので追加すると前のものが消える．
		memory_.put(indiv1,indiv1.fitness());
		assertEquals(1,memory_.getMaxMemorySize());
		assertFalse(memory_.containsIndividual(indiv4)); //消された．		
		assertTrue(memory_.containsIndividual(indiv1)); //最新の個体のみ残る．
		
		memory_.clear();
		memory_.setMaxMemorySize(100);
		for (int i = 0; i < 200; i++) {
			//遺伝子型が重ならない個体を追加
			memory_.put(new Individual(String.valueOf(i)),0.0); //適応度はどうでもいい．
			if(i < 100){
				assertEquals(i+1,memory_.size());
				assertEquals(100,memory_.getMaxMemorySize());
			}else{
				assertEquals(100,memory_.size());
				assertEquals(100,memory_.getMaxMemorySize());
			}
		}
		for (int i = 0; i < 200; i++) {
			//すでに，上のループですべて追加されている．
			memory_.put(new Individual(String.valueOf(i)),0.0); //適応度はどうでもいい．
			assertEquals(100,memory_.size()); //上限は越さない．
			assertEquals(100,memory_.getMaxMemorySize());
		}		
		memory_.setMaxMemorySize(-1);//無制限
		assertEquals(100,memory_.size()); //サイズは不変
	}

}
