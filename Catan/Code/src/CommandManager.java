import java.util.ArrayList;
import java.util.List;

/**
 * The CommandManager class is responsible for managing the execution,
 * undoing, and redoing of commands in the system.
 * 
 * @author Raadhikka Gupta, Ranica Chawla
 */

public class CommandManager {
    private List<Command> commandBuffer; // list to store commands
    private int currentCommand; // currentCommand command integer

    /**
     * Constructs an empty command history.
     */
    public CommandManager() {
        this.commandBuffer = new ArrayList<Command>();
        this.currentCommand = -1;
    }

    /**
     * Executes a new command and adds it to the command history.
     * 
     * @param command the command to execute
     */
    public void executeCommand(Command command) {
        // Remove redoable commands
        while (commandBuffer.size() > currentCommand + 1) {
            commandBuffer.remove(commandBuffer.size() - 1);
        }

        // Execute the command
        command.execute();

        // Add it to the list of commands
        commandBuffer.add(command);
        currentCommand++;
    }

    /**
     * Undoes the most recently executed command.
     */
    public boolean undo() {
        if (currentCommand >= 0) {
            commandBuffer.get(currentCommand).undo();
            currentCommand--;
            return true;
        }
        return false;
    }

    /**
     * Redoes the next command in the history if available.
     */
    public boolean redo() {
        if (currentCommand < commandBuffer.size() - 1) {
            currentCommand++;
            commandBuffer.get(currentCommand).redo();
            return true;
        }
        return false;
    }
}