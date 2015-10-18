package org.abimon.omnis.ludus;

import java.awt.Point;

public class Entity {
	
	int x = 0;
	int y = 0;
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public Point getCoords(){
		return new Point(x, y);
	}
}
