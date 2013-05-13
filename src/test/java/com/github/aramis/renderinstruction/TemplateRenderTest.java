package com.github.aramis.renderinstruction;

import org.junit.Test;

public class TemplateRenderTest {

	@Test
	public void test() {
		
		TemplateRenderInstruction tr = new TemplateRenderInstruction( null, null, " id:'123' , perspective:'test', count:4 ");

		System.out.println("perspective: "+tr.perspective);
		System.out.println("id: "+tr.id);
		
		
		
		
	}
	
	
	

}
