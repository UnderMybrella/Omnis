package org.abimon.omnis.ludus.gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public interface Gui extends KeyListener{
	
	public void render(Graphics g);

	public void dismiss();

	public void keyTyped(KeyEvent e);

	public void keyPressed(KeyEvent e);

	public void keyReleased(KeyEvent e);
}
