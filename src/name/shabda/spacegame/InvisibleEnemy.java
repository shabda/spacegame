/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.io.Serializable;

/**
 * @author shabda
 */
public class InvisibleEnemy extends RogueShip implements Serializable{
	public InvisibleEnemy(double pos,double pos2,double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		health=50;
		right=false;
		up=false;
		value=100;
	}
	
		public void draw(Graphics g) {
	}
}
