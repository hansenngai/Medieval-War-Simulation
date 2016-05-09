import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A tombstone gets displayed every time a soldier dies (who can be revived). Disappears once the medic healed that soldier.
 * 
 * @author (Nicholas Chan) 
 * @version (0.0.6)
 */
public class Tombstone extends Actor
{
    private boolean side;  // the side of the tombstone
    
    /**
     * Constructs a tombstone. Sets which side the tombstone belongs to.
     * @param side - true - left side, false -right side
     */
    public Tombstone(boolean side){
        this.side =side;  // store which side the tombstone will belong to
    }

    /**
     * Returns which side the tombstone belongs to.
     * @return true -left side, false-right side
     */
    public boolean getSide(){
        return side;
    }
}
