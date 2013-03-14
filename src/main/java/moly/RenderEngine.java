package moly;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderEngine {
	
	private static final Logger logger = LoggerFactory.getLogger( RenderEngine.class);
	
	String controllerPackageName;
	String templatePackageName;
	
	Map<String,Template> templates = new HashMap<String,Template>();
	

	
	public RenderEngine( String controllerPackageName, String templatePackageName) {
		this.controllerPackageName = controllerPackageName;
		this.templatePackageName = templatePackageName;
	}
	
	
	public void render ( OutputStream os, Object content, String perspective, Map<String,Object> transferValues){
		
		Context templateContext = new Context();

		String className = content.getClass().getSimpleName();
		
		String controllerClassName = className +"Controller";
		
		Object controller = null;
		try {
			controller = Class.forName( controllerPackageName + "."+ controllerClassName);
		} catch (ClassNotFoundException e) {
			logger.info( "No controller ({}) found in package {} for model class {} ", controllerClassName, controllerPackageName, className);
		}
		
		templateContext.setController( controller);
		
		String templateName = className.toLowerCase() +".moly";
		
		Template template = templates.get( templateName);
		if( template==null) {
			template = initTemplate( templateName);
		}
		
		System.out.println("templateName: " + templateName );

	}

	

	private Template initTemplate(String templateName) {
		TemplateBuilder tb = new TemplateBuilder();
		Parser smp = new Parser( tb);
		
		try {
			smp.parse( getClass().getResourceAsStream( templatePackageName +"."+ templateName));
			Template template = tb.getTemplate();
			templates.put( templateName, template);
			return template;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
