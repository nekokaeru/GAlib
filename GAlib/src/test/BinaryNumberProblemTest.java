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
import problem.BinaryNumberProblem;
import problem.function.LinearFunction;
/**
 * @author mori
 * @version 1.0
 */

public class BinaryNumberProblemTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BinaryNumberProblemTest.class);
	}

	public BinaryNumberProblemTest(String name) {
		super(name);
	}

	/**
	 * problem.BitCountProblem.getFitness(Number[])のテストをする
	 * @see problem.BitCountProblem#getObjectiveFunctionValue(Number[])
	 */
	public void testGetFitness() {
		BinaryNumberProblem bnp = new BinaryNumberProblem();
		Number[] array = {0,0,0,0,0};
		//2進数で0
		assertEquals(0.0,bnp.getObjectiveFunctionValue(array),0.0);
		assertEquals(0.0,bnp.getFitness(array),0.0);
		array[4]=1; //2進数で1
		assertEquals(1.0,bnp.getObjectiveFunctionValue(array),0.0);
		assertEquals(1.0,bnp.getFitness(array),0.0);
		array[0]=1; //2進数で16+1
		assertEquals(17.0,bnp.getObjectiveFunctionValue(array),0.0);
		assertEquals(17.0,bnp.getFitness(array),0.0);
		// 0,1 以外は例外．
		array[0]=2;
		try{
			bnp.getFitness(array);
			fail();
		}catch (Exception e) {
			assertTrue(true);
		}
	}
	
	/**
	 * problem.BitCountProblem.getName()のテスト
	 * @see problem.BitCountProblem#getName()
	 */
	public void testGetName(){
		BinaryNumberProblem bnp = new BinaryNumberProblem();
		assertEquals("BinaryNumber",bnp.getName());
	}
	
	public void testGetParameterInfo(){
		BinaryNumberProblem p = new BinaryNumberProblem();
		assertEquals(p.getParameterInfo(),"");
		p.addFunction(new LinearFunction());
		assertEquals(p.getParameterInfo(),"Linear{GRADIENT:3.0,INTERCEPT:1.0}");
		p.clearFunctionList();
		p.addFunction(new LinearFunction(5.1,63.0));
		assertEquals(p.getParameterInfo(),"Linear{GRADIENT:5.1,INTERCEPT:63.0}");
	}

	public void testToString(){
		BinaryNumberProblem p = new BinaryNumberProblem();
		assertEquals(p.toString(),"BinaryNumber{}");
		p.addFunction(new LinearFunction());
		assertEquals(p.toString(),"BinaryNumber{Linear{GRADIENT:3.0,INTERCEPT:1.0}}");
	}
}
