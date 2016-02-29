package com.neumont.csc150;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
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
	private Vector<Vector<Tile>> tiles;
	private ArrayList<Integer> aboves;
	private HashMap<Integer, String> teleporters;
	private String imgPath, path;
	private int colLayer;
	
	public Area() {
		path = "";
		tiles = new Vector<Vector<Tile>>();
		aboves = new ArrayList<Integer>();
		teleporters = new HashMap<Integer, String>();
		colLayer = -1;
	}
	
	public Area(String path) {
		this.path = path;
		tiles = new Vector<Vector<Tile>>();
		aboves = new ArrayList<Integer>();
		teleporters = new HashMap<Integer, String>();
		colLayer = -1;
		
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
			ArrayList<JSONObject> tileLayers = new ArrayList<JSONObject>();
			ArrayList<JSONObject> tileTilesets = new ArrayList<JSONObject>();
			
			for (int i = 0; i < layers.size(); i++) {
				tileLayers.add((JSONObject) layers.get(i));
				if ((boolean) tileLayers.get(i).get("collidable")) {
					colLayer = i;
				}
				
				if ((boolean) tileLayers.get(i).get("above")) {
					aboves.add(i);
				}
				
				if ((boolean) tileLayers.get(i).get("teleport")) {
					teleporters.put(i, (String) tileLayers.get(i).get("location"));
				}
			}
			
			for (int i = 0; i < tilesets.size(); i++) {
				tileTilesets.add((JSONObject) tilesets.get(i));
			}
			
			JSONObject tsObj = (JSONObject) tilesets.get(0);
			
			ArrayList<String> imgPaths = new ArrayList<String>();
			for (int i = 0; i < tileTilesets.size(); i++) {
				imgPaths.add((String) tileTilesets.get(i).get("image"));
			}
			imgPath = (String) tsObj.get("image");
			
			int tempW = (int) ((long)tsObj.get("tilewidth"));
			int tempH = (int) ((long)tsObj.get("tileheight"));
			
			ArrayList<Integer> imgWidths = new ArrayList<Integer>();
			
			for (int i = 0; i < tileTilesets.size(); i++) {
				imgWidths.add((int) ((long)tileTilesets.get(i).get("imagewidth")));
			}
			
			ArrayList<Integer> widths = new ArrayList<Integer>();
			ArrayList<Integer> heights = new ArrayList<Integer>();
			
			for (int i = 0; i < tileLayers.size(); i++) {
				widths.add((int) ((long)tileLayers.get(i).get("width")));
				heights.add((int) ((long)tileLayers.get(i).get("height")));
			}
			
			w = widths.get(0);
			h = heights.get(0);
			
			ArrayList<JSONArray> datas = new ArrayList<JSONArray>();
			ArrayList<Object[]> finalData = new ArrayList<Object[]>();
			
			for (int i = 0; i < tileLayers.size(); i++) {
				datas.add((JSONArray) tileLayers.get(i).get("data"));
				finalData.add(datas.get(i).toArray());
			}
			
			ArrayList<Integer> tileIDs = new ArrayList<Integer>();
			ArrayList<Integer> tileCounts = new ArrayList<Integer>();
			for (int i = 0; i < tileTilesets.size(); i++) {
				tileIDs.add((int) ((long) tileTilesets.get(i).get("firstgid")));
				tileCounts.add((int) ((long) tileTilesets.get(i).get("tilecount")));
			}
			
			// Creates a cache to store the images used and the tile pulls from the
			// cache when it loads the image, this speeds of loading time drastically
			// because it is not repeatedly using the image from the sprite sheet
			int index;
			int loadedFiles = 0, totalFiles = 0;
			for (int i = 0; i < tileLayers.size(); i++) {
				totalFiles += widths.get(i)*heights.get(i);
			}
			HashMap<Integer, BufferedImage> cache = new HashMap<Integer, BufferedImage>();
			for (int k = 0; k < tileLayers.size(); k++) {
				Vector<Tile> temp = new Vector<Tile>();
				index = 0;
				//System.out.println("Loading layer " + k + "...");
				for (int i = 0; i < heights.get(k); i++) {
					for (int j = 0; j < widths.get(k); j++) {
						int tempID = Integer.parseInt(finalData.get(k)[index].toString());
						int curTS = 0;
					
						for (int l = 0; l < tileIDs.size(); l++) {
							if (tempID >= tileIDs.get(l) && tempID < tileIDs.get(l) + tileCounts.get(l)) {
								curTS = l;
								break;
							}
						}
						
						if (!cache.containsKey(tempID)) {
							BufferedImage sp;
							try {
								BufferedImage sh = ImageIO.read(new File(imgPaths.get(curTS)));
								
								int tempX = 0;
								int tempY = 0;
								if (tempID != 0) {
									tempX = ((tempID - tileIDs.get(curTS)) * tempW) % imgWidths.get(curTS);
									tempY = (((tempID - tileIDs.get(curTS)) * tempH) / imgWidths.get(curTS)) * tempH;
								}
								
								sp = sh.getSubimage(tempX, tempY, tempW, tempH);
								cache.put(tempID, sp);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						temp.add(new Tile(cache.get(tempID), tempW * j, tempH * i, tempW, tempH, tempID));
						index++;
						loadedFiles++;
						
						// Update the load percent
						Donutz.getInstance().setLoadPerc((double)(loadedFiles / (double)totalFiles));
					}
				}
				tiles.add(temp);
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
	public void render(Graphics g, boolean renAbove) {
		for (int j = 0; j < tiles.size(); j++) {
			if (j != colLayer && teleporters.get(j) == null) {
				if (renAbove && aboves.contains(j) || (!renAbove && !aboves.contains(j))) {
					for (int i = 0; i < tiles.get(j).size(); i++) {
						tiles.get(j).get(i).render(g);
					}
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
	
	public Vector<Vector<Tile>> getTiles() {
		return tiles;
	}
	
	public String getLocation(int i) {
		return teleporters.get(i);
	}
	
	public HashMap<Integer, String> getTPMap() {
		return teleporters;
	}
	
	public Vector<Vector<Tile>> getTeleportLayers() {
		Vector<Vector<Tile>> temp = new Vector<Vector<Tile>>();
		Set<Integer> keys = teleporters.keySet();
		Iterator<Integer> iter = keys.iterator();
		while (iter.hasNext()) {
			temp.add(tiles.get(iter.next()));
		}
		
		return temp;
	}
	
	public Vector<Tile> getColLayer() {
		if (colLayer != -1) {
			return tiles.get(colLayer);
		}
		else {
			return null;
		}
	}
	
	public String getPath() {
		return path;
	}

	public String getImgPath() {
		return imgPath;
	}
}
