/*
 * Created on Dec 8, 2004
 */
package name.shabda.spacegame;

/**
 * @author dicky
 * This interface is needs to be implemented by any  special thing in the game.
 * It has only one method. However *only* that method will be called in the game 
 * ,so whatever the specialThing needs to do has to be called in this method. 
 */
public interface Special {
	public void doSpecialStuff(Space s);//all your special stuff in this method
}
