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
import problem.function.PowerFunction;
/**
 * @author mori
 * @version 1.0
 */

public class PowerFunctionTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(PowerFunctionTest.class);
	}

	public PowerFunctionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * @see problem.function.PowerFunction#function(double)
	 */
	public void testFunction() {
		PowerFunction pf1 = new PowerFunction();//default は指数2
		PowerFunction pf2 = new PowerFunction(3);// 指数3で初期化
		assertEquals(pf1.function(-1.0),1.0,0);
		assertEquals(pf2.function(-1.0),-1.0,0);
		pf1.setExponent(3); // 指数3
		assertEquals(pf1.getExponent(),3.0,0);
		assertEquals(pf1.function(2.0),8.0,0);
	}

	/**
	 * @see problem.function.PowerFunction#getName()
	 */
	public void testGetName() {
		PowerFunction pf = new PowerFunction();
		assertEquals(pf.getName(),"Power");
	}
	
	/**
	 *@see problem.function.PowerFunction#setParameter(Object[])
	 */
	public void testSetParam(){
		PowerFunction lf = new PowerFunction();
		lf.setParameter("3"); //指数3
		assertEquals(lf.function(2),8.0,0);
		lf.setParameter(4); //指数4
		assertEquals(lf.function(2),16.0,0);
		lf.setParameter(-0.5); //指数4
		assertEquals(lf.function(4),0.5,0); //1/√4 = 0.5
		//引数がおかしいと例外を発生．数
		try {
			lf.setParameter("4","1");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		//引数がおかしいと例外を発生．内容
		try {
			lf.setParameter("1a");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);			
		}		
	}	
	
	public void testGetParameterInfo(){
		PowerFunction f = new PowerFunction();
		assertEquals(f.getParameterInfo(),"EXPONENT:2.0");
		f.setParameter(new Object[]{"5.1"});
		assertEquals(f.getParameterInfo(),"EXPONENT:5.1");
	}

	public void testToString(){
		PowerFunction f = new PowerFunction();
		assertEquals(f.toString(),"Power{EXPONENT:2.0}");
		f.setParameter(new Object[]{"5.1"});
		assertEquals(f.toString(),"Power{EXPONENT:5.1}");
	}
}
