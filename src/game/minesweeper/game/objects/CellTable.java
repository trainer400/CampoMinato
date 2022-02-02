package game.minesweeper.game.objects;

import javax.swing.JOptionPane;

import game.minesweeper.window.listener.MouseEvent;
import game.minesweeper.window.listener.MouseEvent.MouseEventType;

/**
 * This class represents the cell matrix in the game
 * @author Matteo Pignataro
 */
public class CellTable 
{
	/**
	 * Enumerates the different game states
	 * @author Matteo Pignataro
	 */
	public static enum GameState
	{
		GAME_RUN, GAME_FAIL, GAME_SUCCESS, GAME_STOP;
	}
	
	/**
	 * X position
	 */
	private int x;
	
	/**
	 * Y position
	 */
	private int y;
	
	/**
	 * Game state
	 */
	private GameState state;
	
	/**
	 * Game difficulty (probability that the single cell is not a mine)
	 */
	private final float difficulty;
	
	/**
	 * Number of actual bombs
	 */
	private int bombNumber;
	
	/**
	 * Number of actual discovered bombs
	 */
	private int bombDiscovered;
	
	/**
	 * Number of placed flags
	 */
	private int flags;
	
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
	public CellTable(int x, int y, int width, int height, int size, float difficulty)
	{
		//Assign all the variables
		this.x = x;
		this.y = y;
		this.cellSize 	= size;
		this.difficulty = difficulty > 0 && difficulty < 1 ? difficulty : 0.8f;
		
		//Init all the counters to 0
		bombNumber		= 0;
		bombDiscovered 	= 0;
		flags 			= 0;
		
		//Set the game status to run
		state = GameState.GAME_RUN;
		
		//Instance the matrix (in case of negative or 0 width or height i use the value 10)
		table = new Cell[width > 0 ? width : 10][height > 0 ? height : 10];
		
		//Randomly assign the bombs
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//Percentage of bombs
				if(Math.random() > this.difficulty)
				{
					table[i][j] = new Cell(x + i * cellSize, y + j * cellSize, cellSize, cellSize, CellState.BOMB);
					//Increment the bombNumber
					bombNumber++;
				}
				else 
				{
					//Otherwise it is a void cell for now
					table[i][j] = new Cell(x + i * cellSize, y + j * cellSize, cellSize, cellSize, CellState.CELL_NONE);
				}
			}
		}
		
		//Init properly the table
		initTable();
	}
	
	/**
	 * Method to count the bombs and assign the numbers in the table
	 * @param size The cells size
	 */
	private void initTable()
	{	
		//After all the bombs have been assigned i can calculate the numbers
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//If this cell is not a bomb
				if(table[i][j].getRealState() != CellState.BOMB)
				{
					//Count how many bombs are near this cell
					int count = 0;
					
					//Top left cell
					if(i - 1 >= 0 && j - 1 >= 0 && table[i - 1][j - 1].getRealState() == CellState.BOMB) count++;
					//Middle left cell
					if(i - 1 >= 0 && table[i - 1][j].getRealState() == CellState.BOMB) count++;
					//Bottom left cell
					if(i - 1 >= 0 && j + 1 < table[0].length && table[i - 1][j + 1].getRealState() == CellState.BOMB) count++;
					//Top middle cell
					if(j - 1 >= 0 && table[i][j - 1].getRealState() == CellState.BOMB) count++;
					//Top right cell
					if(i + 1 < table.length && j - 1 >= 0 && table[i + 1][j - 1].getRealState() == CellState.BOMB) count++;
					//Middle right cell
					if(i + 1 < table.length && table[i + 1][j].getRealState() == CellState.BOMB) count++;
					//Bottom right cell
					if(i + 1 < table.length && j + 1 < table[0].length && table[i + 1][j + 1].getRealState() == CellState.BOMB) count++;
					//Bottom middle cell
					if(j + 1 < table[0].length && table[i][j + 1].getRealState() == CellState.BOMB) count++;
					
					//Only if the count differs from 0 i change the texture
					if(count > 0)
					{
						table[i][j].setRealState(CellState.values()[1 + count]);
					}
				}
			}
		}
	}
	
	/**
	 * Method to reset the table with new configuration
	 */
	public void resetTable()
	{
		//Reset all the states
		bombNumber		= 0;
		bombDiscovered 	= 0;
		flags 			= 0;
		
		//Set the game status to run
		state = GameState.GAME_RUN;
		
		//I select randomly the bomb pattern
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//Percentage of bombs
				if(Math.random() > difficulty)
				{
					table[i][j].setRealState(CellState.BOMB);
					table[i][j].setState(CellState.CELL_HIDDEN);
					//I count the bomb
					bombNumber++;
				}
				else 
				{
					//Otherwise it is a void cell for now
					table[i][j].setRealState(CellState.CELL_NONE);
					table[i][j].setState(CellState.CELL_HIDDEN);
				}
			}
		}
		
		//After i assign the bombs i can re-init the table
		initTable();
	}
	
	/**
	 * It handles an event (after checking that it differs from null and is a left click).
	 * @param event Mouse event that determines what cell has been clicked
	 */
	public void handleMouseEvent(MouseEvent event)
	{
		//If the game is in stop mode i do nothing
		if(state == GameState.GAME_STOP) { return; }
		
		//If the game is in the success mode i check if all the cells are not hidden
		if(state == GameState.GAME_SUCCESS)
		{
			//Set the flag to true
			boolean win = true;
			
			for(int i = 0; i < table.length; i++)
			{
				for(int j = 0; j < table[0].length; j++)
				{
					//If i find an hidden cell i cannot say that the player won
					if(table[i][j].getState() == CellState.CELL_HIDDEN)
					{
						win = false;
						break;
					}
				}
			}
			
			//If no hidden cells are found i message the player that he won
			if(win)
			{
				JOptionPane.showMessageDialog(null, "You won the game!", "You won!", JOptionPane.INFORMATION_MESSAGE);
				//Set the game to stop
				state = GameState.GAME_STOP;
			}
		}
		
		//If the state is fail i communicate the result and stop the game
		if(state == GameState.GAME_FAIL)
		{
			JOptionPane.showMessageDialog(null, "You lost the game..", "You lost", JOptionPane.ERROR_MESSAGE);
			//Set the state to stop
			state = GameState.GAME_STOP;
		}
		
		//If the event is null or if the event is different from the clicks
		if(event == null || (event.getEventType() != MouseEventType.RIGHT_CLICK &&
							 event.getEventType() != MouseEventType.LEFT_CLICK  &&
							 event.getEventType() != MouseEventType.CENTER_CLICK)) { return; }
		
		//Check if the event is about one of the cells
		for(int i = 0; i < table.length; i++)
		{
			for(int j = 0; j < table[0].length; j++)
			{
				//Check if the mouse position is inside the cell and if the cell has not already been discovered
				if(table[i][j].isInside((int)event.getPosX(), (int)event.getPosY()) && (table[i][j].getState() == CellState.CELL_HIDDEN
																						|| table[i][j].getState() == CellState.FLAG))
				{
					//I check if it is a center/right clic
					if(event.getEventType() != MouseEventType.LEFT_CLICK)
					{
						//I place or remove the flag
						if(table[i][j].getState() == CellState.FLAG)
						{
							//If it is a flag i remove it
							table[i][j].setState(CellState.CELL_HIDDEN);
							//Decrement the flags counter
							flags--;
							//Decrement the discovered bombs if it is a bomb
							bombDiscovered -= table[i][j].getRealState() == CellState.BOMB ? 1 : 0;
							
							//If the state was on success and i left behind a bomb i put it on run
							if(bombDiscovered < bombNumber)
							{
								state = GameState.GAME_RUN;
							}
						}
						else
						{
							//I put a flag
							table[i][j].setState(CellState.FLAG);
							//Increment the flags counter
							flags++;
							//If it is a bomb i increment also the discovered counter
							bombDiscovered += table[i][j].getRealState() == CellState.BOMB ? 1 : 0;
							
							//If the discovered bombs are the same number as the bombs i put the game state in success
							if(bombDiscovered == bombNumber)
							{
								state = GameState.GAME_SUCCESS;
							}
						}
						//Then i terminate
						return;
					}
					
					//If this is a blank cell i swap the state with the real state and i call an event for all
					//the nearest cells. It needs to not be a flag
					if(table[i][j].getRealState() == CellState.CELL_NONE && table[i][j].getState() != CellState.FLAG)
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
					else if(table[i][j].getState() != CellState.FLAG)
					{
						//If i discover a bomb
						if(table[i][j].getRealState() == CellState.BOMB)
						{
							//Set the state to red bomb
							table[i][j].setState(CellState.BOMB_RED);
							
							//Set the game state to fail
							state = GameState.GAME_FAIL;
						}
						else
						{
							//Swap the states
							table[i][j].setState(table[i][j].getRealState());
						}
					}
				}
				else if(table[i][j].isInside((int)event.getPosX(), (int)event.getPosY()) && table[i][j].getRealState() != CellState.BOMB &&
																							table[i][j].getRealState() != CellState.CELL_NONE)
				{
					//If the cell has already been discovered and is not a bomb
					//If the flags near it are the same number or above the real state i can discover all
					//the near hidden cells
					//Count how many flags are near this cell
					int count = 0;
					
					//Top left cell
					if(i - 1 >= 0 && j - 1 >= 0 && table[i - 1][j - 1].getState() == CellState.FLAG) count++;
					//Middle left cell
					if(i - 1 >= 0 && table[i - 1][j].getState() == CellState.FLAG) count++;
					//Bottom left cell
					if(i - 1 >= 0 && j + 1 < table[0].length && table[i - 1][j + 1].getState() == CellState.FLAG) count++;
					//Top middle cell
					if(j - 1 >= 0 && table[i][j - 1].getState() == CellState.FLAG) count++;
					//Top right cell
					if(i + 1 < table.length && j - 1 >= 0 && table[i + 1][j - 1].getState() == CellState.FLAG) count++;
					//Middle right cell
					if(i + 1 < table.length && table[i + 1][j].getState() == CellState.FLAG) count++;
					//Bottom right cell
					if(i + 1 < table.length && j + 1 < table[0].length && table[i + 1][j + 1].getState() == CellState.FLAG) count++;
					//Bottom middle cell
					if(j + 1 < table[0].length && table[i][j + 1].getState() == CellState.FLAG) count++;
					
					//If the counted flags are more than the reported number i discover all the near ones
					if(count < table[i][j].getState().NUMBER)
					{
						return;
					}
					
					//Top left cell
					if(i - 1 >= 0 && j - 1 >= 0 && table[i - 1][j - 1].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX() - cellSize, 
														event.getPosY() - cellSize,
														MouseEventType.LEFT_CLICK));
					//Middle left cell
					if(i - 1 >= 0 && table[i - 1][j].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX() - cellSize, 
														event.getPosY(),
														MouseEventType.LEFT_CLICK));
					//Bottom left cell
					if(i - 1 >= 0 && j + 1 < table[0].length && table[i - 1][j + 1].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX() - cellSize, 
														event.getPosY() + cellSize,
														MouseEventType.LEFT_CLICK));
					//Top middle cell
					if(j - 1 >= 0 && table[i][j - 1].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX(), 
														event.getPosY() - cellSize,
														MouseEventType.LEFT_CLICK));
					//Top right cell
					if(i + 1 < table.length && j - 1 >= 0 && table[i + 1][j - 1].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX() + cellSize, 
														event.getPosY() - cellSize,
														MouseEventType.LEFT_CLICK));
					//Middle right cell
					if(i + 1 < table.length && table[i + 1][j].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX() + cellSize, 
														event.getPosY(),
														MouseEventType.LEFT_CLICK));
					//Bottom right cell
					if(i + 1 < table.length && j + 1 < table[0].length && table[i + 1][j + 1].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX() + cellSize, 
														event.getPosY() + cellSize,
														MouseEventType.LEFT_CLICK));
					//Bottom middle cell
					if(j + 1 < table[0].length && table[i][j + 1].getState() == CellState.CELL_HIDDEN)
						handleMouseEvent(new MouseEvent(event.getPosX(), 
														event.getPosY() + cellSize,
														MouseEventType.LEFT_CLICK));
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
	public GameState getGameState() { return state; }
	public int getBombsNumber() { return bombNumber; }
	public int getFlagsNumber() { return flags; }
}
