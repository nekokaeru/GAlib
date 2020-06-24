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

package sample;

import java.util.Arrays;
import java.util.Random;

/**
 * Java 初心者用単純GAクラス.
 * オブジェクト指向的な要素は一切なく C 言語レベルの実装である．
 * @author mori and takeuchi 
 * @version 1.0
 */
public class CLike_SGA {
	/** 遺伝子長 */
	int LENGTH = 10;

	/** 個体数 */
	int POPSIZE = 10;

	/** 突然変異率 */
	double MRATE = 0.1;

	/** 交叉率 */
	double CRATE = 0.6;

	/** 世代数 */
	int GENERATION_SIZE = 100;

	/** エリート個体 */
	int[] elite_;

	/** 個体群 */
	int[][] pop_ = new int[POPSIZE][LENGTH];

	/** 乱数 */
	Random rand_ = new Random();

	/** デフォルトコンストラクタ */
	public CLike_SGA() {
		// 個体群を初期化
		for (int i = 0; i < POPSIZE; i++) {
			for (int j = 0; j < LENGTH; j++) {
				pop_[i][j] = rand_.nextInt(2);
			}
		}
	}

	/** 個体群を表示 */
	public void printPop() {
		for (int i = 0; i < POPSIZE; i++) {
			System.out.println(Arrays.toString(pop_[i]) + " fitness:"
					+ fitness(pop_[i]));
		}
	}

	/** エリート個体を保存 */
	public void setElite() {
		int bestFitness = fitness(pop_[0]); // 先頭個体の適応度で初期化
		int bestNum = 0; // 先頭個体を仮のエリートとする
		for (int i = 1; i < POPSIZE; i++) {
			int f = fitness(pop_[i]);
			if (f > bestFitness) { // より良い個体がいればそちらを保存
				bestFitness = f;
				bestNum = i;
			}
		}
		System.out.println(bestNum);
		elite_ = pop_[bestNum].clone(); // コピーを保存
	}

	/** 探索をスタート */
	public void start() {
		setElite();
		selection();
		crossover();
		mutation();
		returnElite();
	}

	/**
	 * 個体群に一点交叉を適用．
	 */
	public void crossover() {
		// 選択後に呼ばれることを前提にしているのでシャッフル省く
		for (int i = 0; i < POPSIZE / 2; i++) {
			/*
			 * TODO 個体 pop_[2*i] と pop_[2*i+1] に交叉を適用． pop_ の中身を直接書き換える．
			 */
			if (rand_.nextDouble() < CRATE) {
				// 交叉点
				int point = rand_.nextInt(LENGTH);
				for (int j = point; j < LENGTH; j++) {
					int tmp = pop_[2 * i][j];
					pop_[2 * i][j] = pop_[2 * i + 1][j];
					pop_[2 * i + 1][j] = tmp;
				}
			}
		}
	}

	/**
	 * 個体群に突然変異を適用．
	 */
	public void mutation() {
		for (int i = 0; i < POPSIZE; i++) {
			/*
			 * TODO 個体 pop_[i]に突然変異を適用． pop_ の中身を直接書き換える．
			 */
			for (int j = 0; j < LENGTH; j++) {
				if (rand_.nextDouble() < MRATE) {
					// 遺伝子が1の場合0に, 0の場合1に換える.
					// それ以外の数の時は例外発生.
					try {
						switch (pop_[i][j]) {
						case 0:
							pop_[i][j] = 1;
							break;
						case 1:
							pop_[i][j] = 0;
							break;
						default:
							throw new IllegalArgumentException();
						}
					} catch (IllegalArgumentException e) {
						System.out.println("Gene must be used 0 or 1.");
					}
				}
			}
		}
	}

	/**
	 * 個体群に適応度比例選択(ルーレット選択)を適用．
	 */
	public void selection() {
		int[][] tmp = new int[POPSIZE][LENGTH];
		/*
		 * TODO pop_ からルーレットを回して一個体を選び tmp[i] に追加． この場合，選んだ個体のコピーを渡すこと. pop_[x]
		 * が選ばれた場合には <br>
		 * tmp[i]=pop_[x] <br>
		 * ではなく， 
		 * tmp[i]=pop_[x].clone() <br>
		 * とする．pop_[x]はプリミティブ型の配列なのでこれで OK.
		 */
		double sum = 0;
		int[] all_fitness = new int[POPSIZE];
		for (int i = 0; i < POPSIZE; i++) {
			all_fitness[i] = fitness(pop_[i]);
			sum += all_fitness[i];
		}
		for (int i = 0; i < POPSIZE; i++) {
			// ルーレットの針を回す.
			double needle = rand_.nextDouble() * sum;
			int index = 0;
			// 針が指す場所を探索
			while (needle - all_fitness[index] > 0) {
				needle -= all_fitness[index];
				index++;
			}
			// 選んだ個体を保存
			tmp[i] = pop_[index].clone();
		}
		// 個体群の更新
		pop_ = tmp;
	}

	/**
	 * 個体群にエリート個体を戻す為に エリート個体と適応度の最も低い個体とを入れ替える.
	 */
	public void returnElite() {
		// 適応度の最も低い個体を探索
		int worstFitness = fitness(pop_[0]); // 先頭個体の適応度で初期化
		int worstNum = 0; // 先頭個体を仮とする
		for (int i = 1; i < POPSIZE; i++) {
			int f = fitness(pop_[i]);
			if (f < worstFitness) { // より悪い個体がいればそちらを保存
				worstFitness = f;
				worstNum = i;
			}
		}
		pop_[worstNum] = elite_.clone(); // エリートに置き換える
	}

	/**
	 * 個体の適応度を返す．ビットカウント．
	 * @param indiv 個体
	 * @return 適応度
	 */
	public int fitness(int[] indiv) {
		int sum = 0;
		for (int i = 0; i < indiv.length; i++) {
			if (indiv[i] == 1) {
				sum += 1;
			}
		}
		return sum;
	}

	public static void main(String[] args) {
		CLike_SGA ga = new CLike_SGA();
		for (int i = 0; i < ga.GENERATION_SIZE; i++) {
			ga.start();
			System.out.println(Arrays.toString(ga.elite_));
		}
		ga.printPop();
	}

}
