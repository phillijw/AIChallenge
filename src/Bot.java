/**
 * Provides basic game state handling.
 */
public abstract class Bot extends AbstractSystemInputParser {
    private Ants ants;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setup(int loadTime, int turnTime, int rows, int cols, int turns, int viewRadius2, int attackRadius2, int spawnRadius2) {
        setAnts(new Ants(loadTime, turnTime, rows, cols, turns, viewRadius2, attackRadius2, spawnRadius2));
    }
    
    /**
     * Returns game state information.
     * 
     * @return game state information
     */
    public Ants getAnts() {
        return ants;
    }
    
    /**
     * Sets game state information.
     * 
     * @param ants game state information to be set
     */
    protected void setAnts(Ants ants) {
        this.ants = ants;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void beforeUpdate() {
        ants.setTurnStartTime(System.currentTimeMillis());
        ants.clearMyAnts();
        ants.clearEnemyAnts();
        ants.clearMyHills();
        ants.clearEnemyHills();
        ants.clearFood();
        ants.clearDeadAnts();
        ants.getOrders().clear();
        ants.clearVision();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addWater(int row, int col) {
        ants.update(col, row, Ilk.WATER);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addAnt(int row, int col, int owner) {
        ants.update(col, row, owner > 0 ? Ilk.ENEMY_ANT : Ilk.MY_ANT);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addFood(int row, int col) {
        ants.update(col, row, Ilk.FOOD);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAnt(int row, int col, int owner) {
        ants.update(col, row, Ilk.DEAD);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addHill(int row, int col, int owner) {
        ants.updateHills(row, col, owner);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterUpdate() {
        ants.setVision();
    }
}
