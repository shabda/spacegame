/*
 * Created on Dec 5, 2004
 */
package name.shabda.spacegame;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dicky
 * this is a enemy which fires in the reverse direction to normal fire
 * usually this should enter from the back of the screen
 */
public class BackupEnemies extends RogueShip implements Serializable{

	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 */
	public BackupEnemies(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		value=150;
	}
	public List fireMultiple() {
		List l=new ArrayList();
		
		Bullet b=new EnemyBullet(xPos,yPos,right,up);
		b.setYSpeed(-UW.enemyBulletSpeed);
		l.add(b);
		return (l);
		}

}
