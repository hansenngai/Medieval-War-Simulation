 import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Storage facility class that keeps track of the amount of storage point a team has, 
 * also allowing increases and decreases in storage points and can return the current amount of 
 * storage points to be checked by Base class to see if a troop can be spawn.
 * 
 * @author Isaac Chang
 * @version 0.0.1
 */
public class Storage extends Buildings
{
    //instance variables
    private int storagePoint;  // variable that will store storage points

    /**
     * Creates a Storage building that has the default hitPoints and 
     * side that needs to be specified 
     * 
     * @param sideIn    the side the Storage facility is on, true for one side, false for the other
     */
    public Storage(boolean sideIn)
    {
        hitPoints = 1000; //storage can bear hit points (not currently used in the stimulation)
        side = sideIn;  // sets which side this storage belongs too.
        storagePoint  = 0;  // set the initial storage points to be 0
    }

    /**
     * Creates a Storage facility that requires a specified initial 
     * side, hitPoint, and initial storage point
     *
     * @param sideIn            the side the Storage facility is on, true for one side, false for the other
     * @param storagePointIn    the inital storagePoint the storage facility will start off with
     */
    public Storage(boolean sideIn, int storagePointIn)
    {
        hitPoints = 1000; //storage can bear hit points (not currently used in the stimulation)
        side = sideIn;    // sets which side this storage belongs too.
        storagePoint  = storagePointIn;  // sets the storage points to the required points
    }

    /**
     * Modifies storagePoint amount to a new amount 
     * 
     * @param storagePoint  the new storagePoint amount that the storage facility will now have
     */
    public void setStoragePoint(int storagePoint)
    {
        this.storagePoint = storagePoint;
    }

    /**
     * Returns current storagePoint amount 
     *
     * @return int  the current storagePoint amount
     */
    public int getStoragePoint()
    {
        return storagePoint;
    }       
}
