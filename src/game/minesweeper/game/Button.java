package game.minesweeper.game;

import game.minesweeper.render.DrawableElement;

/**
 * This class represents a typical button with sizeX, sizeY
 * And position related to the top left corner of the window
 * @author Matteo Pignataro
 */
public class Button extends DrawableElement
{
	/**
	 * X position
	 */
	private float x;
	
	/**
	 * Y position
	 */
	private float y;
	
	/**
	 * X dimensions
	 */
	private int dimX;
	
	/**
	 * Y dimensions
	 */
	private int dimY;
	
	/**
	 * Vertices array
	 */
	private float vertices[];
	
	/**
	 * Elements array
	 */
	private int elements[];
	
	/**
	 * Constructor
	 * @param x the top left corner
	 * @param y the top left corner
	 * @param dimX the button X dimension
	 * @param dimY the button Y dimension
	 */
	public Button(float x, float y, int dimX, int dimY)
	{
		//Assign all the variables
		this.x = x - 1;
		this.y = y + 1;
		
		this.dimX = dimX > 0 ? dimX : 1;
		this.dimY = dimY > 0 ? dimY : 1;
		
		//Instance the arrays
		vertices = new float[16]; //4 floats per vertex (2 for x and y) (2 for texture mapping)
		elements = new int[]{0, 1, 3, 0, 3, 2}; //sequence for rectangle draw
	}
	
	/**
	 * To adapt the button to the window i have to calculate all the vertices
	 */
	@Override
	public void updateWindowSize(int width, int height) 
	{
		vertices[0] = x;
		vertices[1] = y;
		vertices[2] = 0; 			//Texture TODO adjust
		vertices[3] = 0;			//Texture TODO adjust
		
		vertices[4] = x + (float)dimX * 2 / width;
		vertices[5] = y;
		vertices[6] = 0.125f; 		//Texture TODO adjust
		vertices[7] = 0;	 		//Texture TODO adjust
		
		vertices[8]	 = x;
		vertices[9]  = y - (float)dimY * 2 / height;
		vertices[10] = 0; 			//Texture TODO adjust
		vertices[11] = 0.125f;		//Texture TODO adjust
		
		vertices[12] = x + (float)dimX * 2 / width;
		vertices[13] = y - (float)dimY * 2 / height;
		vertices[14] = 0.125f;		//Texture TODO adjust
		vertices[15] = 0.125f;		//Texture TODO adjust
		
		//Toggle the need to update
		updated = true;
	}

	@Override
	public float[] getVertices() { return vertices.clone(); }

	@Override
	public int getVerticesSize() { return vertices.length; }

	@Override
	public int[] getElements() { return elements.clone(); }

	@Override
	public int getElementsSize() { return elements.length; }
}
