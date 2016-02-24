package com.neumont.csc150.item;

public class Weapon extends Item {
	private int dmg;
	
	public Weapon(String name, int ID, String path, int dmg) {
		super(name, ID, path);
		
		this.dmg = dmg;
	}

	public int getDmg() {
		return dmg;
	}
}
