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
 * Created on 2005/07/13
 * 
 */
package test;

import java.util.Random;

import junit.framework.TestCase;
import util.MyRandom;

/**
 * @author mori
 * @version 1.0
 */
public class MyRandomTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(MyRandomTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		MyRandom.reset();		
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		MyRandom.reset();
	}

	/**
	 * Constructor for MyRandomTest.
	 * 
	 * @param arg0
	 */
	public MyRandomTest(String arg0) {
		super(arg0);
	}

	public void testGetInstance() {
		long i, j;
		Random r = MyRandom.getInstance();
		r = MyRandom.getInstance();
		i = r.nextLong();
		try {
			// �����̏������͎��ԊԊu���Z������ƌ��ʂ������ɂȂ邱�Ƃ�����D
			Thread.sleep(50);
		} catch (Exception ex) {

		}
		MyRandom.reset();
		r = MyRandom.getInstance();
		j = r.nextLong();
		// �����͋��R i==j �ɂȂ邱�Ƃ���D�m���͂قƂ��0�����ǁD
		assertTrue(i != j);
	}

public void testSetSeed() {
        int i,j;
        boolean b1 = MyRandom.setSeed(0); // ����
        assertTrue(b1);
        assertEquals(0,MyRandom.getSeed().longValue());
        Random r = MyRandom.getInstance();
        //������������̓V�[�h�̃Z�b�g�͂ł��Ȃ��D
        boolean b2 = MyRandom.setSeed(1); // ���s
        assertFalse(b2);
        r = MyRandom.getInstance();
        i=r.nextInt();
        
        MyRandom.reset();
        MyRandom.setSeed(0);
        r = MyRandom.getInstance();        
        j=r.nextInt();
        assertEquals(i,j);
        
        MyRandom.reset();
        MyRandom.setSeed(3);
        assertEquals(3,MyRandom.getSeed().longValue());
        //getInstance �O�Ȃ�V�[�h�͕ύX��
        MyRandom.setSeed(4);
        assertEquals(4,MyRandom.getSeed().longValue());
        // �V�[�h�͂S�Ŋm��D
        MyRandom.getInstance();
        //�ȉ��͎��s�D�����V�[�h�� reset �ȊO�ł͕ύX�ł��Ȃ��D
        boolean b3 = MyRandom.setSeed(5);
        assertFalse(b3);
        assertEquals(4,MyRandom.getSeed().longValue());
        
        
    }}
