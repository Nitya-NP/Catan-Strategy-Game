import java.util.Random;

/**
 * Computer player respresents an automated player in Catan.
 * It randomly selects actions during its turn
 * 
 * The player must roll the dice before performing any other action.
 * 
 * @author Nitya Patel
 */
public class ComputerPlayer extends Player {

    /**
     * Random object
     */
    private final Random rand = new Random();

    /**
     * Tracks whether the player has alrady rolled the dice this turn
     */
    private boolean hasRolled = false;

    /**
     * Constructs a ComputerPlayer with given player ID
     * 
     * @param playerId player id
     */
    public ComputerPlayer(int playerId) {
        super(playerId);

    }

    /**
     * Determines the next action for computer player.
     * The computer must roll the dice first before performing any other action
     * 
     */
    @Override
    public PlayerCommand takeTurn() {

        if (!hasRolled) {
            hasRolled = true;
            return new PlayerCommand(UserInput.ROLL, 0, 0);
        }

        UserInput[] actions = {
                UserInput.BUILD_ROAD,
                UserInput.BUILD_SETTLEMENT,
                UserInput.BUILD_CITY,
                UserInput.LIST,
                UserInput.GO
        };

        UserInput action = actions[rand.nextInt(actions.length)];

        if (action == UserInput.BUILD_ROAD) {
            int node1 = rand.nextInt(53);
            int node2 = node1+1;
            return new PlayerCommand(UserInput.BUILD_ROAD, node1, node2);
        }

        if (action == UserInput.BUILD_SETTLEMENT) {
            int node = rand.nextInt(54);
            return new PlayerCommand(UserInput.BUILD_SETTLEMENT, node, 0);
        }

        if (action == UserInput.BUILD_CITY) {
            int node = rand.nextInt(54);
            return new PlayerCommand(UserInput.BUILD_CITY, node, 0);
        }

        if (action == UserInput.LIST) {
            return new PlayerCommand(UserInput.LIST, 0, 0);
        }

        if (action == UserInput.GO) {
            hasRolled = false; // reset for next turn
            return new PlayerCommand(UserInput.GO, 0, 0);
        }

        return new PlayerCommand(UserInput.GO, 0, 0);
    }

}
