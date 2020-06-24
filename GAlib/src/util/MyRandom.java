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
 * 乱数クラス．
 */
package util;

import java.util.Random;

/**
 * 乱数クラス．GABase では再現性が求められるため，乱数シードを固定した場合と，
 * そうでない場合(現在時刻使用)への対応が必要．Singleton で実装．
 * @author mori
 * @version 1.0
 */
public final class MyRandom {
    /**  
     * 乱数シード. シードを設定していない状態が null
     */
    private static Long seed_ = null;

    /** 
     * 乱数ジェネレータ．プログラム中で一つしか使わないようにする．
     */
    private static Random rand_ = null;

    /**
     * Singleton なのでコンストラクタを private にする. 乱数ジェネレータの取得法は MyRandom.getInstance()
     */
    private MyRandom() {
    }

    /**
     * インスタンス取得メソッド
     * 
     * @return 唯一の乱数オブジェクト
     */
    public static Random getInstance() {
        // 一度だけ乱数オブジェクトを初期化
        if (rand_ == null) {
            if (seed_ == null) {
                // シードが未設定なら現在時刻を利用して設定
                setSeed(System.currentTimeMillis());
                rand_ = new Random(seed_);
            }
            rand_ = new Random(seed_);
        }
        return rand_;
    }

    /**
     * シードを取得．現在時刻を用いた場合も設定されている．
     * @return シードを返す．
     */
    public static Long getSeed() {
        return seed_;
    }

    /**
     * シードの設定．シードは rand_ が null の場合(初期化前)のみ変更可能．
     * @param seed 乱数のシード
     * @return seed がセットできたか？
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
     * 乱数オブジェクトとシードをリセットする．シードの変更等に対応．
     * ただし，これは非常に危険．使用先で参照に古い方が保存されてると無効．
     * 基本的には使わない．使う場合は，かならず getInstance() を使うこと．
     */
    public static void reset() {
        rand_ = null;
        seed_ = null;
    }

    /**
     * 実行例
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println(MyRandom.getInstance().nextInt(100));
        }
    }
}
