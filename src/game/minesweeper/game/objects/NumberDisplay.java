package game.minesweeper.game.objects;

import game.minesweeper.render.DrawableElement;

/**
 * Drawable element that represent a single digit number
 * @author Matteo Pignataro
 */
public class NumberDisplay extends DrawableElement
{
	/**
	 * Top left corner X position
	 */
	private int x;
	
	/**
	 * Top left corner Y position
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
	 * Number visualized
	 */
	private NumberDisplayState state;
	
	/**
	 * Vertex array
	 */
	private float vertices[];
	
	/**
	 * Elements array
	 */
	private int elements[];
	
	/**
	 * Constructor
	 * @param x The top left x coordinate
	 * @param y The top left y coordinate
	 * @param dimX The X dimensions
	 * @param dimY The Y dimensions
	 */
	public NumberDisplay(int x, int y, int dimX, int dimY)
	{
		//Assign all the variables
		this.x = x;
		this.y = y;
		
		this.dimX = dimX > 0 ? dimX : 1;
		this.dimY = dimY > 0 ? dimY : 1;
		
		//By default i set the state to 0
		state = NumberDisplayState.NUMBER_0;
		
		//Instance the arrays
		vertices = new float[16]; //4 vertices with 4 values
		elements = new int[]{0, 1, 3, 0, 3, 2}; //sequence for rectangle draw
	}
	
	/**
	 * Method to setup the texure mappings
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
	 * Method to increment the number visualized.
	 * In case of state 9 the next value is 0
	 */
	public void increment()
	{
		if(state.NUMBER < 9)
		{
			//Set the number to its successor
			state = NumberDisplayState.values()[state.NUMBER + 1];
		}
		else 
		{
			state = NumberDisplayState.NUMBER_0;
		}
		//Force the update
		updated = true;
	}
	
	/**
	 * Reset method. Initial state is 0
	 */
	public void reset()
	{
		//Set the number to 0
		state = NumberDisplayState.NUMBER_0;
		//Force the update
		updated = true;
	}
	
	/**
	 * Method to set manually the number visualized
	 * @param state The state number
	 */
	public void setState(NumberDisplayState state)
	{
		//Set the state
		this.state = state;
		//Force update
		updated = true;
	}
	
	/**
	 * State getter
	 * @return The current state
	 */
	public NumberDisplayState getState() { return state; }
	
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
