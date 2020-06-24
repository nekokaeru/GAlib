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

import java.util.ArrayList;
import java.util.Arrays;

/**
 * GA の実行例を示すサンプルクラス
 * @author mori
 * @version 1.0
 */
public class GAMain {
	public static void main(String[] args) {
		// 用いる GA Builder の指定. デフォルトは DefaultGABuilder．
		String builderName = "gabuilder.DefaultGABuilder";
		// パラメータ配列をリスト化
		ArrayList<String> params = new ArrayList<String>(Arrays.asList(args));
		// -gabuilder が指定されているかをチェック．
		int index = params.indexOf("-gabuilder");
		// -gabuilder が存在していればその次の文字列を builderName にする．
		if (index >= 0) {
			builderName = (String) params.get(index + 1);
			// -gabuilder は Director には不適なので削る．
			params.remove(index + 1);
			params.remove(index);
		}
		// builderName を引数として GABuilderFactory で GA Builder を作る．
		IGABuilder builder = GABuilderFactory.createGABuilder(builderName);
		Director d = new Director(builder);
		d.constract();
		d.setParameter(params);
		// GA を作る．
		GABase ga = builder.buildGA();
		// GA を実行．
		ga.start();
	}

}
