package moly.renderinstruction;

import moly.Context;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Util {
	
	private static final Logger logger = LoggerFactory.getLogger( Util.class);
	
	static Object lookupProperty( Object compiledExpression, Context localContext) {
		Object property = executeExpression( compiledExpression,localContext.getParameters());
        while( property==null && localContext.getParent()!=null) {
        	localContext = localContext.getParent();
        	property = executeExpression( compiledExpression, localContext.getParameters());
        	
            if( property==null) {
            	property = executeExpression( compiledExpression, localContext.getModel());
            }
        }
        
        // check controller and model on root
        if( property==null) {
        	property = executeExpression( compiledExpression, localContext.getController());
        }
        if( property==null) {
        	property = executeExpression( compiledExpression,localContext.getModel());
        }
		return property;
	}
	
	static private Object executeExpression( Object compiledExpression, Object context) {
		try{
			return MVEL.executeExpression( compiledExpression, context);
		}
		catch( Exception e){
			logger.debug("", e);
			return null;
		}
	}
}
