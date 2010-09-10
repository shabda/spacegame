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
public class Dots implements Drawable,Moving,Artifact,Serializable{
	public boolean collision(Artifact a) {
		return false;
	}
	public Point2D getCentre() {
		return new Point2D.Double(xPos,yPos);
	}
	public int getSize() {
		return size;
	}
	public double getXPos() {
		return xPos;
	}
	public double getYPos() {
		return yPos;
	}
	public boolean isAlive() {
		return false;
	}
	public void setAlive(boolean a) {
	}
	public void setXPos(double pos) {
		xPos=pos;

	}
	public void setYPos(double pos) {
		yPos=pos;

	}
	double xPos;
	double yPos;
	double speed;
	int size;
	/**
	 * 
	 */
	public Dots(double xP,double yP,double sp) {
		xPos=xP;
		yPos=yP;
		speed=sp;
		size=2;
	}
	public Dots(double xP,double yP) {
		xPos=xP;
		yPos=yP;
		speed=UW.passiveSpeed;
		size=2;
	}
	public void move() {
		yPos+=speed;
		}
	public void draw(Graphics g) {
		Graphics2D g2d=(Graphics2D) g;
		g2d.setColor(Color.white);
		g2d.fill(new Ellipse2D.Double(xPos,yPos,size,size));							
		}
}
