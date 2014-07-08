package com.github.aramis;

import static org.junit.Assert.assertEquals;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.ObjectContext;
import org.junit.Test;

import com.github.aramis.el.DelegatingJexlContext;
import com.github.aramis.model.Image2;

public class ExpressionParserTest {

	@Test
	public void testPlainObjectExpression() {
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression( "b.c" );

        A a = new A();
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
		JexlContext jc = new ObjectContext(jexl, a);
        Object o = e.evaluate(jc);
        assertEquals(o,a.b.c);
	}
	

	@Test
	public void testLookupFromDifferentContexts() throws Exception{
		
		Context aramisContext = new Context();
		aramisContext.setModel(new Image2());
		aramisContext.getParameters().put("idtest", "image1");
		
        JexlEngine jexl = new JexlEngine();
		Expression e = jexl.createExpression("idtest!=imageID?'same':'not same'"); 
		JexlContext jc = new DelegatingJexlContext(aramisContext, jexl);
        Object o = e.evaluate(jc);
		
		assertEquals("same",o);
	}
	
	
	public static class A{
		public B b = new B();
		
		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

		public static class B{
			public String c = "ccc";

			public String getC() {
				return c;
			}

			public void setC(String c) {
				this.c = c;
			}
			
		}
	}
	
}
