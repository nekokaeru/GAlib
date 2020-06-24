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

import problem.function.SigmoidFunction;
import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */
public class SigmoidFunctionTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(SigmoidFunctionTest.class);
	}
	
	public SigmoidFunctionTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testFunction(){
		/* パラメータの初期値は1.0 */
		SigmoidFunction f1 = new SigmoidFunction();
		//デフォルトコンストラクタで初期化した場合は，alpha は 1.0
		assertEquals(1.0, f1.getAlpha());
		assertEquals(0.5, f1.function(0.0),0.0);
		assertEquals(1./(1+Math.exp(1.0)), f1.function(1.0),0.0);
		//極限値のテスト
		assertEquals(0.0, f1.function(Double.POSITIVE_INFINITY),0.0);						
		assertEquals(1.0, f1.function(Double.NEGATIVE_INFINITY),0.0);								
		//コンストラクタで指数を指定した場合
		SigmoidFunction f2 = new SigmoidFunction(-1.0);
		assertEquals(-1.0, f2.getAlpha());
		assertEquals(1./(1+Math.exp(-1.0)), f2.function(1.0),0.0);
		//極限値のテスト		
		assertEquals(1.0, f2.function(Double.POSITIVE_INFINITY),0.0);						
		assertEquals(0.0, f2.function(Double.NEGATIVE_INFINITY),0.0);								
	}
	
	public void testGetName(){
		SigmoidFunction f = new SigmoidFunction();
		assertEquals("Sigmoid", f.getName());
	}
	
	public void testParam(){
		SigmoidFunction f = new SigmoidFunction();
		f.setParameter(3.0);
		assertEquals(3.0, f.getAlpha());
		assertEquals(1./(1+Math.exp(3.0)), f.function(1.0),0.0);				
		f.setParameter("-3.0");
		assertEquals(-3.0, f.getAlpha());
		// パラメータ数が 1 じゃないと例外発生 
		try{
			f.setParameter(3.0, -1.0); //例外発生
			fail(); //例外が発生しないとここでテスト失敗
		}catch(Exception e){ 
			assertTrue(true); //例外が発生すればここにくる．
		}

		/* パラメータが数字でないと例外をはく */
		try{
			f.setParameter("5a");
			fail();
		}catch(Exception e){
			assertTrue(true);
		}
	}
	
	public void testAlpha(){
		SigmoidFunction f = new SigmoidFunction();
		f.setAlpha(3.5);
		assertEquals(3.5, f.getAlpha(),0.0);
		int a=2;
		f.setAlpha(a);		
		assertEquals(2.0, f.getAlpha(),0.0);
		f.setAlpha(new Double(1.1)); //アンボクシング
		assertEquals(1.1, f.getAlpha(),0.0);		
	}
	
	public void testGetParameterInfo(){
		SigmoidFunction f = new SigmoidFunction();
		assertEquals(f.getParameterInfo(),"ALPHA:1.0");
		f.setParameter(new Object[]{"5.1"});
		assertEquals(f.getParameterInfo(),"ALPHA:5.1");
	}

	public void testToString(){
		SigmoidFunction f = new SigmoidFunction();
		assertEquals(f.toString(),"Sigmoid{ALPHA:1.0}");
		f.setParameter(new Object[]{"5.1"});
		assertEquals(f.toString(),"Sigmoid{ALPHA:5.1}");
	}
}
