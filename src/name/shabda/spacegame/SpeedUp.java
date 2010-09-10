/*
 * Created on Dec 5, 2004
 */
package name.shabda.spacegame;

//import java.util.Timer;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author dicky
 */
public class SpeedUp extends PowerUp implements Serializable {

	/**
	 * @param xP
	 */
	public SpeedUp(int xP) {
		super(xP);
	}
	public void affect(final Space s) {
		System.out.print("hero.maxSpeed"+s.hero.maxSpeed);
		s.hero.maxSpeed*=2;
		s.hero.xSpeed*=2;
		s.hero.ySpeed*=2;
		System.out.print("hero.maxSpeed"+s.hero.maxSpeed);
		final Timer timer=new Timer();
		timer.schedule(new TimerTask() {

			public void run() {
				System.out.print("hero.maxSpeed"+s.hero.maxSpeed);
				s.hero.maxSpeed/=2;
				s.hero.xSpeed/=2;
				s.hero.ySpeed/=2;
				System.out.print("hero.maxSpeed"+s.hero.maxSpeed);
				timer.cancel();
			}
			
			}, 5000);
		}

}
