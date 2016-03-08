package com.neumont.csc150;

import java.util.Random;
import java.util.Vector;

import com.neumont.csc150.audio.AudioPlayer;
import com.neumont.csc150.entity.Chest;
import com.neumont.csc150.entity.Player;
import com.neumont.csc150.item.Item;

public class Donutz {
	public static final String DEFAULT_FILE = "save.json";
	public static final String[] TIPS = { "Don't die", "Attack enemies", "Fear the Donutz", "Acquire space dollars", "Explore", "Get better gear" };
	public static String CUR_TIP = "";
	public static boolean DEBUG = false;
	private static Donutz instance;
	private Vector<Area> areas;
	private Vector<Vector<Chest>> chests;
	private Area curArea;
	private Player p;
	private Item selected;
	private int lastSelected;
	private int curChestInv;
	
	private int maxOffsetX, maxOffsetY, minOffsetX, minOffsetY;
	private int camX, camY;
	
	private double loadPerc;

	boolean showGameOver;
	private boolean end;
	private boolean inMenu, inCombat = false, inTown, inForest1, inCave;
	
	private boolean running;
	private boolean invOpen;
	
	private int selector;
	private int menuDelay, combatDelay;
	private int combatCounter;
	
	private String map;
	
	private Random r;
	
	private Display d;
	
	private Combat c;

	private AudioPlayer ap, town = new AudioPlayer("Town.wav"), forest1 = new AudioPlayer("Forest1.wav"), forest2 = new AudioPlayer("Forest2.wav"),
			battle = new AudioPlayer("Battle.wav"), cave = new AudioPlayer("Cave.wav");
	
	public Donutz() {
		this(null);
	}
	
	public Donutz(Display d) {
		r = new Random();
		
		chests = new Vector<Vector<Chest>>();
		areas = new Vector<Area>();
		selected = null;
		lastSelected = -1;
		
		curArea = null;
		loadPerc = 0.0;
		
		showGameOver = false;
		inMenu = true;
		running = true;
		end = false;
		invOpen = false;
		
		selector = 0;
		menuDelay = 0;
		combatDelay = 0;
		curChestInv = -1;
		
		map = "";
		
		p = new Player(Display.WIDTH / 4, Display.HEIGHT / 4);
		
		camX = (int) (p.getX() - Display.WIDTH / 4);
		camY = (int) (p.getY() - Display.HEIGHT / 4);
		
		this.d = d;
		
		c = new Combat(p);
		
		if (instance == null) {
			instance = this;
		}
	}
	
	public void run() {
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
			
			d.render();
			frames++;
			
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				d.getFrame().setTitle("Donutz | " + frames + " fps");
				frames = 0;
			}
			
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * The game loop
	 **/
	public void update() {
		if (!inMenu) {
			if(inTown == false){
//				if (combatCounter >= 600) {
//					combatCounter = 0;
//					inCombat = true;
////					if(inCombat == true){
////						combatUpdate();
////					}
//				}
			}
			if(inCombat == false){
				if (!end && loadPerc >= 1.0 && !invOpen) {
					if (d.getInvX() < Display.WIDTH/2) {
						d.setInvX(d.getInvX() + 6);
					}
					else if (d.getInvX() != Display.WIDTH/2) {
						d.setInvX(Display.WIDTH/2);
					}
					playerUpdate();
					if (areas.size() > 0) {
						chestUpdate();
					}
				}
				else if (!end && loadPerc >= 1.0 && invOpen) {
					invUpdate();
				}
			}
		}
		else {
			menuUpdate();
		}
		if(inCombat == true){
			combatUpdate();
		}
	}
	
