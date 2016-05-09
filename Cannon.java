import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A cannon is spawn on the screen if the reaseach facility reaches 4 (last level). The cannon doesn't aim at anything specific, but rather shoots at a random spot
 * on the screen. Therefore a possiblity of a friendly fire to occur is high. All units 150 pixels around the location the ammo lands, disappear. There is a possiblility
 * for the cannon to kill the opposite base's sniper (however really low).
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
public class Cannon extends Actor
{

    private boolean stop=true;  // variable controls when to shoot the cannon (true- don't shoot, false -shoot)
    private int randomX,randomY;  // random x and y coordinates that the cannon will shoot at.
    private int act=0;

    /**
     * Act - do whatever the Cannon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Generates a random number that the cannon will shoot at
     */
    public void act() 
    {

        if (stop ==false){  // if stop is equal to false, then
            randomX = Greenfoot.getRandomNumber (591)+250;  // generate a random number that will be used for the x-coordinate
            randomY = Greenfoot.getRandomNumber (521) +80;   // generate a random number that will be used for the y-coordinate
            turnTowards (randomX, randomY);   // turn towards that random position
            fire();  // fire - spawn a cannon ball on the screen that will travel towards that random location
            stop = true;  // close the switch - don't fire again until this switch is turned on again.
        }
        act++;
        if (act%900 == 0){
            stop =false;	
        }

    }    

    /**
     * Spawns a cannon ball on the screen. Gets called by the act method.
     */
    private void fire(){
        getWorld().addObject (new Ammo (randomX, randomY), getX(), getY()+2);  // spawn a cannon ball to the screen with the same rotation as this cannon
        Greenfoot.playSound("cannon2.mp3");
    }

    /**
     * Shoots the cannon. (The random coordinates are in the act method)
     */
    public void shootCannon(){
        stop =false;  // turn the switch for firing on

    }
}
