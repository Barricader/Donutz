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
	
	// TODO: put main model class here
//	private Space s;
	
	private Thread thread;
	private JFrame frame;
	// TODO: make a listener with a mouselistener here
	//private Listener l;
	
	private boolean running = true;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame();
		
		//s = new Space(this);
		
		//l = new Listener(s);
		//addKeyListener(l);
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
	
	// TODO: remove me and put in main model
	public void update() {
		
	}

	public void run() {
		// TODO: put main model run method here
		//s.run();
		
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		// Game loop
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			
			render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				getFrame().setTitle("Donutz | " + frames + " fps");
				frames = 0;
			}
		}
		
		//stop();
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
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		
		
		g.dispose();
		bs.show();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
//	public Listener getListener() {
//		return l;
//	}
	
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
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON1_MASK);

		game.start();
	}
}