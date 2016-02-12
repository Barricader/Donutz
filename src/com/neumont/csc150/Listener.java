package com.neumont.csc150;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Handles the mouse input from the Display
 * @author JoJones
 */
public class Listener implements KeyListener, MouseListener {
	private Donutz d;
	
	// States of keys
	public boolean q = false, w = false, e = false, r = false, space = false, esc = false, enter = false;
	
	Listener(Donutz d) {
		this.d = d;
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
				// TODO: main model exit here
				//sp.setRunning(false);
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
				// TODO: main model exit here
				//sp.setRunning(false);
				break;
			case KeyEvent.VK_ENTER:
				enter = false;
				break;
		}
	}

	public void mouseClicked(MouseEvent e) {
		// TODO: main model click handler here
		//m.clicked(e.x, e.y);
		System.out.println(e.getX() + " " + e.getY());
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}
}
