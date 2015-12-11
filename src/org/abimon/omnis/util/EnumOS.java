package org.abimon.omnis.util;

import java.io.File;

public enum EnumOS {
	
	WINDOWS(System.getProperty("user.home") + File.separator),
	MACOSX(System.getProperty("user.home") + "/Library/Application Support/"),
	LINUX(System.getProperty("user.home") + File.separator),
	OTHER;
	
	String storageLocation = "Omnis";
	
	EnumOS(){}
	
	EnumOS(String storageLocation){
		this.storageLocation = storageLocation;
	}
	
	public static EnumOS determineOS(){
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("mac"))
			return MACOSX;
		return OTHER;
	}
	
	public File getStorageLocation(String folderName){
		return new File(storageLocation + folderName);
	}

	public boolean hasANSI() {
		return this == MACOSX || this == LINUX;
	}
}
