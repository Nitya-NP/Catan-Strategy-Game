/**
 * The BuildCityCommand class represents a command to build a city
 * on the board for a specific player, and performs, undoes, and redoes the action of building
 * a city.
 * 
 * @author Raadhikka Gupta, Ranica Chawla
 */
public class BuildCityCommand implements Command{
    private Board board;
    private Player player;
    private int nodeId;

    /**
     * Constructs a BuildCityCommand.
     * 
     * @param board  the game board
     * @param player the player building the city
     * @param nodeId the node where the city will be built
     */
    public BuildCityCommand(Board board, Player player, int nodeId) {
        this.board = board;
        this.player = player;
        this.nodeId = nodeId;
    }

    /**
     * Executes the command by building a city on the board.
     */
    @Override
    public void execute() {
        board.buildCity(player, nodeId);
    }

    /**
     * Undoes the command by removing the city from the board
     * and restoring the player's resources.
     */
    @Override
    public void undo() {
        board.removeCity(player, nodeId); 
    }

    /**
     * Redoes the command by executing it again.
     */
    @Override
    public void redo() {
        execute();
    }
}