/**
 * To implement the robber's actions of discarding, stealing, moving, and blocking resources to be built when provoked.
 * 
 * @author Raadhikka Gupta
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RobberActionsManager {
	/**
	 * To store the robber's current location.
	 */
	private Tile currentTile;

	/**
	 * To store all players
	 */
	private Player[] players;

	/**
	 * Random number generator.
	 */
	private final Random rand = new Random();

	/**
	 * All stealable resources
	 */
	private static final Resources[] STEAL_RESOURCES = {Resources.BRICK, Resources.GRAIN, Resources.LUMBER, Resources.ORE, Resources.WOOL};

	/**
	 * To initialize the robber to be on the desert tile initially
	 */
	public RobberActionsManager(Board board, Player[] players) {
		this.players = players;

		// to find the desert tile
		for (Tile t : board.getTile()) {
			if (t.getResource() == Resources.NOTHING) {
				currentTile = t;
				break;
			}
		}
	}

	/**
	 * Handles to discard resource cards when a 7 is rolled.
	 * 
	 * @return True if discarded
	 */
	public boolean discardResourceCards() {
		// to validate
		if (players == null)
			return false;

		// to loop through the players
		for (Player p : players) {
			// to validate
			if (p == null) 
				continue;

			int totalResources = p.getTotalResources();

			// if the current player has more than 7 cards
			if (totalResources > 7) {
				System.out.println("Checking players for discard...");
				int discardCardAmount = totalResources / 2;

				// to remove half the resources the player has, randomly
				for (int i = 0; i < discardCardAmount; i++) {
					// to find a resource that's available and then remove it
					Resources toRemove;
					do { 
						toRemove = STEAL_RESOURCES[rand.nextInt(STEAL_RESOURCES.length)];
					} while (!p.hasResources(toRemove, 1));

					p.removeResource(toRemove, 1);
				}

				System.out.println("Player " + p.getPlayerId() + " discards " + discardCardAmount + " cards");
			}
		}

		return true;
	}

	/**
	 * To choose a random victim player to steal from.
	 * 
	 * @param currentPlayer the current player
	 */
	public Player chooseVictim(Player currentPlayer) {
		// to validate tile
		if(currentTile == null) {
			return null;
		}

		Node[] nodes = currentTile.getNodes(); // nodes on all tiles
		List<Player> possibleVictims = new ArrayList<>(); // list of victims

		// find all the possible victims to steal from
		for(Node n : nodes){
			if(n.isOccupied()){
				Player p = n.getBuilding().getOwner();

				// add if it's not the current player and has resources
				if(p != currentPlayer && p.getTotalResources() > 0){
					possibleVictims.add(p);
				}
			}
		}

		// if no possible victims
		if(possibleVictims.isEmpty())
			return null;

		return possibleVictims.get(rand.nextInt(possibleVictims.size()));
	}

	/**
	 * To move the robber to a new tile
	 * 
	 * @param newTile the new tile to move to
	 * @return True when moved
	 */
	public boolean moveRobber(Board board) {
		// valid tile or if it's the same tile
		if (board == null)
			return false;

		Tile[] tiles = board.getTile();
		Tile newTile;

		// choose a different tile
		do {
			newTile = tiles[rand.nextInt(tiles.length)];
		} while (newTile == currentTile);
		
		currentTile = newTile;
		System.out.println("Robber moved to tile " + currentTile.getTileId());
		return true;
	}

	/**
	 * Returns the current tile that the robber is on
	 * 
	 * @return Tile robber is on
	 */
	public Tile getCurrentTile() {
		return currentTile;
	}

	/**
	 * To check if the robber is on the tile given
	 * 
	 * @param tile the tile to check with
	 * @return true if it is
	 */
	public boolean isRobberOnTile(Tile tile) {
		if (tile == null)
			return false;
		
		return currentTile == tile;
	}

	/**
	 * To steal a resource from a player
	 * 
	 * @param victimPlayer the player to steal from
	 */
	public void stealResource(Player victimPlayer, Player currentPlayer) {
		// if they don't have resources
		if (victimPlayer == null || victimPlayer.getTotalResources() == 0)
			return;

		// to find a resource that's available and then remove it
		Resources toRemove;
		do { 
			toRemove = STEAL_RESOURCES[rand.nextInt(STEAL_RESOURCES.length)];
		} while (!victimPlayer.hasResources(toRemove, 1));

		System.out.println("Stealing resource from player " + victimPlayer.getPlayerId());
		victimPlayer.removeResource(toRemove, 1);
		currentPlayer.addResource(toRemove, 1);
	}
}
