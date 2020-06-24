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

import java.util.HashMap;

import population.Individual;
import population.Population;
import population.PopulationAnalyzer;
import problem.BitCountProblem;
import problem.IProblem;
import problem.function.PowerFunction;
import util.MyRandom;
import fitness.FitnessManager;
/**
 * Population クラスの使用例.
 * @author mori
 * @version 1.0
 */

public class PopulationSample {
	public static void main(String[] args) {
		// シード固定
		MyRandom.setSeed(0);
		// 遺伝子長 3 の 4 個体からなる個体群を作成
		Population pop = new Population(4, 3);
		// ビットカウント問題を作成
		IProblem problem = new BitCountProblem();
		// 2乗するようにべき乗変換を設定
		problem.addFunction(new PowerFunction(2));
		// 問題を設定
		FitnessManager.setProblem(problem);
		for (Individual individual : pop.getIndivList()) {
			// 全個体の遺伝子型と適応度を表示
			System.out.println(individual + " " + individual.fitness());
		}
		// 適応度情報表示
		HashMap result = PopulationAnalyzer.fitnessInfo(pop);
		System.out.println("Fitness info max:" + result.get("max") 
				+ " mean:" + result.get("mean") + " min:" + result.get("min"));
		// 遺伝子型の種類表示
		System.out.println("genotype num:" + PopulationAnalyzer.genotypeNum(pop));
		// エントロピー表示
		System.out.println("entropy:" + PopulationAnalyzer.entropy(pop));
	}

}
