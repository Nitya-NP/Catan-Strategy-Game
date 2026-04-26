
import java.util.ArrayList;
import java.util.List;

/**
 * Evaluates all possible actions for the AI and assigns values based on rules
 * Values: VP actions = 1.0, Non-VP building = 0.8, Spending = 0.5
 * 
 * @author Krisha Patel
 */
public class ActionEvaluator {

    private RuleBasedAI player;
    private Board board;

    /**
     * Constructs an ActionEvaluator for the given AI player
     * 
     * @param player the AI player for which to evaluate actions
    */
    public ActionEvaluator(RuleBasedAI player) {
        this.player = player;
    }

    /**
     * Sets the board reference for evaluating actions
     * 
     * @param board the game board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Evaluates all possible actions and returns a list with their assigned values.
     * Checks for buildable settlements, cities, and roads.
     * Applies differnt values based on action type and resource situation.
     * 
     * @return List of AIAction objects representing possible actions and their values
     */
    public List<AIAction> evaluate() {
        List<AIAction> actions = new ArrayList<>();

        // Check for builable settlements 
        if (player.hasResources(Resources.BRICK, 1)
                && player.hasResources(Resources.LUMBER, 1)
                && player.hasResources(Resources.WOOL, 1)
                && player.hasResources(Resources.GRAIN, 1)) {

            for (Node node : board.getNode()) {
                if (!node.isOccupied()) {
                    boolean valid = true;
                    for (Node adj : node.getAdjacentNodes()) {
                        if (adj.isOccupied()) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        actions.add(new AIAction(UserInput.BUILD_SETTLEMENT, node.getNodeId(), 0, 1.0));
                    }
                }
            }
        }

        // Check for buildable cities
        if (player.hasResources(Resources.ORE, 3) && player.hasResources(Resources.GRAIN, 2)) {
            for (Node node : board.getNode()) {
                if (node.isOccupied() && node.getBuilding() instanceof Settlement && node.getBuilding().getOwner() == player) {
                    actions.add(new AIAction(UserInput.BUILD_CITY, node.getNodeId(), 0, 1.0));
                }
            }
        }

        // Check for buildable roads
        if (player.hasResources(Resources.BRICK, 1) && player.hasResources(Resources.LUMBER, 1)) {
            for (int[] road : getBuildableRoads()) {
                actions.add(new AIAction(UserInput.BUILD_ROAD, road[0], road[1], 0.8));
            }
        }

        // Spending actions if resources are high
        if (player.getTotalResources() > 7) {
            if (player.hasResources(Resources.BRICK, 1)
                    && player.hasResources(Resources.LUMBER, 1)
                    && player.hasResources(Resources.WOOL, 1)
                    && player.hasResources(Resources.GRAIN, 1)) {

                for (Node node : board.getNode()) {
                    if (!node.isOccupied()) {
                        boolean valid = true;
                        for (Node adj : node.getAdjacentNodes()) {
                            if (adj.isOccupied()) {
                                valid = false;
                                break;
                            }
                        }

                        if (valid) {
                            actions.add(new AIAction(UserInput.BUILD_SETTLEMENT, node.getNodeId(), 0, 0.5));
                        }
                    }
                }
            }

            // Road spending actions
            if (player.hasResources(Resources.BRICK, 1) && player.hasResources(Resources.LUMBER, 1)) {
                for (int[] road : getBuildableRoads()) {
                    actions.add(new AIAction(UserInput.BUILD_ROAD, road[0], road[1], 0.5));
                }
            }
        }
        return actions;
    }
    
    /**
     * Finds all valid road locations that the player can build on.
     * A road is buildable if:
     * - No road already exists between the two nodes
     * - The player has a settlement or city or an existing road adjacent
     * 
     * @return List of int arrays where each array contains the two node IDs for a potential road
     */
    private List<int[]> getBuildableRoads() {
        List<int[]> roads = new ArrayList<>();
        List<Road> playerRoads = new ArrayList<>();

        // Get all roads owned by the player
        for (Road road : board.getRoad()) {
            if (road != null && road.getOwner() == player) {
                playerRoads.add(road);
            }
        }

        // Check every possible edge between nodes for potential road building
        for (int i = 0; i < board.getNode().length; i++) {
            Node n1 = board.getNode()[i];
            for (Node n2 : n1.getAdjacentNodes()) {
                if (n1.getNodeId() < n2.getNodeId()) {

                    // check if road already exists between n1 and n2
                    boolean exists = false;
                    for (Road road : board.getRoad()) {
                        if (road != null && road.isConnected(n1) && road.isConnected(n2)) {
                            exists = true;
                            break;
                        }
                    }
                    if (exists) {
                        continue;
                    }

                    // Check if player can build here
                    boolean connected = false;
                    if (n1.isOccupied() && n1.getBuilding().getOwner() == player) {
                        connected = true;
                    }

                    if (n2.isOccupied() && n2.getBuilding().getOwner() == player) {
                        connected = true;
                    }
                    for (Road road : playerRoads) {
                        if (road.isConnected(n1) || road.isConnected(n2)) {
                            connected = true;
                            break;
                        }
                    }

                    if (connected) {
                        roads.add(new int[]{n1.getNodeId(), n2.getNodeId()});
                    }

                }
            }
        }
        return roads;
    }
}
