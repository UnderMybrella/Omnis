package org.abimon.omnis.io;

import java.io.File;

public class VirtualFile {
	String name = "";
	VirtualDirectory parent = null;
	
	public VirtualFile(String name){
		this.name = name;
	}
	
	public String toString(){
		return (parent != null ? parent.toString() + File.separator : "") + name;
	}

	public String getName() {
		return name;
	}
}
