package com.github.aramis;

import java.io.ByteArrayOutputStream;

import org.junit.Test;

import com.github.aramis.ContentProvider;
import com.github.aramis.Context;
import com.github.aramis.RenderEngine;
import com.github.aramis.Template;
import com.github.aramis.TemplateFactory;

import static org.hamcrest.Matchers.*; 
import static org.junit.Assert.*;

public class DecoratorTest {

	
	
	@Test
	public void testDecorator() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "com.github.aramis","/templates");
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp, "/templates");
		
		Template template = templateFactory.getTemplate( "decorate-me.template");
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