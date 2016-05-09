import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * User clicks the button to progress to the loading bar.
 * 
 * @author Isaac Chang
 * @version 0.0.1
 */
public class StartButton extends Actor
{    
    /**
     * Act - do whatever the StartButton wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Checks to see if the user clicks the mouse on the image.
     */
    public void act() 
    {
        checkClicked();  // check if the user clicked the button
    }    
    
    /**
     * Checks to see if this object is clicked and if it is, 
     * instantiate a new loading world using a method in the start world class.
     */
    private void checkClicked()
    {
        if (Greenfoot.mouseClicked(this) == true)  // if user clicks this image, then
        {
            Start start = (Start)getWorld();  // storing the current world
            start.goToLoading();  // access the loading method (from the world) that will take the user to the loading page.
        }
    }
}
