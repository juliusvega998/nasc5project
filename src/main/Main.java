package main;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import game.SpaceImpact;
import util.Config;

public class Main {
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new SpaceImpact("Space Impact"));
			app.setDisplayMode(Config.WIDTH, Config.HEIGHT, false);
			app.setTargetFrameRate(60); //cap FPS to 60
			app.setShowFPS(Config.DEBUG);
			app.setAlwaysRender(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
}
