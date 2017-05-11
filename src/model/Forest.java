package model;

import java.util.Random;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.enemy.Enemy;
import model.enemy.EnemyFast;
import model.powerup.Powerup;
import model.powerup.ShieldPowerup;
import model.powerup.UnliAmmoPowerup;
import util.Config;

public class Forest extends Entity {
	public static final int WIDTH = Config.WIDTH;
	public static final int HEIGHT = WIDTH/8;
	public static final int MAX_LIFE = 100;
	public static final int SPAWN_CHANCE = 30;
	
	private static Forest instance;
	
	private int life;
	
	private Sound cutSound, fireSound, healSound;
	
	private Forest(Sound cutSound, Sound fireSound, Sound healSound) {
		super(0, Config.HEIGHT - HEIGHT);
		this.life = MAX_LIFE;
		
		this.cutSound = cutSound;
		this.fireSound = fireSound;
		this.healSound = healSound;
	}
	
	public void damage(int dmg, Enemy e) {
		this.life -= dmg;
		
		try {
			if(e instanceof EnemyFast) {
				fireSound.play();
			} else {
				cutSound.play();
			}
		} catch(IllegalArgumentException ex) {}
	}
	
	public void heal(int heal) {
		if(!this.isAlive()) return;
		
		this.life = (this.life + heal > MAX_LIFE)? MAX_LIFE: this.life + heal;
		
		try {
			healSound.play();
		} catch(IllegalArgumentException ex) {}
	}
	
	public int getLife() {
		return this.life;
	}
	
	public boolean isAlive() {
		return this.life > 0;
	}
	
	public void reset() {
		this.life = MAX_LIFE;
	}

	public void spawnPowerup() {
		Random r = new Random();
		
		if(r.nextInt(100) > SPAWN_CHANCE) return;
		
		switch(r.nextInt(2)) {
		case ShieldPowerup.ID: 
			new ShieldPowerup(r.nextInt(Config.WIDTH-Powerup.WIDTH), (int) Forest.this.getY()-Powerup.HEIGHT); 
			break;
		case UnliAmmoPowerup.ID: 
			new UnliAmmoPowerup(r.nextInt(Config.WIDTH-Powerup.WIDTH), (int) Forest.this.getY()-Powerup.HEIGHT); 
			break;
		}
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), WIDTH, HEIGHT);
	}
	
	public static Forest getInstance(Sound cutSound, Sound fireSound, Sound healSound) {
		if(instance == null) {
			instance = new Forest(cutSound, fireSound, healSound);
		}
		
		return instance;
	}
	
	public static Forest getInstance() {
		if(instance == null) {
			throw new RuntimeException("Instance is null!");
		}
		
		return instance;
	}
}
