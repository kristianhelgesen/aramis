package com.github.aramis;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateFactory {
	
	private static final Logger logger = LoggerFactory.getLogger( TemplateFactory.class);

	Map<String, Template> templates = new HashMap<String, Template>();
	ContentProvider contentProvider;
	RenderEngine renderEngine;
	boolean uncachedTemplateMode = false;
	
	public TemplateFactory() {	
	}
	
	public TemplateFactory( RenderEngine renderEngine, ContentProvider contentProvider) {
		this.renderEngine = renderEngine;
		this.contentProvider = contentProvider;
	}
	
	public Template getTemplate( String templateName) {
		Template template = templates.get( templateName);
		if( template==null || uncachedTemplateMode) {
			template = initTemplate( templateName);
		}
		return template;
	}
	
	private Template initTemplate( String templateName) {
		TemplateBuilder tb = new TemplateBuilder( templateName, renderEngine, contentProvider, this);
		Lexer smp = new Lexer( tb);
		
		try {
			InputStream is = getClass().getResourceAsStream( templateName);
			if( is == null) {
				logger.error( "template not found "+templateName);
				return null;
			}
			
			smp.process( is);
			Template template = tb.getTemplate();
			templates.put( templateName, template);
			return template;
		} catch (Exception e) {
			logger.error( "", e);
			return null;
		}
	}

	public boolean isUncachedTemplateMode() {
		return uncachedTemplateMode;
	}

	public void setUncachedTemplateMode(boolean uncachedTemplateMode) {
		this.uncachedTemplateMode = uncachedTemplateMode;
	}
	
	

}
