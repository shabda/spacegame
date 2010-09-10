/*
 * Created on Sep 26, 2004
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
 * @author shabda
 * This is the basic enemyship fires which one bullet in the forward direction
 * and moves only in forward direction 
 */
public class RogueShip extends Ship implements Drawable,Serializable{
	public int size=15;
	static int bullets=100;
	static ImageIcon img=UW.createImageIcon("../images/rougeShip1.gif","");
	static final Image image=img.getImage();
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
	public RogueShip(double pos,double pos2,double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		health=50;
		right=false;
		up=false;
		value=20;
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		//Graphics2D g2d=(Graphics2D) g;
		//g2d.setColor(Color.RED);
		//g2d.fill(new Ellipse2D.Double(xPos,yPos,size,size));
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);
	}
	public Point2D getCentre() {
		Point2D c=new Point2D.Double(xPos+size/2,yPos+size/2);
		return c;
		}
	public Weapon fire() {
		bullets--;
		Bullet b=new EnemyBullet(xPos,yPos,right,up);
		b.setYSpeed(UW.enemyBulletSpeed);
		return (b);
		}
	public List fireMultiple() {
		List l=new ArrayList();
		bullets--;
		Bullet b=new EnemyBullet(xPos,yPos,right,up);
		b.setYSpeed(UW.enemyBulletSpeed);
		l.add(b);
		return (l);
		}
	public String toString() {
		String s;
		s="a rogue ship";
		return s;
	}
	public static void main(String[] args) {
	}
}
