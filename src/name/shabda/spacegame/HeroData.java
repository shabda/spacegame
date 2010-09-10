/*
 * Created on Dec 7, 2004
 */
package name.shabda.spacegame;

import java.io.Serializable;

/**
 * @author dicky
 * this class is used to group all the hero data which needs to be
 * saved between the hero deaths. All the variables here are public.
 * Basically this is a C struct.
 */
public class HeroData implements Serializable{
	public int money;
	public int friends;
	public boolean[] guns;
	public boolean technology;
	public HeroData() {
	money=0;
	friends=0;
	guns=new boolean[HeroShip.weapons];
	guns[0]=true;
	guns[1]=true;
	technology=false;
	for(int i=2;i<guns.length;i++) {
		guns[i]=false;
		}
	}
	public void setData(HeroShip h) {
		friends=h.friends;
	}

}
