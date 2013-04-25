package moly;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import static org.hamcrest.Matchers.*; 
import static org.junit.Assert.*;

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
		
		assertThat( baos.toString(), containsString("AAA"));
		assertThat( baos.toString(), containsString("S1"));
		assertThat( baos.toString(), containsString("BBB"));
		assertThat( baos.toString(), containsString("S2"));
		assertThat( baos.toString(), containsString("CCC"));
		assertThat( baos.toString(), not(containsString("ignore")));
		
	}
	
	
}