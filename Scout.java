import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Scout is a Greenfoot Actor. A scout's purpose is to retrieve supplies either through collecting supply drops (crates) and/or through "stealing" supplies from an enemy's storage. A scout will be spawned in
 * whenever there are crates that are available to be picked up from the battlefield; it will not leave the battlefield until it as picked up ALL POSSIBLE crates, and at this point, will then return to the base
 * in which it came from. Periodically, when storage points are low, scouts will randomly leave base to "steal supplies" from the enemy storage, and will then return back to base. Like medics, scouts do not have 
 * lives as they are not targetted by enemeies. Scouts move slightly faster than Medics and BareFoots.
 * 
 * @author Nicholas Chan & Ramy Elbakari
 * @version (0.0.3)
 */
public class Scout extends Helpers
{
    
    private boolean returnToBase = false;   //Boolean used to determine if the scout should return to its base
    private boolean returned=false;   // Boolean the indicates whether the scout returned to his base or not
    
    private List <Crate> crates;    //Intializing an array list of crates
    private Crate target;   //Creating an instance of Crate called target

    /**
     * Creates an instance of Scout. Set the speed of the scout and which base it comes from.
     * 
     * @param side Determines the side in which the scout belongs to
     * @param b Determines the base the scout will belong to
     */
    public Scout(boolean side, MainBase base)
    {
        this.speed = 2; //Setting the speed of a scout
        this.side = side;   //Setting the side in which the scout belongs to
        this.base = base; //Setting the scout's base

    }

    /**
     * Act - do whatever the Scouts wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls the movement of the scout in different situations
     */
    public void act() 
    {
        //If there are crates on the field and the scout is not yet required to return back to base, it walks to the nearest crate
        if (locateNearestCrate() && target!= null && returnToBase == false)
        {
            walkToNearestCrate();
        }

        //If the scout comes into contact with a crate, the crate is removed from the world
        if (checkHitCrate() == true)
        {
            getWorld().removeObject(target);       
        }        


        //If no more crates are available to pick up on the field, the scout returns back to its base
        if (checkCratesInWorld())
        {
            returnToBase = true;
        }

        //If it collides with an obstacle, it moves around it instead
        while(checkObstacle())
        {
            turn (15);                 
            move (1);
        }

        //If returnToBase is set to true, this commands the scout to return to its base
        if (returnToBase == true)
        {
            returnToBase(); 
        }
    }    

    /**
     * Determines out of the possible crates on the screen, the crate that is closest to the medic (same side)
     */
    protected void determineClosestTarget()
    {
        for (int i=0; i< crates.size(); i++)  // loop through all the crates in the world
        {
            nearestDistance = calculateNearestActor(crates.get(i));  // calculate the distance between this crate and the scout
            if (nearestDistance < distance && crates.get(i).getSide() == this.side)   // if the crate belongs to your side and is the nearest crate to the medic, then
            {
                distance = nearestDistance;  // the distance will be stored to test the remaining crates
                target = crates.get(i);   // this tombstone will be stored as the closest for now
            }
        }
        distance = 1100;  // set the distance back to a huge number so this method can be used again
    }

    /**
     * Locates the nearest crate and defines it to be used in the walkToNearestCrate method.
     */
    private boolean locateNearestCrate()
    {
        if (this.getWorld()!=null){ // if the scout is in the world, then
            crates = getWorld().getObjects(Crate.class);  //Locates all crates in the world
            for (int i=0; i< crates.size(); i++)  // loop through all the crates in the list
            {
                nearestDistance = calculateNearestActor(crates.get(i));  // calculate the distance between this crate and the medic 
                if (nearestDistance < distance && crates.get(i).getSide() == this.side)  // if the crate belongs to your side and is the nearest crate to the medic, then
                {
                    distance = nearestDistance;  // the distance will be stored to test the remaining crates
                    target = crates.get(i);  // this crate will be stored as the closest for now
                }
            }

            if (crates.size()!= 0)  // if there is at least one crate from your side on the world, then
            {
                distance = 1100;   // set the distance back to a huge number so this method can be used again
                return true;  // return true
            } else  // otherwise, if there are no crates, then
            {
                return false;   // return false
            }
        }
        return false;  // if the actor is not in the world then return false (no crates located and therefore no codes will run afterwards)
    }

    /**
     * Scout walks towards the nearest crate on its side
     */
    private void walkToNearestCrate()
    {
        turnTowards (target.getX(), target.getY()+5); //Scou turns towards targetted crate
        move(speed); //Moves at a specified speed, in this case, 2
    }

    /**
     * Determines if the scout has collided with a crate
     * 
     * @return boolean true if scout is touching a crate, false if otherwise
     */
    public boolean checkHitCrate()
    {
        crates = getObjectsInRange(10,Crate.class); //Locates all crates within a radius of 10 pixels around the scout
        determineClosestTarget();  // Determine the closest crate

        //If a scout comes into contact with a tombstone from its side, returns true
        if (crates.size() > 0)
        {
            return true;
        }

        //If the previous condition is not met, returns false instead
        return false;
    }

    /**
     * Scout walks back to the MainBase in which it came from. This gets called by the main method
     * once the scout collects all the crates the belongs to his side.
     */
    public void returnToBase()
    {
        turnTowards (base.getX(), base.getY()); //turns towards the base when moving
        move(speed); //enables medic to walk

        //If scout comes into contact with the base, it is removed from the world
        // Set storage points based on the current level- higher level increase the storage further because more vehicles will be spawned
        if (isTouching(MainBase.class))
        {
            if (base.myResearch.getResearchLevel() == 1)
            {
                base.myStorage.setStoragePoint(base.myStorage.getStoragePoint()+50);
            }
            else if (base.myResearch.getResearchLevel() == 2)
            {
                base.myStorage.setStoragePoint(base.myStorage.getStoragePoint()+70);
            }
            else if (base.myResearch.getResearchLevel() == 3)
            {
                base.myStorage.setStoragePoint(base.myStorage.getStoragePoint()+100);
            }
            else if (base.myResearch.getResearchLevel() == 4)
            {
                base.myStorage.setStoragePoint(base.myStorage.getStoragePoint()+150);
            }
            returnToBase=false;  // sets the scout not to return to base next time he is spawned -collect crates instead
            getWorld().removeObject(this);
        }
    }

    /**
     * Checks if there are crates existent in the world for a scout to pick up
     * 
     * @return boolean true there are no crates available in the world, false if there are crates
     */
    private boolean checkCratesInWorld(){
        crates = getWorld().getObjects (Crate.class); //Locates all crates within a radius of 10 pixels around the scout
        for (int i=0; i< crates.size();i++){
            if (crates.get(i).getSide()!=side){
                crates.remove(i);
                i--;
            }
        }
        //If there are still crates in the array list return false
        if (crates.size() > 0)
        {
            return false;
        }
        //Otherwise return true if no crates are existent currently
        return true;
    }

}
