import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * a fade that can be used between screen transitions
 * 
 * @author Isaac Chang
 * @version 0.0.1
 */
public class BlackFade extends Actor
{
    //instance variables
    private int transparency;  // counter that countrols the transparency
    private boolean check; //false means fade to black, true means fade from black to transparent
    private boolean checkDone = false;  // variable checks to see if the fade is complete

    /**
     * Creates Fade that fades from black to transparent
     * @param check - true fade from black to transparent, false- fade to black
     */
    public BlackFade(boolean check)
    {
        this.check = check;  // true fade from black to transparent, false- fade to black
        
        if (check == true)  // if the user wants to slowly show the screen
        {
            transparency = 255;  // it should be completely opaque to start with
        }
        else if (check == false)  // if user wants to set the screen to slowly diappear, then
        {
            transparency = 0;    // it should be completely transparent to start with (then increasing the degree for opaqueness to appear fading away)
        }
    }   

    /**
     * Act - do whatever the BlackFade wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls the animation for the fade
     */
    public void act() 
    {
        //for fading into black, so leaving to another world
        if (check == false)
        {
            if (transparency < 255)  // if the transparency is below 255 (still not completely opaque), then
            {
                transparency = transparency+1;  // increase the transparency
            }
            getImage().setTransparency(transparency);  // apply the new transparency to the stimulation
            if (transparency == 255)  // once it reaches opaque, 
            {              
                checkDone = true;  // set the fade as finished
                getWorld().removeObject(this);  // remove this from the world
            }
        }
        //for fading from black so entering another world
        else if (check == true)
        {
            if (transparency > 0)  // if the tranparency is below 0 (still not completely transparent), then
            {
                transparency = transparency-1;  // decrease the transparency
            }
            getImage().setTransparency(transparency);  // apply the new transparency to the stimulation
            if (transparency == 0)  // once the transparency is completely tranparent
            {
                checkDone = true;  // set the fade as finished 
                getWorld().removeObject(this);  // remove this from the world
            }
        }
    }   
    
    /**
     *  Gets the checkDone to see if the fade is finished fading
     *  
     *  @return boolean     the status of whether the black fade is done fading
     */
    public boolean getCheckDone()
    {
        return checkDone;  // returns whether the fade is done or not
    }
}
