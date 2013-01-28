package moly;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import moly.renderinstruction.RenderInstruction;

public class Template {
	
	private List<RenderInstruction> ri = new LinkedList<RenderInstruction>();

	public List<RenderInstruction> getRenderInsturctions() {
		return ri;
	}

	public void addRenderInstruction(RenderInstruction renderInstruction) {
		ri.add( renderInstruction);
	}
	
	public void apply( OutputStream os, Context context) {
		
		for( RenderInstruction r: ri) {
			try {
				context = r.apply( os, context);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}

}
