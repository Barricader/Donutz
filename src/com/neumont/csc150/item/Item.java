package com.neumont.csc150.item;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Item {
	protected String name;
	protected BufferedImage sprite;
	protected int ID;
	
	public Item(String name, int ID, String path) {
		this.name = name;
		this.ID = ID;
		
		load(path);
	}
	
	protected void load(String path) {
		if (path != "null") {
			try {
				sprite = ImageIO.read(new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	public BufferedImage getSprite() {
		return sprite;
	}
}
