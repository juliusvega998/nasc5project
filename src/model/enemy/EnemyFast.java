package model.enemy;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

public class EnemyFast extends Enemy {
	public static final float SPEED = 1;
	
	public EnemyFast(float x, float y, Sound hurt) {
		super(x, y, hurt);
	}
	
	@Override
	public Rectangle boundingRect() {
		return new Rectangle(this.getX(), this.getY(), EnemyFast.WIDTH, EnemyFast.HEIGHT);
	}
}
