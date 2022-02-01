package game.minesweeper.game.objects;

/**
 * NumberDisplay states: number texture mappings
 * @author Matteo Pignataro
 */
public enum NumberDisplayState 
{
	NUMBER_0 (0,	0,		0.250f,
			  		0.125f,	0.250f,
			  		0,		0.500f,
			  		0.125f,	0.500f),
	
	NUMBER_1 (1,	0.125f,	0.250f,
					0.250f,	0.250f,
					0.125f,	0.500f,
					0.250f,	0.500f),
	
	NUMBER_2 (2,	0.250f,	0.250f,
					0.375f,	0.250f,
					0.250f,	0.500f,
					0.375f,	0.500f),
	
	NUMBER_3 (3,	0.375f,	0.250f,
			  		0.500f,	0.250f,
			  		0.375f,	0.500f,
			  		0.500f,	0.500f),
	
	NUMBER_4 (4,	0.500f,	0.250f,
			  		0.625f,	0.250f,
			  		0.500f,	0.500f,
			  		0.625f,	0.500f),
	
	NUMBER_5 (5, 	0.625f,	0.250f,
			  		0.750f,	0.250f,
			  		0.625f,	0.500f,
			  		0.750f,	0.500f),
	
	NUMBER_6 (6,	0.750f,	0.250f,
			  		0.875f,	0.250f,
			  		0.750f,	0.500f,
			  		0.875f,	0.500f),
	
	NUMBER_7 (7,	0.875f,	0.250f,
			  		1,		0.250f,
			  		0.875f,	0.500f,
			  		1,		0.500f),
	
	NUMBER_8 (8,	0,		0.500f,
			  		0.125f,	0.500f,
			  		0,		0.750f,
			  		0.125f,	0.750f),
	
	NUMBER_9 (9,	0.125f,	0.500f,
			  		0.250f,	0.500f,
			  		0.125f,	0.750f,
			  		0.250f,	0.750f);
	
	/**
	 * Number being mapped
	 */
	public final int NUMBER;
	
	/**
	 * Texture mapping
	 */
	private float textureMapping[];
	
	/**
	 * Constructor
	 * @param args float texture mapping
	 */
	private NumberDisplayState(int number, float... args) { textureMapping = args.clone(); this.NUMBER = number;}
	
	/**
	 * Texture mapping getter
	 * @return the texture map vertices
	 */
	public float[] getTextureMapping() { return textureMapping.clone(); }
}
