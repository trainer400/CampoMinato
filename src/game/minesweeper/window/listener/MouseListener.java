package game.minesweeper.window.listener;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

import game.minesweeper.window.listener.MouseEvent.MouseEventType;

/**
 * This class describes the mouse listener for the window and the 
 * mouse button callback.
 * @author Matteo Pignataro
 */
public class MouseListener extends GLFWMouseButtonCallback
{
	/**
	 * Last event registered
	 */
	private MouseEvent lastMouseEvent;
	
	/**
	 * Mouse Position Listener
	 */
	private MousePositionListener positionListener;
	
	/**
	 * Constructor
	 */
	public MouseListener()
	{
		//Initialize the position listener
		positionListener = new MousePositionListener();
	}
	
	@Override
	public synchronized void invoke(long window, int button, int action, int mods) 
	{
		//Check if the user pressed the button
		if(action == GLFW_PRESS)
		{
			//Create the new event
			lastMouseEvent = new MouseEvent(positionListener.getPosX(),
											positionListener.getPosY(),
											MouseEventType.values()[button < 3 ? button : 3]);
		}
	}
	
	/**
	 * @return Last mouse event. Null value in case of no recent event.
	 */
	public synchronized MouseEvent getLastMouseEvent() 
	{
		//Check if there is something to return
		if(lastMouseEvent == null) { return null; }
		
		//Create a temporary variable where i store the details
		MouseEvent temp = new MouseEvent(lastMouseEvent.getPosX(),
				 						 lastMouseEvent.getPosY(),
				 						 lastMouseEvent.getEventType());
										 
		//Destroy the mouse event
		this.lastMouseEvent = null;
		 
		//Return the temporary variable
		return temp; 
	}
	
	/**
	 * @return Mouse Position Listener
	 */
	public MousePositionListener getMousePositionListener() { return positionListener; }
}
