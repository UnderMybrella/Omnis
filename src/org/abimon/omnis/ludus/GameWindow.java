package org.abimon.omnis.ludus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;

import org.abimon.omnis.ludus.multithreading.FloorReloadThread;
import org.abimon.omnis.ludus.multithreading.GameWindowRepaintThread;
import org.abimon.omnis.ludus.multithreading.LayerReloadThread;

public class GameWindow extends JFrame{
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

		this.setBackground(Color.white);

		buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
	}

	public void setFloor(Floor floor){
		this.floor = floor;
		//		floorThread.setFloor(floor);
		for(LayerReloadThread thread : layerThreads)
			thread.setFloor(floor);
	}

	public Floor getFloor(){
		return floor;
	}

	public BufferedImage buffer;

	@Override
	public void paint(Graphics g2){
		int width = this.getWidth();
		int height = this.getHeight();

		int imgWidth = floor.getTileWidth() * floor.FLOOR_SCALE_X;
		int imgHeight = floor.getTileHeight() * floor.FLOOR_SCALE_Y;

		int xPos = (width / 2) - (imgWidth / 2);
		int yPos = (height / 2) - (imgHeight / 2);

		Graphics g = buffer.getGraphics();

		g.setColor(this.getBackground());
		g.fillRect(0, 0, width, height);

		for(int layer = 0; layer < LayerList.LAYER_COUNT; layer++)
			if(layer == LayerList.ENTITY_LAYER){
				if(Ludus.thePlayer != null)
					g.drawImage(Ludus.thePlayer.getIcon(), (int) (Ludus.thePlayer.getX() * floor.FLOOR_SCALE_X + xPos), (int) (Ludus.thePlayer.getY() * floor.FLOOR_SCALE_Y + yPos), (int) (floor.FLOOR_SCALE_X * Ludus.thePlayer.getScaleX()), (int) (floor.FLOOR_SCALE_Y * Ludus.thePlayer.getScaleY()), null);
			}
			else
				if(floor != null && floor.getImage(layer) != null){
					g.drawImage(floor.getImage(layer), xPos, yPos, null);
				}
				else
					System.out.println("Layer " + layer + " is null");

		if(Ludus.guiInUse != null)
			Ludus.guiInUse.render(g);
		g.dispose();
		g2.drawImage(buffer, 0, 0, null);
	}

	public void setVisible(boolean b) {
		thread.start();
		floorThread.start();
		for(int i = 0; i < layerThreads.size(); i++)
			layerThreads.get(i).start();
		super.setVisible(b);
	}
}
