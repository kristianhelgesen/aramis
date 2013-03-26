package moly;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import moly.renderinstruction.RenderInstruction;
import moly.renderinstruction.RenderInstructionContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecorationReceivingTemplate implements RenderInstructionContainer {
	private static final Logger logger = LoggerFactory.getLogger( DecorationReceivingTemplate.class);

	Map<String,List<RenderInstruction>> decorateSectionRenderInstructions = new HashMap<String,List<RenderInstruction>>();
	String name;
	
	public DecorationReceivingTemplate( String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}


	public void addRenderInstruction(RenderInstruction renderInstruction, String sectionName) {
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
	

	
	public void applySection( String decorateSectionName, OutputStream os, Context context) {
		
		logger.info(decorateSectionRenderInstructions.toString());
		
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
