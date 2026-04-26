import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Player logic test cases 
 * 1. Turn switching
 * 2. Validation: Correct points after turn
 * 3. Validation: Correct resources after building
 * 4. Validation: Player builds if >=7 resources
 * 
 * @author Nitya Patel, Krisha Patel
 */
public class PlayerLogicTest {


	/**
	 * Test switching turns doesnt break the game.
	 */
	@Test
	public void testPlayerSwitchingTurns() {
		Player[] players= new Player[2];
		players[0] =new ComputerPlayer(1);
		players[1]=new ComputerPlayer(2);

		Game game= new Game(players,1, new GameLogger());

		int p1Points= players[0].getPoints();
		int p2Points= players[1].getPoints();

		int p1Resource= players[0].getTotalResources();
		int p2Resource= players[1].getTotalResources();

		game.start();

		//After round, points/ resources should have updates
		assertTrue(players[0].getPoints()>=p1Points); //Player 1 points should not descrease
		assertTrue(players[1].getPoints()>=p2Points); //Player 2 points should not descrease

		assertTrue(players[0].getPoints()>=p1Resource); //Player 1 resource should not descrease
		assertTrue(players[1].getPoints()>=p2Resource); //Player 2 resource should not descrease");
		
	}

	/**
	 * Test that adding a settlement increases player's points by 1
	 */
	@Test
	public void testSettlementAddsOnePoint(){
		Player player=new ComputerPlayer(1);
		Building s=new Settlement(player);
		player.addBuilding(s);

		assertEquals(1, player.getPoints());
		
	}

	/**
	 * Test that adding a cities increases player's points by 2
	 */
	@Test
	public void testCityAddsTwoPoint(){
		Player player=new ComputerPlayer(1);
		Building c=new City(player);
		player.addBuilding(c);

		assertEquals(2, player.getPoints());
		
	}

	/**
	 * Test that adding a road does NOT give points
	 */
	@Test
	public void testRoadAddsZeroPoint(){
		Player player=new ComputerPlayer(1);
		player.addRoad();

		assertEquals(0,player.getPoints());

	}

		/**
	 * Test that points after taking a turn are at least as much as before
	 */
	@Test
	public void testPointsAfterBuilding() {
		Player[] player = new Player[1];
		player[0]= new ComputerPlayer(1);
    	GameLogger logger = new GameLogger();
    	Board board = new Board(logger);
		RobberActionsManager robberManager = new RobberActionsManager(board,player);
		board.setRobberManager(robberManager);
		TurnManager t= new TurnManager(board, logger, new MultiDice(), robberManager, new CommandManager());

		int before = player[0].getPoints();

		t.executeTurn(player[0]);

		int after = player[0].getPoints();

		assertTrue(after >= before);
	}

	/**
 	* Tests that players take building actions when resource > 7
	 */
	@Test
	public void testPlayerBuildsIfResourcesHigh() {
		Player[] player = new Player[1];
		player[0]= new ComputerPlayer(1);
    	GameLogger logger = new GameLogger();
    	Board board = new Board(logger);
		RobberActionsManager robberManager = new RobberActionsManager(board,player);
		board.setRobberManager(robberManager);
		TurnManager t= new TurnManager(board, logger, new MultiDice(), robberManager, new CommandManager());

    	player[0].addResource(Resources.LUMBER, 4);
    	player[0].addResource(Resources.BRICK, 4);
    	player[0].addResource(Resources.WOOL, 2);
    
    	int beforePoints = player[0].getPoints();
    
    	t.executeTurn(player[0]);
    
    	int afterPoints = player[0].getPoints();

    	assertTrue(afterPoints >= beforePoints);
	}
}
