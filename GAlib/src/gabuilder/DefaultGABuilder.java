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

package gabuilder;

import fitness.FitnessManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import operator.AbstractCrossover;
import operator.AbstractMutation;
import operator.AbstractSelection;
import operator.Elitism;
import operator.IOperator;
import operator.OperatorFactory;
import operator.transform.IArrayTransform;
import operator.transform.TransformFactory;
import population.Population;
import problem.IProblem;
import problem.ProblemFactory;
import problem.function.FunctionFactory;
import problem.function.IFunction;
import util.LocalTestException;
import util.MyRandom;
import viewer.IViewer;
import viewer.TextViewer;
import viewer.ViewerFactory;
/**
 * デフォルトの GA ビルダ．SGA を作成する．
 * @author mori
 * @version 1.0
 */
public class DefaultGABuilder implements IGABuilder {
	/**
	 * 作成する GABase
	 */
	protected GABase ga_;

	/**
	 * GAパラメータ
	 */
	protected GAParameters params_;

	/**
	 * 生成したGAを返す.
	 * @return GABase
	 */
	public GABase buildGA() {
		HashMap<String, String> map = params_.getParametersMap();
		// シードの設定は，最優先でする．
		setSeed(map);
		ga_ = new GABase();
		// 問題の設定
		setProblem(map);
		// 個体群の設定
		setPopulation(map);
		// 遺伝演算子の設定
		ArrayList<IOperator> list = new ArrayList<IOperator>();
		list.add(makeCrossover(map));
		list.add(makeMutation(map));
		list.add(makeSelection(map));
		// エリート保存フラグが設定されていればエリート保存追加．
		if (Boolean.valueOf(map.get(GAParameters.IS_ELITISM))) {
			list.add(new Elitism());
		}
		ga_.setOperatorList(list);
		// 世代数の設定
		setGenerationSize(map);
		// Viewer の設定．
		ga_.addGenerationViewer(makeViewer(map));
		// 高速化フラグと個体メモリフラグの設定
		setTurboAndMemoryFlag(map);
		return ga_;
	}

	/**
	 * シードの設定．
	 * @param map パラメータ保存用ハッシュマップ
	 * @return シードが設定できたか？
	 */
	protected boolean setSeed(HashMap<String, String> map) {
		boolean result = false;
		if (map.get(GAParameters.SEED) != null) {
			long seed = Long.parseLong(map.get(GAParameters.SEED));
			result = MyRandom.setSeed(seed);
		}
		return result;
	}

	/**
	 * 問題設定
	 * @param map パラメータ保存用ハッシュマップ
	 */
	protected void setProblem(HashMap<String, String> map) {
		IProblem problem = ProblemFactory.createProblem(map
				.get(GAParameters.PROBLEM));
		if (map.get(GAParameters.PROBLEM_PARAMETER) != null) {
			// 複数のパラメータは : で仕切られる．
			Object[] params = map.get(GAParameters.PROBLEM_PARAMETER)
					.split(":");
			problem.setParameter(params);
		}
		// Function の設定．最小化問題→最大化問題などの変換をする．
		if (map.get(GAParameters.FUNCTION) != null) {
			String functionName = map.get(GAParameters.FUNCTION);
			// 問題で用いる関数を作る．
			IFunction function = FunctionFactory.createFunction(functionName);
			// Function 用のパラメータ設定
			if (map.get(GAParameters.FUNCTION_PARAMETER) != null) {
				// 複数のパラメータは : で仕切られる．
				Object[] params = map.get(GAParameters.FUNCTION_PARAMETER)
						.split(":");
				function.setParameter(params);
			}
			// 問題に function を追加
			problem.addFunction(function);
		}
		FitnessManager.setProblem(problem);
	}

	/**
	 * 個体群の設定
	 * @param map パラメータ保存用ハッシュマップ
	 */
	protected void setPopulation(HashMap<String, String> map) {
		int popSize = Integer.parseInt(map.get(GAParameters.POPULATION_SIZE));
		int chromosomeLength = Integer.parseInt(map
				.get(GAParameters.CHROMOSOME_LENGTH));
		Population pop = new Population(popSize, chromosomeLength);
		ga_.setPopulation(pop);
	}

