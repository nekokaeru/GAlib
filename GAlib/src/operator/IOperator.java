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

package operator;

import population.Population;

/**
 * ��`���Z�q�p�C���^�[�t�F�[�X�D
 * @author mori
 * @version 1.0
 */
public interface IOperator {
	/**
	 * �ϒ������𗘗p�����z��ŏ�����.
	 * @param params �ϒ��p�����[�^�ɂ��p�����[�^�Z�b�g
	 */
	public void setParameter(Object... params);

	/**
	 * �K�p�O��� pop �̓��ꐫ�͕ۏ؂��Ȃ��D�����̉��Z�� pop �𒼐ڏ��������邽��<br>
	 * �K�v�Ȃ�ΓK�p�O�ɕۑ����K�v�D
	 * @param pop �̌Q
	 */
	public void apply(Population pop);

	/**
	 * �I�y���[�^����Ԃ��D
	 * @return �I�y���[�^��
	 */
	public String getName();

	/**
	 * �p�����[�^�̏���Ԃ��D��F TOURNAMENT_SIZE:3
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo();
}
