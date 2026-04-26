import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Human player represents a real person playing Catan via console input.
 * The player must roll first, then can build, list cards, or end turn with Go.
 * 
 * @author Krisha Patel
 */
public class HumanPlayer extends Player {

    private Scanner scanner;
    private GameLogger logger;

    // Tracks whether the player has already rolled the dice this turn
    private boolean hasRolled = false;
    

    /**
     * Constructs a HumanPlayer with given player ID
     */
    public HumanPlayer(int playerId, GameLogger logger) {
        super(playerId);
        this.scanner = new Scanner(System.in);
        this.logger = logger;
    }

    /**
     * Gets the next action from human player
     */
    @Override
    public PlayerCommand takeTurn() {

        if (!hasRolled) {
            logger.log(getPlayerId(), "Human: Enter 'roll':");
            String input = scanner.nextLine().trim();

            if (Pattern.matches("^(?i)roll$", input)) {
                hasRolled = true;
                return new PlayerCommand(UserInput.ROLL, 0, 0);
            } else {
                logger.log(getPlayerId(), "Invalid input. You must roll first. Try again.");
                return takeTurn();
            }
        }

        logger.log(getPlayerId(), "Enter command:");
        String input = scanner.nextLine().trim();

        // GO
        if (Pattern.matches("^(?i)go$", input)) {
            hasRolled = false;
            return new PlayerCommand(UserInput.GO, 0, 0);
        }

        // LIST
        if (Pattern.matches("^(?i)list$", input)) {
            return new PlayerCommand(UserInput.LIST, 0, 0);
        }

        // BUILD SETTLEMENT
        if (Pattern.matches("^(?i)build settlement \\d+$", input)) {
            String[] parts = input.split(" ");
            int node = Integer.parseInt(parts[2]);
            return new PlayerCommand(UserInput.BUILD_SETTLEMENT, node, 0);
        }

        // BUILD CITY
        if (Pattern.matches("^(?i)build city \\d+$", input)) {
            String[] parts = input.split(" ");
            int node = Integer.parseInt(parts[2]);
            return new PlayerCommand(UserInput.BUILD_CITY, node, 0);
        }

        // BUILD ROAD
        if (Pattern.matches("^(?i)build road \\d+,\\d+$", input)) {
            String[] parts = input.split(" ");
            String[] nodes = parts[2].split(",");

            int node1 = Integer.parseInt(nodes[0]);
            int node2 = Integer.parseInt(nodes[1]);

            return new PlayerCommand(UserInput.BUILD_ROAD, node1, node2);
        }

        // UNDO ACTION
        if (Pattern.matches("^(?i)undo$", input)) {
            return new PlayerCommand(UserInput.UNDO, 0, 0);
        }

        // REDO ACTION
        if (Pattern.matches("^(?i)redo$", input)) {
            return new PlayerCommand(UserInput.REDO, 0, 0);
        }

        logger.log(getPlayerId(), "Invalid command. Try again.");
        return takeTurn();
    }
}