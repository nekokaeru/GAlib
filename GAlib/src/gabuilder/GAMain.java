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
 * GA �̎��s��������T���v���N���X
 * @author mori
 * @version 1.0
 */
public class GAMain {
	public static void main(String[] args) {
		// �p���� GA Builder �̎w��. �f�t�H���g�� DefaultGABuilder�D
		String builderName = "gabuilder.DefaultGABuilder";
		// �p�����[�^�z������X�g��
		ArrayList<String> params = new ArrayList<String>(Arrays.asList(args));
		// -gabuilder ���w�肳��Ă��邩���`�F�b�N�D
		int index = params.indexOf("-gabuilder");
		// -gabuilder �����݂��Ă���΂��̎��̕������ builderName �ɂ���D
		if (index >= 0) {
			builderName = (String) params.get(index + 1);
			// -gabuilder �� Director �ɂ͕s�K�Ȃ̂ō��D
			params.remove(index + 1);
			params.remove(index);
		}
		// builderName �������Ƃ��� GABuilderFactory �� GA Builder �����D
		IGABuilder builder = GABuilderFactory.createGABuilder(builderName);
		Director d = new Director(builder);
		d.constract();
		d.setParameter(params);
		// GA �����D
		GABase ga = builder.buildGA();
		// GA �����s�D
		ga.start();
	}

}
