package org.abimon.omnis.ludus.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import org.abimon.omnis.hid.io.HIDInputStream;
import org.abimon.omnis.hid.io.HIDOutputStream;

import com.codeminders.hidapi.HIDDevice;

public class WiimoteController extends Thread implements Controller{

	InputStream in;
	OutputStream out;

	public boolean A_PRESSED;
	public boolean B_PRESSED;

	public boolean PLUS_PRESSED;
	public boolean MINUS_PRESSED;

	public boolean HOME_PRESSED;

	public boolean LEFT_PRESSED;
	public boolean RIGHT_PRESSED;
	public boolean UP_PRESSED;
	public boolean DOWN_PRESSED;
	public byte[] recentData;
	public boolean[] leds;

	public WiimoteController(){}

	public WiimoteController(InputStream in, OutputStream out){
		this.in = in;
		this.out = out;
		this.start();
	}

	public void process(byte[] data){ 
		
		System.out.println(Arrays.toString(data));
		
		A_PRESSED = bitMask(data[2], 0x8);
		B_PRESSED = bitMask(data[2], 0x4);

		PLUS_PRESSED = bitMask(data[1], 0x10);
		MINUS_PRESSED = bitMask(data[2], 0x10);

		HOME_PRESSED = bitMask(data[2], 0x80);

		UP_PRESSED = bitMask(data[1], 0x8);
		DOWN_PRESSED = bitMask(data[1], 0x4);
		LEFT_PRESSED = bitMask(data[1], 0x1);
		RIGHT_PRESSED = bitMask(data[1], 0x2);
	}

	public boolean bitMask(byte num, int check){
		return (num & check) == check;
	}

	public void run(){
		while(true){
			try{
				byte[] data = new byte[23];
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

		if(button.equals("PLUS"))
			return PLUS_PRESSED;
		if(button.equals("MINUS"))
			return MINUS_PRESSED;

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
	public void setButtonPressed(String button, boolean pressed) {
		if(button.equals("A"))
			A_PRESSED = pressed;
		if(button.equals("B"))
			B_PRESSED = pressed;

		if(button.equals("PLUS"))
			PLUS_PRESSED = pressed;
		if(button.equals("MINUS"))
			MINUS_PRESSED = pressed;

		if(button.equals("HOME"))
			HOME_PRESSED = pressed;

		if(button.equals("UP"))
			UP_PRESSED = pressed;
		if(button.equals("DOWN"))
			DOWN_PRESSED = pressed;
		if(button.equals("LEFT"))
			LEFT_PRESSED = pressed;
		if(button.equals("RIGHT"))
			RIGHT_PRESSED = pressed;
	}

	@Override
	public String[] getAnalogueSticks() {
		return new String[]{""};
	}

	@Override
	public int getAnalogueStickX(String stick) {
		return 0;
	}

	@Override
	public int getAnalogueStickY(String stick) {
		return 0;
	}

	@Override
	public int getAnalogueStickXPositiveBoundary(String stick) {
		return 0;
	}

	@Override
	public int getAnalogueStickYPositiveBoundary(String stick) {
		return 0;
	}

	@Override
	public int getAnalogueStickXNegativeBoundary(String stick) {
		return 0;
	}

	@Override
	public int getAnalogueStickYNegativeBoundary(String stick) {
		return 0;
	}

	@Override
	public String getControllerName() {
		return "Wiimote";
	}

	@Override
	public boolean doesNameMatch(String name) {
		if(name.equalsIgnoreCase("Nintendo RVL-CNT-01"))
			return true;
		else if(name.startsWith("Wiimote"))
			return true;
		return false;
	}

	@Override
	public Controller getNewInstance(HIDDevice device, OutputStream out, InputStream in) {
		return new WiimoteController(in != null ? in : new HIDInputStream(device), out != null ? out :new HIDOutputStream(device));
	}

	@Override
	public void vibrate(final long millis) throws IOException {
		out.write(new byte[]{(byte) 0xA2, 0x11, 0x01});
		new Thread(){
			public void run(){
				long waiting = millis;
				while(waiting > 0){
					try{
						waiting -= 100;
						Thread.sleep(100);
					}
					catch(Throwable th){}
				}
				try{
					out.write(new byte[]{(byte) 0xA2, 0x11, 0x0});
				}
				catch(Throwable th){
					th.printStackTrace();
				}
			}
		}.start();
		//setLEDs(this.leds);
	}

	public void setLEDs(boolean[] leds) throws IOException{
		boolean[] ledU = new boolean[4];
		for(int i = 0; i < Math.min(leds.length, 4); i++)
			ledU[i] = leds[i];

		int mask = 0;
		for(int i = 0; i < 4; i++)
			if(ledU[i])
				mask = mask ^ 1 << (4 + i);
		System.out.println(mask + ":" + Arrays.toString(ledU));
		this.leds = ledU;
		out.write(new byte[]{(byte) 0xA2, 0x11, (byte) mask});
	}

	@Override
	public OutputStream getOutputStream() {
		return out;
	}
}
