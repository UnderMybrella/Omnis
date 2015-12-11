package org.abimon.omnis.ludus;

import java.awt.image.BufferedImage;

public class EntityPlayer extends Entity {

	BufferedImage[] imgs;

	EnumDirection dir = EnumDirection.SOUTH;
	int step = -1;
	long waitingOn = -1;
	boolean moving = false;

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
		BufferedImage img = null;
		if(moving){
			if(step < 0)
			{
				System.out.println(System.currentTimeMillis());
				waitingOn = System.currentTimeMillis() + 125;
				step = 0;
			}
		}
		if(moving || step >= 0)
		{
			if(System.currentTimeMillis() >= waitingOn)
			{
				step++;
				if(step >= 4)
				{	
					step = -1;
					img = imgs[dir.toInt() * 4];
					if(Ludus.thePlayer.dir == EnumDirection.NORTH)
						Ludus.thePlayer.y--;
					if(Ludus.thePlayer.dir == EnumDirection.WEST)
						Ludus.thePlayer.x--;
					if(Ludus.thePlayer.dir == EnumDirection.SOUTH)
						Ludus.thePlayer.y++;
					if(Ludus.thePlayer.dir == EnumDirection.EAST)
						Ludus.thePlayer.x++;
					return img;
				}
				waitingOn = System.currentTimeMillis() + 125;
			}
			img = imgs[dir.toInt() * 4 + step];
		}
		else
			img = imgs[dir.toInt() * 4];
		return img;
	}

	public float getX(){
		if(moving || step >= 0)
			if(dir == EnumDirection.EAST)
				return x + (0.25f * step);
			else if(dir == EnumDirection.WEST)
				return x - (0.25f * step);
		return x;
	}

	public float getY(){
		if(moving || step >= 0)
			if(dir == EnumDirection.NORTH)
				return y - (0.25f * step);
			else if(dir == EnumDirection.SOUTH)
				return y + (0.25f * step);
		return y;
	}


}
