/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

/**
 * @author shabda
 */
public class EnemyBullet extends Bullet implements Serializable{
	/**
	 * 
	 */
	public EnemyBullet(double xP,double yP) {
		xPos=xP;
		yPos=yP;
		xSpeed=UW.enemyBulletSpeed;
	}
	/**
	 * 
	 */
	public EnemyBullet(double xP,double yP,boolean r,boolean y) {
		super(xP,yP,r,y);
		}
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(Color.cyan);
		g2d.fill(new Ellipse2D.Double(xPos,yPos,size,size));
		}

}
