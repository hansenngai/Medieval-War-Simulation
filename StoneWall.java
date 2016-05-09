import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The Stone Wall acts as a barrier for the sniper. The user can specify which side the wall is for. Initially, each side has one stone wall.
 * Currently the wall serves no logistic purpose but could be used to store hit points and get incorporated in the stimulation.
 * 
 * @author Hansen Ngai
 * @version 0.03
 */
public class StoneWall extends Buildings
{
    /**
     * Creates a stone wall for an indicated side.
     * 
     * @param inputSide Indicates which side the stone wall is for. True for one, and false for the other.
     */
    public StoneWall(boolean inputSide){
        this.side = inputSide; //user inputted side
        hitPoints = 100;//wall has 100 hit points
    }

    /**
     * Creates a stone wall for an indicated side with a predetermined starting hit points.
     * 
     * @param inputSide Indicates which side the stone wall is for. True for one, and false for the other.
     * @param hitPointsIn The amount of hit points the wall starts with.
     */
    public StoneWall(boolean inputSide, int hitPointsIn){
        this.side = inputSide; //user inputted side
        this.hitPoints = hitPointsIn;//user inputted initial hit points
    }

    /**
     * Sets the Stone Wall's current hit points. Used to decrease 
     * the hit points of the stone wall.
     * 
     * @param hitPoints the new hit point the stone wall will now have
     */
    public void setHitPoints(int hitPoints)
    {
        this.hitPoints = hitPoints; //sets the amount of hit points
    }

}
