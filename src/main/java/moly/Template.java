package moly;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import moly.renderinstruction.RenderInstruction;

public class Template {

	private static final Logger logger = LoggerFactory.getLogger( RenderEngine.class);

	List<RenderInstruction> ri = new LinkedList<RenderInstruction>();

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
