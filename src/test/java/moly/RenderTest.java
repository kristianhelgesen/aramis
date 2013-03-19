package moly;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import moly.ctrl.Image2Controller;
import moly.model.Article;
import moly.model.Image2;
import moly.model.Model1;

import org.junit.Test;
import org.mvel2.MVEL;


public class RenderTest {

	
	
	@Test
	public void testExpression() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		
		
		Model1 model = new Model1();
		model.setTitle( "TITLE");
		model.setBody("BODY");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.render( baos, model, "", new HashMap<String,Object>());
		
		assertTrue( baos.toString().contains("TITLE"));
		assertTrue( baos.toString().contains("BODY"));
		
	}
	
	@Test
	public void testInclude() throws Exception {
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.render( baos, new Article(), "", new HashMap<String,Object>());

		assertTrue( baos.toString().contains("Mock article title"));		
		assertTrue( baos.toString().contains("<img src='http://localhost/image234' width='255'/>"));		
		assertTrue( baos.toString().contains("Mock article body"));		
	}

	
	@Test
	public void testIncludeWithController() throws Exception {
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly.ctrl","/moly/templates");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderEngine.render( baos, new Image2(), "", new HashMap<String,Object>());
		
		System.out.println(baos.toString());
		
		assertEquals( "<img id='image1' src='http://images/image1' />", baos.toString().trim());
	
	}

	public static void main(String[] args) {
		Image2Controller ctrl = new Image2Controller();
		ctrl.setContent( new Image2());
		
		Object compiledExpression = MVEL.compileExpression("src"); 
		System.out.println( MVEL.executeExpression( compiledExpression, ctrl));
		

	}
	
	
}