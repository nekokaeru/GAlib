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

import fitness.FitnessManager;
import gabuilder.GAParameters;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.TestCase;
import operator.UniformCrossover;
import population.Individual;
import population.Population;
import util.MyRandom;
import util.NStatistics;

/**
 * @author mori
 * @version 1.0
 */
public class UniformCrossoverTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(UniformCrossoverTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);   
		FitnessManager.reset();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
		MyRandom.reset();      
		FitnessManager.reset();		
    }

    /**
     * Constructor for UniformCrossoverTest.
     * @param arg0
     */
    public UniformCrossoverTest(String arg0) {
        super(arg0);
    }

    public void testCrossover() {
        UniformCrossover uniformx = new UniformCrossover();
        assertEquals(GAParameters.DEFAULT_CROSSOVER_RATE,uniformx.getCrossoverRate());
        uniformx.setCrossoverRate(1);
        Individual i1 = new Individual("11111"); //遺伝子長 5
        Individual i2 = new Individual("00000");
        int maxi=3000; //テスト回数．
        int[] sum1 = new int[i1.size()];
        int[] sum2 = new int[i2.size()];
        Arrays.fill(sum1,0);
        Arrays.fill(sum2,0);
        for (int i = 0; i < maxi; i++) {
        	//交叉は破壊的なので，i1test, i2test を渡してテスト．
        	Individual i1test = i1.clone(); 
            Individual i2test = i2.clone(); 
            uniformx.crossover(i1test,i2test);
            assertEquals(i1.size(),i1test.size());
            assertEquals(i2.size(),i2test.size());
            //i1 と i2 には共通遺伝子がないので，交叉によって必ず遺伝子が変わる．
            assertFalse(i1.equals(i1test));
            assertFalse(i1.equals(i2test));
            assertFalse(i2.equals(i1test));            
            assertFalse(i2.equals(i2test));
            for (int j = 0; j < i1.size(); j++) {
            	sum1[j] += (Integer)i1test.getGeneAt(j);
            	sum2[j] += (Integer)i2test.getGeneAt(j);
            }
        }
        for (int i = 0; i < sum1.length-1; i++) {
        	//一様交叉なので， sum1, sum2 の各要素は 0.5*maxi が期待値．
        	//これが４σ内にあることを確認．
        	NStatistics.binomialTest(maxi,sum1[i],0.5,TestParameters.SIGMA_MARGIN);
        }
     }
    public void testApply(){
    	int size = 100;
    	Number[] c1 = new Number[size];
    	Number[] c2 = new Number[size];
    	Arrays.fill(c1,0);
    	Arrays.fill(c2,1);
        Individual i1 = new Individual(c1); //長さ100
        Individual i2 = new Individual(c2);
        ArrayList<Individual> list = new ArrayList<Individual>();
        //2個体づつ50回追加．
        for (int i = 0; i < 50; i++) {
        	list.add(i1.clone());
        	list.add(i2.clone());
		}
        Population pop = new Population(list);
        //50*2
        assertEquals(100,pop.getIndivList().size());
        assertEquals(100,pop.getPopulationSize());
        UniformCrossover uniformx = new UniformCrossover();
        //ランダムシャッフルをするので，all 0 と all1 の個体が交叉する確率は0.5*交叉率
        uniformx.apply(pop);
        // all 0 と all 1 で交叉した回数．
        int num = 0;
        for (Individual indiv : pop.getIndivList()) {
			if(indiv.equals(i1) || indiv.equals(i2)){
				continue;
			}
			//上の条件文を抜ければ all 0 と all 1 で交叉している． 
			num++;
			String s = indiv.toString();
			//一様交叉になっていることの簡単なチェック．
			assertTrue(s.matches(".*10*1.*")||s.matches(".*01*0.*"));
		}
        assertTrue(NStatistics.binomialTest(pop.getPopulationSize(),num,0.5*GAParameters.DEFAULT_CROSSOVER_RATE,TestParameters.SIGMA_MARGIN));
    }    
    
    public void testLocal(){
        UniformCrossover uniformx = new UniformCrossover();
        try{
        	uniformx.localTest();
        }catch (Exception e) {
        	e.printStackTrace();
        	fail("UniformCrossover.localTest() was failed!");
		}
    }
    
    /**
	 * クラス UniformCrossover の setParameter(Object ... params) のテスト．
	 * @see UniformCrossover#setParameter
	 **/

	public void testSetParameter() {
		UniformCrossover target = new UniformCrossover();

		//テスト記述．以下は実数値一つをパラメータとする場合の例．
		//数値が引数の場合は 1. 数値， 2. 文字列， 3. 配列を渡した場合のテストを書く
		target.setParameter(0.6);
		assertEquals(0.6,target.getCrossoverRate(), 0.0);
		target.setParameter("0.6");
		assertEquals(0.6,target.getCrossoverRate(), 0.0);
		Object[] tmpParam = { new Double(0.6) };
		target.setParameter(tmpParam);
		assertEquals(0.6,target.getCrossoverRate(), 0.0);

		//例外の発生テスト．
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
	public void testGetParameterInfo(){
		UniformCrossover target = new UniformCrossover();
		target.setCrossoverRate(0.3);
		assertEquals(target.getParameterInfo(),"CROSSOVER_RATE:0.3");
	}
	
	/**
	 * @see operator.UniformCrossover#getName()
	 */
	public void testGetName(){
		UniformCrossover target = new UniformCrossover();
		assertEquals("UniformCrossover", target.getName());
	}
	
	/**
	 * @see operator.AbstractCrossover#toString()
	 */
	public void testToString(){
		UniformCrossover target = new UniformCrossover();
		// 交叉率を 0.65 に設定
		target.setCrossoverRate(0.65);
		assertEquals("UniformCrossover{CROSSOVER_RATE:0.65}", target.toString());
	}
}
