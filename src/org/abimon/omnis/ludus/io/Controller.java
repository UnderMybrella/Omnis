package org.abimon.omnis.ludus.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.codeminders.hidapi.HIDDevice;

public interface Controller {
	
	public String getControllerName();
	public boolean doesNameMatch(String name);
	
	public String[] getAllButtons();
	public boolean isButtonPressed(String button);
	public void setButtonPressed(String button, boolean pressed);
	
	public String[] getAnalogueSticks();
	public int getAnalogueStickX(String stick);
	public int getAnalogueStickY(String stick);
	
	public int getAnalogueStickXPositiveBoundary(String stick);
	public int getAnalogueStickYPositiveBoundary(String stick);
	
	public int getAnalogueStickXNegativeBoundary(String stick);
	public int getAnalogueStickYNegativeBoundary(String stick);
	
	/** 'out' and 'in' are optional and may be null */
	public Controller getNewInstance(HIDDevice device, OutputStream out, InputStream in);
	
	public void vibrate(long millis) throws IOException;
	
	public OutputStream getOutputStream();
	
}
