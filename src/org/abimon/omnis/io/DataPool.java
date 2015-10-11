package org.abimon.omnis.io;

import java.io.IOException;

/** 
 * The Datapool interface allows access to files from a central location. 
 * This location may be a classloader, a directory, or other creative locations.
 * @author Undermybrella
 */
public interface DataPool
{
	/**
	 * Returns if the data pool contains data corresponding to the name provided
	 * @param name The 'key' for the data
	 * @return true if the pool contains a value for the key
	 */
	public boolean hasData(String name);
	
	/**
	 * Get the data corresponding to the key 'name' 
	 * @param name The 'key' for the data
	 * @return the Data corresponding to key 'name', or null if the pool doesn't contain an object for the key 'name'
	 * @throws IOException if there is an IOException while reading the data
	 */
	public Data getData(String name) throws IOException;
}
