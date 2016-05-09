import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The winning page displays after one base triumphed over the other. The winning pages states who won.
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.6)
 */
public class WinningPage extends World
{
    private boolean side;  // the winning side

    /**
     * Constructor the WinningPage. Identifies the winning side
     * @param side true - left side won, false- right side won
     * 
     */
    public WinningPage(boolean side)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(530, 384, 1); 

        this.side =side;  // sets the winning side
    }

    /**
     * Diplays the winning screen.
     */
    public void act(){
        if (this.side ==false){  // if the winning side is the orange one then,
            setBackground ("base2Winner.png");  // set the image that states that the orange side one- default it white
        }

    }
}
