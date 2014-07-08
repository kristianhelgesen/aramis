package com.github.aramis.el;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.ObjectContext;

import com.github.aramis.Context;

public class DelegatingJexlContext implements JexlContext {
	Context aramisContext;
	JexlEngine jexl;
	
	public DelegatingJexlContext( Context aramisContext, JexlEngine jexl){
		this.aramisContext = aramisContext;
		this.jexl = jexl;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object get(String name) {
		Object property = null;
		Context rootContext = aramisContext;	
		
		// if model is set in section scope, this takes precedence. Wait with checking the root model..
		while( rootContext.getParent()!=null){
			property = new ObjectContext( jexl, rootContext.getModel()).get(name);
        	rootContext = rootContext.getParent();
		}
		
		Context localContext = aramisContext;
        while( property==null && localContext!=null) {
        	property = new MapContext( localContext.getParameters()).get(name);
        	localContext = localContext.getParent();
        }

        // check controller on root only
        if( property==null) {
			property = new ObjectContext( jexl, rootContext.getController()).get(name);
        }
        
        // check reference on root only
        if( property==null) {
			property = new ObjectContext( jexl, rootContext.getReference()).get(name);
        }

        // finally, check root model
        if( property==null) {
			property = new ObjectContext( jexl, rootContext.getModel()).get(name);
        }
		return property;
	}
	
	
	public boolean has(String target) {
		return get(target)!=null;
	}

	
	public void set(String key, Object value) {
		throw new UnsupportedOperationException();
	}

}
