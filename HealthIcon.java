import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * HealthIcon is a Greenfoot Actor. HealthIcons are objects that the simulation utilizes to display the amount of lives in which a soldier or specific vehicle has. The amount of lives in which an actor currently
 * has will directly correspond to the number of HealthIcons hovering above it. HealthIcons will always follow its target actor throughout the simulation.
 * 
 * @author Nicholas Chan
 * @version 0.0.3
 */
public class HealthIcon extends Actor
{
    //Initializing instance variables
    private int number;     //The number of the health icon that is spawned in, helps to determine location with respect to actor
    private Actor target;   //The actor in which the health icon will be following 
    
    /**
     * Creates an instance of HealthIcon. 
     * @param number Determines the number associated with the HealthIcon
     * @param target Determines the actor that the HealthIcon will be following
     */

    public HealthIcon (int number, Actor target){
        this.number = number;   // store the number of health icons to be displayed on the screen
        this.target = target;   // store the actor that will bear the health icons
    }

    /**
     * The act method for the HealthIcon class. Retrieves the current location of the target on the screen and sets the location of the HealthIcon 
     * based on these coordinates. Each HealthIcon has a different offset based on its corresponding number.
     */
    public void act() 
    {
        this.setRotation(target.getRotation()); //Sets the rotation of the HealthIcon to the current rotation of the target actor
        
        //If statements that adjust the offset of the HealthIcon depending on its number. For example, if number = 1, HealthIcon's location will always be 7 
        //7 pixels to the right of the target and 30 pixels upwards of it. Offsets are used to prevent overlapping of the HealthIcons
        if (this.number == 1){
            setLocation (target.getX() + 7, target.getY()-30);
        }
        else if (this.number == 2){
            setLocation (target.getX() - 3, target.getY()-30);
        }
        else if (this.number == 3){
            setLocation (target.getX() - 13, target.getY()-30);
        }
        else if (this.number == 4){
            setLocation (target.getX() - 23, target.getY()-30);
        }
        else if (this.number == 5){
            setLocation (target.getX() - 33, target.getY()-30); 
        }   
    }    
}
