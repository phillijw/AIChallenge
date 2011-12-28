
public class MapTile {
	private Map map;
	
	private int x;
	private int y;
	
	private boolean isVisible = false;
	private boolean hasFood = false;
	private boolean isMyHill = false;
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
	
	//Ant
	public void setAnt(Ant ant) { this.ant = ant; }
	public Ant getAnt() { return ant; }
	public boolean hasAnt() { return ant != null; }
	
	//Neighbors
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
	
	//public Tile getTile() {
	//	return this.map.getBot().getAnts()
	//}
	
	//Math
	public double distance(MapTile t) {
		int x = this.x - t.getX();
		int y = this.y - t.getY();
		
		return Math.sqrt(x*x + y*y);
	}
}
