import java.util.ArrayList;
import java.util.List;
/**
 * The Node class represents a node on the board. 
 * It has an ID and can hold a building.
 * @author Ranica Chawla, Nitya Patel
 */
public class Node {
	// Private attributes of the node class
	private int nodeId;
	private Building building;
	private List<Node> adjacentNodes;

	/**
	 * Constructs a new Node with the given ID
	 * @param id The ID of the node
	 */
	public Node(int id) {
		this.nodeId = id;
		adjacentNodes = new ArrayList<>();
	}

	/**
	 * Checks if the node is alreadly occupied by a building
	 * @return true if the node is occupied by a building, false otherwise
	 */
	public boolean isOccupied() {
		return (building != null);
	}

	/**
	 * @return the current building on the node, or null if there is no building
	 */
	public Building getBuilding() {
		return this.building;
	}

	/**
	 * @return the ID of the node
	 */
	public int getNodeId() {
		return this.nodeId;
	}

	/**
	 * Sets a building on the node
	 * @param building The building to be placed on the node
	 */
	public void setBuilding(Building building) {
		this.building = building;
	}

	
	/**
	 * Adds a negiboring node to this node's adhacency list
	 * @param node the node object to mark as adjacent
	 */
	public void addAdjacentNode(Node node){
		adjacentNodes.add(node);
	}

	/**
	 * @return a list of all nodes adjacent to this node. 
	 * Adjacent nodes are used to check settlement distance rules and road connections.
	 */
	public List<Node> getAdjacentNodes(){
		return adjacentNodes;
	}
}
