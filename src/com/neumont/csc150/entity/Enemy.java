package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Enemy extends Entity {

	private int maxHealth;
	private int curHealth;
	private int maxDam;
	private EnemyType type;
	protected BufferedImage[][] sprites;
	protected Random r;

	public Enemy(double x, double y, double speed) {
		super(x, y, speed);
		super.collides = false;
		super.dead = false;
	}

	public Enemy(double x, double y, double speed, int health, EnemyType type, int dam) {
		super(x, y, speed);
		super.collides = false;
		super.dead = false;
		w = 32;
		h = 32;
		this.maxHealth = health;
		this.curHealth = health;
		this.type = type;
		this.maxDam = dam;
	}

	@Override
	public void render(Graphics g) {
		if (this.type == EnemyType.RANGED) {
			g.drawImage(sprites[0][0], (int) x, (int) y, null);
			if (collides = true) {
				g.drawImage(sprites[1][0], (int) x, (int) y, null);
			}
		}

		if (this.type == EnemyType.MELEE) {
			g.drawImage(sprites[0][1], (int) x, (int) y, null);
			if (collides = true) {
				g.drawImage(sprites[1][1], (int) x, (int) y, null);
			}
		}

		if (this.type == EnemyType.FAST) {
			g.drawImage(sprites[0][2], (int) x, (int) y, null);
			if (collides = true) {
				g.drawImage(sprites[1][2], (int) x, (int) y, null);
			}
		}
		
		if (this.type == EnemyType.HEAVY) {
			g.drawImage(sprites[0][3], (int) x, (int) y, null);
			if (collides = true) {
				g.drawImage(sprites[1][3], (int) x, (int) y, null);
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

	public void load(String i) {
		try {
			BufferedImage buff = ImageIO.read(new File(i));

				//Ranged
				sprites[0][0] = buff.getSubimage(0, 0, this.w, this.h);
				sprites[1][0] = buff.getSubimage(0, 0, this.w, this.h);
			
				//Melee
				sprites[0][1] = buff.getSubimage(0, 0, this.w, this.h);
				sprites[1][1] = buff.getSubimage(0, 0, this.w, this.h);
			
				//Fast
				sprites[0][2] = buff.getSubimage(0, 0, this.w, this.h);
				sprites[1][2] = buff.getSubimage(0, 0, this.w, this.h);
				
				//Heavy
				sprites[0][3] = buff.getSubimage(0, 0, this.w, this.h);
				sprites[1][3] = buff.getSubimage(0, 0, this.w, this.h);
				
				//Boss
				sprites[0][4] = buff.getSubimage(0, 0, this.w, this.h);
				sprites[1][4] = buff.getSubimage(0, 0, this.w, this.h);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//	Attack method--------------------------------------------
	public int attack() {
		int damage = r.nextInt(getMaxDam()) + 1;
		return damage;
	}
//	Special Attack method------------------------------------
	public int specialAttack(){
		int attack = getMaxDam() + 5;
		if(this.type == EnemyType.HEAVY){
		attack = getMaxDam() + 10;
		return r.nextInt(attack) + 1;
		}
		else
			return r.nextInt(attack) + 1;
	}
//	Dodge Method---------------------------------------------
	public double dodge() {
		double dodge = getSpeed()/100;
		return dodge;
	}

	public Rectangle getRect() {
		Rectangle r = new Rectangle((int) this.x, (int) this.y, this.w, this.h);
		return r;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int mHealth) {
		this.maxHealth = mHealth;
	}
	
	public int getCurHealth() {
		return curHealth;
	}

	public void setCurHealth(int cHealth) {
		this.curHealth = cHealth;
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
