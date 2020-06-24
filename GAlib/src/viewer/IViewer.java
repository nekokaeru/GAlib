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
 * �r���[���p�̃C���^�[�t�F�[�X.
 * @author mori
 * @version 1.0
 */
public interface IViewer {
	/**
	 * �J�n���̕\��
	 */
	public void begin();

	/**
	 * �I�����̕\��
	 */
	public void end();

	/**
	 * �\���̍X�V
	 * @param info �t�����
	 */
	public void update(String info);

	/**
	 * ���O��Ԃ�
	 */
	public String getName();

	/**
	 * GABase �ւ̎Q�Ƃ̐ݒ�.
	 */
	public void setGA(GABase ga);

	/**
	 * GABase �ւ̎Q�Ƃ�Ԃ�.
	 */
	public GABase getGA();

	/**
	 * �\�����x���̐ݒ�D
	 */
	public void setPrintLevel(int printLevel);

	/**
	 * �\�����x���𓾂�D
	 * @return �\�����x��
	 */
	public int getPrintLevel();

	/**
	 * �o�̓t�@�C������Ԃ�.
	 * @return �o�̓t�@�C����
	 */
	public String getFilename();

	/**
	 * �o�̓t�@�C������ݒ�.
	 * @param filename �o�̓t�@�C����
	 */
	public void setFilename(String filename);
}
