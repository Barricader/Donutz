package com.neumont.csc150;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Area {
	private int w, h;
	private Vector<Tile> tiles;
	private String imgPath, path;
	
	public Area() {
		path = "";
		tiles = new Vector<Tile>();
	}
	
	public Area(String path) {
		this.path = path;
		tiles = new Vector<Tile>();
		
		load();
	}
	
	public void load() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			System.out.println("Loading: " + path + " successful");
			
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
			
			int k = 0;
			for (int i = 0; i < (long)layObj.get("height"); i++) {
				for (int j = 0; j < (long)layObj.get("width"); j++) { 
					int tempID = Integer.parseInt(d[k].toString());
					tiles.add(new Tile(imgPath, tempW * j, tempH * i, tempW, tempH, tempID, imgW));
					k++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < tiles.size(); i++) {
			tiles.get(i).render(g);
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
