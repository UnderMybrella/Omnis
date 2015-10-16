package org.abimon.omnis.ludus;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import org.abimon.omnis.io.Data;
import org.abimon.omnis.io.ZipData;
import org.abimon.omnis.util.General;

import tiled.core.Map;
import tiled.core.TileLayer;
import tiled.io.TMXMapReader;

public class Tiled {

	public static final char[] LIBRARY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();

	public static ZipData tiledToZipData(String tiledFile) throws Exception{
		TMXMapReader reader = new TMXMapReader();
		Map map = reader.readMap(tiledFile);
		ZipData data = new ZipData();
		HashMap<String, String> uniqueIDCollection = new HashMap<String, String>();
		LinkedList<Integer> uniquePhrase = new LinkedList<Integer>();
		uniquePhrase.add(-1);
		System.out.println(map.getLayerCount());
		for(int i = 0; i < Math.min(map.getLayerCount(), LayerList.LAYER_COUNT); i++)
			if(map.getLayer(i) != null)
				if(map.getLayer(i) instanceof TileLayer)
				{
					String layerString = "";
					TileLayer layer = (TileLayer) map.getLayer(i);
					for(int x = 0; x < layer.getWidth(); x++){
						for(int y = 0; y < layer.getHeight(); y++)
						{	
							Tile tile = Ludus.getAirTile();
							if(layer.getTileAt(x, y) != null)
							{
								BufferedImage img = General.toBufferedImage(layer.getTileAt(x, y).getImage());
								tile = Ludus.getTileForImage(img);
								if(tile == null)
									tile = Ludus.getAirTile();
							}
							if(!uniqueIDCollection.containsKey(tile.uniqueTileName))
							{
								System.out.println("Found " + tile);
								uniquePhrase.set(0, uniquePhrase.get(0) + 1);
								if(uniquePhrase.get(0) >= LIBRARY.length)
								{
									uniquePhrase.set(0, 0);
									if(uniquePhrase.size() == 1)
										uniquePhrase.add(1);
									else
									{
										boolean success = false;
										for(int j = 1; j < uniquePhrase.size(); j++)
										{
											uniquePhrase.set(j, uniquePhrase.get(j) + 1);
											if(uniquePhrase.get(j) >= LIBRARY.length)
												uniquePhrase.set(j, 0);
											else{
												success = true;
												break;
											}
										}
										if(!success)
											uniquePhrase.add(0);
									}
								}
								String phrase = "";
								for(Integer j : uniquePhrase)
									phrase += LIBRARY[j];
								uniqueIDCollection.put(tile.uniqueTileName, phrase);
							}
							layerString += uniqueIDCollection.get(tile.uniqueTileName) + "|";
						}
						layerString += "\n";
					}
					layerString = layerString.trim();
					data.put("Layer" + i + ".txt", new Data(layerString));
				}
		String uniqueString = "";
		for(String key : uniqueIDCollection.keySet())
			uniqueString += key + "=" + uniqueIDCollection.get(key) + "\n";
		uniqueString = uniqueString.trim();
		data.put("Keys.txt", new Data(uniqueString));
		System.out.println(uniqueString);
		System.out.println(data);
		return data;
	}
}
