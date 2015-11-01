package org.abimon.omnis.ludus;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DefaultKeyListener implements KeyListener, Runnable {

	private Thread internalThread = new Thread(this);

	public DefaultKeyListener(){
		internalThread.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(Ludus.guiInUse != null)
			return;
		char key = (e.getKeyChar() + "").toLowerCase().charAt(0);
		if((key == 'w' || key == 'a' || key == 's' || key == 'd') && !Ludus.thePlayer.moving && Ludus.thePlayer.step < 0)
		{
			Ludus.thePlayer.dir = key == 'w' ? EnumDirection.NORTH : key == 'a' ? EnumDirection.WEST : key == 's' ? EnumDirection.SOUTH : EnumDirection.EAST;
			//			if(Ludus.thePlayer.dir == EnumDirection.NORTH)
			//				Ludus.thePlayer.y--;
			//			if(Ludus.thePlayer.dir == EnumDirection.WEST)
			//				Ludus.thePlayer.x--;
			//			if(Ludus.thePlayer.dir == EnumDirection.SOUTH)
			//				Ludus.thePlayer.y++;
			//			if(Ludus.thePlayer.dir == EnumDirection.EAST)
			//				Ludus.thePlayer.x++;
			Ludus.thePlayer.moving = true;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(Ludus.guiInUse != null)
			return;
		//		System.out.println("Pressed");
		//		char key = (e.getKeyChar() + "").toLowerCase().charAt(0);
		//		if(key == 'w' || key == 'a' || key == 's' || key == 'd')
		//		{
		//			Ludus.thePlayer.dir = key == 'w' ? 3 : key == 'a' ? 1 : key == 's' ? 0 : 2;
		//			Ludus.thePlayer.moving = true;
		//		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(Ludus.guiInUse != null)
			return;
		char key = (e.getKeyChar() + "").toLowerCase().charAt(0);
		if(key == 'w' || key == 'a' || key == 's' || key == 'd')
		{
			if(Ludus.thePlayer.moving){
				System.out.println("Moving");
//				if(Ludus.thePlayer.dir == EnumDirection.NORTH)
//					Ludus.thePlayer.y--;
//				if(Ludus.thePlayer.dir == EnumDirection.WEST)
//					Ludus.thePlayer.x--;
//				if(Ludus.thePlayer.dir == EnumDirection.SOUTH)
//					Ludus.thePlayer.y++;
//				if(Ludus.thePlayer.dir == EnumDirection.EAST)
//					Ludus.thePlayer.x++;
			}
			Ludus.thePlayer.moving = false;
		}
	}

	@Override
	public void run(){
		while(true){
			try{
				//				if(Ludus.thePlayer.moving){
				//					if(Ludus.thePlayer.dir == EnumDirection.NORTH)
				//						Ludus.thePlayer.y--;
				//					if(Ludus.thePlayer.dir == EnumDirection.WEST)
				//						Ludus.thePlayer.x--;
				//					if(Ludus.thePlayer.dir == EnumDirection.SOUTH)
				//						Ludus.thePlayer.y++;
				//					if(Ludus.thePlayer.dir == EnumDirection.EAST)
				//						Ludus.thePlayer.x++;
				//				}
				Thread.sleep(750);
			}
			catch(Throwable th){
			}
		}
	}
}
