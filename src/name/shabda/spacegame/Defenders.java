/*
 * Created on Jan 7, 2002
 */
package name.shabda.spacegame;

import java.io.Serializable;

/**
 * @author dicky
 */
public class Defenders extends HelperShip implements Serializable{
	boolean attached;//should this defender follow the hero?
	public Defenders(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		attached =true;
	}

	public Defenders(double pos, double pos2, double speed, double speed2,
			double health, int armour, int fuel, boolean right, boolean up) {
		super(pos, pos2, speed, speed2, health, armour, fuel, right, up);
		attached =true;
	}

	/* (non-Javadoc)
	 * @see name.shabda.spacegame.HelperShip#doStuff(name.shabda.spacegame.Ship)
	 */
	public void doStuff(Ship s) {
		if(attached) {
		xSpeed=s.getXSpeed();
		ySpeed=s.getYSpeed();
		right=s.right;
		up=s.up;
		}
		//else no need to do anything. The ship is not attached to hero.
	}
	public void move() {
		if(right){
			xPos+=xSpeed;
			}
		else if(!right)
			{xPos-=xSpeed;}
		if(up){
			yPos-=ySpeed;
			}
		else if(!up){
			yPos+=ySpeed;
		}

}
}
