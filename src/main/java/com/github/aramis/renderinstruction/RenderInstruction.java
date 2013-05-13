package com.github.aramis.renderinstruction;

import java.io.IOException;
import java.io.OutputStream;

import com.github.aramis.Context;


public interface RenderInstruction {
	
	public Context apply( OutputStream os, Context context) throws IOException;

}
