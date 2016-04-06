package org.abimon.omnis.io;

import java.io.IOException;
import java.io.OutputStream;

public class EmptyOutputStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {}

}
