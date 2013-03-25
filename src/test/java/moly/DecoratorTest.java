package moly;

import java.io.ByteArrayOutputStream;

import org.junit.Test;


public class DecoratorTest {

	
	
	@Test
	public void testDecorator() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp, "/moly/templates");
		
		Template template = templateFactory.getTemplate( "decorate-me.moly");
		Context context = new Context();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		template.apply( baos, context);
		
		System.out.println( baos.toString());
		
	}
	
	
}