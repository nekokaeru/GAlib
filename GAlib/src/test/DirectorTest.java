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
import gabuilder.DefaultGABuilder;
import gabuilder.Director;
import gabuilder.GABase;
import gabuilder.GAParameters;
import gabuilder.IGABuilder;
import gabuilder.TDGABuilder;
import junit.framework.TestCase;
import util.LocalTestException;
import util.MyRandom;

/**
 * @author mori
 * @version 1.0
 */
public class DirectorTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DirectorTest.class);
	}

	/**
	 * Constructor for DirectorTest.
	 * @param name
	 */
	public DirectorTest(String name) {
		super(name);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'gabuilder.Director.Director(IGABuilder)'
	 */
	public void testDirector() {
		try {
			DefaultGABuilder builder = new DefaultGABuilder();
			Director d = new Director(builder);
			d.constract();
			assertEquals(GAParameters.DEFAULT_POPULATION_SIZE, builder
					.buildGA().getPopulation().getPopulationSize());
			IGABuilder builder2 = new DefaultGABuilder();
			d = new Director(builder2);
			d.constract();
			assertEquals(GAParameters.DEFAULT_POPULATION_SIZE, builder
					.buildGA().getPopulation().getPopulationSize());
			IGABuilder builder3 = new TDGABuilder();
			d = new Director(builder3);
			d.constract();
			assertEquals(GAParameters.DEFAULT_POPULATION_SIZE, builder
					.buildGA().getPopulation().getPopulationSize());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	/*
	 * Test method for 'gabuilder.Director.constract()'
	 */
	public void testConstract() {
		IGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		d.constract();
	}

	/*
	 * Test method for 'gabuilder.Director.setParameter(String[])'
	 */
	public void testSetParameter() {
		IGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		String[] s = { "-scaling1", "problem.function.LinearFunction",
				"-scaling1param", "1.5:-11", "-P", "problem.KnapsackProblem" };
		d.setParameter(s);
		d.constract();
		builder.buildGA();
		assertEquals(
				"Knapsack{itemNum:30,Linear{GRADIENT:1.5,INTERCEPT:-11.0}}",
				FitnessManager.getProblem().toString());

		Director d2 = new Director(builder);
		String[] s2 = { "-C", "operator.UniformCrossover", "-Cparam", "0.99",
				"-S", "operator.TournamentSelection", "-Sparam", "3",
				"-Mparam", "0.11", "-scaling2",
				"operator.transform.LinearScalingTransform", "-scaling2param",
				"1.2:-3.2" };
		d2.setParameter(s2);
		d2.constract();
		GABase ga = builder.buildGA();
		assertEquals("UniformCrossover{CROSSOVER_RATE:0.99}", ga
				.getOperatorList().get(0).toString());
		assertEquals("BitMutation{MUTATION_RATE:0.11}", ga.getOperatorList()
				.get(1).toString());
		assertEquals(
				"TournamentSelection{TOURNAMENT_SIZE:3,LinearScalingTransform{TOP_VALUE:1.2, BOTTOM_VALUE:-3.2}}",
				ga.getOperatorList().get(2).toString());
		assertEquals("Elitism{IS_ELITISM:true}", builder.buildGA()
				.getOperatorList().get(3).toString());

		MyRandom.reset();
		Director d3 = new Director(builder);
		String[] s3 = { "-popsize", "12", "-gsize", "34", "-clength", "5",
				"-printlevel", "4", "-output", "test.dat", "-viewer",
				"viewer.TextViewer", "-turbo", "true", "-memory", "true",
				"-seed", "-123" };
		d3.setParameter(s3);
		d3.constract();
		ga = builder.buildGA();
		// デフォルト．
		assertEquals("OnePointCrossover{CROSSOVER_RATE:0.6}", ga
				.getOperatorList().get(0).toString());
		assertEquals(12, ga.getPopulation().getPopulationSize());
		assertEquals(34, ga.getGenerationSize());
		// 遺伝子長
		assertEquals(5, ga.getPopulation().getIndividualAt(0).size());
		assertTrue(FitnessManager.isTurbo());
		assertTrue(FitnessManager.isMemory());
		assertEquals(-123, MyRandom.getSeed().intValue());
		MyRandom.reset();

		MyRandom.reset();
		Director d4 = new Director(builder);
		String[] s4 = { "-popsize", "123", "-gsize", "45", "-clength", "6",
				"-S", "operator.ThermoDynamicalSelection", "-Sparam", "0.98" };
		d4.setParameter(s4);
		d4.constract();
		ga = builder.buildGA();
		assertEquals(
				"ThermoDynamicalSelection{TEMPERATURE:0.98,isPackIndivList:false}",
				ga.getOperatorList().get(2).toString());
		MyRandom.reset();
	}

	/*
	 * Test method for 'gabuilder.Director.getGAParams()'
	 */
	public void testGetGAParams() {
		IGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		String[] s = { "-scaling1", "problem.function.LinearFunction",
				"-scaling1param", "1.5:-11", "-P", "problem.KnapsackProblem" };
		d.setParameter(s);
		GAParameters gaParam = d.getGAParams();
		assertEquals("problem.function.LinearFunction", gaParam
				.getParametersMap().get(GAParameters.FUNCTION));
		assertEquals("1.5:-11", gaParam.getParametersMap().get(
				GAParameters.FUNCTION_PARAMETER));
		assertEquals("problem.KnapsackProblem", gaParam.getParametersMap().get(
				GAParameters.PROBLEM));
		assertEquals(null, gaParam.getParametersMap().get(
				GAParameters.PROBLEM_PARAMETER));
	}

	/*
	 * Test method for 'gabuilder.Director.setGAParamsByCSVFile()'
	 */
	public void testSetGAParamsByCSVFiles() {
		IGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		String[] s = { "sampleParams.csv"};
		d.setParameter(s);
		GAParameters gaParam = d.getGAParams();		
		assertEquals("12345", gaParam.getParametersMap().get(GAParameters.SEED));
	}
	
	/*
	 * Test method for 'gabuilder.Director.localTest()'
	 */
	public void testLocalTest() {
		Director director = new Director(new DefaultGABuilder());
		try {
			director.localTest();
		} catch (LocalTestException e) {
			e.printStackTrace();
			fail();
		}
	}

}
