import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Holds all game data and current game state.
 */
public class Ants {
    /** Maximum map size. */
    public static final int MAX_MAP_SIZE = 256 * 2;
    private final int loadTime;
    private final int turnTime;
    private final int height;
    private final int width;
    private final int turns;
    private final int viewRadius2;
    private final int attackRadius2;
    private final int spawnRadius2;
    private final boolean visible[][];
    private final boolean[][] visionOffsets;
    private long turnStartTime;
    private final Ilk map[][];
    private final boolean[][] myAnts;
    private final boolean[][] enemyAnts;
    private final boolean[][] myHills;
    private final boolean[][] enemyHills;
    private final boolean[][] foodTiles;
    private final Set<Order> orders = new HashSet<Order>();

    /**
     * Creates new {@link Ants} object.
     * 
     * @param loadTime timeout for initializing and setting up the bot on turn 0
     * @param turnTime timeout for a single game turn, starting with turn 1
     * @param width game map height
     * @param height game map width
     * @param turns maximum number of turns the game will be played
     * @param viewRadius2 squared view radius of each ant
     * @param attackRadius2 squared attack radius of each ant
     * @param spawnRadius2 squared spawn radius of each ant
     */
    public Ants(int loadTime, int turnTime, int width, int height, int turns, int viewRadius2, int attackRadius2, int spawnRadius2) {
    	myAnts = new boolean[width][height];
    	enemyAnts = new boolean[width][height];
    	myHills = new boolean[width][height];
    	enemyHills = new boolean[width][height];
    	foodTiles = new boolean[width][height];
    	
        this.loadTime = loadTime;
        this.turnTime = turnTime;
        this.height = width;
        this.width = height;
        this.turns = turns;
        this.viewRadius2 = viewRadius2;
        this.attackRadius2 = attackRadius2;
        this.spawnRadius2 = spawnRadius2;
        map = new Ilk[width][height];
        for (Ilk[] row : map) {
            Arrays.fill(row, Ilk.LAND);
        }
        
        visible = new boolean[width][height];
        for (boolean[] row : visible) {
            Arrays.fill(row, false);
        }
        
        // calc vision offsets
        visionOffsets = new boolean[width][height];
        int mx = (int)Math.sqrt(viewRadius2);
        for (int row = -mx; row <= mx; ++row) {
            for (int col = -mx; col <= mx; ++col) {
                int d = row * row + col * col;
                if (d <= viewRadius2) {
                	//visionOffsets[row][col] = true;
                }
            }
        }
    }

    /**
     * Returns timeout for initializing and setting up the bot on turn 0.
     * 
     * @return timeout for initializing and setting up the bot on turn 0
     */
    public int getLoadTime() {
        return loadTime;
    }

    /**
     * Returns timeout for a single game turn, starting with turn 1.
     * 
     * @return timeout for a single game turn, starting with turn 1
     */
    public int getTurnTime() {
        return turnTime;
    }

    /**
     * Returns game map height.
     * 
     * @return game map height
     */
    public int getRows() {
        return height;
    }

    /**
     * Returns game map width.
     * 
     * @return game map width
     */
    public int getCols() {
        return width;
    }

    /**
     * Returns maximum number of turns the game will be played.
     * 
     * @return maximum number of turns the game will be played
     */
    public int getTurns() {
        return turns;
    }

    /**
     * Returns squared view radius of each ant.
     * 
     * @return squared view radius of each ant
     */
    public int getViewRadius2() {
        return viewRadius2;
    }

    /**
     * Returns squared attack radius of each ant.
     * 
     * @return squared attack radius of each ant
     */
    public int getAttackRadius2() {
        return attackRadius2;
    }

    /**
     * Returns squared spawn radius of each ant.
     * 
     * @return squared spawn radius of each ant
     */
    public int getSpawnRadius2() {
        return spawnRadius2;
    }

    /**
     * Sets turn start time.
     * 
     * @param turnStartTime turn start time
     */
    public void setTurnStartTime(long turnStartTime) {
        this.turnStartTime = turnStartTime;
    }

    /**
     * Returns how much time the bot has still has to take its turn before timing out.
     * 
     * @return how much time the bot has still has to take its turn before timing out
     */
    public int getTimeRemaining() {
        return turnTime - (int)(System.currentTimeMillis() - turnStartTime);
    }

