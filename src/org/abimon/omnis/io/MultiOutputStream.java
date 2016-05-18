package org.abimon.omnis.io;

import java.io.IOException;
import java.io.OutputStream;

public class MultiOutputStream extends OutputStream {

	OutputStream[] oses;
	
	public MultiOutputStream(OutputStream... oses){
		this.oses = oses;
	}
	
	@Override
	public void write(int b) throws IOException {
		for(OutputStream os : oses)
			os.write(b);
	}

}
