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
 * �w�肳�ꂽ���O�̊֐���\���N���X�̃C���X�^���X�𐶐�����N���X
 * @author mori
 * @version 1.0
 */
public class FunctionFactory {

	/**
	 * �w�肳�ꂽ���O�̊֐��N���X�̃C���X�^���X�𐶐�.
	 * ���݂��Ȃ��N���X����IFunction�C���^�[�t�F�[�X���������Ă��Ȃ��N���X���������ɂƂ�Ɨ�O����.
	 * @param name �����������֐��̃N���X��
	 * @return �w�肳�ꂽ���O�̃N���X�̃C���X�^���X��Ԃ�
	 */
	public static IFunction createFunction(String name) {
		try {
			// �w�肳�ꂽ���O�̃N���X���Ăяo���C���X�^���X�𐶐����ĕԂ�
			return (IFunction) (Class.forName(name).newInstance());
		} catch (Exception e) {
			// �s�K�؂ȃN���X���������ɂƂ����ꍇ��O����
			throw new IllegalArgumentException();
		}
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		IFunction f = FunctionFactory
				.createFunction("problem.function.NegativeFunction");
		// -3.0 ���\�������D
		System.out.println(f.function(3));
	}
}