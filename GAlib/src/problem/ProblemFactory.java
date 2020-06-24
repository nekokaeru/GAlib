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

/**
 * 問題クラスを作成するファクトリクラス． String でクラス名を指定する． 指定したクラス名のクラスが無いと例外発生．
 * @author mori
 * @version 1.0
 */
public class ProblemFactory {
	/**
	 * String でクラス名を指定して問題クラスを返す． 指定したクラス名のクラスが無いと例外発生．
	 * @param name 問題クラスのクラス名
	 * @return 問題クラス
	 */
	public static IProblem createProblem(String name) {
		try {
			return (IProblem) (Class.forName(name).newInstance());
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		IProblem p = ProblemFactory.createProblem("problem.BitCountProblem");
		Number[] array = { 1, 1, 0, 0, 1 };
		// 3.0が表示される．
		System.out.println(p.getFitness(array));
	}
}
