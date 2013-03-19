package moly.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

import moly.ContentProvider;
import moly.Context;
import moly.RenderEngine;

public class TemplateRenderInstruction implements RenderInstruction {

	RenderEngine renderEngine;
	ContentProvider contentProvider;
	String id;
	String perspective = "";
	Map<String,Object> transferExpressions = new HashMap<String,Object>(); 
	
	
	public TemplateRenderInstruction( RenderEngine renderEngine, ContentProvider contentProvider, String description) {
		this.renderEngine = renderEngine; 
		this.contentProvider = contentProvider;
		
		for( String part : description.split(",")){
			
			// TODO: Use MVEL to parse setters as well? setter = value 
			// eller noe ala: JSON.parse("{" + description + "}");
			
			
			String[] parts = part.split("=");
			
			String key = parts[0].trim();
			String val = parts[1].trim();
			
			if( "id".equals( key)){
				id = val;
			}
			else
			if( "perspective".equals( key)){
				perspective = val;
			}
			else {
				Object targetExpression = MVEL.compileExpression( val); 
				transferExpressions.put( key, targetExpression);
			}
		}
	}
	

	
	@Override
	public Context apply(OutputStream os, Context context) throws IOException{
		
		Object content = contentProvider.getContent( id);
		
		Map<String,Object> transferValues = new HashMap<String,Object>(); // <compiled expression, resolved value>
		
		for( Map.Entry<String,Object> entry : transferExpressions.entrySet() ){
			
			String targetProperty = entry.getKey();
			Object targetExpression = entry.getValue();
			
			Object targetObj = MVEL.executeExpression( targetExpression, context);
			transferValues.put( targetProperty, targetObj);
		}
		
		renderEngine.render( os, content, perspective, transferValues);
		
		return context;
	}
	
	
}
