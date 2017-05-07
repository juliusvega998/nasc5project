package model.enemy;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.bullet.BulletEnemy;
import model.Entity;
import model.Forest;
import model.Player;
import util.Config;

public class Enemy extends Entity {
	public static final ArrayList<Enemy> ENEMIES = new ArrayList<>();
	
	public static final int WIDTH = 25;
	public static final int HEIGHT = 25;
	public static final float SPEED = 0.5f;
	
	protected int life;
	
	protected Random rand;
	
	protected Thread move;
	
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
				try{
					while(!this.isInterrupted()) {
						Enemy.this.setY(Enemy.this.getY() + SPEED);
						
						if(Enemy.this.boundingRect().intersects(Player.getInstance().boundingRect())) {
							Player.getInstance().kill();
							Enemy.this.kill();
						} else if(Enemy.this.boundingRect().intersects(Forest.getInstance().boundingRect())) {
							Forest.getInstance().damage(10);
							Enemy.this.kill();
							Enemy.ENEMIES.remove(Enemy.this);
							break;
						} else if(Enemy.this.getY() >= Config.HEIGHT) {
							Enemy.this.kill();
							Enemy.ENEMIES.remove(Enemy.this);
							break;
						}
						
						Thread.sleep(10);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		};
		
		move.start();
	}
	
	public void interrupt() {
		move.interrupt();
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
	
	public void kill() {
		this.life = 0;
	}
	
	public boolean isDead() {
		return this.life < 1;
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), Enemy.WIDTH, Enemy.HEIGHT);
	}
}
