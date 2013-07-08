package com.github.aramis.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

import com.github.aramis.ContentProvider;
import com.github.aramis.Context;
import com.github.aramis.RenderEngine;


public class TemplateRenderInstruction implements RenderInstruction {

	RenderEngine renderEngine;
	ContentProvider contentProvider;
	String id;
	String perspective = "";
	Map<String,Object> transferExpressions = new HashMap<String,Object>(); 
	
	
	public TemplateRenderInstruction( RenderEngine renderEngine, ContentProvider contentProvider, String description) {
		this.renderEngine = renderEngine; 
		this.contentProvider = contentProvider;
		
		String[] mainParts = description.split("\\|");
		id = mainParts[0].trim();
		
		if( mainParts.length==1) return;

		String maybePerspective = mainParts[1];
		String parameters = null;
		
		if( maybePerspective.contains(":")) {
			parameters = maybePerspective;
		}
		else {
			perspective = maybePerspective.trim();
			if( mainParts.length==3) {
				parameters = mainParts[2];
			}
			else {
				return;
			}
		}
		
		for( String part : parameters.split(",")){
			
			// TODO: Use MVEL to parse setters as well? setter = value 
			// eller noe ala: JSON.parse("{" + description + "}");
			
			String[] parts = part.split(":");
			
			String key = parts[0].trim();
			String val = parts[1].trim();
			
			Object targetExpression = MVEL.compileExpression( val); 
			transferExpressions.put( key, targetExpression);
		}
	}
	

	
	@Override
	public Context apply(OutputStream os, Context context) throws IOException{
		
		Map<String,Object> transferValues = new HashMap<String,Object>(); // <compiled expression, resolved value>
		
		for( Map.Entry<String,Object> entry : transferExpressions.entrySet() ){
			
			String targetProperty = entry.getKey();
			Object targetExpression = entry.getValue();
			
			Object targetObj = MVEL.executeExpression( targetExpression, context);
			transferValues.put( targetProperty, targetObj);
		}
		
		renderEngine.render( os, id, perspective, transferValues);
		
		return context;
	}
	
	
}
