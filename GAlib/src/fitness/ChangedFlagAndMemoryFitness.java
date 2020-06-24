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
 * 変更フラグとメモリを持つ高速化された適応度クラス.
 * @author mori
 * @version 1.0
 */
public class ChangedFlagAndMemoryFitness implements IFitness {

	/**
	 * Individual のインスタンスを受け取り適応度を返す． 高速化フラグが立っており，変更がなければ以前の適応度を返す．<br>
	 * 記憶フラグが立っており，自身が記憶されていればその情報を返す． 適応度が計算された時は，エリートを保存する． <br>
	 * また前適応度と変更フラグを更新する． また最後に記憶フラグが立っていた場合にはこの個体と適応度情報を追加する．
	 * @param indiv 個体クラス
	 * @see fitness.IFitness#fitness(population.Individual)
	 */
	public double fitness(Individual indiv) {
		// 高速化フラグが立っており，変更がなければ以前の適応度を返す．
		if (FitnessManager.isTurbo() && !indiv.isChanged()) {
			return indiv.getPreviousFitness();
		}
		// 記憶フラグが立っており，自身が記憶されていればその情報を返す．
		if (FitnessManager.isMemory()
				&& FitnessManager.getMemoryBank().containsIndividual(indiv)) {
			double f = FitnessManager.getMemoryBank().getFitness(indiv);
			indiv.setPreviousFitness(f);
			indiv.setChanged(false);
			return f;
		}
		double f = indiv.simpleFitness();
		// 新たに適応度を計算したので，前適応度と変更フラグを更新．
		indiv.setPreviousFitness(f);
		indiv.setChanged(false);
		// 記憶フラグが立っていた場合にはこの個体と適応度情報を追加．
		if (FitnessManager.isMemory()) {
			FitnessManager.getMemoryBank().put(indiv, f);
		}
		return f;
	}

}
