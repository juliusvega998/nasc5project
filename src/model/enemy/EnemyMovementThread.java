package model.enemy;

import model.Forest;
import model.Player;
import model.powerup.ShieldPowerup;
import util.Config;

public class EnemyMovementThread extends Thread {
	private Enemy e;
	
	public EnemyMovementThread(Enemy e) {
		this.e = e;
	}
	
	@Override
	public void run() {
		try{
			while(!this.isInterrupted()) {
				if(e instanceof EnemyFast)
					e.setY(e.getY() + EnemyFast.SPEED);
				else
					e.setY(e.getY() + Enemy.SPEED);
				
				if(e.boundingRect().intersects(Forest.getInstance().boundingRect())) {
					if(Player.getInstance().getPowerup() != ShieldPowerup.ID) {
						if(e instanceof EnemyFast)
							Forest.getInstance().damage(15, e);
						else
							Forest.getInstance().damage(10, e);
					}
					
					e.kill();
					Enemy.ENEMIES.remove(e);
					break;
				} else if(e.getY() >= Config.HEIGHT) {
					e.kill();
					Enemy.ENEMIES.remove(e);
					break;
				}
				
				Thread.sleep(10);
			}
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
