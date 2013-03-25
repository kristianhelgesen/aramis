package moly;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import moly.renderinstruction.RenderInstruction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Template {

	private static final Logger logger = LoggerFactory.getLogger( RenderEngine.class);

	List<RenderInstruction> ri = new LinkedList<RenderInstruction>();
	Map<String,List<RenderInstruction>> decorateSectionRenderInstructions = new HashMap<String,List<RenderInstruction>>();
	String name;
	
	public Template( String name) {
		this.name = name;
	}

	public List<RenderInstruction> getRenderInsturctions() {
		return ri;
	}

	public void addRenderInstruction(RenderInstruction renderInstruction, String sectionName) {
		ri.add( renderInstruction);
		if( sectionName!=null) {
			addSectionRenderInstruction( renderInstruction, sectionName);
		}
	}

	private void addSectionRenderInstruction(RenderInstruction renderInstruction, String sectionName) {
		List<RenderInstruction> sri = decorateSectionRenderInstructions.get(sectionName);
		if( sri==null) {
			sri = new LinkedList<RenderInstruction>();
			decorateSectionRenderInstructions.put( sectionName, sri);
		}
		sri.add( renderInstruction);
	}
	
	
	public void apply( OutputStream os, Context context) {
		for( RenderInstruction r: ri) {
			try {
				logger.debug( "Applying renderInstruction: "+r);
				context = r.apply( os, context);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public void applySection( String decorateSectionName, OutputStream os, Context context) {
		
		List<RenderInstruction> sri = decorateSectionRenderInstructions.get(decorateSectionName);
		if( sri==null) {
			logger.error("Template section {} not found..", decorateSectionName);
			return;
		}
		
		for( RenderInstruction r: sri) {
			try {
				logger.debug( "Applying decorator renderInstruction: "+r);
				context = r.apply( os, context);
			} catch (IOException e) {
				logger.error("",e);
			}
			
		}
	}
	

}
