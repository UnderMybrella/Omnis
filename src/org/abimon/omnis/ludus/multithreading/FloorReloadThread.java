package org.abimon.omnis.ludus.multithreading;

import org.abimon.omnis.ludus.Floor;

public class FloorReloadThread extends Thread{

	volatile Floor floor;

	public FloorReloadThread(){}
	
	public FloorReloadThread(Floor floor){
		this.floor = floor;
	}

	public void setFloor(Floor floor){
		this.floor = floor;
	}

	public void run(){
		while(true){
			if(floor != null){
				floor.rerender();
				Floor copy = floor.clone();
				long sleepyTime = floor.getReloadTime();
				for(long i = 0L; i < sleepyTime / 1000L; i+=100L)
				{
					if(floor.getReloadTime() != sleepyTime)
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
