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

package sample;

import java.util.Arrays;

import junit.framework.TestCase;

/**
 * CLike_SGA のテスト．個体数 10，遺伝子長 10 に特化しているので注意．
 * @author mori and takeuchi
 * @version 1.0
 */
public class CLike_SGATest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(CLike_SGATest.class);
	}

	public CLike_SGATest(String arg0) {
		super(arg0);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * @see sample.CLike_SGA#CLike_SGA()
	 */
	public void testSGA() {
		CLike_SGA g = new CLike_SGA();
		// 遺伝子長10
		assertEquals(10, g.LENGTH);
		for(int i = 0; i < g.pop_.length; i++){
			assertEquals(10, g.pop_[i].length);
		}
		// 個体数10
		assertEquals(10, g.POPSIZE);
		assertEquals(10, g.pop_.length);
		// 突然変異率0.1
		assertEquals(0.1, g.MRATE, 0);
		// 交叉率0.6
		assertEquals(0.6, g.CRATE, 0);
		// 世代数100
		assertEquals(100, g.GENERATION_SIZE);
	}

	/**
	 * @see sample.CLike_SGA#printPop()
	 */
	public void testPrintPop() {

	}

	/**
	 * @see sample.CLike_SGA#setElite()
	 */
	public void testSetElite() {
		CLike_SGA g = new CLike_SGA();
		for(int i = 0; i < g.pop_.length; i++) {
			g.pop_[i][0] = 0;//すべての第一遺伝子座を0にする．最適解は all 1.
		}
		g.setElite();//すべての適応度を計算し, エリートを更新.
		//始めの遺伝子が 0 なので必ずエリートの適応度は10以下．
		assertTrue(g.fitness(g.elite_)<10);
		int[] opt = new int[]{1,1,1,1,1,1,1,1,1,1}; //最適解．
		g.pop_[0] = opt; //最適個体をセット．
		g.setElite();//すべての適応度を計算し, エリートを更新．
		assertEquals(g.fitness(g.elite_),10,0);//エリートの適応度は当然10
		g.pop_[0][0] = 0; //最適解を壊す．
		assertEquals(g.fitness(g.elite_),10,0);//エリートの適応度は10のまま
		//すべての適応度を計算．
		for (int i = 0; i < g.POPSIZE; i++) {
			assertTrue(g.fitness(g.pop_[i])<10); //個体群中には最適解はない．
		}
	}

	/**
	 * @see sample.CLike_SGA#start()
	 */
	public void testStart() {

	}

	/**
	 * @see sample.CLike_SGA#crossover()
	 */
	public void testCrossover() {
		CLike_SGA g = new CLike_SGA();
		int[][] pop = new int[g.POPSIZE][g.LENGTH];
		// 個体群の先頭の2個体を以下のc1, c2に置き換える.
		int[] c1 = new int[g.LENGTH];
		int[] c2 = new int[g.LENGTH];
		Arrays.fill(c1, 1);
		Arrays.fill(c2, 0);
		System.arraycopy(c1, 0, g.pop_[0], 0, g.LENGTH);
		System.arraycopy(c2, 0, g.pop_[1], 0, g.LENGTH);
		for(int i = 0; i < g.POPSIZE; i++){
			System.arraycopy(g.pop_[i],0,pop[i],0,g.LENGTH);
		}
		g.POPSIZE = 2;
		int maxi = 3000;
		int nsig = 4;
		int sum = 0;
		// 個体群の先頭の2個体のみ交叉に参加可能
		for (double crate = 0; crate <= 1; crate += 0.2) {
			g.CRATE = crate;
			sum = 0;
			for (int i = 0; i < maxi; i++) {
				// 交叉前の個体群に初期化
				for(int j = 0; j < g.POPSIZE; j++){
					System.arraycopy(pop[j],0,g.pop_[j],0,g.LENGTH);
				}
				g.crossover();
				if (Arrays.equals(g.pop_[0],pop[0])) {
					// 交叉が起きていなければ染色体に変化なし．
					assertTrue(Arrays.equals(g.pop_[1],pop[1]));
				} else {
					// 交叉が起きていれば染色体が変化．
					assertFalse(Arrays.equals(g.pop_[1],pop[1]));
					sum++; // 交叉が起きた回数．
				}
			}
			double E = g.CRATE * maxi;	// 期待値
			double SD = Math.sqrt(E * (1.0 - g.CRATE));	// 標準偏差
			assertTrue(Math.abs(sum - E) <= nsig * SD);
		}
	}

	/**
	 * @see sample.CLike_SGA#mutation()
	 */
	public void testMutation() {
		CLike_SGA g = new CLike_SGA();
		int[] indiv0_c = new int[g.LENGTH];
		Arrays.fill(indiv0_c, 0);
		// 個体群の全個体の全遺伝子を0にする
		for(int i = 0; i < g.POPSIZE; i++){
			System.arraycopy(indiv0_c, 0, g.pop_[i], 0, g.LENGTH);
		}
		// 突然変異率0の場合は変化なし
		g.MRATE = 0.0;
		g.mutation();
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < g.LENGTH; j++){
				assertFalse(g.pop_[i][j] == 1);
			}
		}
		// 突然変異率1の場合はすべて変化
		g.MRATE = 1.0;
		g.mutation();
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < g.LENGTH; j++){
				assertFalse(g.pop_[i][j] == 0);
			}
		}
		double mrate;
		double[] sum0 = new double[g.LENGTH];
		int maxi = 10000;
		double nsig = 4; //何シグマまで調べるか．
		for (mrate = 0.2; mrate < 1; mrate += 0.2) {
			g.MRATE = mrate;
			Arrays.fill(sum0, 0);
			for (int i = 0; i < maxi; i++) {
				// 突然変異前の個体群に初期化
				Arrays.fill(indiv0_c, 0);
				for(int j = 0; j < g.POPSIZE; j++){
					System.arraycopy(indiv0_c, 0, g.pop_[j], 0, g.LENGTH);
				}
				g.mutation();
				for (int j = 0; j < g.pop_[0].length; j++) {
					sum0[j] += g.pop_[0][j];
				}
			}
			for (int j = 0; j < g.pop_[0].length; j++) {
				// 確率的に false になることあり. 4σ内にあることを調べる．
				double E = g.MRATE * maxi;	// 期待値
				double SD = Math.sqrt(E * (1 - g.MRATE));	// 標準偏差
				assertTrue(Math.abs(sum0[j]-E)<=nsig*SD);
			}
		}
	}

	/**
	 * @see sample.CLike_SGA#selection()
	 */
	public void testSelection() {
		CLike_SGA g = new CLike_SGA();
		// 個体群の各個体のインデックスと適応度が等しくなるように遺伝子を変更
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < i % g.LENGTH; j++){
				g.pop_[i][j] = 1;	// インデックス分だけ遺伝子を１
			}
			for(int j = i; j < g.LENGTH; j++){
				g.pop_[i][j] = 0;	// その他を0
			}
		}
        int[] result = new int[g.POPSIZE];
        double sum = 0;
        int maxi = 3000;
        double nsig = 4; //何σまで調べるか．
        for (int i = 0; i < g.POPSIZE; i++) {
            sum+=g.fitness(g.pop_[i]);	// 総適応度
            result[i]=0;
        }
        int[] indexArray = new int[g.POPSIZE];
        for (int i = 0; i < maxi; i++) {
        	// 選択前の個体群に初期化
        	for(int k = 0; k< g.POPSIZE;k++){
    			for(int j = 0; j < k % g.LENGTH; j++){
    				g.pop_[k][j] = 1;
    			}
    			for(int j = k; j < g.LENGTH; j++){
    				g.pop_[k][j] = 0;
    			}
    		}
            g.selection();
            for(int j = 0; j < g.POPSIZE; j++){
            	// 選ばれた個体の適応度つまりインデックスを返す
            	indexArray[j] = g.fitness(g.pop_[j]);
            }
            for(int j = 0; j < indexArray.length; j++){
            	// 各インデックスごとの選択された回数をカウント
                result[indexArray[j]]++;
            }
        }
        //適応度０の個体は選ばれない
        assertTrue(result[0]==0);
        for (int i = 1; i < result.length; i++) {
            //確率的に false になることあり
        	double p = i/sum; // i 番目が選ばれる確率．
        	double E = p * maxi *  g.POPSIZE; //期待値
        	double SD = Math.sqrt(E * (1 - p)); //標準偏差
            assertTrue(Math.abs(result[i]-E)< nsig*SD);
        }
	}

	/**
	 * @see sample.CLike_SGA#returnElite()
	 */
	public void testReturnElite() {
		CLike_SGA g = new CLike_SGA();
		// 個体群の各個体のインデックスと適応度が等しくなるように遺伝子を変更
		// つまり個体群の最小適応度は0, 最大適応度は9
		double[] data={0,1,2,3,4,5,6,7,8,9};
		for(int i = 0; i < g.POPSIZE; i++){
			for(int j = 0; j < data[i]; j++){
				g.pop_[i][j] = 1;
			}
			for(int j = (int)data[i]; j < g.LENGTH; j++){
				g.pop_[i][j] = 0;
			}
		}
		int[] opt = new int[]{1,1,1,1,1,1,1,1,1,1}; //最適解．
		g.elite_ = opt; //最適個体をエリートにセット．
		g.returnElite();	// エリートを個体群に戻す
		// 適応度が最小の個体とエリートが交換される
		assertEquals(10.0, g.fitness(g.pop_[0]), 0);
	}

	/**
	 * @see sample.CLike_SGA#fitness(int[])
	 */
	public void testFitness() {
		CLike_SGA g = new CLike_SGA();
		int[] c1 = new int[g.POPSIZE];
		int[] c2 = new int[g.POPSIZE];
		Arrays.fill(c1, 0);
		Arrays.fill(c2, 1);
		// 最小適応度0
		assertEquals(0, g.fitness(c1));
		// 最大適応度10
		assertEquals(10, g.fitness(c2));
	}

}
