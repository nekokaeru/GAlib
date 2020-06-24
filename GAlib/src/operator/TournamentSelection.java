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

import gabuilder.GAParameters;

import java.util.ArrayList;
import java.util.Arrays;

import util.LocalTestException;
import util.MyRandom;
import util.NStatistics;

/**
 * トーナメント選択.
 * @author mori
 * @version 1.0
 */
public class TournamentSelection extends AbstractSelection {
	/**
	 * トーナメントサイズ
	 */
	private int tournamentSize_ = GAParameters.DEFAULT_TOURNAMENT_SIZE;

	/**
	 * 可変長引数を利用した配列で初期化． パラメータ数は 1 で第一要素がトーナメントサイズ．
	 * @param params パラメータの配列
	 */
	@Override
	public void setParameter(Object... params) {
		try {
			// パラメータの要素数が正しくないとき
			if (params.length != 1) {
				throw new Exception(
						"params length of TournamentSelection must be 1!");
			}
			// 文字列の場合と数値の場合で場合分け
			if (params[0] instanceof Number) { // 数値クラスの場合
				int x = ((Number) params[0]).intValue(); // 数値化
				setTournamentSize(x); // トーナメントサイズの設定

			} else { // 文字列の場合
				int x = Integer.parseInt(params[0].toString()); // 数値化
				setTournamentSize(x); // トーナメントサイズの設定
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("params "
					+ Arrays.toString(params) + " are invalid!");
		}
	}

	/**
	 * 選択
	 * @param allFitness 全個体の適応度情報
	 * @return 選ばれた個体の index
	 */
	@Override
	public int[] select(double[] allFitness) {
		int[] indexArray = new int[allFitness.length];
		int[] numArray;
		// 適応度が等しい場合のタイブレーク用．
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		for (int i = 0; i < allFitness.length; i++) {
			// ランダムにトーナメントサイズ分の数字を選ぶ．
			numArray = makeNumbers(getTournamentSize(), allFitness.length);
			// numArray の中から allFitness の値が最大の index を調べる．
			double max = Double.NEGATIVE_INFINITY;
			buffer.clear();
			for (int j = 0; j < numArray.length; j++) {
				if (allFitness[numArray[j]] > max) {
					buffer.clear();
					buffer.add(numArray[j]);
					max = allFitness[numArray[j]];
				} else if (allFitness[numArray[j]] == max) {
					// 等しい場合にはタイブレーク用に保持して終わり．
					buffer.add(numArray[j]);
				}
			}
			// タイブレーク．
			int selectNum = MyRandom.getInstance().nextInt(buffer.size());
			indexArray[i] = buffer.get(selectNum);
		}
		return indexArray;
	}

	/**
	 * 0～maxSize-1 の中から重複しない size 個の数字を選ぶ.
	 * @param size 選ぶ数字の個数
	 * @param maxSize 数字の全個数
	 * @return 選んだ数字の組
	 */
	private int[] makeNumbers(int size, int maxSize) {
		// 選ぶ個数が全個数より大きかったら例外
		if (size > maxSize) {
			throw new IllegalArgumentException(size + ">" + maxSize);
		}
		/**
		 * 選ぶ数が全体の半数を超した場合には，選ばれない方を選んだ方が早い． <br>
		 * 特に，size と maxSize が近い場合には効果が大きい．<br>
		 * isNormal が true: selectSize == size 個を普通に選ぶ． isNormal が false:
		 * 選ばれない方の個体を selectSize 個選んで maxSize-selectSize 個を返す．
		 */
		boolean isNormal = true;
		// 選ばれる個体数．
		int selectSize = size;
		if (2 * size > maxSize) {
			isNormal = false;
			// この場合は selectSize は選ばれないほうの個体数である．
			selectSize = maxSize - size;
		}
		int[] numArray = new int[selectSize];
		// 一致しない番号で初期化
		Arrays.fill(numArray, Integer.MIN_VALUE);
		for (int i = 0; i < selectSize; i++) {
			// 既に選ばれた番号か？
			boolean isNewValue = false;
			int num = -1;
			while (!isNewValue) {
				MyRandom.getInstance().nextInt(maxSize);
				num = MyRandom.getInstance().nextInt(maxSize);
				isNewValue = true;
				for (int j = 0; j < numArray.length; j++) {
					if (num == numArray[j]) {
						// ひとつでも既に選ばれた数字と一致すればやり直し．
						isNewValue = false;
						break;
					}
				}
			}
			numArray[i] = num;
		}
		if (isNormal) {
			// 普通に選ばれた数字を返す．ほとんどの場合はこちらが実行される．
			return numArray;
		} else {
			/**
			 * 選ばれない番号を探した場合には，残った方を返す．少し余分な処理が必要だが，<br>
			 * GA ではトーナメントサイズは一桁，個体数は 100 以上の場合が 多いので，<br>
			 * ここが実行されることはほとんどないはず.
			 */
			boolean[] isSelected = new boolean[maxSize];
			Arrays.fill(isSelected, true);
			// size == maxSize のときは numArray.length == 0 なので次のループに入らない．
			for (int i = 0; i < numArray.length; i++) {
				// 選ばれた番号の index を false にする．
				isSelected[numArray[i]] = false;
			}
			int[] result = new int[size];
			int index = 0; // result 用のindex
			for (int i = 0; i < maxSize; i++) {
				if (isSelected[i]) { // もしも，残った方であればresult に追加．
					result[index++] = i;
				}
			}
			// 残りの方を返す．
			return result;
		}
	}

	/**
	 * 名前を返す.
	 * @return name 名前
	 */
	public String getName() {
		return "TournamentSelection";
	}

	/**
	 * トーナメントサイズを返す.
	 * @return トーナメントサイズ
	 */
	public final int getTournamentSize() {
		return tournamentSize_;
	}

	/**
	 * トーナメントサイズの設定.
	 * @param size トーナメントサイズ
	 */
	public final void setTournamentSize(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size: " + size + "<" + 0);
		}
		tournamentSize_ = size;
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		TournamentSelection ts = new TournamentSelection();
		int i = 0;
		try {
			for (i = 0; i < 10000; i++) {
				if (i % 200 == 0)
					System.out.println(i);
				ts.localTest();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			System.err.println(i);
		}
	}

	/**
	 * private と protected のテスト関数．
	 * @throws LocalTestException
	 */
	public void localTest() throws LocalTestException {
		int size = 20;
		int[] result = null;
		// 全部選ぶ場合には，数字が順番(0,1,2...)に入る．
		result = makeNumbers(size, size);
		for (int i = 0; i < result.length; i++) {
			if (result[i] != i) {
				throw new LocalTestException();
			}
		}

		for (int i = 0; i < size; i++) {
			result = makeNumbers(i, size);
			if (result.length != i) {
				throw new LocalTestException(i + "!=" + result.length);
			}
		}
		int maxi = 5000;
		// 3 を選ぶ場合の統計的テスト
		int[] sum = new int[size];
		Arrays.fill(sum, 0);
		int selectNum = 3;
		for (int i = 0; i < maxi; i++) {
			result = makeNumbers(selectNum, size);
			for (int j = 0; j < result.length; j++) {
				sum[result[j]]++;
			}
		}
		for (int i = 0; i < sum.length; i++) {
			if (NStatistics.binomialTest(maxi, sum[i], (double) selectNum
					/ size, 5) == false) {
				throw new LocalTestException(maxi * 0.1 + " " + sum[i]
						+ " seed:" + MyRandom.getSeed());
			}
		}
		// 17 を選ぶ場合の統計的テスト
		Arrays.fill(sum, 0);
		selectNum = 17;
		for (int i = 0; i < maxi; i++) {
			result = makeNumbers(selectNum, size);
			for (int j = 0; j < result.length; j++) {
				sum[result[j]]++;
			}
		}
		for (int i = 0; i < sum.length; i++) {
			if (NStatistics.binomialTest(maxi, sum[i], (double) selectNum
					/ size, 5) == false) {
				System.err.println(i + " " + sum[i]);
				throw new LocalTestException(maxi * 0.9 + " " + sum[i]
						+ " seed:" + MyRandom.getSeed());
			}
		}

	}

	/**
	 * パラメータの情報を返す．例： TOURNAMENT_SIZE:3
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo() {
		String info = "TOURNAMENT_SIZE:" + String.valueOf(getTournamentSize());
		String s = super.getParameterInfo();
		if (s.equals("")) {
			return info;
		} else {
			return info + "," + s;
		}
	}
}
