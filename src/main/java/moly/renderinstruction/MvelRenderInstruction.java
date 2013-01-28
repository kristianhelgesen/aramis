package moly.renderinstruction;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;

import org.mvel2.MVEL;
import org.mvel2.compiler.CompiledExpression;

public class MvelRenderInstruction implements RenderInstruction {

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
		catch( Exception ex){
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
