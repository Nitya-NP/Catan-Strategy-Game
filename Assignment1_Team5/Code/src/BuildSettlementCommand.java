/**
 * The BuildSettlementCommand class represents a command to build a settlement
 * on the board for a specific player, and performs, undoes, and redoes the action of building
 * a settlement.
 * 
 * @author Raadhikka Gupta, Ranica Chawla
 */

public class BuildSettlementCommand implements Command {
    private Board board;
    private Player player;
    private int nodeId;

    /**
     * Constructs a BuildSettlementCommand.
     * 
     * @param board  the game board
     * @param player the player building the settlement
     * @param nodeId the node where the settlement will be built
     */
    public BuildSettlementCommand(Board board, Player player, int nodeId) {
        this.board = board;
        this.player = player;
        this.nodeId = nodeId;
    }

    /**
     * Executes the command by building a settlement on the board.
     */
    @Override
    public void execute() {
        board.buildSettlement(player, nodeId);
    }

    /**
     * Undoes the command by removing the settlement from the board
     * and restoring the player's resources.
     */
    @Override
    public void undo() {
        board.removeSettlement(player, nodeId); 
    }

    /**
     * Redoes the command by executing it again.
     */
    @Override
    public void redo() {
        execute();
    }
}