package com.github.aramis.renderinstruction;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;


import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aramis.Context;

public class ExpressionRenderInstruction implements RenderInstruction {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger( ExpressionRenderInstruction.class);
	
	Object compiledExpression; 
	boolean escape;
	
	public ExpressionRenderInstruction( String expression) {
		this(expression, true);
	}

	public ExpressionRenderInstruction( String expression, boolean escape) {
		compiledExpression = MVEL.compileExpression(expression); 
		this.escape = escape;
	}
	

	public Context apply(OutputStream os, final Context context) throws IOException{

		Object property = Util.lookupProperty( compiledExpression, context);

        if( property!=null){
        	String prop = property.toString();
        	if( escape) {
        		prop = escapeHtml4( prop);
        	}
        	os.write( prop.getBytes());
        }
		
		return context;
	}



	@Override
	public String toString() {
		return "MvelRenderInstruction(" +compiledExpression.toString()+")";
	}
	
	
	
	
}
