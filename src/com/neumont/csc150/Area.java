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
		//this(0, 0, "test", "testTiles.png");
	}
	
	public Area(/*int w, int h, String name, */String path) {
//		this.w = w;
//		this.h = h;
//		this.name = name;
//		this.sheet = sheet;
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
			tiles.get(i).render(g, i);
		}
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
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
