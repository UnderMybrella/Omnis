package org.abimon.omnis.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author graham from http://www.darkcoding.net
 *
 */
public class TTYConfig {

	public static String ttyConfig = "";

	public static void setTerminalToCBreak() throws IOException, InterruptedException {
		
		if(System.console() == null)
			throw new IOException("No console object! Exiting for safety precautions");

		ttyConfig = stty("-g");
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run(){
				try {
					stty(ttyConfig.trim());
					System.out.println("Returned Stty");
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// set the console to be character-buffered instead of line-buffered
		stty("-icanon min 1");

		// disable character echoing
		stty("-echo");
	}
	
	public static void setTerminalToLBreak() throws IOException, InterruptedException {
		
		if(System.console() == null)
			throw new IOException("No console object! Exiting for safety precautions");

		if(!ttyConfig.equals(""))
			stty(ttyConfig.trim());
	}

	/**
	 *  Execute the stty command with the specified arguments
	 *  against the current active terminal.
	 */
	public static String stty(final String args)
			throws IOException, InterruptedException {
		String cmd = "stty " + args + " < /dev/tty";

		return exec(new String[] {
				"sh",
				"-c",
				cmd
		});
	}

	/**
	 *  Execute the specified command and return the output
	 *  (both stdout and stderr).
	 */
	public static String exec(final String[] cmd)
			throws IOException, InterruptedException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();

		Process p = Runtime.getRuntime().exec(cmd);
		int c;
		InputStream in = p.getInputStream();

		while ((c = in.read()) != -1) {
			bout.write(c);
		}

		in = p.getErrorStream();

		while ((c = in.read()) != -1) {
			bout.write(c);
		}

		p.waitFor();

		String result = new String(bout.toByteArray());
		return result;
	}

}
