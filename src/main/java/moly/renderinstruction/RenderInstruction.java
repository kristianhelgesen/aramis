package moly.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;

import moly.Context;

public interface RenderInstruction {
	
	public Context apply( OutputStream os, Context context) throws IOException;

}
