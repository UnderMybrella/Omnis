package org.abimon.omnis.ludus.io;

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
	
	public Controller getNewInstance(HIDDevice device);
	
}
