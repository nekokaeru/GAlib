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

package operator.transform;

import java.util.Arrays;

/**
 * 一般的な線形スケーリング変換.
 * @author mori
 * @version 1.0
 */
public class LinearScalingTransform implements IArrayTransform {
	/**
	 * 変換後の最大値
	 */
	private double topValue_;

	/**
	 * 変換後の最小値
	 */
	private double bottomValue_;

	/**
	 * デフォルトコンストラクタ 変換後の最大値3および最小値1で初期化する.
	 */
	public LinearScalingTransform() {
		this(3, 1);
	}

	/**
	 * 変換後の最大値および最小値で初期化する.
	 * @param top 変換後の最大値
	 * @param bottom 変換後の最小値
	 */
	public LinearScalingTransform(double top, double bottom) {
		topValue_ = top;
		bottomValue_ = bottom;
	}

	/**
	 * 配列の値を線形スケーリングする.
	 * @param array オリジナルの数値配列
	 * @return result 変換後の配列
	 */
	public double[] transform(double[] array) {
		// array を最大 topValue, 最小 bottomValue の線形スケーリングを適用．
		if (array == null) {
			return null;
		}
		if (array.length == 0 || array.length == 1) {
			return (double[]) array.clone();
		}
		double[] result = new double[array.length];
		double maxFitness = -Double.MAX_VALUE, minFitness = Double.MAX_VALUE;
		// 最大値と最小値を調べる
		for (int i = 0; i < array.length; i++) {
			if (array[i] > maxFitness) {
				maxFitness = array[i];
			}
			if (array[i] < minFitness) {
				minFitness = array[i];
			}
		}
		if (maxFitness == minFitness) {
			return (double[]) array.clone();
		}
		for (int i = 0; i < array.length; i++) {
			result[i] = (topValue_ - bottomValue_) / (maxFitness - minFitness)
					* array[i]
					+ (bottomValue_ * maxFitness - topValue_ * minFitness)
					/ (maxFitness - minFitness);
		}
		return result;
	}

	/**
	 * 変換後の最小値を返す.
	 * @return 変換後の最小値
	 */
	public final double getBottomValue() {
		return bottomValue_;
	}

	/**
	 * 変換後の最小値を設定.
	 * @param bottomValue 変換後の最小値
	 */
	public final void setBottomValue(double bottomValue) {
		bottomValue_ = bottomValue;
	}

	/**
	 * 変換後の最大値を返す.
	 * @return 変換後の最大値
	 */
	public final double getTopValue() {
		return topValue_;
	}

	/**
	 * 変換後の最大値を設定.
	 * @param topValue 変換後の最大値
	 */
	public final void setTopValue(double topValue) {
		topValue_ = topValue;
	}

	/**
	 * 文字列化
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "LinearScalingTransform";
	}

	/**
	 * パラメータの情報を返す． 例：TOP_VALUE:300,BOTTOM_VALUE:10
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		return "TOP_VALUE:" + getTopValue() + ", BOTTOM_VALUE:"
				+ getBottomValue();
	}

	/**
	 * @see problem.function.IFunction#setParameter(java.lang.Object...)
	 * @param params パラメータの配列．第一要素が変換後の最大値，第二要素が変換後の最小値．
	 *        数値以外が指定されていたり，要素が2つでない場合には例外発生．
	 */
	public void setParameter(Object... params) {
		try {
			if (params.length != 2) {
				throw new Exception(
						"params length must be 2!");
			}
			// 変換後の最大値を設定． 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) {
				setTopValue(((Number) (params[0])).doubleValue());
			} else { // 数値系クラス以外の場合には文字列と判断して設定．
				setTopValue(Double.parseDouble(params[0].toString()));
			}
			// 切片設定． 文字列の場合と数値の場合で場合分け
			if (params[1] instanceof Number) {
				setBottomValue(((Number) (params[1])).doubleValue());
			} else { // 数値系クラス以外の場合には文字列と判断して設定．
				setBottomValue(Double.parseDouble(params[1].toString()));
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	public static void main(String[] args) {
		LinearScalingTransform ls = new LinearScalingTransform();
		System.out.println(ls.getParameterInfo());
	}
}
