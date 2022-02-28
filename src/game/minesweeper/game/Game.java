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

import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;

import org.lwjgl.opengl.GL;
import org.w3c.dom.Document;

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
	private static int widthCell;
	private static int heightCell;
	private static int sizeCell;
	private static float difficulty;
	
	private static final int screenWidthReference 	= 1920;
	private static final int screenHeightReference 	= 1080;
	
	private static float scaleFactor;
	
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
	
	private static void parseConfig()
	{
		try 
		{
			//Take and parse the document
			Document configDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("config.xml");
			int width 		= Integer.parseInt(configDocument.getElementsByTagName("width").item(0).getTextContent());
			int height 		= Integer.parseInt(configDocument.getElementsByTagName("height").item(0).getTextContent());
			int cellSize 	= Integer.parseInt(configDocument.getElementsByTagName("cellSize").item(0).getTextContent());
			int diff		= Integer.parseInt(configDocument.getElementsByTagName("difficulty").item(0).getTextContent());
			
			//Control the parsed arguments
			width 		= width > 0 ? width : 20;
			height		= height > 0 ? height : 20;
			cellSize	= cellSize > 0 ? cellSize : 30;
			diff	 	= diff > 0 && diff < 100 ? diff : 15;
			
			//Assign all the data
			widthCell 	= width;
			heightCell 	= height;
			sizeCell	= cellSize;
			difficulty 	= 1 - diff / 100.0f;
			
		} catch (Exception e) 
		{
			// Failed parsing i throw a runtime error
			JOptionPane.showMessageDialog(null, "Error parsing the config.xml document", "Document parsing error", JOptionPane.ERROR_MESSAGE);
			throw new RuntimeException("Failed parsing config.xml file [Game]");
		}
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
		//Take the screen size to scale the components
		int width 	= (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int height 	= (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		
		//Calculate the scaling factor
		scaleFactor = (float)(width * height) / (float)(screenWidthReference * screenHeightReference);
		
		//Scale the cellsize
		sizeCell = (int)(sizeCell * scaleFactor);
		
		//Parse the config document
		parseConfig();
		
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
