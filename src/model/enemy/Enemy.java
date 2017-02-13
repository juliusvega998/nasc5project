package model.enemy;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.BulletEnemy;
import model.Entity;
import model.Player;
import util.Config;

public class Enemy extends Entity {
	public static final ArrayList<Enemy> ENEMIES = new ArrayList<>();
	
	public static final int WIDTH = 25;
	public static final int HEIGHT = 25;
	public static final float SPEED = 1;
	
	protected int life;
	
	protected Random rand;
	
	protected Thread move;
	protected Thread fire;
	
	private Sound hurt;
	protected Sound shoot;
	
	public Enemy(float x, float y, Sound hurt, Sound shoot) {
		super(x, y);
		
		rand = new Random();
		this.hurt = hurt;
		this.shoot = shoot;
		this.life = 1;
		ENEMIES.add(this);
		start();
	}
	
	public Enemy(float x, float y, int life, Sound hurt, Sound shoot) {
		super(x, y);
		
		rand = new Random();
		this.hurt = hurt;
		this.shoot = shoot;
		this.life = life;
		ENEMIES.add(this);
		start();
	}
	
	public void start(){
		move = new Thread() {
			@Override
			public void run() {
				boolean moveLeft = rand.nextBoolean();
				try{
					while(!this.isInterrupted()) {
						if(moveLeft) {
							Enemy.this.setX(Enemy.this.getX() - SPEED);
						} else {
							Enemy.this.setX(Enemy.this.getX() + SPEED);
						}
						
						if(Enemy.this.getX() < 10) {
							moveLeft = false;
						} else if(Enemy.this.getX() > Config.WIDTH - 10) {
							moveLeft = true;
						} else if(Enemy.this.boundingRect().intersects(Player.getInstance().boundingRect())) {
							Player.getInstance().kill();
						}
						
						Thread.sleep(10);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};
		
		fire = new Thread() {
			@Override
			public void run() {
				try{
					while(!this.isInterrupted()) {
						new BulletEnemy(Enemy.this);
						shoot.play();
						Thread.sleep(rand.nextInt(200) + 900);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};
		
		move.start();
		fire.start();
	}
	
	public void interrupt() {
		move.interrupt();
		fire.interrupt();
	}
	
	public static void reset() {
		for(Enemy e : Enemy.ENEMIES) {
			e.interrupt();
		}
		Enemy.ENEMIES.clear();
	}
	
	public void playDead() {
		this.life--;
		this.hurt.play();
	}
	
	public int getLife() {
		return this.life;
	}
	
	public boolean isDead() {
		return this.life < 1;
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), Enemy.WIDTH, Enemy.HEIGHT);
	}
}
