
import java.util.ArrayList;
import java.util.List;

/**
 * Checks for and handles constraints situations that override normal action selection.
 * Constraints:
 * - If player has more than 7 resources, must spend
 * - If player has a road that can connect two existing roads, must build there
 * - If player is close to losing longest road, must build there if possible
 * 
 * @author Krisha Patel
 */
public class ConstraintChecker {

    private RuleBasedAI player;
    private Board board;
    private Player[] allPlayers;

    /**
     * Constructs a ConstraintChecker for the given AI player
     * 
     * @param player the AI player for which to check constraints
     */
    public ConstraintChecker(RuleBasedAI player) {
        this.player = player;
    }

    /**
     * Sets the board reference for checking constraints
     * 
     * @param board the game board to set
     */
    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * Sets the list of all players in the game
     * 
     * @param players the array of players to set
     */
    public void setAllPlayers(Player[] players) {
        this.allPlayers = players;
    }

    /**
     * Checks all constraints in priority order and returns a command if any applu.
     * Priority order: Card constraint, Road connection constraint, Longest road constraint
     * 
     * @return a PlayerCommand to handle the constraint if any apply, otherwise null
     */
    public PlayerCommand check() {
        PlayerCommand command;

        command = checkCardConstraint();
        if (command != null) {
            return command;
        }

        command = checkRoadConnectionConstraint();
        if (command != null) {
            return command;
        }

        command = checkLongestRoadConstraint();
        if (command != null) {
            return command;
        }
        return null;
    }

/**
 * Checks if player has more than 7 resources and must spend. If so, checks for possible actions in priority order:
 * - Build city if possible
 * - Build settlement if possible
 * - Build road if possible
 * 
 * @return a PlayerCommand to spend resources if constraint applies, otherwise null
 */
    private PlayerCommand checkCardConstraint() {
        if (player.getTotalResources() <= 7) {
            return null;
        }

        // Try to build a city first
        if (player.hasResources(Resources.ORE, 3) && player.hasResources(Resources.GRAIN, 2)) {
            for (Node node : board.getNode()) {
                if (node.isOccupied() && node.getBuilding() instanceof Settlement && node.getBuilding().getOwner() == player) {
                    return new PlayerCommand(UserInput.BUILD_CITY, node.getNodeId(), 0);
                }
            }
        }

        // Try to build a settlement 
        if (player.hasResources(Resources.BRICK, 1) && player.hasResources(Resources.LUMBER, 1)
                && player.hasResources(Resources.WOOL, 1) && player.hasResources(Resources.GRAIN, 1)) {
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
                        return new PlayerCommand(UserInput.BUILD_SETTLEMENT, node.getNodeId(), 0);
                    }
                }
            }
        }

        // Try to build a road
        if (player.hasResources(Resources.BRICK, 1) && player.hasResources(Resources.LUMBER, 1)) {
            List<int[]> roads = getBuildableRoads();
            if (!roads.isEmpty()) {
                int[] road = roads.get(0);
                return new PlayerCommand(UserInput.BUILD_ROAD, road[0], road[1]);
            }
        }
        return null;
    }

    /**
     * Checks if there are disconnected road segments that should be connected.
     * Looks for a buildable road that touches two different player-owned road segments.
     * 
     * @return a PlayerCommand to build the road if constraint applies, otherwise null
     */
    private PlayerCommand checkRoadConnectionConstraint() {
        List<Road> playerRoads = new ArrayList<>();
        for (Road road : board.getRoad()) {
            if (road != null && road.getOwner() == player) {
                playerRoads.add(road);
            }
        }

        if (playerRoads.size() < 2) {
            return null;
        }

        List<int[]> buildableRoads = getBuildableRoads();

        for (int[] road : buildableRoads) {
            Node n1 = board.getNode()[road[0]];
            Node n2 = board.getNode()[road[1]];

            boolean touchesFirst = false;
            boolean touchesSecond = false;

            for (Road r : playerRoads) {
                if (r.isConnected(n1)) {
                    touchesFirst = true;
                }
                if (r.isConnected(n2)) {
                    touchesSecond = true;
                }
            }

            if (touchesFirst && touchesSecond) {
                return new PlayerCommand(UserInput.BUILD_ROAD, road[0], road[1]);
            }
        }
        return null;

    }

    /**
     * Checks if another player's longest road threatens the AI's longest road.
     * If opponent's longest road is within 1 of AI's, tries to extend AI's road
     * 
     * @return a PlayerCommand to build the road if constraint applies, otherwise null
     */
    private PlayerCommand checkLongestRoadConstraint() {
        int myLongest = getLongestRoadLength(player);
        if (myLongest < 3) {
            return null;
        }

        for (Player p : allPlayers) {
            if (p == player || p == null) {
                continue;
            }
            int longest = getLongestRoadLength(p);
            if (longest >= myLongest - 1) {
                List<int[]> buildableRoads = getBuildableRoads();
                List<Road> myRoads = new ArrayList<>();
                for (Road r : board.getRoad()) {
                    if (r != null && r.getOwner() == player) {
                        myRoads.add(r);
                    }
                }

                for (int[] road : buildableRoads) {
                    Node n1 = board.getNode()[road[0]];
                    Node n2 = board.getNode()[road[1]];

                    for (Road r : myRoads) {
                        if (r.isConnected(n1) || r.isConnected(n2)) {
                            return new PlayerCommand(UserInput.BUILD_ROAD, road[0], road[1]);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Finds all valid road locations that the player can build on
     * A road is buildable if no road exists and player has adjacency
     * 
     * @return List of int arrays where each array contains the two node IDs for a potential road
     */
    private List<int[]> getBuildableRoads() {
        List<int[]> roads = new ArrayList<>();
        List<Road> playerRoads = new ArrayList<>();

        for (Road road : board.getRoad()) {
            if (road != null && road.getOwner() == player) {
                playerRoads.add(road);
            }
        }

        for (int i = 0; i < board.getNode().length; i++) {
            Node n1 = board.getNode()[i];
            for (Node n2 : n1.getAdjacentNodes()) {
                if (n1.getNodeId() < n2.getNodeId()) {
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

    /**
     * Gets the approximate longest road length for a player.
     * Uses road cound as a simple approximation
     * 
     * @param player the player for which to calculate longest road length
     * @return the number of roads owned by the player as an approximation of longest road length
     */
    private int getLongestRoadLength(Player player) {
        int count = 0;
        for (Road road : board.getRoad()) {
            if (road != null && road.getOwner() == player) {
                count++;
            }
        }
        return count;
    }
}
