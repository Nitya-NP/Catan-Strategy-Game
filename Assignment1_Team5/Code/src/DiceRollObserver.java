/**
 * Any class wants to be notified when dice are rolled implements this. 
 * 
 * @author Nitya Patel
 */
public interface DiceRollObserver {

    /**
     * Called automatically when dice are rolled
     * @param diveValue the total value rolled
     */
    void onDiceRolled(int diceValue);
}