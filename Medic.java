import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Medic is a Greenfoot Actor. A medic's purpose in this simulation is to walk from its base to revive nearby ally soldiers who have been killed out on the battlefield. A special feature of
 * the medic is that once it revives a maximum of 3 ally soldiers, it will then return to its base; a new medic must be spawned in to revive any remaining soldiers. Medics walk at
 * the same speed in which soldiers do and do not have "lives" as they are not targetted at by enemies. A medic can only revive the same soldier once, once a soldier is killed after
 * being revived, it is simply removed from the world. 
 * 
 * @author Nicholas Chan & Ramy Elbakari
 * @version March 2015
 */
public class Medic extends Helpers
{
    //Initializing instance variables

    private int soldiersRevived;    //Number of soldiers the medic has revived    
    private boolean returnToBase = false;   //Determines whether or not medic should return to base

    private List <Tombstone> fallenSoldiers;    //Initializing list of tombstones
    private Tombstone target;   //Creating an instance of Tombstone called "target"
    private ImaginaryTree tree;  // tree that the medic will be dodging

    /**
     * Creates an instance of Medic. Sets the speed and the side from which the medic comes from.
     * @param side true if medic belongs to the left side, false if it belongs to the right side
     * @param base in which the medic originates from
     */
    public Medic(boolean side, MainBase base) 
    {
        this.speed = 2; //Same speed as a regular soldier
        this.side = side;   //Determines side in which medic belongs to
        if (this.side == false)
        {
            this.setImage ("orangeMedic.png");  //If medic belongs to false side, it appears as an "orange" medic
        }
        this.soldiersRevived = 0;   //Soldiers revived when medic spawns in is 0
        this.base = base; //Determining the medic's base
    }

    /**
     * Act - do whatever the Medics wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls the movement of the actor at different situations.
     */
    public void act() 
    {
        //If the medic collides with an obstacle, it walks around it
        while(checkObstacle())
        {
            turn (20);                 
            move (1);
        }

        //If the variable returnToBase is true, the medic returns to base
        if (returnToBase == true)
        {
            returnToBase(); //Method that causes the medic to walk back to the base it came from
        } else if (healSoldier() == true)  //Once in contact with an ally tombstone, it will revive the soldier 
        {
            BareFoot bareFoot = new BareFoot(target.getSide(), base);  //New instance of BareFoot
            bareFoot.revived(); //Indicates that the soldier has been revived once, if it dies again, it cannot be revived again
            if (this.getWorld() != null)
            {
                getWorld().addObject(bareFoot, this.getX(), this.getY());  //Add a new instance of a new BareFoot that is on the same side as the medic
            }            
            getWorld().removeObject(target);    //Removes tombstone it touches
            this.soldiersRevived++; //Increases the number of soldiers

            //After reviving 3 soldiers, medic returns to base
            if (this.soldiersRevived == 3)
            {
                returnToBase = true;
            }
        }

        //If there are tombstones available on the field and the medic is not required to return to base, then it will walk to the nearest tombstone
        if (returnToBase == false && locateNearestTombstone() && target != null)
        {
            walkToNearestTombstone();   //Method that tells Medic to walk to nearest tombstone (on its own side)
        }

    }    

    /**
     * Determines out of the possible tombstones on the screen, the tombstone that is closest to the medic (same side)
     */
    protected void determineClosestTarget()
    {
        for (int i=0; i< fallenSoldiers.size(); i++){  // loop through all the tombstones in the list
            nearestDistance = calculateNearestActor(fallenSoldiers.get(i));  // calculate the distance between this tombstone and the medic
            if (nearestDistance < distance && fallenSoldiers.get(i).getSide() == this.side){  // if the tombstone belongs to your side and is the nearest tombstone to the medic, then
                distance = nearestDistance; // the distance will be stored to test the remaining tombstones
                target = fallenSoldiers.get(i);  // this tombstone will be stored as the closest for now
            }
        }
        distance = 1100;  // set the distance back to a huge number so this method can be used again
    }

    /**
     * Locates the nearest Tombstone from its side. Defines the target to be used in the walkToNearestTombstone and act methods.
     * @return true - there is a tombstone in the area and successfully located it, false - did not locate any tombstone
     */
    private boolean locateNearestTombstone()
    {
        if (this.getWorld()!= null){  // if the medic is in the world, then
            fallenSoldiers = getWorld().getObjects(Tombstone.class); //Locates all tombstones in the world
            for (int i=0; i< fallenSoldiers.size(); i++){  // loop through all the tombstones in the list

                if (fallenSoldiers.get(i).getSide() != this.side){  // if the tombstone doesn't belong to you, then
                    fallenSoldiers.remove(i); // remove that tombstone from the list
                    i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
                }else{  // if neither conditions are meet, then the rest of the tombstones are what you are looking for and now it is time to determine which one is closer
                    nearestDistance = calculateNearestActor(fallenSoldiers.get(i));  // calculate the distance between this tombstone and the medic 
                    if (nearestDistance < distance){  // if the distance is closer than any other previously calculated distances then, this tombstone is the closest
                        distance = nearestDistance;   // the distance will be stored to test the remaining tombstones
                        target = fallenSoldiers.get(i);  // this tombstone will be stored as the closest for now
                    }
                }
            }

            if (fallenSoldiers.size()!= 0){  // if there are tombstones on the screen  then, 

                distance =1100;  // set the distance back to a huge number so this method can be used again
                return true;  // return true
            } else{  // otherwise, if there is no tombstones, then
                return false;  // return false
            }
        }
        return false;  // if the actor is not in the world then return false (no tombstones located and therefore no codes will run afterwards)
    }

    /**
     * Medic walks towards the nearest tombstone on its side.
     */
    private void walkToNearestTombstone()
    {
        turnTowards (target.getX(), target.getY()); //Medic turns towards targetted tombstone
        move (speed);   //Moves at a specified speed, in this case, 2
    }

    /**
     * Medic walks back to the MainBase in which it came from. This method gets called in the act method after
     * the medic successfully revives all 3 soldiers from his side.
     */
    protected void returnToBase()
    {
        turnTowards (base.getX(), base.getY());   //turns towards the base when moving
        move(speed);    //enables medic to walk

        //If medic comes into contact with the base, it is removed from the world
        if (isTouching(MainBase.class))
        {
            soldiersRevived = 0;  // the soldier he revived is set to 0- so he can revive more soldiers when he comes back again
            returnToBase = false;  // do not return to the base - change back so when he gets spawned again he seeks the soldiers
            getWorld().removeObject(this);  // remove this actor for now

        }
    }

    /**
     * Checks to see if a medic collides with a tombstone from its own side.
     * 
     * @return boolean true if collision occurs, otherwise, false
     */
    private boolean healSoldier()
    {
        fallenSoldiers = getObjectsInRange(10,Tombstone.class); //Locates all tombstones within a radius of 10 pixels around the medic
        determineClosestTarget();  // determines the closest tombstone

        //If the medic comes into contact with a tombtstone from its side, returns true
        if (fallenSoldiers.size() > 0)
        {
            return true;
        }

        //If the previous condition is not met, returns false instead
        return false;
    }

    /**
     * Checks if the soldier hits an obstacle (tree) or not.
     * @return true - there is an obstacle, false - there isn't an obstacle
     */

    protected boolean checkObstacle(){

        //         tree = (ImaginaryTree) getOneObjectAtOffset(7, 7, ImaginaryTree.class);  // scan a tree whose 7 pixels away from the actor
        // 
        //         if (tree != null){  // if there is indeed a tree, then
        //             return true;  // return true
        //         }
        //         return false;  // otherwise return false
        return false;
    }
}
