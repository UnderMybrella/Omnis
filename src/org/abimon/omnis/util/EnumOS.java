package org.abimon.omnis.util;

import java.io.File;

public enum EnumOS {
	
	WINDOWS(System.getProperty("user.home") + File.separator),
	MACOSX(System.getProperty("user.home") + "/Library/Application Support/"),
	LINUX(System.getProperty("user.home") + File.separator),
	OTHER(System.getProperty("user.home") + File.separator);
	
	String storageLocation = "Omnis";
	
	EnumOS(){}
	
	EnumOS(String storageLocation){
		this.storageLocation = storageLocation;
	}
	
	public static EnumOS determineOS(){
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("mac"))
			return MACOSX;
		if(os.contains("windows"))
			return WINDOWS;
		return OTHER;
	}
	
	public static File getHomeLocation(){
		return new File(System.getProperty("user.home"));
	}
	
	public File getStorageLocation(String folderName){
		return new File(storageLocation, folderName);
	}


	public boolean hasANSI() {
		return this == MACOSX || this == LINUX;
	}
}
