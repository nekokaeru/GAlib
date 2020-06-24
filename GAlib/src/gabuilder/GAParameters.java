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
 * Created on 2005/05/11
 * GABase パラメータ用ユーティリティクラス．
 */
package gabuilder;

import java.util.HashMap;
import java.util.HashSet;

/**
 * GABase パラメータ用クラス． 使用時は new すること．
 * @author mori
 * @version 1.0
 */
public class GAParameters {
	/** 個体数の初期値 */
	public static final int DEFAULT_POPULATION_SIZE = 100;

	/** 世代数の初期値 */
	public static final int DEFAULT_GENERATION_SIZE = 100;

	/** 遺伝子長の初期値 */
	public static final int DEFAULT_CHROMOSOME_LENGTH = 10;

	/** 突然変異率の初期値 */
	public static final double DEFAULT_MUTATION_RATE = 0.01;

	/** 交叉率の初期値 */
	public static final double DEFAULT_CROSSOVER_RATE = 0.6;

	/** トーナメントサイズの初期値 */
	public static final int DEFAULT_TOURNAMENT_SIZE = 3;

	/** TDGA温度の初期値 */
	public static final double DEFAULT_TEMPERATURE = 1;

	/** 問題の初期値 */
	public static final String DEFAULT_PROBLEM = "problem.BitCountProblem";

	/** 選択の初期値 */
	public static final String DEFAULT_SELECTION = "operator.RouletteSelection";

	/** 交叉の初期値 */
	public static final String DEFAULT_CROSSOVER = "operator.OnePointCrossover";

	/** 突然変異の初期値 */
	public static final String DEFAULT_MUTATION = "operator.BitMutation";

	/** ビューワの初期値 */
	public static final String DEFAULT_VIEWER = "viewer.TextViewer";

	/** 表示レベルの初期値 */
	public static final int DEFAULT_PRINT_LEVEL = 1;

	/** 高速化フラグの初期値 */
	public static final boolean DEFAULT_IS_TURBO = false;

	/** 個体メモリフラグの初期値 */
	public static final boolean DEFAULT_IS_MEMORY = false;

	/** シードの初期値 */
	public static final String DEFAULT_SEED = null;


	/** HashMap のキー:  選択 */
	public static final String SELECTION = "SELECTION";

	/** HashMap のキー:  交叉 */
	public static final String CROSSOVER = "CROSSOVER";

	/** HashMap のキー:  突然変異 */
	public static final String MUTATION = "MUTATION";

	/** HashMap のキー:  選択パラメータ */
	public static final String S_PARAM = "S_PARAM";

	/** HashMap のキー:  交叉パラメータ */
	public static final String C_PARAM = "C_PARAM";

	/** HashMap のキー:  突然変異パラメータ */
	public static final String M_PARAM = "M_PARAM";

	/** HashMap のキー:  個体数 */
	public static final String POPULATION_SIZE = "POPULATION_SIZE";

	/** HashMap のキー:  目的関数値で使う変換関数 */
	public static final String FUNCTION = "FUNCTION";

	/** HashMap のキー:  目的関数値で使う変換関数パラメータ */
	public static final String FUNCTION_PARAMETER = "FUNCTION_PARAMETER";

	/** HashMap のキー:  選択で使うスケーリング */
	public static final String TRANSFORM = "TRANSFORM";

	/** HashMap のキー:  選択で使うスケーリングパラメータ */
	public static final String TRANSFORM_PARAMETER = "TRANSFORM_PARAMETER";

	/** HashMap のキー:  問題 */
	public static final String PROBLEM = "PROBLEM";

	/** HashMap のキー:  問題パラメータ */
	public static final String PROBLEM_PARAMETER = "PROBLEM_PARAMETER";

	/** HashMap のキー:  世代数 */
	public static final String GENERATION_SIZE = "GENERATION_SIZE";

	/** HashMap のキー:  遺伝子長 */
	public static final String CHROMOSOME_LENGTH = "CHROMOSOME_LENGTH";

	/** HashMap のキー:  プリントレベル */
	public static final String PRINT_LEVEL = "PRINT_LEVEL";

	/** HashMap のキー:  出力ファイル名 */
	public static final String OUTPUT_FILE = "OUTPUT_FILE";

	/** HashMap のキー:  エリート主義の有無 */
	public static final String IS_ELITISM = "IS_ELITISM";

	/** HashMap のキー:  ビューワ */
	public static final String VIEWER = "VIEWER"; // viewer.TextViewer

	/** HashMap のキー:  高速化フラグ */
	public static final String IS_TURBO = "IS_TURBO";

