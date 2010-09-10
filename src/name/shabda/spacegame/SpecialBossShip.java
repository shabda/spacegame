/*
 * Created on Dec 8, 2004
 */
package name.shabda.spacegame;

import java.io.Serializable;

/**
 * @author dicky
 */
public class SpecialBossShip extends RogueShip implements Special,Serializable{
	final static int chanceSpawns=100;
	final static int chanceFires=100;
	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 */
	public SpecialBossShip(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
	}

	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Special#doSpecialStuff(name.shabda.spacegame.Space)
	 */
	public void doSpecialStuff(Space s) {
		move();//need to call as move will not be called as this is a special ship
		int x=(int) (Math.random()*chanceSpawns);
		if(x==0) {
		RogueShip r=new RogueShip(xPos,yPos,UW.xSpeed,UW.ySpeed);//create a new enemy
		s.rogues.add(r);//add it to the game
		}
		x=(int) (Math.random()*chanceFires);
		if(x==0) {
			Bullet b=new EnemyBullet(xPos,yPos,right,up);
			b.setYSpeed(UW.enemyBulletSpeed);
			s.enemyWeapons.add(b);
			}
	}



}
