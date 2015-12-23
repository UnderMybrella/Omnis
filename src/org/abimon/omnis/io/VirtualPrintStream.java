package org.abimon.omnis.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class VirtualPrintStream extends PrintStream {

	ByteArrayOutputStream out;
	
	public VirtualPrintStream(ByteArrayOutputStream out) {
		super(out);
		this.out = out;
	}
	
	public VirtualPrintStream(){
		super(new ByteArrayOutputStream());
		out = (ByteArrayOutputStream) super.out;
		out.reset();
	}
	
	public Data getContents() throws IOException{
		return new Data(out.toByteArray());
	}
	
	
}
