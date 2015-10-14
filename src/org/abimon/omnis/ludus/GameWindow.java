package org.abimon.omnis.ludus;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.LinkedList;

import javax.swing.JFrame;

import org.abimon.omnis.ludus.multithreading.FloorReloadThread;
import org.abimon.omnis.ludus.multithreading.GameWindowRepaintThread;
import org.abimon.omnis.ludus.multithreading.LayerReloadThread;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private volatile Floor floor;
	
	public GameWindowRepaintThread thread;
	public FloorReloadThread floorThread;
	
	public LinkedList<LayerReloadThread> layerThreads = new LinkedList<LayerReloadThread>();

	public GameWindow(){
		super.setTitle("THIS IS SPARTAAA");
		super.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		//super.setVisible(true);
		
		thread = new GameWindowRepaintThread(this);
		thread.start();
		
		floorThread = new FloorReloadThread();
		floorThread.start();
		
		for(int i = 0; i < 3; i++)
		{
			LayerReloadThread thread = new LayerReloadThread(i);
			thread.start();
			layerThreads.add(thread);
		}
	}
	
	public void setFloor(Floor floor){
		this.floor = floor;
		floorThread.setFloor(floor);
		for(LayerReloadThread thread : layerThreads)
			thread.setFloor(floor);
	}
	
	public Floor getFloor(){
		return floor;
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
