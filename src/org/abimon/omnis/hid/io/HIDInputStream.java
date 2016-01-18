package org.abimon.omnis.hid.io;

import java.io.IOException;
import java.io.InputStream;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;

public class HIDInputStream extends InputStream {

	HIDDevice device;
	
	public HIDInputStream(HIDDevice device){
		this.device = device;
	}

	public HIDInputStream(HIDDeviceInfo device) throws IOException{
		this.device = device.open();
	}
	
	@Override
	public int read() throws IOException {
		byte[] data = new byte[1];
		device.read(data);
		return data[0];
	}
	
	@Override
	public int read(byte[] data) throws IOException{
		return device.read(data);
	}

}
