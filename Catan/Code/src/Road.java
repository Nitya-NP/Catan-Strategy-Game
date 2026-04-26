/**
 * The road class represents a road build by a player that is connected to two nodes on the board.
 * 
 * @author Raadhikka Gupta, Nitya Patel
 */
public class Road {
	/**
	 * The two nodes connected to the road.
	 */
	private Node[] connectedNodes;

	/**
	 * Owner of that building
	 */
	private Player owner;

	/**
	 * Constructs a Road connecting to two nodes and owned by a player.
	 * 
	 * @param nodes the nodes connected
	 * @param owner the player who owns the road
	 */
	public Road(Node[] nodes, Player owner) {
		this.connectedNodes = new Node[2];
		this.connectedNodes[0] = nodes[0];
		this.connectedNodes[1] = nodes[1];
		this.owner = owner;
	}

	/**
	 * Checks whether the road is connected to the node
	 * 
	 * @param node the nodes to check
	 * @return true if the node is connected, otherwise false
	 */
	public boolean isConnected(Node node) {
		return connectedNodes[0] == node || connectedNodes[1] == node; 
	}

	/**
	 * Returns the nodes connected by the road
	 * 
	 * @return the two nodes connected
	 */
	public Node[] getConnectedNodes() {
		return connectedNodes;
	}

	/**
	 * 
	 * @return owner - the player who owns the building
	 */
	public Player getOwner() {
		return owner;
	}
}
