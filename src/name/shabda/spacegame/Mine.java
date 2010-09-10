/*
 * Created on Oct 6, 2004
 */
package name.shabda.spacegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * @author shabda
 */
public class Mine extends Weapon implements Drawable, Serializable {
	boolean isShowing=false;
	int twinkle=0;
	int size;

	/**
	 * 
	 */
	public Mine() {
		super();
		
	}
	public Mine(double xP,double yP) {
		//super();
		xPos=xP;
		yPos=yP;
		xSpeed=0;
		ySpeed=0;
		size=8;
		
	}

	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 * @param damage
	 * @param right
	 * @param up
	 */
	public Mine(double pos, double pos2, double speed, double speed2,
			double damage, boolean right, boolean up) {
		super(pos, pos2, speed, speed2, damage, right, up);
	}

	/**
	 * @param pos
	 * @param pos2
	 * @param damage
	 * @param right
	 * @param up
	 */
	public Mine(double pos, double pos2, double damage, boolean right,
			boolean up) {
		super(pos, pos2, damage, right, up);
	}

	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		if(isShowing&&twinkle<=4) {
			if(twinkle==2)
			isShowing=false;
			twinkle++;
			Graphics2D g2d=(Graphics2D) g;
			g2d.setColor(Color.red);
			g2d.fill(new Ellipse2D.Double(xPos,yPos,size,size));			
			}
		else { 
			if(twinkle==0)
			isShowing=true;
			twinkle--;
		}

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
	public Point2D getCentre() {
		Point2D c=new Point2D.Double(xPos+size/2,yPos+size/2);
		return c;
	}

	public static void main(String[] args) {
	}
}
