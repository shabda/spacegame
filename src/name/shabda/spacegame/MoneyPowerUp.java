/*
 * Created on Dec 10, 2004
 */
package name.shabda.spacegame;

import java.io.Serializable;

/**
 * @author dicky
 * This powerUp increases the hero's money.
 */
public class MoneyPowerUp extends PowerUp implements Serializable{

	/**
	 * @param xP
	 */
	public MoneyPowerUp(int xP) {
		super(xP);
	}
	public void affect(Space s) {
		s.heroData.money+=100;
		}

}
