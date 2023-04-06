package equipment.st;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.Iterator;

import control.Controller;
import equipment.weapon.Weapon;
import project.Area;
import project.Enemy;
import project.Equipment;
import project.Projectile;
import project.Rotate;

public class BladeOfTheMindless extends Weapon{
	int minDmg = 40;
	int maxDmg = 55;
	
	public BladeOfTheMindless() {
		super("Blade Of The Mindless", "");
		this.img = Equipment.ART_BLADEOFTHEMINDLESS;
		this.subtype = "sword";
		this.tierDisplay = "ST";
		this.fireRate = 120;
		
		this.desc = new String[] {"Used by Warrior, Paladin", "Damage: " + minDmg + "-" + maxDmg, "Rate of Fire: 120%", "Shots: 4", "+5 SPD"};
	}

	@Override
	public void active() {
		new BladeOfTheMindlessShot(Controller.chr.getX(), Controller.chr.getY(), Controller.chr.getX() + Controller.mouseCoords[0] - 400, 
				Controller.chr.getY() + Controller.mouseCoords[1] - 400, 6, 31, convert(minDmg, Controller.chr.getAttack()), 
				convert(maxDmg, Controller.chr.getAttack()), -27);
		new BladeOfTheMindlessShot(Controller.chr.getX(), Controller.chr.getY(), Controller.chr.getX() + Controller.mouseCoords[0] - 400, 
				Controller.chr.getY() + Controller.mouseCoords[1] - 400, 6, 31, convert(minDmg, Controller.chr.getAttack()), 
				convert(maxDmg, Controller.chr.getAttack()), -9);
		new BladeOfTheMindlessShot(Controller.chr.getX(), Controller.chr.getY(), Controller.chr.getX() + Controller.mouseCoords[0] - 400, 
				Controller.chr.getY() + Controller.mouseCoords[1] - 400, 6, 31, convert(minDmg, Controller.chr.getAttack()), 
				convert(maxDmg, Controller.chr.getAttack()), 9);
		new BladeOfTheMindlessShot(Controller.chr.getX(), Controller.chr.getY(), Controller.chr.getX() + Controller.mouseCoords[0] - 400, 
				Controller.chr.getY() + Controller.mouseCoords[1] - 400, 6, 31, convert(minDmg, Controller.chr.getAttack()), 
				convert(maxDmg, Controller.chr.getAttack()), 27);
		Controller.chr.setFireTicks(this.getFireCooldown(Controller.chr.getDexterity()));
	}
	
	@Override
	public void equip() {
		Controller.chr.setSpeed(Controller.chr.getSpeed() + 5);
	}
	
	@Override
	public void dequip() {
		Controller.chr.setSpeed(Controller.chr.getSpeed() - 5);
	}
}

class BladeOfTheMindlessShot extends Projectile{
	final int SIZE = 24;
	
	int minDmg, maxDmg, size;
	BufferedImage img;

	public BladeOfTheMindlessShot(int x, int y, int targetX, int targetY, double speed, int range, 
			int minDmg, int maxDmg, int alt) {
		super(x, y, targetX, targetY, range, speed);
		this.size = SIZE;
		this.minDmg = minDmg;
		this.maxDmg = maxDmg;
		
		double angle = getAngle(x, y, targetX, targetY) + alt;
		double rotation = angle;
		
		double tempX = this.xVel;
		double tempY = this.yVel;
		double sina = Math.sin(Math.toRadians(alt));
		double cosa = Math.cos(Math.toRadians(alt));
		
		this.xVel = tempX * cosa - tempY * sina;
		this.yVel = tempX * sina + tempY * cosa;
		
		this.size = (int) (SIZE * Rotate.getSizeConstant(rotation+45));
		
		this.img = Rotate.rotateImage(Projectile.BLADEOFTHEMINDLESS_SHOT, rotation+45);
		Controller.allyShots.add(this);
	}

	@Override
	public boolean ranged() {
		if(this.range <= 0) {
			return true;
		}
		if(inWall()) {
			return true;
		}
		for(Area a: Area.list) {
			Iterator<Enemy> enemyLi = a.getEnemies().iterator();
			while(enemyLi.hasNext()) {
				Enemy e = enemyLi.next();
				if(e.collision(this)) {
					if(Controller.random.nextInt(0, 100) < Controller.chr.getCrit()) {
						if(e.damage(Controller.random.nextInt((int)(this.minDmg * critDamage), (int)(this.maxDmg * critDamage) + 1), true)) {
							enemyLi.remove();
							Controller.removedEnemies.add(e);
							Controller.chr.kill(e.getName());
						}
					}
					else {
						if(e.damage(Controller.random.nextInt(this.minDmg, this.maxDmg+1), false)) {
							enemyLi.remove();
							Controller.removedEnemies.add(e);
							Controller.chr.kill(e.getName());
						}
					}
					return true;
				}
			}
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
