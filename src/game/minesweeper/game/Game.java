package game.minesweeper.game;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;

import javax.swing.JOptionPane;

import org.lwjgl.opengl.GL;

import game.minesweeper.render.Shader;
import game.minesweeper.render.Texture;
import game.minesweeper.render.VAO;
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
		window = new Window("Mine Sweeper", 500, 500, true);
		
		//Create all OpenGL bindings
		GL.createCapabilities();
		
		//Create all the VAOs
		Shader buttonShader = new Shader("Shaders/buttonVertex.glsl", "Shaders/buttonFragment.glsl", null);
		Texture buttonTexture = new Texture("Textures/ButtonTexture.png");
				
		VAO buttonVAO = new VAO(buttonShader, buttonTexture);
				
		//Create all the game objects
		Button button = new Button(0, 0, 500, 500);
				
		//Add all the elements to the VAOs
		buttonVAO.addElement(button);
			
		//Add all the attributes to the VAOs
		buttonVAO.addAttribute(2);
		buttonVAO.addAttribute(2);
				
		//Add the VAO to the window
		window.addVAO(buttonVAO);
		
		//Visualize the window
		window.showWindow();
		
		//Set the clear color to black
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		//Enable the alfa value in color settings
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_BLEND);
		
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
			//Draw the VAOs
			window.drawVAO();
			
			//Update the window
			window.update();
			
			//Reset the scene to default color
			glClear(GL_COLOR_BUFFER_BIT);
		}
		
		//After i stop and clean all
		stop();
	}
}
