package org.abimon.omnis.ludus;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
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

		thread = new GameWindowRepaintThread(this);

		floorThread = new FloorReloadThread();
		for(int i = 0; i < LayerList.LAYER_COUNT; i++)
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
		for(int layer = 0; layer < LayerList.LAYER_COUNT; layer++)
			if(floor != null && floor.getImage(layer) != null){
				int width = this.getWidth();
				int height = this.getHeight();

				int imgWidth = floor.getImage(layer).getWidth();
				int imgHeight = floor.getImage(layer).getHeight();

				int xPos = (width / 2) - (imgWidth / 2);
				int yPos = (height / 2) - (imgHeight / 2);

				BufferedImage img = floor.getImage(layer);

				g.drawImage(img, xPos, yPos, null);
				System.out.println("Layer " + layer + " is rendered");
			}
			else
				System.out.println("Layer " + layer + " is null");
	}

	public void setVisible(boolean b) {
		thread.start();
		//floorThread.start();
		for(int i = 0; i < layerThreads.size(); i++)
			layerThreads.get(i).start();
		super.setVisible(b);
	}
}
