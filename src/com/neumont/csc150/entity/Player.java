package com.neumont.csc150.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import javax.swing.Timer;

import com.neumont.csc150.Display;

public class Player extends Entity {
	public static final int MAX_SPEED_MULTIPLIER = 4;
	private int fDelay = 0;
	private int bDelay = 0;
	private boolean accel = false;
	private boolean rocketDraw = true;
	private boolean hide = false;

	public Player(double x, double y, double speed) {
		super(x, y, speed);
		w = 20;
		h = 20;
		direction = 0;
		
		// Rocket strobe timer
		Timer rocketAnim = new Timer(35, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rocketDraw = !rocketDraw;
			}
		});
		
		rocketAnim.start();
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;

		// Rotate the points based on the current angle
		Point[] temp = getPoints(1);
		AffineTransform.getRotateInstance(Math.toRadians(direction), x, y)
		.transform(getPoints(1), 0, temp, 0, 3);

		Polygon p = toPoly(temp);

		// Draw the rocket polygon strobe when moving
		g2d.setColor(Color.WHITE);
		if (accel && rocketDraw) {
			Point[] temp2 = getPoints(2);
			AffineTransform.getRotateInstance(Math.toRadians(direction), x, y)
			.transform(getPoints(2), 0, temp2, 0, 3);
			
			Polygon p2 = toPoly(temp2);
			g2d.drawPolygon(p2);
		}
		g2d.drawPolygon(p);
		
		// HITBOX DEBUG
//		if (Space.DEBUG) {
//			g2d.setColor(Color.GREEN);
//			g2d.drawRect((int)x-10, (int)y-10, w, h);
//		}
	}

	/**
	 * Convert the points into a Polygon
	 * @param points - Array of Point to create a Polygon from
	 * @return Polygon generated from the points
	 */
	public Polygon toPoly(Point[] points){
		Polygon tempPoly = new Polygon();

		for (int i = 0; i < points.length; i++){
			tempPoly.addPoint(points[i].x, points[i].y);
		}

		return tempPoly;
	}
	
	/**
	 * Hard coded base points to pull from when rotating the points
	 * @param t - Which points to use<br>0 is ship, 1 is rocket strobe
	 * @return Base points
	 */
	public Point[] getPoints(int t) {
		Point[] points = new Point[3];
		if (t == 1) {
			points[0] = new Point((int)x + 15, (int)y);
			points[1] = new Point((int)x - 15, (int)y - 10);
			points[2] = new Point((int)x - 15, (int)y + 10);
		}
		else {
			points[0] = new Point((int)x - 25, (int)y);
			points[1] = new Point((int)x - 15, (int)y - 5);
			points[2] = new Point((int)x - 15, (int)y + 5);
		}
			
		return points;
	}
	
	/**
	 * Adds friction and shooting delay
	 */
	public void update() {
		super.update();
		
		if (bDelay > 0) {
			bDelay--;
		}
		
		if (fDelay <= 0) {
			dx /= 1.01;
			dy /= 1.01;
		}
		else {
			fDelay--;
		}
	}
	
	// Reinitialize the player
	public void restart() {
		x = Display.WIDTH / 2 - 10;
		y = Display.HEIGHT / 2 - 10;
		dx = 0.0;
		dy = 0.0;
		direction = 0.0;
		
		//setInvincible(2.0);
	}
	
	public void setVel(double dx, double dy) {
		fDelay = 30;
		
		this.dx += dx;
		this.dy += dy;
		
		if (dx > 0) {
			if (this.dx > dx * MAX_SPEED_MULTIPLIER) {
				this.dx = dx * MAX_SPEED_MULTIPLIER;
			}
		}
		if (dx < 0) {
			if (this.dx < dx * MAX_SPEED_MULTIPLIER) {
				this.dx = dx * MAX_SPEED_MULTIPLIER;
			}
		}
		
		if (dy > 0) {
			if (this.dy > dy * MAX_SPEED_MULTIPLIER) {
				this.dy = dy * MAX_SPEED_MULTIPLIER;
			}
		}
		if (dy < 0) {
			if (this.dy < dy * MAX_SPEED_MULTIPLIER) {
				this.dy = dy * MAX_SPEED_MULTIPLIER;
			}
		}
		
		accel = true;
	}
	
	public boolean collide(Entity e) {
		Rectangle r = new Rectangle((int)x - 10, (int)y - 10, w, h);
		Rectangle r2 = new Rectangle((int)e.getX(), (int)e.getY(), e.getWidth(), e.getHeight());
		
		if (r.intersects(r2)) {
			return true;
		}
		
		return false;
	}
	
	public void hide(boolean h) {
		hide = h;
	}
	
	public boolean getHide() {
		return hide;
	}
	
	public void setAccel(boolean a) {
		accel = a;
	}

	public int getBDelay() {
		return bDelay;
	}

	public void setBDelay(int bDelay) {
		this.bDelay = bDelay;
	}
}
