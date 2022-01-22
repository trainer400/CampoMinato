package game.minesweeper.game;

import static org.lwjgl.glfw.GLFW.glfwInit;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.GL;

import game.minesweeper.window.Window;

/**
 * This is the main class that implements all the game rules
 * @author Matteo Pignataro
 */
public class Game
{
	/**
	 * Main window
	 */
	private static Window window;
	
	/**
	 * Initializes all the class objects and sets them
	 */
	public static void start()
	{
		//Check if glfw initializes
		if(!glfwInit())
		{
			JOptionPane.showMessageDialog(null, "Error initializing GLFW!", "OpenGL error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//Create the window object
		window = new Window("Mine Sweeper", 500, 500, false);
		
		//Create all OpenGL bindings
		GL.createCapabilities();
		
		//Visualize the window
		window.showWindow();
		
		//At the end i start the game loop
		run();
	}
	
	/**
	 * Stop function
	 */
	public static void stop()
	{
		
	}
	
	/**
	 * Game loop
	 */
	private static void run()
	{
		//While the game is running
		while(window.isOpen())
		{
			//Update the window
			window.update();
		}
		
		//After i stop and clean all
		stop();
	}
}
