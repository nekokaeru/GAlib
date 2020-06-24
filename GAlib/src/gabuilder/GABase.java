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

package gabuilder;

import fitness.FitnessManager;

import java.util.ArrayList;

import operator.BitMutation;
import operator.Elitism;
import operator.IOperator;
import operator.OnePointCrossover;
import operator.TournamentSelection;
import population.Population;
import problem.BitCountProblem;
import viewer.IViewer;
import viewer.TextViewer;

/**
 * GABase の基本クラス．個体群に演算子を繰り返し適用する．<br>
 * 交叉, 選択, 突然変異を適用 することは前提としない．任意の演算子列を適用可能．
 * @author mori
 * @version 1.0
 */

public class GABase {
	/**
	 * オペレータを格納するリスト．このリスト内のオペレータを一度適用する期間が一世代となる．
	 */
	private ArrayList<IOperator> operatorList_;

	/**
	 * 個体群．
	 */
	private Population population_;

	/**
	 * GAパラメータ
	 */
	private GAParameters param_;

	/**
	 * 現在の世代数．初期個体群は世代 0 とし，演算子をすべて適用したら1増やす．
	 */
	private long currentGeneration_ = 0;

	/**
	 * 世代数．
	 */
	private long generationSize_ = GAParameters.DEFAULT_GENERATION_SIZE;

	/**
	 * 次に実行する演算子の operatorList_ における位置
	 */
	private int nextIndex_ = 0;

	/**
	 * 世代ごとの表示ビューワリスト
	 */
	private ArrayList<IViewer> generationViewerList_;

	/**
	 * 演算子ごとの表示ビューワリスト
	 */
	private ArrayList<IViewer> operationViewerList_;

	/**
	 * 引数なしコンストラクタ
	 */
	public GABase() {
		operatorList_ = new ArrayList<IOperator>();
	}

	/**
	 * とりあえず動かしたい場合用の SGA 初期化関数．外部からのパラメータ変更はできない．<br>
	 * あくまでもサンプルなので，IGABuilder と Director を使うことを奨励．
	 * @see Director
	 * @see IGABuilder
	 * @see DefaultGABuilder
	 * @see TDGABuilder
	 */
	public void sampleInit() {
		// ビットカウント問題
		FitnessManager.setProblem(new BitCountProblem());
		// 個体群の設定. 100 個体 遺伝子長 50
		setPopulation(new Population(100, 50));
		// 遺伝演算子の設定
		ArrayList<IOperator> list = new ArrayList<IOperator>();
		// 交叉は交叉率0．6の一点交叉
		OnePointCrossover c = new OnePointCrossover();
		c.setParameter(0.6);
		// 突然変異率0．02
		BitMutation m = new BitMutation();
		m.setParameter(0.02);
		// 選択はトーナメントサイズ 3 のトーナメント選択．
		TournamentSelection s = new TournamentSelection();
		s.setParameter(3);
		list.add(c);
		list.add(m);
		list.add(s);
		// エリート保存あり
		list.add(new Elitism());
		setOperatorList(list);
		// 世代数 100
		setGenerationSize(100);
		// ビューワの設定．表示レベル2の TextViewer．標準出力に表示．
		TextViewer v = new TextViewer();
		v.setPrintLevel(2);
		addGenerationViewer(v);
	}

	/**
	 * 開始時に一度だけ行われる処理を記述．
	 */
	public void begin() {
		for (IViewer viewer : getGenerationViewerList()) {
			viewer.begin();
		}
	}

	/**
	 * 終了時に一度だけ行われる処理を記述．
	 */
	public void end() {
		for (IViewer viewer : getGenerationViewerList()) {
			viewer.end();
		}
	}

	/**
	 * GABase のメインループ. 世代数回 nextGeneration を呼ぶ．
	 * @see #nextGeneration()
	 */
	public void start() {
		begin();
		for (int i = 0; i < getGenerationSize(); i++) {
			nextGeneration();
		}
		end();
	}

