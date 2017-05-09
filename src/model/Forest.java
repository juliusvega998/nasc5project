package model;

import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

import model.powerup.Powerup;
import model.powerup.ShieldPowerup;
import model.powerup.UnliAmmoPowerup;
import util.Config;

public class Forest extends Entity {
	public static final int WIDTH = Config.WIDTH;
	public static final int HEIGHT = WIDTH/8;
	public static final int MAX_LIFE = 100;
	
	private static final Forest instance = new Forest();
	
	private int life;
	
	private Forest() {
		super(0, Config.HEIGHT - HEIGHT);
		this.life = MAX_LIFE;
	}
	
	public void damage(int dmg) {
		this.life -= dmg;
	}
	
	public void heal(int heal) {
		this.life = (this.life + heal > MAX_LIFE)? MAX_LIFE: this.life + heal;
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
		
		if(r.nextInt(100) <= 25) return;
		
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
	
	public static Forest getInstance() {
		return instance;
	}
}
