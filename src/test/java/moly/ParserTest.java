package moly;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;

import moly.renderinstruction.RenderInstruction;
import moly.renderinstruction.RenderRenderInstruction;

import org.junit.Test;


public class ParserTest {

	
	@Test
	public void testTextOnly() throws Exception{
		
//		String test = "asdfasdf {{test}} [[id to render]] asdf";
		String test = "asdfasdf asdf";
		
		TemplateBuilder tb = new TemplateBuilder();
		Parser smp = new Parser( tb);
		
		smp.parse( new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	@Test
	public void testExpression() throws Exception{
		
//		String test = "asdfasdf {{test}} [[id to render]] asdf";
		String test = "{{test}}";
		
		TemplateBuilder tb = new TemplateBuilder();
		Parser smp = new Parser( tb);
		
		smp.parse( new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		for( RenderInstruction ri: template.getRenderInsturctions()){
			System.out.println(ri);
		}
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	
	@Test
	public void testRenderTag() throws Exception{
		
		String test = "xxxx [[ id=123 , perspective='test', count=4 ]] yyyy";
		
		TemplateBuilder tb = new TemplateBuilder();
		Parser smp = new Parser( tb);
		
		smp.parse( new ByteArrayInputStream(test.getBytes()));
	
		Template template = tb.getTemplate();
		
		assertEquals( 3, template.getRenderInsturctions().size());
		
		RenderInstruction ri = template.getRenderInsturctions().get(1);
		
		assertEquals( RenderRenderInstruction.class, ri.getClass());
		
		
	}
		
	
}