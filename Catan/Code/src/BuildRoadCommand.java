/**
 * The BuildRoadCommand class represents a command to build a road
 * on the board for a specific player, and performs, undoes, and redoes the action of building
 * a road.
 * 
 * @author Raadhikka Gupta, Ranica Chawla
 */

public class BuildRoadCommand implements Command {
    private Board board;
    private Player player;
    private int nodeId1;
    private int nodeId2;

    /**
     * Constructs a BuildRoadCommand.
     * 
     * @param board  the game board
     * @param player the player building the road
     * @param nodeId1 the first node where the road will be built
     * @param nodeId2 the first node where the road will be built
     */
    public BuildRoadCommand(Board board, Player player, int nodeId1, int nodeId2) {
        this.board = board;
        this.player = player;
        this.nodeId1 = nodeId1;
        this.nodeId2 = nodeId2;
    }

    /**
     * Executes the command by building a road on the board.
     */
    @Override
    public void execute() {
        board.buildRoad(player, nodeId1, nodeId2);
    }

    /**
     * Undoes the command by removing the road from the board
     * and restoring the player's resources.
     */
    @Override
    public void undo() {
        board.removeRoad(player, nodeId1, nodeId2);
    }

    /**
     * Redoes the command by executing it again.
     */
    @Override
    public void redo() {
        execute();
    }
}