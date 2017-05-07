package model.enemy;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.Forest;
import model.Player;
import util.Config;

public class EnemyStrong extends Enemy {
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	public static final float SPEED = 0.25f;

	public EnemyStrong(float x, float y, Sound hurt, Sound shoot) {
		super(x, y, 5, hurt, shoot);
	}
	
	@Override
	public void start(){
		move = new Thread() {
			@Override
			public void run() {
				try{
					while(!this.isInterrupted()) {
						EnemyStrong.this.setY(EnemyStrong.this.getY() + SPEED);
						
						if(EnemyStrong.this.boundingRect().intersects(Player.getInstance().boundingRect())) {
							Player.getInstance().kill();
							EnemyStrong.this.kill();
						} else if(EnemyStrong.this.boundingRect().intersects(Forest.getInstance().boundingRect())) {
							Forest.getInstance().damage(20);
							EnemyStrong.this.kill();
							Enemy.ENEMIES.remove(EnemyStrong.this);
							break;
						} else if(EnemyStrong.this.getY() >= Config.HEIGHT) {
							EnemyStrong.this.kill();
							Enemy.ENEMIES.remove(EnemyStrong.this);
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
	
	@Override
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), EnemyStrong.WIDTH, EnemyStrong.HEIGHT);
	}
}
