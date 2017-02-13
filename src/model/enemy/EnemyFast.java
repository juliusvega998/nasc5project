package model.enemy;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.BulletEnemy;
import model.Player;
import util.Config;

public class EnemyFast extends Enemy {

	public static final int WIDTH = 15;
	public static final int HEIGHT = 15;
	public static final float SPEED = 5;
	
	public EnemyFast(float x, float y, Sound hurt, Sound shoot) {
		super(x, y, hurt, shoot);
	}

	
	@Override
	public void start(){
		move = new Thread() {
			@Override
			public void run() {
				boolean moveLeft = rand.nextBoolean();
				try{
					while(!this.isInterrupted()) {
						if(moveLeft) {
							EnemyFast.this.setX(EnemyFast.this.getX() - SPEED);
						} else {
							EnemyFast.this.setX(EnemyFast.this.getX() + SPEED);
						}
						
						if(EnemyFast.this.getX() < 10) {
							moveLeft = false;
						} else if(EnemyFast.this.getX() > Config.WIDTH - 10) {
							moveLeft = true;
						} else if(EnemyFast.this.boundingRect().intersects(Player.getInstance().boundingRect())) {
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
						if(EnemyFast.this.getX() >= Player.getInstance().getX() 
								&&EnemyFast.this.getX() <= Player.getInstance().getX() + Player.WIDTH - SPEED) {
							new BulletEnemy(EnemyFast.this);
							shoot.play();
							Thread.sleep(100);
						}
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}; 
		
		move.start();
		fire.start();
	}
	
	@Override
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), EnemyFast.WIDTH, EnemyFast.HEIGHT);
	}
}
