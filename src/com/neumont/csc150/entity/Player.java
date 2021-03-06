package com.neumont.csc150.entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.neumont.csc150.Display;
import com.neumont.csc150.Donutz;
import com.neumont.csc150.Tile;
import com.neumont.csc150.item.Item;
import com.neumont.csc150.item.Weapon;

// TODO: add variables for skill points and what not
public class Player extends Entity {
	public static final int MAX_SPEED = 3, MAX_INVENTORY = 15;
	private boolean moving = false;
	private boolean hide = false;
	private boolean sprinting;
	private double destX, destY;
	private BufferedImage[][] sprites;
	private int step;
	private Timer stepTime;
	private Vector<Item> items;
	private Weapon eWeapon;
	private int curHP, maxHP;
	private int minDmg, maxDmg;
	protected Random rand;

	public Player(double x, double y) {
		super(x, y, MAX_SPEED);
		w = 32;
		h = 32;
		direction = 0;
		destX = x;
		destY = y;
		sprinting = false;
		maxHP = 1000;
		curHP = 1000;
		
		minDmg = 1;
		maxDmg = 2;
		
		rand = new Random();

		// A timer that just animates the player
		step = 0;
		stepTime = new Timer(150, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!Donutz.getInstance().getInvOpen()) {
					step++;
					if (step > 3) {
						step = 0;
					}
				}
			}
		});
		

		items = new Vector<Item>();
		
		for (int i = 0; i < MAX_INVENTORY; i++) {
			items.add(null);
		}
		
		eWeapon = null;
		
		stepTime.start();
	
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

		Vector<Tile> tColLayer = Donutz.getInstance().getCurArea().getColLayer();
		Vector<Vector<Tile>> exitLayers = Donutz.getInstance().getCurArea().getTeleportLayers();
		Rectangle temp = getRect();
		
		if (tColLayer != null) {
			for (int i = 0; i < tColLayer.size(); i++) {
				if (tColLayer.get(i).getID() != 0) {
					Rectangle t = tColLayer.get(i).getRect();
					if ((t.x + t.width > temp.x + dx - 16 && t.x < temp.x + temp.width + 16) &&
						(t.y + t.height > temp.y + dy - 16 && t.y < temp.y + temp.height + 16)) {
						
						if (t.x + t.width > temp.x+4 + dx && t.x < temp.x+4 + dx && t.y < temp.y + temp.height-6 + dy && t.y + t.height > temp.y+6 + dy) {
							if (dx < 0) {
								dx = 0;
								collides = true;
							}
						}
						
						if (t.x < temp.x + temp.width-4 + dx && t.x + t.width > temp.x + temp.width-4 + dx && t.y < temp.y + temp.height-6 + dy && t.y + t.height > temp.y+6 + dy) {
							if (dx > 0) {
								dx = 0;
								collides = true;
							}
						}
						
						if (t.y + t.height > temp.y+4 + dy && t.y < temp.y+4 + dy && t.x < temp.x + temp.width-8 + dx && t.x + t.width > temp.x+8 + dx) {
							if (dy < 0) {
								dy = 0;
								collides = true;
							}
						}
						
						if (t.y < temp.y + temp.height-4 + dy && t.y + t.height > temp.y + temp.height-4 + dy && t.x < temp.x + temp.width-8 + dx && t.x + t.width > temp.x+8 + dx) {
							if (dy > 0) {
								dy = 0;
								collides = true;
							}
						}
					}
				}
			}
		}
		
		if (Donutz.getInstance().getAreas().size() > 0 && Donutz.getInstance().getLoadPerc() == 1.0) {
			Vector<Chest> tempChests = Donutz.getInstance().getChests().get(Donutz.getInstance().getAreas().indexOf(Donutz.getInstance().getCurArea()));
			for (int i = 0; i < tempChests.size(); i++) {
				Rectangle t = tempChests.get(i).getRect();
				if ((t.x + t.width > temp.x + dx - 16 && t.x < temp.x + temp.width + 16) &&
						(t.y + t.height > temp.y + dy - 16 && t.y < temp.y + temp.height + 16)) {
	
					if (t.x + t.width > temp.x+4 + dx && t.x < temp.x+4 + dx && t.y < temp.y + temp.height-6 + dy && t.y + t.height > temp.y+6 + dy) {
						if (dx < 0) {
							dx = 0;
							collides = true;
						}
					}
	
					if (t.x < temp.x + temp.width-4 + dx && t.x + t.width > temp.x + temp.width-4 + dx && t.y < temp.y + temp.height-6 + dy && t.y + t.height > temp.y+6 + dy) {
						if (dx > 0) {
							dx = 0;
							collides = true;
						}
					}
	
					if (t.y + t.height > temp.y+4 + dy && t.y < temp.y+4 + dy && t.x < temp.x + temp.width-8 + dx && t.x + t.width > temp.x+8 + dx) {
						if (dy < 0) {
							dy = 0;
							collides = true;
						}
					}
	
					if (t.y < temp.y + temp.height-4 + dy && t.y + t.height > temp.y + temp.height-4 + dy && t.x < temp.x + temp.width-8 + dx && t.x + t.width > temp.x+8 + dx) {
						if (dy > 0) {
							dy = 0;
							collides = true;
						}
					}
				}
			}
		}
		
		Object[] tempKeys = Donutz.getInstance().getCurArea().getTPMap().keySet().toArray();

		for (int j = 0; j < exitLayers.size(); j++) {
			for (int i = 0; i < exitLayers.get(j).size(); i++) {
				if (exitLayers.get(j).get(i).getID() != 0) {
					if (getRect().intersects(exitLayers.get(j).get(i).getRect())) {
						int tempIndex = Integer.parseInt(tempKeys[j].toString());
						Donutz.getInstance().requestTP(Donutz.getInstance().getCurArea().getLocation(tempIndex));
					}
				}
			}
		}
		
		// TODO: fix reading from the key set and stuff 
		
		if (sprinting) {
			speed = MAX_SPEED + 2;
		}
		else {
			speed = MAX_SPEED;
		}
		
		super.update();
		
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
//		Rectangle r = new Rectangle((int)x - w/2, (int)y - w/2, w, h);	// DEBUG
//		g.drawRect(r.x, r.y, r.width, r.height);
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
		
		try {
			g2d.transform(af.createInverse());
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
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
	
	public Vector<Item> getItems() {
		return items;
	}
	
	public void setEWeapon(Weapon w) {
		eWeapon = w;
	}
	
	public Weapon getEWeapon() {
		if (eWeapon != null) {
			return eWeapon;
		}
		else {
			return new Weapon("null", -1, "null", 1);
		}
	}
	
	public void addItem(Item it) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == null) {
				items.set(i, it);
				return;
			}
		}
		
		System.out.println("NO FREE SPACES");
	}
//	Player attack----------------------------------------------
	public int attack() {
		int damage = rand.nextInt(maxDmg) + minDmg;
		return damage;
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

	public int getCurHP() {
		return curHP;
	}

	public void setCurHP(int curHP) {
		this.curHP = curHP;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(int maxHP) {
		this.maxHP = maxHP;
	}

	public int getMinDmg() {
		return minDmg;
	}

	public void setMinDmg(int minDmg) {
		this.minDmg = minDmg;
	}

	public int getMaxDmg() {
		return maxDmg;
	}

	public void setMaxDmg(int maxDmg) {
		this.maxDmg = maxDmg;
	}
}
