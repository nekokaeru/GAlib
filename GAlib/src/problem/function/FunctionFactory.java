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

package problem.function;

/**
 * 指定された名前の関数を表すクラスのインスタンスを生成するクラス
 * @author mori
 * @version 1.0
 */
public class FunctionFactory {

	/**
	 * 指定された名前の関数クラスのインスタンスを生成.
	 * 存在しないクラス名やIFunctionインターフェースを実装していないクラス名を引数にとると例外発生.
	 * @param name 生成したい関数のクラス名
	 * @return 指定された名前のクラスのインスタンスを返す
	 */
	public static IFunction createFunction(String name) {
		try {
			// 指定された名前のクラスを呼び出しインスタンスを生成して返す
			return (IFunction) (Class.forName(name).newInstance());
		} catch (Exception e) {
			// 不適切なクラス名を引数にとった場合例外発生
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 実行例
	 * @param args
	 */
	public static void main(String[] args) {
		IFunction f = FunctionFactory
				.createFunction("problem.function.NegativeFunction");
		// -3.0 が表示される．
		System.out.println(f.function(3));
	}
}