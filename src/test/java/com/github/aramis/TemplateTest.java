package com.github.aramis;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;


import org.junit.Test;

import com.github.aramis.Context;
import com.github.aramis.Template;
import com.github.aramis.model.Model1;
import com.github.aramis.renderinstruction.ExpressionRenderInstruction;


public class TemplateTest {

	
	
	@Test
	public void testExpression() throws Exception{
	
		Template t = new Template("test");
		t.addRenderInstruction( new ExpressionRenderInstruction( "m1"), null);
		t.addRenderInstruction( new ExpressionRenderInstruction( "m2"), null);
		
		Context root = new Context();
		Context section1 = new Context(root);
		
		Model1 m = new Model1();
		m.setM1("mmmm1");
		m.setM2("mmmm2");
		root.setModel(m);
		
		section1.getParameters().put( "m2", "ssss2");
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		t.apply( baos, section1);
		
		String output = baos.toString();
		
		
		assertEquals( "mmmm1ssss2",output);
		
	}
	
	
	
	
}