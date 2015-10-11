package org.abimon.omnis.io;

import java.io.File;
import java.io.IOException;

public class FolderDataPool implements DataPool{

	File folder;

	public FolderDataPool(File folder){
		this.folder = folder;
	}

	/**
	 * Gets the file associated with the given key.
	 * @param key The file associated with the key
	 * @return The file object associated with the key, or null if no file is found
	 */
	public File getFileValue(String name){
		if(folder.isDirectory())
			for(File f : folder.listFiles()){
				try{
					if(f.getName().equals(name) || f.getName().startsWith(name) || f.getName().matches(name))
						return f;
				}
				catch(Throwable th){}
			}
		return null;
	}
	
	@Override
	public boolean hasData(String name) {
		return getFileValue(name) != null;
	}

	@Override
	public Data getData(String name) throws IOException {
		File value = getFileValue(name);
		if(value != null)
			return new Data(value);
		return null;
	}

}
