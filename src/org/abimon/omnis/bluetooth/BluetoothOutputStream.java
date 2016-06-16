package org.abimon.omnis.bluetooth;

import java.io.IOException;
import java.io.OutputStream;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;

public class BluetoothOutputStream extends OutputStream {
	
	Connection con;
	
	public BluetoothOutputStream(RemoteDevice device, int port, String params) throws IOException{
		System.out.println(device.getBluetoothAddress());
		String l2cap =  "btl2cap://"
				+ device.getBluetoothAddress()
				+ ":" + port + (params.equals("") ? "" : ";" + params);
		System.out.println(l2cap);
		con = Connector.open(l2cap, Connector.WRITE, true);
	}
	
	public BluetoothOutputStream(BluetoothDevice device, int port, String params) throws IOException{
		this(device.getRemoteDevice(), port, params);
	}
	
	@Override
	public void write(int b) throws IOException {
		
	}

}
