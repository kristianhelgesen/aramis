package moly;

import java.io.OutputStream;
import java.util.Map;

public class RenderEngine {
	
	private static Logger logger = LoggerFactory.getLogger(HelloWorld.class);
	
	String controllerPackageName;
	
	
	
	public RenderEngine( String controllerPackageName) {
		this.controllerPackageName = controllerPackageName;
	}
	
	
	
	public void render ( OutputStream os, Object content, String perspective, Map<String,Object> transferValues){
		
		Context templateContext = new Context();

		String className = content.getClass().getSimpleName();
		
		String controllerClassName = className +"Controller";
		
		Object controller;
		try {
			controller = Class.forName(controllerPackageName + "."+ controllerClassName);
		} catch (ClassNotFoundException e) {
			logger.debug( "", e);
		}
		
		
		String templateName = className.toLowerCase() +".moly";
		
		System.out.println("templateName: " + templateName );
	
		
		
		
	}

}
