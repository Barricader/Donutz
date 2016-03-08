package com.neumont.csc150;

import java.awt.Graphics;
import java.util.Random;

import com.neumont.csc150.entity.Enemy;
import com.neumont.csc150.entity.EnemyType;
import com.neumont.csc150.entity.Player;

public class Combat {
//	Variables----------------------------------------
	private Random rand;
	private Player p;
	private Enemy e;
//	Constructor--------------------------------------
	public Combat(Player p){
		rand = new Random();
		this.p = p;
	}
//	Renders enemy
	public void renderCombat(Graphics g, int x, int y){
		if(e == null){
			int i = rand.nextInt(4) + 1;
			if(i == 1){
				e = new Enemy(x, y, 7, 20, EnemyType.RANGED, 5, 3);
			}
			else if(i == 2){
				e = new Enemy(x, y, 8, 25, EnemyType.MELEE, 4, 2);
			}
			else if(i == 3){
				e = new Enemy(x, y, 10, 15, EnemyType.FAST, 2, 1);
			}
			else{
				e = new Enemy(x, y, 5, 30, EnemyType.HEAVY, 5, 1);
			}
		}
		e.load("Enemy.png");
		e.render(g);
	}
//	Does Something????
	public void initCombat(Player p, Enemy e){
		boolean choice = firstTurn(p, e);
		System.out.println("5");
		do{
			if(choice == true){
				//player attacks
				System.out.println("6.1");
				recieveDam(2, p.attack());
				System.out.println("6.2");
				checkIsDead(p, e);
				System.out.println("6.3");
			}
			else{
				//enemy attacks
				System.out.println("6.4");
				recieveDam(1, e.attack());
				System.out.println("6.5");
				checkIsDead(p, e);
				System.out.println("6.6");
			}
			if(choice == true){
				choice = false;
			}
			else if(choice == false){
				choice = true;
			}
		}while(p.isDead() != true || e.isDead() != true);
		System.out.println("7");
	}
//	Checks to see if enemy or player is dead--------------------
	public boolean checkIsDead(Player p, Enemy e){
		if(e.getCurHealth() <= 0){
			e.setDead(true);;
		return e.isDead();
		}
		else if(p.getCurHP() <= 0){
			p.setDead(true);
			return p.isDead();
		}
		else{
			return false;
		}
	}
//	What happens after battle-----------------------------------
	public boolean battleResult(){
		if(e.isDead() == true){
			return true;
		}
		else if(p.isDead() == true){
			return false;
		}
		else{
			return false;
		}
	}
//	Decide who goes first---------------------------------------
	public boolean firstTurn(Player p, Enemy e){
		if(p.getSpeed() >= e.getSpeed()){
			//Player goes first
			return true;
		}
		else{
			//enemy goes first
			return false;
		}
	}
//	Allows player and enemy to receive damage
	public int recieveDam(int id,int damage){
		if(id == 1){
			int pain = getP().getCurHP() - damage;
			getP().setCurHP(pain);
			return getP().getCurHP();
		}
		else if(id == 2){
			int pain2 = getE().getCurHealth() - damage;
			getE().setCurHealth(pain2);
			return getE().getCurHealth();
		}
		else{
			return 0;
		}
	}
//	Getters/Setters----------------------------------------------
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public Enemy getE() {
		return e;
	}
	public void setE(Enemy e) {
		this.e = e;
	}

}