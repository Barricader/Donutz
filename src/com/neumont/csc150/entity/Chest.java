package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.neumont.csc150.Display;
import com.neumont.csc150.Donutz;
import com.neumont.csc150.Tile;
import com.neumont.csc150.item.Item;
import com.neumont.csc150.item.Weapon;

public class Chest extends Entity {
	public static final int MAX_INVENTORY = 6;
	private boolean hide = false;
	private BufferedImage[] sprites;
	private Vector<Item> items;

	public Chest(double x, double y) {
		super(x, y, 0);
		w = 32;
		h = 32;
		direction = 0;

		items = new Vector<Item>();
		
		for (int i = 0; i < MAX_INVENTORY; i++) {
			items.add(null);
		}
	
		sprites = new BufferedImage[4];
	}
	
	public void update() {
		
	}
	
	/**
	 * Draw the player
	 */
	public void render(Graphics g) {
//		Rectangle r = new Rectangle((int)x - w/2, (int)y - w/2, w, h);	// DEBUG
//		g.drawRect(r.x, r.y, r.width, r.height);
		
		g.drawImage(sprites[0], (int)x - w/2, (int)y - h/2, null);
	}
	
	/**
	 * Load the player's sprite sheet into memory
	 * @param sheet
	 */
	public void load(String sheet) {
		try {
			BufferedImage sh = ImageIO.read(new File(sheet));
			
			int tempHeight = sh.getHeight();
			for (int i = 0; i < tempHeight/h; i++) {
				sprites[i] = sh.getSubimage(w, i*h, w, h);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Collision checking
	 */
	public boolean collide(Entity e) {
		Rectangle r = new Rectangle((int)x - w/2, (int)y - w/2, w, h);
		Rectangle r2 = new Rectangle((int)e.getX(), (int)e.getY(), e.getWidth(), e.getHeight());
		
		if (r.intersects(r2)) {
			return true;
		}
		
		return false;
	}
	
	public Vector<Item> getItems() {
		return items;
	}
	
	public void addItem(Item it) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == null) {
				items.set(i, it);
				return;
			}
		}
		
		System.out.println("NO FREE SPACES");
	}
	
	/**
	 *  Get the player's collision box
	 * @return The collision box
	 */
	public Rectangle getRect() {
		Rectangle r = new Rectangle((int)x - w/2, (int)y - w/2, w, h);
		return r;
	}
	
	public void hide(boolean h) {
		hide = h;
	}
	
	public boolean getHide() {
		return hide;
	}
}
