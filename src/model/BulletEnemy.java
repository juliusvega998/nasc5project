package model;

import org.newdawn.slick.geom.Rectangle;

public class BulletEnemy extends Bullet {
	public BulletEnemy(Enemy e) {
		super(e.getX() + (Enemy.WIDTH-Bullet.WIDTH)/2, e.getY());
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
						if(BulletEnemy.this.boundingRect().intersects(p.boundingRect())) {
							p.kill();
							break;
						}
						
						BulletEnemy.this.setY(BulletEnemy.this.getY() + increment);
						increment += SPEED;
						Thread.sleep(Bullet.TIME);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				BULLETS.remove(BulletEnemy.this);
			}
		};
		
		move.start();
	}
	
	@Override
	public void interrupt() {
		move.interrupt();
	}
}
