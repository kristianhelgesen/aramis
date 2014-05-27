package com.github.aramis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.junit.Test;

import com.github.aramis.model.Article;
import com.github.aramis.model.Article.ImageReference;
import com.github.aramis.model.ArticleB;
import com.github.aramis.model.Image2;
import com.github.aramis.model.Image3;
import com.github.aramis.model.Image3Reference;
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
		renderEngine.renderContent( baos, new Image2(), new ImageReference(), "", new HashMap<String,Object>());
		
		assertEquals( "<img id='image1' src='http://images/image1' />", baos.toString().trim());
	}
	
	@Test
	public void testIncludeWithControllerAndReference() throws Exception {
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.renderContent( baos, new Image3(), new Image3Reference(), "", new HashMap<String,Object>());
		
		assertTrue( baos.toString().contains("Image caption"));
	}

	@Test
	public void testIncludeWithControllerAndValue() throws Exception {
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.renderContent( baos, new ArticleB(), null, "", new HashMap<String,Object>());
		
		String result = baos.toString().trim();
		result = result.substring(result.indexOf("src='")+5);
		result = result.substring(0, result.indexOf("'"));
		
		assertEquals( "http://images/image1?w=200", result);
	}
	
	
	
}