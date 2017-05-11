package model.volunteer;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.Entity;
import model.enemy.Enemy;
import model.enemy.EnemyMovementThread;

public class Volunteer extends Entity {
public static final ArrayList<Volunteer> VOLUNTEERS = new ArrayList<>();
	
	public static final int WIDTH = 25;
	public static final int HEIGHT = 25;
	public static final float SPEED = 0.5f;
	
	protected int life;
	
	protected Random rand;
	
	protected VolunteerMovementThread move;
	
	private Sound hurt;
	
	public Volunteer(float x, float y, Sound hurt) {
		super(x, y);
		
		rand = new Random();
		this.hurt = hurt;
		this.life = 1;
		this.move = new VolunteerMovementThread(this);
		
		VOLUNTEERS.add(this);
		this.move.start();
	}
	
	public Volunteer(float x, float y, int life, Sound hurt, Sound shoot) {
		super(x, y);
		
		rand = new Random();
		this.hurt = hurt;
		this.life = life;
		this.move = new VolunteerMovementThread(this);
		
		VOLUNTEERS.add(this);
		this.move.start();
	}
	
	public void interrupt() {
		move.interrupt();
	}
	
	public static void reset() {
		for(Volunteer e : Volunteer.VOLUNTEERS) {
			e.interrupt();
		}
		Volunteer.VOLUNTEERS.clear();
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
