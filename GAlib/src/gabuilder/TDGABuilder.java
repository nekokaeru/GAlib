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
import java.util.HashMap;

import operator.AbstractCrossover;
import operator.AbstractMutation;
import operator.AbstractSelection;
import operator.Dummy;
import operator.IOperator;
import operator.Merge;
import operator.ThermoDynamicalSelection;
import operator.transform.IArrayTransform;
import operator.transform.TransformFactory;
import util.LocalTestException;
import util.MyRandom;
import viewer.TextViewer;
/**
 * TDGA を作成する GA ビルダ．
 * @author mori
 * @version 1.0
 */
public class TDGABuilder extends DefaultGABuilder {
	/**
	 * 生成したTDGAを返す.
	 * @return GABase
	 */
	@Override
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
		Merge merge = new Merge();
		merge.getOperators().add(new Dummy());
		merge.getOperators().add(makeCrossover(map));
		list.add(merge);
		list.add(makeMutation(map));
		list.add(makeSelection(map));
		// エリート保存は設定する必要なし．
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
	 * 適切にパラメータが設定された選択を作って返す．スケーリング関数も設定される．
	 * @param map パラメータ保存用ハッシュマップ
	 * @return selection
	 */
	@Override
	protected AbstractSelection makeSelection(HashMap<String, String> map) {
		// 選択設定．TDGA は必ず熱力学的選択．
		AbstractSelection selection = new ThermoDynamicalSelection();
		// TDGA では個体数と遺伝子長を明示的に設定する必要がある．
		ArrayList<String> paramsList = new ArrayList<String>();
		paramsList.add(map.get(GAParameters.POPULATION_SIZE));
		paramsList.add(map.get(GAParameters.CHROMOSOME_LENGTH));
		// S_PARAM による選択パラメータ設定. S_PARAM は温度のみの場合と温度と個体群圧縮フラグの場合がある．
		// 圧縮フラグは省略可．例: -Sparam 3 (温度 3，フラグ false) -Sparam 3:true (温度 3，フラグ true) 
		if (map.get(GAParameters.S_PARAM) != null) {
			for (String object : map.get(GAParameters.S_PARAM).split(":")) {
				paramsList.add(object);
			}
		}
		// 作成したリストを配列化して渡す．
		selection.setParameter(paramsList.toArray());
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
	 * 実行例
	 */
	public static void main(String[] args) {
		long t1, t2;
		t1 = System.currentTimeMillis();
		TDGABuilder builder = new TDGABuilder();
		Director d = new Director(builder);
		d.setParameter(args);
		d.constract();
		GABase ga = builder.buildGA();
		ga.addOperationViewer(new TextViewer());
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
	@Override
	public void localTest() throws LocalTestException {
		GAParameters params = new GAParameters();
		HashMap<String, String> map = params.getParametersMap();
		ga_ = new GABase();

		// setSeed のテスト
		MyRandom.reset();
		map.put(GAParameters.SEED, "-1");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != -1) {
			throw new LocalTestException("seed != -1");
		}
		// シードは getInstance() するまでは変更可
		map.put(GAParameters.SEED, "100");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 100) {
			throw new LocalTestException("seed != 100");
		}
		// シードが100で確定．
		MyRandom.getInstance();
		map.put(GAParameters.SEED, "100");
		setSeed(map);
		if (MyRandom.getSeed().longValue() != 100) {
			throw new LocalTestException("seed != 100");
		}
		// setProblem のテスト. デフォルトのビットカウントが呼ばれる．
		setProblem(map);
		if (!FitnessManager.getProblem().toString().equals("BitCount{}")) {
			throw new LocalTestException(FitnessManager.getProblem().toString()
					+ " is wrong!");
		}
		// setPopulation のテスト
		map.put(GAParameters.POPULATION_SIZE, "14"); // 個体数14
		map.put(GAParameters.CHROMOSOME_LENGTH, "12"); // 遺伝子長12
		setPopulation(map);
		// 個体数
		if (ga_.getPopulation().getPopulationSize() != 14) {
			throw new LocalTestException(ga_.getPopulation()
					.getPopulationSize()
					+ "!= 14");
		}
		// 遺伝子長
		if (ga_.getPopulation().getIndividualAt(0).size() != 12) {
			throw new LocalTestException(ga_.getPopulation().getIndividualAt(0)
					.size()
					+ "!= 12");
		}

		// makeCrossover のテスト
		map.put(GAParameters.CROSSOVER, "operator.OnePointCrossover"); // 一様交叉
		map.put(GAParameters.C_PARAM, "0.1"); // 交叉率0.1
		AbstractCrossover crossover = makeCrossover(map);
		if (!crossover.toString().equals(
				"OnePointCrossover{CROSSOVER_RATE:0.1}")) {
			throw new LocalTestException(crossover.toString() + " is wrong!");
		}

		// makeMutation のテスト
		map.put(GAParameters.MUTATION, "operator.BitMutation"); // ビット突然変異
		map.put(GAParameters.M_PARAM, "0.89"); // 突然変異率0.98
		AbstractMutation mutation = makeMutation(map);
		if (!mutation.toString().equals("BitMutation{MUTATION_RATE:0.89}")) {
			throw new LocalTestException(mutation.toString() + " is wrong!");
		}

		// makeSelection のテスト. 必ず熱力学的選択が設定される．
		map.put(GAParameters.SELECTION, "operator.TournamentSelection"); // トーナメント選択
		map.put(GAParameters.S_PARAM, "0.01"); // 温度
		AbstractSelection selection = makeSelection(map);
		if (!selection.toString().equals(
				"ThermoDynamicalSelection{TEMPERATURE:0.01,isPackIndivList:false}")) {
			throw new LocalTestException(selection.toString() + " is wrong!");
		}
		
		map.put(GAParameters.SELECTION, "operator.TournamentSelection"); // トーナメント選択
		map.put(GAParameters.S_PARAM, "0.1:true"); // 温度と個体圧縮フラグ
		selection = makeSelection(map);
		if (!selection.toString().equals(
				"ThermoDynamicalSelection{TEMPERATURE:0.1,isPackIndivList:true}")) {
			throw new LocalTestException(selection.toString() + " is wrong!");
		}
		

		// setGenerationSize のテスト
		map.put(GAParameters.GENERATION_SIZE, "111"); // 世代数111
		setGenerationSize(map);
		if (ga_.getGenerationSize() != 111) {
			throw new LocalTestException(ga_.getGenerationSize() + "!=111");
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
		map.put(GAParameters.OUTPUT_FILE, "stdout"); // 標準出力		
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.out) {
			throw new LocalTestException("v.getOut() != System.out");
		}
		if (!v.getFilename().equals("STDOUT")) {
			throw new LocalTestException(v.getFilename() + "!= STDOUT");
		}
		map.put(GAParameters.OUTPUT_FILE, "sTDERR"); // 標準エラー出力		
		v = (TextViewer) makeViewer(map);
		if (v.getOut() != System.err) {
			throw new LocalTestException("v.getOut() != System.err");
		}
		if (!v.getFilename().equals("STDERR")) {
			throw new LocalTestException(v.getFilename() + "!= STDERR");
		}		

		map.put(GAParameters.OUTPUT_FILE, "__test2__.dat");
		map.put(GAParameters.PRINT_LEVEL, "-1");
		v = (TextViewer) makeViewer(map);
		if (v.getPrintLevel() != -1) {
			throw new LocalTestException(v.getPrintLevel() + "!=-1");
		}
		if (!v.getFilename().equals("__test2__.dat")) {
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
		if (!buildGA().getOperatorNameList().toString().equals(
				"[Merge, BitMutation, ThermoDynamicalSelection]")) {
			throw new LocalTestException(ga_.getOperatorNameList()
					+ "is wrong!");
		}

		if (!ga_.getOperatorList().get(0).toString().equals(
				"Merge{Dummy{},OnePointCrossover{CROSSOVER_RATE:0.1}}")) {
			throw new LocalTestException(ga_.getOperatorList().get(0)
					+ "is wrong!");
		}
	}
}