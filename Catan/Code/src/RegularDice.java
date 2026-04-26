import java.util.Random;
/**
 * A RegularDice class is able to roll a six sided die.
 * @author Ranica Chawla
 */
public class RegularDice implements Dice {
    private final Random rand = new Random();
    /**
     * Simulates rolling a regular six sided die.
     * @return a random integer between 1 and 6
     */
    @Override
    public int roll() {
        
        // Generates a random number between 1 and 6 to return
        return rand.nextInt(6) + 1;
    }
}
