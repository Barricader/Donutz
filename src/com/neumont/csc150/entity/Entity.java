package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.neumont.csc150.util.Vector;

// TODO: move the polygon, points, and toVel stuff to here
/**
 * An object that can interact with the game
 * @author JoJones
 */
public abstract class Entity {
	protected double x, y, dx, dy;
	protected int w, h, bh, bw;
	protected boolean collides;
	protected double speed;
	protected double direction;
	protected boolean dead;
	
	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public Entity(double x, double y, double speed) {
		this.x = x;
		this.y = y;
		collides = false;
		dead = false;
		this.speed = speed;
	}
	
	/**
	 * Update method for the entity
	 */
	public void update() {
		x += dx;
		y += dy;
	}

	/**
	 * How you want to render the Entity on the display
	 * @param g - Graphics to draw on
	 */
	public abstract void render(Graphics g);
	
	/**
	 * Set the velocity for the Entity
	 * @param x - x coord to point at
	 * @param y - y coord to point at
	 */
	public void setVelocity(int x, int y) {
		double testX = x - this.x;
		double testY = y - this.y;
		
		Vector vec = new Vector(testX, testY);
		vec = vec.normalize();
		
		dx = vec.getdX() * speed;
		dy = vec.getdY() * speed;
	}
	
	/**
	 * Check collision with another entity
	 * @param e - Entity to check for
	 * @return Whether or not this object collided with e
	 */
	public boolean collide(Entity e) {
		Rectangle r = new Rectangle((int)x, (int)y, w, h);
		Rectangle r2 = new Rectangle((int)e.getX(), (int)e.getY(), e.getWidth(), e.getHeight());
		
		if (r.intersects(r2)) {
			return true;
		}
		
		return false;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
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
		this.h = h;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public boolean isColliding() {
		return collides;
	}

	public void setCollides(boolean collides) {
		this.collides = collides;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public boolean isDead() {
		return dead;
	}
}
