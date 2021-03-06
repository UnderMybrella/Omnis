package org.abimon.omnis.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import static org.abimon.omnis.util.EnumANSI.*;

public class General {

	public static EnumOS OS = EnumOS.OTHER;

	static{
		OS = EnumOS.determineOS();
	}

	public static String[] split(String s, String delimiter, int cap){
		LinkedList<String> strings = new LinkedList<String>();

		for(String str : s.split(delimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)", cap)){
			if(str.startsWith("\""))
				str = str.substring(1);
			if(str.endsWith("\""))
				str = str.substring(0, str.length() - 1);
			strings.add(str);
		}

		return strings.toArray(new String[0]);
	}

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
	public static String getMD5Hash(String s){
		try{
			return getMD5Hash(s.getBytes("UTF-8"));
		}
		catch(Throwable th){
			return s;
		}
	}

	public static String getMD5Hash(byte[] bytes){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytes);
			return new BigInteger(1, thedigest).toString(16);
		}
		catch(Throwable th){
			return "";
		}
	}

	public static LinkedList<File> iterate(File dir, boolean includeDirs){
		LinkedList<File> files = new LinkedList<File>();

		if(dir != null && dir.isDirectory() && dir.listFiles() != null)
			for(File f : dir.listFiles()){
				if(includeDirs || f.isFile())
					files.add(f);
				if(f.isDirectory())
					files.addAll(iterate(f, includeDirs));
			}

		return files;
	}

	public static LinkedList<File> iterateDirs(File dir){
		LinkedList<File> files = new LinkedList<File>();

		files.add(dir);

		try {
			if (dir != null && dir.isDirectory() && dir.exists() && dir.listFiles() != null)
				for (File f : dir.listFiles()) {
					if (f.isDirectory())
						files.addAll(iterateDirs(f));
				}
		}
		catch(NullPointerException npe){} //W h y

		return files;
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
	 * Joins an array with a newline between each element
	 * @param lines
	 * @return
	 */
	public static String join(String[] lines){
		String s = "";
		for(String str : lines)
			s += str + "\n";
		return s.trim();
	}

	public static boolean doesFirstComeBeforeSecond(String first, String second){
		first = first.toLowerCase();
		second = second.toLowerCase();

		if(second.length() < first.length())
			return false;
		if(second.length() > first.length())
			return true;

		for(int i = 0; i < first.length(); i++)
			if(first.charAt(i) > second.charAt(i))
				return false;
			else if(first.charAt(i) < second.charAt(i))
				return true;

		return true;
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

	public static File getRunningLocation(){
		try {
			return new File(General.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {}
		return null;
	}

	public static boolean runningInTerminal(){
		if(System.console() != null)
			return true;
		else if(getRunningLocation().isDirectory())
			return true;
		return false;
	}

	public static int runMenu(String menuGreeting, String[] menu){
		if(OS.hasANSI())
			System.out.print(CURSOR_ERASESCREEN.getCursor(2) + CURSOR_POS.getCursor("0;0"));
		System.out.println(menuGreeting);
		System.out.println("Menu: ");
		for(String s : menu)
			System.out.println("   > " + s);
		int selected = 0;
		try{
			InputStream in = System.in;
			while(true){
				if(OS.hasANSI()){
					for(int i = 0; i < menu.length; i++){
						System.out.print(CURSOR_POS.getCursor((i + 3) + ";" + (4)));
						if(i == selected)
							System.out.print(">");
						else
							System.out.print(" ");
						System.out.print(CURSOR_POS.getCursor((i + 3) + ";" + (menu[i].length() + 7)));
						if(i == selected)
							System.out.print("<");
						else
							System.out.print(" ");
					}
				}
				if(OS.hasANSI())
					System.out.print(CURSOR_POS.getCursor((menu.length + 3) + ";" + 1));
				byte[] data = new byte[4];
				in.read(data);
				if(data[0] == 27 && data[1] == 91)
					if(data[2] == 65)
						selected--;
					else if(data[2] == 66)
						selected++;

				if(data[0] == 119 || data[0] == 87)
					selected--;
				else if(data[0] == 115 || data[0] == 83)
					selected++;

				if(selected < 0)
					selected = 0;
				else if(selected >= menu.length)
					selected = menu.length - 1;
				if(data[0] == 10)
					return selected;
			}
		}
		catch(Throwable th){
		}
		return 0;
	}

	/**
	 * 
	 * @param date
	 * @param formattingString dd for day, mm for month, yyyy for year
	 * @return
	 */
	public static String formatDate(Date date, String formattingString){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String formatted = formattingString.toLowerCase();
		formatted = formatted.replace("dd", "" + cal.get(Calendar.DAY_OF_MONTH));
		formatted = formatted.replace("mm", "" + (cal.get(Calendar.MONTH) + 1));
		formatted = formatted.replace("yyyy", "" + cal.get(Calendar.YEAR));

		formatted = formatted.replace("hh", "" + cal.get(Calendar.HOUR_OF_DAY));
		formatted = formatted.replace("min", "" + cal.get(Calendar.MINUTE));
		formatted = formatted.replace("ss", "" + cal.get(Calendar.SECOND));
		return formatted;
	}

	public static String formatDate(Date date){
		return formatDate(date, "dd-mm-yyyy hh:min:ss");
	}

	public static String formatDate(){
		return formatDate(new Date());
	}

	/** Formatting String: %day, %month, %year, %hh, %min, %sec*/
	public static String timeDifference(LocalDateTime timeAgo, String formattingString){
		LocalDateTime now = LocalDateTime.now();
		String formatted = formattingString.toLowerCase();
		
		LocalDateTime modified = LocalDateTime.ofEpochSecond(now.toEpochSecond(ZoneOffset.UTC) - timeAgo.toEpochSecond(ZoneOffset.UTC), 0, ZoneOffset.UTC);
		
		formatted = formatted.replace("%day", Integer.toString(modified.getDayOfMonth() - 1));
		formatted = formatted.replace("%month", Integer.toString(modified.getMonthValue() - 1));
		formatted = formatted.replace("%year", Integer.toString(modified.getYear() - 1970));

		formatted = formatted.replace("%hh", Integer.toString(modified.getHour()));
		formatted = formatted.replace("%min", Integer.toString(modified.getMinute()));
		formatted = formatted.replace("%sec", Integer.toString(modified.getSecond()));
		
		return formatted;
	}

	public static String splitEveryXCharacters(String s, int chars){
		String newS = "";
		String tmp = "";
		for(String word : s.split("\\s+")){
			if((tmp + word).length() >= chars){
				newS += tmp.trim() + "\n";
				tmp = "";
			}
			tmp += word + " ";
		}

		newS += tmp;

		return newS.trim();
	}

	public static String printStackTrace(Throwable th){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		th.printStackTrace(new PrintStream(baos));
		return new String(baos.toByteArray());
	}
}
