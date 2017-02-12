package model;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

public abstract class Bullet extends Entity {
	public static final ArrayList<Bullet> BULLETS = new ArrayList<>();
	public static final float SPEED = 0.1f;
	public static final long TIME = 10;
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 15;
	
	protected Thread move;
	
	protected Bullet(float x, float y) {
		super(x, y);
		
		BULLETS.add(this);
		this.start();
	}
	
	public abstract void start();
	public abstract void interrupt();
	
	public static void reset() {
		try {
			for(int i=0; i < Bullet.BULLETS.size(); i++) {
				Bullet.BULLETS.get(i).interrupt();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		Bullet.BULLETS.clear();
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), Bullet.WIDTH, Bullet.HEIGHT);
	}
}
