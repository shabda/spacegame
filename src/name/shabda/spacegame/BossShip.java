/*
 * Created on Oct 5, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * @author shabda
 */
public class BossShip extends RogueShip implements Serializable{
	static final int speed=1;
	static final int barrelSize=2;
	static final int spawnSize=2;
	static int deadly;
	static final ImageIcon img=UW.createImageIcon("../images/bossShip.gif","");
	static final Image image=img.getImage();
	public BossShip(double pos, double pos2, double speed, double speed2) {
		super(pos, pos2, speed, speed2);
		size=50;
		xSpeed=speed;
		int rand=(int) (Math.random()*2);
		if(rand==0) {
		right=true;	
		}
		else right=false;
		value=200;
	}
	public BossShip(double pos, double pos2, double speed, double speed2,int deadlyNess) {
		super(pos, pos2, speed, speed2);
		size=50;
		xSpeed=speed;
		int rand=(int) (Math.random()*2);
		if(rand==0) {
		right=true;	
		}
		else right=false;
		deadly=deadlyNess;
		value=200;
	}
	
	public void move(){
		////System.out.println(xSpeed);	
		if(xPos>UW.xSize-2||xPos<2) {
			right=!right;
			}
		if(right){
			xPos+=speed;
			}
		else if(!right)
			{xPos-=speed;}
		int rand=(int) (Math.random()*2);
		if(rand==0) {
			if(yPos>1)
				yPos--;
			}
		else if(rand==1) {
			yPos++;
			}
		}
	public void draw(Graphics g) {
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);	
	}
	public List spawnShips() {
		List l=new ArrayList();
		int num=(int) (Math.random()*deadly*3+spawnSize);
		for(int i=0;i<num;i++) {
			int randXSpeed=(int) (Math.random()*10-5);
			l.add(new RogueShip(xPos,yPos,randXSpeed,5));
			}
		return l;
		}
	public List fireMultiple() {
		List l=new ArrayList();
		int rand=(int) (Math.random()*barrelSize)+deadly;
		for(int i=0;i<rand;i++) {
			int randXSpeed=(int) (Math.random()*UW.enemyBulletSpeed*2)-UW.enemyBulletSpeed;
			Bullet b=new EnemyBullet(xPos,yPos,right,up);
			b.setXSpeed(randXSpeed);
			b.setYSpeed(UW.enemyBulletSpeed);
			l.add(b);
			}
		return l;
		}
	public static void main(String[] args) {
	}
}
