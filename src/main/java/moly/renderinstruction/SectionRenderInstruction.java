package moly.renderinstruction;


import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import moly.Context;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectionRenderInstruction implements RenderInstruction, RenderInstructionContainer {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger( SectionRenderInstruction.class);
	
	Object compiledExpression;
	List<RenderInstruction> ri = new LinkedList<RenderInstruction>();

	
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
        
        System.out.println("Boolean prop: "+property);
        
        if( property instanceof Boolean && Boolean.TRUE.equals(((Boolean)property))) {
        	applyInternal(os, localContext);
        }
        
		return context;
	}

	
	public void applyInternal( OutputStream os, Context context) {
		for( RenderInstruction r: ri) {
			try {
				logger.debug( "Applying renderInstruction: "+r);
				context = r.apply( os, context);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	


	@Override
	public void addRenderInstruction(RenderInstruction ri) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public String toString() {
		return "MvelRenderInstruction(" +compiledExpression.toString()+")";
	}

	
}
