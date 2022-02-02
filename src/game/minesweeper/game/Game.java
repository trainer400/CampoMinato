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

import game.minesweeper.game.objects.Cell;
import game.minesweeper.game.objects.CellTable;
import game.minesweeper.game.objects.Menu;
import game.minesweeper.game.objects.CellTable.GameState;
import game.minesweeper.render.DrawableElement;
import game.minesweeper.render.Shader;
import game.minesweeper.render.Texture;
import game.minesweeper.render.VAO;
import game.minesweeper.window.Window;
import game.minesweeper.window.listener.MouseEvent;

/**
 * This is the main class that implements all the game rules
 * @author Matteo Pignataro
 */
public class Game
{
	/**
	 * Game properties
	 */
	private static final int widthCell 		= 20;
	private static final int heightCell 	= 20;
	private static final int sizeCell 		= 40;
	private static final float difficulty 	= 0.85f;
	
	/**
	 * Main window
	 */
	private static Window window;
	
	/**
	 * Cell table variables
	 */
	private static CellTable table;
	
	/**
	 * Game menu
	 */
	private static Menu menu;
	
	/**
	 * Method to initialize all that is needed for the cell table
	 */
	private static void initCells()
	{
		//Create texture and shader for cells
		Shader cellShader = new Shader("Shaders/buttonVertex.glsl", "Shaders/buttonFragment.glsl", null);
		Texture cellTexture = new Texture("Textures/ButtonTexture.png");
				
		//Create the VAO for cells
		VAO cellVAO = new VAO(cellShader, cellTexture);
		
		//Create the cellTable
		table = new CellTable(0, 3 * sizeCell, widthCell, heightCell, sizeCell, difficulty);
		
		//Create a temporary copy of the table
		Cell[][] temp = table.getTable();
		
		//Add all the cells to the VAO
		for(int i = 0; i < temp.length; i++)
		{
			for(int j = 0; j < temp[i].length; j++)
			{
				cellVAO.addElement(temp[i][j]);
			}
		}
		
		//Add the cell attributes
		cellVAO.addAttribute(2);
		cellVAO.addAttribute(2);
		
		//At the end add the VAO to the window
		window.addVAO(cellVAO);
	}
	
	/**
	 * Method to initialize the menu space
	 */
	private static void initMenu()
	{
		//Create texture and shader for the menu
		Shader menuShader = new Shader("Shaders/buttonVertex.glsl", "Shaders/buttonFragment.glsl", null);
		Texture menuTexture = new Texture("Textures/ButtonTexture.png");
		
		//Create the VAO for the menu
		VAO menuVAO = new VAO(menuShader, menuTexture);
		
		//Create the menu
		menu = new Menu(0, 0, window.getWidth(), 3 * sizeCell, sizeCell);
		
		//Take all the drawable elements
		DrawableElement array[] = menu.getDrawableElements();
		
		//Forall the elements i add them
		for(int i = 0; i < array.length; i++)
		{
			//Add the reset button to the VAO
			menuVAO.addElement(array[i]);
		}
			
		//Add the attributes
		menuVAO.addAttribute(2);
		menuVAO.addAttribute(2);
		
		//At the end i add the vao to the window
		window.addVAO(menuVAO);
	}
	
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
		window = new Window("Mine Sweeper", widthCell * sizeCell, heightCell * sizeCell + 3 * sizeCell, false);
		
		//Create all OpenGL bindings
		GL.createCapabilities();
		
		//Init the cells
		initCells();
		
		//Init the top menu
		initMenu();
		
		//Visualize the window
		window.showWindow();
		
		//Set the clear color to black
		glClearColor(0.617f, 0.617f, 0.617f, 1.0f);
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
			//Take the event
			MouseEvent event = window.getLastMouseEvent();
			
			//Call the handleMouseEvent functions
			table.handleMouseEvent(event);
			menu.handleMouseEvent(event);
			
			//If the game state is running
			if(table.getGameState() != GameState.GAME_STOP)
			{
				//I update the time passed counter
				menu.tick();
			}
			
			//I update the number of minest counter
			menu.showNumber(table.getBombsNumber() - table.getFlagsNumber());
			
			//Check if we should reset the game
			if(menu.shouldReset())
			{
				menu.resetCounts();
				table.resetTable();
			}
			
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
