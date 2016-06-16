package org.abimon.omnis.io;

public class VirtualFile {
	String name = "";
	public VirtualDirectory parent = null;
	
	public VirtualFile(String name){
		this.name = name;
	}
	
	public String toString(){
		return (parent != null ? parent.toString() + "/" : "") + name;
	}

	public String getName() {
		return name;
	}
}
