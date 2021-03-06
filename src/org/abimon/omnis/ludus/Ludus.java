package org.abimon.omnis.ludus;

import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.abimon.omnis.io.ClassLoaderDataPool;
import org.abimon.omnis.io.Data;
import org.abimon.omnis.io.DataPool;
import org.abimon.omnis.io.FolderDataPool;
import org.abimon.omnis.ludus.gui.Gui;
import org.abimon.omnis.ludus.gui.GuiListener;
import org.abimon.omnis.ludus.gui.HUD;
import org.abimon.omnis.util.General;

public class Ludus 
{
	private static LinkedList<DataPool> dataPools = new LinkedList<DataPool>();

	public static volatile EntityPlayer thePlayer;
	public static volatile Gui guiInUse;
	public static volatile LinkedList<HUD> huds = new LinkedList<HUD>();

	public static GameWindow mainWindow = null;

	public static final DefaultKeyListener defaultKeyListener = new DefaultKeyListener();
	public static LinkedList<GuiListener> guiListeners = new LinkedList<GuiListener>();

	/**
	 * Tile Registry
	 * Key is a unique string
	 * Value is a Tile
	 */
	private static HashMap<String, Tile> tileRegistry = new HashMap<String, Tile>();

	public static void registerPlayer(EntityPlayer player) {
		thePlayer = player;
	}

	public static void dismissGui() {
		guiInUse.dismiss();

		Gui dismissing = guiInUse;
		mainWindow.removeKeyListener(guiInUse);
		guiInUse = null;

		for(GuiListener listener : guiListeners)
			listener.onGuiClosed(dismissing);
	}

	public static void showGui(Gui gui) {
		if(guiInUse != null){
			dismissGui();

			for(GuiListener listener : guiListeners)
				listener.onGuiReplaced(guiInUse, gui);
		}
		if(thePlayer != null){
			thePlayer.step = -1;
			thePlayer.moving = false;
		}
		guiInUse = gui;
		mainWindow.addKeyListener(guiInUse);

		for(GuiListener listener : guiListeners)
			listener.onGuiOpened(guiInUse);
	}

	public static void addGuiListener(GuiListener listener) {
		guiListeners.add(listener);
	}

	/**
	 * Register a tile. Allows for overriding of existing tiles.
	 * @param name The name to register the tile under
	 * @param tile The tile to register
	 */
	public static void registerTile(String name, Tile tile){
		tileRegistry.put(name, tile);
	}

	public static Tile getAirTile(){
		for(Tile tile : tileRegistry.values())
			if(tile != null)
				if(tile.unlocalised.toLowerCase().contains("air") || tile.uniqueTileName.toLowerCase().contains("air"))
					return tile;
		return null;
	}

	/**
	 * Get a registered tile
	 * @param name The name that the tile we are retrieving is under
	 * @return The tile registered for the name provided. May return null if there is no tile registered for the name.
	 */
	public static Tile getRegisteredTile(String name){
		if(tileRegistry.containsKey(name))
			return tileRegistry.get(name).clone();
		return null;
	}

	public static Tile getTileForImage(BufferedImage img){
		for(Tile tile : tileRegistry.values())
			if(General.equal(tile.getTileIcon(), img))
				return tile;
		return null;
	}

	/**
	 * Adds a class loader to the available data pools. 
	 * Make sure not to add the same class loader multiple times, as that may result in weird bugs
	 * @param loader The loader that will get added to the data pool collection
	 */
	public static void registerDataPool(ClassLoader loader){
		DataPool pool = new ClassLoaderDataPool(loader);
		dataPools.add(pool);
	}

	public static void registerDataPool(DataPool pool){
		dataPools.add(pool);
	}

	public static void reloadIcons(){
		for(String tileKey : tileRegistry.keySet())
			tileRegistry.get(tileKey).reloadIcon();


	}

	/**
	 * Adds a folder to the available data pools. 
	 * Make sure not to add the same folder multiple times, as that may result in weird bugs
	 * @param folder The folder that will get added to the data pool collection
	 */
	public static void registerDataPool(File folder){
		DataPool pool = new FolderDataPool(folder);
		dataPools.add(pool);
	}
	
	public static boolean removeDataPool(DataPool pool){
		return dataPools.remove(pool);
	}

	public static void clearDataPools(){
		dataPools.clear();
	}
	
	public static boolean hasData(String name){
		for(DataPool pool : dataPools)
			if(pool.hasData(name))
				return true;
		return false;
	}

	public static Data getData(String name) throws IOException{
		for(DataPool pool : dataPools)
			if(pool.hasData(name))
				return pool.getData(name);
		return null;
	}

	public static Data getDataUnsafe(String name){
		try{
			for(DataPool pool : dataPools)
				if(pool.hasData(name))
					return pool.getData(name);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return null;
	}
	
	public static String[] getAllDataNames(String regex) throws IOException{
		HashMap<String, Data> data = new HashMap<String, Data>(); //Use a hashmap to prevent double-ups of data
		for(DataPool pool : dataPools)
			for(String name : pool.getAllDataNames())
				if(name.matches(regex))
					if(pool.hasData(name))
						data.put(name, pool.getData(name));
		return data.keySet().toArray(new String[0]);
	}

	public static Data[] getAllData(String regex) throws IOException{
		HashMap<String, Data> data = new HashMap<String, Data>(); //Use a hashmap to prevent double-ups of data
		for(DataPool pool : dataPools)
			for(String name : pool.getAllDataNames())
				if(name.matches(regex))
					if(pool.hasData(name))
						data.put(name, pool.getData(name));
		return data.values().toArray(new Data[0]);
	}

	public static void registerKeyListener(KeyListener listener){
		mainWindow.addKeyListener(listener);
	}
}
