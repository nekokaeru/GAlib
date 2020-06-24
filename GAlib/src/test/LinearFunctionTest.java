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

import junit.framework.TestCase;
import problem.function.LinearFunction;
/**
 * @author mori
 * @version 1.0
 */

public class LinearFunctionTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(LinearFunctionTest.class);
	}

	public LinearFunctionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * @see problem.function.LinearFunction#function(double)
	 */
	public void testFunction() {
		LinearFunction lf1 = new LinearFunction(); //default は y=3x+1
		LinearFunction lf2 = new LinearFunction(2,-0.5); // y=2x-0.5
		assertEquals(4.0,lf1.function(1),0);
		assertEquals(1.5,lf2.function(1),0);
		lf1.setGradient(0.5);
		lf1.setIntercept(0.3);
		assertEquals(0.5,lf1.getGradient(),0);
		assertEquals(0.3,lf1.getIntercept(),0);
		assertEquals(-0.2,lf1.function(-1),0);
	}	

	/**
	 * @see problem.function.LinearFunction#getName()
	 */
	public void testGetName() {
		LinearFunction lf = new LinearFunction();
		assertEquals("Linear",lf.getName());
	}
	
	/**
	 * @see problem.function.LinearFunction#setParameter(Object[])
	 */
	public void testSetParam(){
		LinearFunction lf = new LinearFunction();
		lf.setParameter("3","1"); //傾き3， 切片1
		assertEquals(7.0,lf.function(2),0);
		lf.setParameter("4",1); //傾き4， 切片1 配列で渡す．
		assertEquals(9.0,lf.function(2),0);
		//引数がおかしいと例外を吐く．数
		try {
			lf.setParameter("4","1","3");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		//引数がおかしいと例外発生．
		try {
			lf.setParameter("4","1a");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);			
		}		
	}
	
	public void testGetParameterInfo(){
		LinearFunction f = new LinearFunction();
		assertEquals("GRADIENT:3.0,INTERCEPT:1.0",f.getParameterInfo());
		f.setParameter(new Object[]{"5.1","63.0"});
		assertEquals("GRADIENT:5.1,INTERCEPT:63.0",f.getParameterInfo());
	}

	public void testToString(){
		LinearFunction f = new LinearFunction();
		assertEquals("Linear{GRADIENT:3.0,INTERCEPT:1.0}",f.toString());
		f.setParameter(new Object[]{"5.1","63.0"});
		assertEquals("Linear{GRADIENT:5.1,INTERCEPT:63.0}",f.toString());
	}

}
