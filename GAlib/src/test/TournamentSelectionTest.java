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

import gabuilder.GAParameters;

import java.util.Arrays;
import junit.framework.TestCase;
import operator.AbstractSelection;
import operator.TournamentSelection;
import population.Individual;
import population.Population;
import util.MyRandom;
import util.NStatistics;
/**
 * @author mori
 * @version 1.0
 */

public class TournamentSelectionTest extends TestCase {
	/**
	 */
	TournamentSelection ts_;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TournamentSelectionTest.class);
	}

	public TournamentSelectionTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
		ts_ = new TournamentSelection();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
	}

	/*
	 * Test method for 'operator.TournamentSelection.select(double[])'
	 */
	public void testSelect() {
		// i 番目の順位は data.length-i;
		double[] data = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		int[] result = new int[data.length];
		double sum = 0;
		int maxi = 3000;
		for (int i = 0; i < data.length; i++) {
			sum += (data[i] + 1); // 順位は 1～11まで
			result[i] = 0;
		}
		// トーナメントサイズが1の場合. すべてが平等に選ばれる．
		ts_.setTournamentSize(1);
		int[] indexArray;
		for (int i = 0; i < maxi; i++) {
			indexArray = ts_.select(data);
			for (int j = 0; j < indexArray.length; j++) {
				result[indexArray[j]]++;
			}
		}

		for (int i = 0; i < result.length; i++) {
			double p = 1.0 / data.length; // i 番目が選ばれる確率．
			// 一度のselect で data.length 個体が選ばれるので，全部でmaxi*data.length 選ばれる．
			NStatistics.binomialTest(maxi * data.length, result[i], p,
					TestParameters.SIGMA_MARGIN);
		}
		// トーナメントサイズが 2 の場合.
		ts_.setTournamentSize(2);
		Arrays.fill(result, 0);
		for (int i = 0; i < maxi; i++) {
			indexArray = ts_.select(data);
			for (int j = 0; j < indexArray.length; j++) {
				result[indexArray[j]]++;
			}
		}
		assertEquals(0, result[0]);
		for (int i = 0; i < result.length; i++) {
			/**
			 * ランクr が一回のトーナメントに参加できる確率 2/N ランクr がトーナメントに参加した場合に選ばれる確率
			 * (N-r)/(N-1) <- 相手が自分より下ならよい． ランクr が一回のトーナメントで選ばれる確率
			 * {(N-r)/(N-1)}*{2/N}
			 */
			double p = data[i] / (data.length - 1) * (2.0 / data.length); // data[i]=N-r
			NStatistics.binomialTest(maxi * data.length, result[i], p,
					TestParameters.SIGMA_MARGIN);
		}
		// トーナメントサイズが Nt のときはランクが N-Nt+2 以下の個体は選ばれない．
		maxi = 100;
		// tsize 1,2 は試した.
		for (int tsize = 3; tsize <= data.length; tsize++) {
			Arrays.fill(result, 0);
			ts_.setTournamentSize(tsize);
			for (int i = 0; i < maxi; i++) {
				indexArray = ts_.select(data);
				for (int j = 0; j < indexArray.length; j++) {
					result[indexArray[j]]++;
				}
			}
			// 下位個体が選ばれていないことのテスト
			// data は小さい順なので，前から tsize-1 個(index 0~tsize-2 まで)が選ばれない．
			for (int i = 0; i < tsize - 1; i++) {
				assertEquals(0, result[i]);
			}
		}
	}

	/*
	 * Test method for 'operator.TournamentSelection.init(List)'
	 */
	public void testInit() {
		// デフォルト値は３
		assertEquals(GAParameters.DEFAULT_TOURNAMENT_SIZE, ts_.getTournamentSize());
		ts_.setParameter("12");
		assertEquals(12, ts_.getTournamentSize());
		ts_.setParameter(1);
		assertEquals(1, ts_.getTournamentSize());
		try {
			ts_.setParameter("a");
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
		try {
			ts_.setParameter(-1);
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}
		try {
			ts_.setParameter(0);
			fail("IllegalArgumentException must be caught!");
		} catch (IllegalArgumentException ex) {
			assertTrue(true);
		}

	}

	/*
	 * Test method for 'operator.TournamentSelection.getName()'
	 */
	public void testGetName() {
		assertEquals("TournamentSelection", ts_.getName());
	}

	/*
	 * Test method for 'operator.TournamentSelection.getTournamentSize()' Test
	 * method for 'operator.TournamentSelection.setTournamentSize(int)'
	 */
	public void testGetAndSetTournamentSize() {
		int size = 123;
		ts_.setTournamentSize(size);
		assertEquals(size, ts_.getTournamentSize());
	}

	// 適応度の更新フラグが適切に更新されているかのテスト
	public void testIsChanged() {
		Population pop = new Population(100, 20);// 100 個体
		for (Individual indiv : pop.getIndivList()) {
			assertTrue(indiv.isChanged());
		}
		ts_.apply(pop);
		// 選択時にはすべての適応度が計算される．選択では新しい個体はできない．
		for (Individual indiv : pop.getIndivList()) {
			assertFalse(indiv.isChanged());
		}
	}

	/*
	 * Test method for 'operator.TournamentSelection.localTest()'
	 */
	public void testLocalTest() {
		try {
			ts_.localTest();
		} catch (Exception e) {
			e.printStackTrace();
			fail("TournamentSelection.localTest() was failed!");
		}
	}

	/**
	 * クラス TournamentSelection の setParameter(Object ... params) のテスト．
	 * 
	 * @see TournamentSelection#setParameter
	 */

	public void testSetParameter() {
		TournamentSelection target = new TournamentSelection();

		// テスト記述．以下は実数値一つをパラメータとする場合の例．
		// 数値が引数の場合は 1. 数値， 2. 文字列， 3. 配列を渡した場合のテストを書く
		target.setParameter(2);
		assertEquals(2, target.getTournamentSize());
		target.setParameter("2");
		assertEquals(2, target.getTournamentSize());
		Object[] tmpParam = { new Integer(2) };
		target.setParameter(tmpParam);
		assertEquals(2, target.getTournamentSize());

		// 例外の発生テスト．
		// 引数の数がおかしいと例外発生．パラメータ数
		try {
			target.setParameter("1", "2");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		// 引数の内容がおかしいと例外発生．文字列の内容
		try {
			target.setParameter("num");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * @see TournamentSelection#getParameterInfo()
	 */
	public void testGetParameterInfo(){
		// トーナメントサイズを 7 に設定
		ts_.setTournamentSize(7);
		assertEquals("TOURNAMENT_SIZE:7", ts_.getParameterInfo());
	}
	
	/**
	 * @see AbstractSelection#toString()
	 */
	public void testToString(){
		// トーナメントサイズを 7 に設定
		ts_.setTournamentSize(7);
		assertEquals("TournamentSelection{TOURNAMENT_SIZE:7}", ts_.toString());
	}
}
