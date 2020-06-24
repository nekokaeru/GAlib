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

import population.Individual;

/**
 * 個体の情報を保持する機構のインターフェース.
 * オンメモリや外部データベースを利用.
 * @author mori
 * @version 1.0
 */
public interface IFitnessMemoryBank {
	/**
	 * 個体の適応度データを取得する． 記憶にない個体だった場合は null が返る．
	 * @param indiv 対象個体
	 * @return 適応度
	 */
	public Double getFitness(Individual indiv);

	/**
	 * 検索個体が記憶されているかを調べる．
	 * @param indiv 検索個体
	 * @return true: 存在する false: 存在しない
	 */
	public boolean containsIndividual(Individual indiv);

	/**
	 * 個体と適応度の組を追加する．
	 * @param indiv 個体, fitness indivの適応度
	 * @return 追加されたかのブール値 true: 追加された false: すでに存在していた
	 */
	public boolean put(Individual indiv, Double fitness);

	/**
	 * 個体の情報を削除する．
	 * @param indiv 個体
	 * @return 削除されたかのブール値 true: 削除された false: 存在しなかった
	 */
	public boolean remove(Individual indiv);

	/**
	 * 記憶している個体数．
	 * @return 記憶している個体数
	 */
	public long size();

	/**
	 * 全情報の消去
	 */
	public void clear();
}
