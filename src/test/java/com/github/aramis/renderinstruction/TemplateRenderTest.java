package com.github.aramis.renderinstruction;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mvel2.MVEL;

public class TemplateRenderTest {

	@Test
	public void idOnlyTest() {
		TemplateRenderInstruction tr = new TemplateRenderInstruction( null, null, " '123' ");
		assertEquals( "123", MVEL.executeExpression( tr.contentRefExpr));
	}

	@Test
	public void idAndPerspectiveTest() {
		TemplateRenderInstruction tr = new TemplateRenderInstruction( null, null, " 123 | test");
		assertEquals( 123, MVEL.executeExpression( tr.contentRefExpr));
		assertEquals( "test", tr.perspective);
	}
	
	@Test
	public void idAndPerspectiveAndParametersTest() {
		TemplateRenderInstruction tr = new TemplateRenderInstruction( null, null, " '123' | test | a:1, b:2");
		assertEquals( "123", MVEL.executeExpression( tr.contentRefExpr));
		assertEquals( "test", tr.perspective);
		assertEquals( 1 , MVEL.executeExpression( tr.transferExpressions.get("a")));
	}

	@Test
	public void idAndParametersTest() {
		TemplateRenderInstruction tr = new TemplateRenderInstruction( null, null, " '123' | a:1, b:2, c:'asdf'");
		assertEquals( "123", MVEL.executeExpression( tr.contentRefExpr));
		assertEquals( "", tr.perspective);
		assertEquals( 1 , MVEL.executeExpression( tr.transferExpressions.get("a")));
		assertEquals( 2 , MVEL.executeExpression( tr.transferExpressions.get("b")));
		assertEquals( "asdf" , MVEL.executeExpression( tr.transferExpressions.get("c")));
	}

}
