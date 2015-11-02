package org.abimon.omnis.io;

import java.io.IOException;
import java.util.HashMap;

public class VirtualDataPool implements DataPool {

	HashMap<String, Data> pool = new HashMap<String, Data>();
	
	public void add(String key, Data value){
		pool.put(key, value);
	}
	
	@Override
	public boolean hasData(String name) {
		return pool.containsKey(name);
	}

	@Override
	public Data getData(String name) throws IOException {
		return pool.get(name);
	}

}
