package model;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import util.Config;

public class Player extends Entity {
	public static final float SPEED = 5;
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	private Sound fire;
	private Sound killed;
	
	private int score;
	private boolean fired;
	private boolean dead;
	
	private static Player instance;
	
	private Player(Sound fire, Sound killed) {
		super((Config.WIDTH-Player.WIDTH)/2, (Config.HEIGHT-Player.HEIGHT)*0.75f);
		this.score = 0;
		this.fired = false;
		this.dead = false;
		
		this.fire = fire;
		this.killed = killed;
	}
	
	public void reset() {
		this.score = 0;
		fired = false;
		dead = false;
	}
	
	public void moveUp(){
		float newPos = this.getY() - SPEED;
		if(newPos > 0) {
			this.setY(newPos);
		}
	}
	
	public void moveDown(){
		float newPos = this.getY() + SPEED;
		if(newPos + HEIGHT < Config.HEIGHT) {
			this.setY(newPos);
		}
	}
	
	public void moveLeft(){
		float newPos = this.getX() - SPEED;
		if(newPos > 0) {
			this.setX(newPos);
		}
	}
	
	public void moveRight(){
		float newPos = this.getX() + SPEED;
		if(newPos + HEIGHT < Config.WIDTH) {
			this.setX(newPos);
		}
	}
	
	public void fire() {
		if(!fired) {
			new BulletPlayer(this);
			fire.play();
			fired = true;
			
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					fired = false;
				}
			}.start();
		}
	}
	
	public void kill() {
		killed.play();
		this.dead = true;
	}
	
	public void addScore(int inc) {
		this.score += inc;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public boolean getFired() {
		return this.fired;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), Player.WIDTH, Player.HEIGHT);
	}
	
	public static Player getInstance(Sound fire, Sound killed) {
		if(instance == null) {
			instance = new Player(fire, killed);
		}
		
		return instance;
	}
	
	public static Player getInstance() {
		if(instance == null) {
			throw new RuntimeException("Instance is null!");
		}
		
		return instance;
	}
}