	/**
	 * 適切にパラメータが設定された交叉を作って返す．
	 * @param map パラメータ保存用ハッシュマップ
	 * @return crossover
	 */
	protected AbstractCrossover makeCrossover(HashMap<String, String> map) {
		// 交叉設定． 交叉以外が設定されており，キャストに失敗すると例外発生．
		AbstractCrossover crossover = (AbstractCrossover) OperatorFactory
				.createOperator(map.get(GAParameters.CROSSOVER));
		// GAParameters.C_PARAM により交叉パラメータ設定
		if (map.get(GAParameters.C_PARAM) != null) {
			Object[] params = map.get(GAParameters.C_PARAM).split(":");
			crossover.setParameter(params);
		}
		return crossover;
	}

	/**
	 * 適切にパラメータが設定された突然変異を作って返す．
	 * @param map パラメータ保存用ハッシュマップ
	 * @return mutation
	 */
	protected AbstractMutation makeMutation(HashMap<String, String> map) {
		// 突然変異設定．突然変異以外が設定されており，キャストに失敗すると例外発生．
		AbstractMutation mutation = (AbstractMutation) OperatorFactory
				.createOperator(map.get(GAParameters.MUTATION));
		// GAParameters.M_PARAM により突然変異パラメータ設定
		if (map.get(GAParameters.M_PARAM) != null) {
			Object[] params = map.get(GAParameters.M_PARAM).split(":");
			mutation.setParameter(params);
		}
		return mutation;
	}

	/**
	 * 適切にパラメータが設定された選択を作って返す．スケーリング関数も設定される．
	 * @param map パラメータ保存用ハッシュマップ
	 * @return selection
	 */

	protected AbstractSelection makeSelection(HashMap<String, String> map) {
		// 選択設定．選択以外が設定されており，キャストに失敗すると例外発生．
		AbstractSelection selection = (AbstractSelection) OperatorFactory
				.createOperator(map.get(GAParameters.SELECTION));
		// DefaultGABuilder でも熱力学的選択を使うことを可能とするための処理
		if(selection.getName().equals("ThermoDynamicalSelection")){
			ArrayList<String> paramsList = new ArrayList<String>();
			// ThermoDynamicalSelection では個体数と遺伝子長を必ず指定する必要有り．
			paramsList.add(map.get(GAParameters.POPULATION_SIZE));
			paramsList.add(map.get(GAParameters.CHROMOSOME_LENGTH));
			// S_PARAM による選択パラメータ設定. S_PARAM は温度のみの場合と温度と個体群圧縮フラグの場合がある．
			// 圧縮フラグは省略可．例: -Sparam 3 (温度 3，フラグ false) -Sparam 3:true (温度 3，フラグ true) 
			if (map.get(GAParameters.S_PARAM) != null) {
				for (String object : map.get(GAParameters.S_PARAM).split(":")) {
					paramsList.add(object);
				}				
			}
			selection.setParameter(paramsList.toArray());
		}else{ // 通常の選択．
			// S_PARAM により選択パラメータ設定
			if (map.get(GAParameters.S_PARAM) != null) {
				Object[] params = map.get(GAParameters.S_PARAM).split(":");
				selection.setParameter(params);
			}
		}
		
		// 選択時のスケーリングの設定
		if (map.get(GAParameters.TRANSFORM) != null) {
			String transformName = map.get(GAParameters.TRANSFORM);
			// 選択で用いるスケーリングを作る．
			IArrayTransform transform = TransformFactory
					.createTransform(transformName);
			// IArrayTransform 用のパラメータ設定
			if (map.get(GAParameters.TRANSFORM_PARAMETER) != null) {
				// 複数のパラメータは : で仕切られる．
				Object[] params = map.get(GAParameters.TRANSFORM_PARAMETER)
						.split(":");
				transform.setParameter(params);
			}
			// 選択に transform を追加
			selection.addTransform(transform);
		}
		return selection;
	}

	/**
	 * 世代数設定
	 * @param map パラメータ保存用ハッシュマップ
	 */
	protected void setGenerationSize(HashMap<String, String> map) {
		int gsize = Integer.parseInt(map.get(GAParameters.GENERATION_SIZE));
		ga_.setGenerationSize(gsize);
	}

