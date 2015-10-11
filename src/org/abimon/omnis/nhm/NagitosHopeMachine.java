package org.abimon.omnis.nhm;

import java.io.File;

import org.abimon.omnis.ludus.Floor;
import org.abimon.omnis.ludus.LayerList;
import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.ludus.Tile;

public class NagitosHopeMachine {
	
	public static Tile air = new Tile("NHM:air", "air", "");
	public static Tile grass = new Tile("NHM:grass", "grass", "resources/Grass.png");
	public static Tile rock = new Tile("NHM:rock", "rock", "resources/Rock.png");
	
	public static void main(String[] args){
		Ludus.registerDataPool(NagitosHopeMachine.class.getClassLoader());
		Ludus.registerDataPool(new File("resources"));
		
		Ludus.reloadIcons();
		
		Floor floor = new Floor("Floor #1");
		floor.setLayer(LayerList.BACKGROUND_LAYER, new Tile[][]{{grass, grass, grass}, {grass, grass, grass}, {grass, grass, grass}});
		floor.setLayer(LayerList.FOREGROUND_LAYER, new Tile[][]{{air, air, air}, {air, rock, air}, {air, air, air}});

		Ludus.mainWindow.setFloor(floor);
	}
}
