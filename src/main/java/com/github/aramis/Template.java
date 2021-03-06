package com.github.aramis;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.aramis.renderinstruction.RenderInstruction;
import com.github.aramis.renderinstruction.RenderInstructionContainer;

public class Template implements RenderInstructionContainer{

	private static final Logger logger = LoggerFactory.getLogger( Template.class);

	String name;
	List<RenderInstruction> ri = new LinkedList<RenderInstruction>();
	
	public Template( String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public List<RenderInstruction> getRenderInsturctions() {
		return ri;
	}

	public void addRenderInstruction(RenderInstruction renderInstruction, String sectionName) {
		ri.add( renderInstruction);
	}

	
	public void apply( OutputStream os, Context context) {
		for( RenderInstruction r: ri) {
			try {
				logger.debug( "Applying renderInstruction: "+r);
				context = r.apply( os, context);
			} catch (IOException e) {
				logger.error("", e);
			}
			
		}
	}
		

}
