package game.minesweeper.game;

/**
 * Button state enumeration with texture atlas coordinates
 * @author Matteo Pignataro
 */
public enum CellStates
{
	//Cell to click state
	CELL_HIDDEN	(0,			0,
				 0.125f,	0,
				 0,			0.125f,
				 0.125f,	0.125f),
	
	//No cell state (no number and no bomb)
	CELL_NONE 	(0.125f,	0,
				 0.250f,	0,
				 0.125f,	0.125f,
				 0.250f,	0.125f),
	
	//Number cell 1
	CELL_1		(0.250f,	0,
				 0.375f,	0,
				 0.250f,	0.125f,
				 0.375f,	0.125f),
	
	//Number cell 2
	CELL_2		(0.375f,	0,
				 0.500f,	0,
				 0.375f,	0.125f,
				 0.500f,	0.125f),
	
	//Number cell 3
	CELL_3		(0.500f,	0,
				 0.625f,	0,
				 0.500f,	0.125f,
				 0.625f,	0.125f),
	
	//Number cell 4
	CELL_4		(0.625f,	0,
				 0.750f,	0,
				 0.625f,	0.125f,
				 0.750f,	0.125f),
	
	//Number cell 5
	CELL_5		(0.750f,	0,
				 0.875f,	0,
				 0.750f,	0.125f,
				 0.875f,	0.125f),
	
	//Number cell 6
	CELL_6		(0.875f,	0,
				 1,			0,
				 0.875f,	0.125f,
				 1,			0.125f),
	
	//Number cell 7
	CELL_7		(0,			0.125f,
				 0.125f,	0.125f,
				 0,			0.250f,
				 0.125f,	0.250f),
	
	//Number cell 8
	CELL_8		(0.125f,	0.125f,
				 0.250f,	0.125f,
				 0.125f,	0.250f,
				 0.250f,	0.250f),
	
	//Bomb cell
	BOMB		(0.250f,	0.125f,
				 0.375f,	0.125f,
				 0.250f,	0.250f,
				 0.375f,	0.250f),
	
	//Red bomb cell
	BOMB_RED	(0.375f,	0.125f,
				 0.500f,	0.125f,
				 0.375f,	0.250f,
				 0.500f,	0.250f),
	
	//Flag cell
	FLAG		(0.500f,	0.125f,
				 0.625f,	0.125f,
				 0.500f,	0.250f,
				 0.625f,	0.250f);
	
	/**
	 * Texture mapping
	 */
	private float textureMapping[];
	
	/**
	 * Constructor
	 * @param args float texture mapping
	 */
	private CellStates(float... args)
	{
		//If and only if it is a 2D texture mapping for 4 vertices i accept
		if(args.length == 8)
		{
			textureMapping = args.clone();
		}
		else
		{
			//0 float array
			textureMapping = new float[] {0, 0, 0, 0, 0, 0, 0, 0};
		}
	}
	
	/**
	 * Texture mapping getter
	 * @return the texture map vertices
	 */
	public float[] getTextureMapping() { return textureMapping.clone(); }
}
