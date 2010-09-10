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
 * This class is the representation of the hero on the screen.
 */
public class HeroShip extends Ship implements Drawable,Serializable{
	boolean technology=false;
	int friends=5;
	int maxSpeed=5;//max speed of ship
	int bullets=500;//number 0f bullets
	int mines=20;
	int size=60;
	static final int weapons=5;
	Image i;
	boolean[] guns=new boolean[5];
	int currentWeapon;//current weapon 1 is single bullet 2 is triple shot 3 is barrage mode
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s="the hero";
		return s;
		}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Artifact#getCentre()
	 */
	public Point2D getCentre() {
		Point2D c=new Point2D.Double(xPos+size/2,yPos+size/2);
		return c;
	}
	public int getBullets() {
		return bullets;
	}
	public void setBullets(int bullets) {
		this.bullets = bullets;
	}
	public int getCurrentWeapon() {
		return currentWeapon;
	}
	public void setCurrentWeapon(int currentWeapon) {
		this.currentWeapon = currentWeapon;
	}
	public int getMaxSpeed() {
		return maxSpeed;
	}
	public void setMaxSpeed(int maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	/**
	 * @param size The size to set.
	 */
	public  void setSize(int size) {
		this.size = size;
	}
	public Defenders addDefender() {
		double xP=getCentre().getX()+1.2*size*Math.cos(2*Math.PI*Math.random())-7.5;
		double yP=getCentre().getY()+1.2*size*Math.sin(2*Math.PI*Math.random())-7.5;
		return new Defenders(xP,yP,xSpeed,ySpeed);
		}
	public List callReinforcements() {
		List l=new ArrayList();
		if(friends>0) {
		l.add(addDefender());
		friends--;
		}
		return l;
		} 
	/**
	 * @return Returns the size.
	 */
	public int getSize() {
		return size;
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
	public HeroShip(double pos, double pos2, double speed, double speed2,double health,
			int armour, int fuel, boolean right, boolean up) {
		super(pos, pos2, speed, speed2, health, armour, fuel, right, up);
		guns[0]=true;//this gun fires a single bullet in the forward direction.this should always be true. Coz hero needs at least the basic gun.
		guns[1]=true;//fires 3 bullets
		guns[2]=false;//fires multiple bullets in random forward directions.has bullet penalty
		guns[3]=false;//fires multiple bullets in all random directions.has extreme bullet penalty.
		guns[4]=false;//lays mines.uses mines.
		ImageIcon ii=UW.createImageIcon("../images/hero3.gif", "the hero");
		i=ii.getImage();
		currentWeapon=2;
		armour=100;
	}
	
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Drawable#draw(java.awt.Graphics)
	 */
	public void accelerate() {
		if(ySpeed<maxSpeed) {
			if(up)
			ySpeed+=.5;
			else if(!up)
			ySpeed-=.5;
			}
		}
	public void reverse() {
		if(ySpeed>-maxSpeed) {
			if(up)
			ySpeed-=.5;
			else if(!up)
			ySpeed+=.5;
			}
		}
	public void speedRight() {
		if(xSpeed<maxSpeed) {
			if(right)
			xSpeed+=.5;
			else if(!right)
			xSpeed-=.5;
			}
		}
	public void speedLeft() {
		if(xSpeed>-maxSpeed) {
			if(right)
			xSpeed-=.5;
			else if(!right)
			xSpeed+=.5;
			}
		}
	public List fireMultiple() {
		//TODO check if checking if current gun active is neccesary
		List l=new ArrayList();
		if(bullets>0) {
		if(currentWeapon==1) {
			if(guns[0]) {
			if(bullets>0) {
			Bullet b=new Bullet(getCentre().getX()-size/10,yPos,right,up);
			bullets--;
			l.add(b);
			}
			}
		}
		else if(currentWeapon==2) {
			if(guns[1]) {
			Bullet b=new Bullet(getCentre().getX(),yPos,right,up);
			Bullet b1=new Bullet(xPos,yPos,right,up);
			Bullet b2=new Bullet(xPos+size,yPos,right,up);
			bullets-=3;
			if(bullets>0) 
			l.add(b);
			if(bullets>0)
			l.add(b1);
			if(bullets>0)
			l.add(b2);
			}
			}
		else if(currentWeapon==3) {
			int randomNumber=(int) (Math.random()*50);
			for(int i=0;i<randomNumber;i++) {
				if(bullets>0) {
				int randXSpeed=(int) (Math.random()*UW.bulletSpeed*2)-UW.bulletSpeed;
				Bullet b=new Bullet(getCentre().getX(),yPos,right,up);
				b.setXSpeed(randXSpeed);
				l.add(b);
				bullets--;
				}
				}
			if(!technology)//you don't have the technology
			bullets-=40;//cost for this bullet
			}
		else if(currentWeapon==4) {
			int randomNumber=(int) (Math.random()*50);
			for(int i=0;i<randomNumber;i++) {
				if(bullets>0) {
				int randXSpeed=(int) (Math.random()*UW.bulletSpeed*2)-UW.bulletSpeed;
				int randYSpeed=(int) (Math.random()*UW.bulletSpeed*2)-UW.bulletSpeed+2;
				if(randYSpeed!=0) {
				Bullet b=new Bullet(getCentre().getX(),yPos,right,up);
				b.setXSpeed(randXSpeed);
				b.setYSpeed(randYSpeed);
				l.add(b);
				bullets--;
				}
				}
				}
			if(!technology)//you don't have the technology, so pay extra!
			bullets-=60;
			}
		}
		if(bullets<0) {
			bullets=0;}
		if(mines>0) {
			if(currentWeapon==5) {
				if(bullets>0) {
				//Bullet b=new Bullet(getCentre().getX()-size/10,yPos,right,up);
				Weapon b=new Mine(getCentre().getX()-size/10,yPos);
				mines--;
				l.add(b);
				}
			}
		}
		return l;
		}
	public void draw(Graphics g) {
		/*int xDir;
		if(right)xDir=1;
		else xDir=-1;
		int yDir;
		if(up)yDir=-1;
		else yDir=1;
		double realXSpeed=xDir*xSpeed;
		double realYSpeed=yDir*ySpeed;
		double theta=Math.atan(realXSpeed/realYSpeed);
		AffineTransform af=new AffineTransform();
		//af=AffineTransform.getRotateInstance(theta);
		af.translate(xPos, yPos);
		af.rotate(theta);
		Graphics2D g2d=(Graphics2D) g;
		g2d.drawImage(i, af, null);*/
		g.drawImage(i,(int)xPos,(int)yPos,size,size,null);
	}
	public void isHit(Weapon w) {
		armour-=w.damage;
		}
	public void teleport() {
		xPos=Math.random()*UW.xSize;
		yPos=Math.random()*UW.ySize;
		}
	public void nextWeapon() {
		System.out.println("current weapon:"+currentWeapon);
		do {
			if(currentWeapon<weapons)
			currentWeapon++;
			else currentWeapon=1;
			}while(!guns[currentWeapon-1]);
		}
	public void move(int xSize,int ySize) {
		if(xPos>xSize) {
			right=!right;
			}
		else if(xPos<0) {
			right=!right;
			}
		if (yPos>ySize) {
			up=!up;
			}
		else if (yPos<0) {
			up=!up;
			}
		super.move();
		}
	public void setData(HeroData hd){
		for(int i=0;i<weapons;i++) {
			guns[i]=hd.guns[i];
			}
		technology=hd.technology;
		friends=hd.friends;
		}
	public static void main(String[] args) {
		//HeroShip hs=new HeroShip();
	}
}
