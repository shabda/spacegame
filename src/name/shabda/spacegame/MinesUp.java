/*
 * Created on Dec 4, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;

/**
 * @author dicky
 * This powerUp will add 10 mines to the hero
 */
public class MinesUp extends PowerUp implements Serializable{

	/**
	 * @param xP
	 */
	static final Image image=UW.createImageIcon("../images/minepower.gif","bullet powerUp image").getImage();
	public MinesUp(int xP) {
		super(xP);
		}
	public void draw(Graphics g) {
		
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);
	}
	public void affect(HeroShip h) {
		h.mines+=10;
		}
	public void affect(Space s) {
		s.hero.mines+=10;
		}
}
