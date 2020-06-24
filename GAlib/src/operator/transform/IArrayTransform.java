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
 * �z��̒l��ϊ����鑀��p�C���^�[�t�F�[�X
 * Created on 2005/07/08
 * 
 */
package operator.transform;

/**
 * �K���x�� double �z��ϊ��p�̃C���^�[�t�F�[�X�D
 * @author mori
 * @version 1.0
 */
public interface IArrayTransform {
	/**
	 * �̌Q����p���ăX�P�[�����O����.
	 * @param array ���ׂĂ̓K���x���
	 * @return �ϊ���̓K���x���
	 */
	public double[] transform(double[] array);

	/**
	 * �ϊ����얼��Ԃ��D��ʓI�Ȑ��`�X�P�[�����O�⃉���N�ւ̕ϊ��ȂǁD
	 * @return �ϊ����얼
	 */
	public String getName();

	/**
	 * �p�����[�^�̐ݒ�֐��D
	 * @param params �p�����[�^��D�ϒ��p�����[�^�𗘗p�D
	 */
	public void setParameter(Object... params);

	/**
	 * �p�����[�^�̏���Ԃ��D
	 * @return �p�����[�^�̏�񕶎���
	 */
	public String getParameterInfo();
}
