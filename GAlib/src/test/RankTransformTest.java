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
 * Created on 2005/07/08
 * 
 */
package test;

import java.util.ArrayList;

import operator.transform.RankTransform;

import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */
public class RankTransformTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RankTransformTest.class);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Constructor for RankTransformTest.
     * @param arg0
     */
    public RankTransformTest(String arg0) {
        super(arg0);
    }

    public void testTransform() {
        RankTransform rankTrans = new RankTransform();
        assertEquals(rankTrans.transform((double[])null),null);
        double[] d01=new double[0];
        assertEquals(rankTrans.transform(d01).length,d01.length);
        d01=new double[1];
        d01[0]=12.34;
        assertEquals(rankTrans.transform(d01)[0],d01[0]);                
        double[] a = new double[5];
        ArrayList<Integer> list = new ArrayList<Integer>();
        // 2 と 3 は同じ値
        a[0] = 3; a[1] = -1; a[2] = 4; a[3] = 4; a[4]=5;
        double[] rank = rankTrans.transform(a);
        
        assertTrue(rank[0]==4);
        assertTrue(rank[1]==5);        
        assertTrue(rank[2]==2);
        assertTrue(rank[3]==2);                
        assertTrue(rank[4]==1);
        a = new double[100];
        list.clear();
        for (int i = 0; i < a.length; i++) {
            a[i]=i+0.1;
            list.add(new Integer(i));
        }
        rank = rankTrans.transform(a);
        for (int i = 0; i < rank.length; i++) {
            assertTrue(rank[i]==100-i);
        }
    }
    
    /**
     * @see RankTransform#getName()
     */
    public void testGetName(){
    	RankTransform target = new RankTransform();
    	assertEquals("RankTransform", target.getName());
    }
    
    /**
     * @see RankTransform#getParameterInfo()
     */
    public void testGetParameterInfo(){
    	RankTransform target = new RankTransform();
    	// パラメータはない
    	assertEquals("", target.getParameterInfo());
    }
    
    /**
     * @see RankTransform#toString()
     */
    public void testToString(){
    	RankTransform target = new RankTransform();
    	assertEquals("RankTransform{}", target.toString());
    }
}
