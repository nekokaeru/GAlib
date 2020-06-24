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

package viewer;

import gabuilder.GABase;

/**
 * ビューワ用のインターフェース.
 * @author mori
 * @version 1.0
 */
public interface IViewer {
	/**
	 * 開始時の表示
	 */
	public void begin();

	/**
	 * 終了時の表示
	 */
	public void end();

	/**
	 * 表示の更新
	 * @param info 付加情報
	 */
	public void update(String info);

	/**
	 * 名前を返す
	 */
	public String getName();

	/**
	 * GABase への参照の設定.
	 */
	public void setGA(GABase ga);

	/**
	 * GABase への参照を返す.
	 */
	public GABase getGA();

	/**
	 * 表示レベルの設定．
	 */
	public void setPrintLevel(int printLevel);

	/**
	 * 表示レベルを得る．
	 * @return 表示レベル
	 */
	public int getPrintLevel();

	/**
	 * 出力ファイル名を返す.
	 * @return 出力ファイル名
	 */
	public String getFilename();

	/**
	 * 出力ファイル名を設定.
	 * @param filename 出力ファイル名
	 */
	public void setFilename(String filename);
}
