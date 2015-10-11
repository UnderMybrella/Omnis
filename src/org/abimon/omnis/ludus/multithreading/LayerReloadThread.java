package org.abimon.omnis.ludus.multithreading;

import org.abimon.omnis.ludus.Floor;

public class LayerReloadThread extends Thread{

	volatile Floor floor;
	int layer;

	public LayerReloadThread(int layer){
		super("Layer #" + layer + " Reload");
		this.layer = layer;
	}

	public LayerReloadThread(Floor floor, int layer){
		this.floor = floor;
		this.layer = layer;
	}

	public void setFloor(Floor floor){
		this.floor = floor;
	}

	public void run(){
		while(true){
			if(floor != null){
				floor.rerender(layer);
				Floor copy = floor.clone();
				long sleepyTime = floor.getReloadTime(layer);
				if(sleepyTime < 1000)
				{
					try{
						Thread.sleep(sleepyTime);
					}
					catch(Throwable th){}
				}
				else
					for(long i = 0L; i < sleepyTime / 1000L; i+=100L)
					{
						if(floor.getReloadTime(layer) != sleepyTime)
							break;
						if(!copy.equals(floor))
							break;
						try{
							Thread.sleep(1000);
						}
						catch(Throwable th){}
					}
			}
		}
	}
}
