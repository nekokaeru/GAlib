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

import junit.framework.TestCase;
import operator.BitMutation;
import operator.IOperator;
import operator.Merge;
import operator.OnePointCrossover;
import operator.RouletteSelection;
import operator.UniformCrossover;
import population.Population;
/**
 * @author mori
 * @version 1.0
 */

public class MergeTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MergeTest.class);
	}

	public MergeTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'operator.Merge.apply(Population)'
	 */
	public void testApply() {

	}

	/*
	 * Test method for 'operator.Merge.getName()'
	 */
	public void testGetName() {
		Merge mg = new Merge();
		assertEquals(mg.getName(),"Merge");
	}

	/*
	 * Test method for 'operator.Merge.getOperators()'
	 */
	public void testGetOperators() {
		IOperator op1 = new BitMutation();
		IOperator op2 = new OnePointCrossover();
		ArrayList<IOperator> opList = new ArrayList<IOperator>();
		opList.add(op1);
		opList.add(op2);
		Merge mg = new Merge();
		mg.setOperators(opList);//浅いコピーでセット
		assertEquals(mg.getOperators().get(0),op1); //同じ参照
		assertEquals(mg.getOperators().get(1),op2);
		Merge mg2 = new Merge();
		mg2.getOperators().add(new RouletteSelection());
		mg2.getOperators().add(null);
		mg2.apply(new Population());
		
	}
	
	/**
	 * クラス Merge の setParameter(Object ... params) のテスト．
	 * @see Merge#setParameter
	 **/

	public void testSetParameter() {
		Merge target = new Merge();
		target.setParameter(); //パラメータ無し．
		target.setParameter((Object[])null); //パラメータ null		
		Object p[] = new Object[0]; //パラメータ 空配列
		target.setParameter(p); 				
		//引数の数がおかしいと例外を吐く．数
		try {
			target.setParameter(3);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * @see Merge#getParameterInfo()
	 */
	public void testGetParameterInfo(){
		Merge target1 = new Merge();
		// 一点交叉(交叉率 0.65), null を設定
		IOperator operator1 = new OnePointCrossover();
		operator1.setParameter(0.65);
		target1.getOperators().add(operator1);
		target1.getOperators().add(null);		
		assertEquals("OnePointCrossover{CROSSOVER_RATE:0.65},null", target1.getParameterInfo());
		Merge target2 = new Merge();
		// 一様交叉(交叉率 0.3)および null, 突然変異(突然変異率 0.2)を設定したMerge型の演算子を加える
		IOperator operator2 = new UniformCrossover();
		operator2.setParameter(0.3);
		target2.getOperators().add(operator2);
		target2.getOperators().add(null);
		IOperator operator3 = new BitMutation();
		operator3.setParameter(0.2);
		target2.getOperators().add(operator3);
		target1.getOperators().add(target2);
		assertEquals("OnePointCrossover{CROSSOVER_RATE:0.65},null,Merge{UniformCrossover{CROSSOVER_RATE:0.3},null,BitMutation{MUTATION_RATE:0.2}}", target1.getParameterInfo());
	}
	
	/**
	 * @see Merge#toString()
	 */
	public void testToString(){
		Merge target = new Merge();
		// 一点交叉(交叉率 0.65), null を設定
		IOperator operator = new OnePointCrossover();
		operator.setParameter(0.65);
		target.getOperators().add(operator);
		target.getOperators().add(null);		
		assertEquals("Merge{OnePointCrossover{CROSSOVER_RATE:0.65},null}", target.toString());
	}

}
