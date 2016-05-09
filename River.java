import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An animation at the top of the screen that the ship can travel through
 * 
 * @author Isaac Chang 
 * @version 0.0.1
 */
public class River extends Actor
{
    //instance variables that control when to flip between the images
    private boolean tideCheck = false;
    private boolean waveCheck = false;

    private int count = 0;  // serves as a counter to determine when to change the images of the river

    /**
     * Act - do whatever the River wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls the animation of the river.
     */
    public void act() 
    {
        moveWave();  // call the method responsible for switching between the different images of the river
    }   

    /**
     * Switching between 2 images to create wave effect
     */
    private void moveWave()
    {
        if (waveCheck == false)  // if the wave image is not on screen,
        {
            count++;  // increase the counter by one
            
        }        
        if (count == 20 && waveCheck == false)  // if the wave image is not on screen and counter reaches 20, then
        {
            
            setImage("RiverNext.png");  // switch to next image (wave)
            waveCheck = true;  // the wave is on the screen
        }
        if (waveCheck == true)  // if the wave is on the screen, then
        {
            
            count--;  // decrease the counting variable by one
        }
        if (count == 0 && waveCheck == true)  // if the wave is on the screen and the counter reaches 0, then
        {
            
            setImage("River.png");  // switch to the next image (tide)
            waveCheck = false;  // the wave is not on the screen
        }
    }
}
