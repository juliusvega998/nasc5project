package game;

import java.awt.Font;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;

import model.Bullet;
import model.BulletPlayer;
import model.Cloud;
import model.Player;
import model.enemy.Enemy;
import model.enemy.EnemyFast;
import model.enemy.EnemyStrong;
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
	private Image start;
	
	private Sound cupidFire;
	private Sound enemyFire;
	private Sound cupidDead;
	private Sound enemyHurt;
	
	private Music music;
	
	private int level;
	private int mode;
	private int highscore;
	private int direction;
	
	private boolean show;
	
	private Random rand;
	
	private TrueTypeFont titles;
	private TrueTypeFont indicators;
	
	private TextField inputField;
	
	public SpaceImpact(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		this.mode = 0;
		this.level = 0;
		this.highscore = Config.getScore();
		this.direction = NEUTRAL;
		this.show = true;
		this.rand = new Random();
		
		this.playerSheet = new SpriteSheet("resources/img/cupid2.png", 300, 300);
		this.enemySheet = new Animation(new SpriteSheet("resources/img/Enemy.png", 225, 225), 100);
		
		this.cupidFire = new Sound("resources/sound/cupid_shoot.wav");
		this.enemyFire = new Sound("resources/sound/enemy_shoot.wav");
		this.cupidDead = new Sound("resources/sound/cupid_dead.wav");
		this.enemyHurt = new Sound("resources/sound/enemy_dead.wav");
		
		this.music = new Music("resources/sound/music.ogg");
		
		this.player = Player.getInstance(cupidFire, cupidDead);
		
		this.eBulletSprite = new Image("resources/img/enemy_bullet.png");
		this.pBulletSheet = new Image("resources/img/cuArrow.png");
		this.cloudSprite = new Image("resources/img/cloud.png");
		this.logo = new Image("resources/img/logo.png");
		this.start = new Image("resources/img/start.png");
		
		this.clouds = new Cloud[CLOUD_NO];
		for(int i=0; i<CLOUD_NO; i++) {
			this.clouds[i] = new Cloud(rand.nextInt(Config.WIDTH/2)+Config.WIDTH/4, rand.nextInt((Config.HEIGHT-100)/2)+100);
		}
		
		this.titles = new TrueTypeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14), true);
		this.indicators = new TrueTypeFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10), true);
		
		this.inputField = new TextField(container, titles, Config.WIDTH/2-125, Config.HEIGHT/2-10, 250, 20);
		
		new Thread() {
			@Override
			public void run() {
				while(true) {
					try {
						show = !show;
						Thread.sleep(500);
					} catch(Exception e) {}
				}
			}
		}.start();
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
		
		if(input.isKeyDown(Input.KEY_SPACE) && this.mode < 2) {
			this.player.fire();
		}
		
		if(mode == 0) {
			if(input.isKeyDown(Input.KEY_ENTER)) {
				this.mode = 1;
				this.music.loop();
				this.player.reset();
			} else if(input.isKeyDown(Input.KEY_ESCAPE)) {
				System.exit(0);
			}
		} else if(mode == 1) {
			if(Enemy.ENEMIES.size() == 0) {
				level++;
				for(int i=0; i<level; i++) {
					new Enemy(
							rand.nextInt(Config.WIDTH+Enemy.WIDTH)-Enemy.WIDTH, 
							rand.nextInt(200) + 50, 
							this.enemyHurt, 
							this.enemyFire
					);
				}
				
				for(int i=0; i<level/5; i++) {
					new EnemyStrong(
							rand.nextInt(Config.WIDTH+Enemy.WIDTH)-Enemy.WIDTH, 
							rand.nextInt(200) + 50, 
							this.enemyHurt, 
							this.enemyFire
					);
				}
				
				for(int i=0; i<level/3; i++) {
					new EnemyFast(
							rand.nextInt(Config.WIDTH+Enemy.WIDTH)-Enemy.WIDTH, 
							rand.nextInt(200) + 50, 
							this.enemyHurt, 
							this.enemyFire
					);
				}
			}
			
			if(player.isDead()) {
				this.music.pause();
				
				if(highscore >= this.player.getScore()) {
					new Thread() {
						@Override
						public void run() {
							try {
								SpaceImpact.this.mode = 3;
								Thread.sleep(3000);
							} catch(InterruptedException e) {}
							SpaceImpact.this.mode = 0;
							SpaceImpact.this.reset();
						}
					}.start();
				} else {
					this.mode = 2;
					inputField.setFocus(true);
				}
			}
		} else if (mode == 2) {
			if(input.isKeyDown(Input.KEY_ENTER)) {
				this.highscore = this.player.getScore();
				Config.saveScore(highscore, inputField.getText());
				inputField.setFocus(false);
				new Thread() {
					@Override
					public void run() {
						try {
							SpaceImpact.this.mode = 3;
							Thread.sleep(1000);
						} catch(InterruptedException e) {}
						SpaceImpact.this.mode = 0;
						SpaceImpact.this.reset();
					}
				}.start();
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
			titles.drawString(Config.WIDTH - 150, 10, "High Score: " + this.highscore, Color.black);
		} else if(mode == 2) {
			titles.drawString(
					Config.WIDTH/2-125, 
					Config.HEIGHT/2-45, 
					"New Highscore!", 
					Color.black
			);
			titles.drawString(
					Config.WIDTH/2-125, 
					Config.HEIGHT/2-30, 
					"Please enter your name and cellphone number separated by a space:",
					Color.black
			);
			inputField.render(container, graphics);
		} else {
			titles.drawString(Config.WIDTH - 150, 10, "Score: " + player.getScore(), Color.black);
			titles.drawString(Config.WIDTH * 3/4, 10, "Level: " + level, Color.black);
		}
		
		
	}
	
	public void renderMenu(Graphics graphics) {
		graphics.setBackground(new Color(224, 247, 250));
		logo.draw(Config.WIDTH/2 - logo.getWidth()/2, Config.HEIGHT/10);
		
		titles.drawString(100, Config.HEIGHT-70, "Created by: Alliance of Computer Science Students (ACSS)", Color.black);
		titles.drawString(100, Config.HEIGHT-50, "Music: http://www.bensound.com/", Color.black);
		if(show) {
			start.draw(Config.WIDTH/2-62, Config.HEIGHT/2-12);
		}
	}
	
	public void renderGame(Graphics graphics) {
		for(Cloud c : clouds) {
			cloudSprite.draw(c.getX(), c.getY());
		}
		
		if(player.isDead()) {
			titles.drawString(
					Config.WIDTH/2-50,
					Config.HEIGHT*0.60f,
					"GAME OVER!",
					Color.red
			);
		} else {
			switch(direction) {
				case NEUTRAL: playerSheet.getSubImage(0, 0).draw(player.getX(), player.getY(), Player.WIDTH, Player.HEIGHT); break;
				case LEFT: playerSheet.getSubImage(1, 1).draw(player.getX(), player.getY(), Player.WIDTH, Player.HEIGHT); break;
				case RIGHT: playerSheet.getSubImage(1, 0).draw(player.getX(), player.getY(), Player.WIDTH, Player.HEIGHT); break;
			}
		}
		
		if(!player.getFired() && !player.isDead()) {
			indicators.drawString(
					player.getX()+1, player.getY()+Player.HEIGHT+5, "READY!", Color.black
			);
		}
		
		try {
			for(int i=0; i<Bullet.BULLETS.size(); i++) {
				try {
					Bullet b = Bullet.BULLETS.get(i); 
					if(b instanceof BulletPlayer) {
						pBulletSheet.draw(b.getX(), b.getY(), Bullet.WIDTH, Bullet.HEIGHT);
					} else {
						eBulletSprite.draw(b.getX(), b.getY(), Bullet.WIDTH, Bullet.HEIGHT);
					}
				} catch(Exception e) {}
			}
			
			for(int i=0; i<Enemy.ENEMIES.size(); i++) {
				try {
					Enemy e = Enemy.ENEMIES.get(i);
					
					graphics.setColor(Color.green);
					if(e instanceof EnemyStrong) {
						if(e.getLife() == 1) {
							graphics.setColor(Color.red);
						}
						graphics.fillRect(e.getX(), e.getY() - 10, EnemyStrong.WIDTH * e.getLife()/5, 5);
						enemySheet.draw(e.getX(), e.getY(), EnemyStrong.WIDTH, EnemyStrong.HEIGHT);
					} else if (e instanceof EnemyFast) {
						graphics.fillRect(e.getX(), e.getY() - 10, EnemyFast.WIDTH, 5);
						enemySheet.draw(e.getX(), e.getY(), EnemyFast.WIDTH, EnemyFast.HEIGHT);
					} else {
						graphics.fillRect(e.getX(), e.getY() - 10, Enemy.WIDTH, 5);
						enemySheet.draw(e.getX(), e.getY(), Enemy.WIDTH, Enemy.HEIGHT);
					}
					
				} catch(Exception e) {}
			}
			graphics.setColor(Color.black);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
