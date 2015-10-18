package org.abimon.omnis.ludus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;

import org.abimon.omnis.ludus.multithreading.FloorReloadThread;
import org.abimon.omnis.ludus.multithreading.GameWindowRepaintThread;
import org.abimon.omnis.ludus.multithreading.LayerReloadThread;

public class GameWindow extends JFrame implements KeyListener{
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
		
		this.addKeyListener(this);
		this.setBackground(Color.white);
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
		int width = this.getWidth();
		int height = this.getHeight();

		int imgWidth = floor.getTileWidth() * floor.FLOOR_SCALE_X;
		int imgHeight = (floor.getTileHeight() + (Ludus.thePlayer.getIcon().getHeight() / floor.FLOOR_SCALE_Y)) * floor.FLOOR_SCALE_Y;

		int xPos = (width / 2) - (imgWidth / 2);
		int yPos = (height / 2) - (imgHeight / 2);
		
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		
		for(int layer = 0; layer < LayerList.LAYER_COUNT; layer++)
			if(layer == LayerList.ENTITY_LAYER){
				g.drawImage(Ludus.thePlayer.getIcon(), Ludus.thePlayer.getX() * floor.FLOOR_SCALE_X + xPos, Ludus.thePlayer.getY() * floor.FLOOR_SCALE_Y + yPos, null);
			}
			else
				if(floor != null && floor.getImage(layer) != null){
					BufferedImage img = floor.getImage(layer);

					g.drawImage(img, xPos, yPos, null);
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

	@Override
	public void keyTyped(KeyEvent e) {
		System.out.println(e);
		char key = (e.getKeyChar() + "").toLowerCase().charAt(0);
		if(key == 'w' || key == 'a' || key == 's' || key == 'd')
		{
			Ludus.thePlayer.dir = key == 'w' ? 3 : key == 'a' ? 1 : key == 's' ? 0 : 2;
			if(Ludus.thePlayer.dir == 3)
				Ludus.thePlayer.y--;
			if(Ludus.thePlayer.dir == 1)
				Ludus.thePlayer.x--;
			if(Ludus.thePlayer.dir == 0)
				Ludus.thePlayer.y++;
			if(Ludus.thePlayer.dir == 2)
				Ludus.thePlayer.x++;
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
