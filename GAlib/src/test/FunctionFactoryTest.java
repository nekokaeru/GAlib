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
import problem.function.FunctionFactory;
import problem.function.IFunction;

/**
 * @author mori
 * @version 1.0
 */
public class FunctionFactoryTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FunctionFactoryTest.class);
	}
	
	/**
	 * @see problem.function.FunctionFactory#createFunction(String)
	 */
	public static void testCreateFunction(){
		// LinearFunctionクラスのインスタンスを生成
		IFunction f1=FunctionFactory.createFunction("problem.function.LinearFunction");
		assertEquals("Linear",f1.getName());
		assertEquals(7.0,f1.function(2.0),0); //傾き3，切片1
		// NegativeFunctionクラスのインスタンスを生成
		IFunction f2=FunctionFactory.createFunction("problem.function.NegativeFunction");
		assertEquals("Negative",f2.getName());
		assertEquals(-2.0,f2.function(2.0),0); //*-1
		// 引数がおかしいと例外を発生
		try{
			FunctionFactory.createFunction("Power");
			fail("Exception must be caught!");
		}catch(IllegalArgumentException e){
			assertTrue(true);			
		}
	}
}
