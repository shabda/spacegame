/*
 * Created on Dec 8, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

/**
 * @author dicky
 * A stupid ship is a ship which will come in bonus stages.
 * It doesnot fire and it does not do any fancy stuff.
 */
public class StupidShip	extends RogueShip implements Serializable{

	/**
	 * 
	 */
	
	static final ImageIcon img=UW.createImageIcon("../images/StupidShip.gif","");
	static final Image image=img.getImage();
	public StupidShip(int pos,int pos2,int speed1,int speed2) {
		super(pos,pos2,speed1,speed2);
		size=25;
		value=50;
	}
	public List fireMultiple() {//if it fires it has no bullets
	List l=new ArrayList(0);
	return l;
	}
	public void draw(Graphics g) {
		g.drawImage(image,(int)xPos,(int)yPos,size,size,null);	
	}
	

}
