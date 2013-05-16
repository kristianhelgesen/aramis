package com.github.aramis;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;


import org.junit.Test;

import com.github.aramis.ContentProvider;
import com.github.aramis.Context;
import com.github.aramis.RenderEngine;
import com.github.aramis.Template;
import com.github.aramis.TemplateFactory;
import com.github.aramis.model.Model1;

public class SectionTest {

	
	
	@Test
	public void testBooleanSection() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp);
		Template template = templateFactory.getTemplate( "/templates/sectiontest.art");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Context context = new Context();
		context.getParameters().put("trueBool", Boolean.TRUE);
		context.getParameters().put("falseBool", Boolean.FALSE);
		
		template.apply( baos, context);
		assertThat( baos.toString(), containsString("TRUESECTION"));
		assertThat( baos.toString(), not(containsString("FALSESECTION")));
		assertThat( baos.toString(), containsString("INVSELECTION"));
		
		
	}

	@Test
	public void testBooleanNestedSection() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp);
		Template template = templateFactory.getTemplate( "/templates/sectionnestingtest.art");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Context context = new Context();
		context.getParameters().put("trueBool", Boolean.TRUE);
		context.getParameters().put("falseBool", Boolean.FALSE);
		
		template.apply( baos, context);
		assertThat( baos.toString(), containsString("TRUESECTION1"));
		assertThat( baos.toString(), containsString("TRUESECTION2"));
		assertThat( baos.toString(), not(containsString("FALSESECTION")));
		
	}
	
	@Test
	public void testIterateSection() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp);
		Template template = templateFactory.getTemplate( "/templates/iteration.art");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Context context = new Context();
		List<Model1> list = new LinkedList<Model1>();
		context.getParameters().put("list", list);
		list.add( new Model1().setTitle("T1"));
		list.add( new Model1().setTitle("T2"));
		list.add( new Model1().setTitle("T3"));
		
		template.apply( baos, context);
		assertThat( baos.toString(), containsString("T1"));
		assertThat( baos.toString(), containsString("T2"));
		assertThat( baos.toString(), containsString("T3"));
		
	}
	
}