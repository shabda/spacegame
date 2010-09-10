/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.io.Serializable;

/**
 * @author shabda
 */
public abstract class Ship implements Artifact, Moving,Serializable {
	boolean alive;
	int value=1;//this value represents how valuable a ship is. Better ships have heigher value.
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#collision(name.shabda.spacegame.Artifact)
	 */
	public boolean collision(Artifact a){
	int safe=5;
	boolean hasHit=false;
	if(xPos-a.getXPos()<safe&&yPos-a.getYPos()<safe)
		hasHit=true;
		return hasHit;
	}
	double xPos,yPos;//position
	double xSpeed,ySpeed;//speed
	double health;//health
	double armour;//armour of the ship
	double fuel;
	boolean right,up;
	
	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 */
	public Ship(double pos, double pos2,double speed, double speed2) {
		super();
		xPos = pos;
		yPos = pos2;
		xSpeed = speed;
		ySpeed = speed2;
		alive=true;
	}
	/**
	 * @return Returns the isAlive.
	 */
	public boolean isAlive() {
		return alive;
	}
	/**
	 * @param isAlive The isAlive to set.
	 */
	public void setAlive(boolean isAlive) {
		this.alive = isAlive;
	}
	/**
	 * @return Returns the right.
	 */
	public boolean isRight() {
		return right;
	}
	/**
	 * @param right The right to set.
	 */
	public void setRight(boolean right) {
		this.right = right;
	}
	/**
	 * @return Returns the up.
	 */
	public boolean isUp() {
		return up;
	}
	/**
	 * @param up The up to set.
	 */
	public void setUp(boolean up) {
		this.up = up;
	}
	/**
	 * @return Returns the armour.
	 */
	public void move(){
		if(right){
			xPos+=xSpeed;
			}
		else if(!right)
			{xPos-=xSpeed;}
		if(up){
			yPos-=ySpeed;
			}
		else if(!up){
			yPos+=ySpeed;
			}
		}
	public double getArmour() {
		return armour;
	}
	/**
	 * @param armour The armour to set.
	 */
	public void setArmour(double armour) {
		this.armour = armour;
	}
	/**
	 * @return Returns the health.
	 */
	public double getHealth() {
		return health;
	}
	/**
	 * @param health The health to set.
	 */
	public void setHealth(double health) {
		this.health = health;
	}
	/**
	 * @return Returns the speed.
	 */
	public double getXSpeed() {
		return xSpeed;
	}
	/**
	 * @param speed The speed to set.
	 */
	public void setXSpeed(double speed) {
		this.xSpeed = speed;
	}
	/**
	 * @return Returns the xPos.
	 */
	public double getXPos() {
		return xPos;
	}
	/**
	 * @param pos The xPos to set.
	 */
	public void setXPos(double pos) {
		xPos = pos;
	}
	/**
	 * @return Returns the yPos.
	 */
	public double getYPos() {
		return yPos;
	}
	/**
	 * @param pos The yPos to set.
	 */
	public void setYPos(double pos) {
		yPos = pos;
	}
	/**
	 * @return Returns the pos.
	 */
	public double[] getPos() {
		double[] pos=new double[2];
		pos[0]=xPos;
		pos[1]=yPos;
		return pos;
	}
	/**
	 * @param pos
	 * @param pos2
	 * @param speed
	 * @param speed2
	 * @param health
	 * @param armour
	 * @param fuel
	 * @param right
	 * @param up
	 */
	public Ship(double pos, double pos2, double speed, double speed2, double health,
			int armour, int fuel, boolean right, boolean up) {
		super();
		xPos = pos;
		yPos = pos2;
		xSpeed = speed;
		ySpeed = speed2;
		this.health = health;
		this.armour = armour;
		this.fuel = fuel;
		this.right = right;
		this.up = up;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s="a ship";
		return s;
	}
	/**
	 * @return Returns the fuel.
	 */
	public double getFuel() {
		return fuel;
	}
	/**
	 * @return Returns the ySpeed.
	 */
	public double getYSpeed() {
		return ySpeed;
	}
	/**
	 * @param speed The ySpeed to set.
	 */
	public void setYSpeed(double speed) {
		ySpeed = speed;
	}
	/**
	 * @param fuel The fuel to set.
	 */
	public void setFuel(int fuel) {
		this.fuel = fuel;
	}
}
