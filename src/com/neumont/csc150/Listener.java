package com.neumont.csc150;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import com.neumont.csc150.entity.Chest;
import com.neumont.csc150.item.Weapon;

/**
 * Handles the mouse and keyboard input from the Display
 * @author JoJones
 */
public class Listener implements KeyListener, MouseListener, MouseMotionListener {
	private Donutz d;
	
	// States of keys
	public boolean i = false, w = false, s = false, up = false, down = false, space = false,
			esc = false, enter = false, shift = false;
	
	private int mx, my;
	private boolean mPressed;
	private Timer t;
	private TimerTask tt;
	
	Listener(Donutz d) {
		this.d = d;
		
		t = new Timer();
		tt = new TimerTask() {
			public void run() {
				// Does nothing, just used to instantiate
				d.getPlayer().setVelocity(mx, my);
			}
		};
		
		mPressed = false;
		mx = 0;
		my = 0;
	}
	
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
			case KeyEvent.VK_I:
				i = true;
				break;
			case KeyEvent.VK_W:
				w = true;
				break;
			case KeyEvent.VK_S:
				s = true;
				break;
			case KeyEvent.VK_UP:
				up = true;
				break;
			case KeyEvent.VK_DOWN:
				down = true;
				break;
			case KeyEvent.VK_SPACE:
				space = true;
				break;
			case KeyEvent.VK_ESCAPE:
				esc = true;
				d.setRunning(false);
				break;
			case KeyEvent.VK_ENTER:
				enter = true;
				break;
			case KeyEvent.VK_SHIFT:
				d.getPlayer().setSprinting(true);
				shift = true;
				break;
		}
	}

	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
			case KeyEvent.VK_I:
				i = false;
				if (d.getLoadPerc() == 1.0) {
					d.setInvOpen(!d.getInvOpen());
					
					if (!d.getInvOpen()) {
						if (d.getLastSelected() != -1) {
							if (d.getLastSelected() != 100) {
								d.getPlayer().getItems().set(d.getLastSelected(), d.getSelected());
								d.setSelected(null);
							}
							else {
								d.getPlayer().setEWeapon((Weapon) d.getSelected());
								d.setSelected(null);
							}
							
							d.setLastSelected(-1);
						}
					}
				}
				break;
			case KeyEvent.VK_W:
				w = false;
				break;
			case KeyEvent.VK_S:
				s = false;
				break;
			case KeyEvent.VK_UP:
				up = false;
				break;
			case KeyEvent.VK_DOWN:
				down = false;
				break;
			case KeyEvent.VK_P:
				d.getPlayer().addItem(new Weapon("Brick Breaker", 1, "BB.png", 20));
				break;
			case KeyEvent.VK_C:
				d.getPlayer().setEWeapon(new Weapon("Brick Breaker", 1, "BB.png", 20));
				break;
			case KeyEvent.VK_SPACE:
				space = false;
				break;
			case KeyEvent.VK_ESCAPE:
				esc = false;
				break;
			case KeyEvent.VK_ENTER:
				enter = false;
				if (!d.isInMenu() && d.getLoadPerc() == 1.0) {
					if (d.getPlayer().getX() >= 288 && d.getPlayer().getX() <= 320 && d.getPlayer().getY() >= 1850 && d.getPlayer().getY() >= 1865) {
						if (d.isInForest1() == true) {
							d.setInForest1(false);
							d.getForest1().stop();
							d.getForest2();
							d.getForest2().play();
						}
					}
					
					Rectangle temp = d.getPlayer().getRect();
					temp.x -= 8;
					temp.y -= 8;
					temp.width += 16;
					temp.height += 16;
					Vector<Chest> tempChests = Donutz.getInstance().getChests().get(Donutz.getInstance().getAreas().indexOf(Donutz.getInstance().getCurArea()));
					for (int i = 0; i < tempChests.size(); i++) {
						Rectangle t = tempChests.get(i).getRect();
						if (temp.intersects(t)) {
							tempChests.get(i).setOpened(true);
							d.setCurChestInv(i);
						}
					}
				}
				break;
			case KeyEvent.VK_SHIFT:
				d.getPlayer().setSprinting(false);
				shift = false;
				break;
		}
	}

	public void mouseClicked(MouseEvent e) {}

	/**
	 * Checks when the mouse is pressed and sets the players velocity
	 * to the coordinates of the mouse
	 */
	public void mousePressed(MouseEvent e) {
		Point temp = new Point(e.getX()/2, e.getY()/2);
		if (!d.isInMenu() && d.getLoadPerc() == 1.0) {
			if (!d.getInvOpen()) {
				if (!d.getPlayer().getRect().contains(temp)) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						mPressed = true;
						t.cancel();
						t = new Timer();
						if (!tt.cancel() || tt.cancel()) {
							tt = new TimerTask() {
								public void run() {
									if (!d.getInvOpen()) {
										int newX = mx;
										int newY = my;
										
										if (d.getPlayer().getX() > Display.WIDTH / 4) {
											newX = (int) ((mx - Display.WIDTH / 4) + d.getPlayer().getX());
										}
										if (d.getPlayer().getX() > d.getMaxOffsetX() - (Display.WIDTH * (3.0/4.0))) {
											newX = (mx - Display.WIDTH/4) + (d.getMaxOffsetX() - Display.WIDTH + Display.WIDTH/4);
										}
										if (d.getPlayer().getY() > Display.HEIGHT / 4) {
											newY = (int) ((my - Display.HEIGHT / 4) + d.getPlayer().getY());
										}
										if (d.getPlayer().getY() > d.getMaxOffsetY() - Display.HEIGHT*2) {
											newY = (my - Display.HEIGHT/4) + (d.getMaxOffsetY() - Display.HEIGHT*2 + 60);
										}
										
										d.getPlayer().setVelocity(newX, newY);
									}
								}
							};
						}
						
						t.scheduleAtFixedRate(tt, 0, 100);
					}
				}
			}
			else {
				if (e.getButton() == MouseEvent.BUTTON1) {
					for (int i = 0; i < d.getPlayer().getItems().size(); i++) {
						Rectangle itemBox = new Rectangle(d.getDisplay().getInvX() + (i%3*40) + 4, Display.HEIGHT/4 - 100 + i/3*40 + 4, 32, 32);			
						if (itemBox.contains(temp)) {
							if (d.getSelected() == null && d.getPlayer().getItems().get(i) != null) {
								d.setSelected(d.getPlayer().getItems().get(i));
								d.getPlayer().getItems().set(i, null);
								d.setLastSelected(i);
							}
						}
					}
					
					Rectangle equipBox = new Rectangle(d.getDisplay().getInvX() + 140, Display.HEIGHT/4 - 100 + 25, 32, 32);			
					if (equipBox.contains(temp)) {
						if (d.getSelected() == null && !d.getPlayer().getEWeapon().getName().equals("null")) {
							d.setSelected(d.getPlayer().getEWeapon());
							d.getPlayer().setEWeapon(null);
							d.setLastSelected(100);
						}
					}
				}
			}
		}
	}

	/**
	 * Resets the timer task
	 */
	public void mouseReleased(MouseEvent e) {
		Point temp = new Point(e.getX()/2, e.getY()/2);
		if (!d.isInMenu() || d.getLoadPerc() == 1.0) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (!d.getInvOpen()) {
					mPressed = false;
					if (d.getPlayer().isColliding()) {
						d.getPlayer().setDx(0);
						d.getPlayer().setDy(0);
						d.getPlayer().setCollides(false);
					}
					if (tt.cancel()) {
						t.cancel();
					}
				}
				else {
					for (int i = 0; i < d.getPlayer().getItems().size(); i++) {
						Rectangle itemBox = new Rectangle(d.getDisplay().getInvX() + (i%3*40) + 4, Display.HEIGHT/4 - 100 + i/3*40 + 4, 32, 32);			
						if (itemBox.contains(temp)) {
							if (d.getSelected() != null && d.getPlayer().getItems().get(i) == null) {
								d.getPlayer().getItems().set(i, d.getSelected());
								d.setSelected(null);
								d.setLastSelected(-1);
							}
						}
					}
	
					Rectangle equipBox = new Rectangle(d.getDisplay().getInvX() + 140, Display.HEIGHT/4 - 100 + 25, 32, 32);			
					if (equipBox.contains(temp)) {
						if (d.getSelected() != null && d.getPlayer().getEWeapon().getName().equals("null")) {
							if (d.getSelected().getClass().getSimpleName().equals("Weapon")) {
								d.getPlayer().setEWeapon((Weapon) d.getSelected());
								d.setSelected(null);
								d.setLastSelected(-1);
							}
						}
					}
					
					if (d.getSelected() != null) {
						if (d.getLastSelected() != 100) {
							d.getPlayer().getItems().set(d.getLastSelected(), d.getSelected());
							d.setSelected(null);
						}
						else {
							d.getPlayer().setEWeapon((Weapon) d.getSelected());
							d.setSelected(null);
						}
	
						d.setLastSelected(-1);
					}
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
	/**
	 * Updates the current x and y of the mouse
	 */
	public void mouseDragged(MouseEvent e) {
		mx = e.getX()/2;
		my = e.getY()/2;
	}

	/**
	 * Updates the current x and y of the mouse
	 */
	public void mouseMoved(MouseEvent e) {
		mx = e.getX()/2;
		my = e.getY()/2;
	}
	
	public boolean isMPressed() {
		return mPressed;
	}
	
	public int getMx() {
		return mx;
	}
	
	public int getMy() {
		return my;
	}
}
