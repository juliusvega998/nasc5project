package model.enemy;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.bullet.BulletEnemy;
import model.Forest;
import model.Player;
import util.Config;

public class EnemyFast extends Enemy {
	public static final float SPEED = 1;
	
	public EnemyFast(float x, float y, Sound hurt, Sound shoot) {
		super(x, y, hurt, shoot);
	}

	
	@Override
	public void start(){
		move = new Thread() {
			@Override
			public void run() {
				try{
					while(!this.isInterrupted()) {
						EnemyFast.this.setY(EnemyFast.this.getY() + SPEED);
						
						if(EnemyFast.this.boundingRect().intersects(Forest.getInstance().boundingRect())) {
							Forest.getInstance().damage(15);
							EnemyFast.this.kill();
							Enemy.ENEMIES.remove(EnemyFast.this);
							break;
						} else if(EnemyFast.this.getY() >= Config.HEIGHT) {
							EnemyFast.this.kill();
							Enemy.ENEMIES.remove(EnemyFast.this);
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
		return new Rectangle(this.getX(), this.getY(), EnemyFast.WIDTH, EnemyFast.HEIGHT);
	}
}
