/*
 * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;


/**
 * @author shabda
 */
public class Temp {
	public void renderGraphics(Graphics g) {
		int val1=(int) (Math.random()*800);
		int val2=(int) (Math.random()*800);
	ImageIcon img=UW.createImageIcon("../images/alien.gif", null);
	Image i=img.getImage();
	g.drawImage(i,val1,val2,null);
	}

	public static void main(String[] args) {
		final Temp app=new Temp();
		final Temp2 tt=new Temp2();
		//final Screen s=new Screen();
		final Space s=new Space();
		JFrame frame=new JFrame();
		
		frame.setUndecorated(true);
		frame.setResizable(false);
		JLabel l=new JLabel("label");
		l.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent arg0) {
				System.exit(0);
				
			}

			public void mouseMoved(MouseEvent arg0) {
				System.out.print('.');
				
			}});
		frame.getContentPane().add(l);
		GraphicsEnvironment env = GraphicsEnvironment.
        getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = env.getScreenDevices();
		devices[0].setFullScreenWindow(frame);
		//final Graphics g=devices[0].getFullScreenWindow().getGraphics();
		//g.setColor(Color.RED);
		frame.createBufferStrategy(2);
		final BufferStrategy strat=frame.getBufferStrategy();
		final Graphics g=strat.getDrawGraphics();
		Timer t=new Timer(100,new ActionListener() {	 
			public void actionPerformed(ActionEvent arg0) {
				
				int val1=(int) (Math.random()*800);
				int val2=(int) (Math.random()*800);
				int val3=(int) (Math.random()*300);
				int val4=(int) (Math.random()*300);
				g.clearRect(0, 0, 800, 600);
				g.setColor(Color.black);
				g.fillRect(0,0,800, 600);
				g.setColor(Color.RED);
				//g.drawString("String",30, 40);
				//app.renderGraphics(g);
				//tt.render(g);
				s.s.paintComponent(g);
				strat.show();
				//g.dispose();
			}
			
			});
		t.start();
		//g.fillRect(0, 0, 300, 300);
	}
}
