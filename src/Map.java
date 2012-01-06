import java.util.*;

public class Map {
	private Bot bot;
	private int width=0;
	private int height=0;
	private MapTile[][] tiles;
	
	public Map() { }
	
	public Map(Bot b, int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new MapTile[width][height];
		bot = b;
	}
	
	public void setTile(MapTile t, int x, int y) {
		tiles[x][y] = t;
	}
	
	public void setBot(Bot b) { bot = b; }
	public Bot getBot() { return bot; }
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public MapTile getTile(int x, int y) {
		return tiles[x][y];
	}
	
	public List<MapTile> getTiles() {
		ArrayList<MapTile> tiles = new ArrayList<MapTile>(width*height);
		for (int x=0; x < width; x++) {
			for (int y=0; y < height; y++) {
				tiles.add(this.getTile(x, y));
			}
		}
		return tiles;
	}
	
	public MapTile getNearestFood(int x, int y) {
		MapTile base = getTile(x, y);
		MapTile closest = new MapTile(Integer.MAX_VALUE, Integer.MAX_VALUE);
		MapTile cur;

		for (int i=0; i < width; i++) {
			for (int j=0; j < height; j++) {
				if (getTile(i, j).hasFood()) {
					cur = getTile(i, j); 
					closest = (base.distance(cur) <= base.distance(closest)) ? cur : closest;
				}
			}
		}
		
		return closest;
	}
	
	public void print() {
		String item = "?";
		System.out.println();
		System.out.println();
		for (int x=0; x < width; x++) {
			for (int y=0; y < height; y++) {
				if (getTile(x, y).hasAnt())
					item = "a";
				else if (getTile(x, y).hasFood())
					item = "*";
				else if (getTile(x, y).isWater())
					item = "%";
				else if (getTile(x, y).isPassable())
					item = ".";
				
				System.out.print(item);
				
				if (y == height-1)
					System.out.println();
			}
		}
		System.out.println();
		System.out.println();
	}
}
