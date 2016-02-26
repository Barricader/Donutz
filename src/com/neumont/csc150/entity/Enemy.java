package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Enemy extends Entity {
	
	private double health;
	private int maxDam;
	private EnemyType type;
	private BufferedImage[] sprites;
	private Random r;
	

	public Enemy(double x, double y, double speed) {
		super(x, y, speed);
		super.collides = false;
		super.dead = false;
	}
	
	public Enemy(double x,double y, double speed,double health, EnemyType type,int dam){
		super(x, y, speed);
		super.collides = false;
		super.dead = false;
		w = 32;
		h = 32;
		this.health = health;
		this.type = type;
		setMaxDam(dam);
	}

	@Override
	public void render(Graphics g) {
			if(this.type == EnemyType.HEAVY){
			g.drawImage(sprites[0], (int)x, (int)y, null);
			if(collides = true){
				g.drawImage(sprites[1], (int)x, (int)y, null);
			}
		}
		
		if(this.type == EnemyType.FAST){
			g.drawImage(sprites[2], (int)x, (int)y, null);
			if(collides = true){
				g.drawImage(sprites[3], (int)x, (int)y, null);
			}
		}
		
		if(this.type == EnemyType.RANGED){
			g.drawImage(sprites[4], (int)x, (int)y, null);
			if(collides = true){
				g.drawImage(sprites[5], (int)x, (int)y, null);
			}
		}
		
		if(this.type == EnemyType.MELEE){
			g.drawImage(sprites[6], (int)x, (int)y, null);
			if(collides = true){
				g.drawImage(sprites[7], (int)x, (int)y, null);
			}
		}
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
		try {
			BufferedImage buff = ImageIO.read(new File(i));
			
		if(this.type == EnemyType.HEAVY){
			sprites[0] = buff.getSubimage(0, 0, this.w, this.h);
			sprites[1] = buff.getSubimage(0, 0, this.w, this.h);
			
		}
		
		if(this.type == EnemyType.FAST){
			sprites[2] = buff.getSubimage(0, 0, this.w, this.h);
			sprites[3] = buff.getSubimage(0, 0, this.w, this.h);
		}
		
		if(this.type == EnemyType.RANGED){
			sprites[4] = buff.getSubimage(0, 0, this.w, this.h);
			sprites[5] = buff.getSubimage(0, 0, this.w, this.h);
			
		}
		
		if(this.type == EnemyType.MELEE){
			sprites[6] = buff.getSubimage(0, 0, this.w, this.h);
			sprites[7] = buff.getSubimage(0, 0, this.w, this.h);
			
		}
		}catch(IOException e){
			e.printStackTrace();
		}
	
	}
	
	public int attack(){	
		r = new Random();
		int damage = r.nextInt(getMaxDam())+1;
		return damage;
	}
	
	
	
	public void dodge(){
		
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
	
	
	public int getMaxDam() {
		return maxDam;
	}

	public void setMaxDam(int maxDam) {
		this.maxDam = maxDam;
	}

}
