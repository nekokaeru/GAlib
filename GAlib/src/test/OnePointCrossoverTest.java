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

/*
 * Created on 2005/07/14
 * 
 */
package test;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import operator.OnePointCrossover;
import population.Individual;
import population.Population;
import util.MyRandom;
import util.NStatistics;

/**
 * @author mori
 * @version 1.0
 */
public class OnePointCrossoverTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(OnePointCrossoverTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
	}

	/**
	 * Constructor for OnePointCrossoverTest.
	 * 
	 * @param arg0
	 */
	public OnePointCrossoverTest(String arg0) {
		super(arg0);
	}

	public void testCrossover() {
		OnePointCrossover opx = new OnePointCrossover();
		opx.setCrossoverRate(1);
		Number[] p1_c = new Number[10];
		Number[] p2_c = new Number[10];
		int index0, index1, maxi = 3000;
		int[] sum = new int[p1_c.length];
		Arrays.fill(sum, 0);
		Individual p1 = new Individual();
		Individual p2 = new Individual();
		// all 0 と all 1 の個体を一点交叉する．
		for (int i = 0; i < maxi; i++) {
			Arrays.fill(p1_c, 0);
			Arrays.fill(p2_c, 1);
			p1.setChromosome(p1_c);
			p2.setChromosome(p2_c);
			opx.crossover(p1, p2); //交叉
			//一点交叉は破壊的．(p1, p2 を直接書き換える)
			ArrayList<Number> chrom1 = new ArrayList<Number>();
			ArrayList<Number> chrom2 = new ArrayList<Number>();
			for (int j = 0; j < p1.size(); j++) {
				//出来た子の遺伝子型をそれぞれ ArrayList に入れる．
				chrom1.add(p1.getGeneAt(j)); 
				chrom2.add(p2.getGeneAt(j));
			}
			for (int j = 0; j < chrom1.size(); j++) {
				sum[j] += (Integer)chrom1.get(j);
			}
			//p1 の後半が切れた個体は，0のみが複数回続き，その後1のみが続いているはず．
			index0 = chrom1.lastIndexOf(chrom1.get(0));
			index1 = chrom1.indexOf(chrom1.get(chrom1.size() - 1));
			assertEquals(index0 + 1, index1);
			//p2 の後半が切れた個体は，1のみが複数回続き，その後0のみが続いているはず．			
			index0 = chrom2.lastIndexOf(chrom2.get(0));
			index1 = chrom2.indexOf(chrom2.get(chrom2.size() - 1));
			assertEquals(index0 + 1, index1);
		}
		// p1 の後半が切れた子の左端は必ず0，右端は必ず1．一点交叉は必ず内部を切る．
		assertTrue(sum[0] == 0);
		assertTrue(sum[sum.length - 1] == maxi);
		for (int i = 1; i < sum.length - 1; i++) {
			double p = (1.0 / (p1.size() - 1)) * i; // 遺伝子座 i に 1 が現れる確率．
			assertTrue(NStatistics.binomialTest(maxi,sum[i],p,TestParameters.SIGMA_MARGIN));
		}
	}

	public void testApply() {
		Population pop = new Population();
		OnePointCrossover ox = new OnePointCrossover();
		Number[] c1 = { 0, 0, 0 };
		Number[] c2 = { 1, 1, 1 };
		Individual i1 = new Individual(c1);
		Individual i2 = new Individual(c2);
		ArrayList<Individual> list = new ArrayList<Individual>();
		list.add(i1);
		list.add(i2);
		pop.setIndivList(list);
		int maxi = 3000;
		int sum = 0;
		for (double crate = 0; crate <= 1; crate += 0.2) {
			ox.setCrossoverRate(crate);
			sum = 0;
			for (int i = 0; i < maxi; i++) {
				Population tmp = pop.clone();
				ox.apply(tmp);
				// shuffle しているので, 同じなのは 0 か 1 かわからない．
				if (tmp.getIndividualAt(0).equals(
						pop.getIndividualAt(0))
						|| tmp.getIndividualAt(0).equals(
								pop.getIndividualAt(1))) {
					// 交叉が起きていなければ染色体に変化なし．
					assertTrue(tmp.getIndividualAt(1).equals(
							pop.getIndividualAt(1))
							|| tmp.getIndividualAt(1).equals(
									pop.getIndividualAt(0)));
				} else {
					// 交叉が起きていれば染色体が変化．
					assertFalse(tmp.getIndividualAt(1).equals(
							pop.getIndividualAt(1)));
					assertFalse(tmp.getIndividualAt(1).equals(
							pop.getIndividualAt(0)));
					sum++; // 交叉が起きた回数．
				}
			}
			assertTrue(NStatistics.binomialTest(maxi,sum,ox.getCrossoverRate(),TestParameters.SIGMA_MARGIN));			
		}
	}

	public void testGetName(){
		OnePointCrossover ox = new OnePointCrossover();
		assertEquals("OnePointCrossover",ox.getName());
	}
	/**
	 * クラス OnePointCrossover の setParameter(Object ... params) のテスト．
	 * @see OnePointCrossover#setParameter
	 **/

	public void testSetParameter() {
		OnePointCrossover target = new OnePointCrossover();

		//TODO テスト記述．以下は実数値一つをパラメータとする場合の例．
		//数値が引数の場合は 1. 数値， 2. 文字列， 3. 配列を渡した場合のテストを書く
		target.setParameter(1.0);
		assertEquals(1.0,target.getCrossoverRate(), 0.0);
		target.setParameter("2");
		assertEquals(2.0,target.getCrossoverRate(), 0.0);
		Object[] tmpParam = { new Double(3) };
		target.setParameter(tmpParam);
		assertEquals(3.0,target.getCrossoverRate(), 0.0);

		//TODO 例外の発生テスト．
		//引数の数がおかしいと例外発生．パラメータ数
		try {
			target.setParameter("1", "2");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		//引数の内容がおかしいと例外発生．文字列の内容
		try {
			target.setParameter("num");
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	/**
	 * @see operator.AbstractCrossover#getParameterInfo()
	 */
	public void testGetParameterInfo(){
		OnePointCrossover target = new OnePointCrossover();
		target.setCrossoverRate(1.2);
		assertEquals(target.getParameterInfo(),"CROSSOVER_RATE:1.2");
	}
	
	/**
	 * @see operator.AbstractCrossover#toString()
	 */
	public void testToString(){
		OnePointCrossover target = new OnePointCrossover();
		// 交叉率を 1.2 に設定
		target.setCrossoverRate(1.2);
		assertEquals("OnePointCrossover{CROSSOVER_RATE:1.2}", target.toString());
	}
}