	/**
	 * GABase の世代をひとつ進める．operatorList_ の途中で止まっている場合は途中からリストの最後まで進める．
	 */
	public void nextGeneration() {
		int startIndex = getNextIndex();
		for (int i = startIndex; i < getOperatorList().size(); i++) {
			// null はエラー． 恒等演算子は Dummy を使う．
			nextOperation();
		}
		// 世代に関するビューワの更新
		for (IViewer viewer : getGenerationViewerList()) {
			// 世代を付加情報として表示
			viewer.update("generation:" + getCurrentGeneration() + " ");
		}
	}

	/**
	 * 個体群に次の遺伝演算子を適用する．
	 */
	public void nextOperation() {
		// 次の遺伝演算子を適用．
		getNextOperator().apply(population_);
		/**
		 * 世代表示．n世代の途中の場合は，世代は n-1 世代だが，すでに n 世代に突入していると考えるので <br>
		 * currentGeneration に 1 を足す． n 世代に適用予定の演算子数が M の場合は 
		 * generation: n operator: 1/M <br>
		 * などとして，何番目の演算子を適用中かわかるようにする．n 世代が丁度 終了するのは <br>
		 * generation: n operator: M/M のときとなる．<br>
		 */
		long generation = getCurrentGeneration() + 1;
		int opSize = getOperatorList().size();
		// 演算子に関するビューワの更新
		for (IViewer viewer : getOperationViewerList()) {
			viewer.update("generation:" + generation + " operator:"
					+ getNextIndex() + "/" + opSize + " ");
		}
		// operator_ の現在位置を 進める．
		if (getNextIndex() == getOperatorList().size() - 1) {
			// operatorList_ の最後の演算子の場合は nextIndex_ を 0 にして世代を一つ進める．
			setNextIndex(0);
			setCurrentGeneration(getCurrentGeneration() + 1);
		} else {
			// 途中の場合は単に nextIndex_ を一つ進める．
			setNextIndex(getNextIndex() + 1);
		}
	}

	/**
	 * 次に適用する遺伝演算子を返す．オペレータが空だとエラーになる． <br>
	 * 何もしない場合を表現したければ，Dummy演算子を入れておく．
	 * @return 次に適用する遺伝演算子．
	 */
	public IOperator getNextOperator() {
		return getOperatorList().get(getNextIndex());
	}

	/**
	 * オペレータリストを返す． 参照を直接返す．
	 * @return オペレータリスト
	 */
	public ArrayList<IOperator> getOperatorList() {
		return operatorList_;
	}

	/**
	 * オペレータリストを設定する．
	 * @param operators オペレータリスト
	 */
	public void setOperatorList(ArrayList<IOperator> operators) {
		operatorList_ = operators;
	}

	/**
	 * 演算子リスト内で，演算子1と演算子2の位置を交換する．
	 * @param index1 演算子1の位置
	 * @param index2 演算子2の位置
	 */
	public void swapOperatorPosition(int index1, int index2) {
		IOperator op1 = getOperatorList().get(index1);
		IOperator op2 = getOperatorList().get(index2);
		// op1 と op2 の位置を交換
		getOperatorList().set(index1, op2);
		getOperatorList().set(index2, op1);

	}

	/**
	 * 個体群を返す． 参照を直接返す．
	 * @return 個体群
	 */
	public Population getPopulation() {
		return population_;
	}

	/**
	 * 個体群を設定する．
	 * @param pop 個体群
	 */
	public void setPopulation(Population pop) {
		population_ = pop;
	}

	/**
	 * パラメータを返す.
	 * @return パラメータ
	 */
	public GAParameters getGAParameter() {
		return param_;
	}

	/**
	 * パラメータの設定. 
	 * @param param パラメータ
	 */
	public void setGAParameter(GAParameters param) {
		param_ = param;
	}

	/**
	 * 現在の世代数を返す.
	 * @return 現在の世代数
	 */
	public long getCurrentGeneration() {
		return currentGeneration_;
	}

	/**
	 * 現在の世代数を設定.
	 * @param currentGeneration 現在の世代数
	 */
	public void setCurrentGeneration(long currentGeneration) {
		currentGeneration_ = currentGeneration;
	}

	/**
	 * 次に実行する演算子の operatorList_ における位置を返す.
	 * @return 次に実行する演算子の operatorList_ における位置
	 */
	public int getNextIndex() {
		return nextIndex_;
	}

