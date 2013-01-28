package moly;

import org.apache.commons.jexl2.JexlContext;

public class TemplateContext implements JexlContext {
	
	private TemplateContext parent;

	public TemplateContext(){
	}
	
	public TemplateContext( TemplateContext parent) {
		this.parent = parent;
	}
	
	@Override
	public Object get(String paramString) {
		return null;
	}

	@Override
	public void set(String paramString, Object paramObject) {
	}

	@Override
	public boolean has(String paramString) {
		return false;
	}
	
	

}
