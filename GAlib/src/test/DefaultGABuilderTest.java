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
import junit.framework.TestCase;
import util.MyRandom;

/**
 * @author mori
 * @version 1.0
 */
public class DefaultGABuilderTest extends TestCase {
	DefaultGABuilder gabuilder_;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(DefaultGABuilderTest.class);
	}

	/**
	 * Constructor for DefaultGABuilderTest.
	 * @param name
	 */
	public DefaultGABuilderTest(String name) {
		super(name);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		FitnessManager.reset();
		gabuilder_=new DefaultGABuilder();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'gabuilder.DefaultGABuilder.buildGA()'
	 */
	public void testBuildGA() {
		IGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		String[] s = {"-gsize","777"};
		d.setParameter(s);
		d.constract();
		GABase ga = builder.buildGA();
		assertEquals(777,ga.getGenerationSize());
	}


	public void testSetGAParameters() {
		MyRandom.reset();
		GAParameters gaparam = new GAParameters();
		gaparam.getParametersMap().put(GAParameters.SEED,"618");
		gaparam.getParametersMap().put(GAParameters.PROBLEM,"problem.BinaryNumberProblem");
		gaparam.getParametersMap().put(GAParameters.POPULATION_SIZE,"36");		
		IGABuilder builder = new DefaultGABuilder();
		// builder ÇÕ Director Ç»ÇµÇ≈Ç‡ GAParameter Ç©ÇÁíºê⁄ê›íËÇ∑ÇÈÇ±Ç∆Ç‡Ç≈Ç´ÇÈÅD
		builder.setGAParameters(gaparam); 
		GABase ga = builder.buildGA();
		assertEquals("BinaryNumber{}", FitnessManager.getProblem().toString());
		assertEquals(618, MyRandom.getSeed().intValue());		
		assertEquals(36, ga.getPopulation().getPopulationSize());
		
		// ThermoDynamicalSelection
		MyRandom.reset();
		gaparam = new GAParameters();
		gaparam.getParametersMap().put(GAParameters.SELECTION,"operator.ThermoDynamicalSelection");		
		gaparam.getParametersMap().put(GAParameters.S_PARAM,"1.23:true");				
		builder = new DefaultGABuilder();
		builder.setGAParameters(gaparam); 
		ga = builder.buildGA();
		assertEquals("ThermoDynamicalSelection{TEMPERATURE:1.23,isPackIndivList:true}", ga.getOperatorList().get(2).toString());		
	}

	/*
	 * Test method for 'gabuilder.DefaultGABuilder.localTest()'
	 */
	public void testLocalTest() {
		try{
			gabuilder_.localTest();
		}catch(Exception e){
			e.printStackTrace();
			fail();
		}
	}
}
