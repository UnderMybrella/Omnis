package org.abimon.omnis.ludus;

import java.awt.Point;
import java.awt.image.BufferedImage;
public class Entity {

	int x = 0;
	int y = 0;

	String imageLocation;
	BufferedImage img;

	public Entity(String imageLocation){
		this.imageLocation = imageLocation;

		reloadIcon();
	}

	public void reloadIcon(){
		img = Ludus.getDataUnsafe(imageLocation).getAsImage();
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public Point getCoords(){
		return new Point(x, y);
	}

	public BufferedImage getIcon(){
		return img;
	}
}
