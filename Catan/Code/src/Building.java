/**
 * Abstract class representing a Building in the game.
 * 
 * Building serves as the foundation for all types of buildings 
 * such as Settlements and Cities. Each building is owned by a Player
 * and has a specific point value associated with it.
 *
 *  @author Krisha Patel, Nitya Patel
 */
public abstract class Building {
	/**
	 * Owner of that building
	 */
	private Player owner;
	
	/**
	 * 
	 * @param owner - the player who owns the building
	 */
	protected Building(Player owner) {
		this.owner = owner;
	}

	/**
	 * 
	 * @return owner - the player who owns the building
	 */
	public Player getOwner() {
		return owner;
	}
	
	/**
	 * Returns the points
	 * @return points - each city is worth 2 victory points
	 */
	public abstract int getPoints();
}
