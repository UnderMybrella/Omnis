package org.abimon.omnis.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

/** The Data class is capable of holding standard amounts of data (Up tp ~2 GB) */
public class Data {
	
	private byte[] data;
	
	/** Creates an empty data object. Only 1 kB of data is storable in this instance */
	public Data(){
		data = new byte[1000];
	}
	
	/** Creates a data object from the contents of the file. 
	 * @throws IOException
	 */
	public Data(File file) throws IOException{
		FileInputStream in = new FileInputStream(file);
		data = new byte[in.available()];
		in.read(data);
		in.close();
	}
	
	/** Creates a data object by writing the object passed to the internal array 
	 * @throws IOException 
	 */
	public Data(Serializable... objects) throws IOException{
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(bOut);
		for(Serializable obj : objects)
			out.writeObject(obj);
		out.close();
		data = bOut.toByteArray();
	}
	
	public byte[] toArray(){
		return Arrays.copyOf(data, data.length);
	}
}
