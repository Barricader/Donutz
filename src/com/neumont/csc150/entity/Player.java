package com.neumont.csc150.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.neumont.csc150.Display;

public class Player extends Entity {
	public static final int MAX_SPEED_MULTIPLIER = 3;
	private boolean moving = false;
	private boolean hide = false;
	private double destX, destY;
	private BufferedImage[][] sprites;

	public Player(double x, double y, double speed) {
		super(x, y, speed);
		w = 32;
		h = 32;
		direction = 0;
		destX = x;
		destY = y;
	}
	
	public void update() {
		super.update();
		
		if ((destX > x - 4 && destX < x + 4) && (destY > y - 4 && destY < y + 4)) {
			dx = 0;
			dy = 0;
		}
		
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect((int)x - w/2, (int)y - h/2, w, h);
	}
	
	// Reinitialize the player
	public void restart() {
		x = Display.WIDTH / 2 - 10;
		y = Display.HEIGHT / 2 - 10;
		dx = 0.0;
		dy = 0.0;
		direction = 0.0;
	}
	
	public void setVelocity(int x, int y) {
		if ((x < this.x - 4 || x > this.x + 4) && (y < this.y - 4 || y > this.y + 4)) {
			super.setVelocity(x, y);
			
			destX = x;
			destY = y;
			
			moving = true;
			direction = -Math.atan2(y - this.y, x - this.x) * 180 / Math.PI;
			if (direction < 0) {
				direction += 360;
			}
			System.out.println(direction);
		}
	}
	
	public void load(String sheet) {
		try {
			BufferedImage sh = ImageIO.read(new File(sheet));
			
			sprites[0][0] = sh.getSubimage(0, 0, w, h);
			sprites[0][1] = sh.getSubimage(0, h, w, h);
			sprites[0][2] = sh.getSubimage(0, h*2, w, h);
			sprites[0][3] = sh.getSubimage(0, h*3, w, h);
			sprites[1][0] = sh.getSubimage(w, 0, w, h);
			sprites[1][1] = sh.getSubimage(w, h, w, h);
			sprites[1][2] = sh.getSubimage(w, h*2, w, h);
			sprites[1][3] = sh.getSubimage(w, h*3, w, h);
			sprites[2][0] = sh.getSubimage(w*2, 0, w, h);
			sprites[2][1] = sh.getSubimage(w*2, h, w, h);
			sprites[2][2] = sh.getSubimage(w*2, h*2, w, h);
			sprites[2][3] = sh.getSubimage(w*2, h*3, w, h);
			sprites[3][0] = sh.getSubimage(w*3, 0, w, h);
			sprites[3][1] = sh.getSubimage(w*3, h, w, h);
			sprites[3][2] = sh.getSubimage(w*3, h*2, w, h);
			sprites[3][3] = sh.getSubimage(w*3, h*3, w, h);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean collide(Entity e) {
		Rectangle r = new Rectangle((int)x - 10, (int)y - 10, w, h);
		Rectangle r2 = new Rectangle((int)e.getX(), (int)e.getY(), e.getWidth(), e.getHeight());
		
		if (r.intersects(r2)) {
			return true;
		}
		
		return false;
	}
	
	public Rectangle getRect() {
		Rectangle r = new Rectangle((int)x - 10, (int)y - 10, w, h);
		return r;
	}
	
	public void hide(boolean h) {
		hide = h;
	}
	
	public boolean getHide() {
		return hide;
	}
	
	public void setMoving(boolean a) {
		moving = a;
	}
	
	public boolean isMoving() {
		return moving;
	}
}
