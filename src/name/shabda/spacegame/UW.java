/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import javax.swing.ImageIcon;

/**
 * @author shabda
 * this is my configuration file and keeps track of global data
 * 
 */
public class UW {
	final static int xSize=800;
	final static int ySize=500;
	final static int xSpeed=0;
	final static double ySpeed=1;
	final static int bulletSpeed=7;
	final static int aVal=8;
	final static int enemyBulletSpeed=-2;
	final static int powerUpSpeed=1;
	static double passiveSpeed=.4;
	public static int Rand(int start,int end) {
		int val=(int)(Math.random()*(end-start)+start);
		return val;
	}
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = UW.class.getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}


	public static void main(String[] args) {
		System.out.print(Rand(10,20));
	}
}
