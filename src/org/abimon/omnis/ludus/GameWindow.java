package org.abimon.omnis.ludus;

import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.abimon.omnis.ludus.multithreading.FloorReloadThread;
import org.abimon.omnis.ludus.multithreading.GameWindowRepaintThread;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private Floor floor;
	
	public GameWindowRepaintThread thread;
	public FloorReloadThread floorThread;

	public GameWindow(){
		super.setTitle("THIS IS SPARTAAA");
		super.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		super.setVisible(true);
		
		thread = new GameWindowRepaintThread(this);
		thread.start();
		
		floorThread = new FloorReloadThread();
		floorThread.start();
	}
	
	public void setFloor(Floor floor){
		this.floor = floor;
		floorThread.setFloor(floor);
	}

	@Override
	public void paint(Graphics g){
		if(floor != null && floor.floorImage != null){
			int width = this.getWidth();
			int height = this.getHeight();

			int imgWidth = floor.floorImage.getWidth();
			int imgHeight = floor.floorImage.getHeight();
			
			int xPos = (width / 2) - (imgWidth / 2);
			int yPos = (height / 2) - (imgHeight / 2);
			
			g.drawImage(floor.floorImage, xPos, yPos, null);
		}
	}
}
