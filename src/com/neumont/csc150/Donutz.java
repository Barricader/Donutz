package com.neumont.csc150;

import java.util.Random;
import java.util.Vector;

import com.neumont.csc150.entity.Player;

public class Donutz {
	public static final String DEFAULT_FILE = "save.json";
	public static boolean DEBUG = false;
//	private Vector<Asteroid> ast;
//	private Vector<Bullet> bul;
//	private Vector<Particle> par;
	private Vector<Area> areas;
	private Area curArea;
	private Player p;

	boolean showGameOver;
	private boolean end;
	private boolean inMenu;
	private boolean running;
	private boolean init;
	
	private int selector;
	private int menuDelay;
	
	private Random r;
	
	private Display d;
	
	public Donutz() {
		this(null);
	}
	
	public Donutz(Display d) {
		r = new Random();
		
		areas = new Vector<Area>();
		areas.add(new Area("dirtMap.json"));
		curArea = areas.get(0);
//		ast = new Vector<Asteroid>();
//		bul = new Vector<Bullet>();
//		par = new Vector<Particle>();
		
		//load();
		
		showGameOver = false;
		inMenu = true;
		running = true;
		end = false;
		init = true;
		
		selector = 0;
		menuDelay = 0;
		
		p = new Player(Display.WIDTH/2 - 10, Display.HEIGHT/2 - 10, 0);
		
		this.d = d;
	}
	
	public void run() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		int frames = 0;
		// Game loop
		
		areas.add(new Area("dirtMap.json"));
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1) {
				update();
				delta--;
			}
			
			d.render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				d.getFrame().setTitle("Donutz | " + frames + " fps");
				frames = 0;
			}
		}
	}
	
	/**
	 * The game loop
	 **/
	public void update() {
		if (!inMenu) {
			if (!end) {
				playerUpdate();
			}
		}
		else {
			menuUpdate();
		}
	}
	
	/**
	 * Update the player
	 */
	private void playerUpdate() {
		if (!p.isDead()) {
//			if (init || ast.size() == 0) {
//				for (int i = 0; i < NUM_AST; i++) {
//					ast.addElement(genAsteroid());
//				}
//				
//				// Give extra lives if you clear a level
//				if (!init) {
//					lives += 2;
//				}
//				
//				init = false;
//				
//				p.setInvincible(2.0);
//			}
//			
//			p.setAccel(false);
//			if (d.getListener().w || d.getListener().up) {
//				double x2 = Math.cos(Math.toRadians(p.getDirection()));
//				double y2 = Math.sin(Math.toRadians(p.getDirection()));
//				p.setVel(x2, y2);
//				p.setAccel(true);
//			}
//			if (d.getListener().a || d.getListener().left) {
//				p.setDirection(p.getDirection() - 4);
//			}
//			else if (d.getListener().d || d.getListener().right) {
//				p.setDirection(p.getDirection() + 4);
//			}
//			if (d.getListener().space && p.getBDelay() <= 0 && !p.isCollides()) {
//				bul.addElement(genBullet());
//			}
			
			p.update();
		}
		else {
			if (!showGameOver) {
				end = true;
			}
		}
	}
	
	/**
	 * Update the asteroids
	 */
//	private void asteroidUpdate() {
//		for (int i = 0; i < ast.size(); i++) {
//			ast.get(i).update();
//			
//			if (p.collide(ast.get(i)) && !p.isInvincible() && lives > 0 && !inMenu) {
//				p.setCollides(true);
//				
//				Timer timeout = new Timer(1000, new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						p.hide(false);
//						p.restart();
//						((Timer)e.getSource()).stop();
//					}
//				});
//				
//				timeout.start();
//				
//				p.setInvincible(0.999);
//				p.hide(true);
//				
//				lives--;
//				score += ast.get(i).giveScore();
//				
//				Random r = new Random();
//				for (int j = 0; j < r.nextInt(ast.get(i).getSize()) + 5; j++) {
//					Particle p = new Particle(ast.get(i).getX(), ast.get(i).getY(), 1 + r.nextDouble() * 2);
//					p.setVelocity(r.nextInt(Display.WIDTH), r.nextInt(Display.HEIGHT));
//					par.add(p);
//				}
//				ast.addAll(ast.get(i).destroy());
//				ast.remove(i);
//			}
//		}
//		
//	}
	
	/**
	 * Update the menu
	 */
	private void menuUpdate() {
//		if ((d.getListener().s || d.getListener().down) && menuDelay <= 0) {
//			selector++;
//			menuDelay = 20;
//		}
//		else if ((d.getListener().w || d.getListener().up) && menuDelay <= 0) {
//			selector--;
//			menuDelay = 20;
//		}
		
		if (menuDelay > 0) {
			menuDelay--;
		}
	}

	public void chooseSelected() {
		// Play game
		if (selector % 2 == 0) {
			restart();
			inMenu = false;
		}
		// Show highscores
		else {
			showGameOver = true;
		}
	}
	
	/**
	 * Generate a random Asteroid
	 * @return Generated asteroid
	 */
