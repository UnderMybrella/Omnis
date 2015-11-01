package org.abimon.omnis.nhm;

import java.io.File;
import org.abimon.omnis.io.ZipData;
import org.abimon.omnis.lang.Language;
import org.abimon.omnis.ludus.AnimatedTile;
import org.abimon.omnis.ludus.EntityPlayer;
import org.abimon.omnis.ludus.Floor;
import org.abimon.omnis.ludus.LayerList;
import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.ludus.Tile;
import org.abimon.omnis.ludus.Tiled;

public class NagitosHopeMachine {
	
	public static Tile air = new Tile("NHM:air", "air", "", false);
	public static Tile grass = new Tile("NHM:grass", "grass", "resources/Grass.png", false);
	public static Tile rock = new Tile("NHM:rock", "rock", "resources/Rock.png", true);
	public static Tile flower = new AnimatedTile("NHM:flower", "flower", "resources/flower.ani", false);
	public static Tile sea = new AnimatedTile("NHM:seaAnimated", "sea", "resources/sea.ani", true);
	
	//public static Tile sea = new Tile("NHM:sea", "sea", "resources/sea.png");
	
	public static void main(String[] args){
		Ludus.registerDataPool(NagitosHopeMachine.class.getClassLoader());
		Ludus.registerDataPool(new File("resources"));
		Ludus.registerDataPool(new File("maps"));
		
		Ludus.registerPlayer(new EntityPlayer("resources/Player.png"));
		
		Ludus.reloadIcons();
		
		try {
			ZipData data = Tiled.tiledToZipData("maps/suck.tmx");
			data.writeToFile("maps/Suck.map");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Floor tmx = Floor.loadFromFile("maps/Suck.map");
		System.out.println(tmx);
		
		Floor floor = new Floor("Floor #1");
		floor.setLayer(LayerList.BACKGROUND_LAYER, new Tile[][]{{sea, sea, sea}, {sea, sea, sea}, {sea, sea, sea}});
		floor.setLayer(LayerList.FOREGROUND_LAYER, new Tile[][]{{air, air, air}, {air, rock, air}, {air, air, air}});
		floor.setLayer(LayerList.ANIMATED_FOREGROUND_LAYER, new Tile[][]{{air, flower, air}, {air, air, air}, {air, flower, air}});

		Ludus.registerKeyListener(Ludus.defaultKeyListener);
		Ludus.mainWindow.setFloor(tmx);
		Ludus.mainWindow.setVisible(true);
		
		Language swahili = new Language();
		//swahili.learn("English", " ", "", new String[]{"nitanipenda", "nitanipenda", "nitanipenda"},  new String[]{"I will like me", "I will like me", "I will like me"});
		swahili.learn("English", " ", "", new String[]{"nitanipenda", "utanipenda", "atanipenda"},  new String[]{"I will like me", "you will like me", "s/he will like me"});
		swahili.learn("English", " ", "", new String[]{"nita", "nili", "nina"},  new String[]{"I will", "I did", "I do"});
		swahili.learn("English", " ", "", new String[]{"nitanipenda", "nitakupenda", "nitampenda"},  new String[]{"I will like me", "I will like you", "I will like him/her"});
		swahili.learn("English", " ", "", new String[]{"nitanipenda", "nitanisumbua", "nitanipiga"},  new String[]{"I will like me", "I will annoy me", "I will beat me"});
		//System.out.println(swahili.translate("English", "I will like me", " ", ""));
	}
}
