package com.neumont.csc150.entity;

import java.awt.Color;
import java.awt.Graphics;

public class Particle extends Entity {
	public static final int LIFETIME = 75;
	private int life;

	public Particle(double x, double y, double speed) {
		super(x, y, speed);
		life = LIFETIME;
	}

	public void update() {
		super.update();
		life--;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawRect((int)x, (int)y, 1, 1);
	}
	
	public int getLife() {
		return life;
	}
}
