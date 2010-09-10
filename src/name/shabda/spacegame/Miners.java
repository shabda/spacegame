/*
 * Created on Jan 7, 2002
 */
package name.shabda.spacegame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dicky
 */
public class Miners extends HelperShip implements Serializable {
	int speed=3;
	public Miners(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
	}
	public Miners(double pos, double pos2, double speed, double speed2,
			double health, int armour, int fuel, boolean right, boolean up) {
		super(pos, pos2, speed, speed2, health, armour, fuel, right, up);
	}
	public List fireMultiple() {
		List l=new ArrayList();
		Weapon b=new Mine(getCentre().getX()-size/10,yPos);
		l.add(b);
		return l;
		}
	public void move() {
		if(xPos>UW.xSize-4||xPos<4) {
			speed=-speed;
			}
		if(right){
			xPos+=speed;
			}
		else if(!right)
			{xPos-=speed;}
		int rand=(int) (Math.random()*3);
		if(rand<2) {
			yPos--;
		}
		else yPos++;
		}
	
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.HelperShip#doStuff(name.shabda.spacegame.Ship)
	 */
	public void doStuff(Ship s) {
		// Miners dont do anything fancy

	}

}
