package model;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.geom.Rectangle;

import util.Config;

public class Enemy extends Entity {
	public static final ArrayList<Enemy> ENEMIES = new ArrayList<>();
	
	public static final int WIDTH = 25;
	public static final int HEIGHT = 25;
	public static final float SPEED = 1;
	
	private Random rand;
	private Thread move;
	private Thread fire;
	
	public Enemy(float x, float y) {
		super(x, y);
		
		rand = new Random();
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
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), WIDTH, HEIGHT);
	}

}
