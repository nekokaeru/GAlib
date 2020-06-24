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

import java.util.ArrayList;
import java.util.Arrays;

import population.Population;

/**
 * 複数の演算子の結果を併せる演算子.
 * @author mori
 * @version 1.0
 */
public class Merge implements IOperator {
	/**
	 * オペレータリスト
	 */
	private ArrayList<IOperator> operators_;

	/**
	 * デフォルトコンストラクタ
	 */
	public Merge() {
		super();
	}

	/**
	 * 可変長引数を利用した配列で初期化． 個のクラスはパラメータなし．
	 * @param params パラメータの配列
	 */
	public void setParameter(Object... params) {
		// パラメータ無し
		if (params == null) {
			return;
		}
		try {
			// 長さ0の配列は認める．
			if (params.length != 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * 個体群に適用する. <br>
	 * 演算子が登録されていない → なにもしない <br>
	 * 登録演算子が一つだけ → 登録演算子を pop に適用して終了 <br>
	 * 登録演算子が 2 以上 → 登録演算子を pop の深いコピーに適用した結果を合わせる <br>
	 * @param pop 個体群
	 */
	public void apply(Population pop) {
		// 演算子が何もセットされてなければ
		if (operators_ == null || operators_.size() == 0) {
			return;
		}
		// 演算子が一つだけの場合．
		if (operators_.size() == 1) {
			operators_.get(0).apply(pop);
		}
		// 複数の演算子がある場合. 各演算子の適用結果を保存する配列．
		Population[] popArray = new Population[operators_.size()];
		popArray[0] = pop; // 先頭はオリジナル．clone を一回減らす．
		for (int i = 1; i < popArray.length; i++) {
			popArray[i] = pop.clone(); // 個体群の深いコピーを保存． i=1 より
		}
		// popArray.length == operators_.size()
		for (int i = 0; i < operators_.size(); i++) {
			IOperator op = operators_.get(i);
			if (op == null) { // null が入っていた場合には個体群に何もしない．
				continue;
			}
			op.apply(popArray[i]); // 各個体に演算子を適用
		}
		// pop==popArray[0] である． pop の IndivList にコピー＆演算子を適用した分を追加
		for (int i = 1; i < popArray.length; i++) {
			pop.getIndivList().addAll(popArray[i].getIndivList());
		}
	}

	/**
	 * 名前を返す.
	 * @return 名前
	 */
	public String getName() {
		return "Merge";
	}

	/**
	 * オペレータリストを返す.
	 * @return オペレータリスト
	 */
	public final ArrayList<IOperator> getOperators() {
		if (operators_ == null) {
			operators_ = new ArrayList<IOperator>();
		}
		return operators_;
	}

	/**
	 * オペレータリストの設定.
	 * @param operators オペレータリスト
	 */
	public final void setOperators(ArrayList<IOperator> operators) {
		operators_ = operators;
	}

	/**
	 * パラメータの情報を返す．Merge の場合は複数の演算子を内部に持つので，それぞれの情報を表示．<br>
	 * 例：BitMutation{MUTATION_RATE:0.01} OnePointCrossover{CROSSOVER_RATE:0.6}
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		if (getOperators().size() == 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		for (IOperator op : getOperators()) {
			result.append(op + ",");
		}
		// 最後の , を消す
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}

	/**
	 * 文字列化.
	 * @return 文字列表現
	 */
	public String toString() {
		return getName() + "{" + getParameterInfo() + "}";
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		Merge m = new Merge();
		Merge m2 = new Merge();
		m.getOperators().add(new BitMutation());
		m.getOperators().add(new OnePointCrossover());
		m.getOperators().add(null);
		m2.getOperators().add(new BitMutation());
		m2.getOperators().add(null);
		m2.getOperators().add(new RouletteSelection());
		m.getOperators().add(m2);
		System.out.println(m.getParameterInfo());
		System.out.println(m);
	}

}
