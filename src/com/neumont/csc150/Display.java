package com.neumont.csc150;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * The view of the user
 * @author JoJones
 */
public class Display extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;
	
	private Donutz d;
	
	private Thread thread;
	private JFrame frame;
	private Listener l;
	
	private DecimalFormat df;
	
	private int loadAngle;
	private BufferedImage loadImage;
	private BufferedImage menuImage;
	private BufferedImage hpImage;
	private BufferedImage dmgImage;

	private int invX;

	public Display() {
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame();
		frame.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("DonutCursor.png").getImage(),new Point(0,0),"custom cursor"));
		
		d = new Donutz(this);
		
		// Listeners for keyboard and mouse
		l = new Listener(d);
		addKeyListener(l);
		addMouseListener(l);
		addMouseMotionListener(l);
		
		invX = WIDTH/2;
		
		// Used for loading screen to show percent loaded
		df = new DecimalFormat("##.#%");
		loadAngle = 0;
		try {
			loadImage = ImageIO.read(new File("earth.png"));
			menuImage = ImageIO.read(new File("Donut.png"));
			hpImage = ImageIO.read(new File("heart.png"));
			dmgImage = ImageIO.read(new File("sword2.png"));
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
	 * @throws IOException 
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
		
		// Draw the menu if we are in the menu state
		g.setColor(Color.WHITE);
		if (d.isInMenu()) {
			g.setFont(new Font("LucidaConsole", Font.PLAIN, 64));
			g.drawString("DONUTZ", WIDTH / 2 - 450, HEIGHT / 4 - 10);
			g.drawImage(menuImage, WIDTH / 2 - 175, HEIGHT / 2 - 250, null);
			g.drawRect(WIDTH / 2 - 450, HEIGHT / 2 - 250, WIDTH / 2 - 370, HEIGHT / 2 - 290);
			g.setFont(new Font("LucidaConsole", Font.PLAIN, 18));
			g.drawString("New Game", WIDTH / 2 - 450, HEIGHT / 2 - 140);
			g.drawString("Load Game", WIDTH / 2 - 450, HEIGHT / 2 - 120);
			g.drawString("Exit", WIDTH / 2 - 450, HEIGHT / 2 - 100);
			
			Polygon p = new Polygon();
			
			if (d.getSelector() == 0) {
				p.addPoint(WIDTH / 2 - 457, HEIGHT / 2 - 147);
				p.addPoint(WIDTH / 2 - 462, HEIGHT / 2 - 142);
				p.addPoint(WIDTH / 2 - 462, HEIGHT / 2 - 152);
				g.drawLine(WIDTH / 2 - 450, HEIGHT / 2 - 137, WIDTH / 2 - 360, HEIGHT / 2 - 137);
			}
			else if(d.getSelector() == 1){
				p.addPoint(WIDTH / 2 - 457, HEIGHT / 2 - 127);
				p.addPoint(WIDTH / 2 - 462, HEIGHT / 2 - 122);
				p.addPoint(WIDTH / 2 - 462, HEIGHT / 2 - 132);
				g.drawLine(WIDTH / 2 - 450, HEIGHT / 2 - 117, WIDTH / 2 - 355, HEIGHT / 2 - 117);
			}
			else if(d.getSelector() == 2){
				p.addPoint(WIDTH / 2 - 457, HEIGHT / 2 - 107);
				p.addPoint(WIDTH / 2 - 462, HEIGHT / 2 - 102);
				p.addPoint(WIDTH / 2 - 462, HEIGHT / 2 - 112);
				g.drawLine(WIDTH / 2 - 450, HEIGHT / 2 - 97, WIDTH / 2 - 420, HEIGHT / 2 - 97);
			}
			
			g.fillPolygon(p);
		}
		else {
			if (d.getCurArea() != null && d.getLoadPerc() >= 1.0) {
				d.getCurArea().render(g, false);
				d.getPlayer().render(g);
				d.getCurArea().render(g, true);
				
				drawInv(g2d);
			}
			else {
				// If the area is not loaded yet, draw the load screen
				g.drawImage(loadImage, 0, 0, null);
				g.setColor(Color.WHITE);
				g.drawString("Pro tip: " + Donutz.CUR_TIP, (int)(WIDTH/4-(Donutz.CUR_TIP.length() * 4.75)), HEIGHT / 3 - 20);
				g.drawString("Loading: " + df.format(d.getLoadPerc()), WIDTH / 4 - 40, HEIGHT / 4 - 10);
				g.drawArc(WIDTH/4-10, HEIGHT/4-70, 20, 20, loadAngle, 60);
				loadAngle++;
			}
		}
		g.dispose();
		bs.show();
	}
	
	private void drawInv(Graphics2D g2d) {
		g2d.setFont(new Font("LucidaConsole", Font.PLAIN, 16));
		g2d.setColor(new Color(140, 140, 140, 200));
		g2d.fillRect(invX + d.getCamX()/2, HEIGHT/4 - 100 + d.getCamY()/2, 200, 200);
		

		g2d.setColor(new Color(220, 220, 220, 240));
		g2d.drawString("Equipped", invX + d.getCamX()/2 + 125, HEIGHT/4 - 100 + d.getCamY()/2 + 15);
		g2d.drawString("Stats", invX + d.getCamX()/2 + 138, HEIGHT/4 - 100 + d.getCamY()/2 + 80);
		
		g2d.setFont(new Font("LucidaConsole", Font.PLAIN, 12));
		g2d.setColor(new Color(140, 60, 60, 240));
		
		String hp = d.getPlayer().getCurHP() + "/" + d.getPlayer().getMaxHP();
		g2d.drawImage(hpImage, invX + d.getCamX()/2 + 125, HEIGHT/4 - 100 + d.getCamY()/2 + 87, null);
		g2d.drawString(hp, invX + d.getCamX()/2 + 145, HEIGHT/4 - 100 + d.getCamY()/2 + 100);
		
		String dmg = d.getPlayer().getMinDmg() * d.getPlayer().getEWeapon().getDmg() + " - " + d.getPlayer().getMaxDmg() * d.getPlayer().getEWeapon().getDmg();
		g2d.drawImage(dmgImage, invX + d.getCamX()/2 + 125, HEIGHT/4 - 100 + d.getCamY()/2 + 110, null);
		g2d.drawString(dmg, invX + d.getCamX()/2 + 145, HEIGHT/4 - 100 + d.getCamY()/2 + 123);
		
		g2d.setColor(new Color(200, 200, 200, 140));
		g2d.drawRect(invX + d.getCamX()/2 + 140, HEIGHT/4 - 100 + d.getCamY()/2 + 25, 32, 32);
		if (d.getPlayer().getEWeapon() != null) { 
			g2d.drawImage(d.getPlayer().getEWeapon().getSprite(), invX + d.getCamX()/2 + 140, HEIGHT/4 - 100 + d.getCamY()/2 + 25, null);
		}
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 3; j++) {
				g2d.drawRect(invX + d.getCamX() /2 + j*40 + 4, HEIGHT/4 - 100 + d.getCamY()/2 + i*40 + 4, 32, 32);
				
				if (d.getPlayer().getItems().get(i*3 + j) != null) {
					g2d.drawImage(d.getPlayer().getItems().get(i*3 + j).getSprite(),
						invX + d.getCamX() /2 + j*40 + 4, HEIGHT/4 - 100 + d.getCamY()/2 + i*40 + 4, null);
				}
			}
		}
	}
	
	public void setInvX(int x) {
		invX = x;
	}
	
	public int getInvX() {
		return invX;
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public Listener getListener() {
		return l;
	}
	
	static boolean test;
	public static void main(String[] args) throws AWTException {
		Display game = new Display();
		//game.frame.setResizable(false);
		game.frame.setTitle("Donutz");
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		
		test = false;
		game.frame.addComponentListener(new ComponentListener() {
			public void componentResized(ComponentEvent e) {
				if (test) {
					WIDTH = e.getComponent().getWidth();
					HEIGHT = e.getComponent().getHeight();
					
					game.d.setMaxOffsetX(game.d.getCurArea().getWidth() * game.d.getCurArea().getTiles().get(0).get(0).getWidth()
							- Display.WIDTH / 2);
					game.d.setMaxOffsetY(game.d.getCurArea().getHeight() * game.d.getCurArea().getTiles().get(0).get(0).getHeight()
							- Display.HEIGHT / 2);
					game.d.setMaxOffsetX(game.d.getMaxOffsetX()*2);
					game.d.setMaxOffsetY(game.d.getMaxOffsetY()*2);
					
					if (game.d.getInvOpen()) {
						game.invX = WIDTH/2 - 198;
					}
					else {
						game.invX = WIDTH/2;
					}
				}
				else {
					test = true;
				}
			}
			
			public void componentMoved(ComponentEvent e) {}

			public void componentShown(ComponentEvent e) {	}

			public void componentHidden(ComponentEvent e) {}
		});
		
		// Used to move mouse to the middle of the window
		Robot r = new Robot();
		r.mouseMove(game.frame.getLocationOnScreen().x + WIDTH /2, game.frame.getLocationOnScreen().y + HEIGHT/2);
		r.mousePress(InputEvent.BUTTON1_MASK);
		r.mouseRelease(InputEvent.BUTTON2_MASK);
		
		game.start();
	}
}
