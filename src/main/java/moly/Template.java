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
	Map<String,List<RenderInstruction>> sectionRenderInstructions = new HashMap<String,List<RenderInstruction>>();
	String sectionName;
	String name;
	
	public Template( String name) {
		this.name = name;
	}

	public List<RenderInstruction> getRenderInsturctions() {
		return ri;
	}

	public void addRenderInstruction(RenderInstruction renderInstruction) {
		ri.add( renderInstruction);
		addRenderInstructionToSectionList( renderInstruction);
	}

	private void addRenderInstructionToSectionList(
			RenderInstruction renderInstruction) {
		if( sectionName!=null) {
			List<RenderInstruction> sri = sectionRenderInstructions.get(sectionName);
			if( sri==null) {
				sri = new LinkedList<RenderInstruction>();
				sectionRenderInstructions.put( sectionName, sri);
			}
			sri.add( renderInstruction);
		}
	}
	
	
	public void startSection( String sectionName) {
		this.sectionName = sectionName;
	}
	
	public void endSection( String sectionName) {
		this.sectionName = null;
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
	
	public void applySection( OutputStream os, Context context, String sectionName) {
		
		List<RenderInstruction> sri = sectionRenderInstructions.get(sectionName);
		if( sri==null) {
			logger.error("Template section {} not found..", sectionName);
			return;
		}
		
		for( RenderInstruction r: sri) {
			try {
				logger.debug( "Applying decorator renderInstruction: "+r);
				context = r.apply( os, context);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
