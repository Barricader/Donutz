package com.neumont.csc150;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 * The view of the user
 * @author JoJones
 */
public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	
	private Donutz d;
	
	private Thread thread;
	private JFrame frame;
	private Listener l;
	
	private DecimalFormat df;
	
	private int loadAngle;
	private BufferedImage loadImage;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame();
		
		d = new Donutz(this);
		
		l = new Listener(d);
		addKeyListener(l);
		addMouseListener(l);
		addMouseMotionListener(l);
		
		df = new DecimalFormat("##.#%");
		loadAngle = 0;
		try {
			loadImage = ImageIO.read(new File("earth.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(2.0, 2.0);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (d.getCurArea() != null) {
			d.getCurArea().render(g);
		}
		
		if (d.getLoadPerc() >= 1.0) {
			d.getPlayer().render(g);
		}
		else {
			g.drawImage(loadImage, 0, 0, null);
			g.setColor(Color.WHITE);
			g.drawString("Welcome to Donutz, loading the starting level for you", WIDTH/4-140, HEIGHT / 3 - 20);
			g.drawString("Loading: " + df.format(d.getLoadPerc()), WIDTH / 4 - 40, HEIGHT / 4 - 10);
			g.drawArc(WIDTH/4-10, HEIGHT/4-70, 20, 20, loadAngle, 60);
			loadAngle++;
		}
		
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