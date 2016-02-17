package com.neumont.csc150;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

/**
 * The view of the user
 * @author JoJones
 */
public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 800;
	public static final int HEIGHT = 448;
	
	private Donutz d;
	
	private Thread thread;
	private JFrame frame;
	private Listener l;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame();
		
		d = new Donutz(this);
		
		l = new Listener(d);
		addKeyListener(l);
		addMouseListener(l);
		addMouseMotionListener(l);
	}
	
	/**
	 ** Start the thread
	 **/
	public synchronized void start() {
		//running = true;
		thread = new Thread(this, "Donutz");
		thread.start();
	}
	
	/**
	 ** Stop the thread
	 **/
	public synchronized void stop() {
		try {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			frame.dispose();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		d.run();
		
		stop();
	}
	
	/**
	 ** Renders the screen
	 **/
	public void render() {
		// Create a triple buffering strategy to create very smooth animations and movements
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		// Get the graphics object from the buffer strategy of the canvas
		Graphics g = bs.getDrawGraphics();
		g.translate(-d.getCamX(), -d.getCamY());
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		d.getCurArea().render(g);
		
		d.getPlayer().render(g);
		
		g.dispose();
		bs.show();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public Listener getListener() {
		return l;
	}
	
	public static void main(String[] args) throws AWTException {
		Display game = new Display();
		game.frame.setResizable(false);
		game.frame.setTitle("Donutz");
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		// A hack to have the window focused on start up
		Robot r = new Robot();
		r.mouseMove(game.frame.getLocationOnScreen().x + WIDTH /2, game.frame.getLocationOnScreen().y + HEIGHT/2);
		//r.mousePress(InputEvent.BUTTON1_MASK);
		//r.mouseRelease(InputEvent.BUTTON1_MASK);

		game.start();
	}
}