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
import java.util.Random;

import junit.framework.TestCase;
import operator.BitMutation;
import population.Individual;
import util.MyRandom;
import util.NStatistics;
/**
 * @author mori
 * @version 1.0
 */

public class BitMutationTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(BitMutationTest.class);
	}

	public BitMutationTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset(); // 乱数リセット
		MyRandom.setSeed(TestParameters.SEED);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset(); // 乱数リセット
	}

	/*
	 * Test method for 'operator.BitMutation.mutation(Number[])'
	 */
	public void testMutation() {
		BitMutation bm = new BitMutation();
		Number[] indiv0_c = new Number[10];
		Number[] indiv1_c = new Number[10];
		for (int i = 0; i < 10; i++) {
			indiv0_c[i] = 0;
			indiv1_c[i] = 1;
		}
		Individual indiv0 = new Individual(indiv0_c);
		Individual indiv1 = new Individual(indiv1_c);
		// 突然変異率0の場合は変化なし
		bm.setMutationRate(0);
		bm.mutation(indiv0);
		bm.mutation(indiv1);
		assertTrue(!containsValue(indiv0.getChromosome(), 1));
		assertTrue(!containsValue(indiv1.getChromosome(), 0));
		// 突然変異率1の場合はすべて変化
		bm.setMutationRate(1);
		bm.mutation(indiv0);
		bm.mutation(indiv1);
		assertTrue(!containsValue(indiv0.getChromosome(), 0));
		assertTrue(!containsValue(indiv1.getChromosome(), 1));
		double mrate;
		int[] sum0 = new int[10], sum1 = new int[10];
		long maxi = 10000;
		for (mrate = 0.2; mrate < 1; mrate += 0.2) {
			bm.setMutationRate(mrate);
			Arrays.fill(sum0, 0);
			Arrays.fill(sum1, 0);
			for (int i = 0; i < maxi; i++) {
				Arrays.fill(indiv0_c, 0);
				Arrays.fill(indiv1_c, 1);
				indiv0.setChromosome(indiv0_c);
				indiv1.setChromosome(indiv1_c);
				bm.mutation(indiv0);
				bm.mutation(indiv1);
				for (int j = 0; j < indiv0.size(); j++) {
					sum0[j] += (Integer) indiv0.getGeneAt(j);
					sum1[j] += (Integer) indiv1.getGeneAt(j);
				}
			}
			for (int j = 0; j < indiv0.size(); j++) {
				// 確率的に false になることあり. 3σ内にあることを調べる．
				assertTrue(NStatistics.binomialTest(maxi, sum0[j], mrate,
						TestParameters.SIGMA_MARGIN));
				assertTrue(NStatistics.binomialTest(maxi, maxi - sum1[j],
						mrate, TestParameters.SIGMA_MARGIN));
			}
		}
	}

	/**
	 * 個体引数用のテスト関数 シードを覚えておいて同じ場所で突然変異が適用されたかをチェック．
	 */
	public void testMutation2() {
		int len = 100;
		double mrate = 0.5;
		Random rand = MyRandom.getInstance();
		long seed = MyRandom.getSeed(); // seed 記憶
		Number[] chrom = new Number[len];
		Arrays.fill(chrom, 0);// all 0
		boolean[] mflag = new boolean[len]; // all false
		for (int i = 0; i < len; i++) {
			// 突然変異と同じアルゴリズム．
			if (rand.nextDouble() < mrate) {
				mflag[i] = true; // この遺伝子座で突然変異発生．
			}
		}
		MyRandom.reset();
		MyRandom.setSeed(seed); // 同じシードで初期化
		Individual indiv = new Individual(chrom); // all 0
		BitMutation bm = new BitMutation();
		bm.setMutationRate(mrate);
		bm.mutation(indiv);
		Number[] chrom2 = indiv.getChromosome();
		for (int i = 0; i < len; i++) {
			if (mflag[i] == true) {
				assertEquals(1, chrom2[i]);
			} else {
				assertEquals(0, chrom2[i]);
			}
		}
	}

	protected boolean containsValue(Number[] array, int val) {
		for (int i = 0; i < array.length; i++) {
			if ((Integer) array[i] == val) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Test method for 'operator.BitMutation.getName()'
	 */
	public void testGetName() {
		BitMutation bm = new BitMutation();
		assertEquals("BitMutation", bm.getName());
	}

	/**
	 * クラス BitMutation の setParameter(Object ... params) のテスト．
	 * @see BitMutation#setParameter
	 */

	public void testSetParameter() {
		BitMutation target = new BitMutation();
		// テスト記述．以下は実数値一つをパラメータとする場合の例．
		// 数値が引数の場合は 1. 数値， 2. 文字列， 3. 配列を渡した場合のテストを書く
		target.setParameter(0.1);
		assertEquals(0.1, target.getMutationRate(), 0.0);
		target.setParameter("0.1");
		assertEquals(0.1, target.getMutationRate(), 0.0);
		Object[] tmpParam = { new Double(0.1) };
		target.setParameter(tmpParam);
		assertEquals(0.1, target.getMutationRate(), 0.0);

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

	public void testGetParameterInfo() {
		BitMutation m = new BitMutation();
		m.setParameter(0.015);
		assertEquals(m.getParameterInfo(), "MUTATION_RATE:0.015");
	}

	public void testToString() {
		BitMutation m = new BitMutation();
		m.setParameter(0.022);
		assertEquals(m.toString(), "BitMutation{MUTATION_RATE:0.022}");
	}
}
