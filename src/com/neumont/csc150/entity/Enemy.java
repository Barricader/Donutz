package com.neumont.csc150.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Enemy extends Entity {
	
	private double health;
	private EnemyType type;
	
	public Enemy(double x, double y, double speed) {
		super(x, y, speed);
		super.collides = false;
		super.dead = false;
	}
	
	public Enemy(double x,double y, double speed,double health, EnemyType type){
		super(x, y, speed);
		super.collides = false;
		super.dead = false;
		w = 32;
		h = 32;
		this.health = health;
		this.type = type;
		
	}

	@Override
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fillRect((int)this.x-w/2, (int)this.y-h/2, this.w, this.h);
	}
	
	
	public boolean collide(Entity e){
		Rectangle r = new Rectangle((int)this.x,(int)this.y,this.w,this.h);
		Rectangle r2 = new Rectangle((int)e.getX(),(int)e.getY(),e.getWidth(),e.getHeight());
		if(r.intersects(r2)){
			return true;
		}
		
		return false;
		
	}
	
	public void load(String i){
		
	}
	
	
	public Rectangle getRect(){
		Rectangle r = new Rectangle((int)this.x,(int)this.y,this.w,this.h);
		return r;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(double health) {
		this.health = health;
	}

	public EnemyType getType() {
		return type;
	}

	public void setType(EnemyType type) {
		this.type = type;
	}

}
