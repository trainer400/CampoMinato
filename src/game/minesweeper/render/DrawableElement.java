package game.minesweeper.render;

/**
 * @author Matteo Pignataro
 * @biref Class that describes the drawable standard
 */
public abstract class DrawableElement 
{
	/**
	 * @brief Boolean that represents the element status.
	 * If it needs to be refreshed by the VAO it is true
	 */
	protected boolean updated;
	
	/**
	 * @return the internal updated status
	 */
	public boolean isUpdated()	{ return updated; }
	
	/**
	 * @return the object's vertices array
	 */
	public abstract float[] getVertices();
	
	/**
	 * @return the object's elements array
	 */
	public abstract int[] getElements();
}
