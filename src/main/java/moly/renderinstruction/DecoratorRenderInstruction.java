package moly.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;
import moly.Template;

public class DecoratorRenderInstruction implements RenderInstruction {
	
	Template decorateTarget;
	String sectionName;
	
	public DecoratorRenderInstruction( Template decorateTarget, String sectionName) {
		this.decorateTarget = decorateTarget; 
		this.sectionName = sectionName;
	}

	@Override
	public Context apply(OutputStream os, Context context) throws IOException{
		
		decorateTarget.applySection( sectionName, os, context);
		
		return context;
	}

}
