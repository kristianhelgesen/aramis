package com.github.aramis.el;

import java.util.Map;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.ObjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aramis.Context;

public class DelegatingJexlContext implements JexlContext {
	
	private static final Logger logger = LoggerFactory.getLogger(DelegatingJexlContext.class);
	
	Context aramisContext;
	JexlEngine jexl;
	
	public DelegatingJexlContext( Context aramisContext, JexlEngine jexl){
		this.aramisContext = aramisContext;
		this.jexl = jexl;
		this.jexl.setSilent(true);
	}

	public Object get(String name) {
		Object property = null;
		Context rootContext = aramisContext;	
		
		// if model is set in section scope, this takes precedence. Wait with checking the root model..
		while( rootContext.getParent()!=null){
			property = getProp( name, rootContext.getModel());
        	rootContext = rootContext.getParent();
		}
		
		Context localContext = aramisContext;
        while( property==null && localContext!=null) {
			property = getProp(name, localContext.getParameters());
        	localContext = localContext.getParent();
        }

        // check controller on root only
        if( property==null) {
			property = getProp(name, rootContext.getController());
        }
        
        // check reference on root only
        if( property==null) {
			property = getProp(name, rootContext.getReference());
        }

        // finally, check root model
        if( property==null) {
			property = getProp(name, rootContext.getModel());
        }
        
        if( property==null) {
        	logger.warn("Lookup of property "+name+" returned without result");
        }
        
		return property;
	}

	private Object getProp(String name, Object o) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ObjectContext oc = new ObjectContext( jexl, o);
		return oc.get(name);
	}
	
	private Object getProp(String name, Map<String,Object> m) {
		MapContext mc = new MapContext( m);
		return mc.get(name);
	}

	
	
	public boolean has(String target) {
		return get(target)!=null;
	}

	
	public void set(String key, Object value) {
		throw new UnsupportedOperationException();
	}

}
