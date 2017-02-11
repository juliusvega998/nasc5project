package model;

import org.newdawn.slick.geom.Rectangle;

import util.Config;

public class Player extends Entity {
	public static final float SPEED = 5;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	
	private static final Player instance = new Player();
	
	private int score;
	private boolean fired;
	private boolean dead;
	
	private Player() {
		super(100, Config.HEIGHT - 100);
		this.score = 0;
		fired = false;
		dead = false;
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
			fired = true;
			
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					fired = false;
				}
			}.start();
		}
	}
	
	public void kill() {
		this.dead = true;
	}
	
	public void addScore(int inc) {
		this.score += inc;
	}
	
	public int getScore() {
		return this.score;
	}
	
	public boolean isDead() {
		return this.dead;
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), Player.WIDTH, Player.HEIGHT);
	}
	
	public static Player getInstance() {
		return instance;
	}
}
