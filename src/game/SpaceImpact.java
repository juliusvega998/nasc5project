package game;

import java.util.Random;

import javax.swing.JOptionPane;

import org.newdawn.slick.Animation;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import model.Bullet;
import model.BulletPlayer;
import model.Cloud;
import model.Enemy;
import model.Player;
import util.Config;

public class SpaceImpact extends BasicGame {
	public static final int NEUTRAL = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	
	public static final int CLOUD_NO = 3;
	
	private Player player;
	private Cloud[] clouds;
	
	private SpriteSheet playerSheet;
	private Image pBulletSheet;
	
	private Animation enemySheet;
	private Image eBulletSprite;
	
	private Image cloudSprite;
	private Image logo;
	
	private int level;
	private int mode;
	private int highscore;
	private int direction;
	private boolean stuck;
	private Random rand;
	
	public SpaceImpact(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.player = Player.getInstance();
		this.mode = 0;
		this.level = 1;
		this.highscore = Config.getScore();
		this.direction = NEUTRAL;
		this.stuck = false;
		this.rand = new Random();
		
		this.playerSheet = new SpriteSheet("resources/cupid2.png", 300, 300);
		this.enemySheet = new Animation(new SpriteSheet("resources/Enemy.png", 225, 225), 100);
		
		this.eBulletSprite = new Image("resources/enemy_bullet.png");
		this.pBulletSheet = new Image("resources/cuArrow.png");
		this.cloudSprite = new Image("resources/cloud.png");
		this.logo = new Image("resources/logo.png");
		
		clouds = new Cloud[CLOUD_NO];
		for(int i=0; i<CLOUD_NO; i++) {
			clouds[i] = new Cloud(rand.nextInt(Config.WIDTH/2)+Config.WIDTH/4, rand.nextInt((Config.HEIGHT-100)/2)+100);
		}
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		
		if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
			this.player.moveLeft();
			this.direction = LEFT;
		} else if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
			this.player.moveRight();
			this.direction = RIGHT;
		} else {
			this.direction = NEUTRAL;
		}
		
		if(input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
			this.player.moveDown();
		} else if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
			this.player.moveUp();
		}
		
		if(input.isKeyPressed(Input.KEY_SPACE)) {
			this.player.fire();
		}
		
		if(mode == 0) {
			if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)
					|| input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)
					|| input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)
					|| input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
				stuck = true;
			} else if(input.isKeyDown(Input.KEY_ENTER)) {
				this.mode = 1;
				this.player.reset();
			} else if(input.isKeyPressed(Input.KEY_ESCAPE)) {
				System.exit(0);
			} else {
				stuck = false;
			}
			
		} else {
			if(Enemy.ENEMIES.size() == 0) {
				for(int i=0; i<level; i++) {
					new Enemy(rand.nextInt(Config.WIDTH+Enemy.WIDTH)-Enemy.WIDTH, rand.nextInt(200) + 50);
				}
				level++;
			}
			
			if(player.isDead()) {
				if(highscore > this.player.getScore()) {
					JOptionPane.showMessageDialog(null, "Game over! Score: " + this.player.getScore());
				} else {
					String contactDetails = JOptionPane.showInputDialog(null, 
							"New Highscore!\nEnter your name and cellphone number:\n(Name/Cellphone Number)",
							JOptionPane.PLAIN_MESSAGE);
					
					this.highscore = this.player.getScore();
					Config.saveScore(highscore, contactDetails);
				}
				this.reset();
				input = null;
			}
		}
	}
	
	public void reset() {
		this.player.reset();
		this.level = 1;
		this.mode = 0;
		
		Enemy.reset();
		Bullet.reset();
	}
	
	@Override
	public void render(GameContainer container, Graphics graphics) throws SlickException {
		renderGame(graphics);
		if(mode == 0) {
			renderMenu(graphics);
			graphics.drawString("High Score: " + this.highscore, Config.WIDTH - 150, 10);
		} else {
			graphics.drawString("Score: " + player.getScore(), Config.WIDTH - 150, 10);
		}
	}
	
	public void renderMenu(Graphics graphics) {
		graphics.setBackground(new Color(224, 247, 250));
		logo.draw(Config.WIDTH/2 - logo.getWidth()/2, Config.HEIGHT/10);
		
		if(this.stuck) {
			graphics.setColor(Color.red);
			graphics.drawString("Keyboard seems pressed.\nIf not press the arrow keys and the WASD keys...", Config.WIDTH-500, Config.HEIGHT-75);
		} else {
			graphics.setColor(Color.black);
			graphics.drawString("Press enter to start the game...", Config.WIDTH-500, Config.HEIGHT-50);
		}
	}
	
	public void renderGame(Graphics graphics) {
		for(Cloud c : clouds) {
			cloudSprite.draw(c.getX(), c.getY());
		}
		
		switch(direction) {
			case NEUTRAL: playerSheet.getSubImage(0, 0).draw(player.getX(), player.getY(), Player.WIDTH, Player.HEIGHT); break;
			case LEFT: playerSheet.getSubImage(1, 1).draw(player.getX(), player.getY(), Player.WIDTH, Player.HEIGHT); break;
			case RIGHT: playerSheet.getSubImage(1, 0).draw(player.getX(), player.getY(), Player.WIDTH, Player.HEIGHT); break;
		}
		
		try {
			for(int i=0; i<Bullet.BULLETS.size(); i++) {
				if(Bullet.BULLETS.get(i) instanceof BulletPlayer) {
					pBulletSheet.draw(Bullet.BULLETS.get(i).getX(), Bullet.BULLETS.get(i).getY(), Bullet.WIDTH, Bullet.HEIGHT);
				} else {
					eBulletSprite.draw(Bullet.BULLETS.get(i).getX(), Bullet.BULLETS.get(i).getY(), Bullet.WIDTH, Bullet.HEIGHT);
				}
			}
			
			for(int i=0; i<Enemy.ENEMIES.size(); i++) {
				enemySheet.draw(Enemy.ENEMIES.get(i).getX(), Enemy.ENEMIES.get(i).getY(), Enemy.WIDTH, Enemy.HEIGHT);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
