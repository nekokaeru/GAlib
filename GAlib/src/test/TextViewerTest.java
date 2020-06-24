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

package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import gabuilder.DefaultGABuilder;
import gabuilder.Director;
import gabuilder.GABase;
import viewer.TextViewer;
import junit.framework.TestCase;

/**
 * @author mori
 * @version 1.0
 */
public class TextViewerTest extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TextViewerTest.class);
	}

	/**
	 * Constructor for TextViewerTest.
	 * @param name
	 */
	public TextViewerTest(String name) {
		super(name);
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

	/*
	 * Test method for 'viewer.TextViewer.begin()'
	 */
	public void testBegin() {
//		TextViewer viewer = new TextViewer();
//		DefaultGABuilder builder = new DefaultGABuilder();
//		Director d = new Director(builder);
//		d.constract();
//		viewer.setGA(builder.buildGA());
//		viewer.begin();
	}

	/*
	 * Test method for 'viewer.TextViewer.end()'
	 */
	public void testEnd() {

	}

	/*
	 * Test method for 'viewer.TextViewer.update()'
	 */
	public void testUpdate() {

	}

	/*
	 * Test method for 'viewer.TextViewer.printPopulation(Population)'
	 */
	public void testPrintPopulation() {

	}

	/*
	 * Test method for 'viewer.TextViewer.printElite()'
	 */
	public void testPrintElite() {

	}

	/*
	 * Test method for 'viewer.TextViewer.getOut()'
	 */
	public void testGetOut() {
		TextViewer viewer = new TextViewer();
		assertEquals(viewer.getOut(),System.out);
		viewer.setOut(null);
		viewer.setFilename("__test__.log");
		assertTrue(viewer.getOut() != System.out);
	}

	/*
	 * Test method for 'viewer.TextViewer.setOut(PrintStream)'
	 */
	public void testSetOut() {
		TextViewer viewer = new TextViewer();
		viewer.setOut(System.out);
		assertEquals(viewer.getOut(),System.out);
		PrintStream p = null;
		try {
			p = new PrintStream(new File("__test__.log"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		viewer.setOut(p);
		assertEquals(viewer.getOut(),p);
	}

	/*
	 * Test method for 'viewer.TextViewer.getFilename()'
	 */
	public void testGetFilename() {
		TextViewer viewer = new TextViewer();
		assertEquals(viewer.getFilename(),"");
		viewer.setFilename("__test__.log");
		assertEquals(viewer.getFilename(),"__test__.log");		
	}

	/*
	 * Test method for 'viewer.TextViewer.setFilename(String)'
	 */
	public void testSetFilename() {
		TextViewer viewer = new TextViewer();
		assertEquals(viewer.getFilename(),"");
		viewer.setFilename("__test__.log");
		assertEquals(viewer.getFilename(),"__test__.log");		

	}

	/*
	 * Test method for 'viewer.TextViewer.getGA()'
	 */
	public void testGetGA() {
		TextViewer viewer = new TextViewer();
		DefaultGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		d.constract();
		GABase ga = builder.buildGA();
		assertEquals(viewer.getGA(),null);
		viewer.setGA(ga);
		assertEquals(viewer.getGA(),ga);
	}

	/*
	 * Test method for 'viewer.TextViewer.setGA(GABase)'
	 */
	public void testSetGA() {
		TextViewer viewer = new TextViewer();
		DefaultGABuilder builder = new DefaultGABuilder();
		Director d = new Director(builder);
		d.constract();
		GABase ga = builder.buildGA();
		assertEquals(viewer.getGA(),null);
		viewer.setGA(ga);
		assertEquals(viewer.getGA(),ga);
	}

	/*
	 * Test method for 'viewer.TextViewer.getName()'
	 */
	public void testGetName() {
		TextViewer viewer = new TextViewer();
		assertEquals(viewer.getName(),"TextViewer");
	}

	/*
	 * Test method for 'viewer.TextViewer.getPrintLevel()'
	 */
	public void testGetPrintLevel() {
		TextViewer viewer = new TextViewer();
		assertEquals(viewer.getPrintLevel(),1);
		viewer.setPrintLevel(2);
		assertEquals(viewer.getPrintLevel(),2);		
		viewer.setPrintLevel(3);
		assertEquals(viewer.getPrintLevel(),3);		
	}

	/*
	 * Test method for 'viewer.TextViewer.setPrintLevel(int)'
	 */
	public void testSetPrintLevel() {
		TextViewer viewer = new TextViewer();
		assertEquals(viewer.getPrintLevel(),1);
		viewer.setPrintLevel(2);
		assertEquals(viewer.getPrintLevel(),2);		
		viewer.setPrintLevel(3);
		assertEquals(viewer.getPrintLevel(),3);		

	}

}
