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
					//MapTile food = currentTurnMap.getNearestFood(a.getX(), a.getY());
					
					MapTile north = a.getTile().getNorth();
					MapTile east = a.getTile().getEast();
					MapTile south = a.getTile().getSouth();
					MapTile west = a.getTile().getWest();
					
					//This if-block sucks. Instead of going through each one, calculate the best choice
					//and do that instead. Then there won't be a built in priority depending on which
					//if block is at the top. Also, it might actually work correctly.
//					if (a.getX() == Integer.MAX_VALUE && a.getY() == Integer.MAX_VALUE) {
//						a.moveRandom();
//					}
//					else if (a.getX() < food.getX() && eastNeighbor != null && eastNeighbor.isPassable() && !eastNeighbor.isOccupied()) { 
//						a.move(Direction.EAST);
//					}
//					else if (a.getX() > food.getX() && westNeighbor != null && westNeighbor.isPassable() && !westNeighbor.isOccupied()) {
//						a.move(Direction.WEST);
//					}
//					else if (a.getY() < food.getY() && southNeighbor != null && southNeighbor.isPassable() && !southNeighbor.isOccupied()) {
//						a.move(Direction.SOUTH);
//					}
//					else if (a.getY() > food.getY() && northNeighbor != null && northNeighbor.isPassable() && !northNeighbor.isOccupied()) {
//						a.move(Direction.NORTH);
//					}
//					else {
//						a.moveRandom();
//					}
					
					int nearest = Integer.MAX_VALUE;
					Direction bestMove = null;
					
					if (north != null && north.getStepsFromFood() < nearest) {
						nearest = north.getStepsFromFood();
						bestMove = Direction.NORTH;
					}
					if (east != null && east.getStepsFromFood() < nearest) {
						nearest = east.getStepsFromFood();
						bestMove = Direction.EAST;
					}
					if (south != null && south.getStepsFromFood() < nearest) {
						nearest = south.getStepsFromFood();
						bestMove = Direction.SOUTH;
					}
					if (west != null && west.getStepsFromFood() < nearest) {
						nearest = west.getStepsFromFood();
						bestMove = Direction.WEST;
					}
					
					if (bestMove == null)
						a.moveRandom();
					else
						a.move(bestMove);
				
					
				}
			}
		}

	}

	private void initMaps() {
		Ants state = getAnts();

		// Prepare maps
		currentTurnMap = new Map(this, state.getCols(), state.getRows());
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
		
		// Populate water		
		for (int y=0; y < currentTurnMap.getHeight(); y++) {
			for (int x=0; x < currentTurnMap.getWidth(); x++) {
				if (state.getIlk(new Tile(x, y)).isWater()) {
					currentTurnMap.getTile(x, y).setIsWater(true);
				}
			}
		}
		
		//Print the map
		currentTurnMap.print();

	}

}
