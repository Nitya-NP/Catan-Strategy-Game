import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Any class that can be observed extends this class.
 * Managers observers and notifies them when something happends. 
 * 
 * @author Nitya Patel
 * 
 */
public abstract class DiceRollSubject {
    private List<DiceRollObserver> observers= Collections.synchronizedList(new ArrayList<>());

    /**
     * Add an observer to get notifications
     * @param ob observer
     */
    public void addObserver(DiceRollObserver ob){
        if(ob !=null && !observers.contains(ob)){
            observers.add(ob);
            //System.out.println("Observer added: "+ observers.getClass().getSimpleName());
        }
    }

    /**
     * Remover an observer
     * @param ob observer
     */
    public void removeObserver(DiceRollObserver ob){
        observers.remove(ob);
    }

    /**
     * Tell all observers something happened
     * @param diceValue the total dice rolled 
     */
    protected void notifyObservers(int diceValue){
        for(DiceRollObserver ob: observers){
            if(ob!=null){
                ob.onDiceRolled(diceValue);
            }
        }
    }
    
}
