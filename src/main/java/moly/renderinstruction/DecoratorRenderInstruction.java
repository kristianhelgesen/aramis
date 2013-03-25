package moly.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;
import moly.DecorationReceivingTemplate;

public class DecoratorRenderInstruction implements RenderInstruction {
	
	String decorateSectionName;
	DecorationReceivingTemplate decorateMe;
	
	public DecoratorRenderInstruction( String sectionName) {
		this.decorateSectionName = sectionName;
	}
	
	public void setDecorationReceivingTemplate( DecorationReceivingTemplate decorateMe){
		this.decorateMe = decorateMe;
	}

	@Override
	public Context apply(OutputStream os, Context context) throws IOException{
		
		decorateMe.applySection( decorateSectionName, os, context);
		
		return context;
	}

	@Override
	public String toString() {
		return "DecoratorRenderInstruction("+decorateSectionName+","+decorateMe.getName()+")";
	}
	
	

}
