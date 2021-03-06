package com.github.aramis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

import org.junit.Test;

import com.github.aramis.renderinstruction.DecoratorRenderInstruction;
import com.github.aramis.renderinstruction.ExpressionRenderInstruction;
import com.github.aramis.renderinstruction.RenderInstruction;
import com.github.aramis.renderinstruction.TemplateRenderInstruction;
import com.github.aramis.renderinstruction.TextRenderInstruction;


public class LexerTest {

	
	@Test
	public void testTextOnly() throws Exception{
		
		String test = "asdfasdf asdf";
		
		ContentProvider cp = new MockContentProvider();
		RenderEngine renderEngine = new RenderEngine( cp);
		TemplateBuilder tb = new TemplateBuilder( "test", renderEngine, cp, null);
		Lexer smp = new Lexer( tb);
		
		smp.process( new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	@Test
	public void testUTF8() throws Exception {
		String templateOutput = parseTestWithEncoding( "æøå", Charset.forName("UTF-8"));
		assertEquals( "æøå", templateOutput);
	}
	
	@Test
	public void testFailWithWrongEncoding() throws Exception {
		String templateOutput = parseTestWithEncoding( "æøå", Charset.forName("ISO-8859-1"));
		assertNotSame( "æøå", templateOutput);
	}
	
	
	private String parseTestWithEncoding( String text, Charset cs) throws Exception{
		TemplateBuilder tb = new TemplateBuilder( "test", null, null, null);
		Lexer smp = new Lexer(tb);
		smp.process( new ByteArrayInputStream(text.getBytes()), cs);
		
		Template template = tb.getTemplate();
		assertEquals( 1, template.getRenderInsturctions().size());
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		template.apply( baos, null);
		return new String( baos.toByteArray());
	}

	
	
	@Test
	public void testExpression() throws Exception{
		
		String test = "{{test}}";
		
		TemplateBuilder tb = new TemplateBuilder( "test", null, null, null);
		Lexer smp = new Lexer( tb);
		
		smp.process( new ByteArrayInputStream(test.getBytes()));
		
	
		Template template = tb.getTemplate();
		
		
		assertEquals( 1, template.getRenderInsturctions().size());
		
	}
	
	
	
	@Test
	public void testRenderTag() throws Exception{
		
		String test = "xxxx [[ 123 | test | count:4, index:2 ]] yyyy";
		
		TemplateBuilder tb = new TemplateBuilder( "test1", null, null, null);
		Lexer smp = new Lexer( tb);
		
		smp.process( new ByteArrayInputStream(test.getBytes()));
	
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
		Lexer smp = new Lexer( tb);
		
		smp.process( new ByteArrayInputStream(test.getBytes()));		
	
		Template template = tb.getTemplate();

		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(0).getClass());
		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(1).getClass());
		assertEquals( "TextRenderInstruction(text123)", template.getRenderInsturctions().get(1).toString());
		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(2).getClass());
	}
	
	@Test
	public void testTagAtEnd() throws Exception {
		
		String test = "xxxx {{ test }}";
		
		TemplateFactory tf = new TemplateFactory(null,null);
		TemplateBuilder tb = new TemplateBuilder( "test2", null, null, tf);
		Lexer smp = new Lexer( tb);
		
		smp.process( new ByteArrayInputStream( test.getBytes()));		
	
		Template template = tb.getTemplate();

		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(0).getClass());
		assertEquals( ExpressionRenderInstruction.class, template.getRenderInsturctions().get(1).getClass());
	}
	
	@Test
	public void testCharacterFromTagAtEnd() throws Exception {
		
		String test = "xxxx {{ test }}[";
		
		TemplateFactory tf = new TemplateFactory(null,null);
		TemplateBuilder tb = new TemplateBuilder( "test2", null, null, tf);
		Lexer smp = new Lexer( tb);
		
		smp.process( new ByteArrayInputStream( test.getBytes()));		
	
		Template template = tb.getTemplate();

		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(0).getClass());
		assertEquals( ExpressionRenderInstruction.class, template.getRenderInsturctions().get(1).getClass());
		assertEquals( TextRenderInstruction.class, template.getRenderInsturctions().get(2).getClass());
		assertEquals( "TextRenderInstruction([)", template.getRenderInsturctions().get(2).toString());
	}
	
}