	/**
	 * ビューワの設定
	 * @param map パラメータ保存用ハッシュマップ
	 */
	protected IViewer makeViewer(HashMap<String, String> map) {
		IViewer v = ViewerFactory.createViewer(map.get(GAParameters.VIEWER));
		int printLevel = Integer.parseInt(map.get(GAParameters.PRINT_LEVEL));
		v.setPrintLevel(printLevel);
		if (map.get(GAParameters.OUTPUT_FILE).equalsIgnoreCase("stdout")) {
			v.setFilename("STDOUT");
		} else if (map.get(GAParameters.OUTPUT_FILE).equalsIgnoreCase("stderr")) {
			v.setFilename("STDERR");
		} else {
			v.setFilename(map.get(GAParameters.OUTPUT_FILE));
		}
		return v;
	}

	/**
	 * 高速化フラグと個体メモリフラグの設定
	 * @param map パラメータ保存用ハッシュマップ
	 */
	protected void setTurboAndMemoryFlag(HashMap<String, String> map) {
		boolean isTurbo = Boolean.parseBoolean(map.get(GAParameters.IS_TURBO));
		boolean isMemory = Boolean
				.parseBoolean(map.get(GAParameters.IS_MEMORY));
		FitnessManager.setTurbo(isTurbo);
		FitnessManager.setMemory(isMemory);
	}

	/**
	 * GAパラメータを設定．シードの設定も行う．
	 * @param params GAパラメータ
	 */
	public void setGAParameters(GAParameters params) {
		params_ = params;
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		long t1, t2;
		t1 = System.currentTimeMillis();
		DefaultGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		d.setParameter(args);
		d.constract();
		GABase ga = builder.buildGA();
		ga.start();
		t2 = System.currentTimeMillis();
		System.out.println("time:" + (t2 - t1));
		try {
			builder.localTest();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ローカルテスト
	 * @throws LocalTestException
	 */
	public void localTest() throws LocalTestException {
		GAParameters params = new GAParameters();
		HashMap<String, String> map = params.getParametersMap();
		ga_ = new GABase();

		// setSeed のテスト．
		MyRandom.reset();
		map.put(GAParameters.SEED, "3");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 3) {
			throw new LocalTestException("seed != 3");
		}
		// シードは getInstance() するまでは変更可．
		map.put(GAParameters.SEED, "8");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 8) {
			throw new LocalTestException("seed != 8");
		}
		// シードが8で確定．
		MyRandom.getInstance();
		map.put(GAParameters.SEED, "4");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 8) {
			throw new LocalTestException("seed != 8");
		}
		// setProblem のテスト
		map.put(GAParameters.PROBLEM, "problem.KnapsackProblem");
		map.put(GAParameters.PROBLEM_PARAMETER, "100");
		map.put(GAParameters.FUNCTION, "problem.function.LinearFunction");
		map.put(GAParameters.FUNCTION_PARAMETER, "-2:1.23");
		setProblem(map);
		if (!FitnessManager.getProblem().toString().equals(
				"Knapsack{itemNum:100,Linear{GRADIENT:-2.0,INTERCEPT:1.23}}")) {
			throw new LocalTestException(FitnessManager.getProblem().toString()
					+ " is wrong!");
		}
		Number[] indiv = new Number[100];
		Arrays.fill(indiv, 0);
		indiv[0] = 1;
		// 100荷物ナップサック問題の始めの荷物の価値は68
		if (FitnessManager.getProblem().getObjectiveFunctionValue(indiv) != 68) {
			throw new LocalTestException(FitnessManager.getProblem()
					.getObjectiveFunctionValue(indiv)
					+ "!=68");
		}
		// 線形スケーリング後の値
		if (FitnessManager.getProblem().getFitness(indiv) != 68 * (-2.0) + 1.23) {
			throw new LocalTestException(FitnessManager.getProblem()
					.getFitness(indiv)
					+ "!=68*(-2.0)+1.23=-134.77");
		}

		// setPopulation のテスト
		map.put(GAParameters.POPULATION_SIZE, "13"); // 個体数13
		map.put(GAParameters.CHROMOSOME_LENGTH, "17"); // 遺伝子長17
		setPopulation(map);
		// 個体数
		if (ga_.getPopulation().getPopulationSize() != 13) {
			throw new LocalTestException(ga_.getPopulation()
					.getPopulationSize()
					+ "!= 13");
		}
		// 遺伝子長
		if (ga_.getPopulation().getIndividualAt(0).size() != 17) {
			throw new LocalTestException(ga_.getPopulation().getIndividualAt(0)
					.size()
					+ "!= 17");
		}

