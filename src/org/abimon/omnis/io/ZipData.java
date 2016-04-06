package org.abimon.omnis.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipData extends Data implements Map<String, Data>, Iterable<String>
{
	HashMap<String, Data> dataStructure = new HashMap<String, Data>();

	public ZipData(){}
	
	public ZipData(Data data) throws IOException{
		ZipInputStream in = new ZipInputStream(data.getAsInputStream());
		ZipEntry entry = null;
		while((entry = in.getNextEntry()) != null){
			Data dat = new Data(in, false);
			dataStructure.put(entry.getName(), dat);
		}
		in.close();
	}
	
	public ZipData(ZipFile zip){
		
	}

	public String toString(){
		return dataStructure.toString();
	}

	public void writeToFile(String fileLoc) throws IOException{
		File loc = new File(fileLoc);
		if(!loc.exists())
			loc.createNewFile();
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(loc));
		for(String s : this)
		{
			out.putNextEntry(new ZipEntry(s));
			out.write(get(s).toArray());
		}
		out.close();
	}
	
	public void writeToFile(File loc) throws IOException{
		if(!loc.exists())
			loc.createNewFile();
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(loc));
		for(String s : this)
		{
			out.putNextEntry(new ZipEntry(s));
			out.write(get(s).toArray());
		}
		out.close();
	}

	public void writeToFileUnsafe(String fileLoc){
		try{
			File loc = new File(fileLoc);
			if(!loc.exists())
				loc.createNewFile();
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(loc));
			for(String s : this)
			{
				out.putNextEntry(new ZipEntry(s));
				out.write(get(s).toArray());
			}
			out.close();
		}
		catch(IOException e){}
	}

	@Override
	public int size() {
		return dataStructure.size();
	}

	@Override
	public boolean isEmpty() {
		return dataStructure.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return dataStructure.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return dataStructure.containsValue(value);
	}

	@Override
	public Data get(Object key) {
		return dataStructure.get(key);
	}

	@Override
	public Data put(String key, Data value) {
		return dataStructure.put(key, value);
	}

	@Override
	public Data remove(Object key) {
		return dataStructure.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Data> m) {
		dataStructure.putAll(m);
	}

	@Override
	public void clear() {
		dataStructure.clear();
	}

	@Override
	public Set<String> keySet() {
		return dataStructure.keySet();
	}

	@Override
	public Collection<Data> values() {
		return dataStructure.values();
	}

	@Override
	public Set<java.util.Map.Entry<String, Data>> entrySet() {
		return dataStructure.entrySet();
	}

	@Override
	public Iterator<String> iterator() {
		return dataStructure.keySet().iterator();
	}

	/**
	 * @return null. Use the other method with a string key
	 */
	public BufferedImage getAsImage(){
		return null;
	}

	public BufferedImage getAsImage(String key){
		if(dataStructure.containsKey(key))
			return dataStructure.get(key).getAsImage();
		return null;
	}

	/**
	 * @return toString(). Use the other method with a string key
	 */
	public String getAsString(){
		return toString();
	}

	public String getAsString(String key){
		if(dataStructure.containsKey(key))
			return dataStructure.get(key).getAsString();
		return null;
	}

	/**
	 * @return toString().split("\n"). Use the other method with a string key and a splitter regex
	 */
	public String[] getAsStringArray(){
		return toString().split("\n");
	}

	/**
	 * @return toString().split(splitter). Use the other method with a string key and a splitter regex
	 */
	public String[] getAsStringArray(String splitter){
		return toString().split(splitter);
	}

	public String[] getAsStringArray(String key, String splitter){
		if(dataStructure.containsKey(key))
			return dataStructure.get(key).getAsStringArray(splitter);
		return null;
	}

	public byte[] toArray(){
		return Arrays.copyOf(data, data.length);
	}

	public InputStream getAsInputStream(){
		return new ByteArrayInputStream(data);
	}
}
