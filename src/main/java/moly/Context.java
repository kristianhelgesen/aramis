package moly;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.JexlEngine;

public class Context {
	
	
	public static void main( String[] args) {
		
		Context context = new Context();
		
		
        JexlEngine jexl = new JexlEngine();
        
        
        System.out.println( "invoke: "+jexl.getProperty( context, "test"));
        
	}

	
	
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


		
	
	
}
