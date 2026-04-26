/**
 * A Tile class that represents a tile on the board. 
 * It has a resource, an ID, and a token number. 
 * Tile implements DiceRollObserver so it can react to dice rolls. 
 * 
 * @author Ranica Chawla, Nitya Patel
 */
public class Tile implements DiceRollObserver{
	// Private attributes of the tile class
	private int tileID;
	private int token;
	private Resources resource;
	private Node[] nodes;
	private RobberActionsManager robber;  //To check robber positions
	private GameLogger logger;

	/**
	 * Initializes a tile with the given resource, ID, and token number
	 * @param resource The resource that the tile holds
	 * @param id The tile's ID number
	 * @param token The token number that is associated with the tile
	 */
	public Tile(Resources resource, int id, int token) {
		// Initialize private attributes with the given parameters
		this.resource = resource;
		this.tileID = id;
		this.token = token;
		nodes = new Node[6];
	}

	/**
	 * @return the resource that the tile holds
	 */
	public Resources getResource() {
		return this.resource;
	}

	/**
	 * @return the tile's ID number
	 */
	public int getTileId() {
		return this.tileID;
	}

	/**
	 * @return the token number that is associated with the tile
	 */
	public int getToken() {
		return this.token;
	}

	/**
	 * @return the nodes that are adjacent to the tile
	 */
	public Node[] getNodes() {
		return this.nodes;
	}

	/**
	 * Sets the nodes that are adjacent to the tile
	 * @param nodes An array of nodes that are adjacent to the tile
	 */
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	@Override
	public void onDiceRolled(int diceValue) {

		if(this.token!=diceValue){
			//System.out.println("Tile "+tileID +" skipped - token "+ token + " != " +diceValue);
			return; 
		}

		if(shouldSkipTile(diceValue)) return;

		if(resource == Resources.NOTHING) {
            //System.out.println("Tile " + tileID + " is desert - no resources");
            return;
        }

		//Distribute resources
        //System.out.println("Tile " + tileID + " producing " + resource);
        distributeResources();
	}

	/**
     * Determines if a Tile should be skipped during resoruce production
	 * 
	 * @param diceValue The dice value rolled
	 * @return true if tile should be skipped
     */
    private boolean shouldSkipTile(int diceValue) {
        return (robber != null && robber.isRobberOnTile(this)) || this.token!=diceValue;
    }

	/**
     * Gives resources to the player owning the building on the node
     * settlements produce 1 resource, cities produce 2.
     * 
     * @param n Node the node with a building
     */
    private void distributeResourceToPlayer(Node n) {
        Player owner = n.getBuilding().getOwner();
        Building b = n.getBuilding();

        int amount = (b instanceof City) ? 2 : 1;
        owner.addResource(resource, amount);

        logger.log(owner.getPlayerId(), "gained " + amount + " " + resource + " from Tile " + n.getNodeId());
    }

	/**
     *  Distributes resources to all players with buildings on this tile
     */
    private void distributeResources() {
		if(resource == Resources.NOTHING){
			return;  //Desert produces nothing
		}
        for (Node n : nodes) {
            if (n!=null && n.isOccupied()) {
                distributeResourceToPlayer(n);
            }
        }
    }

	// Setter methods for robber
    public void setRobberManager(RobberActionsManager robber) {
        this.robber = robber;
    }

	//Setter mehtods for logger
    public void setLogger(GameLogger logger) {
        this.logger = logger;
    }

	
}
