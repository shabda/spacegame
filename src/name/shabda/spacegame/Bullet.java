/*
 * Created on Sep 26, 2004
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
public class Bullet extends Weapon implements Drawable,Serializable{
	final static double damage=5;
	int size=5;
	/**
	 * @param pos
	 * @param pos2
	 * @param damage
	 * @param right
	 * @param up
	 */
	public Bullet(double pos, double pos2, double damage, boolean right, boolean up) {
		super(pos, pos2, damage, right, up);
	}
	public Bullet(double pos,double pos2, boolean right, boolean up) {
		super(pos, pos2,damage,right, up);
	}
	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size The size to set.
	 */
	public void setSize(int size) {
		this.size = size;
	}
	public String toString() {
		String s="a bullet";
		return s;
		}
	
	/**
	 * 
	 */
	public Bullet() {
		super();
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		Graphics2D g2d=(Graphics2D) g;
		g2d.setColor(Color.orange);
		g2d.fill(new Ellipse2D.Double(xPos,yPos,size,size));							

	}
	public Point2D getCentre() {
		Point2D c=new Point2D.Double(xPos+size/2,yPos+size/2);
		return c;
		}
	public static void main(String[] args) {
	}
}
