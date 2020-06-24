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

import problem.IProblem;
import problem.ProblemFactory;
import problem.function.FunctionFactory;
import problem.function.IFunction;

/**
 * 問題と変換関数の使い方の例．問題はビットカウントとバイナリ変換とし，プログラムへの引数で
 * 変換関数を指定する．このとき，パッケージ名(problem.function.)は指定しなくてよい．
 * クラス名の後に：で区切ってパラメータを指定できる．例： PowerFunction:2 パラメータ数は
 * NegativeFunction： なし PowerFunction： 1 (指数) LinearFunction： 2 (傾き，切片)
 * である． java Main PowerFunction:2 NegativeFunction
 * とすると，目的関数値を2乗して符号を反転する変換を行う．
 * @author mori
 * @version 1.0
 */
public class ProblemSample {
	public static void main(String[] args) {
		// 抽象クラスとして問題を指定．
		IProblem bitcount = ProblemFactory
				.createProblem("problem.BitCountProblem");
		IProblem binary = ProblemFactory
				.createProblem("problem.BinaryNumberProblem");
		// 3個体からなる個体群
		Number[][] pop = { { 0, 1, 0, 1, 0 }, { 0, 0, 0, 0, 0 },
				{ 1, 1, 1, 1, 1 } }; // 2次元配列
		// 変換関数のパッケージ
		String prefix = "problem.function.";
		// プログラム引数で指定されたクラスを変換関数として追加していく．
		for (String s : args) {
			IFunction f;
			if (s.indexOf(':') > 0) { // パラメータの指定あり
				String name = s.split(":")[0]; // デリミタ：で分割したときの先頭がクラス名
				f = FunctionFactory.createFunction(prefix + name);
				// 先頭のクラス名を取り除く
				String paramsName = s.replaceFirst("^" + name + ":*", "");
				Object[] params = new Object[0];
				if (!paramsName.equals("")) { // 空文字列の場合はパラメータ無し
					params = paramsName.split(":");
				}
				f.setParameter(params);
			} else { // パラメータの指定なし
				f = FunctionFactory.createFunction(prefix + s);
			}
			bitcount.addFunction(f);
			binary.addFunction(f);
		}
		for (Number[] genotype : pop) {
			// 遺伝子型表示
			System.out.println("genotype->" + Arrays.toString(genotype));
			// ビットカウント問題の場合の適応度 f と元の目的関数値 g を表示
			System.out.println("bitcount:" + "f="
					+ bitcount.getFitness(genotype) + " g="
					+ bitcount.getObjectiveFunctionValue(genotype));
			// バイナリ変換問題の場合の適応度 f と元の目的関数値 g を表示
			System.out.println("binary:  " + "f=" + binary.getFitness(genotype)
					+ " g=" + binary.getObjectiveFunctionValue(genotype) + "\n");
		}
	}

}
