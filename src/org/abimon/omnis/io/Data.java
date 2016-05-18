package org.abimon.omnis.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;

import javax.imageio.ImageIO;

/** The Data class is capable of holding standard amounts of data (Up tp ~2 GB) 
 * @author Undermybrella
 */
public class Data {

	protected byte[] data;

	/** 
	 * Creates an empty data object. Only 1 kB of data is storable in this instance 
	 */
	public Data(){
		data = new byte[1000];
	}

	/** 
	 * Creates a data object from the contents of the file. 
	 * @throws IOException
	 */
	public Data(File file) throws IOException{
		FileInputStream in = new FileInputStream(file);
		data = new byte[in.available()];
		in.read(data);
		in.close();
	}
	
	public Data(byte[] data){
		this.data = data;
	}

	public Data(InputStream in) throws IOException{
		this(in, true);
	}

	public Data(InputStream in, boolean close) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while(true){
			byte[] tmpData = new byte[1024];
			
			if(in.getClass().getName().equalsIgnoreCase("java.net.SocketInputStream"))
				if(in.available() == 0)
					break;
			
			int read = in.read(tmpData);
			if(read <= 0)
				break;
			baos.write(tmpData, 0, read);
		}
		data = baos.toByteArray();
		if(close)
			in.close();
	}

	public Data(String str){
		data = str.getBytes();
	}
	
	public Data(BufferedImage img) {
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(img, "PNG", out);
			out.close();
			
			data = out.toByteArray();
		}
		catch(Throwable th){}
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
	
	/** Be VERY careful */
	public byte[] getData(){
		return data;
	}
	
	public int length(){
		return data.length;
	}
	
	public int size(){
		return data.length;
	}

	public String toString(){
		return data.length + " bytes of data stored";
	}

	//TODO: Data conversion

	/**
	 * Returns the data contained in this Data objects buffer as a BufferedImage
	 * @return the data as a BufferedImage, or null if an error occurred, such as the data not being an image
	 */
	public BufferedImage getAsImage(){
		try {
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(toArray()));
			return img;
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAsString(){
		return new String(data);
	}

	public String[] getAsStringArray(){
		return getAsStringArray("\n");
	}

	public String[] getAsStringArray(String splitter){
		return new String(data).split(splitter);
	}

	public InputStream getAsInputStream(){
		return new ByteArrayInputStream(data);
	}

	public void write(File file) throws IOException {
		
		if(!file.exists())
			file.createNewFile();
		
		FileOutputStream out = new FileOutputStream(file);
		out.write(data);
		out.close();
	}

	public Data append(byte b) {
		byte[] newData = new byte[this.data.length + 1];
		for(int i = 0; i < newData.length; i++)
			newData[i] = data[i];
		newData[data.length] = b;
		this.data = newData;
		return this;
	}
}
