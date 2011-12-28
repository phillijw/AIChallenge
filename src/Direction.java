import java.util.HashMap;
import java.util.Map;


public enum Direction {
	NORTH ( 0, -1, 'n'),
	EAST  ( 1,  0, 'e'),
	SOUTH ( 0,  1, 's'),
	WEST  (-1,  0, 'w');
	
	private final int x;
	private final int y;
	private final char abr;
	
	Direction(int x, int y, char abr) {
		this.x = x;
		this.y = y;
		this.abr = abr;
	}
		
    private static final Map<Character, Direction> symbolLookup = new HashMap<Character, Direction>();
    
    static {
        symbolLookup.put('n', NORTH);
        symbolLookup.put('e', EAST);
        symbolLookup.put('s', SOUTH);
        symbolLookup.put('w', WEST);
    }
    
    public static Direction fromSymbol(char symbol) {
        return symbolLookup.get(symbol);
    }

	public char getSymbol() {
		return abr;
	}
}
