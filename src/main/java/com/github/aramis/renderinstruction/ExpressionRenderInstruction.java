package com.github.aramis.renderinstruction;

import static org.apache.commons.lang3.StringEscapeUtils.*;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aramis.Context;
import com.github.aramis.el.Evaluator;

public class ExpressionRenderInstruction implements RenderInstruction {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger( ExpressionRenderInstruction.class);
	
	Expression compiledExpression;
	boolean escape;
	
	public ExpressionRenderInstruction( String expression) {
		this(expression, true);
	}

	public ExpressionRenderInstruction( String expression, boolean escape) {
		compiledExpression = new JexlEngine().createExpression(expression);
		this.escape = escape;
	}
	

	public Context apply(OutputStream os, final Context context) throws IOException{

		Object property = Evaluator.lookup( compiledExpression, context);

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
		return "ExpressionRenderInstruction(" +compiledExpression.toString()+")";
	}
	
	
	
	
}
