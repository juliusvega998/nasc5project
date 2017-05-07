package model.bullet;

import model.Player;
import model.enemy.Enemy;

public class BulletEnemy extends Bullet {
	private int type;
	
	public BulletEnemy(Enemy e) {
		super(e.getX() + (Enemy.WIDTH-Bullet.WIDTH)/2, e.getY());
		this.type = 0;
	}
	
	public BulletEnemy(Enemy e, int type) {
		super(e.getX() + (Enemy.WIDTH-Bullet.WIDTH)/2, e.getY());
		this.type = type;
	}

	@Override
	public void start() {
		move = new Thread() {
			@Override
			public void run() {
				try{
					float increment = SPEED;
					Player p = Player.getInstance();
					while(BulletEnemy.this.getY() > 0 || this.isInterrupted()) {
						if(BulletEnemy.this.boundingRect().intersects(p.boundingRect()) && !p.isDead()) {
							p.kill();
							break;
						}
						
						BulletEnemy.this.setY(BulletEnemy.this.getY() + increment);
						if(type == 1) {
							BulletEnemy.this.setX(BulletEnemy.this.getX() + increment);
						} else if(type == 2) {
							BulletEnemy.this.setX(BulletEnemy.this.getX() - increment);
						}
						increment += SPEED;
						Thread.sleep(Bullet.TIME);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				try {
					Bullet.BULLETS.remove(BulletEnemy.this);
				} catch(Exception e) {}
			}
		};
		
		move.start();
	}
	
	@Override
	public void interrupt() {
		move.interrupt();
	}
}
