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
	
	private int maxOffsetX, maxOffsetY, minOffsetX, minOffsetY;
	private int camX, camY;

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
		//areas.add(new Area("LostHaven.json"));
		//curArea = areas.get(0);
		curArea = new Area("LostHaven.json");
		
		maxOffsetX = (curArea.getWidth() * curArea.getTiles().get(0).getWidth()) - Display.WIDTH;
		maxOffsetY = (curArea.getHeight() * curArea.getTiles().get(0).getHeight()) - Display.HEIGHT;
		maxOffsetX *= 2;
		maxOffsetY *= 2;
		minOffsetX = 0;
		minOffsetY = 0;
		
//		ast = new Vector<Asteroid>();
//		bul = new Vector<Bullet>();
//		par = new Vector<Particle>();
		
		//load();
		
		showGameOver = false;
		inMenu = false;
		running = true;
		end = false;
		init = true;
		
		selector = 0;
		menuDelay = 0;
		
		p = new Player(Display.WIDTH / 4, Display.HEIGHT / 4, 3);
		
		camX = (int) (p.getX() - Display.WIDTH / 4);
		camY = (int) (p.getY() - Display.HEIGHT / 4);
		
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
			
			p.update();
			camX = (int) (p.getX() - Display.WIDTH / 4) * 2;
			camY = (int) (p.getY() - Display.HEIGHT / 4) * 2;
			
			if (camX > maxOffsetX) {
				camX = maxOffsetX;
			}
			else if (camX < minOffsetX) {
				camX = minOffsetX;
			}
			if (camY > maxOffsetY) {
				camY = maxOffsetY;
			}
			else if (camY < minOffsetY) {
				camY = minOffsetY;
			}
			
		}
		else {
			if (!showGameOver) {
				end = true;
			}
		}
	}
	
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
	 * Restart and init the game
	 */
	public void restart() {
		r = new Random();
		
		end = false;
		showGameOver = false;
		
//		ast = new Vector<Asteroid>();
//		bul = new Vector<Bullet>();
//		par = new Vector<Particle>();
		
		p = new Player(Display.WIDTH/2 - 10, Display.HEIGHT/2 - 10, 3);
		
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

	public int getCamX() {
		return camX;
	}

	public void setCamX(int camX) {
		this.camX = camX;
	}

	public int getCamY() {
		return camY;
	}

	public void setCamY(int camY) {
		this.camY = camY;
	}

	public int getMaxOffsetX() {
		return maxOffsetX;
	}

	public void setMaxOffsetX(int maxOffsetX) {
		this.maxOffsetX = maxOffsetX;
	}
	
	public int getMaxOffsetY() {
		return maxOffsetY;
	}

	public void setMaxOffsetY(int maxOffsetY) {
		this.maxOffsetY = maxOffsetY;
	}
}
