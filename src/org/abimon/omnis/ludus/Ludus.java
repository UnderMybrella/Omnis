package org.abimon.omnis.ludus;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import org.abimon.omnis.io.ClassLoaderDataPool;
import org.abimon.omnis.io.Data;
import org.abimon.omnis.io.DataPool;
import org.abimon.omnis.io.FolderDataPool;

public class Ludus 
{
	private static LinkedList<DataPool> dataPools = new LinkedList<DataPool>();

	/**
	 * Adds a class loader to the available data pools. 
	 * Make sure not to add the same class loader multiple times, as that may result in weird bugs
	 * @param loader The loader that will get added to the data pool collection
	 */
	public static void registerDataPool(ClassLoader loader){
		DataPool pool = new ClassLoaderDataPool(loader);
		dataPools.add(pool);
	}
	
	/**
	 * Adds a folder to the available data pools. 
	 * Make sure not to add the same folder multiple times, as that may result in weird bugs
	 * @param folder The folder that will get added to the data pool collection
	 */
	public static void registerDataPool(File folder){
		DataPool pool = new FolderDataPool(folder);
		dataPools.add(pool);
	}

	public static boolean hasData(String name){
		for(DataPool pool : dataPools)
			if(pool.hasData(name))
				return true;
		return false;
	}

	public static Data getData(String name) throws IOException{
		for(DataPool pool : dataPools)
			if(pool.hasData(name))
				return pool.getData(name);
		return null;
	}

	public static Data getDataUnsafe(String name){
		try{
			for(DataPool pool : dataPools)
				if(pool.hasData(name))
					return pool.getData(name);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return null;
	}
}
