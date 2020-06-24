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
import gabuilder.GABase;
import gabuilder.GAParameters;

import java.util.ArrayList;

import junit.framework.TestCase;
import operator.BitMutation;
import operator.Dummy;
import operator.Elitism;
import operator.EvaluateFitness;
import operator.IOperator;
import operator.OnePointCrossover;
import operator.RouletteSelection;
import operator.TournamentSelection;
import operator.UniformCrossover;
import population.Population;
import util.MyRandom;
import viewer.TextViewer;

/**
 * @author mori
 * @version 1.0
 */
public class GABaseTest extends TestCase {
	
	private GABase gabase_;
	public static void main(String[] args) {
		junit.textui.TestRunner.run(GABaseTest.class);
	}

	/**
	 * Constructor for GABaseTest.
	 * @param name
	 */
	public GABaseTest(String name) {
		super(name);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		//毎回新しく作る．
		gabase_=new GABase();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
		FitnessManager.reset();
	}
	/*
	 * Test method for 'gabuilder.GABase.GA()'
	 */
	public void testGA() {
		assertEquals(GAParameters.DEFAULT_GENERATION_SIZE,gabase_.getGenerationSize());
		assertEquals(0,gabase_.getCurrentGeneration());		
		assertEquals(0,gabase_.getNextIndex());				
		assertEquals("",gabase_.getOperatorInfo());						
		assertEquals(0,gabase_.getOperatorList().size());
		try{
			//オペレータリストが空っぽなので，getNextOperator を実行するとエラーになる．
			gabase_.getNextOperator();
			fail();
		}catch (Exception e) {
			assertTrue(true);
		}
	}
	
	/*
	 * Test method for 'gabuilder.GABase.sampleInit()'
	 */
	public void testSampleInit() {
		gabase_.sampleInit();
		assertEquals(100,gabase_.getGenerationSize());
		assertEquals(0,gabase_.getCurrentGeneration());		
		assertEquals(0,gabase_.getNextIndex());				
		assertEquals("OnePointCrossover{CROSSOVER_RATE:0.6}",gabase_.getOperatorList().get(0).toString());						
		assertEquals("BitMutation{MUTATION_RATE:0.02}",gabase_.getOperatorList().get(1).toString());						
		assertEquals("TournamentSelection{TOURNAMENT_SIZE:3}",gabase_.getOperatorList().get(2).toString());
		assertEquals("Elitism{IS_ELITISM:true}",gabase_.getOperatorList().get(3).toString());
		assertEquals(4,gabase_.getOperatorList().size());
		assertEquals(100,gabase_.getPopulation().getPopulationSize());
		assertEquals(50,gabase_.getPopulation().getIndividualAt(0).size());
		assertEquals("BitCount",FitnessManager.getProblem().getName());
	}	

	/*
	 * Test method for 'gabuilder.GABase.nextGeneration()'
	 */
	public void testNextGeneration() {
		gabase_.getOperatorList().add(new OnePointCrossover());
		gabase_.getOperatorList().add(new BitMutation());
		gabase_.getOperatorList().add(new TournamentSelection());
		gabase_.setPopulation(new Population(10,5));
		assertEquals(gabase_.getCurrentGeneration(),0);
		gabase_.nextGeneration();
		assertEquals(gabase_.getCurrentGeneration(),1);
		gabase_.nextGeneration();		
		assertEquals(gabase_.getCurrentGeneration(),2);
		//一度の nextOperation では世代は増えない．
		gabase_.nextOperation();		
		assertEquals(gabase_.getCurrentGeneration(),2);
		// nextGeneration で世代は増え，nextIndex_ は 0 になる．
		gabase_.nextGeneration();	
		assertEquals(gabase_.getCurrentGeneration(),3);						
		assertEquals(gabase_.getNextIndex(),0);								
	}

