package org.abimon.omnis.ludus;

import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class AnimatedTile extends Tile {

	static class AnimationPairing{
		BufferedImage img;
		Long delay;
		
		public AnimationPairing(BufferedImage img, Long delay){
			this.img = img;
			this.delay = delay;
		}
	}
	
	private final String animationLocation;

	long waitingOn = -1;
	int step = -1;
	LinkedList<AnimationPairing> delays = new LinkedList<AnimationPairing>();

	public AnimatedTile(String uniqueTileName, String unlocalised, String animationLocation, boolean collide) {
		super(uniqueTileName, unlocalised, "", collide);
		this.animationLocation = animationLocation;
	}

	public void reloadIcon(){
		if(Ludus.hasData(animationLocation))
		{
			String[] lines = Ludus.getDataUnsafe(animationLocation).getAsStringArray();
			BufferedImage img = null;
			long delay = 0L;
			for(String line : lines)
				if(img == null)
					img = Ludus.getDataUnsafe(line).getAsImage();
				else
				{
					delay = Long.parseLong(line);
					delays.add(new AnimationPairing(img, delay));
					img = null;
					delay = 0L;
				}
		}
	}

	public BufferedImage getIcon(){
		if(step < 0)
		{
			System.out.println(System.currentTimeMillis());
			waitingOn = System.currentTimeMillis() + delays.get(0).delay;
			step = 0;
		}
		else
		{
			if(System.currentTimeMillis() >= waitingOn)
			{
				step++;
				if(step >= delays.size())
					step = 0;
				waitingOn = System.currentTimeMillis() + delays.get(step).delay;
			}
		}
		return delays.get(step).img;
	}
	
	@Override
	public BufferedImage getTileIcon(){
		if(delays.isEmpty())
			return null;
		return delays.peek().img;
	}

	public long getReloadTime(){
		if(step < 0)
			return 1000L;
		long reload = delays.get(step).delay;
		return reload;
	}
}
