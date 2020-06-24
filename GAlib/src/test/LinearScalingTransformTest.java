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

import java.util.Arrays;

import operator.transform.LinearScalingTransform;

import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */
public class LinearScalingTransformTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(LinearScalingTransformTest.class);
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
     * Constructor for LinearScalingTransformTest.
     * @param arg0
     */
    public LinearScalingTransformTest(String arg0) {
        super(arg0);
    }

    public void testTransform() {
        LinearScalingTransform lsTrans = new LinearScalingTransform();
        assertEquals(lsTrans.transform((double[])null),null);
        double[] d01=new double[0];
        assertEquals(lsTrans.transform(d01).length,d01.length);
        d01=new double[1];
        d01[0]=12.34;
        assertEquals(lsTrans.transform(d01)[0],d01[0]);        
        double [] a={1,2,3,4,5};
        double [] b;
        b = lsTrans.transform(a); // デフォルトは 3-1 
        assertTrue(b[0]-lsTrans.getBottomValue()==0);
        assertTrue(b[4]-lsTrans.getTopValue()==0);        
        Arrays.fill(a,3);
        b = lsTrans.transform(a);
        for (int i = 0; i < b.length; i++) {
            assertEquals((int)b[i],3);        
        }        

        Arrays.fill(a,-1);
        b = lsTrans.transform(a);
        for (int i = 0; i < b.length; i++) {
            assertEquals((int)b[i],-1);        
        }        
        
        Arrays.fill(a,0);
        b = lsTrans.transform(a);
        for (int i = 0; i < b.length; i++) {
            assertEquals((int)b[i],0);        
        }
        a[4]=1;
        b = lsTrans.transform(a);
        assertEquals((int)b[0],1);                
        assertEquals((int)b[4],3);

        a[0]=-10; // min
        a[1]=-3.5;
        a[2]=3.2;
        a[3]=18; //max
        a[4]=10.2;
        lsTrans.setBottomValue(1.5);
        lsTrans.setTopValue(10);
        b = lsTrans.transform(a);
        assertTrue(b[0]-lsTrans.getBottomValue()==0);
        assertTrue(b[3]-lsTrans.getTopValue()==0);
        
        lsTrans.setTopValue(-10);
        b = lsTrans.transform(a);
        assertTrue(b[0]-lsTrans.getBottomValue()<1e-15);
        assertTrue(b[3]-lsTrans.getTopValue()<1e-15);        
        
        lsTrans.setBottomValue(-12);
        b = lsTrans.transform(a);
        assertTrue(b[0]-lsTrans.getBottomValue()==0);
        assertTrue(b[3]-lsTrans.getTopValue()==0);             

        lsTrans.setTopValue(1);
        lsTrans.setBottomValue(1);
        b = lsTrans.transform(a);
        assertTrue(b[0]-lsTrans.getBottomValue()==0);
        assertTrue(b[3]-lsTrans.getTopValue()==0);
        
        a[0]=-1;
        a[1]=-2;
        a[2]=-3;
        a[3]=-4;
        a[4]=-5;
        lsTrans.setBottomValue(1.5);
        lsTrans.setTopValue(10);
        b = lsTrans.transform(a);
        assertTrue(b[4]-lsTrans.getBottomValue()==0);
        assertTrue(b[0]-lsTrans.getTopValue()==0);        

        //マイナスで逆転
        lsTrans.setBottomValue(-3);
        lsTrans.setTopValue(-10);
        b = lsTrans.transform(a);
        assertTrue(b[4]-lsTrans.getBottomValue()==0);
        assertTrue(b[0]-lsTrans.getTopValue()==0);                
    }

    /**
     * @see LinearScalingTransform#getName()
     */
    public void testGetName(){
    	LinearScalingTransform target = new LinearScalingTransform();
    	assertEquals("LinearScalingTransform", target.getName());
    }
    
    /**
     * @see LinearScalingTransform#getParameterInfo()
     */
    public void testGetParameterInfo(){
    	LinearScalingTransform target = new LinearScalingTransform();
    	// bottomValue_ を 10 , topValue_ を 1400 に設定
    	target.setBottomValue(10);
    	target.setTopValue(1400);
    	assertEquals("TOP_VALUE:1400.0, BOTTOM_VALUE:10.0", target.getParameterInfo());
    }
    
    /**
     * @see LinearScalingTransform#toString()
     */
    public void testToString(){
    	LinearScalingTransform target = new LinearScalingTransform();
    	// bottomValue_ を 10 , topValue_ を 1400 に設定
    	target.setBottomValue(10);
    	target.setTopValue(1400);
    	assertEquals("LinearScalingTransform{TOP_VALUE:1400.0, BOTTOM_VALUE:10.0}", target.toString());
    }
}