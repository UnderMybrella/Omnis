package org.abimon.omnis.ludus.multithreading;

import org.abimon.omnis.ludus.GameWindow;

public class GameWindowRepaintThread extends Thread {

	GameWindow window;

	public GameWindowRepaintThread(GameWindow window){
		super("Repainting Thread");
		this.window = window;
	}

	public void run(){
		while(true){
			try{
				window.repaint();
				if(window.getFloor() != null)
					Thread.sleep(Math.min(1000, window.getFloor().getReloadTimeUnconditional()));
				else
					System.out.println("Null?!");
			}
			catch(Throwable th){}
		}
	}
}
