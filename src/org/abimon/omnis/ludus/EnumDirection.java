package org.abimon.omnis.ludus;

public enum EnumDirection {
	NORTH(3),
	SOUTH(0),
	EAST(2),
	WEST(1);
	
	int i;
	
	EnumDirection(int i){
		this.i = i;
	}
	
	public int toInt(){
		return i;
	}
}
