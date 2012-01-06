import java.util.*;


public class MapTile {
	private Map map;
	
	private int x;
	private int y;
	
	private boolean isVisible = false;
	private boolean hasFood = false;
	private boolean isMyHill = false;
	private boolean isEnemyHill = false;
	private boolean isWater = false;
	private Ant ant;
	
	private MapTile north;
	private MapTile east;
	private MapTile south;
	private MapTile west;
	
	//Constructors
	public MapTile() { }
	
	public MapTile(Map m, int x, int y) {
		map = m;
		this.x = x;
		this.y = y;
	}
	
	public MapTile(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public MapTile(int x, int y, Ant a) {
		this.x = x;
		this.y = y;
		ant = a;
	}
	
	public MapTile(MapTile n, MapTile e, MapTile s, MapTile w) {
		setNeighbors(n, e, s, w);
	}
	
	//Parent map
	public void setMap(Map m) { map = m; }
	public Map getMap() { return map; }
	
	//Geo
	public int getX() { return x; }
	public int getY() { return y; }
	
	//Visible
	public void setIsVisible(boolean visible) { this.isVisible = visible; }
	public boolean isVisible() { return isVisible; }
	
	//Food
	public void setHasFood(boolean food) { this.hasFood = food; }
	public boolean hasFood() { return hasFood; }
	
	//Water
	public void setIsWater(boolean water) { this.isWater = water; }
	public boolean isWater() { return isWater; }
	
	//Ant
	public void setAnt(Ant ant) { this.ant = ant; }
	public Ant getAnt() { return ant; }
	public boolean hasAnt() { return ant != null; }
	
	//Neighbors
	public MapTile getNorth() { return north; }
	public MapTile getEast() { return east; }
	public MapTile getSouth() { return south; }
	public MapTile getWest() { return west; }
	
	public void setNorth(MapTile tile) { this.north = tile; }
	public void setEast(MapTile tile) { this.east = tile; }
	public void setSouth(MapTile tile) { this.south = tile; }
	public void setWest(MapTile tile) { this.west = tile; }
	public void setNeighbors(MapTile n, MapTile e, MapTile s, MapTile w) {
		north = n;
		east = e;
		south = s;
		west = w;
	}
	
	//Tile status
	public boolean isPassable() {
        return !isWater;
    }
	
	public boolean isOccupied() {
        return hasAnt();
    }
	
	public boolean isMyHill() {
		return isMyHill;
	}
	
	public boolean isEnemyHill() {
		return isEnemyHill;
	}
	
	//Math
	public double distance(MapTile t) {
		int x = this.x - t.getX();
		int y = this.y - t.getY();
		
		return Math.sqrt(x*x + y*y);
	}
	
	//Recursive stuff
	public List<MapTile> getPassableNeighbors() {
		List<MapTile> passableNeighbors = new ArrayList<MapTile>(4);
		if (north.isPassable()) { passableNeighbors.add(north); }
		if (east.isPassable()) { passableNeighbors.add(east); }
		if (south.isPassable()) { passableNeighbors.add(south); }
		if (west.isPassable()) { passableNeighbors.add(west); }
		return passableNeighbors;
	}
	                 
	public int getStepsFromFood() {
		if (!isPassable())
			return Integer.MAX_VALUE;
		else
			return getStepsFromFood(0);
	}
	
	public int getStepsFromFood(int n) {
		if (!isPassable() || !isVisible())
			return Integer.MAX_VALUE;
		
		if (hasFood()) {
			System.out.println("HAS FOOD: (" + x + "," + y + ")");
			return n;
		}
		else {
			System.out.println("CHECKING: (" + x + "," + y + ")");
			return n + Math.min((north != null ? north.getStepsFromFood(n+1) : Integer.MAX_VALUE),
					   Math.min((east != null ? east.getStepsFromFood(n+1) : Integer.MAX_VALUE),
				       Math.min((south != null ? south.getStepsFromFood(n+1) : Integer.MAX_VALUE),
						        (west != null ? west.getStepsFromFood(n+1) : Integer.MAX_VALUE))));
		}
	}
}
