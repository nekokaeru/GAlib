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

package population;

import java.util.HashMap;
import java.util.HashSet;

/**
 * 個体群解析用ユーティリティクラス.
 * @author mori
 * @version 1.0
 */

public class PopulationAnalyzer {
	/**
	 * 適応度情報(最大適応度, 平均適応度, 最小適応度)を返す.
	 * @param pop 個体群
	 * @return result key:value -> "max":最大値, "mean":平均値 "min":最小値
	 */
	public static HashMap<String, Double> fitnessInfo(Population pop) {
		HashMap<String, Double> result = new HashMap<String, Double>();
		double f, mean = 0, min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
		for (Individual indiv : pop.getIndivList()) {
			// 適応度評価回数に影響を及ぼさないように適応度情報を取得．
			f = indiv.fitnessWithoutRecord();
			if (f < min) {
				min = f;
			}
			if (f > max) {
				max = f;
			}
			mean += f;
		}
		mean /= pop.getPopulationSize();
		result.put("max", max);
		result.put("mean", mean);
		result.put("min", min);
		return result;
	}

	/**
	 * 遺伝子型の数
	 * @param pop 個体群
	 */
	public static int genotypeNum(Population pop) {
		HashSet<Individual> geno = new HashSet<Individual>();
		for (Individual indiv : pop.getIndivList()) {
			geno.add(indiv);
		}
		return geno.size();
	}

	/**
	 * エントロピー
	 * @param pop 個体群
	 */
	public static double entropy(Population pop) {
		if (pop.getIndivList().size() == 0) {
			return 0.0;
		}

		double entr = 0.0;
		double x0 = 0.0, x1 = 0.0;
		double n = (double) pop.getIndivList().size();

		for (int i = 0; i < pop.getIndivList().get(0).getChromosome().length; i++) {
			x0 = 0.0;
			x1 = 0.0;

			for (Individual indiv : pop.getIndivList()) {
				if ((Integer) indiv.getGeneAt(i) == 0) {
					x0 += 1.0;
				} else {
					x1 += 1.0;
				}
			}
			if (x0 == 0.0) {
				x0 = 1.0;
			}
			if (x1 == 0.0) {
				x1 = 1.0;
			}

			entr += -1 / n * (x0 * Math.log(x0) + x1 * Math.log(x1))
					+ Math.log(n);
		}
		return entr;
	}
}
