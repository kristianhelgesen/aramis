package moly;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import moly.model.Model1;

import org.junit.Test;


public class DecoratorTest {

	
	
	@Test
	public void testDecorator() throws Exception{
		
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
	
	
}