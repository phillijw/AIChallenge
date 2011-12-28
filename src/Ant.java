import java.util.Random;


public class Ant {
	private AntRole role = AntRole.UNKNOWN;
	private MapTile tile;
	private Party party;
	
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

	public void moveRandom() {
		Random r = new Random();
		int i = r.nextInt(Direction.values().length);
		
		Ants state = tile.getMap().getBot().getAnts();
		state.issueOrder(this, Direction.values()[i]);		
	}
}