	/*
	 * Test method for 'gabuilder.GABase.nextOperation()'
	 */
	public void testNextOperation() {
		gabase_.getOperatorList().add(new UniformCrossover());
		gabase_.getOperatorList().add(new BitMutation());
		gabase_.getOperatorList().add(new RouletteSelection());
		gabase_.getOperatorList().add(new Elitism());		
		gabase_.setPopulation(new Population(8,12));
		int currentGeneration=-1;
		for (int i = 0; i < 100; i++) {
			if(i%4==0){
				assertEquals(gabase_.getNextOperator().getName(),"UniformCrossover");
				assertEquals(gabase_.getNextIndex(),0);
				currentGeneration++;
			}else if(i%4==1){
				assertEquals(gabase_.getNextOperator().getName(),"BitMutation");
				assertEquals(gabase_.getNextIndex(),1);				
			}else if(i%4==2){
				assertEquals(gabase_.getNextOperator().getName(),"RouletteSelection");
				assertEquals(gabase_.getNextIndex(),2);
			}else if(i%4==3){
				assertEquals(gabase_.getNextOperator().getName(),"Elitism");
				assertEquals(gabase_.getNextIndex(),3);				
			}
			assertEquals(gabase_.getCurrentGeneration(),currentGeneration);
			gabase_.nextOperation();
		}
	}

	/**
	 * @see gabuilder.GABase#getOperatorList()
	 */
	public void testGetOperatorList() {
		ArrayList<IOperator> list = new ArrayList<IOperator>();
		list.add(new OnePointCrossover());
		gabase_.setOperatorList(list);
		assertEquals("OnePointCrossover", gabase_.getOperatorList().get(0).getName());
	}

	/**
	 * @see gabuilder.GABase#setOperatorList(ArrayList)
	 */
	public void testSetOperatorList() {
		ArrayList<IOperator> list = new ArrayList<IOperator>();
		list.add(new OnePointCrossover());
		gabase_.setOperatorList(list);
		assertEquals("OnePointCrossover", gabase_.getOperatorList().get(0).getName());
	}

	/**
	 * @see gabuilder.GABase#getPopulation()
	 */
	public void testGetAndSetPopulation() {
		assertEquals(null,gabase_.getPopulation());		
		Population pop = new Population(12,8);
		gabase_.setPopulation(pop);
		assertEquals(12,gabase_.getPopulation().getPopulationSize());
		assertEquals(12,gabase_.getPopulation().getIndividualAt(0).size(),8);
	}

	/*
	 * Test method for 'gabuilder.GABase.start()'
	 */
	public void testStart() {
		gabase_.getOperatorList().add(new Dummy());
		gabase_.setGenerationSize(17);
		gabase_.start();
		assertEquals(17,gabase_.getCurrentGeneration());
	}

	/*
	 * Test method for 'gabuilder.GABase.getNextOperator()'
	 */
	public void testGetNextOperator() {
		gabase_.getOperatorList().add(new EvaluateFitness());
		gabase_.setGenerationSize(17);
		//個体数5，遺伝子長4
		gabase_.setPopulation(new Population(5,4));
		gabase_.start();
		assertEquals(17*5,FitnessManager.getTotalEvalNum());
		assertEquals(17,gabase_.getCurrentGeneration());
	}

	/*
	 * Test method for 'gabuilder.GABase.getGAParameter()'
	 */
	public void testGetGAParameter() {
		assertEquals(null,gabase_.getGAParameter());
		GAParameters param = new GAParameters();
		gabase_.setGAParameter(param);
		assertEquals(param,gabase_.getGAParameter());		
	}

	/*
	 * Test method for 'gabuilder.GABase.getCurrentGeneration()'
	 */
	public void testGetCurrentGeneration() {
		gabase_.getOperatorList().add(new Dummy());
		gabase_.getOperatorList().add(new EvaluateFitness());
		gabase_.setPopulation(new Population(1,1));
		for (int i = 0; i < 10; i++) {
			assertEquals(i,gabase_.getCurrentGeneration());
			gabase_.nextGeneration();
		}
		assertEquals(10,gabase_.getCurrentGeneration());		
		gabase_.nextOperation(); //Dummy
		assertEquals(10,gabase_.getCurrentGeneration());
		gabase_.nextOperation(); //EvaluateFitness
		assertEquals(11,gabase_.getCurrentGeneration());
	}

	/*
	 * Test method for 'gabuilder.GABase.setCurrentGeneration(long)'
	 */
	public void testSetCurrentGeneration() {
		assertEquals(0,gabase_.getCurrentGeneration());
		gabase_.setCurrentGeneration(333);
		assertEquals(333,gabase_.getCurrentGeneration());
	}

