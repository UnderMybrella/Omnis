package org.abimon.omnis.ludus;

import java.awt.image.BufferedImage;

public class EntityBlankPlayer extends EntityPlayer {
	public EntityBlankPlayer() {
		super("");
	}
	
	public void reloadIcon(){
		img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	}

	public BufferedImage getIcon(){
		return img;
	}

	public float getX(){
		return 0.0f;
	}

	public float getY(){
		return 0.0f;
	}
}
