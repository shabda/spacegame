/*
 * Created on Oct 1, 2004
 */
package name.shabda.spacegame;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author shabda
 */
public abstract class SpaceScape implements Artifact, Serializable{
	double xPos,yPos;//position
	double xSpeed,ySpeed;//speed
	int size;
	
	/**
	 * @return Returns the xSpeed.
	 */
	public double getXSpeed() {
		return xSpeed;
	}
	/**
	 * @param speed The xSpeed to set.
	 */
	public void setXSpeed(double speed) {
		xSpeed = speed;
	}
	/**
	 * @return Returns the ySpeed.
	 */
	public double getYSpeed() {
		return ySpeed;
	}
	/**
	 * @param speed The ySpeed to set.
	 */
	public void setYSpeed(double speed) {
		ySpeed = speed;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(int size) {
		this.size= size;
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
		double x=xPos-size/2;
		double y=yPos-size/2;
		Point2D c=new Point2D.Double(x,y);
		return c;
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
	public void setYPos(double pos) {
		yPos=pos;
	}
	public static void main(String[] args) {
	}
}
