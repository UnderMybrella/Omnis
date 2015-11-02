package org.abimon.omnis.ludus.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.util.General;

public class GuiTextOverlay implements Gui{

	int step = -1;
	long waitingOn = 0L;
	public LinkedList<String> text = null;
	public final String originalText;
	public BufferedImage[] imgs;
	public BufferedImage[] arrow;
	public Font font;
	
	protected Point coords;

	public GuiTextOverlay(String text){
		this(text, "resources/TextBox.png");
	}

	public GuiTextOverlay(String text, String textBoxLocation){
		this(text, textBoxLocation, Font.getFont("Times New Roman"));
	}

	public GuiTextOverlay(String text, Font font) {
		this(text, "resources/TextBox.png", font);
	}

	public GuiTextOverlay(String text, String textBoxLocation, Font font){
		originalText = text;
		this.font = font;
		imgs = General.getImagesInGrid(Ludus.getDataUnsafe(textBoxLocation).getAsImage(), 3, 3);
		arrow = General.getImagesInGrid(Ludus.getDataUnsafe("resources/Arrow.png").getAsImage(), 2, 2);
	}

	public BufferedImage constructTextBox(int width, int height){
		BufferedImage textBox = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics txt = textBox.getGraphics();
		int widthDifference = width - imgs[0].getWidth() - imgs[6].getWidth();
		txt.drawImage(imgs[0], 0, 0, null);
		for(int x = 0; x < widthDifference / imgs[3].getWidth(); x++)
			txt.drawImage(imgs[3], imgs[0].getWidth() + x * imgs[3].getWidth(), 0, null);
		if(widthDifference % imgs[3].getWidth() != 0)
			txt.drawImage(imgs[3], widthDifference, 0, null);
		txt.drawImage(imgs[6], width - imgs[6].getWidth(), 0, null);

		int heightDifference = height - imgs[0].getHeight() - imgs[2].getHeight();
		for(int y = 0; y < heightDifference / imgs[1].getHeight(); y++){
			txt.drawImage(imgs[1], 0, imgs[0].getHeight() + y * imgs[1].getHeight(), null);
			for(int x = 0; x < widthDifference / imgs[3].getWidth(); x++)
				txt.drawImage(imgs[4], imgs[1].getWidth() + x * imgs[4].getWidth(), imgs[1].getHeight() + y * imgs[4].getHeight(), null);
			if(widthDifference % imgs[4].getWidth() != 0)
				txt.drawImage(imgs[4], widthDifference, imgs[1].getHeight() + y * imgs[4].getHeight(), null);
			txt.drawImage(imgs[7], width - imgs[7].getWidth(), imgs[1].getHeight() + y * imgs[7].getHeight(), null);
		}

		if(heightDifference % imgs[1].getHeight() != 0){
			int y = heightDifference / imgs[1].getHeight();
			txt.drawImage(imgs[1], 0, imgs[0].getHeight() + y * imgs[1].getHeight(), imgs[1].getWidth(), heightDifference % imgs[1].getHeight(), null);
			for(int x = 0; x < widthDifference / imgs[3].getWidth(); x++)
				txt.drawImage(imgs[4], imgs[1].getWidth() + x * imgs[4].getWidth(), imgs[1].getHeight() + y * imgs[4].getHeight(), imgs[4].getWidth(), heightDifference % imgs[4].getHeight(), null);
			if(widthDifference % imgs[3].getWidth() != 0)
				txt.drawImage(imgs[4], widthDifference, imgs[1].getHeight() + y * imgs[4].getHeight(), null);
			txt.drawImage(imgs[7], width - imgs[7].getWidth(), imgs[1].getHeight() + y * imgs[7].getHeight(), imgs[7].getWidth(), heightDifference % imgs[7].getHeight(), null);
		}

		txt.drawImage(imgs[2], 0, height - imgs[2].getHeight(), null);
		for(int x = 0; x < widthDifference / imgs[6].getWidth(); x++)
			txt.drawImage(imgs[5], imgs[2].getWidth() + x * imgs[5].getWidth(), height - imgs[5].getHeight(), null);
		if(widthDifference % imgs[3].getWidth() != 0)
			txt.drawImage(imgs[5], widthDifference, height - imgs[5].getHeight(), null);
		txt.drawImage(imgs[8], width - imgs[8].getWidth(), height - imgs[2].getHeight(), null);
		return textBox;
	}

	@Override
	public void render(Graphics g) {
		g.setFont(font);
		g.setColor(Color.BLACK);
		FontMetrics metrics = g.getFontMetrics();

		Font font = g.getFont();
		int width = Ludus.mainWindow.getFloor().getWidth();

		if(text == null)
		{
			text = new LinkedList<String>();
			for(String str : originalText.split("\n")){
				String[] strings = getSubstrings(str, g, metrics, (width - imgs[1].getWidth() - imgs[7].getWidth()));
				for(String s : strings)
					text.add(s.trim());
			}
		}

		double fontoriginalTextHeight = metrics.getHeight() * 1.75;
		int originalTexts = Math.min(3, text.size());
		int height = (int) ((fontoriginalTextHeight * originalTexts) + imgs[0].getHeight() + imgs[2].getHeight());

		BufferedImage textBox = constructTextBox(width, height);
		
		coords = General.getStartingPoint(Ludus.mainWindow.getWidth(), Ludus.mainWindow.getHeight(), width, Ludus.mainWindow.getFloor().getHeight());
		coords.y += Ludus.mainWindow.getFloor().getHeight() - textBox.getHeight();

		g.drawImage(textBox, coords.x, coords.y, null);
		for(int i = 0; i < originalTexts; i++){
			g.drawString(text.get(i), coords.x + ((int) (imgs[0].getWidth() * 1.5)), (int) (i * (font.getSize() * 1.25)) + coords.y + ((int) (imgs[0].getHeight() * 2.5)));
		}

		if(step < 0)
		{
			System.out.println(System.currentTimeMillis());
			waitingOn = System.currentTimeMillis() + 250;
			step = 0;
		}
		else{
			if(System.currentTimeMillis() >= waitingOn)
			{
				step++;
				if(step >= 4)
					step = 0;
				waitingOn = System.currentTimeMillis() + 250;
			}
		}
		g.drawImage(arrow[step], coords.x + width - (int) (imgs[8].getWidth() * 2.5), coords.y + textBox.getHeight() - (int) (imgs[8].getHeight() * 2.5), null);
	}

	public static String[] getSubstrings(String originalText, Graphics g, FontMetrics metrics, int width) {
		String[] originalTexts = new String[(int) (metrics.getStringBounds(originalText, g).getWidth() / width) + 1];
		for(int i = 0; i < originalTexts.length - 1; i++){
			String suboriginalText = "";
			int j = 0;
			for(; j < originalText.length(); j++){
				suboriginalText += originalText.charAt(j);
				if((int) (metrics.getStringBounds(suboriginalText, g).getWidth() / width) != 0)
					break;
			}
			int lastSpace = suboriginalText.lastIndexOf(' ');
			suboriginalText = suboriginalText.substring(0, lastSpace);
			originalText = originalText.substring(lastSpace);
			originalTexts[i] = suboriginalText;
		}
		originalTexts[originalTexts.length - 1] = originalText;
		return originalTexts;
	}

	@Override
	public void dismiss() {

	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE)
			if(text != null)
				if(text.size() <= 3)
					Ludus.dismissGui();
				else
					text.poll();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}
