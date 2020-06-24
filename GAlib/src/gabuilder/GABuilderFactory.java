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

/**
 * GA Builder �p�̃t�@�N�g���N���X
 * @author mori
 * @version 1.0
 */

public class GABuilderFactory {
	/**
	 * name �ɂ̓p�b�P�[�W�܂Ŋ܂߂��N���X�����L�q�D<br>
	 * �g�p�� GABuilderFactory.createGABuilder("gabuilder.TDGABuilder")
	 * @param name GA �r���_��
	 * @return �w�肳�ꂽ���O�̃N���X�̃C���X�^���X
	 */
	public static IGABuilder createGABuilder(String name) {
		try {
			return (IGABuilder) Class.forName(name).newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	// ���s��
	public static void main(String[] args) {
		IGABuilder ga = GABuilderFactory.createGABuilder("gabuilder.TDGABuilder");
		System.out.println(ga);
	}
}
