package com.neumont.csc150;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Area in the game, such as the town, forest or etc
 * @author JoJones
 *
 */
public class Area {
	private int w, h;
	private Vector<Tile> tiles;
	private String imgPath, path;
	private Donutz don;
	
	public Area() {
		path = "";
		tiles = new Vector<Tile>();
	}
	
	public Area(String path, Donutz d) {
		this.path = path;
		tiles = new Vector<Tile>();
		don = d;
		
		load();
	}
	
	/**
	 * Load the tiles and the sprite sheet for the tiles
	 */
	public void load() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			JSONParser jp = new JSONParser();
			JSONObject obj = new JSONObject();
			obj = (JSONObject) jp.parse(br);
			
			JSONArray layers = (JSONArray) obj.get("layers");
			JSONArray tilesets = (JSONArray) obj.get("tilesets");
			JSONObject layObj = (JSONObject) layers.get(0);
			JSONObject tsObj = (JSONObject) tilesets.get(0);
			imgPath = (String) tsObj.get("image");
			
			int tempW = (int) ((long)tsObj.get("tilewidth"));
			int tempH = (int) ((long)tsObj.get("tileheight"));
			int imgW = (int) ((long)tsObj.get("imagewidth")); 
			
			w = (int) ((long)layObj.get("width"));
			h = (int) ((long)layObj.get("height"));
			
			JSONArray data = (JSONArray) layObj.get("data");
			Object[] d = data.toArray();
			
			// Creates a cache to store the images used and the tile pulls from the
			// cache when it loads the image, this speeds of loading time drastically
			// because it is not repeatedly using the image from the sprite sheet
			int k = 0;
			HashMap<Integer, BufferedImage> cache = new HashMap<Integer, BufferedImage>();
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) { 
					int tempID = Integer.parseInt(d[k].toString());
					
					if (!cache.containsKey(tempID)) {
						BufferedImage sp;
						try {
							BufferedImage sh = ImageIO.read(new File(imgPath));
							
							int tempX = 0;
							int tempY = 0;
							if (tempID != 0) {
								tempX = ((tempID - 1) * tempW) % imgW;
								tempY = (((tempID - 1) * tempH) / imgW) * tempH;
							}
							
							sp = sh.getSubimage(tempX, tempY, tempW, tempH);
							cache.put(tempID, sp);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					tiles.add(new Tile(cache.get(tempID), tempW * j, tempH * i, tempW, tempH, tempID));
					k++;
					
					// Update the load percent
					don.setLoadPerc(k / (double)(w*h));
				}
			}

			System.out.println("Loading: " + path + " successful");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Renders the map, only renders a tile if it is on screen
	 * @param g Graphics object to draw on
	 */
	public void render(Graphics g) {
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getX() > don.getCamX()/2 - tiles.get(i).getWidth() && tiles.get(i).getX() < don.getCamX()/2 + Display.WIDTH/2) {
				if (tiles.get(i).getY() > don.getCamY()/2 - tiles.get(i).getHeight() && tiles.get(i).getY() < don.getCamY()/2 + Display.HEIGHT/2) {
					tiles.get(i).render(g);
				}
			}
		}
	}

	public int getWidth() {
		return w;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getHeight() {
		return h;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public Vector<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(Vector<Tile> tiles) {
		this.tiles = tiles;
	}

	public String getImgPath() {
		return imgPath;
	}
}
