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

package fitness;

import java.util.ArrayList;
import java.util.HashMap;
import population.Individual;

/**
 * 個体の適応度情報を保存するクラス.
 * @author mori
 * @version 1.0
 */
public class FitnessMemoryBank implements IFitnessMemoryBank {
	/**
	 * 個体オブジェクトと適応度を記憶する． 個体オブジェクトをキーに，適応度を値にしている．
	 */
	private HashMap<Individual, Double> memory_ = new HashMap<Individual, Double>();

	/**
	 * memory_ の key となる個体の履歴．記憶する上限を決めるため．
	 */
	private ArrayList<Individual> history_ = new ArrayList<Individual>();

	/**
	 * 記憶する個体数．負の場合は制限なし．
	 */
	private long maxMemorySize_ = -1;

	/**
	 * メモリ内に該当個体が存在すればその個体の適応度を返す. なければ null が返る．
	 * @param indiv 個体
	 * @return 適応度
	 */
	public Double getFitness(Individual indiv) {
		return memory_.get(indiv);
	}

	/**
	 * メモリ内に該当個体が存在するかを調べる.
	 * @param indiv 個体
	 * @return true: 存在する false: 存在しない
	 */
	public boolean containsIndividual(Individual indiv) {
		return memory_.containsKey(indiv);
	}

	/**
	 * メモリに個体を追加する. メモリ内に同一個体が存在する場合は追加しない. <br>
	 * メモリ内の個体数が規定のサイズを超える場合は, 最も古い個体を削除してから追加する.
	 * @param indiv 個体
	 * @param fitness 適応度
	 * @return true: 追加が成功した false: すでに同じ個体がメモリに存在している
	 */
	public boolean put(Individual indiv, Double fitness) {
		if (memory_.containsKey(indiv)) {
			// すでに存在している場合． TODO 動的環境など適応度が変化する場合には変更が必要．
			return false;
		}
		// 記憶と履歴を更新
		// 必ず，深いコピーをキーにして更新する．そうしないとキーがどこかで勝手に変わってしまう危険性あり．
		Individual keyIndiv = indiv.clone();
		memory_.put(keyIndiv, fitness);
		history_.add(keyIndiv);
		// サイズ超過の場合の処理
		if ((maxMemorySize_ > 0) && (memory_.size() > maxMemorySize_)) {
			remove(history_.get(0)); // 一番古いものを削除．
		}
		return true;
	}

	/**
	 * メモリから対象個体を削除. (履歴も同時に削除する.)
	 * @param indiv 個体
	 * @return true: 削除成功 false: メモリに対象個体が存在しない
	 */
	public boolean remove(Individual indiv) {
		if (memory_.containsKey(indiv)) {
			// 存在している場合は記憶と履歴から削除．
			memory_.remove(indiv);
			history_.remove(indiv);
			return true;
		}
		// 存在していなかった場合
		return false;
	}

	/**
	 * メモリのサイズを返す.
	 * @return メモリのサイズ
	 */
	public long size() {
		assert (memory_.size() == history_.size());
		return memory_.size();
	}

	/**
	 * メモリと履歴を初期化する.
	 */
	public void clear() {
		memory_.clear();
		history_.clear();
	}

	/**
	 * メモリ(HashMap)の文字列表現を返す.
	 * @return メモリの文字列表現
	 */
	public String toString() {
		return memory_.toString();
	}

	/**
	 * 設定されているメモリの上限のサイズを返す.
	 * @return maxMemorySize メモリの上限のサイズ
	 */
	public long getMaxMemorySize() {
		return maxMemorySize_;
	}

	/**
	 * メモリの上限のサイズを設定する. 負数を与えた場合はメモリのサイズを制限しない.
	 * @param maxMemorySize メモリの上限のサイズ
	 */
	public void setMaxMemorySize(long maxMemorySize) {
		maxMemorySize_ = maxMemorySize;
		if (maxMemorySize_ < 0) { // 負の場合は上限なし．
			return;
		}
		if (history_.size() > maxMemorySize) {
			int currentSize = history_.size();
			for (int i = 0; i < currentSize - maxMemorySize; i++) {
				memory_.remove(history_.get(0));// 記憶から超過分の古い個体を削除
				history_.remove(0);// 履歴からも削除
			}
		}
	}

	/**
	 * 実行例．
	 * @param args
	 */
	public static void main(String[] args) {
		FitnessMemoryBank map = new FitnessMemoryBank();
		Number[] i1 = { 1, 0, 1 };
		Number[] i2 = { 1, 0, 1 };
		Individual indiv1 = new Individual(i1);
		Individual indiv2 = new Individual(i2);
		indiv1.fitness();
		indiv2.fitness();
		map.put(indiv1, indiv1.fitness());
		System.err.println(map);
		indiv1.setGeneAt(0, 3);
		System.err.println(map);
	}
}
