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
		assertEquals(1, FitnessManager.getTotalEvalNum()); // �K���x�]���񐔂͑�����
	}

	public void testGetFitnessWithoutRecord() {
		FitnessManager.setProblem(new BitCountProblem());
		Number[] c = { 1, 0, 1, 0, 1, 1 };
		assertEquals(4.0, FitnessManager.getFitnessWithoutRecord(c), 0.0);
		assertEquals(0, FitnessManager.getTotalEvalNum()); // �K���x�]���񐔂͑����Ȃ�
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
		FitnessManager.getFitnessArray(list); // �K���x���v�Z����G���[�g���X�V�����D
		assertEquals(1.0, FitnessManager.getEliteFitness(), 0.0);
		assertTrue(FitnessManager.getElite().equals(i3));
		i1.setGeneAt(0, 1);
		i1.setGeneAt(1, 1);
		i1.setGeneAt(2, 1);
		FitnessManager.getFitnessArray(list); // �K���x���v�Z����G���[�g���X�V�����D
		assertEquals(3.0, FitnessManager.getEliteFitness(), 0.0);
		assertTrue(FitnessManager.getElite().equals(i1));
		i1.setGeneAt(0, 0); // �G���[�g�̓K���x��2�ɉ�����D
		FitnessManager.getFitnessArray(list); // �K���x���v�Z����G���[�g���X�V�����D
		assertEquals(3.0, FitnessManager.getEliteFitness(), 0.0); // �G���[�g�͂��̂܂�
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
		FitnessManager.getFitnessArray(list); // �K���x��3��v�Z�����D
		assertEquals(3, FitnessManager.getTotalEvalNum());
		FitnessManager.getFitnessArray(list); // �K���x��3��v�Z�����D
		assertEquals(6, FitnessManager.getTotalEvalNum());
		FitnessManager.reset();
		i1.setChanged(true);
		i2.setChanged(true);
		i3.setChanged(true);
		FitnessManager.setTurbo(true);
		FitnessManager.getFitnessArray(list); // �K���x��3��v�Z�����D
		assertEquals(3, FitnessManager.getTotalEvalNum());
		FitnessManager.getFitnessArray(list); // �K���x���v�Z����邪�C�ύX���Ȃ��̂ŉ񐔂͑����Ȃ��D
		assertEquals(3, FitnessManager.getTotalEvalNum());
		i1.setGeneAt(0, 1); // �ύX�����D
		FitnessManager.getFitnessArray(list); // i1 �̕����������D
		assertEquals(4, FitnessManager.getTotalEvalNum());
		i2.setGeneAt(0, 1); // �ύX�����D
		FitnessManager.getFitnessArray(list); // i2 �̕����������Di1 �̕��͑������Ȃ��D
		assertEquals(5, FitnessManager.getTotalEvalNum());
		i1.setGeneAt(0, 1); // ������`�q���Z�b�g���Ă��ύX�t���O�͗����Ȃ��D
		FitnessManager.getFitnessArray(list); // �����Ȃ��D
		assertEquals(5, FitnessManager.getTotalEvalNum());
		i1.setChromosome(i1.getChromosome()); // setChromosome �͓���ł��ύX�t���O�����D
		FitnessManager.getFitnessArray(list); // �����Ȃ��D
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
		assertEquals(indiv1.fitness(), 0, 0); // turbo �ł͂Ȃ��̂ő�����D
		assertEquals(2, FitnessManager.getTotalEvalNum());
		FitnessManager.setTurbo(true);
		assertEquals(indiv1.fitness(), 0, 0); // turbo �̏ꍇ�͑����Ȃ��D
		assertEquals(2, FitnessManager.getTotalEvalNum());
		assertEquals(indiv2.fitness(), 1, 0); // turbo �̏ꍇ���V�����̂͑�����D
		assertEquals(3, FitnessManager.getTotalEvalNum());
		assertEquals(indiv2.fitness(), 1, 0); // 2��ڂ͑����Ȃ��D�D
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
		// fitnessWithoutRecord �G���[�g���C�K���x�]���񐔂������Ȃ��D
		indiv.fitnessWithoutRecord();
		assertEquals(null, FitnessManager.getElite());
		assertEquals(Double.NEGATIVE_INFINITY, FitnessManager.getEliteFitness());
		assertEquals(0, FitnessManager.getTotalEvalNum());
		// simpleFitness �G���[�g�͍X�V����Ȃ����C�K���x�]���񐔂͑�����D
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
		// �L���͑����Ȃ��D
		assertEquals(0, FitnessManager.getMemoryBank().size());
		assertEquals(1, FitnessManager.getTotalEvalNum());
		FitnessManager.setMemory(true);
		assertTrue(FitnessManager.isMemory());
		indiv.fitnessWithoutRecord();// �L������Ȃ��D
		assertEquals(0, FitnessManager.getMemoryBank().size());
		assertEquals(1, FitnessManager.getTotalEvalNum());
		indiv.simpleFitness();// �L������Ȃ����K���x�]���񐔂͑�����D
		assertEquals(0, FitnessManager.getMemoryBank().size());
		assertEquals(2, FitnessManager.getTotalEvalNum());

		indiv.fitness();// �L�������D
		assertEquals(3, FitnessManager.getTotalEvalNum());
		assertEquals(1, FitnessManager.getMemoryBank().size());
		// �L���������o����D
		assertEquals(indiv.fitness(), FitnessManager.getMemoryBank()
				.getFitness(indiv));
		indiv.fitness(); // �L���ɂ���̂œK���x�]���񐔂͑����Ȃ��D
		assertEquals(3, FitnessManager.getTotalEvalNum());
		// fitnessWithoutRecord �� simpleFitness �ł��K���x�̒l�͓����D
		assertEquals(indiv.fitnessWithoutRecord(), FitnessManager
				.getMemoryBank().getFitness(indiv));
		assertEquals(3, FitnessManager.getTotalEvalNum());
		assertEquals(indiv.simpleFitness(), FitnessManager.getMemoryBank()
				.getFitness(indiv));
		// simpleFitness �̓������[���g��Ȃ��̂ŏ�̕]���ň��K���x�]���񐔂������Ă��܂��D
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
		// �f�t�H���g�Ő�������Ă��� MemoryBank �ɂ͋L������Ă��Ȃ��D
		assertFalse(FitnessManager.getMemoryBank().containsIndividual(indiv));
		// indiv ��o�^���� memory bank ���Z�b�g�D
		FitnessManager.setMemoryBank(memory);
		assertTrue(FitnessManager.getMemoryBank().containsIndividual(indiv));
	}

}
