package org.abimon.omnis.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

public class General {

	@SuppressWarnings("unchecked")
	public static <T extends Cloneable> T clone(T cloning){
		try{
			Method cloneMethod = cloning.getClass().getMethod("clone");
			return (T) cloneMethod.invoke(cloning);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T unsafeClone(T cloning){
		if(cloning instanceof Cloneable){
			try{
				Method cloneMethod = cloning.getClass().getMethod("clone");
				return (T) cloneMethod.invoke(cloning);
			}
			catch(Throwable th){
				th.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * Converts a given Image into a BufferedImage
	 *
	 * @param img The Image to be converted
	 * @return The converted BufferedImage
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    return bimage;
	}
	
	/**
	 * Checks if two images are equal
	 * @param img
	 * @param img
	 * @return
	 */
	public static boolean equal(BufferedImage img, BufferedImage img2){
		if(img == null || img2 == null)
			return img == img2;
		if(img.getHeight() != img2.getHeight())
			return false;
		if(img.getWidth() != img2.getWidth())
			return false;
		for(int x = 0; x < img.getWidth(); x++)
			for(int y = 0; y < img.getHeight(); y++)
				if(img.getRGB(x, y) != img2.getRGB(x, y))
				{
					return false;
				}
		return true;
	}
	
	/**
	 * Checks if two images are equal
	 * @param img
	 * @param img
	 * @return
	 */
	public static Point[] differences(BufferedImage img, BufferedImage img2){
		if(img == null || img2 == null)
			return new Point[0];
		if(img.getHeight() != img2.getHeight())
			return new Point[0];
		if(img.getWidth() != img2.getWidth())
			return new Point[0];
		LinkedList<Point> diff = new LinkedList<Point>();
		for(int x = 0; x < img.getWidth(); x++)
			for(int y = 0; y < img.getHeight(); y++)
				if(!new Color(img.getRGB(x, y), true).equals(new Color(img2.getRGB(x, y), true))){
					System.out.println(new Color(img.getRGB(x, y)) + " is *apparently* not equal to " + new Color(img2.getRGB(x, y)));
					System.out.println(new Color(img.getRGB(x, y)).getAlpha() + " mutter mutter mutter " + new Color(img2.getRGB(x, y)).getAlpha());
					diff.add(new Point(x, y));
				}
		return diff.toArray(new Point[0]);
	}
	
	public static Point getStartingPoint(int width, int height, int imgWidth, int imgHeight){
		int xPos = (width / 2) - (imgWidth / 2);
		int yPos = (height / 2) - (imgHeight / 2);
		
		return new Point(xPos, yPos);
	}
	
	public static BufferedImage[] getImagesInGrid(BufferedImage img, int width, int height){
		BufferedImage[] imgs = new BufferedImage[width * height];
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				imgs[x * width + y] = img.getSubimage(x * (img.getWidth() / width), y * (img.getHeight() / height), img.getWidth() / width, img.getHeight() / height);
		return imgs;
	}
	
	public static <K, V> HashMap<K, V> createHashmap(K[] keys, V[] values){
		HashMap<K, V> hashmap = new HashMap<K, V>();
		for(int i = 0; i < keys.length; i++)
			hashmap.put(keys[i], i < values.length ? values[i] : null);
		return hashmap;
	}
}
