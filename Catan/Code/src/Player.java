import java.util.HashMap;
import java.util.Map;

/**
 * The Player classs represents an player participating in Catan simulation.
 * A Player owns resources, buildings, and winning points, and can take a turn.
 * It track resources, track buildings owned, track points and exceute a random
 * action during a turn
 * 
 * @author Nitya Patel
 */
public abstract class Player {

	private int playerId; // Unique identifier of the player
	private int points; // Current points
	private Map<Resources, Integer> resources; // Stores the number of resources player owns
	private Map<Class<? extends Building>, Integer> buildings; // Stores buildings owned by the player (Settlement,
																// Cities)
	private int roads;

	/**
	 * Constructs a new Player with empty resources and buildings
	 * 
	 * @param playerId the player's unique ID
	 */
	protected Player(int playerId) {
		this.playerId = playerId;
		this.points = 0;
		this.resources = new HashMap<>();
		this.buildings = new HashMap<>();
		initResources();
		initBuildings();
	}

	/**
	 * Initializes all resources to 0 at the start
	 */
	private void initResources() {
		for (Resources r : Resources.values()) {
			this.resources.put(r, 0);
		}
	}

	/**
	 * Initializes building (Road, City, Settlement) counts to 0 at the start
	 */
	private void initBuildings() {
		this.roads = 0;
		buildings.put(Settlement.class, 0);
		buildings.put(City.class, 0);

	}

	/**
	 * @return the player's id
	 */
	public int getPlayerId() {
		return this.playerId;
	}

	/**
	 * @return the player's points
	 */
	public int getPoints() {
		return this.points;
	}

	/**
	 * Adds points to the player
	 * 
	 * @param points number of points to add
	 */
	public void addPoints(int points) {
		this.points += points;
	}

	/**
	 * @return total number of resources the player owns
	 */
	public int getTotalResources() {
		int total = 0;
		for (Integer amount : resources.values()) {
			total += amount;
		}
		return total;
	}

	/**
	 * Adds specific resources to the player
	 * 
	 * @param resource new resource to add
	 * @param amount   the number of units to add
	 */
	public void addResource(Resources resource, int amount) {
		resources.put(resource, resources.getOrDefault(resource, 0) + amount);
	}

	/**
	 * Adds a building to the player based on its class name
	 * Also updates points where applicable
	 * 
	 * @param building
	 */
	public void addBuilding(Building building) {
		Class<? extends Building> type = building.getClass();
		buildings.put(type, buildings.getOrDefault(type, 0) + 1);

		// Add points according to rules
		if (building instanceof Settlement) {
			addPoints(1);
		} else if (building instanceof City) {
			addPoints(2);
		}
	}

	/**
	 * Removes a building to the player based on its class name
	 * Also updates points where applicable
	 * 
	 * @param building
	 */
	public void removeBuilding(Building building) {
		Class<? extends Building> type = building.getClass();
	
		int current = buildings.getOrDefault(type, 0);
	
		if (current > 0) {
			buildings.put(type, current - 1);
		}
	
		// Adjust points
		if (building instanceof Settlement) {
			this.points -= 1;
		} else if (building instanceof City) {
			this.points -= 2;
		}
	}

	/**
	 * Adds a road to player count
	 */
	public void addRoad() {
		this.roads++;
	}

	/**
	 * Removes a road to player count
	 */
	public void removeRoad() {
		this.roads--;
	}

	/**
	 * Removes a specific amount of a resource from the player
	 * 
	 * @param resource the resource type
	 * @param amount   the number to remove
	 * @return true if removal was successful, false if not enough resources
	 */
	public boolean removeResource(Resources resource, int amount) {
		int current = resources.getOrDefault(resource, 0);

		if (current < amount) {
			return false; // not enough resources
		}

		resources.put(resource, current - amount);
		return true;
	}

	/**
	 * Checks whether the player has atleast a specified amount of resources to
	 * build something
	 * 
	 * @param r      the resource type to check
	 * @param amount minimum amount required
	 * @return true if player has enough, false otherwise
	 */
	public boolean hasResources(Resources r, int amount) {
		return resources.getOrDefault(r, 0) >= amount;
	}

	/**
	 * Displays all resources currently owned by the player
	 */
	public String listResources() {
		StringBuilder sb = new StringBuilder();
		// Iterate over ALL resource types to ensure they are shown
		for (Resources r : Resources.values()) {
			int amount = resources.getOrDefault(r, 0);
			sb.append(r).append(": ").append(amount).append(", ");
		}

		// Remove the last comma and space
		if (sb.length() > 2) {
			sb.setLength(sb.length() - 2);
		}

		return sb.toString();
	}

	// Abstract method to be implemented by Human & Computer
	public abstract PlayerCommand takeTurn();

}
