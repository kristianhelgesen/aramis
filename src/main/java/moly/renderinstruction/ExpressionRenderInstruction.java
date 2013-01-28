package moly.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;

import org.apache.commons.jexl2.JexlEngine;

public class ExpressionRenderInstruction implements RenderInstruction {

	String expression;
	
	public ExpressionRenderInstruction( String expression) {
		this.expression = expression;
	}

	@Override
	public Context apply(OutputStream os, Context context) throws IOException{
		Context localContext = context;
		
        JexlEngine jexl = new JexlEngine();
        jexl.setSilent(true);
        
        Object property = jexl.getProperty( localContext.getParameters(), expression);
        while( property==null && localContext.getParent()!=null) {
        	localContext = localContext.getParent();
        	property = jexl.getProperty( localContext.getParameters(), expression);
        	
            if( property==null) {
            	property = jexl.getProperty( localContext.getModel(), expression);
            }
        }
        
        // check controller and model on root
        if( property==null) {
        	property = jexl.getProperty( localContext.getController(), expression);
        }
        if( property==null) {
        	property = jexl.getProperty( localContext.getModel(), expression);
        }

        if( property!=null){
        	os.write( property.toString().getBytes());
        }
		
		return context;
	}
	
	
}
