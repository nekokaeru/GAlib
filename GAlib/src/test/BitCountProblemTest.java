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

import java.util.Arrays;

import junit.framework.TestCase;
import problem.BitCountProblem;
import problem.function.LinearFunction;
import problem.function.NegativeFunction;
import problem.function.PowerFunction;
/**
 * @author mori
 * @version 1.0
 */

public class BitCountProblemTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BitCountProblemTest.class);
	}

	public BitCountProblemTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * problem.BitCountProblem.getFitness(Number[])�̃e�X�g
	 * @see problem.BitCountProblem#getObjectiveFunctionValue(Number[])
	 */
	public void testGetFitness() {
		BitCountProblem bp = new BitCountProblem();
		Number[] array = {0,0,0,0,0};
		//��`�q�����ׂ� 0 �̎��C�K���x�� 0
		assertEquals(0.0,bp.getObjectiveFunctionValue(array),0.0);
		assertEquals(0.0,bp.getFitness(array),0.0);
		//���F�̂̓������`�q�� 1 �ɕς��Ă����ăe�X�g
		for (int i = 0; i < array.length; i++) {
			array[i]=1;
			assertEquals(i+1.0,bp.getObjectiveFunctionValue(array),0.0);
			assertEquals(i+1.0,bp.getFitness(array),0.0);
		}
		//�X�P�[�����O�i2��j����Ă��邩�e�X�g
		bp.addFunction(new PowerFunction(2));
		assertEquals(5.0,bp.getObjectiveFunctionValue(array),0.0);
		assertEquals(25.0,bp.getFitness(array),0.0);
		//�X�P�[�����O�i2��j����ĕϊ��i�����ϊ��j����Ă��邩�e�X�g
		bp.addFunction(new NegativeFunction());
		assertEquals(5.0,bp.getObjectiveFunctionValue(array),0.0);
		assertEquals(-25.0,bp.getFitness(array),0.0);
		bp.clearFunctionList();
		//���݂̃r�b�g�J�E���g�� 1 �݂̂��J�E���g�D�o�C�i���ȊO�̕\���ł͂������Ȃ��ƂɂȂ�D
		Arrays.fill(array,2);
		array[0]=1;
		assertEquals(1.0,bp.getFitness(array),0.0);		
	}
	/**
	 * problem.BitCountProblem#getName()�̃e�X�g
	 * @see problem.BitCountProblem#getName()
	 */
	public void testGetName(){
		BitCountProblem bp = new BitCountProblem();
		assertEquals("BitCount",bp.getName());
	}
	
	public void testGetParameterInfo(){
		BitCountProblem p = new BitCountProblem();
		assertEquals(p.getParameterInfo(),"");
		p.addFunction(new LinearFunction());
		assertEquals(p.getParameterInfo(),"Linear{GRADIENT:3.0,INTERCEPT:1.0}");
		p.clearFunctionList();
		p.addFunction(new LinearFunction(5.1,63.0));
		assertEquals(p.getParameterInfo(),"Linear{GRADIENT:5.1,INTERCEPT:63.0}");
	}

	public void testToString(){
		BitCountProblem p = new BitCountProblem();
		assertEquals(p.toString(),"BitCount{}");
		p.addFunction(new LinearFunction());
		assertEquals(p.toString(),"BitCount{Linear{GRADIENT:3.0,INTERCEPT:1.0}}");
	}
}
