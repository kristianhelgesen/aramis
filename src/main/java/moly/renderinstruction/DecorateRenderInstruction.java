package moly.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;

public class DecorateRenderInstruction implements RenderInstruction {
	
	
	public DecorateRenderInstruction( String decorate) {
		
	}

	@Override
	public Context apply(OutputStream os, Context context) throws IOException{
//		os.write( textBytes);
		return context;
	}

}