	/*
	 * Test method for 'gabuilder.GABase.getNextIndex()'
	 * Test method for 'gabuilder.GABase.setNextIndex(int)'
	 */
	public void testGetAndSetNextIndex() {
		assertEquals(0,gabase_.getNextIndex());
		// 3 が適切かはチェックしない．
		gabase_.setNextIndex(3);
		assertEquals(3,gabase_.getNextIndex());
	}

	/*
	 * Test method for 'gabuilder.GABase.getOperatorNameList()'
	 */
	public void testGetOperatorNameList() {
		gabase_.getOperatorList().add(new Dummy());
		gabase_.getOperatorList().add(new OnePointCrossover());
		gabase_.getOperatorList().add(new RouletteSelection());
		assertEquals("[Dummy, OnePointCrossover, RouletteSelection]",gabase_.getOperatorNameList().toString());
	}

	/*
	 * Test method for 'gabuilder.GABase.getOperatorInfo()'
	 */
	public void testGetOperatorInfo() {
		gabase_.getOperatorList().add(new OnePointCrossover());
		gabase_.getOperatorList().add(new BitMutation());
		gabase_.getOperatorList().add(new TournamentSelection());
		gabase_.getOperatorList().add(new Elitism());
		String[] result = gabase_.getOperatorInfo().split("\\s");
		assertEquals("OnePointCrossover{CROSSOVER_RATE:0.6}",result[0]);
		assertEquals("BitMutation{MUTATION_RATE:0.01}",result[1]);
		assertEquals("TournamentSelection{TOURNAMENT_SIZE:3}",result[2]);
		assertEquals("Elitism{IS_ELITISM:true}",result[3]);
	}

	/*
	 * Test method for 'gabuilder.GABase.addGenerationViewerList()'
	 */
	public void testAddGenerationViewerList() {
		TextViewer v = new TextViewer();
		assertTrue(gabase_.addGenerationViewer(v));
		//重複登録は出来ない．
		assertFalse(gabase_.addGenerationViewer(v));
		assertEquals("[TextViewer]",gabase_.getGenerationViewerNameList().toString());
		assertEquals("[]",gabase_.getOperationViewerNameList().toString());		
	}

	/*
	 * Test method for 'gabuilder.GABase.addOperationViewerList()'
	 */
	public void testAddOperationViewerList() {
		TextViewer v = new TextViewer();
		assertTrue(gabase_.addOperationViewer(v));
		//重複登録は出来ない．
		assertFalse(gabase_.addOperationViewer(v));
		assertEquals("[TextViewer]",gabase_.getOperationViewerNameList().toString());
		assertEquals("[]",gabase_.getGenerationViewerNameList().toString());
	}

	/*
	 * Test method for 'gabuilder.GABase.getGenerationSize()'
	 * Test method for 'gabuilder.GABase.setGenerationSize(long)'
	 */
	public void testGetAndSetGenerationSize() {
		assertEquals(GAParameters.DEFAULT_GENERATION_SIZE,gabase_.getGenerationSize());
		gabase_.setGenerationSize(77);
		assertEquals(77,gabase_.getGenerationSize());
	}
	
	/*
	 * Test method for 'gabuilder.GABase.swapOperatorPosition()'
	 */
	public void testSwapOperatorPosition() {
		gabase_.getOperatorList().add(new OnePointCrossover());
		gabase_.getOperatorList().add(new BitMutation());
		gabase_.getOperatorList().add(new TournamentSelection());
		gabase_.getOperatorList().add(new Elitism());
		gabase_.swapOperatorPosition(0, 1);
		gabase_.swapOperatorPosition(2, 3);
		String[] result = gabase_.getOperatorInfo().split("\\s");
		//0 と 1 の位置が入れ替わっている．		
		assertEquals("BitMutation{MUTATION_RATE:0.01}",result[0]);
		assertEquals("OnePointCrossover{CROSSOVER_RATE:0.6}",result[1]);
		//2 と 3 の位置が入れ替わっている．
		assertEquals("Elitism{IS_ELITISM:true}",result[2]);		
		assertEquals("TournamentSelection{TOURNAMENT_SIZE:3}",result[3]);
		
		
	}
	
}
