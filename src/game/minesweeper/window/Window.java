package game.minesweeper.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

import game.minesweeper.window.listener.MouseEvent;
import game.minesweeper.window.listener.MouseListener;

/**
 * This singleton class represents the main window for the game
 * @author Matteo Pignataro
 */
public class Window extends GLFWWindowSizeCallback
{
	/**
	 * OpenGL window ID
	 */
	private long ID;
	
	/**
	 * Window signature and name
	 */
	private final String name;
	
	/**
	 * Window width
	 */
	private int width;
	
	/**
	 * Window height
	 */
	private int height;
	
	/**
	 * Boolean of resizable state
	 */
	private final boolean resizable;
	
	/**
	 * Boolean of open window status
	 */
	private boolean open;
	
	/**
	 * Mouse Listener
	 */
	private MouseListener mouseListener;
	
	/**
	 * Private Constructor
	 */
	public Window(String name, int width, int height, boolean resizable)
	{
		//Initial dimensions
		this.name = name == null ? "" : name;
		this.width = 500;
		this.height = 500;
		this.resizable = resizable;
		//Set the window to not initialized
		this.ID = -1;
		//Set the window to open
		open = true;
		
		//Initialize the mouse listener
		mouseListener = new MouseListener();
		
		//Initialize the window
		initWindow();
	}
	
	/**
	 * Window initializer
	 * Fails in case of an already initialized window
	 */
	private void initWindow()
	{
		//Initiliaze if and only if the window has not been initialized yet
		if(ID == -1)
		{
			//Set the default settings for the window
			glfwDefaultWindowHints();
			
			//Set the window to visible
			glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
			
			//Set the window to resizable
			glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
			
			//Create the display
			ID = glfwCreateWindow(width, height, name, NULL, NULL);
			
			//Select the context
			glfwMakeContextCurrent(ID);
			
			//Enable V-Sync (1-enabled 0-disabled)
			glfwSwapInterval(1);
			
			//Set the callbacks to the listeners
			glfwSetMouseButtonCallback(ID, mouseListener);
			glfwSetCursorPosCallback(ID, mouseListener.getMousePositionListener());
			//Set the callback to call the clean function
			glfwSetWindowCloseCallback(ID, (long window) -> clean());
			//Set this object as window size change listener
			glfwSetWindowSizeCallback(ID, this);
		}
	}
	
	/**
	 * Shows the window
	 */
	public void showWindow() { glfwShowWindow(ID); }
	
	/**
	 * Hides the window
	 */
	public void hideWindow() { glfwHideWindow(ID); }
	
	/**
	 * Refresh the screen and poll all the events
	 */
	public void update()
	{
		//Swap OpenGL buffers
		glfwSwapBuffers(ID);
		
		//Call all the events managers
		glfwPollEvents();
	}
	
	/**
	 * Clears all the callbacks and the window itself
	 */
	public void clean()
	{
		//Release callbacks
		glfwFreeCallbacks(ID);
		
		//Destroy the window
		glfwDestroyWindow(ID);
		
		//Put the status to closed
		open = false;
	}

	@Override
	public void invoke(long window, int width, int height) 
	{
		//Update the Width and height values
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Window getters
	 */
	public MouseEvent getLastMouseEvent() 	{ return mouseListener.getLastMouseEvent(); }
	public long getID()						{ return ID; }
	public int getWidth() 				  	{ return width; }
	public int getHeight() 				 	{ return height; }
	public String getName()					{ return name; }
	public boolean isResizable() 			{ return resizable; }
	public boolean isOpen()					{ return open; }
}
