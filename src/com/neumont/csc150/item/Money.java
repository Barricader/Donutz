package com.neumont.csc150.item;

public class Money extends Item {
	private int value;
	
	public Money(String name, int ID, String path, int value) {
		super(name, ID, path);
		
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
