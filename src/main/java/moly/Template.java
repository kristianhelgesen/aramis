package moly;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import moly.renderinstruction.RenderInstruction;
import moly.renderinstruction.RenderInstructionContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Template implements RenderInstructionContainer{

	private static final Logger logger = LoggerFactory.getLogger( Template.class);

	String name;
	List<RenderInstruction> ri = new LinkedList<RenderInstruction>();
	
	public Template( String name) {
		this.name = name;
	}

	public List<RenderInstruction> getRenderInsturctions() {
		return ri;
	}

	public void addRenderInstruction(RenderInstruction renderInstruction) {
		ri.add( renderInstruction);
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
		

}
