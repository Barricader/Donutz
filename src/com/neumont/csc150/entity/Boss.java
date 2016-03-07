package com.neumont.csc150.entity;

import java.awt.Graphics;

public class Boss extends Enemy {
	
	public Boss(double x, double y, double speed){
		super(x,y,speed);
	}

	public Boss(double x, double y, double speed, int health, EnemyType type, int maxDam,int minDam) {
		super(x, y, speed, health, type, maxDam,minDam);
		super.dead = false;
		super.collides = false;
		w = 32;
		h = 32;
	}

	public void render(Graphics g) {
		g.drawImage(sprites[0][4], (int) x, (int) y, null);
		if (collides = true) {
			g.drawImage(sprites[1][4], (int) x, (int) y, null);
		}
	}

	public int attack() {
		int damage = rand.nextInt(getMaxDam()) + getMinDam();
		return damage;
	}

	public int specialAttack() {
		// Increases the boss' maxDamage when health gets to a certain level
		// *Still time to decide effect
		int maxAttack = getMaxDam() + 20;
		int minAttack = getMinDam() + 10;
		setMaxDam(maxAttack);
		setMinDam(minAttack);
		return rand.nextInt(getMaxDam()) + getMinDam();
	}

	public void specialDefense() {
		// Increases the boss' speed to increase dodge rate when health gets to
		// a certain level *Still time to decide effect
		setSpeed(getSpeed() + 10);
	}

	
}
