package moly.renderinstruction;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;
import moly.RenderEngine;
import moly.ctrl.Image2Controller;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MvelRenderInstruction implements RenderInstruction {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger( RenderEngine.class);

	
	Object compiledExpression; 
	boolean escape;
	
	public MvelRenderInstruction( String expression) {
		this(expression, true);
	}

	public MvelRenderInstruction( String expression, boolean escape) {
		compiledExpression = MVEL.compileExpression(expression); 
		this.escape = escape;
	}
	
	
	private Object executeExpression( Object context) {
		try{
			return MVEL.executeExpression( compiledExpression, context);
		}
		catch( Exception e){
			logger.debug("", e);
			return null;
		}
	}
	

	@Override
	public Context apply(OutputStream os, final Context context) throws IOException{
		Context localContext = context;

        Object property = executeExpression( localContext.getParameters());
        while( property==null && localContext.getParent()!=null) {
        	localContext = localContext.getParent();
        	property = executeExpression( localContext.getParameters());
        	
            if( property==null) {
            	property = executeExpression( localContext.getModel());
            }
        }
        
        // check controller and model on root
        if( property==null) {
        	property = executeExpression( localContext.getController());
        }
        if( property==null) {
        	property = executeExpression( localContext.getModel());
        }

        if( property!=null){
        	String prop = property.toString();
        	if( escape) {
        		prop = escapeHtml4( prop);
        	}
        	os.write( prop.getBytes());
        }
		
		return context;
	}
	
	
}
