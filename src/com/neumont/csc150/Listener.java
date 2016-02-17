package com.neumont.csc150;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Handles the mouse input from the Display
 * @author JoJones
 */
public class Listener implements KeyListener, MouseListener, MouseMotionListener {
	private Donutz d;
	
	// States of keys
	public boolean q = false, w = false, e = false, r = false, space = false, esc = false, enter = false;
	
	int mx, my;
	Timer t;
	TimerTask tt;
	
	Listener(Donutz d) {
		this.d = d;
		
		t = new Timer();
		tt = new TimerTask() {
			public void run() {
				d.getPlayer().setVelocity(mx, my);
			}
		};
		
		mx = 0;
		my = 0;
	}
	
	public void keyTyped(KeyEvent e) {}

	public void keyPressed(KeyEvent ke) {
		switch (ke.getKeyCode()) {
			case KeyEvent.VK_Q:
				q = true;
				break;
			case KeyEvent.VK_W:
				w = true;
				break;
			case KeyEvent.VK_E:
				e = true;
				break;
			case KeyEvent.VK_R:
				r = true;
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
		}
	}

	public void keyReleased(KeyEvent ke) {
		switch (ke.getKeyCode()) {
			case KeyEvent.VK_Q:
				q = false;
				break;
			case KeyEvent.VK_W:
				w = false;
				break;
			case KeyEvent.VK_E:
				e = false;
				break;
			case KeyEvent.VK_R:
				r = false;
				break;
			case KeyEvent.VK_SPACE:
				space = false;
				break;
			case KeyEvent.VK_ESCAPE:
				esc = false;
				break;
			case KeyEvent.VK_ENTER:
				enter = false;
				break;
		}
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		if (!d.getPlayer().getRect().contains(e.getPoint())) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				t = new Timer();
				if (!tt.cancel()) {
					tt = new TimerTask() {
						public void run() {
							//if (!d.getPlayer().getRect().contains(e.getPoint())) {
								d.getPlayer().setVelocity(mx, my);
							//}
						}
					};
				}
				t.scheduleAtFixedRate(tt, 0, 100);
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (!tt.cancel()) {
				tt = new TimerTask() {
					public void run() {
						//if (!d.getPlayer().getRect().contains(e.getPoint())) {
							d.getPlayer().setVelocity(mx, my);
						//}
					}
				};
			}
		}
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}
	
	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}
}
