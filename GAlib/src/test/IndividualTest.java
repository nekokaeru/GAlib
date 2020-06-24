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

import fitness.FitnessManager;
import gabuilder.GAParameters;

import java.util.Arrays;
import java.util.HashSet;

import junit.framework.TestCase;
import population.Individual;
import problem.BinaryNumberProblem;
import problem.BitCountProblem;
import util.MyRandom;
import util.NStatistics;
/**
 * @author mori
 * @version 1.0
 */

public class IndividualTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(IndividualTest.class);
	}

	public IndividualTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		FitnessManager.reset();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		FitnessManager.reset();
		MyRandom.reset();
	}
	/*
	 * Test method for 'population.Individual.Individual()'
	 */
	public void testIndividual() {
		Individual indiv = new Individual();
		assertEquals(GAParameters.DEFAULT_CHROMOSOME_LENGTH,indiv.size());
		//整数による初期化
		for (int i = 0; i < indiv.size(); i++) {
			assertTrue(indiv.getGeneAt(i) instanceof Integer);
		}
	}

	/*
	 * Test method for 'population.Individual.Individual(int)'
	 */
	public void testIndividualInt() {
		Individual indiv = new Individual(123);
		assertEquals(123,indiv.size());
		indiv = new Individual(4);
		assertEquals(indiv.size(),4);
		//０でも初期化可能
		indiv = new Individual(0);
		assertEquals(0,indiv.size());
		for (int i = 0; i < indiv.size(); i++) {		
			assertTrue(indiv.getGeneAt(i) instanceof Integer);
		}
	}

	/*
	 * Test method for 'population.Individual.Individual(Number[])'
	 */
	public void testIndividualNumberArray() {
		//Integer
		Number[] ia = {1,0,1,0,1,1};
		Individual indiv = new Individual(ia);
		assertEquals(ia.length,indiv.size());
		for (int i = 0; i < ia.length; i++) {
			assertEquals(ia[i],indiv.getGeneAt(i));
		}
		//外部の影響を受けないかのテスト
		Arrays.fill(ia,2);
		for (int i = 0; i < ia.length; i++) {
			assertFalse(indiv.getGeneAt(i)==ia[i]);
		}		
		for (int i = 0; i < indiv.size(); i++) {
			assertTrue(indiv.getGeneAt(i) instanceof Integer);
		}
		// Double
		Number[] da = {1.0,0.0,1.0,0.0,1.0,1.0};
		Individual indiv2 = new Individual(da);
		assertEquals(da.length,indiv2.size());
		for (int i = 0; i < da.length; i++) {
			assertEquals(da[i],indiv2.getGeneAt(i));
		}
		//外部の影響を受けないかのテスト		
		Arrays.fill(da,2.0);
		for (int i = 0; i < da.length; i++) {
			assertFalse(indiv2.getGeneAt(i)==da[i]);
		}		
		for (int i = 0; i < indiv2.size(); i++) {		
			assertTrue(indiv2.getGeneAt(i) instanceof Double);
		}
	}

	/*
	 * Test method for 'population.Individual.Individual(String)'
	 */
	public void testIndividualString() {
		String s = "11001101";
		Individual indiv = new Individual(s);
		assertEquals(s.length(),indiv.size());
		assertEquals(s,indiv.toString());
		for (int i = 0; i < indiv.size(); i++) {
			assertTrue(indiv.getGeneAt(i) instanceof Integer);
		}
		try{
			s="01a"; //aは駄目
			indiv = new Individual(s);
			fail("IllegalArgumentException を投げるべき");
		}catch(IllegalArgumentException e){
			assertTrue(true);
		}
	}

	/*
	 * Test method for 'population.Individual.Individual(Individual)'
	 */
	public void testIndividualIndividual() {
		Individual indiv1 = new Individual("11111");
		Individual indiv2 = new Individual(indiv1);
		assertEquals(indiv1.size(),indiv2.size());
		for (int i = 0; i < indiv1.size(); i++) {
			assertEquals(indiv1.getGeneAt(i),indiv2.getGeneAt(i));
		}
		//深いコピーであることのチェック
		for (int i = 0; i < indiv1.size(); i++) {
			indiv1.setGeneAt(i,0); //遺伝子を全部0で書き換え．
		}
		for (int i = 0; i < indiv1.size(); i++) {
			assertFalse(indiv1.getGeneAt(i)==indiv2.getGeneAt(i));
		}
		//適応度の更新情報フラグのテスト
		//デフォルトは true (適応度を計算する)
		assertTrue(indiv2.isChanged());
		indiv1.setChanged(false);
		indiv2 = new Individual(indiv1);
		assertFalse(indiv2.isChanged());
	}

	public void testInit(){
		Individual i1 = new Individual(20);
		int[] sum = new int[20];
		Arrays.fill(sum, 0);
		int maxi = 3000;
		for (int i = 0; i < maxi; i++) {
			for (int j = 0; j < 20; j++) {
				sum[j]+=(Integer)i1.getGeneAt(j);	
			}
			i1.init();
		}
		//統計テスト．
		for (int j = 0; j < sum.length; j++) {
			// 確率的に false になることあり. マージン標準偏差内にあることを調べる．
			assertTrue(NStatistics.binomialTest(maxi, sum[j], 0.5, TestParameters.SIGMA_MARGIN));
		}
	}
	
	public void testClone(){
		Number[] chrom = new Number[20];
		Arrays.fill(chrom,0);
		Individual i1 = new Individual(chrom);
		//deep copy
		Individual i2 = (Individual)i1.clone();
		// shallow copy
		Individual i3 = i1;
		// 染色体も deep copy
		Individual i4 = new Individual(i1.getChromosome());
		// コピーコンストラクタ
		Individual i5 = new Individual(i1);		
		
		for (int i = 0; i < chrom.length; i++) {
			i1.setGeneAt(i,1);
		}
		
		for(int i = 0; i < 10; i++) {
			assertEquals(1,i1.getGeneAt(i));
			assertEquals(0,i2.getGeneAt(i));
			assertEquals(1,i3.getGeneAt(i));
			assertEquals(0,i4.getGeneAt(i));
			assertEquals(0,i5.getGeneAt(i));
		}
		
		for (int i = 0; i < chrom.length; i++) {
			i1.setGeneAt(i,0);
		}
		
		Arrays.fill(chrom,1);
		i1.setChromosome(chrom);
		for (int i = 0; i < 10; i++) {
			assertEquals(1,i1.getGeneAt(i));
			assertEquals(0,i2.getGeneAt(i));
			assertEquals(1,i3.getGeneAt(i));
			assertEquals(0,i4.getGeneAt(i));
			assertEquals(0,i5.getGeneAt(i));			
		}

		for (int i = 0; i < chrom.length; i++) {
			i1.setGeneAt(i,0);
		}
		chrom = new Number[20];
		Arrays.fill(chrom,1);
		i1.setChromosome(chrom);
		for (int i = 0; i < 10; i++) {
			assertEquals(1,i1.getGeneAt(i));
			assertEquals(0,i2.getGeneAt(i));
			assertEquals(1,i3.getGeneAt(i));
			assertEquals(0,i4.getGeneAt(i));
			assertEquals(0,i5.getGeneAt(i));			
		}
		//適応度更新情報がコピーされるかのテスト
		//デフォルト true
		assertTrue(i1.isChanged());
		i1.setChanged(false);
		i2 = i1.clone();
		assertFalse(i2.isChanged());
		//異なる型による結果が引き継がれているかのテスト．
		Number[] chrom2 = {0,(short)0,0L,0.0,0.0f}; //Integer, Short, Long, Double, Float
		Individual i6 = new Individual(chrom2);
		assertTrue(i6.getGeneAt(0) instanceof Integer);
		assertTrue(i6.getGeneAt(1) instanceof Short);
		assertTrue(i6.getGeneAt(2) instanceof Long);
		assertTrue(i6.getGeneAt(3) instanceof Double);
		assertTrue(i6.getGeneAt(4) instanceof Float);
		Individual i7 = i6.clone();
		assertTrue(i7.getGeneAt(0) instanceof Integer);
		assertTrue(i7.getGeneAt(1) instanceof Short);
		assertTrue(i7.getGeneAt(2) instanceof Long);
		assertTrue(i7.getGeneAt(3) instanceof Double);
		assertTrue(i7.getGeneAt(4) instanceof Float);		
	}

	public void testEquals(){
		Individual i1 = new Individual("101");
		Individual i2 = new Individual("101");
		Individual i3 = new Individual("100");
		assertFalse(i1==i2); //参照は違う．
		assertTrue(i1.equals(i2)); // equlas では等しい．
		assertFalse(i1.equals(i3)); // equlas でも等しくない．
		assertTrue(i1.equals(i1)); // 自分とは常に等しい．
		i2.setGeneAt(0, 1L); //同じ１でも，Integer と Long は違う．
		assertFalse(i1.equals(i2)); // equlas では等しい．
		Number[] chrom = {0.0,1.0,2.0};
		i1.setChromosome(chrom);
		i2.setChromosome(chrom);		
		//この場合は，浅いコピーになっているが Number はオブジェクトなので問題なし．
		assertTrue(i1.getGeneAt(0)==i2.getGeneAt(0));
		assertTrue(i1.equals(i2)); // equlas では等しい．
		i2.setGeneAt(0, 0);
		assertFalse(i1.equals(i2)); // 型が違えば結果も違う．
		Double d1 = new Double(1.1);
		Double d2 = new Double(1.1);
		i1.setGeneAt(0, d1);
		i2.setGeneAt(0, d2);
		assertFalse(i1.getGeneAt(0)==i2.getGeneAt(0));//参照先は別
		assertTrue(i1.equals(i2)); // equlas では等しい．		
	}
		
	public void testHashCode(){
		Individual i1 = new Individual("101");
		Individual i2 = new Individual("101");
		Individual i3 = new Individual("100");
		assertEquals(i1.hashCode(), i2.hashCode()); //hashCode は同じ．
		assertEquals(i1.hashCode(), i1.hashCode()); //自分とは常に hashCode が等しい．
		HashSet<Individual> set = new HashSet<Individual>();
		Individual i4 = new Individual("100");
		i4.setGeneAt(0, 1L);
		for (int i = 0; i < 10; i++) {
			set.add(i1.clone());
			set.add(i2.clone()); //i1 と i2 は同じものとして登録される．
			set.add(i3.clone()); 
			set.add(i4.clone());// i3 と i4 は遺伝子座0の型が違うので違うもの．
		}
		assertEquals(3, set.size()); // i1(i2), i3, i4 がある．
		assertTrue(set.contains(i1));
		assertTrue(set.contains(i2));
		assertTrue(set.contains(i3));
		assertTrue(set.contains(i3.clone()));
		assertTrue(set.contains(i4));
		Number[] chrom = {0.0,1.0,2.0};
		i1.setChromosome(chrom);
		i2.setChromosome(chrom);
		//遺伝子型を変えればもう含まれていない．
		assertFalse(set.contains(i1));
		assertFalse(set.contains(i2));		
		set.add(i1.clone());
		//含まれた．
		assertEquals(4, set.size());		
		assertTrue(set.contains(i1));
		assertTrue(set.contains(i2));
	}	
	
	/*
	 * Individual の getChromosome は深いコピーを返す．
	 * Test method for 'population.Individual.getChromosome()'
	 */
	public void testGetChromosome() {
		Individual i1 = new Individual();
		for (int i = 0; i < i1.size(); i++){
			i1.setGeneAt(i,1);
		}
		Number[] ch = i1.getChromosome(); // clone が返る．本体は返らない．
		for (int i = 0; i < ch.length; i++) {
			ch[i] = 3;
		}
		for (int i = 0; i < i1.size(); i++){
			assertEquals(1,i1.getGeneAt(i)); // ch と i1 の chromosome は無関係
		}		
		String s1 = i1.toString();
		i1.setGeneAt(0, 1L); //Long の １
		String s2 = i1.toString();
		assertEquals(s1, s2); //Integer の １ も Long の １ も文字列表現は同じ．
		//初めの遺伝子座のみが Long
		assertTrue(i1.getChromosome()[0] instanceof Long);
		assertTrue(i1.getChromosome()[1] instanceof Integer);
	}
	/**
	 * Individual の setChromosome は深いコピーをする．
	 */
	public void testSetChromosome() {
		Individual i1 = new Individual();
		Number[] ch = new Number[i1.size()];
		for (int i = 0; i < ch.length; i++) {
			ch[i] = 1;
		}
		i1.setChromosome(ch); // 深いコピーが渡る
		for (int i = 0; i < ch.length; i++) {
			ch[i] = 3;
		}
		for (int i = 0; i < i1.size(); i++){
			assertEquals(1,i1.getGeneAt(i)); // ch と i1 の chromosome は無関係
			assertTrue(i1.getGeneAt(i) instanceof Integer); //型は Integer			
		}
		//Double
		for (int i = 0; i < ch.length; i++) {
			ch[i] = i/10.0; // Double で設定．
		}
		i1.setChromosome(ch); // 深いコピーが渡る
		for (int i = 0; i < ch.length; i++) {
			ch[i] = 3;
		}
		for (int i = 0; i < i1.size(); i++){
			assertEquals(i/10.0,i1.getGeneAt(i)); // ch と i1 の chromosome は無関係
			assertTrue(i1.getGeneAt(i) instanceof Double); //型は Double
		}
	}
	/*
	 * Test method for 'population.Individual.getSize()'
	 */
	public void testGetSize() {
		// 引数なしの場合は遺伝子長 10
		Individual i1 = new Individual();
		assertEquals(10,i1.size());
		for (int i = 0; i < 10; i++) {
			Individual i2 = new Individual(i);
			assertEquals(i,i2.size());
		}
	}
	
	public void testIsSameChromosome(){
		Number[] c1 = {0,0,0};
		Number[] c2 = {0,0,1};
		Number[] c3 = {0,0,1};
		Number[] c4 = {0,1,1};
		Individual i1 = new Individual(c1);
		Individual i2 = new Individual(c2);
		Individual i3 = new Individual(c3);
		Individual i4 = new Individual(c4);
		//自分は同じ
		assertTrue(i1.equals(i1));
		assertTrue(i2.equals(i2));		
		assertTrue(i3.equals(i3));				
		assertTrue(i4.equals(i4));				
		//染色体が同じ
		assertTrue(i2.equals(i3));
		assertTrue(i3.equals(i2));		
		//染色体が違う		
		assertFalse(i2.equals(i1));				
		assertFalse(i1.equals(i2));				
		assertFalse(i3.equals(i4));						
		assertFalse(i4.equals(i3));
		//長さが違う．
		Number[] c5 = {0,0};
		Individual i5 = new Individual(c5);
		assertFalse(i1.equals(i5));				
		//同じ０でも型が違うと結果が違う．
		Number[] c6 = {0,0,0L};
		Number[] c7 = {0,0,(short)0};
		Number[] c8 = {0,0,0.0};
		Individual i6 = new Individual(c6);
		Individual i7 = new Individual(c7);
		Individual i8 = new Individual(c8);
		assertFalse(i1.equals(i6));				
		assertFalse(i1.equals(i7));				
		assertFalse(i1.equals(i8));						
	}
	public void testFitness(){
		FitnessManager.setProblem(new BitCountProblem());
		Number[] c = {1,0,1,0,1};
		Individual i = new Individual(c);
		assertEquals(3.0,i.fitness(),0);
		assertTrue(FitnessManager.getElite().equals(i)); //始めは必ずエリート
		assertEquals(1,FitnessManager.getTotalEvalNum());//適応度評価回数は１		
		Number[] c2 = {0,0,1,0,0};
		Individual i2 = new Individual(c2);
		assertEquals(1.0,i2.fitness(),0);	
		assertTrue(FitnessManager.getElite().equals(i)); //エリートは更新されない
		assertEquals(2,FitnessManager.getTotalEvalNum());//適応度評価回数は2				
	}

	public void testSimpleFitness() {
		FitnessManager.setProblem(new BinaryNumberProblem());
		Number[] c = {1,0,0,1};
		Individual i = new Individual(c);
		assertEquals(9.0,i.simpleFitness(),0); 
		assertEquals(1,FitnessManager.getTotalEvalNum());//適応度評価回数は1						
		assertEquals(null,FitnessManager.getElite()); //simpleFitness はエリートは更新しない．		
	}

	public void testFitnessWithoutRecord() {
		FitnessManager.setProblem(new BinaryNumberProblem());
		Number[] c = {1,1,0,0};
		Individual i = new Individual(c);
		assertEquals(12.0,i.fitnessWithoutRecord(),0); 
		assertEquals(0,FitnessManager.getTotalEvalNum());//fitnessWithoutRecord では適応度評価回数は増えない．
		assertEquals(null,FitnessManager.getElite()); //fitnessWithoutRecord はエリートは更新しない．			
	}
	public void testGetAndSetSeparator() {
		//区切り文字の初期値は ""
		assertEquals("",Individual.getSeparator());		
		Individual i = new Individual("1001");
		assertEquals("1001",i.toString());
		//区切り文字を , にする．
		Individual.setSeparator(",");
		assertEquals(",",Individual.getSeparator());				
		assertEquals("1,0,0,1",i.toString());
		//区切り文字を ::にする．
		Individual.setSeparator("::");
		assertEquals("::",Individual.getSeparator());				
		assertEquals("1::0::0::1",i.toString());
		//区切り文字を元に戻しておく．
		Individual.setSeparator("");
		
	}
}






