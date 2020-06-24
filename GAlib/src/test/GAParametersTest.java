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

import java.util.HashMap;
import java.util.HashSet;

import gabuilder.GAParameters;
import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */
public class GAParametersTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(GAParametersTest.class);
	}

	/**
	 * Constructor for GAParametersTest.
	 * @param name
	 */
	public GAParametersTest(String name) {
		super(name);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for 'gabuilder.GAParameters.getParametersMap()'
	 */
	public void testGetParametersMap() {
		GAParameters g = new GAParameters();
		HashMap<String,String> map = g.getParametersMap();
		assertTrue(map.containsKey(GAParameters.SELECTION));
		assertTrue(map.containsKey(GAParameters.CROSSOVER));
		assertTrue(map.containsKey(GAParameters.MUTATION));
		assertTrue(map.containsKey(GAParameters.POPULATION_SIZE));
		assertTrue(map.containsKey(GAParameters.FUNCTION));
		assertTrue(map.containsKey(GAParameters.FUNCTION_PARAMETER));
		assertTrue(map.containsKey(GAParameters.TRANSFORM));
		assertTrue(map.containsKey(GAParameters.TRANSFORM_PARAMETER));
		assertTrue(map.containsKey(GAParameters.PROBLEM));
		assertTrue(map.containsKey(GAParameters.PROBLEM_PARAMETER));
		assertTrue(map.containsKey(GAParameters.GENERATION_SIZE));
		assertTrue(map.containsKey(GAParameters.CHROMOSOME_LENGTH));
		assertTrue(map.containsKey(GAParameters.PRINT_LEVEL));
		assertTrue(map.containsKey(GAParameters.OUTPUT_FILE));
		assertTrue(map.containsKey(GAParameters.IS_ELITISM));
		assertTrue(map.containsKey(GAParameters.VIEWER));
		assertTrue(map.containsKey(GAParameters.IS_TURBO));
		assertTrue(map.containsKey(GAParameters.IS_MEMORY));
		assertTrue(map.containsKey(GAParameters.SEED));
		
		assertFalse(map.containsKey("test"));
		
		
		assertEquals(map.get(GAParameters.SELECTION),GAParameters.DEFAULT_SELECTION);
		assertEquals(map.get(GAParameters.CROSSOVER), GAParameters.DEFAULT_CROSSOVER);
		assertEquals(map.get(GAParameters.MUTATION), GAParameters.DEFAULT_MUTATION);
		assertEquals(map.get(GAParameters.POPULATION_SIZE), Integer
				.toString(GAParameters.DEFAULT_POPULATION_SIZE));
		assertEquals(map.get(GAParameters.FUNCTION), null);// �ړI�֐��Ŏg���ϊ��֐� IProblem �Őݒ�-> problem.function
		assertEquals(map.get(GAParameters.FUNCTION_PARAMETER), null);// �ړI�֐��Ŏg���ϊ��̃p�����[�^
		assertEquals(map.get(GAParameters.TRANSFORM), null);// �I���Ŏg���X�P�[�����O -> operator.transform
		assertEquals(map.get(GAParameters.TRANSFORM_PARAMETER), null);// �I���Ŏg���X�P�[�����O�̃p�����[�^
		assertEquals(map.get(GAParameters.PROBLEM), GAParameters.DEFAULT_PROBLEM);// ���
		assertEquals(map.get(GAParameters.PROBLEM_PARAMETER), null);// ���p�����[�^
		assertEquals(map.get(GAParameters.GENERATION_SIZE), Integer
				.toString(GAParameters.DEFAULT_GENERATION_SIZE));// ���㐔
		assertEquals(map.get(GAParameters.CHROMOSOME_LENGTH), Integer
				.toString(GAParameters.DEFAULT_CHROMOSOME_LENGTH));// ��`�q��
		assertEquals(map.get(GAParameters.PRINT_LEVEL), String.valueOf(GAParameters.DEFAULT_PRINT_LEVEL));// �v�����g���x��
		assertEquals(map.get(GAParameters.OUTPUT_FILE), "STDOUT");// �o�̓t�@�C��. STDOUT �͕W���o��
		assertEquals(map.get(GAParameters.IS_ELITISM), "true");// �G���[�g
		assertEquals(map.get(GAParameters.VIEWER), GAParameters.DEFAULT_VIEWER);// �r���[��
		assertEquals(map.get(GAParameters.IS_TURBO), String.valueOf(GAParameters.DEFAULT_IS_TURBO));// �������t���O			
		assertEquals(map.get(GAParameters.IS_MEMORY), String.valueOf(GAParameters.DEFAULT_IS_MEMORY));// �̃������t���O						
		assertEquals(map.get(GAParameters.SEED), GAParameters.DEFAULT_SEED); // �V�[�h	
	}

	/*
	 * Test method for 'gabuilder.GAParameters.getRegalOption()'
	 */
	public void testGetRegalOption() {
		GAParameters g = new GAParameters();
		HashSet<String> set = g.getRegalOption();
		assertTrue(set.contains(("-h")));
		assertTrue(set.contains(("-help")));
		assertTrue(set.contains(("-S"))); // �I���w��
		assertTrue(set.contains(("-C"))); // �����w��
		assertTrue(set.contains(("-M"))); // �ˑR�ψَw��
		assertTrue(set.contains(("-Sparam")));// �I���p�����[�^
		assertTrue(set.contains(("-Cparam")));// �����p�����[�^
		assertTrue(set.contains(("-Mparam")));// �ˑR�ψكp�����[�^
		assertTrue(set.contains(("-scaling1")));// �ړI�֐��Ŏg���ϊ��֐� -> problem.function
		assertTrue(set.contains(("-scaling1param")));// �ړI�֐��Ŏg���ϊ��֐��p�����[�^
		assertTrue(set.contains(("-scaling2")));// �I���Ŏg���X�P�[�����O -> operator.transform
		assertTrue(set.contains(("-scaling2param")));// �I���Ŏg���X�P�[�����O�p�����[�^
		assertTrue(set.contains(("-P")));// ���
		assertTrue(set.contains(("-Pparam")));// ���p�����[�^
		assertTrue(set.contains(("-popsize")));// �̐�
		assertTrue(set.contains(("-gsize")));// ���㐔
		assertTrue(set.contains(("-clength")));// ��`�q��
		assertTrue(set.contains(("-printlevel")));// �v�����g���x��
		assertTrue(set.contains(("-output")));// �o�̓t�@�C��
		assertTrue(set.contains(("-elitism")));// �G���[�g��`
		assertTrue(set.contains(("-viewer")));// �r���[��						
		assertTrue(set.contains(("-turbo")));// �������t���O						
		assertTrue(set.contains(("-memory")));// �̃������[�t���O								
		assertTrue(set.contains(("-seed")));// �V�[�h
		assertFalse(set.contains("-test"));
	}

}
