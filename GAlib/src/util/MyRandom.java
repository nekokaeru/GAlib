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
 * Created on 2005/05/11
 * �����N���X�D
 */
package util;

import java.util.Random;

/**
 * �����N���X�DGABase �ł͍Č��������߂��邽�߁C�����V�[�h���Œ肵���ꍇ�ƁC
 * �����łȂ��ꍇ(���ݎ����g�p)�ւ̑Ή����K�v�DSingleton �Ŏ����D
 * @author mori
 * @version 1.0
 */
public final class MyRandom {
    /**  
     * �����V�[�h. �V�[�h��ݒ肵�Ă��Ȃ���Ԃ� null
     */
    private static Long seed_ = null;

    /** 
     * �����W�F�l���[�^�D�v���O�������ň�����g��Ȃ��悤�ɂ���D
     */
    private static Random rand_ = null;

    /**
     * Singleton �Ȃ̂ŃR���X�g���N�^�� private �ɂ���. �����W�F�l���[�^�̎擾�@�� MyRandom.getInstance()
     */
    private MyRandom() {
    }

    /**
     * �C���X�^���X�擾���\�b�h
     * 
     * @return �B��̗����I�u�W�F�N�g
     */
    public static Random getInstance() {
        // ��x���������I�u�W�F�N�g��������
        if (rand_ == null) {
            if (seed_ == null) {
                // �V�[�h�����ݒ�Ȃ猻�ݎ����𗘗p���Đݒ�
                setSeed(System.currentTimeMillis());
                rand_ = new Random(seed_);
            }
            rand_ = new Random(seed_);
        }
        return rand_;
    }

    /**
     * �V�[�h���擾�D���ݎ�����p�����ꍇ���ݒ肳��Ă���D
     * @return �V�[�h��Ԃ��D
     */
    public static Long getSeed() {
        return seed_;
    }

    /**
     * �V�[�h�̐ݒ�D�V�[�h�� rand_ �� null �̏ꍇ(�������O)�̂ݕύX�\�D
     * @param seed �����̃V�[�h
     * @return seed ���Z�b�g�ł������H
     * @see MyRandom#reset()
     */
    public static boolean setSeed(long seed) {
        if (rand_ != null) {
            return false;
        }
        seed_ = seed;
        return true;
    }

    /**
     * �����I�u�W�F�N�g�ƃV�[�h�����Z�b�g����D�V�[�h�̕ύX���ɑΉ��D
     * �������C����͔��Ɋ댯�D�g�p��ŎQ�ƂɌÂ������ۑ�����Ă�Ɩ����D
     * ��{�I�ɂ͎g��Ȃ��D�g���ꍇ�́C���Ȃ炸 getInstance() ���g�����ƁD
     */
    public static void reset() {
        rand_ = null;
        seed_ = null;
    }

    /**
     * ���s��
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println(MyRandom.getInstance().nextInt(100));
        }
    }
}
