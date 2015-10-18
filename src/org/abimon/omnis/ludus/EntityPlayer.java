package org.abimon.omnis.ludus;

import java.awt.image.BufferedImage;

public class EntityPlayer extends Entity {

	BufferedImage[] imgs;
	
	int dir = 0;
	
	public EntityPlayer(String imageLocation) {
		super(imageLocation);
	}
	
	public void reloadIcon(){
		imgs = new BufferedImage[16];
		img = Ludus.getDataUnsafe(imageLocation).getAsImage();
		System.out.println(img);
		
		int imgWidth = img.getWidth() / 4;
		int imgHeight = img.getHeight() / 4;

		for(int y = 0; y < 4; y++)
			for(int x = 0; x < 4; x++)
				imgs[y * 4 + x] = img.getSubimage(x * imgWidth, y * imgHeight, imgWidth, imgHeight);
	}

	public BufferedImage getIcon(){
		return imgs[dir * 4];
	}
	
}
