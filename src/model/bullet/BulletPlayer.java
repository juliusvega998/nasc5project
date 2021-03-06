package model.bullet;

import model.Player;
import model.enemy.Enemy;
import model.volunteer.Volunteer;

public class BulletPlayer extends Bullet {
	private Player owner;
	
	public BulletPlayer(Player p) {
		super(p.getX() + (Player.WIDTH-Bullet.WIDTH)/2, p.getY());
		this.owner = p;
	}

	@Override
	public void start() {
		move = new Thread() {
			@Override
			public void run() {
				try{
					float increment = SPEED;
					while(BulletPlayer.this.getY() > 0) {
						if(hitsEnemy()) {
							owner.addScore(10);
							break;
						}
						
						if(hitsVolunteer()) {
							break;
						}
						
						BulletPlayer.this.setY(BulletPlayer.this.getY() - increment);
						increment += SPEED;
						Thread.sleep(Bullet.TIME);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				
				Bullet.BULLETS.remove(BulletPlayer.this);
			}
		};
		
		move.start();
	}
	
	@Override
	public void interrupt() {
		move.interrupt();
	}
	
	public boolean hitsEnemy() {
		try{
			for(int i=0; i<Enemy.ENEMIES.size(); i++) {
				if(this.boundingRect().intersects(Enemy.ENEMIES.get(i).boundingRect())) {
					Enemy.ENEMIES.get(i).playDead();
					if(Enemy.ENEMIES.get(i).isDead()) {
						Enemy.ENEMIES.get(i).interrupt();
						Enemy.ENEMIES.remove(Enemy.ENEMIES.get(i));
					}
					
					return true;
				}
			}
			
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean hitsVolunteer() {
		try{
			for(int i=0; i<Volunteer.VOLUNTEERS.size(); i++) {
				if(this.boundingRect().intersects(Volunteer.VOLUNTEERS.get(i).boundingRect())) {
					Volunteer.VOLUNTEERS.get(i).playDead();
					if(Volunteer.VOLUNTEERS.get(i).isDead()) {
						Volunteer.VOLUNTEERS.get(i).interrupt();
						Volunteer.VOLUNTEERS.remove(Volunteer.VOLUNTEERS.get(i));
					}
					
					return true;
				}
			}
			
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
