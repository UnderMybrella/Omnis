package org.abimon.omnis.util;

public enum EnumOS {
	WINDOWS,
	MACOSX,
	LINUX,
	OTHER;
	
	public static EnumOS determineOS(){
		String os = System.getProperty("os.name").toLowerCase();
		if(os.contains("mac"))
			return MACOSX;
		return OTHER;
	}
}
