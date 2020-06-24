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

package problem;

import java.util.Arrays;

/**
 * ナップサック問題のクラス． 30荷物，100荷物を事前に設定する． 新しい問題は，value_, weight_, limitWeight_,
 * optimum を設定して使用する．
 * @author mori
 * @version 1.0
 */
public class KnapsackProblem extends AbstractProblem {
	/**
	 * サンプル用30荷物ナップサック問題の価値
	 */
	private final double[] value30_ = { 23, 45, 15, 87, 24, 47, 63, 85, 26, 45,
			37, 57, 88, 53, 87, 77, 17, 38, 78, 21, 25, 19, 61, 89, 7, 5, 39,
			58, 18, 48 };

	/**
	 * サンプル用30荷物ナップサック問題の重さ
	 */
	private final double[] weight30_ = { 57, 40, 29, 57, 90, 1, 18, 95, 1, 5,
			18, 6, 14, 57, 21, 78, 10, 22, 31, 25, 1, 67, 88, 3, 87, 33, 44,
			61, 27, 10 };

	/**
	 * サンプル用30荷物ナップサック問題の制限重量
	 */
	private final double limitWeight30_ = 548.0;

	/**
	 * サンプル用30荷物ナップサック問題の最適解
	 */
	private double optimum30_ = 1136.0;

	/**
	 * サンプル用100荷物ナップサック問題の価値
	 */
	private final double[] value100_ = { 68, 134, 44, 260, 70, 139, 188, 254,
			78, 134, 110, 169, 264, 157, 261, 231, 50, 114, 233, 62, 75, 55,
			183, 266, 21, 13, 116, 172, 52, 142, 259, 41, 136, 101, 227, 71,
			61, 264, 134, 204, 27, 286, 97, 223, 185, 160, 136, 151, 55, 40,
			112, 283, 236, 116, 174, 225, 235, 44, 68, 179, 29, 95, 239, 137,
			196, 274, 287, 180, 202, 292, 15, 276, 127, 280, 284, 146, 197,
			178, 185, 129, 190, 87, 59, 98, 262, 36, 6, 226, 30, 27, 64, 27,
			111, 96, 217, 74, 27, 282, 127, 46 };

	/**
	 * サンプル用100荷物ナップサック問題の重さ
	 */
	private final double[] weight100_ = { 169, 118, 86, 170, 269, 2, 53, 284,
			3, 13, 53, 18, 41, 171, 63, 232, 29, 64, 93, 74, 2, 200, 263, 9,
			260, 99, 131, 181, 80, 28, 13, 73, 8, 154, 69, 162, 2, 126, 244,
			229, 155, 216, 143, 157, 148, 64, 143, 201, 272, 251, 281, 111,
			173, 246, 70, 19, 201, 211, 31, 266, 252, 39, 165, 245, 293, 177,
			8, 257, 223, 296, 76, 217, 249, 19, 194, 159, 13, 240, 136, 14, 80,
			103, 176, 267, 260, 90, 277, 11, 133, 205, 14, 172, 260, 204, 295,
			225, 106, 161, 86, 80 };

	/**
	 * サンプル用100荷物ナップサック問題の制限重量
	 */
	private final double limitWeight100_ = 7087.0;

	/**
	 * サンプル用100荷物ナップサック問題の最適解
	 */
	private double optimum100_ = 11878.0;

	/**
	 * 荷物の価値
	 */
	private double[] value_ = null;

	/**
	 * 荷物の重さ
	 */
	private double[] weight_ = null;

	/**
	 * 制限重量
	 */
	private double limitWeight_ = Double.NaN;;

	/**
	 * 最適値
	 */
	private double optimum_ = Double.NaN;

	/**
	 * デフォルトコンストラクタ
	 */
	public KnapsackProblem() {
		// 引数なしの場合には30荷物ナップサック問題
		value_ = value30_;
		weight_ = weight30_;
		limitWeight_ = limitWeight30_;
		optimum_ = optimum30_;
	}

	/**
	 * 引数ありコンストラクタ num: 荷物数．30, 100 のいずれか．
	 * @param itemNum で荷物数を決めて問題を初期化
	 */
	public KnapsackProblem(Object itemNum) { // 30, 100, 200 のいずれかを指定．
		setParameter(itemNum);
	}

	/**
	 * 可変長引数を利用した配列で初期化． パラメータ数は 1 で第一要素が荷物数．
	 * @param params パラメータの配列
	 */
	public void setParameter(Object... params) {
		int num = 0;
		try {
			// パラメータの要素数が正しくないとき
			if (params.length != 1) {
				throw new Exception("params length of PowerFunction must be 1!");
			}
			// 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) { // 数値クラスの場合
				num = ((Number) params[0]).intValue(); // 数値化
			} else { // 文字列の場合
				num = Integer.parseInt(params[0].toString()); // 数値化
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
		// num に応じて初期化
		switch (num) {
		case 30:
			// 30荷物で初期化
			value_ = value30_;
			weight_ = weight30_;
			limitWeight_ = limitWeight30_;
			optimum_ = optimum30_;
			break;
		case 100:
			// 100荷物で初期化
			value_ = value100_;
			weight_ = weight100_;
			limitWeight_ = limitWeight100_;
			optimum_ = optimum100_;
			break;
		default:
			// 例外を発生．
			throw new IllegalArgumentException("itemNum " + num
					+ " is not 30 or 100!!!!!!!");
		}
	}

	/**
	 * 目的関数値を計算して返すメソッド
	 * @param array 遺伝子型情報．遺伝子 1 で荷物を選ぶ．
	 * @return ナップサック問題の目的関数値
	 * @see problem.AbstractProblem#getObjectiveFunctionValue(Number[])
	 */
	@Override
	public double getObjectiveFunctionValue(Number[] array) {
		double result = 0.0;
		double weight = 0.0;
		for (int i = 0; i < array.length; i++) {
			if (array[i].intValue() == 1) {
				result += value_[i];
				weight += weight_[i];
			}
		}
		// 制限重量を超したときは評価を 0 にする
		if (weight > limitWeight_) {
			result = 0.0;
		}
		return result;
	}

	/**
	 * 問題の名前を返す
	 * @return 名前を返す
	 * @see problem.AbstractProblem#getName()
	 */
	@Override
	public String getName() {
		return "Knapsack";
	}

	/**
	 * 制限重量を返す．
	 * @return 制限重量
	 */
	public final double getLimitWeight() {
		return limitWeight_;
	}

	/**
	 * 最適解は外部的に与える必要あり． このクラスでは最適性を保証していない
	 * @return 最適解
	 */
	public final double getOptimum() {
		return optimum_;
	}

	/**
	 * 荷物の価値の配列を返す． 数値が外部で変更されないようにクローンを返す．
	 * @return 荷物の価値の配列
	 */
	public final double[] getValue() {
		return value_.clone();
	}

	/**
	 * 荷物の重さの配列を返す． 数値が外部で変更されないようにクローンを返す．
	 * @return 荷物の重さの配列
	 */
	public final double[] getWeight() {
		return weight_.clone();
	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("itemNum:" + getWeight().length);
		if (getFunctionInfo().length() == 0) {
			return sb.toString();
		}
		return sb.toString() + "," + getFunctionInfo();
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		Number[] array = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		KnapsackProblem npsk = new KnapsackProblem();
		// 0.0 が表示される．
		System.out.println(npsk.getFitness(array));
		// toString により Knapsack{itemNum:30} が表示される．
		System.out.println(npsk);
	}

}
