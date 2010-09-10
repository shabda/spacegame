/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.awt.geom.Point2D;

/**
 * @author shabda
 * This interface corresponds to a artifact on the screen
 * 
 */
public interface Artifact {
	int size=5;
	public int getSize();
	//public void setSize(int s);
	/**
	 * @return Returns the xPos.
	 */
	public abstract double getXPos();

	/**
	 * @param pos The xPos to set.
	 */
	public abstract void setXPos(double pos);

	/**
	 * @return Returns the yPos.
	 */
	public abstract double getYPos();

	/**
	 * @param pos The yPos to set.
	 */
	public Point2D getCentre();
	public abstract void setYPos(double pos);
	public boolean isAlive();
	public void setAlive(boolean a);
	public boolean collision(Artifact a);
}