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
import problem.KnapsackProblem;
import problem.function.LinearFunction;
/**
 * @author mori
 * @version 1.0
 */

public class KnapsackProblemTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(KnapsackProblemTest.class);
	}

	public KnapsackProblemTest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * problem.KnapsackProblem�̃R���X�g���N�^�̃e�X�g
	 * @see problem.KnapsackProblem
	 */
	public void testKnapsackProblem() {
		KnapsackProblem k = new KnapsackProblem();
		// �R���X�g���N�^�̃f�t�H���g�͉ו�30
		assertEquals(30, k.getValue().length);
		assertEquals(30, k.getWeight().length);
		assertEquals(1136.0, k.getOptimum(), 0.0);
		assertEquals(548.0, k.getLimitWeight(), 0.0);
		// �R���X�g���N�^�� 30 ������
		KnapsackProblem k30 = new KnapsackProblem(30);
		assertEquals(30, k30.getValue().length);
		assertEquals(30, k30.getWeight().length);
		assertEquals(1136.0, k30.getOptimum(), 0.0);
		assertEquals(548.0, k30.getLimitWeight(), 0.0);
		// �R���X�g���N�^�� "30" ������
		KnapsackProblem ks30 = new KnapsackProblem("30");
		assertEquals(30, ks30.getValue().length);
		assertEquals(30, ks30.getWeight().length);
		assertEquals(1136.0, ks30.getOptimum(), 0.0);
		assertEquals(548.0, ks30.getLimitWeight(), 0.0);
		// �R���X�g���N�^�� 100 ������
		KnapsackProblem k100 = new KnapsackProblem(100);
		assertEquals(100, k100.getValue().length);
		assertEquals(100, k100.getWeight().length);
		assertEquals(11878.0, k100.getOptimum(), 0.0);
		assertEquals(7087.0, k100.getLimitWeight(), 0.0);
		// �R���X�g���N�^�� "100" ������
		KnapsackProblem ks100 = new KnapsackProblem("100");
		assertEquals(100, ks100.getValue().length);
		assertEquals(100, ks100.getWeight().length);
		assertEquals(11878.0, ks100.getOptimum(), 0.0);
		assertEquals(7087.0, ks100.getLimitWeight(), 0.0);

		// ����ȍ~�� 30�C100 �ȊO�̕������ė�O�𔭐�������
		try {
			new KnapsackProblem("20");
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			new KnapsackProblem(20);
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
		try {
			new KnapsackProblem("Power!!");
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	/**
	 * problem.KnapsackProblem.getName()�̃e�X�g
	 * @see problem.KnapsackProblem#getName()
	 */
	public void testGetName() {
		KnapsackProblem k = new KnapsackProblem();
		assertEquals("Knapsack", k.getName());
	}

	/**
	 * problem.KnapsackProblem.getFitness(Number[])�̃e�X�g
	 * @see problem.KnapsackProblem#getObjectiveFunctionValue(Number[])
	 */
	public void testGetRawFitness() {
		KnapsackProblem k = new KnapsackProblem();
		Number[] chromosome = new Number[30];

		// b��`�q�� 30 �̔z���p�ӂ��S���� 0 �����ăe�X�g
		Arrays.fill(chromosome, 0);
		assertEquals(0.0, k.getFitness(chromosome), 0.0);

		// ���F�̂̈�`�q�� ������ 0 �� 1 �ɕς��Ă����āC
		// �����d�ʂ𒴂����� �K���x�� 0 �ɂȂ邱�Ƃ��e�X�g
		// �܂������d�ʂ𒴂��Ȃ�������C���l�̑��a���K���x�ɂȂ邱�Ƃ��e�X�g
		double weightSum = 0.0;
		double valueSum = 0.0;
		for (int i = 0; i < chromosome.length; i++) {
			chromosome[i] = 1;
			weightSum += k.getWeight()[i];
			valueSum += k.getValue()[i];
			if (k.getLimitWeight() < weightSum) {
				assertEquals(0.0, k.getFitness(chromosome), 0.0);
			} else {
				assertEquals(valueSum, k.getFitness(chromosome), 0.0);
			}
		}

	}

	public void testGetParameterInfo() {
		KnapsackProblem p = new KnapsackProblem();
		assertEquals(p.getParameterInfo(), "itemNum:30");
		p.addFunction(new LinearFunction());
		assertEquals(p.getParameterInfo(),
				"itemNum:30,Linear{GRADIENT:3.0,INTERCEPT:1.0}");
		p.clearFunctionList();
		p.addFunction(new LinearFunction(5.1, 63.0));
		assertEquals(p.getParameterInfo(),
				"itemNum:30,Linear{GRADIENT:5.1,INTERCEPT:63.0}");
		KnapsackProblem p2 = new KnapsackProblem("100");
		assertEquals(p2.getParameterInfo(), "itemNum:100");
	}

	public void testToString() {
		KnapsackProblem p = new KnapsackProblem();
		assertEquals(p.toString(), "Knapsack{itemNum:30}");
		p.addFunction(new LinearFunction());
		assertEquals(p.toString(),
				"Knapsack{itemNum:30,Linear{GRADIENT:3.0,INTERCEPT:1.0}}");
	}

	public void testSetParameter() {
		KnapsackProblem target = new KnapsackProblem();

		// TODO �e�X�g�L�q�D�ȉ��͎����l����p�����[�^�Ƃ���ꍇ�̗�D
		// ���l�������̏ꍇ�� 1. ���l�C 2. ������C 3. �z���n�����ꍇ�̃e�X�g������
		target.setParameter(30);
		assertEquals(30, target.getValue().length);
		assertEquals(30, target.getWeight().length);
		target.setParameter(100);
		assertEquals(100, target.getValue().length);
		assertEquals(100, target.getWeight().length);
		target.setParameter("30");
		assertEquals(30, target.getValue().length);
		assertEquals(30, target.getWeight().length);
		target.setParameter("100");
		assertEquals(100, target.getValue().length);
		assertEquals(100, target.getWeight().length);
		Object[] tmpParam = { new Integer(30) };
		target.setParameter(tmpParam);
		assertEquals(30, target.getValue().length);
		assertEquals(30, target.getWeight().length);

		// TODO ��O�̔����e�X�g�D
		// �����̐������������Ɨ�O�����D�p�����[�^��
		try {
			target.setParameter("1", "2");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// �����̓��e�����������Ɨ�O�����D������̓��e
		try {
			target.setParameter("num");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// ������30��100�ȊO���Ɨ�O�����D
		try {
			target.setParameter(20);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
}
