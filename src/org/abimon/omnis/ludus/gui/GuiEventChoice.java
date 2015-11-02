package org.abimon.omnis.ludus.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.reflect.Function;

public class GuiEventChoice extends GuiTextOverlay {

	HashMap<String, Function> events = new HashMap<String, Function>();
	HashMap<String, String[]> eventText = new HashMap<String, String[]>();

	BufferedImage selector;
	int choice = 0;

	/**
	 * Creates a Text gui that gives occasional events
	 * @param text The text to use. This will get separated down into individual lines. 
	 * @param events The events to use. Note that the key is a line of text <i>that does get displayed</i>
	 * @param textBoxLocation
	 * @param font
	 */
	public GuiEventChoice(String text, HashMap<String, String[]> eventText, HashMap<String, Function> events, String textBoxLocation, Font font) {
		super(text, textBoxLocation, font);
		this.events = events;
		this.eventText = eventText;

		selector = Ludus.getDataUnsafe("resources/Selector.png").getAsImage();
	}


	public void render(Graphics g) {
		super.render(g);
		FontMetrics metrics = g.getFontMetrics();
		String key = text.get(Math.min(3, text.size()) - 1);
		if(eventText.containsKey(key)){
			int width = 0;
			String[] responses = eventText.get(key);
			for(String s : responses) {
				int strWidth = (int) (metrics.getStringBounds(s, g).getWidth() * 1.25);
				if(strWidth > width)
					width = strWidth;
			}
			width += imgs[1].getWidth() + imgs[7].getWidth() + selector.getWidth();
			int height = imgs[3].getHeight() + imgs[5].getHeight() + (int) (metrics.getHeight() * eventText.get(key).length * 1.75);
			BufferedImage eventBox = constructTextBox(width, height);
			g.drawImage(eventBox, coords.x + Ludus.mainWindow.getFloor().getWidth() - eventBox.getWidth(), coords.y - eventBox.getHeight(), null);		
			for(int i = 0; i < responses.length; i++){
				g.drawString(responses[i], coords.x + selector.getWidth() + Ludus.mainWindow.getFloor().getWidth() - eventBox.getWidth() + ((int) (imgs[0].getWidth() * 1.25)), (int) (i * metrics.getHeight() * 1.75) + coords.y - eventBox.getHeight() + ((int) (imgs[3].getHeight() * 2.5)));
				if(i == choice)
					g.drawImage(selector, coords.x + Ludus.mainWindow.getFloor().getWidth() - eventBox.getWidth() + ((int) (imgs[0].getWidth() * 1.125)), (int) (i * metrics.getHeight() * 1.75) - selector.getHeight() + coords.y - eventBox.getHeight() + ((int) (imgs[3].getHeight() * 2.5)), null);
			}

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		String key = text.get(Math.min(3, text.size()) - 1);
		if(eventText.containsKey(key)){
			String[] responses = eventText.get(key);
			if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_KP_UP || e.getKeyCode() == KeyEvent.VK_W)
				choice--;
			if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_KP_DOWN || e.getKeyCode() == KeyEvent.VK_S)
				choice++;

			if(choice >= responses.length)
				choice = responses.length - 1;
			if(choice <= 0)
				choice = 0;
			
			if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE)
				events.get(key).invokeUnsafePartially(new Object[]{responses[choice]});
		}
		else
			super.keyPressed(e);
	}

}
