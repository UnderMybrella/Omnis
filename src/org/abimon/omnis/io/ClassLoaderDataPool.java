package org.abimon.omnis.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.abimon.omnis.ludus.Ludus;

public class ClassLoaderDataPool implements DataPool {

	ClassLoader loader;
	Class<?> clazz = Ludus.class;

	public ClassLoaderDataPool(ClassLoader loader){
		this.loader = loader;
	}

	public ClassLoaderDataPool(Class<?> clazz){
		this.loader = clazz.getClassLoader();
		this.clazz = clazz;
	}

	@Override
	public boolean hasData(String name) {
		return loader.getResourceAsStream(name) != null;
	}

	@Override
	public Data getData(String name) throws IOException {
		if(hasData(name))
			return new Data(loader.getResourceAsStream(name));
		return null;
	}

	@Override
	public String[] getAllDataNames() {
		try{
			URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
			File file = new File(url.toURI());
			if(file.getName().endsWith(".jar")){
				List<String> names = new LinkedList<String>();
				ZipFile zip = new ZipFile(file.getAbsolutePath());
				Enumeration<? extends ZipEntry> entries = zip.entries();
				ZipEntry entry = null;
				while(entries.hasMoreElements() && (entry = entries.nextElement()) != null){
					names.add(entry.getName());
				}
				zip.close();
				return names.toArray(new String[0]);
			}
		}
		catch(Throwable th){}
		return new String[0];
	}

	@Override
	public Data[] getAllData() throws IOException {
		return null;
	}

}
