
/**
 * Simple class to represent an action that the AI can take, along with the nodes involved and a
 * value for the action. This class is used to store potential actions for the AI and their
 * associated values, which can be used to make decisions about which action to take.
 * 
 * @author Krisha Patel
 */
public class AIAction {
    public UserInput action;
    public int node1;
    public int node2;
    public double value;

    /**
     * Constructs an AIAction with the given parameters
     * 
     * @param action the type of action (e.g. BUILD_SETTLEMENT, BUILD_ROAD)
     * @param node1 the first node involved in the action (e.g. settlement location
     * @param node2 the second node involved in the action (e.g. road connection), 0 if not applicable
     * @param value the evaluated value of the action for decision making
     */
    public AIAction(UserInput action, int node1, int node2, double value) {
        this.action = action;
        this.node1 = node1;
        this.node2 = node2;
        this.value = value;
    }
}