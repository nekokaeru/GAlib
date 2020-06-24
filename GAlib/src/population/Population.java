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
 * 個体群クラス
 * Created on 2005/07/16
 * 
 */
package population;

import gabuilder.GAParameters;

import java.util.ArrayList;

/**
 * 個体群クラス.
 * @author mori
 * @version 1.0
 */
public class Population implements Cloneable {
	/**
	 * 個体群
	 */
	private ArrayList<Individual> indivList_ = new ArrayList<Individual>();

	/**
	 * 引数なしのコンストラクタ．
	 */
	public Population() {
		// 引数なしの場合は個体数100，遺伝子長10で初期化．
		init(GAParameters.DEFAULT_POPULATION_SIZE,
				GAParameters.DEFAULT_CHROMOSOME_LENGTH);
	}

	/**
	 * 個体数と遺伝子長で初期化するコンストラクタ
	 * @param popsize 個体数
	 * @param clength 遺伝子長
	 */
	public Population(int popsize, int clength) {
		init(popsize, clength);
	}

	/**
	 * indivList による初期化をするコンストラクタ
	 * @param indivList 個体群の ArrayList
	 */
	public Population(ArrayList<Individual> indivList) {
		setIndivList(indivList);
	}

	/**
	 * 初期化関数
	 * @param popsize 個体数
	 * @param clength 遺伝子長
	 */
	public void init(int popsize, int clength) {
		indivList_.clear();
		for (int i = 0; i < popsize; i++) {
			indivList_.add(new Individual(clength));
		}
	}

	/**
	 * 深いコピーをするために，clone をオーバーライド． JDK5.0 の共変戻り値を利用．
	 * @return 深いコピーされた Population
	 */
	@Override
	public Population clone() {
		try {
			Population pop = (Population) super.clone();
			ArrayList<Individual> indivList = new ArrayList<Individual>();
			for (Individual indiv : indivList_) {
				// Individual クラスの clone は深いコピーではないが
				// 遺伝子が Integer 等の不変オブジェクトであれば問題なし．
				indivList.add(indiv.clone());
			}
			pop.setIndivList(indivList);
			return pop;
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e.getMessage());
		}
	}

	/**
	 * 個体数を返す.
	 * @return 個体数
	 */
	public final int getPopulationSize() {
		return indivList_.size();
	}

	/**
	 * 個体群を返す.
	 * @return 個体群
	 */
	public final ArrayList<Individual> getIndivList() {
		return indivList_;
	}

	/**
	 * 一個体を返す．
	 * @param index 個体群のある個体を指すインデックス
	 */
	public final Individual getIndividualAt(int index) {
		return indivList_.get(index);
	}

	/**
	 * 高速化のため深いコピーはしない．
	 * @param pop 個体群
	 */
	public final void setIndivList(ArrayList<Individual> pop) {
		indivList_ = pop;
	}

	/**
	 * 一個体を上書き.
	 * @param index 個体群のとある個体
	 * @param indiv 上書きする個体
	 */
	public final void setIndividualAt(int index, Individual indiv) {
		indivList_.set(index, indiv);
	}

	/**
	 * すべての個体の染色体を String に変換して返す．
	 * @return すべての個体の染色体の文字列
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Individual indiv : indivList_) {
			sb.append(indiv.toString());
			sb.append("\n");

		}
		return sb.toString();
	}

	/**
	 * 個体群全体の変更フラグをセットする．
	 * @param b セットする真偽値
	 */
	public void setAllChanged(boolean b) {
		for (Individual indiv : indivList_) {
			indiv.setChanged(b);
		}
	}

	/**
	 * 実行例．
	 * @param args
	 */
	public static void main(String[] args) {
		Population pop = new Population();
		System.err.println(pop);
	}
}
