package model;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.bullet.BulletPlayer;
import model.powerup.Powerup;
import util.Config;

public class Player extends Entity {
	public static final float SPEED = 5;
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	private Sound fire;
	private Sound killed;
	
	private int score;
	private int powerup;
	
	private boolean fired;
	private boolean dead;
	
	private static Player instance;
	
	private Player(Sound fire, Sound killed) {
		super((Config.WIDTH-Player.WIDTH)/2, (Config.HEIGHT-Player.HEIGHT)*0.75f);
		this.score = 0;
		this.powerup = Powerup.DEFAULT;
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
		
		checkPowerUp();
	}
	
	public void moveDown(){
		float newPos = this.getY() + SPEED;
		if(newPos + HEIGHT < Config.HEIGHT) {
			this.setY(newPos);
		}
		
		checkPowerUp();
	}
	
	public void moveLeft(){
		float newPos = this.getX() - SPEED;
		if(newPos > 0) {
			this.setX(newPos);
		}
		
		checkPowerUp();
	}
	
	public void moveRight(){
		float newPos = this.getX() + SPEED;
		if(newPos + HEIGHT < Config.WIDTH) {
			this.setX(newPos);
		}
		
		checkPowerUp();
	}
	
	public void checkPowerUp() {
		Powerup temp = null;
		
		if(this.powerup == Powerup.DEFAULT) return;
		
		for(Powerup p : Powerup.POWERUPS) {
			if(this.boundingRect().intersects(p.boundingRect())) {
				this.powerup = p.getPowerup();
				System.out.println("Powerup: " + this.powerup);
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(10000); //10 seconds
						} catch(Exception e) {}
						Player.this.powerup = Powerup.DEFAULT;
						System.out.println("Powerup Expired.");
					}
				}.start();
				
				temp = p;
				break;
			}
		}
		
		if(temp != null) Powerup.POWERUPS.remove(temp);
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
						Thread.sleep(500);
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
		return new Rectangle(this.getX(), this.getY(), WIDTH, HEIGHT);
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
