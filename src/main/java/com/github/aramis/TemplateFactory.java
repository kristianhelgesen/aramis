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
	String templatePackageName;
	RenderEngine renderEngine;
	
	public TemplateFactory( RenderEngine renderEngine, ContentProvider contentProvider, String templatePackageName) {
		this.renderEngine = renderEngine;
		this.contentProvider = contentProvider;
		this.templatePackageName = templatePackageName;
	}
	
	public Template getTemplate( String templateName) {
		Template template = templates.get( templateName);
		if( template==null) {
			template = initTemplate( templateName);
		}
		return template;
	}
	
	private Template initTemplate( String templateName) {
		TemplateBuilder tb = new TemplateBuilder( templateName, renderEngine, contentProvider, this);
		Parser smp = new Parser( tb);
		
		try {
			String templateFQN = templatePackageName +"/"+ templateName;
			logger.debug(templateFQN);
			InputStream is = getClass().getResourceAsStream( templateFQN);
			if( is == null) {
				logger.error( "template not found "+templateFQN);
			}
			
			smp.parse( templateName, is);
			Template template = tb.getTemplate();
			templates.put( templateName, template);
			return template;
		} catch (Exception e) {
			logger.error( "", e);
			return null;
		}
	}

}
