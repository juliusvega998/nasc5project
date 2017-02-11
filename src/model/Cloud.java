package model;

import java.util.Random;

import util.Config;

public class Cloud extends Entity {
	public Cloud(int x, int y) {
		super(x, y);
		
		start();
	}
	
	private void start() {
		new Thread() {
			@Override
			public void run() {
				try{
					Random rand = new Random();
					while(true) {
						Cloud.this.setX((Cloud.this.getX()-1>0)? Cloud.this.getX()-1: Config.WIDTH);
						Thread.sleep(rand.nextInt(900)+200);
					}
				} catch(InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}.start();
	}

}
