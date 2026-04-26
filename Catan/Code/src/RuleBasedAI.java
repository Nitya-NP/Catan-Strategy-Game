import java.util.*;

/**
 * Rule-based AI player that makes decisions based on predefined rules and values.
 * Extends ComputerPlayer to inherit basic functionality.
 * 
 * @author Krisha Patel
 */
public class RuleBasedAI extends ComputerPlayer {
    
    private GameLogger logger;
    private Board board;
    private ActionEvaluator actionEvaluator;
    private ConstraintChecker constraintChecker;
    private Random random = new Random();
    private boolean hasRolled = false;  
    private boolean turnEnded = false;

    /**
     * Constructs a new RuleBasedAI player
     * 
     * @param playerId the ID of the player
     * @param logger the game logger to log actions
     */
    public RuleBasedAI(int playerId, GameLogger logger) {
        super(playerId);
        this.logger = logger;
        this.actionEvaluator = new ActionEvaluator(this);
        this.constraintChecker = new ConstraintChecker(this);
    }

    /**
     * Reset the turn
     */
    public void startTurn() {
        hasRolled = false;
        turnEnded = false;
    }

    /**
     * Sets the board reference for this AI and its helper classes.
     * 
     * @param board the game board to set
     */
    public void setBoard(Board board) {
        this.board = board;
        actionEvaluator.setBoard(board);
        constraintChecker.setBoard(board);
    }

    /**
     * Sets the array of all players for constraint checking.
     * 
     * @param players the array of all players in the game
     */
    public void setAllPlayers(Player[] players) {
        constraintChecker.setAllPlayers(players);
    }

    /**
     * Executes the AI's turn by checking constraints first, then evaluating actions.
     * If constraints exist, they are handled immediately.
     * Otherwise, evaluated all possible actions and picks the one with highest value.
     * In case of ties, choose randomly among the best actions.
     * 
     * @return PlayerCommand representing the chosen action
     */
    @Override
    public PlayerCommand takeTurn() {

        if (!hasRolled) {
            hasRolled = true;
            return new PlayerCommand(UserInput.ROLL, 0, 0);
        }

        if (turnEnded) {
            return new PlayerCommand(UserInput.GO, 0, 0);
        }

        logger.log(getPlayerId(), "Resources: " + listResources());
    
        PlayerCommand command = constraintChecker.check();
        if (command != null) {
            if (command.getAction() == UserInput.GO) {
                turnEnded = true;
            }
            
            return command;
        }

        List<AIAction> actions = actionEvaluator.evaluate();
        if (actions.isEmpty()) { 
            turnEnded=true;   
            return new PlayerCommand(UserInput.GO, 0, 0);
        }

        actions.sort((a, b) -> Double.compare(b.value, a.value));

        double bestValue = actions.get(0).value;
        List<AIAction> bestActions = new ArrayList<>();
        for (AIAction action : actions) {
            if (action.value == bestValue) {
                bestActions.add(action);
            } 

        }

        AIAction chosen = bestActions.get(random.nextInt(bestActions.size()));
        if (chosen.action == UserInput.GO) {
            turnEnded=true;
        }
        
        return new PlayerCommand(chosen.action, chosen.node1, chosen.node2);
    }
    
}