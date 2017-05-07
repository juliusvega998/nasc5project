package model;

import org.newdawn.slick.geom.Rectangle;

import util.Config;

public class Forest extends Entity implements Runnable {
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
	
	public int getLife() {
		return this.life;
	}
	
	public boolean isAlive() {
		return this.life > 0;
	}
	
	public void reset() {
		this.life = MAX_LIFE;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), WIDTH, HEIGHT);
	}
	
	public static Forest getInstance() {
		return instance;
	}
}
