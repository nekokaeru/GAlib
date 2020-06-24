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

package test;


/**
 * テスト用プロパティファイル管理クラス．テスト時の各種パラメータを読み込む．
 * @author mori
 * @version 1.0
 */
public class TestParameters {
	/** 2項テスト時に何σまで調べるか？ */
	public static double SIGMA_MARGIN=3;
	/** 乱数シード */	
	public static long SEED=0;
	/** double 比較時の許容誤差 */
	public static double DOUBLE_ERROR=10e-9;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.err.println(TestParameters.class.hashCode());
		System.out.println(TestParameters.SIGMA_MARGIN);
	}
}
