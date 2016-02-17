package com.neumont.csc150.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import com.neumont.csc150.Display;

public class Player extends Entity {
	public static final int MAX_SPEED_MULTIPLIER = 3;
	private boolean moving = false;
	private boolean hide = false;
	private double destX, destY;

	public Player(double x, double y, double speed) {
		super(x, y, speed);
		w = 20;
		h = 20;
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
		Rectangle r = new Rectangle((int)x - 10, (int)y - 10, 20, 20);
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
