/*
 * Created on Jan 7, 2002
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * @author dicky
 */
public abstract class HelperShip extends Ship implements Drawable, Serializable{
	static final ImageIcon img=UW.createImageIcon("../images/helper.gif","");
	static final Image image=img.getImage();
	int size;
	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 */
	public HelperShip(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		size=15;
		
	}

	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 * @param health
	 * @param armour
	 * @param fuel
	 * @param right
	 * @param up
	 */
	public HelperShip(double pos, double pos2, double speed, double speed2,
			double health, int armour, int fuel, boolean right, boolean up) {
		super(pos, pos2, speed, speed2, health, armour, fuel, right, up);
	}

	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#getSize()
	 */
	public int getSize() {
		return size;
	}

	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#getCentre()
	 */
	public void draw(Graphics g) {
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);
	}
	public Point2D getCentre() {
		Point2D c=new Point2D.Double(xPos+size/2,yPos+size/2);
		return c;
		}
	public Weapon fire() {
		Bullet b=new EnemyBullet(xPos,yPos,right,up);
		b.setYSpeed(UW.enemyBulletSpeed);
		return (b);
		}
	public List fireMultiple() {
		//System.out.println("fired");
		List l=new ArrayList();
		//Bullet b=new Bullet(xPos,yPos,right,up);
		Weapon b=new Bullet(getCentre().getX()-size/10,yPos,right,up);
		l.add(b);
		return (l);
		}
	
	public abstract void doStuff(Ship s);

}
