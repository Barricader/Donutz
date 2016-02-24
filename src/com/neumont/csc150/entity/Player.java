package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.neumont.csc150.Display;
import com.neumont.csc150.Donutz;
import com.neumont.csc150.item.Item;

// TODO: add variables for combat such as a WEAPON class and hp and skill points and what not
public class Player extends Entity {
	public static final int MAX_SPEED = 3;
	private boolean moving = false;
	private boolean hide = false;
	private boolean sprinting;
	private double destX, destY;
	private BufferedImage[][] sprites;
	private int step;
	private Timer stepTime;
	private Vector<Item> items;

	public Player(double x, double y) {
		super(x, y, MAX_SPEED);
		w = 32;
		h = 32;
		direction = 0;
		destX = x;
		destY = y;
		sprinting = false;

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
		
		items = new Vector<Item>();
	
		sprites = new BufferedImage[8][4];
	}
	
	public void update() {
		if (x + dx < 16 || x + dx > Donutz.getInstance().getMaxOffsetX() - Display.WIDTH/2 - 16) {
			dx = 0;
		}
		if (y + dy < 16 || y + dy > Donutz.getInstance().getMaxOffsetY() - Display.HEIGHT*1.5 - 134) {
			dy = 0;
		}
		
		// Only move the player if they are not already at the destination
		if ((x > destX - 4 && x < destX + 4) && (y > destY- 4 && y < destY + 4)) {
			dx = 0;
			dy = 0;
		}
		
		if (sprinting) {
			speed = MAX_SPEED + 2;
		}
		else {
			speed = MAX_SPEED;
		}
		
		super.update();
		
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
//		if (moving) {
//			if (direction < 22.5 || direction > 337.5) {
//				g.drawImage(sprites[3][step], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 67.5 && direction < 112.5) {
//				g.drawImage(sprites[0][step], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 157.5 && direction < 202.5) {
//				g.drawImage(sprites[2][step], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 247.5 && direction <= 292.5) {
//				g.drawImage(sprites[1][step], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 22.5 && direction < 67.5) {
//				g.drawImage(sprites[6][step], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 112.5 && direction < 157.5) {
//				g.drawImage(sprites[5][step], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 202.5 && direction <= 247.5) {
//				g.drawImage(sprites[7][step], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 292.5 && direction <= 337.5) {
//				g.drawImage(sprites[4][step], (int)x - w/2, (int)y - h/2, null);
//			}
//		}
//		else {
//			if (direction < 22.5 || direction > 337.5) {
//				g.drawImage(sprites[3][0], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 67.5 && direction < 112.5) {
//				g.drawImage(sprites[0][0], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 157.5 && direction < 202.5) {
//				g.drawImage(sprites[2][0], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 247.5 && direction <= 292.5) {
//				g.drawImage(sprites[1][0], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 22.5 && direction < 67.5) {
//				g.drawImage(sprites[6][0], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 112.5 && direction < 157.5) {
//				g.drawImage(sprites[5][0], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 202.5 && direction <= 247.5) {
//				g.drawImage(sprites[7][0], (int)x - w/2, (int)y - h/2, null);
//			}
//			else if (direction >= 292.5 && direction <= 337.5) {
//				g.drawImage(sprites[4][0], (int)x - w/2, (int)y - h/2, null);
//			}
//		}
		
		Graphics2D g2d = (Graphics2D)g;
		
		AffineTransform af = new AffineTransform();
		af.rotate(Math.toRadians(-(direction-90)), x, y);
		g2d.transform(af);
		
		if (moving) {
			g.drawImage(sprites[0][step], (int)x - w/2, (int)y - h/2, null);
		}
		else {
			g.drawImage(sprites[0][0], (int)x - w/2, (int)y - h/2, null);
		}
	}
	
	/**
	 * Sets the current velocity of the player
	 */
	public void setVelocity(int x, int y) {
		super.setVelocity(x, y);

		destX = x;
		destY = y;

		// Complicated math for finding the direction of the players
		direction = -Math.atan2(y - this.y, x - this.x) * 180 / Math.PI;
		if (direction < 0) {
			direction += 360;
		}
	}
	
	/**
	 * Load the player's sprite sheet into memory
	 * @param sheet
	 */
	public void load(String sheet) {
		try {
			BufferedImage sh = ImageIO.read(new File(sheet));
			
			// Load the column in the first dimension of the array and
			// Load the row in the second dimension of the array
			int tempWidth = sh.getWidth();
			int tempHeight = sh.getHeight();
			for (int i = 0; i < tempWidth/w; i++) {
				for (int j = 0; j < tempHeight/h; j++) {
					sprites[i][j] = sh.getSubimage(i*w, j*h, w, h);
				}
			}
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
	
	public void setSprinting(boolean s) {
		sprinting = s;
	}
}
