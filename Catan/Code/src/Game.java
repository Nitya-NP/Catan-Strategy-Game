/**
 * This class contains the Game class that controlls the overall flow of the Catan Game. It manages the player list, tracks the number
 * of rounds played, runs the game rounds, and ends the game when a player wins. 
 * 
 * @author Raadhikka Gupta
 */

public class Game {
	/**
	 * The number of rounds that have been played so far.
	 */
	private int roundsPlayed;

	/**
	 * The max number of rounds allowed in a game.
	 */
	private int maxRounds;

	/**
	 * The list of player participating in the game, contains null if less than 6.
	 */
	private Player[] players;

	/**
	 * The board for the game.
	 */
	private Board board;

	/**
	 * The Dice to play the game.
	 */
	private MultiDice dice;

	private GameLogger logger;

	private TurnManager manager;

	/**
	 * Constructs a new game with the players and board.
	 * 
	 * @param players the players in the game
	 * @param board   the board on which the game will be played
	 */
	public Game(Player[] players, int maxRounds, GameLogger logger) {
		this.roundsPlayed = 0;
		this.maxRounds = maxRounds;
		this.players = players;
		this.dice = new MultiDice();
		this.logger = logger;
	}

	/**
	 * Starts the game and runs till a player wins or max number of rounds is reached
	 */
	public void start() {
    // build the board, robber, and turn manager
    this.board = new Board(this.logger);
    board.placeInitialSettlements(players);
    
    for (Player p : players) {
        if (p instanceof RuleBasedAI) {
            ((RuleBasedAI) p).setBoard(board);
            ((RuleBasedAI) p).setAllPlayers(players);
        }
	
    }
    
    RobberActionsManager robberManager = new RobberActionsManager(board, players);
    board.setRobberManager(robberManager);
    board.registerTileWithDice(dice);
    
    CommandManager commandManager = new CommandManager();
    this.manager = new TurnManager(board, logger, dice, robberManager, commandManager);
    
    boolean gameOver = false; // to check if game over

    // Continue to play rounds till the game ends
    while (!gameOver && roundsPlayed < maxRounds) {
        playRound();
        roundsPlayed++;

        // Check if any player (not null) has reached the score to win
        for (Player p : players) {
            if (p != null && p.getPoints() >= 10) {
                gameOver = true;
                break;
            }
        }
        logger.endTurn(); // To end the turn and increment the round counter in the logger.
    }
}
	/**
	 * Plays a single round of the game
	 */
	public void playRound() {
		// To play a single round.
		for (Player p : players) {
			if (p != null) {
				manager.executeTurn(p);
			}
		}
	}
}
