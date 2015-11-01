package org.abimon.omnis.ludus;

import java.awt.Point;
import java.awt.image.BufferedImage;
public class Entity {

	public volatile int x = 0;
	public volatile int y = 0;

	String imageLocation;
	BufferedImage img;

	public Entity(String imageLocation){
		this.imageLocation = imageLocation;

		reloadIcon();
	}

	public void reloadIcon(){
		img = Ludus.getDataUnsafe(imageLocation).getAsImage();
	}

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	/** 
	 * Note: Not precise: Do not use unless absolutely necessary
	 * @return An imprecise location of the player; that is, the integer versions. Should only be used for absolute positioning, not rendering
	 */
	public Point getCoords(){
		return new Point(x, y);
	}

	public BufferedImage getIcon(){
		return img;
	}
}
