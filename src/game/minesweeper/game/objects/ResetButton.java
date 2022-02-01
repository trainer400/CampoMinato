package game.minesweeper.game.objects;

import game.minesweeper.render.DrawableElement;
import game.minesweeper.window.listener.MouseEvent;
import game.minesweeper.window.listener.MouseEvent.MouseEventType;

/**
 * The smile reset button typical in mine sweeper
 * @author Matteo Pignataro
 */
public class ResetButton extends DrawableElement
{
	/**
	 * Enumerate the two state: normal, pressed
	 * @author Matteo Pignataro
	 */
	public static enum ResetButtonState
	{
		//Normal smile image
		NORMAL	(0.625f,	0.125f,
				 0.750f,	0.125f,
				 0.625f,	0.250f,
				 0.750f,	0.250f),
		
		//Smile image pressed
		PRESSED	(0.750f,	0.125f,
				 0.875f,	0.125f,
				 0.750f,	0.250f,
				 0.875f,	0.250f);
		
		/**
		 * Texture mapping array
		 */
		private float textureMapping[];
		
		/**
		 * Constructor
		 * @param args The texture mapping
		 */
		private ResetButtonState(float... args)
		{
			textureMapping = args.clone();
		}
		
		/**
		 * Texture mapping getter
		 * @return texture map
		 */
		public float[] getTextureMapping()
		{
			return textureMapping.clone();
		}
	}
	
	/**
	 * The top left corner X position
	 */
	private int x;
	
	/**
	 * The top letf corner Y position
	 */
	private int y;
	
	/**
	 * The X dimensions
	 */
	private int dimX;
	
	/**
	 * The Y dimensions
	 */
	private int dimY;
	
	/**
	 * Internal texture state
	 */
	private ResetButtonState state;
	
	/**
	 * Reset state
	 */
	private boolean reset;
	
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
	 * @param x The top left X coordinate
	 * @param y Tee top left y coordinate
	 * @param dimX The button X dimensions
	 * @param dimY The button Y dimensions
	 */
	public ResetButton(int x, int y, int dimX, int dimY)
	{
		//Assign the coordinates
		this.x = x;
		this.y = y;
		
		//Assign the dimensions with some care
		this.dimX = dimX > 0 ? dimX : 10;
		this.dimY = dimY > 0 ? dimY : 10;
		
		//Set the reset state to false
		reset = false;
		
		//By default the state is normal
		state = ResetButtonState.NORMAL;
		
		//Instance the vertex array
		vertices = new float[16]; // 4 vertices with 4 floats
		
		//Set the elements to the standard drawing procedure
		elements = new int[] {0, 1, 3, 0, 3, 2};
	}
	
	/**
	 * Method to write the selected texture
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
	 * Method that handles the passed mouse event to decide if the
	 * event is a click or a release on this button
	 * @param event
	 */
	public void handleMouseEvent(MouseEvent event)
	{
		//If the event is null i do nothing
		if(event == null) { return; }
		
		//If it is a movement outside the box i put the state to normal
		if(event.getEventType() == MouseEventType.MOVE &&
		   !isInside((int)event.getPosX(), (int)event.getPosY()))
		{
			//Change texture state
			state = ResetButtonState.NORMAL;
			//Force the update
			updated = true;
			return;
		}
		
		//If it is a left click inside the box i set the state to pressed
		if(event.getEventType() == MouseEventType.LEFT_CLICK &&
		   isInside((int)event.getPosX(), (int)event.getPosY()))
		{
			//Change texture state
			state = ResetButtonState.PRESSED;
			//Force the reset state
			reset = true;
			//Force the update
			updated = true;
			return;
		}
		
		//If it is a release click i put the state to normal
		if(event.getEventType() == MouseEventType.LEFT_RELEASE ||
		   event.getEventType() == MouseEventType.CENTER_RELEASE ||
		   event.getEventType() == MouseEventType.RIGHT_RELEASE)
		{
			//Change texture state
			state = ResetButtonState.NORMAL;
			//Force the update
			updated = true;
			return;
		}
	}
	
	/**
	 * Method that checks if the position passed is inside the reset button
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
				//It is inside the reset button
				return true;
			}
		}
		//It's not inside the reset button
		return false;
	}
	
	/**
	 * Method that returns the need to reset (the button has been pressed).
	 * After that, the reset state is false.
	 * @return Boolean that indicates the need of reset
	 */
	public boolean shouldReset()
	{
		//If it's true i set it to false and then return
		if(reset)
		{
			reset = false;
			return true;
		}
		return false;
	}
	
	@Override
	public float[] getVertices() 
	{
		//I set up the selected texture
		setupTexture();
		
		//Return a vertex clone
		return vertices.clone();
	}

	@Override
	public int getVerticesSize() { return vertices.length; }

	@Override
	public int[] getElements() { return elements.clone(); }

	@Override
	public int getElementsSize() { return elements.length; }
}
