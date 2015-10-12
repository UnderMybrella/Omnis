package org.abimon.omnis.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

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
					return false;
		return true;
	}
}
