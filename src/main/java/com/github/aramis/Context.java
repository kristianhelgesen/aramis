package com.github.aramis;

import java.util.HashMap;
import java.util.Map;

public class Context {
	
	private Context parent;
	
	private Object model;
	private Object controller;
	private Map<String,Object> parameters = new HashMap<String,Object>();
	
	public Context(){
	}
	
	public Context( Context parent) {
		this.parent = parent;
	}
	
	
	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public Object getController() {
		return controller;
	}

	public void setController(Object controller) {
		this.controller = controller;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}
	
//	public void setParameters(Map<String, Object> parameters) {
//		this.parameters = parameters;
//	}

	public Context getParent() {
		return parent;
	}
	
	public Context clone() {
		Context clone = new Context( getParent());
		clone.setController( getController());
		clone.setModel( getModel());
		
		for( String key:parameters.keySet()){
			clone.getParameters().put( key, parameters.get(key));
		}
		return clone;
	}


		
	
	
}
