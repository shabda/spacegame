/*
 * Created on Dec 23, 2004
 */
package name.shabda.spacegame;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/**
 * @author dicky
 * The class which is in charge of all drawing
 */
public class Rendered implements Serializable {
	public void renderIt(Graphics g) {
	System.out.print(".");
	g.setColor(Color.black);
	g.drawString("TODO", 120, 120);	
	}
	public static void main(String[] args) {
	}
}
