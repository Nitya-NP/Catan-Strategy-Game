/**
 * The interface is an action that can be executed, undo, and redo in the system. It allows 
 * the user to undo/redo their move.
 * 
 * @author Raadhikka Gupta, Ranica Chawla
 */
public interface Command {
    /**
     * This method executes the command
     */
    public void execute();

    /**
     * This method undoes the command
     */
    public void undo();

    /**
     * This method redoes the command
     */
    public void redo();
}