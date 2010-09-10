/*
 * Created on Dec 8, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.io.Serializable;

/**
 * @author dicky
 * This is a special ship which will spawn enemies
 */
public class Spawner extends RogueShip implements Special,Serializable {

	final static int chanceSpawns=100;//lower value means heigher chance
	public Spawner(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		value=150;
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
	}
	public void draw(Graphics g) {
	g.fillOval((int)xPos,(int) yPos, size, size);	
	
	}

}
