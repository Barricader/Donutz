package com.neumont.csc150.entity;

import java.awt.Graphics;

public class Boss extends Enemy {
	
	public Boss(double x, double y, double speed){
		super(x,y,speed);
	}

	public Boss(double x, double y, double speed, int health, EnemyType type, int dam) {
		super(x, y, speed, health, type, dam);
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
		int damage = r.nextInt(getMaxDam()) + 1;
		return damage;
	}

	public int specialAttack() {
		// Increases the boss' maxDamage when health gets to a certain level
		// *Still time to decide effect
		int attack = getMaxDam() + 20;
		setMaxDam(attack);
		return r.nextInt(getMaxDam()) + 1;
	}

	public void specialDefense() {
		// Increases the boss' speed to increase dodge rate when health gets to
		// a certain level *Still time to decide effect
		setSpeed(getSpeed() + 10);
	}

	
}
