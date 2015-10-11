package org.abimon.omnis.ludus.multithreading;

import org.abimon.omnis.ludus.GameWindow;

public class GameWindowRepaintThread extends Thread {
	
	GameWindow window;
	
	public GameWindowRepaintThread(GameWindow window){
		this.window = window;
	}
	
	public void run(){
		while(true){
			window.repaint();
		}
	}
}
