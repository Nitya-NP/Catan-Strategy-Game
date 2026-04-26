/**
 * Represents a command issued by a player.
 * It stores the action type and optional parameters.
 * 
 * Example:
 * BUILD_ROAD 3,4
 * BUILD_SETTLEMENT 7
 * 
 * @author Nitya Patel
 */
public class PlayerCommand {

    private UserInput action;
    private int node1;
    private int node2;

    public PlayerCommand(UserInput action, int node1, int node2) {
        this.action = action;
        this.node1 = node1;
        this.node2 = node2;
    }

    public UserInput getAction() {
        return action;
    }

    public int getNodeOne() {
        return node1;
    }

    public int getNodeTwo() {
        return node2;
    }
}