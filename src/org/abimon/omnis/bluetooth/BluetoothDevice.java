package org.abimon.omnis.bluetooth;

import java.io.IOException;
import java.util.LinkedList;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

import com.intel.bluetooth.BlueCoveConfigProperties;

public class BluetoothDevice {

	RemoteDevice device;
	
	static{
		System.setProperty(BlueCoveConfigProperties.PROPERTY_JSR_82_PSM_MINIMUM_OFF, "true");
	}

	public BluetoothDevice(RemoteDevice device){
		this.device = device;
	}

	public static BluetoothDevice[] getDevices() throws BluetoothStateException{
		final LinkedList<BluetoothDevice> devices = new LinkedList<BluetoothDevice>();

		for(RemoteDevice device : LocalDevice.getLocalDevice().getDiscoveryAgent().retrieveDevices(DiscoveryAgent.PREKNOWN))
			devices.add(new BluetoothDevice(device));

		return devices.toArray(new BluetoothDevice[0]);
	}

	public String toString(){
		try {
			return device.getFriendlyName(false);
		} catch (IOException e) {
			return device.getBluetoothAddress();
		}
	}

	public RemoteDevice getRemoteDevice() {
		return device;
	}
}
