package org.abimon.omnis.ludus;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Tile implements Cloneable{
	
	String unlocalised;
	String resourceLocation;
	String uniqueTileName;
	
	BufferedImage icon;
	
	public static LinkedList<Tile> tiles = new LinkedList<Tile>();
	
	public Tile(String uniqueTileName, String unlocalised, String resourceLocation){
		this.unlocalised = unlocalised;
		this.resourceLocation = resourceLocation;
		
		this.uniqueTileName = uniqueTileName;
		
		tiles.add(this);
	}
	
	public void reloadIcon(){
		if(Ludus.hasData(resourceLocation))
			icon = Ludus.getDataUnsafe(resourceLocation).getAsImage();
	}
	
	public BufferedImage getIcon(){
		return icon;
	}
	
	public long getReloadTime(){
		return Long.MAX_VALUE;
	}
	
	public Tile clone(){
		Tile tile = new Tile(uniqueTileName, unlocalised, resourceLocation);
		tile.reloadIcon();
		return tile;
	}
	
	public int hashCode(){
		return uniqueTileName.hashCode();
	}
	
	public boolean equals(Object object){
		return hashCode() == object.hashCode();
	}
}
