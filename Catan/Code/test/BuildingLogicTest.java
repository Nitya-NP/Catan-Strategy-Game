import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Building logic test cases 
 * 1. Place a city
 * 2. Place a settlement
 * 3. Place a road
 * 4. Validation case: Nodes start empty
 * @author Ranica Chawla
 */
public class BuildingLogicTest {

    /**
     * To test that placing a settlement on a node correctly marks the node as occupied and 
     * assigns ownership to the correct player.
     */
	@Test
    public void placeSettlementOnBoard() {
        // Build board and player
        Board board = new Board(new GameLogger());
        Player player = new ComputerPlayer(1);

        // Construct new settlement
		Building settlement = new Settlement(player);

        // Place settlement on first node
        Node[] nodes = board.getNode();
        nodes[0].setBuilding(settlement);

        // Check that node contains a settlement
        assertTrue(nodes[0].isOccupied() && nodes[0].getBuilding() instanceof Settlement);
        // Test the settlement is owned by the player
        assertEquals(player, nodes[0].getBuilding().getOwner());
    
    }

    /**
     * To test that placing a city on a node correctly marks the node as occupied and 
     * assigns ownership to the correct player.
     */
    @Test
    public void placeCityOnBoard() {
        // Build board and player
        Board board = new Board(new GameLogger());
        Player player = new ComputerPlayer(1);

        // Construct new city
		Building city = new City(player);

        // Place city on first node
        Node[] nodes = board.getNode();
        nodes[0].setBuilding(city);

        // Check that node contains a city
        assertTrue(nodes[0].isOccupied() && nodes[0].getBuilding() instanceof City);
        // Test the city is owned by the player
        assertEquals(player, nodes[0].getBuilding().getOwner());

    }

    /**
     * To test that placing a road on a node correctly marks the node as occupied and 
     * assigns ownership to the correct player.
     */
    @Test
    public void placeRoadOnBoard() {
        // Build board and player
        Board board = new Board(new GameLogger());
        Player player = new ComputerPlayer(1);

        // Construct new road
        Node[] nodes = board.getNode();
		Road road = new Road(new Node[] { nodes[0], nodes[1] }, player);

        // Place road on board
        board.getRoad()[0] = road;

        // Check that the correct nodes contains the road
        assertTrue(board.getRoad()[0].isConnected(nodes[0]));
        assertTrue(board.getRoad()[0].isConnected(nodes[1]));
        // Test the road is owned by the player
        assertEquals(player, board.getRoad()[0].getOwner());
    }

    /**
     * Tests that upgrading a settlement to a city replaces
     * the existing settlement at the node.
     */
    @Test
    public void shouldUpgradeSettlementToCity() {
        // Build board and player
        Board board = new Board(new GameLogger());
        Player player = new ComputerPlayer(1);

        Node[] nodes = board.getNode();

        // Occupy all nodes with settlements
        for (int i = 0; i < nodes.length; i++) {
            nodes[i].setBuilding(new Settlement(player));
        }

        // Give player resources to force doing an action
        player.addResource(Resources.ORE, 60);
        player.addResource(Resources.GRAIN, 70);

        boolean upgraded = false;

        // Create robber manager for turn manager
        RobberActionsManager robberManager = new RobberActionsManager(board, new Player[] { player });
		board.setRobberManager(robberManager);

        // Simulate managing the turn
        TurnManager turnManager = new TurnManager(board, new GameLogger(), new MultiDice(), robberManager, new CommandManager());
        // Attempt to upgrade the settlement at the first node to a city
        for (int i = 0; i < 80; i++) {
            turnManager.executeTurn(player);
            // Check if any node got upgraded to a city
            for (Node node : nodes) {
                if (node.getBuilding() instanceof City) {
                    upgraded = true;
                    break;
                }
            }
        }

        // Ensure the upgrade happened
        assertTrue(upgraded);
    }

    /**
     * To test if at board creation, all the nodes begin unoccupied.
     */
    @Test
    public void nodesShouldStartEmpty() {
        // Build a new board
        Board board = new Board(new GameLogger());
        Node[] nodes = board.getNode();

        // Loop through all nodes in the board
        for (Node node : nodes) {
            // Ensure all nodes remain unoccupied
            assertFalse(node.isOccupied());
            assertNull(node.getBuilding());
        }
    }

}
