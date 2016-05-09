import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  // imports the list class

/**
 * The vehicle sub-class is further divided into horses and elephants which share common characteristics like having lives, having the ability for soldiers to ride them
 * and shooting at soldiers from the other base. The vehicles tend to have more lives than soldiers and if they die, they cannot be revived and the soldiers riding them
 * get off with full lives. Also, vehicles follow pre-planned path (different for each vehicle).
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
abstract class Vehicles extends Units
{
    
    protected BareFoot target;  // a temporary variable to store the target (the one who will be riding) the vehicles- that value will later be stored in target1/target2
    
    // two variable to store the two closest soldiers to the elephant/horse (who will be making their way to ride it)
    protected BareFoot target1;
    protected BareFoot target2;

    // two variable to check whether the soldiers actually made it to the elephant/horse or not (got shot will trying)
    protected boolean target1Check=true;
    protected boolean target2Check=true;
    
    // two soldiers riding elephant/horse
    protected BareFoot rider1;
    protected BareFoot rider2;
       
    /**
     * Returns the number of riders on the vehicle
     * @return - the number of riders on the vehicle
     */
    abstract int numberOfRiders();
    
    /**
     * Chooses and defines soldiers from the world that will ride the vehicle
     */
    abstract void chooseSoldiers();
    
    /**
     * Soldiers ride the vehicle if they come close to it.
     */
    abstract void ride();

    /**
     * Determines the closest soldier to the vehicle based on the list defined in each vehicle sub-class.
     * Gets called by the checkSoldier and chooseSoldiers methods.
     */
    protected void determineClosestTarget(){
        for (int i=0; i< soldiers.size(); i++){  // loop through all the scanned soldiers
            if (soldiers.get(i).getSide()!=this.side || soldiers.get(i).spotTaken()){  // eliminate any soldier whose not on your side or whose already shooting the base (soldiers will not leave the opponent's base to ride vehicles)
                soldiers.remove(i);  // remove from the list
                i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            }else{
                nearestDistance = calculateNearestActor(soldiers.get(i));  // calculate the distance between this soldier and the vehicle using calculateNearestActor method (which is discussed in the Units class)
                if (nearestDistance < distance){  // if the distance is closer than any other previously calculated distances then, this soldier is the closest
                    distance = nearestDistance;  // the distance will be stored to test the remaining soldiers
                    target = soldiers.get(i);  // the soldier will be stored as the closest soldier (which can be later changed if another soldier is closer)

                }
            }
        }
        distance = 1100;  // set the distance back to a huge number so this method can be used again

    }
    
    /**
     * Checks if there are any nearby soldier in the area
     * and sees if he is close enough to jump on the elephant
     * @return true-soldier ready to jump, false- no soldiers available
     */
    protected boolean checkSoldier(){
        soldiers = getObjectsInRange(30,BareFoot.class);  // scan soldiers that are at least 30 pixels away from the elephant

        determineClosestTarget();  // determine which soldier from your side is closest (if there is any- more on that in the method itself in the vehicles class)
        if (soldiers.size() >0){   // if there are indeed soldiers (from your side ) in the range then, 

            return true;  // return true
        }
        return false;  // return false
    }

}
