/*
 * Created on Sep 26, 2004*/
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author shabda
 */
public class PowerUp implements Artifact,Drawable,Moving, Serializable{
	/**
	 * 
	 */
	double xPos,yPos;
	double xSpeed,ySpeed;
	int size;
	static final Image image=UW.createImageIcon("../images/bulletpower.gif","bullet powerUp image").getImage();
	public PowerUp(int xP) {
		xPos=xP;
		yPos=0;
		xSpeed=0;
		ySpeed=UW.powerUpSpeed;
		size=30;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Moving#move()
	 */
	public void move() {
		yPos+=ySpeed;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#collision(name.shabda.spacegame.Artifact)
	 */
	public boolean collision(Artifact a) {
		return false;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#getCentre()
	 */
	public Point2D getCentre() {
		Point2D c=new Point2D.Double(xPos+size/2,yPos+size/2);
		return c;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#getSize()
	 */
	public int getSize() {
		return size;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#getXPos()
	 */
	public double getXPos() {
		return xPos;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#getYPos()
	 */
	public double getYPos() {
		return yPos;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#isAlive()
	 */
	public boolean isAlive() {
		return false;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#setAlive(boolean)
	 */
	public void setAlive(boolean a) {
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#setXPos(double)
	 */
	public void setXPos(double pos) {
		xPos=pos;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#setYPos(double)
	 */
	public void affect(Space s) {
		s.hero.bullets+=200;
		}
	public void setYPos(double pos) {
		yPos=pos;

	}
}
