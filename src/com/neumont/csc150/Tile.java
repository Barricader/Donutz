package com.neumont.csc150;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {
	private int x, y, w, h;
	private int ID;
	
	BufferedImage sprite;
	
	Tile() {
		this("", 0, 0, 0, 0, -1, 0);
	}
	
	Tile(String sheet, int x, int y, int w, int h, int ID, int temp) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.ID = ID;
		
		try {
			BufferedImage sh = ImageIO.read(new File(sheet));
			
			int tempX = 96;
			int tempY = 3;
			if (ID != 0) {
				tempX = ((ID - 1) * w) % temp;
				tempY = (((ID - 1) * h) / temp) * h;
			}
			
			sprite = sh.getSubimage(tempX, tempY, w, h);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g, int i) {
		if (ID != 0) {
			g.drawImage(sprite, x, y, null);
			//g.drawImage(sprite, (int)isoX, (int)isoY, null);
//			g.setColor(new Color(ID * 50 + 50, 20, 20));
//			g.drawRect(x, y, w, h);
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}