package org.abimon.omnis.ludus;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.abimon.omnis.io.Data;
import org.abimon.omnis.io.ZipData;
import org.abimon.omnis.util.ExtraArrays;

public class Floor implements Cloneable{
	ConcurrentHashMap<Integer, Tile[][]> floor = new ConcurrentHashMap<Integer, Tile[][]>();

	final int FLOOR_SCALE_X = 32;
	final int FLOOR_SCALE_Y = 32;

	volatile BufferedImage floorImage;

	String floorName;

	public Floor(String floorName){
		this.floorName = floorName;
		setLayer(LayerList.BACKGROUND_LAYER, new Tile[1][1]);
	}

	public static Floor loadFromFile(String file){
		try{
			if(!Ludus.hasData(file))
				return null;
			else
			{
				Data data = Ludus.getData(file);
				System.out.println(data);
				if(data == null)
					return null;
				else{
					ZipData zip = new ZipData(data);
					Floor floor = new Floor(file.replaceAll("\\..*", ""));
					Data keys = zip.get("Keys.txt");
					if(keys == null)
						return null;
					for(String key : zip.keySet())
						if(key.startsWith("Layer"))
						{
							String layerNum = key.replace("Layer", "").replace(".txt", "");
							if(!layerNum.matches("\\d+"))
								continue;
							Data layer = zip.get(key);
							String[] layerStrings = layer.getAsStringArray();
							System.out.println(layer);
							Tile[][] tiles = new Tile[layerStrings.length][layerStrings[0].replace("|", "").length()];
							for(int x = 0; x < tiles.length; x++)
							{
								for(int y = 0; y < tiles[x].length; y++)
								{
									String name = layerStrings[x].split("\\|")[y];
									for(String s : keys.getAsStringArray())
										if(s.split("\\=", 2)[1].trim().equalsIgnoreCase(name))
											name = s.split("\\=", 2)[0];
									tiles[x][y] = Ludus.getRegisteredTile(name);
								}
							}
							floor.setLayer(Integer.parseInt(layerNum), tiles);
						}
					return floor;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Tile[][] getLayer(int layer){
		if(!floor.containsKey(layer))
			floor.put(layer, new Tile[0][0]);
		return floor.get(layer);
	}

	public void setLayer(int layer, Tile[][] tileLayer){
		floor.put(layer, tileLayer);
	}

	public BufferedImage getImage(){
		return floorImage;
	}

	public void rerender(){
		floorImage = new BufferedImage(getTileWidth() * FLOOR_SCALE_X, getTileHeight() * FLOOR_SCALE_Y, BufferedImage.TYPE_INT_ARGB);

		Graphics2D graphics = (Graphics2D) floorImage.getGraphics();

		for(int layerNo = 0; layerNo < floor.size(); layerNo++){
			Tile[][] layer = floor.get(layerNo);
			BufferedImage img = new BufferedImage(floorImage.getWidth(), floorImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) img.getGraphics();
			for(int x = 0; x < layer.length; x++)
				for(int y = 0; y < layer[x].length; y++)
					if(layer[x][y] != null)
					{
						graphics.drawImage(layer[x][y].getIcon(), x * FLOOR_SCALE_X, y * FLOOR_SCALE_Y, FLOOR_SCALE_X, FLOOR_SCALE_Y, null);
						g.drawImage(layer[x][y].getIcon(), x * FLOOR_SCALE_X, y * FLOOR_SCALE_Y, FLOOR_SCALE_X, FLOOR_SCALE_Y, null);
					}
			imageLayers.put(layerNo, img);
		}
	}

	HashMap<Integer, BufferedImage> imageLayers = new HashMap<Integer, BufferedImage>();

	public void rerender(int layerNo){
		floorImage = new BufferedImage(getTileWidth() * FLOOR_SCALE_X, getTileHeight() * FLOOR_SCALE_Y, BufferedImage.TYPE_INT_ARGB);
		BufferedImage indImg = new BufferedImage(floorImage.getWidth(), floorImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D graphics = (Graphics2D) floorImage.getGraphics();
		Graphics2D g = (Graphics2D) indImg.getGraphics();

		Tile[][] layer = getLayer(layerNo);
		for(int i = 0; i < layerNo; i++)
			graphics.drawImage(imageLayers.get(i), 0, 0, null);
		for(int x = 0; x < layer.length; x++)
			for(int y = 0; y < layer[x].length; y++)
				if(layer[x][y] != null)
				{
					graphics.drawImage(layer[x][y].getIcon(), x * FLOOR_SCALE_X, y * FLOOR_SCALE_Y, FLOOR_SCALE_X, FLOOR_SCALE_Y, null);
					g.drawImage(layer[x][y].getIcon(), x * FLOOR_SCALE_X, y * FLOOR_SCALE_Y, FLOOR_SCALE_X, FLOOR_SCALE_Y, null);
				}
		for(int i = layerNo + 1; i < floor.size(); i++)
			graphics.drawImage(imageLayers.get(i), 0, 0, null);
		
		imageLayers.put(layerNo, indImg);
	}

	public int getTileWidth() {
		int width = 0;
		for(Tile[][] layer : floor.values())
			if(layer.length > width)
				width = layer.length;
		return width;
	}

	public int getTileHeight() {
		int height = 0;
		for(Tile[][] layer : floor.values())
			if(layer.length > 0)
				if(layer[0].length > 0)
					height = layer[0].length;
		return height;
	}

	public long getReloadTime(){
		long shortestTime = Long.MAX_VALUE;
		for(Tile[][] layer : floor.values())
			for(Tile[] row : layer)
				for(Tile tile : row)
					if(tile != null)
						if(tile.getReloadTime() < shortestTime)
							shortestTime = tile.getReloadTime();
		return Math.max(shortestTime, 10000);
	}

	public long getReloadTimeUnconditional(){
		long shortestTime = Long.MAX_VALUE;
		for(Tile[][] layer : floor.values())
			for(Tile[] row : layer)
				for(Tile tile : row)
					if(tile != null)
						if(tile.getReloadTime() < shortestTime)
							shortestTime = tile.getReloadTime();
		return shortestTime;
	}

	public long getReloadTime(int layer){
		long shortestTime = 1000;
		for(Tile[] row : this.getLayer(layer))
			for(Tile tile : row)
				if(tile != null)
					if(tile.getReloadTime() < shortestTime)
						shortestTime = tile.getReloadTime();
		return shortestTime;
	}

	@Override
	public String toString(){
		return "Floor: " + floorName + "\n" + Arrays.deepToString(floor.values().toArray());
	}

	public Floor clone(){
		Floor copy = new Floor("Copy of " + floorName);
		ConcurrentHashMap<Integer, Tile[][]> floorData = new ConcurrentHashMap<Integer, Tile[][]>();
		for(Integer i : floor.keySet())
			floorData.put(i, ExtraArrays.deepCopyOf(floor.get(i), Tile.class));
		copy.floor = floorData;
		return copy;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Floor){
			Floor floor = (Floor) obj;
			if(floor.floor.size() != this.floor.size())
				return false;
			for(int i = 0; i < floor.floor.size(); i++)
			{
				if(floor.floor.get(i).length != this.floor.get(i).length)
					return false;
				for(int x = 0; x < floor.floor.get(i).length; x++)
				{
					if(floor.floor.get(i)[x].length != this.floor.get(i)[x].length)
						return false;
					for(int y = 0; y < floor.floor.get(i)[x].length; y++){
						if(floor.floor.get(i)[x][y] == null || this.floor.get(i)[x][y] == null)
							if(!(floor.floor.get(i)[x][y] == null && this.floor.get(i)[x][y] == null))
								return false;
							else;
						else if(!floor.floor.get(i)[x][y].equals(this.floor.get(i)[x][y]))
							return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