	/**
	 * 次に実行する演算子の operatorList_ における位置を設定. i が適切かはチェックしない．
	 * @param i 次に実行する演算子の operatorList_ における位置
	 */
	public void setNextIndex(int i) {
		nextIndex_ = i;
	}

	/**
	 * オペレータ名を要素にもつリストを返す.
	 * @return オペレータ名を要素に持つリスト
	 */
	public ArrayList<String> getOperatorNameList() {
		ArrayList<String> result = new ArrayList<String>();
		for (IOperator op : getOperatorList()) {
			result.add(op.getName());
		}
		return result;
	}

	/**
	 * オペレータの詳細な情報を返す．
	 * @return オペレータ情報
	 */
	public String getOperatorInfo() {
		StringBuilder sb = new StringBuilder();
		for (IOperator op : getOperatorList()) {
			// op が null の場合を考慮して明示的に op.toString() を呼ばない．
			sb.append(op + "\n");
		}
		return sb.toString();
	}

	/**
	 * 世代ごとに呼ばれるビューワ登録．重複登録は許さない．
	 * @param viewer
	 * @return 登録できたか?
	 */
	public boolean addGenerationViewer(IViewer viewer) {
		// 既に同一の参照を保持している場合には，重複登録はしない．
		if (getGenerationViewerList().contains(viewer)) {
			return false;
		}
		// viewer に自分を登録．
		viewer.setGA(this);
		// viewer 登録．
		getGenerationViewerList().add(viewer);
		return true;
	}

	/**
	 * generationViewerList_ のクリア
	 */
	public void clearGenerationViewerList() {
		getGenerationViewerList().clear();
	}

	/**
	 * generationViewerList_ 内のビューワの名前のリストを返す．
	 * @return ビューワの名前のリスト
	 */
	public ArrayList<String> getGenerationViewerNameList() {
		ArrayList<String> result = new ArrayList<String>();
		for (IViewer v : getGenerationViewerList()) {
			result.add(v.getName());
		}
		return result;
	}

	/**
	 * 世代ごとのビューワリストを返す.
	 * @return 世代ごとのビューワリスト
	 */
	private ArrayList<IViewer> getGenerationViewerList() {
		if (generationViewerList_ == null) {
			generationViewerList_ = new ArrayList<IViewer>();
		}
		return generationViewerList_;
	}

	/**
	 * 演算子ごとに呼ばれるビューワ登録．重複登録は許さない．
	 * @param viewer
	 * @return 登録できたか?
	 */
	public boolean addOperationViewer(IViewer viewer) {
		// 既に同一の参照を保持している場合には，重複登録はしない．
		if (getOperationViewerList().contains(viewer)) {
			return false;
		}
		// viewer に自分を登録．
		viewer.setGA(this);
		// viewer 登録．
		getOperationViewerList().add(viewer);
		return true;
	}

	/**
	 * operationViewerList_ のクリア
	 */
	public void clearOperationViewerList() {
		getOperationViewerList().clear();
	}

	/**
	 * operationViewerList_ 内のビューワの名前のリストを返す．
	 * @return ビューワの名前のリスト
	 */
	public ArrayList<String> getOperationViewerNameList() {
		ArrayList<String> result = new ArrayList<String>();
		for (IViewer v : getOperationViewerList()) {
			result.add(v.getName());
		}
		return result;
	}

	/**
	 * 演算子ごとのビューワリストを返す.
	 * @return 演算子ごとのビューワリスト
	 */
	private ArrayList<IViewer> getOperationViewerList() {
		if (operationViewerList_ == null) {
			operationViewerList_ = new ArrayList<IViewer>();
		}
		return operationViewerList_;
	}

	/**
	 * 世代数を返す.
	 * @return 世代数
	 */
	public long getGenerationSize() {
		return generationSize_;
	}

	/**
	 * 世代数の設定
	 * @param generationSize 世代数
	 */
	public void setGenerationSize(long generationSize) {
		generationSize_ = generationSize;
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		GABase ga = new GABase();
		ga.sampleInit();
		ga.start();
	}
}
