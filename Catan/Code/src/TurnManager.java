/**
 * This class controls the actions that occurs during a player's turn in the
 * game.
 * 
 * @author Ranica Chawla, Raadhikka Gupta
 */

public class TurnManager {
    // Holds the current state of the turn (either start, action, robber, or end)
    private TurnState currState;

    // Attributes needed to manage the turn
    private Board board;
    private GameLogger logger;
    private MultiDice dice;

    /**
     * To manage the undo/redo commands entered by the player
     */
    private CommandManager commandManager;

    /**
     * To manage the robber actions when activated
     */
    private RobberActionsManager robberManager;

    /**
     * Constructor for the TurnManager class in order to initialize the board,
     * logger, dice, and robber manager.
     * 
     * @param board         the game board
     * @param logger        the game logger to log actions and events during the
     *                      turn
     * @param dice          in order to roll the dice when the player chooses to
     *                      roll
     * @param robberManager to manage the robber actions when activated
     */
    public TurnManager(Board board, GameLogger logger, MultiDice dice, RobberActionsManager robberManager,
            CommandManager commandManager) {
        this.board = board;
        this.logger = logger;
        this.dice = dice;
        this.robberManager = robberManager;
        this.commandManager = commandManager;
    }

    /**
     * Executes a player's turn by managing the sequence of actions based on the
     * current state of the turn.
     * 
     * @param player The current player whose turn is being executed
     */
    public void executeTurn(Player player) {
        // Start the turn by setting the current state to START_TURN
        currState = TurnState.START_TURN;

        if (player instanceof RuleBasedAI) {
            ((RuleBasedAI) player).startTurn();
        }

        // Continue to manage the turn until the player decides to end it by passing
        while (currState != TurnState.END_TURN) {
            // Get the player's input for their action during the turn
            PlayerCommand command = player.takeTurn();
            // Manage the player's action based on their action
            manageTurn(player, command);
        }
    }

    /**
     * Manages the player's action during their turn based on their input.
     * The player can choose to roll, list resources, pass, or build structures.
     * 
     * @param player The player whose turn is being managed
     * @param input  The action chosen by the player
     */
    private void manageTurn(Player player, PlayerCommand command) {
        UserInput input = command.getAction();

        // Handle the player's action based on their input
        switch (input) {
            case ROLL:
                // Handle the roll action
                handleRoll(player);
                break;
            case LIST:
                // Handle the list action to show the player's resources
                handleList(player);
                break;
            case GO:
                // Handle the go action to end the player's turn
                handleGo(player);
                break;
            case BUILD_SETTLEMENT:
                // Handle the build settlement action to build a settlement for the player
                handleBuildSettlement(player, command.getNodeOne());
                break;
            case BUILD_CITY:
                // Handle the build city action to upgrade a settlement to a city for the player
                handleBuildCity(player, command.getNodeOne());
                break;
            case BUILD_ROAD:
                // Handle the build road action to build a road for the player
                handleBuildRoad(player, command.getNodeOne(), command.getNodeTwo());
                break;
            case UNDO:
                // Handle the undo action for the player
                if (!commandManager.undo()) {
                    logger.log(player.getPlayerId(), "No commnd to undo");
                } else {
                    StateWriter.writeState(board, robberManager);
                }
                break;
            case REDO:
                // Handle the redo action for the player
                if (!commandManager.redo()) {
                    logger.log(player.getPlayerId(), "No commnd to redo");
                } else {
                    StateWriter.writeState(board, robberManager);
                }
                break;
            default:
                logger.log(player.getPlayerId(), "Invalid action.");
                break;
        }
    }

