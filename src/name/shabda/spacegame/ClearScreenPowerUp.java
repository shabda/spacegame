/*
 * Created on Dec 5, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

/**
 * @author dicky
 */
public class ClearScreenPowerUp extends PowerUp implements Serializable{
	static final Image image=UW.createImageIcon("../images/clearpower.gif","bullet powerUp image").getImage();
	/**
	 * 
	 */
	public ClearScreenPowerUp(int i) {
		super(i);
	}
	public void draw(Graphics g) {
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);
	}
	public void affect(Space s) {
		s.enemyWeapons.clear();
		s.rogues.clear();
		s.bossShip.clear();
		}

}
