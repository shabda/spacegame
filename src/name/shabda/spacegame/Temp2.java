/*
 * Created on Dec 21, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * @author dicky
 */
public class Temp2 extends JLabel{
	int i=0;
	public void render(Graphics g) {
		i++;
		g.drawString(""+i, 30, 30);
	}

	public static void main(String[] args) {
	}
}
