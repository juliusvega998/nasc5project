package model.volunteer;

import model.Forest;
import model.Player;
import model.enemy.Enemy;
import model.enemy.EnemyFast;
import model.volunteer.Volunteer;
import util.Config;

public class VolunteerMovementThread extends Thread {
private Volunteer e;
	
	public VolunteerMovementThread(Volunteer e) {
		this.e = e;
	}
	
	@Override
	public void run() {
		try{
			while(!this.isInterrupted()) {
				e.setY(e.getY() + Volunteer.SPEED);
				
				if(e.boundingRect().intersects(Forest.getInstance().boundingRect())) {
					Forest.getInstance().heal(10);
					
					e.kill();
					Volunteer.VOLUNTEERS.remove(e);
					break;
				} else if(e.getY() >= Config.HEIGHT) {
					e.kill();
					Volunteer.VOLUNTEERS.remove(e);
					break;
				}
				
				Thread.sleep(10);
			}
		} catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
