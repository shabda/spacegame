/*
 * Created on Oct 1, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * @author shabda
 */
public class Planets extends SpaceScape implements Drawable,Moving,Serializable{
	/**
	 * 
	 */
	Image image;
	int numberOfPics=5;
	final int randBool;
	Timer t;
	public Planets (int xP,int yP) {
		xPos=xP;
		yPos=yP;
		xSpeed=0;
		ySpeed=UW.passiveSpeed;
		ImageIcon img;
		randBool=(int) (Math.random()*2);
		int i=(int) (Math.random()*numberOfPics+1);
	t =new Timer(1000,new ActionListener() {
			//just a stupid trick to give illusion of 3d
			//some planets will be moving away
			//others will be coming towards you
			public void actionPerformed(ActionEvent e) {
				if(randBool==0)
				size*=1.02;
				else size/=1.03;
				//System.out.println("size is dec");
				
			}
		
			} );
		t.start();
		if(i==1)
		{//img=new ImageIcon("planet2.gif");
			img=UW.createImageIcon("../images/planet2.gif", "");
		}
		else if(i==2)
		{//img=new ImageIcon("saturn.gif");
			img=UW.createImageIcon("../images/saturn.gif", "");
		}
		else if(i==3)
		{//img=new ImageIcon("saturn.gif");
			img=UW.createImageIcon("../images/sun.gif", "");
		}
		else if(i==4)
		{//img=new ImageIcon("saturn.gif");
			img=UW.createImageIcon("../images/glow.gif", "");
		}
		else if(i==5)
		{//img=new ImageIcon("saturn.gif");
			img=UW.createImageIcon("../images/earth1.gif", "");
		}
		else
		{//img=new ImageIcon("planet2.gif");
			img=UW.createImageIcon("../images/planet2.gif", "");
		}
		image=img.getImage();
		}
	public Planets(int xP,int yP,int size) {
		this(xP,yP);
		this.size=size;
		}
	
	public static void main(String[] args) {
	}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Drawable#draw(java.awt.Graphics)
	 */
	public void draw(Graphics g) {
		//Graphics2D g2d = (Graphics2D)g;
		//g2d.setColor(Color.yellow);
		//g2d.fill(new Ellipse2D.Double(xPos,yPos,size,size));
		//g2d.drawImage(
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);
}
	/* (non-Javadoc)
	 * @see name.shabda.spacegame.Moving#move()
	 */
	public void move(){
		yPos+=ySpeed;
		}
}