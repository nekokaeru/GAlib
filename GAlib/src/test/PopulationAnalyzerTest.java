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
import java.util.HashMap;
import java.util.Random;

import population.Individual;
import population.Population;
import population.PopulationAnalyzer;

import fitness.FitnessManager;

import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */

public class PopulationAnalyzerTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PopulationAnalyzerTest.class);
	}

	public PopulationAnalyzerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		FitnessManager.reset();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		FitnessManager.reset();
	}

	/*
	 * Test method for 'population.PopulationAnalyzer.printPopulation(Population)'
	 */
	public void testPrintPopulation() {

	}

	/*
	 * Test method for 'population.PopulationAnalyzer.printElite()'
	 */
	public void testPrintElite() {

	}

	/*
	 * Test method for 'population.PopulationAnalyzer.fitnessInfo(Population)'
	 */
	public void testFitnessInfo() {
		ArrayList<Individual> indivList = new ArrayList<Individual>();
		indivList.add(new Individual(new Number[] { 0, 0, 0, 0 }));
		Population pop = new Population(indivList);

		HashMap<String,Double> h = PopulationAnalyzer.fitnessInfo(pop);
		assertEquals(h.get("max"), 0.0, 0.0);
		assertEquals(h.get("min"), 0.0, 0.0);
		assertEquals(h.get("mean"), 0.0, 0.0);


		indivList.clear();
		indivList.add(new Individual(new Number[] { 1, 1, 1, 1 }));
		indivList.add(new Individual(new Number[] { 0, 0, 0, 0 }));

		h = PopulationAnalyzer.fitnessInfo(pop);
		assertEquals(h.get("max"), 4.0,0.0);
		assertEquals(h.get("min"), 0.0, 0.0);
		assertEquals(h.get("mean"), 2.0, 0.0);
		
		indivList.clear();
		indivList.add(new Individual(new Number[] { 1, 1, 0, 0 }));
		indivList.add(new Individual(new Number[] { 1, 1, 1, 1 }));
		h = PopulationAnalyzer.fitnessInfo(pop);
		assertEquals(h.get("max"), 4.0,0.0);
		assertEquals(h.get("min"), 2.0, 0.0);
		assertEquals(h.get("mean"), 3.0, 0.0);
		
				
	}

	/*
	 * Test method for 'population.PopulationAnalyzer.genotypeNum(Population)'
	 */
	public void testGenotypeNum() {
		ArrayList<Individual> indivList = new ArrayList<Individual>();
		indivList.add(new Individual(new Number[] { 0, 0, 0, 0 }));
		Population pop = new Population(indivList);
		
		assertEquals(PopulationAnalyzer.genotypeNum(pop),1);
		
		indivList.add(new Individual(new Number[] { 0, 0, 0, 1 }));
		pop = new Population(indivList);
		indivList.add(new Individual(new Number[] { 0, 0, 1, 0 }));
		pop = new Population(indivList);
		indivList.add(new Individual(new Number[] { 0, 1, 0, 0 }));
		pop = new Population(indivList);

		assertEquals(PopulationAnalyzer.genotypeNum(pop),4);

		indivList.add(new Individual(new Number[] { 0, 0, 0, 1 }));
		pop = new Population(indivList);
		indivList.add(new Individual(new Number[] { 0, 0, 1, 0 }));
		pop = new Population(indivList);
		indivList.add(new Individual(new Number[] { 0, 1, 0, 0 }));
		pop = new Population(indivList);

		assertEquals(PopulationAnalyzer.genotypeNum(pop),4);

	}

	/*
	 * Test method for 'population.PopulationAnalyzer.entropy(Population)'
	 */
	public void testEntropy() {
		ArrayList<Individual> indivList = new ArrayList<Individual>();
		indivList.add(new Individual(new Number[] { 1, 0, 1, 0 }));
		Population pop = new Population(indivList);
		assertEquals(PopulationAnalyzer.entropy(pop), 0.0, 0.0);

		indivList.clear();
		indivList.add(new Individual(new Number[] { 1, 0, 1, 0 }));
		indivList.add(new Individual(new Number[] { 1, 0, 1, 1 }));
		assertEquals(PopulationAnalyzer.entropy(pop), -0.5 * Math.log(0.5) - 0.5
				* Math.log(0.5), 0.0);

		indivList.clear();
		indivList.add(new Individual(new Number[] { 1, 0, 0, 0 }));
		indivList.add(new Individual(new Number[] { 1, 0, 1, 1 }));
		assertEquals(PopulationAnalyzer.entropy(pop),
				2 * (-0.5 * Math.log(0.5) - 0.5 * Math.log(0.5)), 0.0);

		indivList.clear();
		indivList.add(new Individual(new Number[] { 1, 0, 1, 0 }));
		indivList.add(new Individual(new Number[] { 1, 0, 1, 1 }));
		indivList.add(new Individual(new Number[] { 1, 0, 1, 1 }));
		assertEquals(PopulationAnalyzer.entropy(pop), -1.0 / 3.0 * Math.log(1.0 / 3.0)
				- 2.0 / 3.0 * Math.log(2.0 / 3.0), TestParameters.DOUBLE_ERROR);

		indivList.clear();
		indivList.add(new Individual(new Number[] { 1, 0, 0, 0 }));
		indivList.add(new Individual(new Number[] { 1, 0, 1, 1 }));
		indivList.add(new Individual(new Number[] { 1, 0, 1, 1 }));
		assertEquals(PopulationAnalyzer.entropy(pop), 2 * (-1.0 / 3.0
				* Math.log(1.0 / 3.0) - 2.0 / 3.0 * Math.log(2.0 / 3.0)), TestParameters.DOUBLE_ERROR);

		/*
		 * 統計的テスト 遺伝子長 3000 の個体を２つ用意し，ランダムに遺伝子を決める． 遺伝子座の遺伝子が異なるのは期待値 1500 で，
		 * 標準偏差は Math.sqrt(3000 * p * (1 - p)) = 27.386127875258307
		 */
		indivList.clear();
		Number[] chromosome = new Number[3000];
		Number[] chromosome2 = new Number[3000];
		Random r = new Random();
		for (int i = 0; i < chromosome.length; i++) {
			chromosome[i] = r.nextInt(2);
		}
		for (int i = 0; i < chromosome2.length; i++) {
			chromosome2[i] = r.nextInt(2);
		}
		indivList.add(new Individual(chromosome));
		indivList.add(new Individual(chromosome2));
		double p = 1.0 / 2.0;
		double E = 3000 * p, SD = Math.sqrt(3000 * p * (1 - p));
		assertEquals(PopulationAnalyzer.entropy(pop), E
				* (-1.0 / 2.0 * Math.log(1.0 / 2.0) - 1.0 / 2.0 * Math
						.log(1.0 / 2.0)), SD
				* 4
				* (-1.0 / 2.0 * Math.log(1.0 / 2.0) - 1.0 / 2.0 * Math
						.log(1.0 / 2.0)));
	}

}