	/**
	 * Load an area
	 */
	public void load(String path) {
		Thread t = new Thread("load") {
			public void run() {
				boolean notYetLoaded = true;
				int curIndex = areas.indexOf(curArea);
				
				for (int i = 0; i < areas.size(); i++) {
					if (areas.get(i).getPath().equals(path)) {
						curArea = areas.get(i);
						notYetLoaded = false;
						loadPerc = 1.0;
					}
				}

				if (notYetLoaded) {
					loadPerc = 0.0;
					CUR_TIP = TIPS[r.nextInt(TIPS.length)];
					// Load the map
					curArea = new Area(path);

					if (areas.size() == 0) {
						maxOffsetX = (curArea.getWidth() * curArea.getTiles().get(0).get(0).getWidth())
								- Display.WIDTH / 2;
						maxOffsetY = (curArea.getHeight() * curArea.getTiles().get(0).get(0).getHeight())
								- Display.HEIGHT / 2;
						maxOffsetX *= 2;
						maxOffsetY *= 2;
						minOffsetX = 0;
						minOffsetY = 0;

						p.load("player.png");
						
						Vector<Chest> townChests = new Vector<Chest>();
						Vector<Chest> forestChests = new Vector<Chest>();
						Vector<Chest> caveChests = new Vector<Chest>();
						
						townChests.add(new Chest(280, maxOffsetY/2 - 240));
						forestChests.add(new Chest(400, maxOffsetY/4 + 80));
						caveChests.add(new Chest(0, 0));
						
						chests.add(townChests);
						chests.add(forestChests);
						chests.add(caveChests);
					}

					areas.add(curArea);
				}
				
				p.setDx(0);
				p.setDy(0);

				if (path.equals("LostHaven.json") && areas.size() == 1) {
					p.setX(maxOffsetX / 2 - Display.WIDTH / 4);
					p.setY(maxOffsetY / 2 - Display.HEIGHT);
				}
				else if (path.equals("LostHaven.json") && curIndex == 1) {
					p.setX(maxOffsetX - Display.WIDTH/2 - 80);
					p.setY(940);
					inTown = true;
					inForest1 = false;
				}
				else if (path.equals("Eternal_Forest.json") && curIndex == 0) {
					p.setX(80);
					p.setY(850);
					inTown = false;
					inForest1 = true;
				}
				else if (path.equals("Eternal_Forest.json") && curIndex == 2) {
					p.setX(maxOffsetX - Display.WIDTH/2 - 310);
					p.setY(maxOffsetY - Display.HEIGHT/2 - 970);
					inTown = false;
					inForest1 = true;
				}
				else if (path.equals("Doom_Cavern.json") && curIndex == 1) {
					p.setX(340);
					p.setY(280);
					inForest1 = false;
					inCave = true;
				}
				
				//Plays Song for current area
				if (path.equals("LostHaven.json")) {
					if (inTown = true) {
						forest1.stop();
						forest2.stop();
						town.play();
					}
				}
				else {
					if (inTown == false) {
						town.stop();
					}
				}
				
				if (path.equals("Eternal_Forest.json")) {
					if (inForest1 = true) {
						map = "Background1.png";
						forest2.stop();
						forest1.play();
						cave.stop();
					}
				}
				else {
					if (inForest1 == false) {
						forest1.stop();
					}
				}
				
				if (path.equals("Doom_Cavern.json")) {
					if (inCave = true) {
						map = "Background2.png";
						forest1.stop();
						forest2.stop();
						cave.play();
					}
				}
				else {
					if (inCave == false) {
						cave.stop();
					}
				}
				try {
					this.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		t.start();
	}
	
	private void chestUpdate() {
		if (curChestInv != -1) {
			if (d.getChestInvX() < 0) {
				d.setChestInvX(d.getChestInvX() + 6);
			}
			else if (d.getChestInvX() != 0) {
				d.setChestInvX(0);
			}
		}
		else {
			if (d.getChestInvX() > -150) {
				d.setChestInvX(d.getChestInvX() - 6);
			}
			else if (d.getChestInvX() != -150) {
				d.setChestInvX(-150);
			}
		}
		
		for (int i = 0; i < chests.get(areas.indexOf(curArea)).size(); i++) {
			boolean test = false;
			chests.get(areas.indexOf(curArea)).get(i).update(test);
			if (test) {
				System.out.println("HMMMMMMMMM");
			}
		}
	}
	
	private void invUpdate() {
		if (d.getInvX() > Display.WIDTH/2 - 198) {
			d.setInvX(d.getInvX() - 6);
		}
		else if (d.getInvX() != Display.WIDTH/2 - 198) {
			d.setInvX(Display.WIDTH/2 - 198);
		}
	}
	
	/**
	 * Update the player
	 */
	private void playerUpdate() {
		if (!p.isDead()) {
			p.setSprinting(d.getListener().shift);
			
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
		if(inTown == false){
			combatCounter += 1;
		}
	}
	
	/**
	 * Update the menu
	 */
	private void menuUpdate() {
		if ((d.getListener().s || d.getListener().down) && menuDelay <= 0) {
			selector++;
			menuDelay = 20;
		}
		else if ((d.getListener().w || d.getListener().up) && menuDelay <= 0) {
			selector--;
			menuDelay = 20;
		}
		
		if (d.getListener().enter) {
			chooseSelected();
		}
		if (menuDelay > 0) {
			menuDelay--;
		}
		if(selector >= 3){
			selector = 0;
		}
		else if(selector <= -1){
			selector = 2;
		}
	}

	public void chooseSelected() {
		// New game
		if (selector == 0) {
			inMenu = false;
			load("LostHaven.json");
		}
		// Load Game
		else if(selector == 1){
			
		}
		//	Exit
		else if(selector == 2){
			running = false;
		}
	}
	
	/**
	 * Update the menu
	 */
	private void combatUpdate() {
		battleSong(inCombat);
		if(inCombat == true){
			if ((d.getListener().s || d.getListener().down) && combatDelay <= 0) {
				selector++;
				combatDelay = 20;
			}
			else if ((d.getListener().w || d.getListener().up) && combatDelay <= 0) {
				selector--;
				combatDelay = 20;
			}
			
			if (d.getListener().enter) {
				chooseAction();
			}
			if (combatDelay > 0) {
				combatDelay--;
			}
			if(selector >= 3){
				selector = 0;
			}
			else if(selector <= -1){
				selector = 2;
			}
		}
		if(inCombat == false){
			battleSong(inCombat);
			System.out.println("3");
			battle.stop();
			System.out.println("2");
			c.setE(null);
			System.out.println("1");
		}
	}

	public void chooseAction() {
		// Attack
		if (selector == 0) {
			c.recieveDam(2, c.getP().attack());
			c.recieveDam(1, c.getE().attack());
			c.checkIsDead(c.getP(), c.getE());
			if(c.getP().isDead() == true){
				showGameOver = true;
			}
			else if(c.getE().isDead() == true){
				inCombat = false;
			}
		}
		// Item
		else if(selector == 1){
			
		}
		//	Run
		else if(selector == 2){
			inCombat = false;
		}
	}
	
	/**
	 * Sets ap to String s (in other words, to a song)
	 * */
	public void playSong(String s){
		setAp(new AudioPlayer(s));
	}
	/**
	 * 
	 * */
	public void battleSong(boolean inCombat){
		if(inCombat == true){
			forest1.stop();
			forest2.stop();
			cave.stop();
			battle.play();
		}
		else if(inCombat == false){
			if(inForest1 == true){
				forest1.play();
			}
			else if(inCave == true){
				cave.play();
			}
		}
	}
	
	public void requestTP(String location) {
		if (loadPerc == 1.0) {
			loadPerc = 0.0;
			load(location + ".json");
		}
	}
	
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
	
	public boolean isInForest1() {
		return inForest1;
	}

	public void setInForest1(boolean inForest1) {
		this.inForest1 = inForest1;
	}
	
	public void checkpoint() {
		//end = false;
		//save();
		//howGameOver = true;
	}
	
	public Vector<Area> getAreas() {
		return areas;
	}
	
	public Vector<Vector<Chest>> getChests() {
		return chests;
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
	
	public void setLoadPerc(double perc) {
		loadPerc = perc;
	}
	
	public double getLoadPerc() {
		return loadPerc;
	}
	
	public void setInvOpen(boolean open) {
		invOpen = open;
	}
	
	public boolean getInvOpen() {
		return invOpen;
	}
	
	public static Donutz getInstance() {
		return instance;
	}

	public AudioPlayer getAp() {
		return ap;
	}

	public void setAp(AudioPlayer ap) {
		this.ap = ap;
	}
	
	public Display getDisplay() {
		return d;
	}
	
	public Item getSelected() {
		return selected;
	}
	
	public void setSelected(Item item) {
		selected = item;
	}

	public int getLastSelected() {
		return lastSelected;
	}

	public void setLastSelected(int lastSelected) {
		this.lastSelected = lastSelected;
	}

	public AudioPlayer getForest2() {
		return forest2;
	}

	public void setForest2(AudioPlayer forest2) {
		this.forest2 = forest2;
	}
	
	public AudioPlayer getForest1() {
		return forest1;
	}

	public void setForest1(AudioPlayer forest1) {
		this.forest1 = forest1;
	}
	
	public void setCurChestInv(int chestIndex) {
		curChestInv = chestIndex;
	}

	public int getCurChestInv() {
		return curChestInv;
	}

	public boolean isInCombat() {
		return inCombat;
	}

	public void setInCombat(boolean inCombat) {
		this.inCombat = inCombat;
	}
	
	public AudioPlayer getCave() {
		return cave;
	}

	public void setCave(AudioPlayer cave) {
		this.cave = cave;
	}

	public Combat getC() {
		return c;
	}

	public void setC(Combat c) {
		this.c = c;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}
	public AudioPlayer getBattle() {
		return battle;
	}

}
