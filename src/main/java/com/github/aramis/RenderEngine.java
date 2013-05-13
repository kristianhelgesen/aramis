package com.github.aramis;

import java.io.OutputStream;
import java.util.Map;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderEngine {
	
	private static final Logger logger = LoggerFactory.getLogger( RenderEngine.class);
	
	ContentProvider contentProvider;
	String controllerPackageName;
	String templatePackageName;
	TemplateFactory templateFactory;


	public RenderEngine( ContentProvider contentProvider, String controllerPackageName, String templatePackageName) {
		this.contentProvider = contentProvider;
		this.controllerPackageName = controllerPackageName;
		this.templatePackageName = templatePackageName;
		templateFactory = new TemplateFactory( this, contentProvider, templatePackageName);
	}
	
	
	public void render ( OutputStream os, Object content, String perspective, Map<String,Object> transferValues){
		
		Context templateContext = new Context();
		templateContext.setModel( content);
		
		String className = content.getClass().getSimpleName();
		
		initController(content, templateContext, className);
		
		applyValues( templateContext, transferValues);
		
		if( perspective==null) perspective = "";
		String templateName = className.toLowerCase() + (perspective.length()>0?"-"+perspective:"") + ".template";
		Template template = templateFactory.getTemplate( templateName);

		if( template==null) {
			logger.warn("Template {} not found", templateName);
			return;
		}
		
		template.apply( os, templateContext);
		
		logger.debug("templateName: " + templateName );
	}


	@SuppressWarnings({"unchecked","rawtypes"})
	private void initController(Object content, Context templateContext,
			String className) {
		String controllerClassName = className +"Controller";
		
		Object controller = null;
		try {
			String controllerFQName = controllerPackageName + "."+ controllerClassName;
			Class controllerClass = Class.forName( controllerFQName);
			controller = controllerClass.newInstance();
			if( controller instanceof ContentAware) {
				((ContentAware)controller).setContent(content);
			}
		} catch (ClassNotFoundException e) {
			logger.info( "No controller ({}) found in package {} for model class {} ", controllerClassName, controllerPackageName, className);
		} catch (InstantiationException e) {
			logger.error("Error instantiating class",e);
		} catch (IllegalAccessException e) {
			logger.error("Error instantiating class",e);
		}
		
		templateContext.setController( controller);
	}
	

	private void applyValues(Context templateContext, Map<String, Object> transferValues) {

		for( Map.Entry<String, Object> tv: transferValues.entrySet()) {
			String transferProperty = tv.getKey();
			
			if( templateContext.getController()!=null) {
				try{
					MVEL.executeExpression( transferProperty, templateContext.getController());
					continue;
				}
				catch( Exception e){
					logger.debug("", e);
				}
			}
			
			templateContext.getParameters().put( transferProperty, tv.getValue());

		}
		
	}

	

	
}