import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 * The GameLogger class is responsible for logging the actions of players during the game. 
 * It keeps track of the number of rounds, and provides a method to log player actions in required format.
 * @author Ranica Chawla
 */
public class GameLogger {
    // Private attributes to keep track of the number of rounds
    private int rounds;
    private final Logger logger;

    /**
     * Constructor for the GameLogger class that sets the rounds to 1.
     */
    public GameLogger() {
        this.rounds = 1;

        // Get a dedicated logger for this class
        logger = Logger.getLogger(GameLogger.class.getName());

        // Disable all parent handlers to prevent double printing
        logger.setUseParentHandlers(false);

        // Remove any existing handlers (safety)
        for (var h : logger.getHandlers()) {
            logger.removeHandler(h);
        }

        // Add a single console handler with custom formatter
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public synchronized String format(java.util.logging.LogRecord record) {
                return record.getMessage() + "\n"; // ONLY print the message
            }
        });

        logger.addHandler(handler);
        logger.setLevel(Level.ALL);

        
    }

    /**
     * Private method to format the beginning of the formatted output with the current round number and player ID.
     * @param playerId the ID of the player whose doing an action
     * @return a string in the format of "[rounds] / playerId: "
     */
    private String start(int playerId) {
        return "[" + rounds + "] / " + playerId + ": ";
    }

    /**
     * Log the current action of a player in the required format
     * @param playerId the ID of the player whose doing an action
     * @param action the action that the player is doing
     */
    public void log(int playerId, String action) {
        logger.info(()-> start(playerId) + action);
        
    }

    /**
     * Method to let the logger know that the current turn ended so it may increment the rounds counter for the next turn.
     */
    public void endTurn() {
        rounds++;
    }
}
