/**
 * Represents an order to be issued.
 */
public class Order {
    private final int x;
    private final int y;
    private final char direction;
    
    /**
     * Creates new {@link Order} object.
     * 
     * @param tile map tile with my ant
     * @param direction direction in which to move my ant
     */
    public Order(Ant ant, Direction direction) {
        x = ant.getX();
        y = ant.getY();
        this.direction = direction.getSymbol();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "o " + y + " " + x + " " + direction;
    }
}
