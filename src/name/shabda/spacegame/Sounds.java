/* * Created on Sep 26, 2004
 */
package name.shabda.spacegame;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author shabda
 */
public class Sounds {
	static 	final int FASTMUSIC=1,FASTMUSIC2=2,SLOWMUSIC=3,SHOPMUSIC=4;
	
	static boolean isPlaying=true;
	static java.net.URL fastmusic = Sounds.class.getResource("../audio/2.mid");
	static java.net.URL fastmusic2 = Sounds.class.getResource("../audio/fast.mid");
	static java.net.URL slowmusic = Sounds.class.getResource("../audio/slow.mid");
	static java.net.URL shopmusic = Sounds.class.getResource("../audio/shop.mid");
	static AudioClip a=Applet.newAudioClip(fastmusic);
	public static void backgroundMusic() {
		a.loop();
		isPlaying=true;
		}
	public static void stopMusic() {
		a.stop();
		isPlaying=false;
		
		}
	public static void setMusic(int music) {
		a.stop();
		isPlaying=false;
		if(music==FASTMUSIC) {
			a=Applet.newAudioClip(fastmusic);	
		}
		else if(music==FASTMUSIC2) {
			a=Applet.newAudioClip(fastmusic2);	
		}
		else if(music==SLOWMUSIC) {
			a=Applet.newAudioClip(slowmusic);	
		}
		if(music==SHOPMUSIC) {
			a=Applet.newAudioClip(shopmusic);	
		}
		a.loop();
		isPlaying=true;
		}
	
	public static void main(String[] args) throws Exception{
		backgroundMusic();
		setMusic(SLOWMUSIC);
		JFrame f=new JFrame();
		JButton fastMusic=new JButton("fast");
		JButton fastMusic2=new JButton("fast2");
		JButton slowMusic=new JButton("slow");
		JButton shopMusic=new JButton("shop");
		fastMusic.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setMusic(FASTMUSIC);
				
			}});
		fastMusic2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setMusic(FASTMUSIC2);
				
			}});
		slowMusic.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setMusic(SLOWMUSIC);
				
			}});
		shopMusic.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				setMusic(SHOPMUSIC);
				
			}});
		f.setLayout(new FlowLayout());
		f.add(fastMusic);
		f.add(fastMusic2);
		f.add(slowMusic);
		f.add(shopMusic);
		f.pack();
		f.setVisible(true);
		}
}
