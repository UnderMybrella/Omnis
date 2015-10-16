package org.abimon.omnis.ludus;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

import javax.imageio.ImageIO;
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
		
		thread = new GameWindowRepaintThread(this);
		
		floorThread = new FloorReloadThread();
		for(int i = 0; i < 3; i++)
		{
			LayerReloadThread thread = new LayerReloadThread(i);
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
		if(floor != null && floor.getImage() != null){
			int width = this.getWidth();
			int height = this.getHeight();

			int imgWidth = floor.getImage().getWidth();
			int imgHeight = floor.getImage().getHeight();
			
			int xPos = (width / 2) - (imgWidth / 2);
			int yPos = (height / 2) - (imgHeight / 2);
			
			BufferedImage img = floor.getImage();
			
			g.drawImage(img, xPos, yPos, null);
			File f = new File(new Date().toString() + ".png");
			System.out.println(img + "\n" + xPos + ":" + yPos);
			try {
				ImageIO.write(img, "PNG", f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    public void setVisible(boolean b) {
    	thread.start();
    	floorThread.start();
    	for(int i = 0; i < layerThreads.size(); i++)
    		layerThreads.get(i).start();
        super.setVisible(b);
    }
}
