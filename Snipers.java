import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  // import the list class

/**
 * Each base will have its own sniper. There can only be one sniper for each base throughout the whole stimulation. The sniper can only die if hit by a cannon, in which
 * case he base becomes very vulnerable and the other side increased its chances of winning. The sniper's goal is to kill any soldier from the other side who
 * comes close to the base.
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
public class Snipers extends Soldiers
{

    /**
     * Constructs the snipers. Defines the side the sniper comes from.
     * @param side- which base the horse will come from true - right side, false- left side
     */
    public Snipers(boolean side){  // determine the side of the sniper
        this.side = side;  // set the side of the sniper
    }

    /**
     * Shoot at the soldiers from the other side if they come close to the base.
     */
    public void act() 
    {

        counter = Greenfoot.getRandomNumber(31)+5;  // random number (5- 35) that detemines when the soldier will shoot-changes every act (which makes it EXTREMELY random because the number has to be divisible by 20 for the shooting to occur)
        randomMiss = Greenfoot.getRandomNumber(20);  // the amount of miss in pixels (the arrow will go a certain number of pixels beside the target)
        cofficient = Greenfoot.getRandomNumber(2);   // variable used to determine whether the soldier will miss (to the right or left) of the target
        if (cofficient ==1){  // if the cofficient is 1, then the miss will be higher (above the soldier) - appears to be to left/right of the soldier depending on the type of soldier (white- to the left of him/ orange- to the right of him)
            randomMiss = -randomMiss;
        }
        counter ++;   // increase the counting variable by one - will only affect every random number once

        if (checkCloseSoldiers (!side) && aim () && checkTree()){ // if there is a nearby soldier whose from the opposite side and there is no tree/soldier from your side (reduce friendly fire) between the sniper and the shooter 
            shoot(); // shoot at the soldier -shooting is randomized every act
        }
        //getWorld().setPaintOrder (Arrow.class, Snipers.class, StoneWall.class);

    }    

    /**
     * Shoots at the nearby soldiers. Does not work every time it is called.
     * The counter variable should be divisible by 25 for the shooting to take place.
     */    
    protected void shoot(){

        if (counter%25==0){   // the counter variable should be divisible by 25 for the shooting to occur
            Greenfoot.playSound("bow.mp3");
            getWorld().addObject (new Arrow(this.getRotation()+randomMiss, false), this.getX(), this.getY()+3);  // spawn an arrow on the screen with the rotation equal to or slightly off (randomMiss) of the target
        } 

    }

}
