package enemies.corruptedcatacombs.caveshrieker;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import control.Controller;
import project.Projectile;
import project.Rotate;
import statuseffects.AllyQuiet;

public class CaveShriekerStar extends Projectile{
	final int SIZE = 16;
	
	int size, dmg;
	BufferedImage img;

	public CaveShriekerStar(int x, int y, double ori) {
		super(x, y, (double)ori, CaveShrieker.STAR_RANGE, CaveShrieker.STAR_SPEED);
		this.dmg = CaveShrieker.STAR_DMG;
		double rotation = ori;
		
		this.size = (int) (SIZE * Rotate.getSizeConstant(rotation));		
		this.img = Rotate.rotateImage(Projectile.IMGS_CAVESHRIEKER_SHOTS.get(1), rotation);
		Controller.enemyShots.add(this);
	}

	@Override
	public boolean ranged() {
		if(this.range <= 0) {
			return true;
		}
		if(inWall()) {
			return true;
		}
		if(Controller.chr.collision(this)) {
			Controller.chr.damage("Cave Shrieker", this.dmg, true);
			new AllyQuiet(83);
			return true;
		}
		return false;
	}

	@Override
	public void move() {
		this.x += this.xVel;
		this.y += this.yVel;
		this.range--;	
	}

	@Override
	public void draw(Graphics g, int shiftX, int shiftY, ImageObserver i) {
		g.drawImage(this.img, (int)this.x-this.size/2 - shiftX + 400, (int)this.y-this.size/2 - shiftY + 400, size, size, i);	
	}
}
