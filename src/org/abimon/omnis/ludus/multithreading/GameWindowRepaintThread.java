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
				//Thread.sleep(window.getFloor().getReloadTimeUnconditional());
			}
			catch(Throwable th){}
		}
	}
}
