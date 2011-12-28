import java.io.*;

/**
 * Starter bot implementation.
 */
public class MyBot extends Bot {
	/**
	 * Main method executed by the game engine for starting the bot.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static void main(String[] args) throws IOException {
		try {
			MyBot b = new MyBot();
			b.readSystemInput();
		}
		catch (Exception e) {
			  e.printStackTrace();
		}
	}

	private Map currentTurnMap;
	private Map nextTurnMap;

	public MyBot() {
		//initMaps();
	}

	/**
	 * For every ant check every direction in fixed order (N, E, S, W) and move
	 * it if the tile is passable.
	 */
	@Override
	public void doTurn() {
		initMaps();
		// Ants state = getAnts();

		// Types of ants:
		// Scavenger: Finds food, avoids conflict
		// Soldier : Kills enemies in groups, creates conflict
		// Guard : Defends hill, avoids conflict

		// Move ants to the nearest food
		for (MapTile t : currentTurnMap.getTiles()) {
			if (t.hasAnt()) {
				Ant a = t.getAnt();
			
				if (a.getRole() == AntRole.SCAVENGER) {
					MapTile food = currentTurnMap.getNearestFood(a.getX(), a.getY());
					
					if (a.getX() == Integer.MAX_VALUE && a.getY() == Integer.MAX_VALUE) {
						a.moveRandom();
					}
					else if (a.getX() < food.getX()) {
						a.move(Direction.EAST);
					}
					else if (a.getX() > food.getX()) {
						a.move(Direction.WEST);
					}
					else if (a.getY() < food.getY()) {
						a.move(Direction.SOUTH);
					}
					else if (a.getY() > food.getY()) {
						a.move(Direction.NORTH);
					}
					else {
						a.moveRandom();
					}
				}
			}
		}

	}

	private void initMaps() {
		Ants state = getAnts();

		// Prepare maps
		currentTurnMap = new Map(this, state.getRows(), state.getCols());
		//nextTurnMap = new Map(state.getRows(), state.getCols());

		for (int x = 0; x < currentTurnMap.getWidth(); x++) {
			for (int y = 0; y < currentTurnMap.getHeight(); y++) {
				currentTurnMap.setTile(new MapTile(currentTurnMap, x, y), x, y);
				//nextTurnMap.setTile(new MapTile(), x, y);
			}
		}

		// Set neighbor tiles
		for (int x = 0; x < currentTurnMap.getWidth(); x++) {
			for (int y = 0; y < currentTurnMap.getHeight(); y++) {

				MapTile currentTile = currentTurnMap.getTile(x, y);
				currentTile.setNeighbors((y - 1 >= 0) ? currentTurnMap.getTile(x, y - 1) : null,
										 (x + 1 < currentTurnMap.getWidth()) ? currentTurnMap.getTile(x + 1, y) : null,
										 (y + 1 < currentTurnMap.getHeight()) ? currentTurnMap.getTile(x, y + 1) : null,
										 (x - 1 >= 0) ? currentTurnMap.getTile(x - 1, y) : null);

				//MapTile nextTile = nextTurnMap.getTile(x, y);
//				nextTile.setNeighbors((y - 1 >= 0) ? nextTurnMap.getTile(x,
//						y - 1) : null,
//						(x + 1 <= nextTurnMap.getWidth()) ? nextTurnMap
//								.getTile(x + 1, y) : null,
//						(y + 1 <= nextTurnMap.getHeight()) ? nextTurnMap
//								.getTile(x, y + 1) : null,
//						(x - 1 >= 0) ? nextTurnMap.getTile(x - 1, y) : null);

			}
		}

		// Populate my ants
		for (int x = 0; x < currentTurnMap.getWidth(); x++) {
			for (int y = 0; y < currentTurnMap.getHeight(); y++) {
				if (state.getMyAnts()[x][y] == true) {
					Ant a = new Ant(currentTurnMap.getTile(x, y), AntRole.SCAVENGER);
					currentTurnMap.getTile(x, y).setAnt(a);
				}
			}
		}

		// Populate enemy ants
		for (int x = 0; x < currentTurnMap.getWidth(); x++) {
			for (int y = 0; y < currentTurnMap.getHeight(); y++) {
				if (state.getEnemyAnts()[x][y] == true) {
					Ant a = new Ant(currentTurnMap.getTile(x, y), AntRole.ENEMY);
					currentTurnMap.getTile(x, y).setAnt(a);
				}
			}
		}

		// Populate food
		for (int x = 0; x < currentTurnMap.getWidth(); x++) {
			for (int y = 0; y < currentTurnMap.getHeight(); y++) {
				if (state.getFoodTiles()[x][y] == true) {
					currentTurnMap.getTile(x, y).setHasFood(true);
				}
			}
		}

	}

}
