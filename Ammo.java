import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;   // import the list class

/**
 * The ammo is a cannon ball that gets spawned in the world when the cannon shoots. The ammo travels in the same direction it was shoot and has an animation to it
 * to look 3D. All units 150 pixels around the location the ammo lands, disappear. There is a possiblility for the cannon to kill the opposite base's sniper 
 * (however really low). All units that the ammo kills cannot be revived.
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
public class Ammo extends Actor
{

    private int speed =5;  // the speed of the cannon ball
    private int randomX;  // random x-coordinate on the screen
    private int randomY;  // random y-coordinate on the screen
    private int average;  // the average number of acts it will require the cannonball to reach the specific location
    private int halfWay;   // half the distance that the cannon ball should travel
    private int counter=15;  // variable serves as a counter
    private boolean countBackwards=false;  // variable that switches the counting variable from increasing to decreasing and vice versa
    private int act=0;   // the current act the cannon ball is at since its launching
    private double distance;  // the distance the cannon ball should cover
    private boolean stop=false;  // variable used to control the animation (true -stop the animation, false- continue the animation)
    private List <Units> units;   // a variable used to store all the units the ammo touches when it lands 
    
    /**
     * Constructs the ammo. Scales the ammo to a specific size and sets the location at which the ammo will be aimed at.
     * @param randomX- random x-coordinate on the screen (provided by the cannon)
     * @param randomY- random y-coordinate on the screen (provided by the cannon)
     */
    public Ammo(int randomX, int randomY){

        getImage().scale (15,15);  // scale the image to appear small at first
        this.randomX = randomX;  // random x-coordinate on the screen (generated by the cannon)
        this.randomY = randomY;  // random y-coordinate on the screen (generated by the cannon)

    }

    /**
     * Act - do whatever the Ammo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Displays the animation of the cannon ball.
     */
    public void act() 
    {
        
        getWorld().setPaintOrder (Cannon.class, StatDisplay.class, Ammo.class, Tree.class);  // cannon should appear on top of the ammo
        turnTowards (randomX, randomY);  // the ammo should keep turning towards a random spot on the screen
        move (speed);   // the ammo should keep moving towards that location
        if (stop ==false){  // if the stop variable is false, then

            animation();  // method responsible for shriking and making the ammo appear bigger on the screen

            act++;  // increase the current act by 1

            if (countBackwards == false){ // if you are not suppose to count backwards then,
                counter++;   // increase the counting variable
            }else {  // else if you are suppose to count backwards then,
                counter--;  // decrease the counting variable
            }

            if (counter<=15){   // if the counting variable is less than or equal to 15, then
                stop =true;  // stop the animation
                units=getObjectsInRange (150, Units.class);  // get all the units that are at least 150 pixels in range
                for (int i=0; i < units.size(); i++){   // loop through all the units
                    units.get(i).instanceDeath(false);   // kill all those units (no ability to get revived)
                    getWorld().removeObject (units.get(i));   // remove from world
                }
                getWorld().removeObject (this);   // remove this cannon ball from the world (the purpose has been served)
            }

        }
        
    }    

    /**
     * Animates the cannon ball in its journey towards a random position on the screen.
     * Increases the size of the cannon ball until half way and then starts to decrease its size. Imitates the appearance of a top
     * view of the battle field.
     */
    private void animation(){
        average = (int) calculateDistance(this)/speed;   // calculate the average distance the cannon ball should travel to reach the specific location
        halfWay= average/2;   // calculate the half distance 

        if (act> halfWay){   // if the cannon ball is half way through

            countBackwards = true;   // start counting backwards (the counter variable)

        }
        getImage().scale(counter,counter);   // the size of the cannon ball will be the same as the counting variable
        move (speed);  // the cannon ball will move towards the specfic location with the current speed

    }

    /**
     * Calculate the distance between the cannon ball and a random position on the screen.
     * @param actor - the cannon ball
     */
    private double calculateDistance(Actor actor){
        distance= Math.hypot(actor.getX() - randomX, actor.getY() - randomY);  // calculate the distance between the cannon ball and the specific location
        return distance;  // return that distance
    }

}