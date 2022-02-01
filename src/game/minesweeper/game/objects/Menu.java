package game.minesweeper.game.objects;

import game.minesweeper.render.DrawableElement;
import game.minesweeper.window.listener.MouseEvent;

/**
 * The upper menu which can reset the game and visualize
 * the time passed and the rimaining mines.
 * @author Matteo Pignataro
 */
public class Menu 
{
	/**
	 * The seconds passed
	 */
	private int time;
	
	/**
	 * The last tick timestap 
	 */
	private long lastTick;

	/**
	 * Reset button
	 */
	private ResetButton reset;
	
	/**
	 * 3 mines displays
	 */
	private NumberDisplay mine1;
	private NumberDisplay mine2;
	private NumberDisplay mine3;
	
	/**
	 * 3 timer displays
	 */
	private NumberDisplay time1;
	private NumberDisplay time2;
	private NumberDisplay time3;
	
	/**
	 * Constructor
	 * @param x The top left corner X coordinate
	 * @param y The top left corner Y coordinate
	 * @param width the pixel width
	 * @param height the pixel height
	 * @param cellSize the sie in pixel of a cell
	 */
	public Menu(int x, int y, int width, int height, int cellSize)
	{
		//I set the timer to 0
		time = 0;
		lastTick = System.currentTimeMillis();
		
		//Instanciate mine counters
		mine1 = new NumberDisplay(x + width / 4	- cellSize		, y + cellSize / 2, cellSize, cellSize * 2);
		mine2 = new NumberDisplay(x + width / 4 - 2 * cellSize	, y + cellSize / 2, cellSize, cellSize * 2);
		mine3 = new NumberDisplay(x + width / 4 - 3 * cellSize	, y + cellSize / 2, cellSize, cellSize * 2);
		
		//Instanciate time counters
		time1 = new NumberDisplay(x + (3 * width) / 4 + 2 * cellSize	, y + cellSize / 2, cellSize, cellSize * 2);
		time2 = new NumberDisplay(x + (3 * width) / 4 + cellSize		, y + cellSize / 2, cellSize, cellSize * 2);
		time3 = new NumberDisplay(x + (3 * width) / 4					, y + cellSize / 2, cellSize, cellSize * 2);
		
		//Instanciate the reset button
		reset = new ResetButton(x + width / 2 - cellSize, cellSize / 2, cellSize * 2, cellSize * 2); 
	}
	
	/**
	 * Method to handle the mouse event (for reset button)
	 * @param event The mouse event
	 */
	public void handleMouseEvent(MouseEvent event)
	{
		//I pass it to the reset button
		reset.handleMouseEvent(event);
	}
	
	/**
	 * Method to show a number with the mine dissplay
	 * @param number The number to be shown
	 */
	public void showNumber(int number)
	{
		if(number < 0 || number >= 1000)
		{
			//I can do nothing
			return;
		}
		
		//Set the first value to the rest of the division by 10
		mine1.setState(NumberDisplayState.values()[number % 10]);
		
		//Divide the number by 10
		number = number / 10;
		
		//Set the second value to the rest of the division by 10
		mine2.setState(NumberDisplayState.values()[number % 10]);
		
		//Divide the number by 10
		number = number / 10;
		
		//Set the third value to the rest of the division by 10
		mine3.setState(NumberDisplayState.values()[number % 10]);
	}
	
	/**
	 * Method to update the timer on the right
	 */
	public void tick()
	{
		//If a second passed i have to update the time
		if(System.currentTimeMillis() - lastTick >= 1000)
		{
			time++;
			//Update the last tick
			lastTick = System.currentTimeMillis();
			
			//Visualize the number
			int temp = time;
			
			//Set the first value to the rest of the division by 10
			time1.setState(NumberDisplayState.values()[temp % 10]);
			
			//Divide the number by 10
			temp = temp / 10;
			
			//Set the second value to the rest of the division by 10
			time2.setState(NumberDisplayState.values()[temp % 10]);
			
			//Divide the number by 10
			temp = temp / 10;
			
			//Set the third value to the rest of the division by 10
			time3.setState(NumberDisplayState.values()[temp % 10]);
		}
	}
	
	/**
	 * Method to reset all the numbers indicators
	 */
	public void resetCounts()
	{
		showNumber(0);
		time = -1; //So the next thick is 0
		lastTick = 0; //So the time has already passed by
	}
	
	/**
	 * This method asks the reset button if we should reset the game
	 * @return The result of reset button
	 */
	public boolean shouldReset()
	{
		return reset.shouldReset();
	}
	
	/**
	 * Method that returns an array with all the drawable instances
	 * @return The drawableElement array
	 */
	public DrawableElement[] getDrawableElements()
	{
		return new DrawableElement[] {mine1, mine2, mine3, time1, time2, time3, reset};
	}
}