	/** HashMap のキー:  個体メモリフラグ */
	public static final String IS_MEMORY = "IS_MEMORY";

	/** HashMap のキー:  乱数のシード */
	public static final String SEED = "SEED";

	/**
	 * パラメータ保存用ハッシュマップ 
	 */
	private HashMap<String, String> parametersMap_;

	/**
	 * 指定可能なオプション集合
	 */
	private HashSet<String> regalOption_;

	/**
	 * パラメータ保存用ハッシュマップを返す.
	 * @return パラメータ保存用のハッシュマップ
	 */
	public final HashMap<String, String> getParametersMap() {
		if (parametersMap_ == null) {
			// parametersMap_の初期化
			parametersMap_ = new HashMap<String, String>();
			parametersMap_.put(SEED, DEFAULT_SEED); // シード
			parametersMap_.put(SELECTION, DEFAULT_SELECTION);
			parametersMap_.put(CROSSOVER, DEFAULT_CROSSOVER);
			parametersMap_.put(MUTATION, DEFAULT_MUTATION);
			parametersMap_.put(POPULATION_SIZE, Integer
					.toString(DEFAULT_POPULATION_SIZE));
			parametersMap_.put(FUNCTION, null);// 目的関数で使う変換関数 IProblem で設定->problem.function
			parametersMap_.put(FUNCTION_PARAMETER, null);// 目的関数で使う変換のパラメータ
			parametersMap_.put(TRANSFORM, null);// 選択で使うスケーリング ->operator.transform
			parametersMap_.put(TRANSFORM_PARAMETER, null);// 選択で使うスケーリングのパラメータ
			parametersMap_.put(PROBLEM, DEFAULT_PROBLEM);// 問題
			parametersMap_.put(PROBLEM_PARAMETER, null);// 問題パラメータ
			parametersMap_.put(GENERATION_SIZE, Integer
					.toString(DEFAULT_GENERATION_SIZE));// 世代数
			parametersMap_.put(CHROMOSOME_LENGTH, Integer
					.toString(DEFAULT_CHROMOSOME_LENGTH));// 遺伝子長
			parametersMap_
					.put(PRINT_LEVEL, String.valueOf(DEFAULT_PRINT_LEVEL));// プリントレベル
			parametersMap_.put(OUTPUT_FILE, "STDOUT");// 出力ファイル. STDOUT は標準出力, STDERR は標準エラー出力
			parametersMap_.put(IS_ELITISM, "true");// エリート
			parametersMap_.put(VIEWER, DEFAULT_VIEWER);// ビューワ
			parametersMap_.put(IS_TURBO, String.valueOf(DEFAULT_IS_TURBO));// 高速化フラグ
			parametersMap_.put(IS_MEMORY, String.valueOf(DEFAULT_IS_MEMORY));// 個体メモリフラグ
		}
		return parametersMap_;
	}

	/**
	 * オプションを返す.
	 * @return オプション
	 */
	public final HashSet<String> getRegalOption() {
		if (regalOption_ == null) {
			regalOption_ = new HashSet<String>();
			// ヘルプ表示
			regalOption_.add("-h");
			regalOption_.add("-help");
			regalOption_.add("-S"); // 選択指定
			regalOption_.add("-C"); // 交叉指定
			regalOption_.add("-M"); // 突然変異指定
			regalOption_.add("-Sparam");// 選択パラメータ
			regalOption_.add("-Cparam");// 交叉パラメータ
			regalOption_.add("-Mparam");// 突然変異パラメータ
			regalOption_.add("-scaling1");// 目的関数で使う変換関数 -> problem.function
			regalOption_.add("-scaling1param");// 目的関数で使う変換関数パラメータ
			regalOption_.add("-scaling2");// 選択で使うスケーリング -> operator.transform
			regalOption_.add("-scaling2param");// 選択で使うスケーリングパラメータ
			regalOption_.add("-P");// 問題
			regalOption_.add("-Pparam");// 問題パラメータ
			regalOption_.add("-popsize");// 個体数
			regalOption_.add("-gsize");// 世代数
			regalOption_.add("-clength");// 遺伝子長
			regalOption_.add("-printlevel");// プリントレベル
			regalOption_.add("-output");// 出力ファイル
			regalOption_.add("-elitism");// エリート主義
			regalOption_.add("-viewer");// ビューワ
			regalOption_.add("-turbo");// 高速化フラグ
			regalOption_.add("-memory");// 個体メモリフラグ
			regalOption_.add("-seed");// シード
		}
		return regalOption_;
	}
}
