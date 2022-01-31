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
	 * Cell size
	 */
	private int cellSize;
	
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
		this.cellSize = size;
		
		//Instance the matrix (in case of negative or 0 width or height i use the value 10)
		table = new Cell[width > 0 ? width : 10][height > 0 ? height : 10];
		
		//Init properly the table
		initTable();
	}
	
	/**
	 * Method to initialize randomly the table
	 * @param size The cells size
	 */
	private void initTable()
	{
		//Randomly assign the bombs
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//Percentage of bombs
				if(Math.random() > 0.9)
				{
					table[i][j] = new Cell(x + i * cellSize, y + j * cellSize, cellSize, cellSize, CellStates.BOMB);
				}
				else 
				{
					//Otherwise it is a void cell for now
					table[i][j] = new Cell(x + i * cellSize, y + j * cellSize, cellSize, cellSize, CellStates.CELL_NONE);
				}
			}
		}
		
		//After all the bombs have been assigned i can calculate the numbers
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//If this cell is not a bomb
				if(table[i][j].getRealState() != CellStates.BOMB)
				{
					//Count how many bombs are near this cell
					int count = 0;
					
					//Top left cell
					if(i - 1 >= 0 && j - 1 >= 0 && table[i - 1][j - 1].getRealState() == CellStates.BOMB) count++;
					//Middle left cell
					if(i - 1 >= 0 && table[i - 1][j].getRealState() == CellStates.BOMB) count++;
					//Bottom left cell
					if(i - 1 >= 0 && j + 1 < table[0].length && table[i - 1][j + 1].getRealState() == CellStates.BOMB) count++;
					//Top middle cell
					if(j - 1 >= 0 && table[i][j - 1].getRealState() == CellStates.BOMB) count++;
					//Top right cell
					if(i + 1 < table.length && j - 1 >= 0 && table[i + 1][j - 1].getRealState() == CellStates.BOMB) count++;
					//Middle right cell
					if(i + 1 < table.length && table[i + 1][j].getRealState() == CellStates.BOMB) count++;
					//Bottom right cell
					if(i + 1 < table.length && j + 1 < table[0].length && table[i + 1][j + 1].getRealState() == CellStates.BOMB) count++;
					//Bottom middle cell
					if(j + 1 < table[0].length && table[i][j + 1].getRealState() == CellStates.BOMB) count++;
					
					//Only if the count differs from 0 i change the texture
					if(count > 0)
					{
						table[i][j].setRealState(CellStates.values()[1 + count]);
					}
				}
			}
		}
	}
	
	/**
	 * It handles an event (after checking that it differs from null and is a left click).
	 * @param event Mouse event that determines what cell has been clicked
	 */
	public void handleMouseEvent(MouseEvent event)
	{
		//If the event is null
		if(event == null) { return; }
		
		//Check if the event is about one of the cells
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//Check if the mouse position is inside the cell and if the cell has not already been discovered
				if(table[i][j].isInside((int)event.getPosX(), (int)event.getPosY()) && (table[i][j].getState() == CellStates.CELL_HIDDEN
																						|| table[i][j].getState() == CellStates.FLAG))
				{
					//I check if it is a center/right clic
					if(event.getEventType() != MouseEventType.LEFT_CLICK)
					{
						//I place or remove the flag
						if(table[i][j].getState() == CellStates.FLAG)
						{
							//If it is a flag i remove it
							table[i][j].setState(CellStates.CELL_HIDDEN);
						}
						else
						{
							//I put a flag
							table[i][j].setState(CellStates.FLAG);
						}
						//Then i terminate
						return;
					}
					
					//If this is a blank cell i swap the state with the real state and i call an event for all
					//the nearest cells. It needs to not be a flag
					if(table[i][j].getRealState() == CellStates.CELL_NONE && table[i][j].getState() != CellStates.FLAG)
					{
						//Swap the states
						table[i][j].setState(table[i][j].getRealState());
						
						//Generate the new events and call this method recursively
						//Top left corner
						handleMouseEvent(new MouseEvent(event.getPosX() - cellSize, 
														event.getPosY() - cellSize,
														MouseEventType.LEFT_CLICK));
						
						//Top middle
						handleMouseEvent(new MouseEvent(event.getPosX(), 
														event.getPosY() - cellSize,
														MouseEventType.LEFT_CLICK));
						
						//Top right corner
						handleMouseEvent(new MouseEvent(event.getPosX() + cellSize, 
														event.getPosY() - cellSize,
														MouseEventType.LEFT_CLICK));
						
						//Middle left
						handleMouseEvent(new MouseEvent(event.getPosX() - cellSize, 
														event.getPosY(),
														MouseEventType.LEFT_CLICK));
						
						//Bottom left corner
						handleMouseEvent(new MouseEvent(event.getPosX() - cellSize, 
														event.getPosY() + cellSize,
														MouseEventType.LEFT_CLICK));
						
						//Bottom middle
						handleMouseEvent(new MouseEvent(event.getPosX(), 
														event.getPosY() + cellSize,
														MouseEventType.LEFT_CLICK));
						
						//Bottom right
						handleMouseEvent(new MouseEvent(event.getPosX() + cellSize, 
														event.getPosY() + cellSize,
														MouseEventType.LEFT_CLICK));
						
						//Middle right
						handleMouseEvent(new MouseEvent(event.getPosX() + cellSize, 
														event.getPosY(),
														MouseEventType.LEFT_CLICK));
					}
					else if(table[i][j].getState() != CellStates.FLAG)
					{
						//If i discover a bomb
						if(table[i][j].getRealState() == CellStates.BOMB)
						{
							//TODO Game ends
							
							//Set the state to red bomb
							table[i][j].setState(CellStates.BOMB_RED);
						}
						else
						{
							//Swap the states
							table[i][j].setState(table[i][j].getRealState());
						}
					}
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
