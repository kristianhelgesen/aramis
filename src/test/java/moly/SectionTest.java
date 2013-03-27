package moly;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

import moly.model.Model1;

import org.junit.Test;

public class SectionTest {

	
	
	@Test
	public void testBooleanSection() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp, "/moly/templates");
		Template template = templateFactory.getTemplate( "sectiontest.moly");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		Context context = new Context();
		context.getParameters().put("trueBool", Boolean.TRUE);
		context.getParameters().put("falseBool", Boolean.FALSE);
		
		template.apply( baos, context);
		assertThat( baos.toString(), containsString("TRUESECTION"));
		assertThat( baos.toString(), not(containsString("FALSESECTION")));
		
	}

	@Test
	public void testBooleanNestedSection() throws Exception{
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp, "/moly/templates");
		Template template = templateFactory.getTemplate( "sectionnestingtest.moly");
		
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
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		
		TemplateFactory templateFactory = new TemplateFactory( renderEngine, cp, "/moly/templates");
		Template template = templateFactory.getTemplate( "iteration.moly");
		
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