    /**
     * Handles the roll action for the player.
     * If the player rolls a 7, the robber is activated and the player must discard
     * half of their resource cards,
     * move the robber, and steal a resource from another player.
     * If the player rolls any other number, resources are produced based on the
     * dice value.
     * 
     * @param player The player who is rolling the dice
     */
    private void handleRoll(Player player) {
        // Ensure the player can only roll at the start of their turn
        if (currState != TurnState.START_TURN) {
            logger.log(player.getPlayerId(), "You already rolled.");
            return;
        }

        // Roll the dice and get the value
        int diceValue = roll(player);

        // If the player rolls a 7, activate the robber and manage the robber actions
        if (diceValue==7) {
            logger.log(player.getPlayerId(), "Robber activated!");
            // Discard half the cards
            robberManager.discardResourceCards();

            // Move the robber
            robberManager.moveRobber(board);
            StateWriter.writeState(board, robberManager);

            // Steal a resource from another player
            Player victim = robberManager.chooseVictim(player);
            if (victim != null) {
                logger.log(player.getPlayerId(), "Stealing from player " + victim.getPlayerId());
                robberManager.stealResource(victim, player);
            }

            // After handling the robber actions, allow the player to take their action
            currState = TurnState.DO_ACTION;
        }
        else{
            currState=TurnState.DO_ACTION;
        }
        
    

    }

    /**
     * Handles the list action for the player to show their current resources.
     * 
     * @param player The current player
     */
    private void handleList(Player player) {
        logger.log(player.getPlayerId(), " Resources: " + player.listResources());
    }

    /**
     * Handles the go action for the player to end their turn.
     * 
     * @param player The current player
     */
    private void handleGo(Player player) {
        if (!isValidAction(player))
            return;

        logger.log(player.getPlayerId(), "passes");
        currState = TurnState.END_TURN;
    }

    /**
     * Handles the build settlement action for the player to build a settlement on
     * the board.
     * 
     * @param player The current player
     * @param nodeId the location to build the settlement
     */
    private void handleBuildSettlement(Player player, int nodeId) {
        if (!isValidAction(player))
            return;

        Command cmd = new BuildSettlementCommand(board, player, nodeId);
        commandManager.executeCommand(cmd);
        StateWriter.writeState(board, robberManager);
    }

    /**
     * Handles the build city action for the player to upgrade a settlement to a
     * city on the board.
     * 
     * @param player The current player
     * @param nodeId the location to build the city
     */
    private void handleBuildCity(Player player, int nodeId) {
        if (!isValidAction(player))
            return;

        Command cmd = new BuildCityCommand(board, player, nodeId);
        commandManager.executeCommand(cmd);
        StateWriter.writeState(board, robberManager);
    }

    /**
     * Handles the build road action for the player to build a road on the board.
     * 
     * @param player
     * @param fromNodeId the first node provided by player
     * @param toNodeId   the second node provided by player
     */
    private void handleBuildRoad(Player player, int fromNodeId, int toNodeId) {
        if (!isValidAction(player))
            return;

        Command cmd = new BuildRoadCommand(board, player, fromNodeId, toNodeId);
        commandManager.executeCommand(cmd);
        StateWriter.writeState(board, robberManager);
    }

    /**
     * Checks if the player's action is valid based on the current state of the
     * turn.
     * 
     * @param player The current player
     * @return true if the action is valid, false otherwise
     */
    private boolean isValidAction(Player player) {
        if (currState != TurnState.DO_ACTION) {
            logger.log(player.getPlayerId(), "You must roll first.");
            return false;
        }

        return true;
    }

    /**
     * Rolls the dice for the player and updates the current state of the turn based
     * on the dice value.
     * 
     * @param player the player who is rolling the dice
     * @return the value of the rolled dice
     */
    private int roll(Player player) {
        // Roll the dice and log the result
        int diceValue = dice.roll();
        logger.log(player.getPlayerId(), "rolled " + diceValue);

        // If the player rolls a 7, activate the robber
        if (diceValue == 7) {
            currState = TurnState.ROBBER;
        } else {
            // If the player rolls any other number, allow them to take their action
            currState = TurnState.DO_ACTION;
        }

        // Return the value of the rolled dice
        return diceValue;
    }
}