    /**
     * Returns ilk at the specified location.
     * 
     * @param tile location on the game map
     * 
     * @return ilk at the <cod>tile</code>
     */
    public Ilk getIlk(Tile tile) {
        return map[tile.getCol()][tile.getRow()];
    }

    /**
     * Sets ilk at the specified location.
     * 
     * @param tile location on the game map
     * @param ilk ilk to be set at <code>tile</code>
     */
    public void setIlk(Tile tile, Ilk ilk) {
        map[tile.getCol()][tile.getRow()] = ilk;
    }

    /**
     * Returns ilk at the location in the specified direction from the specified location.
     * 
     * @param tile location on the game map
     * @param direction direction to look up
     * 
     * @return ilk at the location in <code>direction</code> from <cod>tile</code>
     */
    public Ilk getIlk(Tile tile, Aim direction) {
        Tile newTile = getTile(tile, direction);
        return map[newTile.getCol()][newTile.getRow()];
    }

    /**
     * Returns location in the specified direction from the specified location.
     * 
     * @param tile location on the game map
     * @param direction direction to look up
     * 
     * @return location in <code>direction</code> from <cod>tile</code>
     */
    public Tile getTile(Tile tile, Aim direction) {
        int row = (tile.getRow() + direction.getRowDelta()) % height;
        if (row < 0) {
            row += height;
        }
        int col = (tile.getCol() + direction.getColDelta()) % width;
        if (col < 0) {
            col += width;
        }
        return new Tile(col, row);
    }

    /**
     * Returns location with the specified offset from the specified location.
     * 
     * @param tile location on the game map
     * @param offset offset to look up
     * 
     * @return location with <code>offset</code> from <cod>tile</code>
     */
    public Tile getTile(Tile tile, Tile offset) {
        int row = (tile.getRow() + offset.getRow()) % height;
        if (row < 0) {
            row += height;
        }
        int col = (tile.getCol() + offset.getCol()) % width;
        if (col < 0) {
            col += width;
        }
        return new Tile(col, row);
    }

    /**
     * Returns a set containing all my ants locations.
     * 
     * @return a set containing all my ants locations
     */
    public boolean[][] getMyAnts() {
        return myAnts;
    }

    /**
     * Returns a set containing all enemy ants locations.
     * 
     * @return a set containing all enemy ants locations
     */
    public boolean[][] getEnemyAnts() {
        return enemyAnts;
    }

    /**
     * Returns a set containing all my hills locations.
     * 
     * @return a set containing all my hills locations
     */
    public boolean[][] getMyHills() {
        return myHills;
    }

    /**
     * Returns a set containing all enemy hills locations.
     * 
     * @return a set containing all enemy hills locations
     */
    public boolean[][] getEnemyHills() {
        return enemyHills;
    }

    /**
     * Returns a set containing all food locations.
     * 
     * @return a set containing all food locations
     */
    public boolean[][] getFoodTiles() {
        return foodTiles;
    }

    /**
     * Returns all orders sent so far.
     * 
     * @return all orders sent so far
     */
    public Set<Order> getOrders() {
        return orders;
    }

    /**
     * Returns true if a location is visible this turn
     *
     * @param tile location on the game map
     *
     * @return true if the location is visible
     */
    public boolean isVisible(Tile tile) {
        return visible[tile.getCol()][tile.getRow()];
    }

    /**
     * Calculates distance between two locations on the game map.
     * 
     * @param t1 one location on the game map
     * @param t2 another location on the game map
     * 
     * @return distance between <code>t1</code> and <code>t2</code>
     */
    public int getDistance(Tile t1, Tile t2) {
        int rowDelta = Math.abs(t1.getRow() - t2.getRow());
        int colDelta = Math.abs(t1.getCol() - t2.getCol());
        rowDelta = Math.min(rowDelta, height - rowDelta);
        colDelta = Math.min(colDelta, width - colDelta);
        return rowDelta * rowDelta + colDelta * colDelta;
    }

