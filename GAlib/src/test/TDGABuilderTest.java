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

import gabuilder.Director;
import gabuilder.GABase;
import gabuilder.IGABuilder;
import gabuilder.TDGABuilder;
import junit.framework.TestCase;
import util.LocalTestException;

/**
 * @author mori
 * @version 1.0
 */
public class TDGABuilderTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TDGABuilderTest.class);
	}

	/**
	 * Constructor for TDGABuilderTest.
	 * @param name
	 */
	public TDGABuilderTest(String name) {
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
	 * Test method for 'gabuilder.TDGABuilder.buildGA()'
	 */
	public void testBuildGA() {
		IGABuilder builder = new TDGABuilder();
		Director d = new Director(builder);
		String[] s = {"-S","operator.TournamentSelection","-Sparam","0.25"};
		d.setParameter(s);
		d.constract();
		GABase ga = builder.buildGA();
		assertEquals("[Merge, BitMutation, ThermoDynamicalSelection]",ga.getOperatorNameList().toString());
		assertEquals("Merge{Dummy{},OnePointCrossover{CROSSOVER_RATE:0.6}}", ga.getOperatorList().get(0).toString());				
		assertEquals("ThermoDynamicalSelection{TEMPERATURE:0.25,isPackIndivList:false}", ga.getOperatorList().get(2).toString());
	}

	/*
	 * Test method for 'gabuilder.TDGABuilder.localTest()'
	 */
	public void testLocalTest(){
		TDGABuilder builder = new TDGABuilder();
		try {
			builder.localTest();
		} catch (LocalTestException e) {
			e.printStackTrace();
		}
	}

}
