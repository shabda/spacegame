/*
 * Created on Dec 23, 2004
 */
package name.shabda.spacegame;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author dicky
 */
public class Fishes {

	public static void main(String[] args) {
		JFrame frame=new JFrame();
		frame.setUndecorated(true);
		frame.setResizable(false);
		GraphicsEnvironment env = GraphicsEnvironment.
        getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		devices[0].setFullScreenWindow(frame);
		frame.createBufferStrategy(2);
		final BufferStrategy strat=frame.getBufferStrategy();
		final Graphics g=strat.getDrawGraphics();
		Rendered r=new Rendered();
		r.renderIt(g);
		strat.show();
	}
}
