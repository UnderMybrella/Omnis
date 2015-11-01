package org.abimon.omnis.ludus.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.util.General;

public class GuiTextOverlay implements Gui{

	public String text;
	public BufferedImage[] imgs;
	public Font font;

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
		this.text = text;
		this.font = font;
		BufferedImage img = Ludus.getDataUnsafe(textBoxLocation).getAsImage();
		imgs = new BufferedImage[9];
		for(int x = 0; x < 3; x++)
			for(int y = 0; y < 3; y++)
				imgs[x * 3 + y] = img.getSubimage(x * (img.getWidth() / 3), y * (img.getHeight() / 3), img.getWidth() / 3, img.getHeight() / 3);
	}

	@Override
	public void render(Graphics g) {
		g.setFont(font);
		g.setColor(Color.BLACK);
		FontMetrics metrics = g.getFontMetrics();

		Font font = g.getFont();
		int width = Ludus.mainWindow.getFloor().getWidth();
		double fontLineHeight = metrics.getHeight() * 1.75;
		int lines = 0;
		for(String s : text.split("\n"))
		{
			int totalLineLength = (int) metrics.getStringBounds(s, g).getWidth();
			lines += (totalLineLength / (width - imgs[1].getWidth() - imgs[7].getWidth()) + 1);
		}
		int height = (int) ((fontLineHeight * lines) + imgs[0].getHeight() + imgs[2].getHeight());
		Point coords = General.getStartingPoint(Ludus.mainWindow.getWidth(), Ludus.mainWindow.getHeight(), width, Ludus.mainWindow.getFloor().getHeight());

		BufferedImage textBox = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics txt = textBox.getGraphics();
		int widthDifference = width - imgs[0].getWidth() - imgs[6].getWidth();
		txt.drawImage(imgs[0], 0, 0, null);
		for(int x = 0; x < widthDifference / imgs[3].getWidth(); x++)
			txt.drawImage(imgs[3], imgs[0].getWidth() + x * imgs[3].getWidth(), 0, null);
		if(widthDifference % imgs[3].getWidth() == 0);
		txt.drawImage(imgs[6], width - imgs[6].getWidth(), 0, null);

		int heightDifference = height - imgs[0].getHeight() - imgs[2].getHeight();
		for(int y = 0; y < heightDifference / imgs[1].getHeight(); y++){
			txt.drawImage(imgs[1], 0, imgs[0].getHeight() + y * imgs[1].getHeight(), null);
			for(int x = 0; x < widthDifference / imgs[3].getWidth(); x++)
				txt.drawImage(imgs[4], imgs[1].getWidth() + x * imgs[4].getWidth(), imgs[1].getHeight() + y * imgs[4].getHeight(), null);
			if(widthDifference % imgs[3].getWidth() == 0);
			txt.drawImage(imgs[7], width - imgs[7].getWidth(), imgs[1].getHeight() + y * imgs[7].getHeight(), null);
		}

		if(heightDifference % imgs[1].getHeight() != 0){
			int y = heightDifference / imgs[1].getHeight();
			txt.drawImage(imgs[1], 0, imgs[0].getHeight() + y * imgs[1].getHeight(), imgs[1].getWidth(), heightDifference % imgs[1].getHeight(), null);
			for(int x = 0; x < widthDifference / imgs[3].getWidth(); x++)
				txt.drawImage(imgs[4], imgs[1].getWidth() + x * imgs[4].getWidth(), imgs[1].getHeight() + y * imgs[4].getHeight(), imgs[4].getWidth(), heightDifference % imgs[4].getHeight(), null);
			if(widthDifference % imgs[3].getWidth() == 0);
			txt.drawImage(imgs[7], width - imgs[7].getWidth(), imgs[1].getHeight() + y * imgs[7].getHeight(), imgs[7].getWidth(), heightDifference % imgs[7].getHeight(), null);
		}

		txt.drawImage(imgs[2], 0, height - imgs[2].getHeight(), null);
		for(int x = 0; x < widthDifference / imgs[6].getWidth(); x++)
			txt.drawImage(imgs[5], imgs[2].getWidth() + x * imgs[5].getWidth(), height - imgs[5].getHeight(), null);
		if(widthDifference % imgs[3].getWidth() == 0);
		txt.drawImage(imgs[8], width - imgs[8].getWidth(), height - imgs[2].getHeight(), null);

		g.drawImage(textBox, coords.x, coords.y + Ludus.mainWindow.getFloor().getHeight() - textBox.getHeight(), null);
		int lineCount = 0;
		for(String line : text.split("\n")){
			int lineWidthThing = (width - imgs[1].getWidth() - imgs[7].getWidth());
			String[] sublines = getSubstrings(line, g, metrics, lineWidthThing);
			for(String subLine : sublines){
				g.drawString(subLine.trim(), coords.x + ((int) (imgs[0].getWidth() * 1.5)), (int) (lineCount * (font.getSize() * 1.25)) + coords.y + Ludus.mainWindow.getFloor().getHeight() - textBox.getHeight() + ((int) (imgs[0].getHeight() * 2.5)));
				lineCount++;
			}
		}
	}

	public static String[] getSubstrings(String line, Graphics g, FontMetrics metrics, int width) {
		String[] lines = new String[(int) (metrics.getStringBounds(line, g).getWidth() / width) + 1];
		for(int i = 0; i < lines.length - 1; i++){
			String subline = "";
			int j = 0;
			for(; j < line.length(); j++){
				subline += line.charAt(j);
				if((int) (metrics.getStringBounds(subline, g).getWidth() / width) != 0)
					break;
			}
			int lastSpace = subline.lastIndexOf(' ');
			subline = subline.substring(0, lastSpace);
			line = line.substring(lastSpace);
			lines[i] = subline;
		}
		lines[lines.length - 1] = line;
		return lines;
	}

	@Override
	public void dismiss() {

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}
