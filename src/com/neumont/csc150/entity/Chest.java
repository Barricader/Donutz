package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.neumont.csc150.Donutz;
import com.neumont.csc150.item.Item;
import com.neumont.csc150.item.Weapon;

public class Chest extends Entity {
	public static final int MAX_INVENTORY = 6;
	private boolean hide = false;
	private BufferedImage[] sprites;
	private Vector<Item> items;
	
	private boolean opened;
	private int inc;
	private int curFrame;

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
		
		opened = false;
		inc = 0;
		curFrame = 0;
		
		load("chest.png");
		
		addItem(new Weapon("Brick Breaker", 1, "BB.png", 20));
	}
	
	// TODO: check if player is near this chest and output it to the donutz chest update loop
	public void update(boolean test) {
		if (opened && curFrame < 3) {
			inc++;
			
			if (inc % 10 == 0) {
				curFrame++;
			}
		}
		
		Rectangle temp = getRect();
		temp.x -= 8;
		temp.y -= 8;
		temp.width += 16;
		temp.height += 16;
		
		if (!getRect().intersects(Donutz.getInstance().getPlayer().getRect())) {
			Donutz.getInstance().setCurChestInv(-1);
		}
		
		if (opened && true && Donutz.getInstance().getCurChestInv() == -1) {
			// TODO: set this as curChestINV
			test = true;
		}
		//else if (opened//TODO: check if this is already current, if so then hide if no player near) {
				
	}
	
	/**
	 * Draw the chest
	 */
	public void render(Graphics g) {
		g.drawImage(sprites[curFrame], (int)x - w/2, (int)y - h/2, null);
	}
	
	/**
	 * Load the chest's sprite sheet into memory
	 * @param sheet
	 */
	public void load(String sheet) {
		try {
			BufferedImage sh = ImageIO.read(new File(sheet));
			
			int tempHeight = sh.getHeight();
			for (int i = 0; i < tempHeight/h; i++) {
				sprites[i] = sh.getSubimage(0, i*h, w, h);
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
	
	public void setOpened(boolean flag) {
		opened = flag;
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
		
		System.out.println("NO FREE SPACES FOR CHEST");
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
