/**
 * Represents a Settlement building in the game.
 * 
 * A Settlement is type of building that provides 1 victory point to its owner.
 *
 *  @author Krisha Patel
 */
public class Settlement extends Building {
	/**
	 * 
	 */
	private int points;

	
	/**
	 * Constructs a Settlement owned by a Player and sets points to 1
	 * @param owner - the player who owns the settlement
	 */
	public Settlement(Player owner) {
		super(owner);
		this.points = 1;
	}
	
	/**
	 * Returns the points
	 * @return points - each settlement is worth 1 victory point
	 */
	@Override
	public int getPoints() {
		return points;
	}

}
