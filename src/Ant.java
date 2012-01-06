import java.util.List;
import java.util.Random;


public class Ant {
	private AntRole role = AntRole.UNKNOWN;
	private MapTile tile;
	private Party party;
	private MapTile nextMove;
	
	public Ant() { }
	
	public Ant(MapTile tile, AntRole role) {
		this.tile = tile;
		this.role = role;
	}
	
	public boolean isEnemy() {
		return role == AntRole.ENEMY;
	}
	
	public AntRole getRole() {
		return role;
	}
	
	public void move(Direction dir) {
		Ants state = tile.getMap().getBot().getAnts();

		state.issueOrder(this, dir); //This needs to be fixed. Give MyBot an issueOrder() method
	}
	
	public int getX() { return tile.getX();	}
	public int getY() { return tile.getY(); }
	public MapTile getTile() { return tile; }

	public void moveRandom() {
		Random r = new Random();		
		int i = r.nextInt(Direction.values().length);
		Direction d = Direction.values()[i];
		
		MapTile northNeighbor = tile.getNorth();
		MapTile eastNeighbor = tile.getEast();
		MapTile southNeighbor = tile.getSouth();
		MapTile westNeighbor = tile.getWest();
		
		if (d == Direction.NORTH && northNeighbor != null && northNeighbor.isPassable() && !northNeighbor.isOccupied()) { 
			move(Direction.NORTH);
		}
		else if (d == Direction.EAST && eastNeighbor != null && eastNeighbor.isPassable() && !eastNeighbor.isOccupied()) { 
			move(Direction.EAST);
		}
		else if (d == Direction.SOUTH && southNeighbor != null && southNeighbor.isPassable() && !southNeighbor.isOccupied()) { 
			move(Direction.SOUTH);
		}
		else if (d == Direction.WEST && westNeighbor != null && westNeighbor.isPassable() && !westNeighbor.isOccupied()) { 
			move(Direction.WEST);
		}
		
		move(d);
	}
	
	public List<MapTile> getPassableNeighbors() {
		return tile.getPassableNeighbors();
	}
	
}