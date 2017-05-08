package model.powerup;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

import model.Entity;

public abstract class Powerup extends Entity{
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	public static final int DEFAULT= -1;
	public static final ArrayList<Powerup> POWERUPS = new ArrayList<>();
	
	private int powerUpId;
	
	public Powerup(int pUI, int x, int y) {
		super(x, y);
		this.powerUpId = pUI;
		POWERUPS.add(this);
	}
	
	public int getPowerup() {
		return this.powerUpId;
	}
	
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), WIDTH, HEIGHT);
	}
}
