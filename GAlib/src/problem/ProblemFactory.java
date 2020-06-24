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
 * ���N���X���쐬����t�@�N�g���N���X�D String �ŃN���X�����w�肷��D �w�肵���N���X���̃N���X�������Ɨ�O�����D
 * @author mori
 * @version 1.0
 */
public class ProblemFactory {
	/**
	 * String �ŃN���X�����w�肵�Ė��N���X��Ԃ��D �w�肵���N���X���̃N���X�������Ɨ�O�����D
	 * @param name ���N���X�̃N���X��
	 * @return ���N���X
	 */
	public static IProblem createProblem(String name) {
		try {
			return (IProblem) (Class.forName(name).newInstance());
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * ���s��
	 * @param args
	 */
	public static void main(String[] args) {
		IProblem p = ProblemFactory.createProblem("problem.BitCountProblem");
		Number[] array = { 1, 1, 0, 0, 1 };
		// 3.0���\�������D
		System.out.println(p.getFitness(array));
	}
}
