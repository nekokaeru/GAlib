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
	 * 初期値で初期化されているか？
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
	 * 初期値以外で初期化されるか？
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
	 * ArrayList で初期化できるか？ 
	 * Test method for 'population.Population.Population(ArrayList<Individual>)'
	 */
	public void testPopulationArrayListOfIndividual() {
		Individual i1 = new Individual("010");
		Individual i2 = new Individual("000");
		Individual i3 = new Individual("111");
		ArrayList<Individual> list = new ArrayList<Individual>();
		list.add(i1); list.add(i2); list.add(i3);
		//浅いコピーしかしない．
		Population pop = new Population(list);
		assertEquals(3,pop.getPopulationSize());
		assertEquals(i1,pop.getIndividualAt(0));
		assertEquals(i2,pop.getIndividualAt(1));
		assertEquals(i3,pop.getIndividualAt(2));
		i1.setGeneAt(1,0); //i1="000" になる．
		//浅いコピーなので，pop の中身も変わる．
		assertTrue(pop.getIndividualAt(0).equals(i1));
		//i1 と i2 は遺伝子型が等しい．
		assertTrue(pop.getIndividualAt(1).equals(i1));
	}
	
	/*
	 * init で初期化できるか?
	 * Test method for 'population.Population.init(int, int)'
	 */
	public void testInit() {
		Population pop = new Population(0,0); //空っぽ
		assertEquals(0,pop.getPopulationSize());
		ArrayList<Individual> list = pop.getIndivList();
		assertEquals(0,list.size());
		for (Individual indiv : list) {
			assertEquals(0,indiv.size());
		}		
		pop.init(5,7); //5個体，7遺伝子長
		assertEquals(5,pop.getPopulationSize());
		list = pop.getIndivList();
		assertEquals(5,list.size());
		for (Individual indiv : list) {
			assertEquals(7,indiv.size());
		}		
	}

	/*
	 * 深いコピー
	 * Test method for 'population.Population.clone()'
	 */
	public void testClone() {
		Population pop = new Population(8,5);
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,1); // pop の全個体の遺伝子を 1 に
			}
		}
		Population pop2 = pop.clone();
		assertEquals(8,pop.getPopulationSize());
		assertEquals(8,pop2.getPopulationSize());
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,2); // pop の全個体の遺伝子を 2 に
				                      // pop2 は影響を受けてはだめ．
			}
		}		
		for (int i = 0; i < 8; i++) {
			Individual i1 = pop.getIndivList().get(i);
			Individual i2 = pop2.getIndivList().get(i);
			for (int j = 0; j < 5; j++) {
				assertEquals(5,i1.size());
				assertEquals(5,i2.size());
				assertEquals(2,i1.getGeneAt(j)); //pop は 2
				assertEquals(1,i2.getGeneAt(j));   //pop2 は 1

			}
		}
	}


	/*
	 * 参照が直接返える．
	 */
	public void testGetIndivList() {
		Population pop = new Population(7,6);
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,1); // pop の全個体の遺伝子を 1 に
			}
		}
		Population pop2 = new Population(0,0); //空っぽ
		assertEquals(pop.getPopulationSize(),7);
		assertEquals(pop2.getPopulationSize(),0);
		// 実際には下記の書き方をすることはほとんどない．深いコピーの clone を使うべき．
		pop2.setIndivList(pop.getIndivList()); //参照が直接渡る．
		assertEquals(pop2.getPopulationSize(),7);
		for (int i = 0; i < 7; i++) {
			Individual i1 = pop.getIndivList().get(i);
			Individual i2 = pop2.getIndivList().get(i);
			for (int j = 0; j < 5; j++) {
				assertEquals(i1.size(),6);
				assertEquals(i2.size(),6);
				assertEquals(i1.getGeneAt(j),1); //pop は 1
				assertEquals(i2.getGeneAt(j),1);   //pop2 は 1

			}
		}		
		
		for (Individual indiv : pop.getIndivList()) {
			for (int i = 0; i < indiv.size(); i++) {
				indiv.setGeneAt(i,2); // pop の全個体の遺伝子を 2 に
				                      // pop2 も影響を受ける．
			}
		}		
		for (int i = 0; i < 7; i++) {
			Individual i1 = pop.getIndivList().get(i);
			Individual i2 = pop2.getIndivList().get(i);
			for (int j = 0; j < 5; j++) {
				assertEquals(i1.size(),6);
				assertEquals(i2.size(),6);
				assertEquals(i1.getGeneAt(j),2); //pop は 2
				assertEquals(i2.getGeneAt(j),2);   //pop2 は 2

			}
		}
	}

	/*
	 * 直接参照が返る．
	 */
	public void testGetIndividualAt() {
		Population pop = new Population(20,10);
		Individual indiv = pop.getIndividualAt(3);
		for (int i = 0; i < indiv.size(); i++) {
			indiv.setGeneAt(i,i); //pop の中身も変わっているはず． 
		}
		// i1, i2 は同じもの
		Individual i1 = pop.getIndividualAt(3);
		Individual i2 = pop.getIndivList().get(3);
		assertEquals(i1.size(),10);
		assertEquals(i2.size(),10);		
		for (int i = 0; i < i1.size(); i++) {
//			 上の変更と同じ結果になるはず
			assertEquals(i1.getGeneAt(i),i); 
			assertEquals(i2.getGeneAt(i),i);
		}
	}

	/*
	 * 浅いコピーでセット
	 */
	public void testSetIndivList() {
		ArrayList<Individual> list = new ArrayList<Individual>();
		Individual i1 = new Individual(2); //遺伝子長2 
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
		assertEquals(pop.getPopulationSize(),2); //2個体
		 //違う取り方を試す．		
		assertEquals(pop.getIndividualAt(0).getGeneAt(0),4);
		assertEquals(pop.getIndividualAt(0).getChromosome()[1],5);
		assertEquals(pop.getIndivList().get(1).getChromosome()[0],10);
		assertEquals(pop.getIndivList().get(1).getGeneAt(1),20); //違う取り方．
		//参照が渡っていることの確認．
		i1.setGeneAt(1,0);
		assertEquals(pop.getIndividualAt(0).getGeneAt(1),0);
	}

	/*
	 * 浅いコピーでセット
	 */
	public void testSetIndividualAt() {
		Population pop = new Population(100,100);
		Individual indiv = new Individual(100);
		for (int i = 0; i < indiv.size(); i++) {
			indiv.setGeneAt(i,7);
		}
		//上記の個体を37番目にセット
		pop.setIndividualAt(37,indiv);
		Individual i2 = pop.getIndividualAt(37);
		for (int i = 0; i < i2.size(); i++) {
			assertEquals(i2.getGeneAt(i),7);
		}
		//参照が渡っていることのチェック．
		indiv.setGeneAt(3,8); //セットした個体の3番目の遺伝子座を8にする．
		assertEquals(pop.getIndivList().get(37).getGeneAt(3),8);
	}

	/*
	 * Test method for 'population.Population.toString()'
	 */
	public void testToString() {
		Individual i1 = new Individual(2); //遺伝子長2 
		Individual i2 = new Individual(2);
		i1.setGeneAt(0,1);
		i1.setGeneAt(1,2);
		i2.setGeneAt(0,3);
		i2.setGeneAt(1,4);		
		Population pop = new Population(2,0); //2個体
		pop.setIndividualAt(0,i1);
		pop.setIndividualAt(1,i2);
		assertEquals(pop.toString(),"12\n34\n");
	}
}
