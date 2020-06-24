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
		assertEquals(map.get(GAParameters.FUNCTION), null);// 目的関数で使う変換関数 IProblem で設定-> problem.function
		assertEquals(map.get(GAParameters.FUNCTION_PARAMETER), null);// 目的関数で使う変換のパラメータ
		assertEquals(map.get(GAParameters.TRANSFORM), null);// 選択で使うスケーリング -> operator.transform
		assertEquals(map.get(GAParameters.TRANSFORM_PARAMETER), null);// 選択で使うスケーリングのパラメータ
		assertEquals(map.get(GAParameters.PROBLEM), GAParameters.DEFAULT_PROBLEM);// 問題
		assertEquals(map.get(GAParameters.PROBLEM_PARAMETER), null);// 問題パラメータ
		assertEquals(map.get(GAParameters.GENERATION_SIZE), Integer
				.toString(GAParameters.DEFAULT_GENERATION_SIZE));// 世代数
		assertEquals(map.get(GAParameters.CHROMOSOME_LENGTH), Integer
				.toString(GAParameters.DEFAULT_CHROMOSOME_LENGTH));// 遺伝子長
		assertEquals(map.get(GAParameters.PRINT_LEVEL), String.valueOf(GAParameters.DEFAULT_PRINT_LEVEL));// プリントレベル
		assertEquals(map.get(GAParameters.OUTPUT_FILE), "STDOUT");// 出力ファイル. STDOUT は標準出力
		assertEquals(map.get(GAParameters.IS_ELITISM), "true");// エリート
		assertEquals(map.get(GAParameters.VIEWER), GAParameters.DEFAULT_VIEWER);// ビューワ
		assertEquals(map.get(GAParameters.IS_TURBO), String.valueOf(GAParameters.DEFAULT_IS_TURBO));// 高速化フラグ			
		assertEquals(map.get(GAParameters.IS_MEMORY), String.valueOf(GAParameters.DEFAULT_IS_MEMORY));// 個体メモリフラグ						
		assertEquals(map.get(GAParameters.SEED), GAParameters.DEFAULT_SEED); // シード	
	}

	/*
	 * Test method for 'gabuilder.GAParameters.getRegalOption()'
	 */
	public void testGetRegalOption() {
		GAParameters g = new GAParameters();
		HashSet<String> set = g.getRegalOption();
		assertTrue(set.contains(("-h")));
		assertTrue(set.contains(("-help")));
		assertTrue(set.contains(("-S"))); // 選択指定
		assertTrue(set.contains(("-C"))); // 交叉指定
		assertTrue(set.contains(("-M"))); // 突然変異指定
		assertTrue(set.contains(("-Sparam")));// 選択パラメータ
		assertTrue(set.contains(("-Cparam")));// 交叉パラメータ
		assertTrue(set.contains(("-Mparam")));// 突然変異パラメータ
		assertTrue(set.contains(("-scaling1")));// 目的関数で使う変換関数 -> problem.function
		assertTrue(set.contains(("-scaling1param")));// 目的関数で使う変換関数パラメータ
		assertTrue(set.contains(("-scaling2")));// 選択で使うスケーリング -> operator.transform
		assertTrue(set.contains(("-scaling2param")));// 選択で使うスケーリングパラメータ
		assertTrue(set.contains(("-P")));// 問題
		assertTrue(set.contains(("-Pparam")));// 問題パラメータ
		assertTrue(set.contains(("-popsize")));// 個体数
		assertTrue(set.contains(("-gsize")));// 世代数
		assertTrue(set.contains(("-clength")));// 遺伝子長
		assertTrue(set.contains(("-printlevel")));// プリントレベル
		assertTrue(set.contains(("-output")));// 出力ファイル
		assertTrue(set.contains(("-elitism")));// エリート主義
		assertTrue(set.contains(("-viewer")));// ビューワ						
		assertTrue(set.contains(("-turbo")));// 高速化フラグ						
		assertTrue(set.contains(("-memory")));// 個体メモリーフラグ								
		assertTrue(set.contains(("-seed")));// シード
		assertFalse(set.contains("-test"));
	}

}
