package com.github.aramis;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import com.github.aramis.renderinstruction.DecoratorRenderInstruction;
import com.github.aramis.renderinstruction.RenderInstruction;
import com.github.aramis.renderinstruction.TemplateRenderInstruction;
import com.github.aramis.renderinstruction.TextRenderInstruction;


public class ParserTest {

	
	@Test
	public void testTextOnly() throws Exception{
		
		String test = "asdfasdf asdf";
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);
		TemplateBuilder tb = new TemplateBuilder( "test", renderEngine, cp, null);
		Parser smp = new Parser( tb);
		
		smp.parse( "test", new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	@Test
	public void testExpression() throws Exception{
		
		String test = "{{test}}";
		
		TemplateBuilder tb = new TemplateBuilder( "test", null, null, null);
		Parser smp = new Parser( tb);
		
		smp.parse( "test", new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	
	@Test
	public void testRenderTag() throws Exception{
		
		String test = "xxxx [[ 123 | test | count:4, index:2 ]] yyyy";
		
		TemplateBuilder tb = new TemplateBuilder( "test1", null, null, null);
		Parser smp = new Parser( tb);
		
		smp.parse( "test", new ByteArrayInputStream(test.getBytes()));
	
		Template template = tb.getTemplate();
		
		assertEquals( 3, template.getRenderInsturctions().size());
		
		RenderInstruction ri = template.getRenderInsturctions().get(1);
		
		assertEquals( TemplateRenderInstruction.class, ri.getClass());
	}
		
	
	
	@Test
	public void testDecorator() throws Exception{
		
		
		TemplateFactory tf = new TemplateFactory( null, null);
		Template template = tf.getTemplate("/templates/decorate-me.art");

		assertEquals( TextRenderInstruction.class, 		template.getRenderInsturctions().get(0).getClass());
		assertEquals( DecoratorRenderInstruction.class, template.getRenderInsturctions().get(1).getClass());
		assertEquals( TextRenderInstruction.class, 		template.getRenderInsturctions().get(2).getClass());
		assertEquals( DecoratorRenderInstruction.class, template.getRenderInsturctions().get(3).getClass());
		assertEquals( TextRenderInstruction.class, 		template.getRenderInsturctions().get(4).getClass());
		
	}
	
	
	
	@Test
	public void testPartial() throws Exception {
		
		String test = "xxxx {{> /templates/text.art }}  yyyy";
		
		TemplateFactory tf = new TemplateFactory(null,null);
		TemplateBuilder tb = new TemplateBuilder( "test1", null, null, tf);
		Parser smp = new Parser( tb);
		
		smp.parse( "test", new ByteArrayInputStream(test.getBytes()));		
	
		Template template = tb.getTemplate();

		System.out.println(template.getRenderInsturctions());
		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(0).getClass());
		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(1).getClass());
		assertEquals( "TextRenderInstruction(text123)", template.getRenderInsturctions().get(1).toString());
		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(2).getClass());
		
		
	}
	
	
	
}