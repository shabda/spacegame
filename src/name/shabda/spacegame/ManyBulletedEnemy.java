/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * @author shabda
 * This is a enemy which fires multiple bullets in forward direction
 */
public class ManyBulletedEnemy extends RogueShip implements Serializable{
	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 */
	public ManyBulletedEnemy(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		setSize(30);
	}
	public List fireMultiple() {
		List l=new ArrayList();
		int randNum=(int)(Math.random()*4);
		for(int i=0;i<randNum;i++) {
			int randXSpeed=(int) (Math.random()*UW.enemyBulletSpeed*2)-UW.enemyBulletSpeed;
			Bullet b=new EnemyBullet(xPos,yPos,right,up);
			b.setXSpeed(randXSpeed);
			b.setYSpeed(UW.enemyBulletSpeed);
			l.add(b);
			}
		return l;
		}
	public void draw(Graphics g) {
		Image img=UW.createImageIcon("../images/ship4.gif", null).getImage();
		g.drawImage(img, (int)xPos,(int) yPos, null);
		}
}