//	public Asteroid genAsteroid() {
//		int t = r.nextInt(4);
//		int x = 0, y = 0;
//		switch (t) {
//			case 0:
//				x = -20;
//				y = r.nextInt(Display.HEIGHT);
//				break;
//			case 1:
//				y = -20;
//				x = r.nextInt(Display.WIDTH);
//				break;
//			case 2:
//				x = Display.WIDTH + 20;
//				y = r.nextInt(Display.HEIGHT);
//				break;
//			case 3:
//				y = Display.HEIGHT + 20;
//				x = r.nextInt(Display.WIDTH);
//				break;
//		}
//
//		Asteroid a = new Asteroid(x, y, Asteroid.SLOW, Asteroid.BIG);
//		a.setVelocity(r.nextInt(Display.WIDTH), r.nextInt(Display.HEIGHT));
//		return a;
//	}
	
	/**
	 * Restart and init the game
	 */
	public void restart() {
		r = new Random();
		
		end = false;
		showGameOver = false;
		
//		ast = new Vector<Asteroid>();
//		bul = new Vector<Bullet>();
//		par = new Vector<Particle>();
		
		p = new Player(Display.WIDTH/2 - 10, Display.HEIGHT/2 - 10, 0);
		
		init = true;
		//b.hide();
	}
	
	/*
	 * Save the state of the game
	 * @param name - Name of file to save to
	 */
//	public void save() {
//		//JSONObject scores = new JSONObject();
//		
//		HashMap<String, Integer> temp = new HashMap<String, Integer>();
//		temp.put(highscoreName, score);
//		
//		System.out.println(temp);
//		
//		hScores.addAll(temp.entrySet());
//		System.out.println(hScores);
//		hScores.remove(hScores.last());
//		System.out.println(hScores);
//		
//		JSONObject obj = new JSONObject();
//		JSONArray names = new JSONArray();
//		JSONArray scores = new JSONArray();
//		
//		Iterator<Entry<String, Integer>> it = hScores.iterator();
//		while (it.hasNext()) {
//			Entry<String, Integer> tempEntry = it.next();
//			names.add(tempEntry.getKey());
//			scores.add(tempEntry.getValue());
//			
//		}
//		obj.put("names", names);
//		obj.put("scores", scores);
//		
//		// Save the data to a file
//		try {
//			PrintStream ps = new PrintStream(new FileOutputStream(DEFAULT_FILE));
//			ps.print(obj.toJSONString());
//			ps.flush();
//			ps.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * Load the state of the game from a file
	 * @param name - Name of the file to load from
	 */
//	public void load() {
//		try {
//			// Read from the scores file and init the JSON parser
//			BufferedReader br = new BufferedReader(new FileReader(DEFAULT_FILE));
//			JSONParser jp = new JSONParser();
//			JSONObject json = (JSONObject) jp.parse(br.readLine());
//			JSONArray names = (JSONArray) json.get("names");
//			JSONArray scores = (JSONArray) json.get("scores");
//			
//			for (int i = 0; i < names.size(); i++) {
//				HashMap<String, Integer> temp = new HashMap<String, Integer>();
//				temp.put((String)names.get(i), ((Long)scores.get(i)).intValue());
//				hScores.addAll(temp.entrySet());
//			}
//			
//			br.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (ParseException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public int getSelector() {
		return selector;
	}
	
	public void setRunning(boolean r) {
		running = r;
	}
	
	public void setInMenu(boolean is) {
		inMenu = is;
	}
	
	public boolean isInMenu() {
		return inMenu;
	}
	
	public void checkpoint() {
		//end = false;
		//ssave();
		//howGameOver = true;
	}
	
	public Area getCurArea() {
		return curArea;
	}
	
	public void setShowGameOver(boolean can) {
		showGameOver = can;
	}
	
	public boolean canShowGameOver() {
		return showGameOver;
	}
	
	public boolean getEnd() {
		return end;
	}

	public Player getPlayer() {
		return p;
	}
}
