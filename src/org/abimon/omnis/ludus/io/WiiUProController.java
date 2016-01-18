package org.abimon.omnis.ludus.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.abimon.omnis.hid.io.HIDInputStream;
import org.abimon.omnis.hid.io.HIDOutputStream;

import com.codeminders.hidapi.HIDDevice;

public class WiiUProController extends Thread implements Controller{

	InputStream in;
	OutputStream out;

	public boolean A_PRESSED;
	public boolean B_PRESSED;
	public boolean X_PRESSED;
	public boolean Y_PRESSED;

	public boolean L_PRESSED;
	public boolean R_PRESSED;

	public boolean ZL_PRESSED;
	public boolean ZR_PRESSED;

	public boolean START_PRESSED;
	public boolean SELECT_PRESSED;

	public boolean HOME_PRESSED;

	public boolean LEFT_PRESSED;
	public boolean RIGHT_PRESSED;
	public boolean UP_PRESSED;
	public boolean DOWN_PRESSED;

	public boolean LEFT_STICK_PRESSED;
	public boolean RIGHT_STICK_PRESSED;

	public int LEFT_STICK_HORIZONTAL;
	public int LEFT_STICK_VERTICAL;

	public int RIGHT_STICK_HORIZONTAL;
	public int RIGHT_STICK_VERTICAL;

	public byte[] recentData;
	
	public WiiUProController(){}

	public WiiUProController(InputStream in, OutputStream out){
		this.in = in;
		this.out = out;
		this.start();
	}

	public void process(byte[] data){
		A_PRESSED = bitMask(data[1], -128);
		B_PRESSED = bitMask(data[2], 1);
		X_PRESSED = bitMask(data[2], 2);
		Y_PRESSED = bitMask(data[2], 4);

		L_PRESSED = bitMask(data[2], 8);
		R_PRESSED = bitMask(data[2], 16);

		ZL_PRESSED = bitMask(data[2], 32);
		ZR_PRESSED = bitMask(data[2], 64);

		START_PRESSED = bitMask(data[0], 64);
		SELECT_PRESSED = bitMask(data[0], -128);

		HOME_PRESSED = bitMask(data[1], 1);

		UP_PRESSED = bitMask(data[1], 8);
		DOWN_PRESSED = bitMask(data[1], 16);
		LEFT_PRESSED = bitMask(data[1], 32);
		RIGHT_PRESSED = bitMask(data[1], 64);

		LEFT_STICK_PRESSED = bitMask(data[2], -128);
		RIGHT_STICK_PRESSED = bitMask(data[3], 1);

		LEFT_STICK_HORIZONTAL = data[4];
		LEFT_STICK_VERTICAL = data[5];
		RIGHT_STICK_HORIZONTAL = data[6];
		RIGHT_STICK_VERTICAL = data[7];
	}

	public boolean bitMask(byte num, int check){
		return (num & check) == check;
	}

	public void print(){
		System.out.println("A Button: " + A_PRESSED);
		System.out.println("B Button: " + B_PRESSED);
		System.out.println("X Button: " + X_PRESSED);
		System.out.println("Y Button: " + Y_PRESSED);

		System.out.println("L Button: " + L_PRESSED);
		System.out.println("R Button: " + R_PRESSED);
		System.out.println("ZL Button: " + ZL_PRESSED);
		System.out.println("ZR Button: " + ZR_PRESSED);

		System.out.println("Start Button: " + START_PRESSED);
		System.out.println("Select Button: " + SELECT_PRESSED);

		System.out.println("Home Button: " + HOME_PRESSED);

		System.out.println("Up Button: " + UP_PRESSED);
		System.out.println("Down Button: " + DOWN_PRESSED);
		System.out.println("Left Button: " + LEFT_PRESSED);
		System.out.println("Right Button: " + RIGHT_PRESSED);

		System.out.println("Left Horizontal: " + LEFT_STICK_HORIZONTAL);
	}
	
	public void setLEDs(boolean[] leds) throws IOException{
		boolean[] ledU = new boolean[4];
		for(int i = 0; i < Math.min(leds.length, 4); i++)
			ledU[i] = leds[i];
		
		byte mask = 0;
		for(int i = 0; i < 4; i++)
			if(ledU[i])
				mask += Math.pow(2, 4+i);
		
		out.write(new byte[]{(byte) 162, 17, mask});
	}

	public void run(){
		while(true){
			try{
				byte[] data = new byte[8];
				int read = in.read(data);
				if(read <= 0)
					continue;

				process(data);

				recentData = data;
			}
			catch(Throwable th){
				th.printStackTrace();
			}
		}
	}

	@Override
	public String[] getAllButtons() {
		return new String[]{"A", "B", "X", "Y", "L", "R", "ZL", "ZR", "START", "SELECT", "HOME", "UP", "DOWN", "LEFT", "RIGHT"};
	}

	@Override
	public boolean isButtonPressed(String button) {
		if(button.equals("A"))
			return A_PRESSED;
		if(button.equals("B"))
			return B_PRESSED;
		if(button.equals("X"))
			return X_PRESSED;
		if(button.equals("Y"))
			return Y_PRESSED;
		
		if(button.equals("L"))
			return L_PRESSED;
		if(button.equals("R"))
			return R_PRESSED;
		if(button.equals("ZL"))
			return ZL_PRESSED;
		if(button.equals("ZR"))
			return ZR_PRESSED;

		if(button.equals("START"))
			return START_PRESSED;
		if(button.equals("SELECT"))
			return SELECT_PRESSED;

		if(button.equals("HOME"))
			return HOME_PRESSED;
		
		if(button.equals("UP"))
			return UP_PRESSED;
		if(button.equals("DOWN"))
			return DOWN_PRESSED;
		if(button.equals("LEFT"))
			return LEFT_PRESSED;
		if(button.equals("RIGHT"))
			return RIGHT_PRESSED;
		return false;
	}

	@Override
	public String[] getAnalogueSticks() {
		return new String[]{"LEFT", "RIGHT"};
	}

	@Override
	public int getAnalogueStickX(String stick) {
		if(stick.equals("LEFT"))
			return LEFT_STICK_HORIZONTAL;
		if(stick.equals("RIGHT"))
			return RIGHT_STICK_HORIZONTAL;
		return 0;
	}

	@Override
	public int getAnalogueStickY(String stick) {
		if(stick.equals("LEFT"))
			return LEFT_STICK_VERTICAL;
		if(stick.equals("RIGHT"))
			return RIGHT_STICK_VERTICAL;
		return 0;
	}

	@Override
	public int getAnalogueStickXPositiveBoundary(String stick) {
		return 128;
	}

	@Override
	public int getAnalogueStickYPositiveBoundary(String stick) {
		return 128;
	}
	
	@Override
	public int getAnalogueStickXNegativeBoundary(String stick) {
		return -128;
	}

	@Override
	public int getAnalogueStickYNegativeBoundary(String stick) {
		return -128;
	}

	@Override
	public String getControllerName() {
		return "WiiU Pro Controller";
	}

	@Override
	public boolean doesNameMatch(String name) {
		if(name.equalsIgnoreCase("Nintendo RVL-CNT-01-UC"))
			return true;
		else if(name.startsWith("Wiimote"))
			return true;
		return false;
	}

	@Override
	public Controller getNewInstance(HIDDevice device) {
		return new WiiUProController(new HIDInputStream(device), new HIDOutputStream(device));
	}
}
