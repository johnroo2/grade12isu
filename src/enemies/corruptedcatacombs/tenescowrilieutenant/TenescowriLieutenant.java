package enemies.corruptedcatacombs.tenescowrilieutenant;

import java.awt.image.BufferedImage;

import control.Controller;
import enemies.corruptedcatacombs.tenescowricrusader.TenescowriCrusaderShot;
import equipment.ability.Ability;
import equipment.armour.Armour;
import equipment.ring.Ring;
import equipment.weapon.Weapon;
import project.Enemy;
import project.LootRoll;
import project.Target;

public class TenescowriLieutenant extends Enemy{
	final static int HEALTH = 2000;
	final static int DEF = 15;
	final static int SIZE = 48;
	final static double SPEED = 2.15;
	final static int SHOT_RANGE = 120;
	final static double SHOT_SPEED = 4;
	final static int DMG = 135;
	final static int MIN_FIRE_RATE = 100;
	final static int MAX_FIRE_RATE = 125;
	
	final static int ANIMATE_INTERVAL = 20;
	final static int MIN_MOVE_INTERVAL = 15;
	final static int MAX_MOVE_INTERVAL = 50;

	final int ATTRACT_RADIUS = 500;
	
	private int fireTicks, animate, moveCycle, initX, initY;
	private int moveInterval = Controller.random.nextInt(MIN_MOVE_INTERVAL, MAX_MOVE_INTERVAL+1);
	
	public TenescowriLieutenant(int x, int y) {
		super(x, y, HEALTH, DEF, "Tenescowri Crusader");
		this.initX = x;
		this.initY = y;
		this.dir = 0;
		this.size = SIZE;
		this.fireTicks = Controller.random.nextInt(MIN_FIRE_RATE, MAX_FIRE_RATE+1);
		this.xp = 125;
		
		this.table = new LootRoll[][] 
				{
			{lr("w6", 2, 45), lr("w7", 5, 45)},
			{lr("ab3", 2, 45), lr("ab4", 5, 45)},
			{lr("ar6", 2, 45), lr("ar7", 3, 45), lr("ar8", 2, 45)},
			{lr("r2", 6, 45)},
			{lr(Weapon.Blade_Of_The_Mindless, 1, 8)},
			{lr(Ring.Lawless_Amulet, 1, 8)}
				};
	}

	@Override
	public boolean inWall() {
		return super.inWall(2);
	}

	@Override
	public void move() {
		if(this.active) {
			this.moveCycle++;
			if(this.moving) {
				this.animate++;
				if(this.animate == 2 * ANIMATE_INTERVAL) {
					animate = 0;
				}
				if(this.moveCycle >= this.moveInterval) {
					int shiftX = Controller.random.nextInt(-100, 101);
					int shiftY = Controller.random.nextInt(-100, 101);
					this.go(Controller.chr.getX() + shiftX, Controller.chr.getY() + shiftY, SPEED);
					if(this.xVel < 0) {
						this.dir = 0;
					}
					else {
						this.dir = 1;
					}
					this.moveCycle = 0;
					this.moveInterval = Controller.random.nextInt(MIN_MOVE_INTERVAL, MAX_MOVE_INTERVAL+1);
					
				}
				if(Math.sqrt((this.x-this.targetX) * (this.x-this.targetX) + (this.y-this.targetY) * (this.y-this.targetY)) <= SPEED * 8) {
					if(this.targetX == this.initX && this.targetY == this.initY) {
						this.active = false;
						return;
					}
					else {
						this.moving = false;
						int shiftX = Controller.random.nextInt(-100, 101);
						int shiftY = Controller.random.nextInt(-100, 101);
						this.go(Controller.chr.getX() + shiftX, Controller.chr.getY() + shiftY, SPEED);
						if(this.xVel < 0) {
							this.dir = 0;
						}
						else {
							this.dir = 1;
						}
						this.moveCycle = 0;
						this.moveInterval = Controller.random.nextInt(MIN_MOVE_INTERVAL, MAX_MOVE_INTERVAL+1);
					}
				}
				else {
					approach();
				}
				if(this.fireTicks > 0) {
					this.fireTicks -= 1;
				}
				else{
					for(int i = -2; i < 3; i++) {
						new TenescowriLieutenantShot((int)this.x, (int)this.y, Controller.chr.getX(), Controller.chr.getY(), 28 * i);
					}
					this.fireTicks = Controller.random.nextInt(MIN_FIRE_RATE, MAX_FIRE_RATE+1);
				}
			}
			else {
				if(this.moveCycle == 0) { //start movements
					this.go(Controller.chr.getX(), Controller.chr.getY(), SPEED);
					if(this.xVel < 0) {
						this.dir = 0;
					}
					else {
						this.dir = 1;
					}
				}
			}
		}
		else {
			if(Controller.chr.getArea() == this.area || (Controller.chr.getX() > this.x - this.ATTRACT_RADIUS && Controller.chr.getX() < this.x + this.ATTRACT_RADIUS
							&& Controller.chr.getY() > this.y - this.ATTRACT_RADIUS && Controller.chr.getY() < this.y + this.ATTRACT_RADIUS)) {
				this.active = true;
				this.go(Controller.chr.getX(), Controller.chr.getY(), SPEED);
				if(this.xVel < 0) {
					this.dir = 0;
				}
				else {
					this.dir = 1;
				}
			}
		}
	}

	@Override
	public BufferedImage getImage() {
		for(int i = 1; i < 3; i++) {
			if(this.animate <= ANIMATE_INTERVAL * i) {
				if(this.dir == 0) {
					return Target.IMGS_TENESCOWRILIEUTENANT.get(i - 1);
				}
				else {
					return Target.IMGS_TENESCOWRILIEUTENANT.get(i + 1);	
				}
			}
		}
		return Target.IMGS_TENESCOWRILIEUTENANT.get(0);
	}
}
