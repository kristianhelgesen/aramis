package moly.renderinstruction;

import static org.junit.Assert.*;

import moly.ContentProvider;
import moly.RenderEngine;

import org.junit.Test;

public class RenderRenderTest {

	@Test
	public void test() {
		
		RenderRenderInstruction rrit = new RenderRenderInstruction( null, null, "id='123' , perspective='test', count=4");

		System.out.println("perspective: "+rrit.perspective);
		System.out.println("id: "+rrit.id);
		
	}
	
	
	

}
