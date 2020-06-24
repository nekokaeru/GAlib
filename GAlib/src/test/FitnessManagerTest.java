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

import java.util.ArrayList;

import fitness.FitnessManager;
import fitness.FitnessMemoryBank;

import population.Individual;
import problem.BitCountProblem;
import util.MyRandom;
import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */

public class FitnessManagerTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FitnessManagerTest.class);
	}

	public FitnessManagerTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();
		MyRandom.setSeed(TestParameters.SEED);
		FitnessManager.reset();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
		FitnessManager.reset();
	}

	/*
	 * Test method for 'operator.FitnessManager.getFitnessArray(ArrayList<Individual>)'
	 */
	public void testGetFitnessArray() {
		FitnessManager.setProblem(new BitCountProblem());
		ArrayList<Individual> list = new ArrayList<Individual>();
		Number[] c1 = { 0, 0, 0 };
		Number[] c2 = { 1, 1, 0 };
		Number[] c3 = { 0, 1, 1 };
		Number[] c4 = { 0, 1, 0 };
		Number[] c5 = { 1, 1, 1 };
		list.add(new Individual(c1));
		list.add(new Individual(c2));
		list.add(new Individual(c3));
		list.add(new Individual(c4));
		list.add(new Individual(c5));
		double[] allFitness = FitnessManager.getFitnessArray(list);
		assertEquals(0.0, allFitness[0], 0.0);
		assertEquals(2.0, allFitness[1], 0.0);
		assertEquals(2.0, allFitness[2], 0.0);
		assertEquals(1.0, allFitness[3], 0.0);
		assertEquals(3.0, allFitness[4], 0.0);
	}

	public void testGetFitness() {
		FitnessManager.setProblem(new BitCountProblem());
		Number[] c = { 1, 0, 1, 0, 1, 1 };
		assertEquals(4.0, FitnessManager.getFitness(c), 0.0);
		assertEquals(1, FitnessManager.getTotalEvalNum()); // 適応度評価回数は増える
	}

	public void testGetFitnessWithoutRecord() {
		FitnessManager.setProblem(new BitCountProblem());
		Number[] c = { 1, 0, 1, 0, 1, 1 };
		assertEquals(4.0, FitnessManager.getFitnessWithoutRecord(c), 0.0);
		assertEquals(0, FitnessManager.getTotalEvalNum()); // 適応度評価回数は増えない
	}

	/*
	 * Test method for 'operator.FitnessManager.updateElite(Individual, double)'
	 */
	public void testUpdateElite() {
		FitnessManager.setProblem(new BitCountProblem());
		ArrayList<Individual> list = new ArrayList<Individual>();
		Number[] c1 = { 0, 0, 0 };
		Number[] c2 = { 0, 0, 0 };
		Number[] c3 = { 0, 0, 1 };
		Individual i1 = new Individual(c1);
		Individual i2 = new Individual(c2);
		Individual i3 = new Individual(c3);
		list.add(i1);
		list.add(i2);
		list.add(i3);
		assertEquals(null, FitnessManager.getElite());
		assertEquals(Double.NEGATIVE_INFINITY, FitnessManager.getEliteFitness());
		FitnessManager.getFitnessArray(list); // 適応度が計算されエリートが更新される．
		assertEquals(1.0, FitnessManager.getEliteFitness(), 0.0);
		assertTrue(FitnessManager.getElite().equals(i3));
		i1.setGeneAt(0, 1);
		i1.setGeneAt(1, 1);
		i1.setGeneAt(2, 1);
		FitnessManager.getFitnessArray(list); // 適応度が計算されエリートが更新される．
		assertEquals(3.0, FitnessManager.getEliteFitness(), 0.0);
		assertTrue(FitnessManager.getElite().equals(i1));
		i1.setGeneAt(0, 0); // エリートの適応度を2に下げる．
		FitnessManager.getFitnessArray(list); // 適応度が計算されエリートが更新される．
		assertEquals(3.0, FitnessManager.getEliteFitness(), 0.0); // エリートはそのまま
	}

	/*
	 * Test method for 'operator.FitnessManager.setTurbo(boolean)'
	 */
	public void testSetTurbo() {
		FitnessManager.setProblem(new BitCountProblem());
		ArrayList<Individual> list = new ArrayList<Individual>();
		Number[] c1 = { 0, 0, 0 };
		Number[] c2 = { 0, 0, 0 };
		Number[] c3 = { 0, 0, 1 };
		Individual i1 = new Individual(c1);
		Individual i2 = new Individual(c2);
		Individual i3 = new Individual(c3);
		list.add(i1);
		list.add(i2);
		list.add(i3);
		assertEquals(0, FitnessManager.getTotalEvalNum());
		FitnessManager.getFitnessArray(list); // 適応度が3回計算される．
		assertEquals(3, FitnessManager.getTotalEvalNum());
		FitnessManager.getFitnessArray(list); // 適応度が3回計算される．
		assertEquals(6, FitnessManager.getTotalEvalNum());
		FitnessManager.reset();
		i1.setChanged(true);
		i2.setChanged(true);
		i3.setChanged(true);
		FitnessManager.setTurbo(true);
		FitnessManager.getFitnessArray(list); // 適応度が3回計算される．
		assertEquals(3, FitnessManager.getTotalEvalNum());
		FitnessManager.getFitnessArray(list); // 適応度が計算されるが，変更がないので回数は増えない．
		assertEquals(3, FitnessManager.getTotalEvalNum());
		i1.setGeneAt(0, 1); // 変更した．
		FitnessManager.getFitnessArray(list); // i1 の分だけ増加．
		assertEquals(4, FitnessManager.getTotalEvalNum());
		i2.setGeneAt(0, 1); // 変更した．
		FitnessManager.getFitnessArray(list); // i2 の分だけ増加．i1 の分は増加しない．
		assertEquals(5, FitnessManager.getTotalEvalNum());
		i1.setGeneAt(0, 1); // 同じ遺伝子をセットしても変更フラグは立たない．
		FitnessManager.getFitnessArray(list); // 増加なし．
		assertEquals(5, FitnessManager.getTotalEvalNum());
		i1.setChromosome(i1.getChromosome()); // setChromosome は同一でも変更フラグが立つ．
		FitnessManager.getFitnessArray(list); // 増加なし．
		assertEquals(6, FitnessManager.getTotalEvalNum());
	}

	/*
	 * Test method for 'operator.FitnessManager.getTotalNum()'
	 */
	public void testGetTotalNum() {
		Number[] c1 = { 0, 0, 0 };
		Number[] c2 = { 1, 0, 0 };
		Individual indiv1 = new Individual(c1);
		Individual indiv2 = new Individual(c2);
		assertEquals(0, FitnessManager.getTotalEvalNum());
		assertEquals(indiv1.fitness(), 0, 0);
		assertEquals(1, FitnessManager.getTotalEvalNum());
		assertEquals(indiv1.fitness(), 0, 0); // turbo ではないので増える．
		assertEquals(2, FitnessManager.getTotalEvalNum());
		FitnessManager.setTurbo(true);
		assertEquals(indiv1.fitness(), 0, 0); // turbo の場合は増えない．
		assertEquals(2, FitnessManager.getTotalEvalNum());
		assertEquals(indiv2.fitness(), 1, 0); // turbo の場合も新しい個体は増える．
		assertEquals(3, FitnessManager.getTotalEvalNum());
		assertEquals(indiv2.fitness(), 1, 0); // 2回目は増えない．．
		assertEquals(3, FitnessManager.getTotalEvalNum());
	}

	/*
	 * Test method for 'operator.FitnessManager.reset()'
	 */
	public void testReset() {
		Individual indiv = new Individual();
		indiv.fitness();
		FitnessManager.setTurbo(true);
		FitnessManager.setMemory(true);
		assertEquals(indiv, FitnessManager.getElite());
		assertEquals(indiv.fitness(), FitnessManager.getEliteFitness());
		assertEquals(1, FitnessManager.getTotalEvalNum());
		assertFalse(FitnessManager.getProblem() == null);
		assertTrue(FitnessManager.isTurbo());
		assertTrue(FitnessManager.isMemory());
		FitnessManager.reset();
		assertEquals(null, FitnessManager.getElite());
		assertEquals(Double.NEGATIVE_INFINITY, FitnessManager.getEliteFitness());
		assertEquals(0, FitnessManager.getTotalEvalNum());
		assertFalse(FitnessManager.isTurbo());
		assertFalse(FitnessManager.isMemory());
		// fitnessWithoutRecord エリートも，適応度評価回数も増えない．
		indiv.fitnessWithoutRecord();
		assertEquals(null, FitnessManager.getElite());
		assertEquals(Double.NEGATIVE_INFINITY, FitnessManager.getEliteFitness());
		assertEquals(0, FitnessManager.getTotalEvalNum());
		// simpleFitness エリートは更新されないが，適応度評価回数は増える．
		indiv.simpleFitness();
		assertEquals(null, FitnessManager.getElite());
		assertEquals(Double.NEGATIVE_INFINITY, FitnessManager.getEliteFitness());
		assertEquals(1, FitnessManager.getTotalEvalNum());
	}

	/*
	 * Test method for 'operator.FitnessManager.isMemory()' Test method for
	 * 'operator.FitnessManager.setMemory()'
	 */
	public void testIsMemory() {
		assertFalse(FitnessManager.isMemory());
		Individual indiv = new Individual();
		indiv.fitness();
		// 記憶は増えない．
		assertEquals(0, FitnessManager.getMemoryBank().size());
		assertEquals(1, FitnessManager.getTotalEvalNum());
		FitnessManager.setMemory(true);
		assertTrue(FitnessManager.isMemory());
		indiv.fitnessWithoutRecord();// 記憶されない．
		assertEquals(0, FitnessManager.getMemoryBank().size());
		assertEquals(1, FitnessManager.getTotalEvalNum());
		indiv.simpleFitness();// 記憶されないが適応度評価回数は増える．
		assertEquals(0, FitnessManager.getMemoryBank().size());
		assertEquals(2, FitnessManager.getTotalEvalNum());

		indiv.fitness();// 記憶される．
		assertEquals(3, FitnessManager.getTotalEvalNum());
		assertEquals(1, FitnessManager.getMemoryBank().size());
		// 記憶を引き出せる．
		assertEquals(indiv.fitness(), FitnessManager.getMemoryBank()
				.getFitness(indiv));
		indiv.fitness(); // 記憶にあるので適応度評価回数は増えない．
		assertEquals(3, FitnessManager.getTotalEvalNum());
		// fitnessWithoutRecord や simpleFitness でも適応度の値は同じ．
		assertEquals(indiv.fitnessWithoutRecord(), FitnessManager
				.getMemoryBank().getFitness(indiv));
		assertEquals(3, FitnessManager.getTotalEvalNum());
		assertEquals(indiv.simpleFitness(), FitnessManager.getMemoryBank()
				.getFitness(indiv));
		// simpleFitness はメモリーを使わないので上の評価で一回適応度評価回数が増えてしまう．
		assertEquals(4, FitnessManager.getTotalEvalNum());
	}

	/*
	 * Test method for 'operator.FitnessManager.setMemoryBank()' Test method for
	 * 'operator.FitnessManager.getMemoryBank()'
	 */
	public void testMemoryBank() {
		Individual indiv = new Individual();
		FitnessMemoryBank memory = new FitnessMemoryBank();
		memory.put(indiv, indiv.fitness());
		// デフォルトで生成されている MemoryBank には記憶されていない．
		assertFalse(FitnessManager.getMemoryBank().containsIndividual(indiv));
		// indiv を登録した memory bank をセット．
		FitnessManager.setMemoryBank(memory);
		assertTrue(FitnessManager.getMemoryBank().containsIndividual(indiv));
	}

}
