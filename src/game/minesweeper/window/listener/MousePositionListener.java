package game.minesweeper.window.listener;

import org.lwjgl.glfw.GLFWCursorPosCallback;

/**
 * This class represents the changing cursor position callback
 * @author Matteo Pignataro
 */
public class MousePositionListener extends GLFWCursorPosCallback
{
	/**
	 * Mouse position variables
	 */
	private double posX;
	private double posY;
	
	@Override
	public synchronized void invoke(long window, double xpos, double ypos) 
	{
		//Set the new position
		this.posX = xpos;
		this.posY = ypos;
	}
	
	/**
	 * Mouse position getters
	 */
	public synchronized double getPosX() { return posX; }
	public synchronized double getPosY() { return posY; }
}
