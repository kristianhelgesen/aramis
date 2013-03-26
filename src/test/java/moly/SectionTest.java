package moly;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import static org.hamcrest.Matchers.*; 
import static org.junit.Assert.*;

public class SectionTest {

	
	
	@Test
	public void testBooleanSection() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp, "/moly/templates");
		Template template = templateFactory.getTemplate( "sectiontest.moly");
		
		System.out.println(template.getRenderInsturctions());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Context context = new Context();
		context.getParameters().put("trueBool", Boolean.TRUE);
		context.getParameters().put("falseBool", Boolean.FALSE);
		
		template.apply( baos, context);
		assertThat( baos.toString(), containsString("TRUESECTION"));
		assertThat( baos.toString(), not(containsString("FALSESECTION")));
		
	}
	
	
}