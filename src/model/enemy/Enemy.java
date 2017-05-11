package model.enemy;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.Entity;

public class Enemy extends Entity {
	public static final ArrayList<Enemy> ENEMIES = new ArrayList<>();
	
	public static final int WIDTH = 25;
	public static final int HEIGHT = 25;
	public static final float SPEED = 0.5f;
	
	protected int life;
	
	protected Random rand;
	
	protected EnemyMovementThread move;
	
	private Sound hurt;
	
	public Enemy(float x, float y, Sound hurt) {
		super(x, y);
		
		rand = new Random();
		this.hurt = hurt;
		this.life = 1;
		this.move = new EnemyMovementThread(this);
		
		ENEMIES.add(this);
		this.move.start();
	}
	
	public Enemy(float x, float y, int life, Sound hurt) {
		super(x, y);
		
		rand = new Random();
		this.hurt = hurt;
		this.life = life;
		this.move = new EnemyMovementThread(this);
		
		ENEMIES.add(this);
		this.move.start();
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
