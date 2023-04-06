package enemies.volatilevolcano.daimanarsonist;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import control.Controller;
import project.AoE;
import project.Projectile;
import statuseffects.AllySick;

public class DaimanArsonistBomb extends Projectile{
	final static double ARRIVAL_FRAMES = 110;
	final static Color BOMB_COLOUR = new Color(240, 200, 100);
	
	int initX, initY;
	int destX, destY;
	double angle;
	
	public DaimanArsonistBomb(int x, int y, int range, int ori) {
		super(x, y, (double)ori, (int)ARRIVAL_FRAMES, (double)range/ARRIVAL_FRAMES);
		Controller.enemyShots.add(this);
		this.initX = x;
		this.initY = y;
		this.angle = Math.toRadians(ori);
		this.destX = (int)(x + (double)range * Math.cos(Math.toRadians(ori)));
		this.destY = (int)(y + (double)range * Math.sin(Math.toRadians(ori)));
	}
	
	//PARA BETWEEN 0 and 1, PARABOLA PROGRESS
	public double[] bp_value(double dx, double dy, double para) {
		double dist = Math.sqrt(dx * dx + dy * dy);

		return new double[] {initX + dist * ((Math.cos(angle) * para)),
				initY + dist * ((Math.sin(angle) * para) - 2*(-para * para + para))};
	}
	
	@Override
	public void draw(Graphics g, int shiftX, int shiftY, ImageObserver i) {
		g.setColor(BOMB_COLOUR);
		for(int j = 0; j < 8; j++) {
			double v = -0.5 + 0.5 * Math.random() + 1.4 * (ARRIVAL_FRAMES-range)/ARRIVAL_FRAMES;
			if(v < 0.1) {
				continue;
			}
			if(v > 1) {
				if(Controller.random.nextInt(0, 3) == 0) {
					v = 1;
				}
				else {
					continue;
				}
			}
			int s = Controller.random.nextInt(5, 9);
			double[] bp = bp_value(destX-initX, destY-initY, v);
			g.fillRect((int)(bp[0]-shiftX+400), (int)(bp[1]-shiftY+400), s, s);
		}
	}
	
	@Override
	public void destroy() {
		new DaimanArsonistSlam((int)this.x, (int)this.y);
	}

	@Override
	public boolean ranged() {
		if(this.range <= 0) {
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
}

class DaimanArsonistSlam extends AoE{
	int dmg;
	boolean hit = false;
	
	public DaimanArsonistSlam(int x, int y) {
		super(x, y, DaimanArsonist.RADIUS_AOE, AOE_LIGHTORANGE, 25);
		this.dmg = DaimanArsonist.DMG_AOE;
		Controller.enemyAoE.add(this);
	}
	
	public boolean inRadius(int x, int y, int radius) {
		return (this.x-x)*(this.x-x) + (this.y-y)*(this.y-y) <= radius * radius;
	}

	@Override
	public boolean ranged() {
		if(this.range <= 0) {
			return true;
		}
		if(!hit && this.inRadius(Controller.chr.getX(), Controller.chr.getY(), (int)(DaimanArsonist.RADIUS_AOE * 1.1))) {
			hit = true;
			Controller.chr.damage("Daiman Arsonist Bomb", this.dmg);
			new AllySick(333); //2 seconds
		}
		return false;
	}
}
