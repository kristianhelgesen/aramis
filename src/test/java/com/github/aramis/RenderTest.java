package com.github.aramis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


import org.junit.Test;
import org.mvel2.MVEL;

import com.github.aramis.ContentProvider;
import com.github.aramis.RenderEngine;
import com.github.aramis.model.Article;
import com.github.aramis.model.Image2;
import com.github.aramis.model.Image2Controller;
import com.github.aramis.model.Model1;


public class RenderTest {

	
	
	@Test
	public void testExpression() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);
		
		Model1 model = new Model1();
		model.setTitle( "TITLE");
		model.setBody("BODY");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.renderContent( baos, model, null, "", new HashMap<String,Object>());
		
		assertTrue( baos.toString().contains("TITLE"));
		assertTrue( baos.toString().contains("BODY"));
		
	}
	
	@Test
	public void testInclude() throws Exception {
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.renderContent( baos, new Article(), null, "", new HashMap<String,Object>());

		System.out.println(baos.toString());

		assertTrue( baos.toString().contains("Mock article title"));		
		assertTrue( baos.toString().contains("<img src='http://localhost/image234' width='255'/>"));		
		assertTrue( baos.toString().contains("Image caption"));		
		assertTrue( baos.toString().contains("Mock article body"));		
	}

	
	@Test
	public void testIncludeWithController() throws Exception {
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.renderContent( baos, new Image2(), null, "", new HashMap<String,Object>());
		
		assertEquals( "<img id='image1' src='http://images/image1' />", baos.toString().trim());
	
	}
	

	public static void main(String[] args) {
		Image2Controller ctrl = new Image2Controller();
		ctrl.setContent( new Image2());
		
		Object compiledExpression = MVEL.compileExpression("src"); 
		System.out.println( MVEL.executeExpression( compiledExpression, ctrl));
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);

		System.out.println("start");
		for( int i=0; i<100000; i++){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			renderEngine.renderContent( baos, new Image2(), null, "", new HashMap<String,Object>());
		}
		System.out.println("done");
		

	}
	
	
}