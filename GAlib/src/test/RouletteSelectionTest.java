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
 * Created on 2005/07/15
 * 
 */
package test;

import java.util.ArrayList;

import junit.framework.TestCase;
import operator.RouletteSelection;
import operator.transform.IArrayTransform;
import operator.transform.LinearScalingTransform;
import population.Individual;
import population.Population;
import problem.BitCountProblem;
import util.MyRandom;
import util.NStatistics;
import fitness.FitnessManager;

/**
 * @author mori
 * @version 1.0
 */
public class RouletteSelectionTest extends TestCase {
	RouletteSelection rs_;  
    public static void main(String[] args) {
        junit.textui.TestRunner.run(RouletteSelectionTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);        
        rs_ = new RouletteSelection();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        MyRandom.reset();        
    }

    /**
     * Constructor for RouletteSelectionTest.
     * @param arg0
     */
    public RouletteSelectionTest(String arg0) {
        super(arg0);
    }

    public void testSelect() {
        double[] data={0,1,2,3,4,5,6,7,8,9,10};
        int[] result = new int[data.length];        
        double sum = 0;
        int maxi = 3000;
        for (int i = 0; i < data.length; i++) {
            sum+=data[i];
            result[i]=0;
        }
        int[] indexArray;
        for (int i = 0; i < maxi; i++) {
            indexArray=rs_.select(data);
            for (int j = 0; j < indexArray.length; j++) {
                result[indexArray[j]]++;
            }
        }
        //適応度０の個体は選ばれない
        assertTrue(result[0]==0);
        for (int i = 1; i < result.length; i++) {
        	double p = data[i]/sum; // i 番目が選ばれる確率．
            assertTrue(NStatistics.binomialTest(maxi*data.length,result[i],p,TestParameters.SIGMA_MARGIN));
        }
    }

    public void testApply(){
        Population pop = new Population();
        RouletteSelection rs = new RouletteSelection();
        Number[] c1 = {0,0,0}; //適応度0
        Number[] c2 = {0,1,0}; //適応度1
        Number[] c3 = {0,1,1}; //適応度2
        Number[] c4 = {1,1,1}; //適応度3
        Individual i1 = new Individual(c1);
        Individual i2 = new Individual(c2);
        Individual i3 = new Individual(c3);        
        Individual i4 = new Individual(c4);        
        ArrayList<Individual> list = new ArrayList<Individual>();
        list.add(i1);
        list.add(i2);
        list.add(i3);
        list.add(i4);
        pop.setIndivList(list);
        FitnessManager.setProblem(new BitCountProblem());
        int maxi = 3000;
        int[] result = new int[pop.getPopulationSize()];
        double sum = 0;
        
        for (int i = 0; i < pop.getPopulationSize(); i++) {
			result[i] = 0;
			sum += pop.getIndividualAt(i).fitness();
		}
        for (int i = 0; i < maxi; i++) {
			Population tmp = pop.clone();
			rs.apply(tmp);
			for (int j = 0; j < tmp.getPopulationSize(); j++) {
				result[ (int)tmp.getIndividualAt(j).fitness() ]++;
			}
		}
        //適応度０の個体は選ばれない
        assertTrue(result[0]==0);        
        for (int i = 0; i < pop.getPopulationSize(); i++) {
            //確率的に false になることあり
        	double p = i/sum; // i 番目が選ばれる確率．
            assertTrue(NStatistics.binomialTest(maxi*pop.getPopulationSize(),result[i],p,TestParameters.SIGMA_MARGIN));
        }        
        
        //線形スケーリングをかけた場合． (3,0)->(4,1)に変換．すべての適応度が1増えるのと同じ．
        IArrayTransform ir = new LinearScalingTransform(4,1);
        rs.addTransform(ir);
        sum = 0;
        for (int i = 0; i < pop.getPopulationSize(); i++) {
			result[i] = 0;
			sum += pop.getIndividualAt(i).fitness()+1; //スケーリングの効果を追加
		}

        for (int i = 0; i < maxi; i++) {
			Population tmp = pop.clone();
			rs.apply(tmp);
			for (int j = 0; j < tmp.getPopulationSize(); j++) {
				result[ (int)tmp.getIndividualAt(j).fitness() ]++;
			}
		}
        for (int i = 0; i < pop.getPopulationSize(); i++) {
            //確率的に false になることあり
        	double p = (i+1)/sum; // i 番目が選ばれる確率．スケーリングの効果で分子が1増える．
        	assertTrue(NStatistics.binomialTest(maxi*pop.getPopulationSize(),result[i],p,TestParameters.SIGMA_MARGIN));
        }        
    }
    
    //適応度の更新フラグが適切に更新されているかのテスト
    public void testIsChanged(){
    	Population pop = new Population(100,20);// 100 個体
    	for (Individual indiv : pop.getIndivList()) {
			assertTrue(indiv.isChanged());
		}
    	rs_.apply(pop);
    	//選択時にはすべての適応度が計算される．選択では新しい個体はできない．
    	for (Individual indiv : pop.getIndivList()) {
			assertFalse(indiv.isChanged());
		}    	
    }

    public void testGetName(){
		assertEquals("RouletteSelection",rs_.getName());
	}
    
    /**
	 * クラス RouletteSelection の setParameter(Object ... params) のテスト．
	 * @see RouletteSelection#setParameter
	 **/

	public void testSetParameter() {
		RouletteSelection target = new RouletteSelection();
		target.setParameter(); //パラメータ無し．
		target.setParameter((Object[])null); //パラメータ null		
		Object p[] = new Object[0]; //パラメータ 空配列
		target.setParameter(p); 				
		//引数の数がおかしいと例外を吐く．数
		try {
			target.setParameter(3);
			fail();
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * @see RouletteSelection#getParameterInfo()
	 */
	public void testGetParameterInfo(){
		// パラメータはない
		assertEquals("", rs_.getParameterInfo());
	}
	
	/**
	 * @see operator.AbstractSelection#toString()
	 */
	public void testToString(){
		assertEquals("RouletteSelection{}", rs_.toString());
	}

}
