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

package viewer;

import fitness.FitnessManager;
import gabuilder.GABase;
import gabuilder.GAParameters;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import population.Individual;
import population.Population;
import population.PopulationAnalyzer;
import util.MyRandom;

/**
 * 文字表示によるビューワクラス.
 * @author mori
 * @version 1.0
 */

public class TextViewer implements IViewer {
	/**
	 * GABase への参照． MVC のモデル．
	 */
	private GABase ga_;

	/**
	 * 表示レベル
	 */
	private int printLevel_ = GAParameters.DEFAULT_PRINT_LEVEL;

	/**
	 * 出力ストリーム
	 */
	private PrintStream out_;

	/**
	 * 出力ファイル名．空文字列の場合は標準出力に表示.
	 */
	private String filename_ = "";

	/**
	 * 開始時に一度だけ表示する内容を記述．
	 */
	public void begin() {
		// printLevel_ < 0 の場合何も表示しない．
		if (printLevel_ < 0) {
			return;
		}
		getOut().println(new Date());
		getOut().println("Problem: " + FitnessManager.getProblem().toString());
		getOut().println(
				"seed:" + MyRandom.getSeed() + " turbo:"
						+ FitnessManager.isTurbo() + " memory:"
						+ FitnessManager.isMemory());
		getOut().print("Operators:\n" + getGA().getOperatorInfo());
		getOut().println(
				"population size:"
						+ getGA().getPopulation().getPopulationSize()
						+ " chromosome length:"
						+ getGA().getPopulation().getIndividualAt(0).size());
		// printLevel_ ==3 の場合は初期個体群も表示する．
		if (printLevel_ == 3) {
			printPopulation(getGA().getPopulation());
			// printLevel_ >=4 の場合は初期個体群+適応度を表示する．
		} else if (printLevel_ >= 4) {
			for (Individual indiv : getGA().getPopulation().getIndivList()) {
				getOut().println("fitness:" + indiv.fitness() + " " + indiv);
			}
		}

		getOut().println("---------begin---------");
	}

	/**
	 * 終了時に一度だけ行われる処理を記述．
	 */
	public void end() {
		// printLevel_ < 0 の場合何も表示しない．
		if (printLevel_ < 0) {
			return;
		}
		getOut().println("----------end----------");
		getOut().println(new Date());
		getOut().println(
				"Total fitness evaluation:" + FitnessManager.getTotalEvalNum());
		// printLevel_ ==3 の場合は最終個体群も表示する．
		if (printLevel_ == 3) {
			printPopulation(getGA().getPopulation());
			// printLevel_ >=4 の場合は最終個体群+適応度も表示する．
		}
		if (printLevel_ >= 4) {
			for (Individual indiv : getGA().getPopulation().getIndivList()) {
				getOut().println("fitness:" + indiv.fitness() + " " + indiv);
			}
		}
		printElite();
		// ストーリムの後処理
		if (filename_.equals("")) {
			// 標準出力の場合は flush で終了
			getOut().flush();
		} else {
			// ファイル出力の場合は close で終了
			getOut().close();
		}
	}

	/**
	 * 表示の更新
	 * @param info 付加情報
	 */
	public void update(String info) {
		HashMap<String, Double> fitInfo = null;
		// printLevel_ <= 0 の場合何も表示しない．
		if (printLevel_ <= 0) {
			return;
		}
		if (printLevel_ >= 1) {
			// begin: printLevel_ >= 1 の場合に表示する部分．
			fitInfo = PopulationAnalyzer.fitnessInfo(getGA().getPopulation());
			getOut().println(info + "maxFitness:" + fitInfo.get("max"));
		}
		if (printLevel_ >= 2) {
			getOut().println(
					"meanFitness:"
							+ fitInfo.get("mean")
							+ " minFitness:"
							+ fitInfo.get("min")
							+ " entropy:"
							+ PopulationAnalyzer.entropy(getGA()
									.getPopulation())
							+ " genotypeNum:"
							+ PopulationAnalyzer.genotypeNum(getGA()
									.getPopulation()) + " fitness evalNum:"
							+ FitnessManager.getTotalEvalNum());

		}
		// printLevel_ ==3 の場合は初期個体群も表示する．
		if (printLevel_ == 3) {
			printPopulation(getGA().getPopulation());
			// printLevel_ >=4 の場合は初期個体群+適応度も表示する．
		}
		if (printLevel_ >= 4) {
			for (Individual indiv : getGA().getPopulation().getIndivList()) {
				getOut().println("fitness:" + indiv.fitness() + " " + indiv);
			}
		}
		return;
	}

	/**
	 * 個体群を表示．
	 * @param pop 個体群
	 */
	public void printPopulation(Population pop) {
		getOut().print(pop);
	}

	/**
	 * エリート個体を表示.
	 */
	public void printElite() {
		ArrayList<Individual> list = FitnessManager.getCopyOfEliteList();
		getOut().println("----elite----");
		getOut().println("elite fitness: " + FitnessManager.getEliteFitness());
		for (Individual individual : list) {
			getOut().println(individual);
		}
		getOut().println("-------------");
	}

	/**
	 * 出力ストリームを返す.
	 * @return 出力ストリーム
	 */
	public PrintStream getOut() {
		if (out_ == null) {
			if (filename_.equals("") || filename_.equalsIgnoreCase("STDOUT")) {
				// 標準出力を返す．
				out_ = System.out;
			} else if (filename_.equalsIgnoreCase("STDERR")) {
				// 標準エラー出力を返す．
				out_ = System.err;

			} else {
				try {
					out_ = new PrintStream(new File(getFilename()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return out_;
	}

	/**
	 * 出力ストリームを設定.
	 * @param out 出力ストリーム
	 */
	public void setOut(PrintStream out) {
		out_ = out;
	}

	/**
	 * 出力ファイル名を返す.
	 * @return 出力ファイル名
	 */
	public String getFilename() {
		return filename_;
	}

	/**
	 * 出力ファイル名を設定.
	 * @param filename 出力ファイル名
	 */
	public void setFilename(String filename) {
		filename_ = filename;
	}

	/**
	 * GABase への参照を返す.
	 * @return GABase への参照
	 */
	public GABase getGA() {
		return ga_;
	}

	/**
	 * GABase への参照の設定.
	 * @param ga GAへの参照
	 */
	public void setGA(GABase ga) {
		ga_ = ga;
	}

	/**
	 * 名前を返す．
	 * @see viewer.IViewer#getName()
	 */
	public String getName() {
		return "TextViewer";
	}

	/**
	 * 表示レベルを返す.
	 * @return 表示レベル
	 */
	public int getPrintLevel() {
		return printLevel_;
	}

	/**
	 * 表示レベルの設定.
	 * @param printLevel 表示レベル
	 */
	public void setPrintLevel(int printLevel) {
		printLevel_ = printLevel;
	}
}
