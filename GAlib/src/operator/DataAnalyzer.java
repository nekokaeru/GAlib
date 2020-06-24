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

package operator;

import java.util.Arrays;

import population.Individual;
import population.Population;
/**
 * 個体群のデータ解析用演算子クラス.
 * @author mori
 * @version 1.0
 */
public class DataAnalyzer implements IOperator {

	/**
	 * ログレベル 初期値は0
	 */
	private int logLevel_ = 0;

	/**
	 * 可変長引数を利用した配列で初期化． パラメータ数は 1 で第一要素がログレベル．
	 * @param params パラメータの配列
	 */
	public void setParameter(Object... params) {
		try {
			// パラメータの要素数が正しくないとき
			if (params.length != 1) {
				throw new Exception("params length must be 1!");
			}
			// 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) { // 数値クラスの場合
				int logLevel = ((Number) params[0]).intValue(); // 数値化
				setLogLevel(logLevel); // ログレベル設定
			} else { // 文字列の場合
				int logLevel = Integer.parseInt(params[0].toString()); // 数値化
				setLogLevel(logLevel); // ログレベル設定
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * 個体群に適用．
	 * @param pop 個体群
	 */
	public void apply(Population pop) {
		switch (logLevel_) {
		case 0:
			break;
		case 1:
			printMaxAveMin(pop);
			break;
		case 2:
			printAll(pop);
			break;
		default:
		}
	}

	/**
	 * 個体群中の個体をすべて表示する.
	 * @param pop 個体群
	 */
	private void printAll(Population pop) {
		System.out.println(pop);
	}

	/**
	 * 個体群の適応度の最大値および平均, 最小値を表示する.
	 * @param pop 個体群
	 */
	private void printMaxAveMin(Population pop) {
		double f, sum = 0, min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
		for (Individual indiv : pop.getIndivList()) {
			// DataAnalyzer によって適応度評価関数が変わらないように isChanged フラグを保存．
			boolean b = indiv.isChanged();
			f = indiv.fitness();
			// 元の状態に戻す．
			indiv.setChanged(b);
			if (f < min) {
				min = f;
			}
			if (f > max) {
				max = f;
			}
			sum += f;
		}
		sum /= pop.getPopulationSize();
		System.out.println("max:" + max + " ave:" + sum + " min:" + min);
	}

	/**
	 * 個体群の最大適応度を返す.
	 * @param pop 個体群
	 * @return 最大適応度
	 */
	public double getMaxFitness(Population pop) {
		double f, max = Double.NEGATIVE_INFINITY;
		for (Individual indiv : pop.getIndivList()) {
			// DataAnalyzer によって適応度評価関数が変わらないように isChanged フラグを保存．
			boolean b = indiv.isChanged();
			f = indiv.fitness();
			// 元の状態に戻す．
			indiv.setChanged(b);
			if (f > max) {
				max = f;
			}
		}
		return max;
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "DataAnalyzer";
	}

	/**
	 * ログレベルを返す.
	 * @return ログレベル
	 */
	public int getLogLevel() {
		return logLevel_;
	}

	/**
	 * ログレベルを設定.
	 * @param logLevel ログレベル
	 */
	public void setLogLevel(int logLevel) {
		logLevel_ = logLevel;
	}

	/**
	 * パラメータの情報を返す．例：LOG_LEVEL:0
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "LOG_LEVEL:" + getLogLevel();
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

}
