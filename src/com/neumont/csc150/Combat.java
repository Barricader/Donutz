package com.neumont.csc150;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import com.neumont.csc150.entity.Enemy;
import com.neumont.csc150.entity.EnemyType;
import com.neumont.csc150.entity.Player;

public class Combat {
//	Variables
	private Random rand;
	private Player p;
	private Enemy e;
	
	public Combat(Player p,Enemy e){
		rand = new Random();
		this.p = p;
		this.e = e;
	}
	
	public void renderCombat(Graphics g){
		g.setColor(Color.BLACK);
		int i = rand.nextInt(4) + 1;
		if(i == 1){
			e = new Enemy(0, 0, 7, 20, EnemyType.RANGED, 5, 3);
		}
		else if(i == 2){
			e = new Enemy(0, 0, 8, 25, EnemyType.MELEE, 6, 5);
		}
		else if(i == 3){
			e = new Enemy(0, 0, 10, 15, EnemyType.FAST, 3, 1);
		}
		else{
			e = new Enemy(0, 0, 5, 30, EnemyType.HEAVY, 8, 6);
		}
		e.load("Enemy Sprites.png");
		e.render(g);
	}
	
	public void initCombat(Player p, Enemy e){
		boolean choice = firstTurn(p, e);
	
		do{
			if(choice == true){
			//player attacks
				//recieveDam(2, p.a);
				recieveDam(2, p.attack());
				checkIsDead(p, e);
			}
			else{
				//enemy attacks
				//recieveDam(1, e.a);
				recieveDam(1, e.attack());
				checkIsDead(p, e);
			}
		}while(p.isDead() == false && e.isDead() == false);
	}
	
	public boolean checkIsDead(Player p, Enemy e){
		boolean eDead = e.isDead();
		boolean pDead = p.isDead();
		if(e.getCurHealth() == 0){
		return eDead;
		}
		else if(p.getCurHP() == 0){
			return pDead;
		}
		return false;
	}
	
	public boolean firstTurn(Player p, Enemy e){
		if(p.getSpeed() >= e.getSpeed()){
			//Player goes first
			return true;
		}
		else{
			return false;
		}
	}
	
	public int recieveDam(int id,int damage){
		switch(id){
		case 1:
		int change = p.getCurHP() - damage;
		p.setCurHP(change);
		return p.getCurHP();
		case 2:
			int change1 = e.getCurHealth() - damage;
			e.setCurHealth(change1);
			return e.getCurHealth();
		}
		return 0;
	}

}