package com.neumont.csc150.item;

public class Potion extends Item {
	private int level;
	
	public Potion(String name, int ID, String path, int level) {
		super(name, ID, path);
		
		this.level = level;
	}

	public int getLevel() {
		return level;
	}
}
