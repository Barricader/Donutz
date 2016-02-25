package com.neumont.csc150;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 * Tile in the area
 * @author JoJones
 *
 */
public class Tile {
	private int x, y, w, h;
	private int ID;
	
	BufferedImage sprite;
	
	Tile() {
		this(null, 0, 0, 0, 0, -1);
	}
	
	public Tile(BufferedImage img, int x, int y, int w, int h, int ID) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.ID = ID;
		
		sprite = img;
	}

	/**
	 * Draw the tile
	 * @param g Graphics to draw on
	 */
	public void render(Graphics g) {
		if (ID != 0) {
			g.drawImage(sprite, x, y, null);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
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
		this.h= h;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
