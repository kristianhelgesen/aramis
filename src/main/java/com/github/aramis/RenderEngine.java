package com.github.aramis;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.Map;

import org.mvel2.MVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderEngine {
	
	private static final Logger logger = LoggerFactory.getLogger( RenderEngine.class);
	
	ContentProvider contentProvider;
	TemplateFactory templateFactory;

	public RenderEngine( ContentProvider contentProvider) {
		this.contentProvider = contentProvider;
		templateFactory = new TemplateFactory( this, contentProvider);
	}
	
	public void render ( OutputStream os, Object reference){
		render( os, reference, "", null);
	}
	
	public void render ( OutputStream os, Object reference, String perspective, Map<String,Object> transferValues){
		Object content = contentProvider.getContent( reference);
		renderContent( os, content, reference, perspective, transferValues);
	}

	public void renderContent ( OutputStream os, Object content, Object reference, String perspective, Map<String,Object> transferValues){
		
		Context templateContext = new Context();
		templateContext.setModel( content);
		templateContext.setReference( reference);
		
		String className = content.getClass().getSimpleName();
		String classPackageName = content.getClass().getPackage().getName();
		
		initController(content, reference, templateContext, className);
		
		applyValues( templateContext, transferValues);
		
		if( perspective==null) perspective = "";
		String templateName = "/"+classPackageName.replace('.', '/') + "/"
								+ className.toLowerCase() 
								+ (perspective.length()>0?"-"+perspective:"") + ".art";
		
		Template template = templateFactory.getTemplate( templateName);

		if( template==null) {
			logger.warn("Template |{}| not found", templateName);
			return;
		}
		
		template.apply( os, templateContext);
		
		logger.debug("templateName: " + templateName );
	}


	@SuppressWarnings({"rawtypes"})
	private void initController(Object content, Object reference, Context templateContext,
			String className) {
		
		String controllerPackageName = content.getClass().getPackage().getName();
		String controllerClassName = className +"Controller";
		
		Object controller = null;
		try {
			String controllerFQName = controllerPackageName + "."+ controllerClassName;
			Class controllerClass = Class.forName( controllerFQName);
			
			controller = initControllerWithContentAndReference(content,	reference, controller, controllerClass);
			if( controller==null){
				controller = initControllerWithContent(content, controller,	controllerClass);
			}
			if( controller==null) {
				controller = controllerClass.newInstance();
			}
			
		} catch (ClassNotFoundException e) {
			logger.info( "No controller ({}) found in package {} for model class {} ", controllerClassName, controllerPackageName, className);
		} catch (InstantiationException e) {
			logger.error("Error instantiating class",e);
		} catch (IllegalAccessException e) {
			logger.error("Error instantiating class",e);
		} catch (SecurityException e) {
			logger.error("Error instantiating class",e);
		} catch (IllegalArgumentException e) {
			logger.error("Error instantiating class",e);
		}
		
		templateContext.setController( controller);
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	private Object initControllerWithContent( Object content, Object controller, Class controllerClass)  {
		try {
			Constructor controllerContentConstructor = controllerClass.getConstructor(content.getClass());
			if( controllerContentConstructor!=null){
				controller = controllerContentConstructor.newInstance( content);
			}
			return controller;
		} catch (Exception e) {
			logger.debug("Error instantiating controller with content and reference",e);
			return null;
		}
	}

	@SuppressWarnings({"rawtypes","unchecked"})
	private Object initControllerWithContentAndReference(Object content, Object reference, Object controller, Class controllerClass) {
		try{
			Constructor controllerRefConstructor = controllerClass.getConstructor(content.getClass(),reference.getClass());
			if( controllerRefConstructor!=null) {
				controller = controllerRefConstructor.newInstance( content, reference);
			}
			return controller;
		} catch (Exception e) {
			logger.debug("Error instantiating controller with content and reference",e);
			return null;
		}
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