package com.github.aramis;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mvel2.MVEL;

import com.github.aramis.model.Image2;
import com.github.aramis.renderinstruction.Util;

public class MvelTest {

	@Test
	public void testLookupFromDifferentContexts() throws Exception{
		
		Context context = new Context();
		context.setModel(new Image2());
		context.getParameters().put("idtest", "image1");
		
//		Object exp = MVEL.compileExpression("imageID"); 
		Object exp = MVEL.compileExpression("idtest==imageID?'same':'not same'"); 
		String val = (String)Util.lookupProperty(exp, context);
		
		assertEquals("same",val);
	}

}
