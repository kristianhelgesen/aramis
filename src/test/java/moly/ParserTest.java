package moly;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import moly.renderinstruction.DecoratorRenderInstruction;
import moly.renderinstruction.RenderInstruction;
import moly.renderinstruction.TemplateRenderInstruction;
import moly.renderinstruction.TextRenderInstruction;

import org.junit.Test;


public class ParserTest {

	
	@Test
	public void testTextOnly() throws Exception{
		
		String test = "asdfasdf asdf";
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		TemplateBuilder tb = new TemplateBuilder( "test", renderEngine, cp, null);
		Parser smp = new Parser( tb);
		
		smp.parse( "test", new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	@Test
	public void testExpression() throws Exception{
		
		String test = "{{test}}";
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		TemplateBuilder tb = new TemplateBuilder( "test", renderEngine, cp, null);
		Parser smp = new Parser( tb);
		
		smp.parse( "test", new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	
	@Test
	public void testRenderTag() throws Exception{
		
		String test = "xxxx [[ id=123 , perspective='test', count=4 ]] yyyy";
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp, "moly","/moly/templates");
		TemplateBuilder tb = new TemplateBuilder( "test1", renderEngine, cp, null);
		Parser smp = new Parser( tb);
		
		smp.parse( "test", new ByteArrayInputStream(test.getBytes()));
	
		Template template = tb.getTemplate();
		
		assertEquals( 3, template.getRenderInsturctions().size());
		
		RenderInstruction ri = template.getRenderInsturctions().get(1);
		
		assertEquals( TemplateRenderInstruction.class, ri.getClass());
	}
		
	
	
	@Test
	public void testDecorator() throws Exception{
		
		
		TemplateFactory tf = new TemplateFactory( null, null, "/moly/templates");
		Template template = tf.getTemplate("decorate-me.moly");

		assertEquals( TextRenderInstruction.class, 		template.getRenderInsturctions().get(0).getClass());
		assertEquals( DecoratorRenderInstruction.class, template.getRenderInsturctions().get(1).getClass());
		assertEquals( TextRenderInstruction.class, 		template.getRenderInsturctions().get(2).getClass());
		assertEquals( DecoratorRenderInstruction.class, template.getRenderInsturctions().get(3).getClass());
		assertEquals( TextRenderInstruction.class, 		template.getRenderInsturctions().get(4).getClass());
		
	}
	
	
	
}