package com.github.aramis;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

public class DecoratorTest {

	
	
	@Test
	public void testDecorator() throws Exception{
		
		TemplateFactory templateFactory = new TemplateFactory();
		
		Template template = templateFactory.getTemplate( "/templates/decorate-me.art");
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