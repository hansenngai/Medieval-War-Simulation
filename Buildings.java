import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The buildings class is further divided into 4 categories: the mainbase, the stonewall, the research facility and the storage. Each builing has its own purpose
 * in the stimulation and control the logistics behind it.
 * 
 * @author (Issac Cheung) 
 * @version (0.0.8)
 */
public abstract class Buildings extends Actor
{
    protected int hitPoints;  // the hit points of the buildings
    protected boolean side;   // the side of which the buildings belong to
    
    
    /**
     * Returns current hitPoints amount of building
     *
     * @return int  the current hit point of the main base
     */
    protected int getHitPoints()
    {
        return hitPoints;
    }
    
    /**
     * Returns the side the building is on
     * 
     * @return boolean  the side the buildin is on, true - left side, false -right side
     */
    protected boolean getSide()
    {
        return side;
    }
}
