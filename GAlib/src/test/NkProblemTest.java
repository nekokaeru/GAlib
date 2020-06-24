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

import java.util.HashSet;

import junit.framework.TestCase;
import problem.NkProblem;
import problem.function.LinearFunction;
/**
 * @author mori
 * @version 1.0
 */

public class NkProblemTest extends TestCase {
	NkProblem Nk_;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(NkProblemTest.class);
	}

	public NkProblemTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Nk_ = new NkProblem();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'problem.NkProblem.NkProblem()'
	 */
	public void testNkProblem() {
		// �����l
		assertEquals(0,Nk_.getInitSeed());
		assertEquals(10,Nk_.getN());
		assertEquals(0,Nk_.getK());
		Number[] array = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		double f1 = Nk_.getObjectiveFunctionValue(array);
		Nk_.setInitSeed(0);
		double f2 = Nk_.getObjectiveFunctionValue(array);
		assertEquals(f1, f2, 0);
	}

	/*
	 * Test method for 'problem.NkProblem.NkProblem(int, int)'
	 */
	public void testNkProblemIntInt() {
		Nk_ = new NkProblem(12, 3); // (N,k)
		assertEquals(12,Nk_.getN());
		assertEquals(3,Nk_.getK());
		Number[] array = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// 10�����Ȃ�
		try {
			// array.length �� N �������Ă��Ȃ��D
			Nk_.getObjectiveFunctionValue(array);
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			// k >= N
			Nk_ = new NkProblem(5, 5); // (N,k)
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/*
	 * Test method for 'problem.NkProblem.NkProblem(int, int, int)'
	 */
	public void testNkProblemIntIntInt() {
		Nk_ = new NkProblem(8, 1, 1234); // (N,k)
		assertEquals(8,Nk_.getN());
		assertEquals(1,Nk_.getK());
		assertEquals(1234,Nk_.getInitSeed());
		Number[] array = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };// 10����D
		try {
			// array.length �� N �������Ă��Ȃ��D
			Nk_.getObjectiveFunctionValue(array);
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			// k >= N
			Nk_ = new NkProblem(5, 5); // (N,k)
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		Number[] array2 = { 0, 0, 0, 0, 0, 0, 0, 0 };// 8����D
		try {
			// ���̏ꍇ�͂��܂������D
			Nk_.getObjectiveFunctionValue(array2);
			assertTrue(true);
		} catch (Exception e) {
			fail("IllegalArgumentException must be caught!");
		}
	}

	/*
	 * Test method for 'problem.NkProblem.getFitness(Number[])'
	 */
	public void testGetRawFitness() {
		HashSet<String> set = new HashSet<String>();
		int size = 10;
		Nk_.setN(size);
		double max = Math.pow(2, size);
		for (int j = 0; j < size - 1; j++) {
			Nk_.setK(j);
			set.clear();
			for (int i = 0; i < max; i++) {
				Number[] array = makeArray(i, size);
				double f1 = Nk_.getObjectiveFunctionValue(array);
				double f2 = Nk_.getFitness(array); // ���͊֐��ϊ����Ă��Ȃ��D
				assertEquals(f1, f2, 0);
				long num = (long) (f1 * 10000000000L); // 10���܂Œ��ׂē������̂��Ȃ����Ƃ��m�F�D
				set.add(String.valueOf(num));
			}
			// 2^10 �̓K���x���قȂ邱�Ƃ��`�F�b�N�D
			assertEquals((int) max,set.size());
		}
		double[] a1 = new double[(int)max];
		Nk_.setInitSeed((int)(Math.random()*Integer.MAX_VALUE));
		Nk_.setK(3);
		double sum = 0;
		Number[] arrayTest={1,1,0,1,1,0,1,0,1,1};
		double fTest = Nk_.getObjectiveFunctionValue(arrayTest);
		for (int i = 0; i < max; i++) {
			Number[] array = makeArray(i, size);			
			a1[i]=Nk_.getObjectiveFunctionValue(array);
			sum += a1[i];
			//�ǂ̃^�C�~���O�Œ��ׂĂ��C����͓̂����K���x�D
			assertEquals(fTest,Nk_.getObjectiveFunctionValue(arrayTest));
		}
		//TODO sum �Ɋւ���e�X�g
		//System.err.println(sum);
	}

	/**
	 * ������2�i���\�L�̔z��ɂ���D
	 * 
	 * @param num
	 *            ����
	 * @param size
	 *            �r�b�g��
	 * @return num ��2�i�\��
	 */
	public Number[] makeArray(long num, int size) {
		Number[] array = new Number[size];
		for (int i = 0; i < size; i++) {
			long x = (long) Math.pow(2, i);
			if ((x & num) == 0) {
				array[size - i - 1] = 0;
			} else {
				array[size - i - 1] = 1;
			}
		}
		return array;
	}

	/*
	 * Test method for 'problem.NkProblem.getInitSeed()'
	 */
	public void testGetInitSeed() {
		Nk_.setInitSeed(111);
		assertEquals(111,Nk_.getInitSeed());
		Number[] array = { 0, 1, 1, 1, 0, 0, 1, 0, 1, 1 };
		Number[] array2 = { 0, 1, 0, 0, 0, 0, 1, 0, 0, 1 };
		double f1 = Nk_.getObjectiveFunctionValue(array);
		double f2 = Nk_.getObjectiveFunctionValue(array2);
		Nk_.setInitSeed(123); // ��x�Ⴄ�V�[�h�ŏ�����
		double f3 = Nk_.getObjectiveFunctionValue(array);
		double f4 = Nk_.getObjectiveFunctionValue(array2);
		Nk_.setInitSeed(111); // ������x��̒l�ɖ߂��D
		f3 = Nk_.getObjectiveFunctionValue(array);
		f4 = Nk_.getObjectiveFunctionValue(array2);
		assertEquals(f1, f3, 0);
		assertEquals(f2, f4, 0);

	}

	/*
	 * Test method for 'problem.NkProblem.getK()'
	 */
	public void testGetK() {
		Nk_.setK(11);
		assertEquals(11,Nk_.getK());
	}

	/*
	 * Test method for 'problem.NkProblem.getN()'
	 */
	public void testGetN() {
		Nk_.setN(13);
		assertEquals(13,Nk_.getN());
	}
		
	public void testGetName(){
		assertEquals("Nk",Nk_.getName());	
	}
	
	public void testGetParameterInfo(){
		NkProblem p = new NkProblem();
		assertEquals(p.getParameterInfo(),"N:10,k:0,seed:0");
		p.addFunction(new LinearFunction());
		assertEquals(p.getParameterInfo(),"N:10,k:0,seed:0,Linear{GRADIENT:3.0,INTERCEPT:1.0}");
		p.clearFunctionList();
		p.addFunction(new LinearFunction(5.1,63.0));
		assertEquals(p.getParameterInfo(),"N:10,k:0,seed:0,Linear{GRADIENT:5.1,INTERCEPT:63.0}");
		NkProblem p2 = new NkProblem(20,10);
		assertEquals(p2.getParameterInfo(),"N:20,k:10,seed:0");
		NkProblem p3 = new NkProblem(20,10,-1);
		assertEquals(p3.getParameterInfo(),"N:20,k:10,seed:-1");		
	}

	public void testToString(){
		NkProblem p = new NkProblem();
		assertEquals(p.toString(),"Nk{N:10,k:0,seed:0}");
		p.addFunction(new LinearFunction());
		assertEquals(p.toString(),"Nk{N:10,k:0,seed:0,Linear{GRADIENT:3.0,INTERCEPT:1.0}}");
	}
	public void testSetParameter() {
		NkProblem target = new NkProblem();		
		//TODO �e�X�g�L�q�D�ȉ��͎����l����p�����[�^�Ƃ���ꍇ�̗�D
		//���l�������̏ꍇ�� 1. ���l�C 2. ������C 3. �z���n�����ꍇ�̃e�X�g������
		target.setParameter(10,3,-1);
		assertEquals(10,target.getN());
		assertEquals(3,target.getK());
		assertEquals(-1,target.getInitSeed());
		target.setParameter("30","2","3");
		assertEquals(30,target.getN());
		assertEquals(2,target.getK());		
		assertEquals(3,target.getInitSeed());
		assertEquals("Nk{N:30,k:2,seed:3}",target.toString());		
		target.setParameter(8,5,"4");
		assertEquals(8,target.getN());
		assertEquals(5,target.getK());		
		assertEquals(4,target.getInitSeed());		
		assertEquals("Nk{N:8,k:5,seed:4}",target.toString());				
		Object[] tmpParam = { new Integer(20), "3", "-10"};
		target.setParameter(tmpParam);
		assertEquals(20,target.getN());
		assertEquals(3,target.getK());		
		assertEquals(-10,target.getInitSeed());		
		assertEquals("Nk{N:20,k:3,seed:-10}",target.toString());				
		//TODO ��O�̔����e�X�g�D
		//�����̐������������Ɨ�O�����D�p�����[�^��
		try {
			target.setParameter("2");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		//�����̓��e�����������Ɨ�O�����D������̓��e
		try {
			target.setParameter(1,2,"seed");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		//k>=N���Ɨ�O�����D
		try {
			target.setParameter(5,5,0);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		//k<0���Ɨ�O�����D
		try {
			target.setParameter(5,-1);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}		
		//N<0���Ɨ�O�����D
		try {
			target.setParameter(-1,-2);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}				
	}	
}
