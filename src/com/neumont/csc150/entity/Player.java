package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

// TODO: add variables for combat such as a WEAPON class and hp and skill points and what not
public class Player extends Entity {
	private boolean moving = false;
	private boolean hide = false;
	private double destX, destY;
	private BufferedImage[][] sprites;
	private int step;
	private Timer stepTime;

	public Player(double x, double y) {
		super(x, y, 3);
		w = 32;
		h = 32;
		direction = 0;
		destX = x;
		destY = y;

		// A timer that just animates the player
		step = 0;
		stepTime = new Timer(150, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				step++;
				if (step > 3) {
					step = 0;
				}
			}
		});
		
		stepTime.start();
	
		sprites = new BufferedImage[4][4];
	}
	
	public void update() {
		super.update();
		
		// Only move the player if they are not already at the destination
		if ((destX > x - 4 && destX < x + 4) && (destY > y - 4 && destY < y + 4)) {
			dx = 0;
			dy = 0;
		}
		
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		
		// If the player's rate of change isn't zero, then he must be moving
		if (dx != 0.0 && dy != 0.0) {
			moving = true;
		}
		else {
			moving = false;
		}
	}
	
	/**
	 * Draw the player
	 */
	public void render(Graphics g) {
		if (moving) {
			if (direction < 45 || direction > 315) {
				g.drawImage(sprites[3][step], (int)x - w/2, (int)y - h/2, null);
			}
			else if (direction >= 45 && direction < 135) {
				g.drawImage(sprites[0][step], (int)x - w/2, (int)y - h/2, null);
			}
			else if (direction >= 135 && direction < 225) {
				g.drawImage(sprites[2][step], (int)x - w/2, (int)y - h/2, null);
			}
			else if (direction >= 225 && direction <= 315) {
				g.drawImage(sprites[1][step], (int)x - w/2, (int)y - h/2, null);
			}
		}
		else {
			if (direction < 45 || direction > 315) {
				g.drawImage(sprites[3][0], (int)x - w/2, (int)y - h/2, null);
			}
			else if (direction >= 45 && direction < 135) {
				g.drawImage(sprites[0][0], (int)x - w/2, (int)y - h/2, null);
			}
			else if (direction >= 135 && direction < 225) {
				g.drawImage(sprites[2][0], (int)x - w/2, (int)y - h/2, null);
			}
			else if (direction >= 225 && direction <= 315) {
				g.drawImage(sprites[1][0], (int)x - w/2, (int)y - h/2, null);
			}
		}
	}
	
	/**
	 * Sets the current velocity of the player
	 */
	public void setVelocity(int x, int y) {
		if ((x < this.x - 4 || x > this.x + 4) && (y < this.y - 4 || y > this.y + 4)) {
			super.setVelocity(x, y);
			
			destX = x;
			destY = y;
			
			// Complicated math for finding the direction of the players
			direction = -Math.atan2(y - this.y, x - this.x) * 180 / Math.PI;
			if (direction < 0) {
				direction += 360;
			}
		}
	}
	
	/**
	 * Load the player's sprite sheet into memory
	 * @param sheet
	 */
	public void load(String sheet) {
		try {
			BufferedImage sh = ImageIO.read(new File(sheet));
			
			sprites[0][0] = sh.getSubimage(0, 0, w, h);
			sprites[0][1] = sh.getSubimage(0, h, w, h);
			sprites[0][2] = sh.getSubimage(0, h*2, w, h);
			sprites[0][3] = sh.getSubimage(0, h*3, w, h);
			sprites[1][0] = sh.getSubimage(w, 0, w, h);
			sprites[1][1] = sh.getSubimage(w, h, w, h);
			sprites[1][2] = sh.getSubimage(w, h*2, w, h);
			sprites[1][3] = sh.getSubimage(w, h*3, w, h);
			sprites[2][0] = sh.getSubimage(w*2, 0, w, h);
			sprites[2][1] = sh.getSubimage(w*2, h, w, h);
			sprites[2][2] = sh.getSubimage(w*2, h*2, w, h);
			sprites[2][3] = sh.getSubimage(w*2, h*3, w, h);
			sprites[3][0] = sh.getSubimage(w*3, 0, w, h);
			sprites[3][1] = sh.getSubimage(w*3, h, w, h);
			sprites[3][2] = sh.getSubimage(w*3, h*2, w, h);
			sprites[3][3] = sh.getSubimage(w*3, h*3, w, h);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Collision checking
	 */
	public boolean collide(Entity e) {
		Rectangle r = new Rectangle((int)x - w/2, (int)y - w/2, w, h);
		Rectangle r2 = new Rectangle((int)e.getX(), (int)e.getY(), e.getWidth(), e.getHeight());
		
		if (r.intersects(r2)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 *  Get the player's collision box
	 * @return The collision box
	 */
	public Rectangle getRect() {
		Rectangle r = new Rectangle((int)x - w/2, (int)y - w/2, w, h);
		return r;
	}
	
	public void hide(boolean h) {
		hide = h;
	}
	
	public boolean getHide() {
		return hide;
	}
	
	public void setMoving(boolean a) {
		moving = a;
	}
	
	public boolean isMoving() {
		return moving;
	}
}