		// makeCrossover のテスト
		map.put(GAParameters.CROSSOVER, "operator.UniformCrossover"); // 一様交叉
		map.put(GAParameters.C_PARAM, "0.99"); // 交叉率0.99
		AbstractCrossover crossover = makeCrossover(map);
		if (!crossover.toString().equals(
				"UniformCrossover{CROSSOVER_RATE:0.99}")) {
			throw new LocalTestException(crossover.toString() + " is wrong!");
		}

		// makeMutation のテスト
		map.put(GAParameters.MUTATION, "operator.BitMutation"); // ビット突然変異
		map.put(GAParameters.M_PARAM, "0.98"); // 突然変異率0.98
		AbstractMutation mutation = makeMutation(map);
		if (!mutation.toString().equals("BitMutation{MUTATION_RATE:0.98}")) {
			throw new LocalTestException(mutation.toString() + " is wrong!");
		}

		// makeSelection のテスト
		map.put(GAParameters.SELECTION, "operator.TournamentSelection"); // トーナメント選択
		map.put(GAParameters.S_PARAM, "7"); // トーナメントサイズ 7
		map.put(GAParameters.TRANSFORM,
				"operator.transform.LinearScalingTransform"); // トーナメント選択
		map.put(GAParameters.TRANSFORM_PARAMETER, "2.1:-3.2"); // トーナメントサイズ 7
		AbstractSelection selection = makeSelection(map);
		if (!selection
				.toString()
				.equals(
						"TournamentSelection{TOURNAMENT_SIZE:7,LinearScalingTransform{TOP_VALUE:2.1, BOTTOM_VALUE:-3.2}}")) {
			throw new LocalTestException(selection.toString() + " is wrong!");
		}

		// setGenerationSize のテスト
		map.put(GAParameters.GENERATION_SIZE, "123"); // 世代数123
		setGenerationSize(map);
		if (ga_.getGenerationSize() != 123) {
			throw new LocalTestException(ga_.getGenerationSize() + "!=123");
		}
		// makeViewer のテスト
		map.put(GAParameters.OUTPUT_FILE, ""); // 空文字列は標準出力
		TextViewer v = (TextViewer) makeViewer(map);
		if (v.getPrintLevel() != GAParameters.DEFAULT_PRINT_LEVEL) {
			throw new LocalTestException(v.getPrintLevel() + "!="
					+ GAParameters.DEFAULT_PRINT_LEVEL);
		}
		if (v.getOut() != System.out) {
			throw new LocalTestException("v.getOut() != System.out");
		}
		if (!v.getFilename().equals("")) {
			throw new LocalTestException(v.getFilename() + "!= \"\"");
		}

		map.put(GAParameters.OUTPUT_FILE, "STDERR"); // 標準エラー出力
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.err) {
			throw new LocalTestException("v.getOut() != System.err");
		}
		if (!v.getFilename().equals("STDERR")) {
			throw new LocalTestException(v.getFilename() + "!= STDERR");
		}

		map.put(GAParameters.OUTPUT_FILE, "STDOUT"); // 標準出力
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.out) {
			throw new LocalTestException("v.getOut() != System.out");
		}
		if (!v.getFilename().equals("STDOUT")) {
			throw new LocalTestException(v.getFilename() + "!= STDOUT");
		}

		map.put(GAParameters.OUTPUT_FILE, "__test__.dat");
		map.put(GAParameters.PRINT_LEVEL, "3");
		v = (TextViewer) makeViewer(map);
		if (v.getPrintLevel() != 3) {
			throw new LocalTestException(v.getPrintLevel() + "!=3");
		}
		if (!v.getFilename().equals("__test__.dat")) {
			throw new LocalTestException(v.getFilename() + "!= __test__.dat");
		}
		// setTurboAndMemoryFlag のテスト
		map.put(GAParameters.IS_TURBO, "true");
		map.put(GAParameters.IS_MEMORY, "true");
		setTurboAndMemoryFlag(map);
		if (!FitnessManager.isTurbo()) {
			throw new LocalTestException("FitnessManager.isTurbo() != true");
		}
		if (!FitnessManager.isMemory()) {
			throw new LocalTestException("FitnessManager.isMemory() != true");
		}

		// getGA のテスト．localTest の必要はないが折角なので...
		setGAParameters(params);
		if (!buildGA()
				.getOperatorNameList()
				.toString()
				.equals(
						"[UniformCrossover, BitMutation, TournamentSelection, Elitism]")) {
			throw new LocalTestException(ga_.getOperatorNameList()
					+ "is wrong!");
		}
	}
}
