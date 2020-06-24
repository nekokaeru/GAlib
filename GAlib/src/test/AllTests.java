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

/**
 * 
 */
package test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author mori
 * @version 1.0
 */
public class AllTests {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(AllTests.suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for test");
		//$JUnit-BEGIN$
		suite.addTestSuite(ThermoDynamicalSelectionTest.class);
		suite.addTestSuite(MergeTest.class);
		suite.addTestSuite(GABaseTest.class);
		suite.addTestSuite(BitMutationTest.class);
		suite.addTestSuite(FitnessManagerTest.class);
		suite.addTestSuite(TournamentSelectionTest.class);
		suite.addTestSuite(FitnessMemoryBankTest.class);
		suite.addTestSuite(BitCountProblemTest.class);
		suite.addTestSuite(IndividualTest.class);
		suite.addTestSuite(DefaultGABuilderTest.class);
		suite.addTestSuite(TextViewerTest.class);
		suite.addTestSuite(PopulationAnalyzerTest.class);
		suite.addTestSuite(EvaluateFitnessTest.class);
		suite.addTestSuite(LinearScalingTransformTest.class);
		suite.addTestSuite(RouletteSelectionTest.class);
		suite.addTestSuite(ProblemFactoryTest.class);
		suite.addTestSuite(PowerFunctionTest.class);
		suite.addTestSuite(OnePointCrossoverTest.class);
		suite.addTestSuite(TDGABuilderTest.class);
		suite.addTestSuite(KnapsackProblemTest.class);
		suite.addTestSuite(FunctionFactoryTest.class);
		suite.addTestSuite(LinearFunctionTest.class);
		suite.addTestSuite(SigmoidFunctionTest.class);
		suite.addTestSuite(MyRandomTest.class);
		suite.addTestSuite(OperatorFactoryTest.class);
		suite.addTestSuite(RankTransformTest.class);
		suite.addTestSuite(DirectorTest.class);
		suite.addTestSuite(PopulationTest.class);
		suite.addTestSuite(NkProblemTest.class);
		suite.addTestSuite(GAParametersTest.class);
		suite.addTestSuite(ElitismTest.class);
		suite.addTestSuite(BinaryNumberProblemTest.class);
		suite.addTestSuite(UniformCrossoverTest.class);
		suite.addTestSuite(ViewerFactoryTest.class);
		suite.addTestSuite(NegativeFunctionTest.class);
		//$JUnit-END$
		return suite;
	}

}
