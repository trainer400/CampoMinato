package game.minesweeper.game;

import game.minesweeper.window.listener.MouseEvent;
import game.minesweeper.window.listener.MouseEvent.MouseEventType;

/**
 * This class represents the cell matrix in the game
 * @author Matteo Pignataro
 */
public class CellTable 
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
	 * Cell matrix
	 */
	private Cell[][] table;
	
	/**
	 * Constructor
	 * @param x The x position of the top left corner
	 * @param y The y position of the top left corner
	 * @param width The width in cells
	 * @param height The height in cells
	 * @param size The size in pixel of every single cell
	 */
	public CellTable(int x, int y, int width, int height, int size)
	{
		//Assign all the variables
		this.x = x;
		this.y = y;
		
		//Instance the matrix (in case of negative or 0 width or height i use the value 10)
		table = new Cell[width > 0 ? width : 10][height > 0 ? height : 10];
		
		//Instance all the table cells
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				table[i][j] = new Cell(x + i * size, y + j * size, size, size, CellStates.CELL_1);
			}
		}
	}
	
	/**
	 * It handles an event (after checking that it differs from null and is a left click).
	 * @param event Mouse event that determines what cell has been clicked
	 */
	public void handleMouseEvent(MouseEvent event)
	{
		//If the event is null or is different from a left click i do nothing
		if(event == null || event.getEventType() != MouseEventType.LEFT_CLICK) { return; }
		
		//Check if the event is about one of the cells
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//Check if the mouse position is inside the cell
				if(table[i][j].isInside((int)event.getPosX(), (int)event.getPosY()))
				{
					//We swap the cell state to it's real state
					table[i][j].setState(table[i][j].getRealState());
				}
			}
		}
	}
	
	/**
	 * @return A copy of the table
	 */
	public Cell[][] getTable()
	{
		//create a copy table
		Cell[][] copy;
		
		//Instance only the first dimension
		copy = new Cell[table.length][];
		for(int i = 0; i < copy.length; i++)
		{
			copy[i] = table[i].clone();
		}
		
		//Return the table
		return copy;
	}
	
	//Simple getters
	public int getPosX() { return x; }
	public int getPosY() { return y; }
}
