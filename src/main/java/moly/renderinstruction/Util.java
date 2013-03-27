package moly.renderinstruction;

import moly.Context;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Util {
	
	private static final Logger logger = LoggerFactory.getLogger( Util.class);
	
	static Object lookupProperty( Object compiledExpression, Context context) {
		
		Object property = null;
		Context rootContext = context;
		
		// if model is set in section scope, this takes precedence. Wait with checking the root model..
		while( rootContext.getParent()!=null){
        	property = executeExpression( compiledExpression, rootContext.getModel());
        	rootContext = rootContext.getParent();
		}
		
		Context localContext = context;
        while( property==null && localContext!=null) {
        	property = executeExpression( compiledExpression, localContext.getParameters());
        	localContext = localContext.getParent();
        }
        
        // check controller on root only
        if( property==null) {
        	property = executeExpression( compiledExpression, rootContext.getController());
        }

        // finally, check root model
        if( property==null) {
        	property = executeExpression( compiledExpression,rootContext.getModel());
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
