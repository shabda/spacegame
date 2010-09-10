/*
 * Created on Dec 2, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * @author dicky
 */
public class MinerEnemy extends RogueShip implements Serializable {
	
	static final ImageIcon img=UW.createImageIcon("../images/minerEnemy.gif","");
	static final Image image=img.getImage();
	public MinerEnemy(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		value=50;
	}
	public List fireMultiple() {
		List l=new ArrayList();
		Weapon b=new Mine(getCentre().getX()-size/10,yPos);
		l.add(b);
		return l;
		}
	public void draw(Graphics g) {
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);	
	}
	public static void main(String[] args) {
	}
}
