package org.abimon.omnis.io;

import java.io.IOException;

public class ClassLoaderDataPool implements DataPool {

	ClassLoader loader;

	public ClassLoaderDataPool(ClassLoader loader){
		this.loader = loader;
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

}
