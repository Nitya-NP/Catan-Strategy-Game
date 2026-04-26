
/**
 * Represents a City building in the game.
 * 
 * A City is type of building that provides 2 victory points to its owner.
 *
 *  @author Krisha Patel
 */
public class City extends Building {
	/**
	 * 
	 */
	private int points;

	/**
	 * Constructs a City owned by a player and sets points to 2
	 * @param owner - the player which owns the city
	 */
	public City(Player owner) {
		super(owner);
		points = 2;
	}
	
	/**
	 * Returns the points
	 * @return points - each city is worth 2 victory points
	 */
	@Override
	public int getPoints() {
		return points;
	}

}
