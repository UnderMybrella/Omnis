package org.abimon.omnis.io;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

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
			for(String s : iterate(folder)){
				try{
					if(s.equals(name) || s.startsWith(name) || s.matches(name))
						return new File(folder.getAbsolutePath() + File.separator + s);
					if(s.equals(folder.getPath() + File.separator + name) || s.startsWith(folder.getPath() + File.separator + name) || s.matches(folder.getPath() + File.separator + name))
						return new File(folder.getAbsolutePath() + File.separator + s);
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

	@Override
	public String[] getAllDataNames() {
		return iterate(folder);
	}

	public String[] iterate(File folder){
		if(folder == null || !folder.isDirectory())
			return new String[]{folder.toString()};
		LinkedList<String> names = new LinkedList<String>();
		for(File f : folder.listFiles())
			if(f.isDirectory())
				for(String s : iterate(f)){
					names.add(s);
				}
			else{
				names.add(f.getAbsolutePath().replace(this.folder.getAbsolutePath() + File.separator, ""));
			}
		return names.toArray(new String[0]);
	}

	@Override
	public Data[] getAllData() throws IOException {
		String[] allFiles = iterate(folder);
		LinkedList<Data> data = new LinkedList<Data>();
		for(String s : allFiles)
			data.add(new Data(folder.getAbsolutePath() + s));
		return data.toArray(new Data[0]);
	}

}
