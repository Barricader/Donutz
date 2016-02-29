package com.neumont.csc150.entity;

import java.awt.Graphics;
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
	
	public void render(Graphics g){
		g.drawImage(sprites[0],(int)x,(int)y,null);
		if(collides = true){
			g.drawImage(sprites[1], (int)x, (int)y,null);
		}		
	}
	
	public int attack(){	
		int damage = 0;
		return damage;
	}
	
	public void specialAttack(){
		//Increases the boss' maxDamage when health gets to a certain level *Still time to decide effect
		setMaxDam(getMaxDam()+20);
	}
	
	public void specialDefense(){
		//Increases the boss' speed to increase dodge rate when health gets to a certain level *Still time to decide effect
		setSpeed(getSpeed()+10);
	}
	
	public void load(String path){
		try{
			BufferedImage buf = ImageIO.read(new File(path));
			sprites[0] = buf.getSubimage((int)x, (int)y, w, h);
			sprites[1] = buf.getSubimage((int)x, (int)y, w, h);			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
