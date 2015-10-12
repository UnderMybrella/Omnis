package org.abimon.omnis.ludus;

import java.awt.image.BufferedImage;

public class Tile implements Cloneable{
	
	final String unlocalised;
	final String resourceLocation;
	final String uniqueTileName;
	
	BufferedImage icon;
	
	public Tile(String uniqueTileName, String unlocalised, String resourceLocation){
		this.unlocalised = unlocalised;
		this.resourceLocation = resourceLocation;
		
		this.uniqueTileName = uniqueTileName;
		
		Ludus.registerTile(uniqueTileName, this);
	}
	
	public void reloadIcon(){
		if(Ludus.hasData(resourceLocation))
			icon = Ludus.getDataUnsafe(resourceLocation).getAsImage();
	}
	
	public BufferedImage getIcon(){
		return icon;
	}
	
	/**
	 * Used to identify this tile when reading data from a tileset
	 * @return The image used to properly identify this tile
	 */
	public BufferedImage getTileIcon(){
		return icon;
	}
	
	public long getReloadTime(){
		return Long.MAX_VALUE;
	}
	
	public Tile clone(){
		return this;
	}
	
	public int hashCode(){
		return uniqueTileName.hashCode();
	}
	
	public boolean equals(Object object){
		return hashCode() == object.hashCode();
	}
	
	public String toString(){
		return uniqueTileName;
	}
	
	public int getWidthUnscaled(){
		return 1;
	}
	
	public int getHeightUnscaled(){
		return 1;
	}
}
