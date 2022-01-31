package game.minesweeper.game;

import game.minesweeper.render.DrawableElement;

/**
 * This class represents a typical minesweeper cell with sizeX, sizeY
 * And position related to the top left corner of the window
 * @author Matteo Pignataro
 */
public class Cell extends DrawableElement
{
	/**
	 * X position
	 */
	private int x;
	
	/**
	 * Y position
	 */
	private int y;
	
	/**
	 * X dimensions
	 */
	private int dimX;
	
	/**
	 * Y dimensions
	 */
	private int dimY;
	
	/**
	 * The game cell actual state
	 */
	private CellStates state;
	
	/**
	 * The undercover state
	 */
	private CellStates realState;
	
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
	public Cell(int x, int y, int dimX, int dimY, CellStates realState)
	{
		//Assign all the variables
		this.x = x;
		this.y = y;
		
		this.dimX = dimX > 0 ? dimX : 1;
		this.dimY = dimY > 0 ? dimY : 1;
		
		//I set the real state
		this.realState = realState;
		
		//By default i set the cell to be hidden
		state = CellStates.CELL_HIDDEN;
		
		//Instance the arrays
		vertices = new float[16]; //4 floats per vertex (2 for x and y) (2 for texture mapping)
		elements = new int[]{0, 1, 3, 0, 3, 2}; //sequence for rectangle draw
	}
	
	/**
	 * Method to transfer the cell state texture mapping to the vertices array
	 */
	private void setupTexture()
	{
		//Memorize the texture mapping
		//Then i can transfer all the informations
		float mapping[] = state.getTextureMapping();
		
		//Top left corner
		vertices[2] = mapping[0];
		vertices[3] = mapping[1];
		
		//Top right corner
		vertices[6] = mapping[2];
		vertices[7] = mapping[3];
		
		//Bottom left corner
		vertices[10] = mapping[4];
		vertices[11] = mapping[5];
		
		//Bottom right corner
		vertices[14] = mapping[6];
		vertices[15] = mapping[7];
	}
	
	/**
	 * To adapt the button to the window i have to calculate all the vertices
	 */
	@Override
	public void updateWindowSize(int width, int height) 
	{
		float xProv =  x * 	2.0f / 	width 	- 1;
		float yProv = -y * 	2.0f / 	height	+ 1;
		
		//Top left corner
		vertices[0] = xProv;
		vertices[1] = yProv;
		
		//Top right corner
		vertices[4] = xProv + (float)dimX * 2 / width;
		vertices[5] = yProv;
		
		//Bottom left corner
		vertices[8]	 = xProv;
		vertices[9]  = yProv - (float)dimY * 2 / height;
		
		//Bottom right corner
		vertices[12] = xProv + (float)dimX * 2 / width;
		vertices[13] = yProv - (float)dimY * 2 / height;
		
		//Toggle the need to update
		updated = true;
	}
	
	/**
	 * Method that checks if the position passed is inside the cell
	 * @param posX The X position to analyze
	 * @param posY The Y position to analyze
	 * @return boolean of the result
	 */
	public boolean isInside(int posX, int posY)
	{
		//If the X position is inside
		if(posX > x && posX < x + dimX)
		{
			//If the Y position is inside
			if(posY > y && posY < y + dimY)
			{
				//It is inside the cell
				return true;
			}
		}
		//It's not inside the cell
		return false;
	}
	
	/**
	 * Method to change the cell internal state
	 * @param state That needs to be set
	 */
	public void setState(CellStates state) { this.state = state; updated = true; }
	
	/**
	 * @return the real cell state hidden
	 */
	public CellStates getRealState() { return realState; }

	@Override
	public float[] getVertices() 
	{	
		setupTexture();
		//Set the texture
		return vertices.clone(); 
	}

	@Override
	public int getVerticesSize() { return vertices.length; }

	@Override
	public int[] getElements() { return elements.clone(); }

	@Override
	public int getElementsSize() { return elements.length; }
}
