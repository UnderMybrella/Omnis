package org.abimon.omnis.nhm;

import java.awt.Font;
import java.io.File;
import java.util.Date;
import java.util.Scanner;

import org.abimon.omnis.io.ZipData;
import org.abimon.omnis.lang.Language;
import org.abimon.omnis.ludus.AnimatedTile;
import org.abimon.omnis.ludus.EntityBlankPlayer;
import org.abimon.omnis.ludus.EntityPlayer;
import org.abimon.omnis.ludus.Floor;
import org.abimon.omnis.ludus.LayerList;
import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.ludus.Tile;
import org.abimon.omnis.ludus.Tiled;
import org.abimon.omnis.ludus.gui.GuiEventChoice;
import org.abimon.omnis.ludus.gui.GuiTextOverlay;
import org.abimon.omnis.net.Pastebin;
import org.abimon.omnis.net.Webserver;
import org.abimon.omnis.reflect.Function;
import org.abimon.omnis.util.General;

@SuppressWarnings("unused")
public class NagitosHopeMachine {

	public static Tile air = new Tile("NHM:air", "air", "", false);
	public static Tile grass = new Tile("NHM:grass", "grass", "resources/Grass.png", false);
	public static Tile rock = new Tile("NHM:rock", "rock", "resources/Rock.png", true);
	public static Tile flower = new AnimatedTile("NHM:flower", "flower", "resources/flower.ani", false);
	public static Tile sea = new AnimatedTile("NHM:seaAnimated", "sea", "resources/sea.ani", true);
	public static Font font;

	//public static Tile sea = new Tile("NHM:sea", "sea", "resources/sea.png");

	public static void main(String[] args){
		
		System.out.println(new Pastebin("6121726c7b038aee0d4d48f3ff0fcbe5", "undermybrella", "nintendo").newPaste("Hello World!", "This is a beautiful test ;)"));
		
		Ludus.registerDataPool(NagitosHopeMachine.class.getClassLoader());
		Ludus.registerDataPool(new File("resources"));
		Ludus.registerDataPool(new File("maps"));

		//Ludus.registerPlayer(new EntityPlayer("resources/Player.png"));
		Ludus.registerPlayer(new EntityBlankPlayer());

		Ludus.reloadIcons();

		if(test("Floor"))
			floorTest();

		if(test("Swahili"))
			swahiliTest();

		if(test("Font"))
			fontTest();

		if(test("Date Format"))
			System.out.println(General.formatDate(new Date(), "dd/mm/yyyy hh:min:ss"));
		
		if(test("Webserver")){
			new Webserver(80, true).addMessageFunction(Webserver.DEFAULT_RETURN);
		}
	}

	private static boolean test(String string) {
		System.out.print("Test " + string + ": ");
		return new Scanner(System.in).nextLine().toLowerCase().startsWith("y");
	}

	public static void fontTest(){
		try{
			font = Font.createFont(Font.TRUETYPE_FONT, Ludus.getData("resources/DefaultFont.ttf").getAsInputStream()).deriveFont(24f);
			Ludus.showGui(new GuiEventChoice("I am a potato\nBut I can also be many potatoes\nAnd many potatoes can also be me; We will be an army\nAre you a potato", General.createHashmap(new String[]{"Are you a potato"}, new String[][]{{"Yes", "No"}}), General.createHashmap(new String[]{"Are you a potato"}, new Function[]{Function.getUnsafe(NagitosHopeMachine.class, "potatoResponse", String.class)}), "resources/TextBox.png", font));
		}
		catch(Throwable th){
			th.printStackTrace();
		}
	}

	public static void floorTest(){
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
	}

	public static void swahiliTest(){
		try{
			Language swahili = new Language();
			//swahili.learn("English", " ", "", new String[]{"nitanipenda", "nitanipenda", "nitanipenda"},  new String[]{"I will like me", "I will like me", "I will like me"});
			swahili.learn("English", " ", "", new String[]{"nitanipenda", "utanipenda", "atanipenda"},  new String[]{"I will like me", "you will like me", "s/he will like me"});
			swahili.learn("English", " ", "", new String[]{"nita", "nili", "nina"},  new String[]{"I will", "I did", "I do"});
			swahili.learn("English", " ", "", new String[]{"nitanipenda", "nitakupenda", "nitampenda"},  new String[]{"I will like me", "I will like you", "I will like him/her"});
			swahili.learn("English", " ", "", new String[]{"nitanipenda", "nitanisumbua", "nitanipiga"},  new String[]{"I will like me", "I will annoy me", "I will beat me"});
			System.out.println(swahili.translate("English", "I will like me", " ", ""));
		}
		catch(Throwable th){}
	}

	public static void potatoResponse(String chosenResponse){
		Ludus.dismissGui();
		Ludus.showGui(new GuiTextOverlay("You chose: " + chosenResponse, font));
	}
}
