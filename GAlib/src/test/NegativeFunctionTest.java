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
import problem.function.NegativeFunction;
/**
 * @author mori
 * @version 1.0
 */

public class NegativeFunctionTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(NegativeFunctionTest.class);
	}

	public NegativeFunctionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * @see problem.function.NegativeFunction#function(double)
	 */
	public void testFunction() {
		NegativeFunction nf = new NegativeFunction();
		assertEquals(nf.function(1),-1.0,0);
		assertEquals(nf.function(-2.2),2.2,0);
	}

	/**
	 * @see problem.function.NegativeFunction#getName()
	 */
	public void testGetName() {
		NegativeFunction nf = new NegativeFunction();
		assertEquals(nf.getName(),"Negative");
	}
	
	/**
	 * @see problem.function.NegativeFunction#setParameter(Object[])
	 */
	public void testSetParam(){
		NegativeFunction lf = new NegativeFunction();
		lf.setParameter(); //パラメータ無し．
		assertEquals(lf.function(2),-2.0,0);
		lf.setParameter((Object[])null); //パラメータ null
		assertEquals(lf.function(2),-2.0,0);		
		Object p[] = new Object[0]; //パラメータ 空配列
		lf.setParameter(p); 
		assertEquals(lf.function(2),-2.0,0);				
		//引数の数がおかしいと例外を吐く．数
		try {
			lf.setParameter(3);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	public void testGetParameterInfo(){
		NegativeFunction f = new NegativeFunction();
		assertEquals(f.getParameterInfo(),"");
	}

	public void testToString(){
		NegativeFunction f = new NegativeFunction();
		assertEquals(f.toString(),"Negative{}");
	}


}
