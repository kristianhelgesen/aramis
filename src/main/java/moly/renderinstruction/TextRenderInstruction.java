package moly.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;

public class TextRenderInstruction implements RenderInstruction {
	
	byte[] textBytes;
	
	public TextRenderInstruction( String text) {
		textBytes = text.getBytes();
	}

	@Override
	public Context apply(OutputStream os, Context context) throws IOException{
		os.write( textBytes);
		return context;
	}
	
	@Override
	public String toString() {
		return "TextRenderInstruction(" +new String(textBytes)+")";
	}
	
		

}
