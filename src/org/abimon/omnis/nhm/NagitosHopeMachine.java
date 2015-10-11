package org.abimon.omnis.nhm;

import java.io.File;

import org.abimon.omnis.io.Data;
import org.abimon.omnis.ludus.Floor;
import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.ludus.Tile;

public class NagitosHopeMachine {
	
	public static Tile grass = new Tile("NHM:GrassSpiral", "grass_spiral", "resources/GrassSpiral.png");
	
	public static void main(String[] args){
		Ludus.registerDataPool(NagitosHopeMachine.class.getClassLoader());
		Ludus.registerDataPool(new File("resources"));
		Data data = Ludus.getDataUnsafe("resources/GrassSpiral.png");
		System.out.println(data);
		
		for(Tile tile : Tile.tiles)
			tile.reloadIcon();
		
		Floor floor = new Floor("Floor #1");
		Floor copy = floor.clone();
		System.out.println(floor + "\n" + copy + "\n" + floor.equals(copy));
	}
}
