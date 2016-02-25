package com.neumont.csc150.entity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Boss extends Enemy {
	BufferedImage[] sprites;

	public Boss(double x, double y, double speed, double health, EnemyType type,int dam) {
		super(x, y, speed, health, type,dam);
		super.dead = false;
		super.collides = false;
		w = 32;
		h = 32;
		
	}
	
	public int attack(){
		
		int damage = 0;
		return damage;
	}
	
	public void specialAttack(){
		
	}
	
	public void specialDefense(){
		
	}
	
	public void load(String path){
		try{
			BufferedImage buf = ImageIO.read(new File(path));
			sprites[0] = buf.getSubimage((int)x, (int)y, w, h);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
