package moly.renderinstruction;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectionRenderInstruction implements RenderInstruction {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger( SectionRenderInstruction.class);
	
	Object compiledExpression; 
	
	public SectionRenderInstruction( String expression) {
		compiledExpression = MVEL.compileExpression(expression); 
	}

	@Override
	public Context apply(OutputStream os, final Context context) throws IOException{

		Context localContext = context.clone();
        Object property = Util.lookupProperty( compiledExpression, context);

        if( property==null){
        	return context;
        }
        
        if( property instanceof Boolean) {
        	
        	//TBD..
        	
        }
        
		return context;
	}



	@Override
	public String toString() {
		return "MvelRenderInstruction(" +compiledExpression.toString()+")";
	}
	
	
	
	
}