    /**
     * Returns one or two orthogonal directions from one location to the another.
     * 
     * @param t1 one location on the game map
     * @param t2 another location on the game map
     * 
     * @return orthogonal directions from <code>t1</code> to <code>t2</code>
     */
    public List<Aim> getDirections(Tile t1, Tile t2) {
        List<Aim> directions = new ArrayList<Aim>();
        if (t1.getRow() < t2.getRow()) {
            if (t2.getRow() - t1.getRow() >= height / 2) {
                directions.add(Aim.NORTH);
            } else {
                directions.add(Aim.SOUTH);
            }
        } else if (t1.getRow() > t2.getRow()) {
            if (t1.getRow() - t2.getRow() >= height / 2) {
                directions.add(Aim.SOUTH);
            } else {
                directions.add(Aim.NORTH);
            }
        }
        if (t1.getCol() < t2.getCol()) {
            if (t2.getCol() - t1.getCol() >= width / 2) {
                directions.add(Aim.WEST);
            } else {
                directions.add(Aim.EAST);
            }
        } else if (t1.getCol() > t2.getCol()) {
            if (t1.getCol() - t2.getCol() >= width / 2) {
                directions.add(Aim.EAST);
            } else {
                directions.add(Aim.WEST);
            }
        }
        return directions;
    }

    /**
     * Clears game state information about my ants locations.
     */
    public void clearMyAnts() {
    	for (int x=0; x < width; x++) {
    		for (int y=0; y < height; y++) {
    			if (myAnts[x][y] == true) {
    				myAnts[x][y] = false;
    				map[x][y] = Ilk.LAND;
    			}
    		}
    	}
    }

    /**
     * Clears game state information about enemy ants locations.
     */
    public void clearEnemyAnts() {
    	for (int x=0; x < width; x++) {
    		for (int y=0; y < height; y++) {
    			if (enemyAnts[x][y] == true) {
    				enemyAnts[x][y] = false;
    				map[x][y] = Ilk.LAND;
    			}
    		}
    	}
    }

    /**
     * Clears game state information about food locations.
     */
    public void clearFood() {
    	for (int x=0; x < width; x++) {
    		for (int y=0; y < height; y++) {
    			if (foodTiles[x][y] == true) {
    				foodTiles[x][y] = false;
    				map[x][y] = Ilk.LAND;
    			}
    		}
    	}
    }

    /**
     * Clears game state information about my hills locations.
     */
    public void clearMyHills() {
    	for (int x=0; x < width; x++) {
    		for (int y=0; y < height; y++) {
    			myHills[x][y] = false;
    		}
    	}
    }

    /**
     * Clears game state information about enemy hills locations.
     */
    public void clearEnemyHills() {
    	for (int x=0; x < width; x++) {
    		for (int y=0; y < height; y++) {
    			enemyHills[x][y] = false;
    		}
    	}
    }

    /**
     * Clears game state information about dead ants locations.
     */
    public void clearDeadAnts() {
        //currently we do not have list of dead ants, so iterate over all map
        for (int x=0; x < width; x++) {
            for (int y=0; y < height; y++) {
                if (map[x][y] == Ilk.DEAD) {
                    map[x][y] = Ilk.LAND;
                }
            }
        }
    }

    /**
     * Clears visible information
     */
    public void clearVision() {
        for (int x=0; x < width; ++x) {
            for (int y=0; y < height; ++y) {
                visible[x][y] = false;
            }
        }
    }

    /**
     * Calculates visible information
     */
    public void setVision() {
    	for (int x=0; x < width; x++) {
    		for (int y=0; y < height; y++) {
    			if (visionOffsets[x][y] == true) {
    				visible[x][y] = true;
    			}
    		}
    	}
    }

    /**
     * Updates game state information about new ants and food locations.
     * 
     * @param ilk ilk to be updated
     * @param tile location on the game map to be updated
     */
    public void update(int y, int x, Ilk ilk) {
        map[x][y] = ilk;
        switch (ilk) {
            case FOOD:
                foodTiles[x][y] = true;
                break;
            case MY_ANT:
                myAnts[x][y] = true;;
                break;
            case ENEMY_ANT:
                enemyAnts[x][y] = true;
                break;
        }
    }

    /**
     * Updates game state information about hills locations.
     *
     * @param owner owner of hill
     * @param tile location on the game map to be updated
     */
    public void updateHills(int y, int x, int owner) {
        if (owner > 0)
            enemyHills[x][y] = true;
        else
            myHills[x][y] = true;
    }

    /**
     * Issues an order by sending it to the system output.
     * 
     * @param myAnt map tile with my ant
     * @param direction direction in which to move my ant
     */
    public void issueOrder(Ant myAnt, Direction direction) {
        Order order = new Order(myAnt, direction);
        orders.add(order);
        System.out.println(order);
    }
}
