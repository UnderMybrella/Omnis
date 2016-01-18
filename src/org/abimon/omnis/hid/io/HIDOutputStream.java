package org.abimon.omnis.hid.io;

import java.io.IOException;
import java.io.OutputStream;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;

public class HIDOutputStream extends OutputStream {

	HIDDevice device;
	
	public HIDOutputStream(HIDDevice device){
		this.device = device;
	}
	
	public HIDOutputStream(HIDDeviceInfo hidDeviceInfo) throws IOException{
		this.device = hidDeviceInfo.open();
	}

	@Override
	public void write(int b) throws IOException {
		device.write(new byte[]{(byte) b});
	}
	
	@Override
	public void write(byte[] data) throws IOException {
		device.write(data);
	}

}
