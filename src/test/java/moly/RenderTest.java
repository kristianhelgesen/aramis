package moly;

import java.util.HashMap;

import org.junit.Test;


public class RenderTest {

	
	
	@Test
	public void testExpression() throws Exception{
	
		RenderEngine renderEngine = new RenderEngine("moly","moly.templates");
		
		renderEngine.render( System.out, new Model1(), "test", new HashMap<String,Object>());
		
	}
	
	
	
	
}