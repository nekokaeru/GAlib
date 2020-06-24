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

/*
 * 配列の値を変換する操作用インターフェース
 * Created on 2005/07/08
 * 
 */
package operator.transform;

/**
 * 適応度の double 配列変換用のインターフェース．
 * @author mori
 * @version 1.0
 */
public interface IArrayTransform {
	/**
	 * 個体群情報を用いてスケーリングする.
	 * @param array すべての適応度情報
	 * @return 変換後の適応度情報
	 */
	public double[] transform(double[] array);

	/**
	 * 変換操作名を返す．一般的な線形スケーリングやランクへの変換など．
	 * @return 変換操作名
	 */
	public String getName();

	/**
	 * パラメータの設定関数．
	 * @param params パラメータ列．可変長パラメータを利用．
	 */
	public void setParameter(Object... params);

	/**
	 * パラメータの情報を返す．
	 * @return パラメータの情報文字列
	 */
	public String getParameterInfo();
}
