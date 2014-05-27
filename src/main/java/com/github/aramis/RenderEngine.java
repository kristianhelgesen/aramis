package com.github.aramis;

import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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


	@SuppressWarnings({"rawtypes", "unchecked"})
	private void initController(Object content, Object reference, Context templateContext,
			String className) {
		
		String controllerPackageName = content.getClass().getPackage().getName();
		String controllerClassName = className +"Controller";
		
		Object controller = null;
		try {
			String controllerFQName = controllerPackageName + "."+ controllerClassName;
			Class controllerClass = Class.forName( controllerFQName);

			Constructor[] constructors = controllerClass.getConstructors();
			Constructor constructor = constructors[0];
			if( constructors.length>1) {
				logger.warn( "Multiple constructors found for controller "+controllerFQName+". Using the first.");
			}
			
			Class[] types = constructor.getParameterTypes();
			Object[] arguments = new Object[types.length];
			int i=0;
			for( Class type : types) {
				if( type.isAssignableFrom( content.getClass())){
					arguments[i] = content;
				}				
				if( reference!=null && type.isAssignableFrom( reference.getClass())){
					arguments[i] = reference;
				}
				if( contentProvider!=null && type.isAssignableFrom( contentProvider.getClass())){
					arguments[i] = contentProvider;
				}
				if( templateFactory!=null && type.isAssignableFrom( templateFactory.getClass())){
					arguments[i] = templateFactory;
				}
				i++;
			}
			controller = constructor.newInstance(arguments);
			
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
		} catch (InvocationTargetException e) {
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