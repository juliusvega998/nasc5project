package model.enemy;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.BulletEnemy;
import model.Player;
import util.Config;

public class EnemyStrong extends Enemy {
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;

	public EnemyStrong(float x, float y, Sound hurt, Sound shoot) {
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
							EnemyStrong.this.setX(EnemyStrong.this.getX() - SPEED);
						} else {
							EnemyStrong.this.setX(EnemyStrong.this.getX() + SPEED);
						}
						
						if(EnemyStrong.this.getX() < 10) {
							moveLeft = false;
						} else if(EnemyStrong.this.getX() > Config.WIDTH - 10) {
							moveLeft = true;
						} else if(EnemyStrong.this.boundingRect().intersects(Player.getInstance().boundingRect())) {
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
						for(int i=0; i<5 && !this.isInterrupted(); i++) {
							new BulletEnemy(EnemyStrong.this);
							shoot.play();
							Thread.sleep(200);
						}
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
	
	@Override
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), EnemyStrong.WIDTH, EnemyStrong.HEIGHT);
	}
}
