import java.util.List;
import java.util.ArrayList;

/**
 * MultiDice implements the Dice interface and allows for multiple dice to be rolled together. 
 * It holds a list of Dice objects and allows adding new dice to the list. 
 * The roll method will roll all the dice in the list and return the total result.
 */
public class MultiDice extends DiceRollSubject implements Dice {
	// List to hold the dice
	private List<Dice> diceList;

	/**
	 * Constructor for MultiDice to initialize an inital list of dice with 2 regular dice
	 */
	public MultiDice() {
		// Initialize the dice list as an ArrayList
		diceList = new ArrayList<>();
		// Start with 2 regular dice
		diceList.add(new RegularDice());
		diceList.add(new RegularDice());
	}

	/**
	 * Add a new Dice object to the list of dice.
	 * @param dice The Dice object to be added
	 */
	public void addDice(Dice dice) {
		diceList.add(dice);
	}

	/**
	 * Roll all the dice in the list and returns the total result.
	 */
	@Override
	public int roll() {
		int sum = 0;
		// Roll each die in the list and sum up the results of the rolls
		for (Dice die : diceList) {
			sum += die.roll();
		}
		notifyObservers(sum); 
		return sum;
	}
}
