import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  // imports list class

/**
 * The units class is divided into three sub-class, the helpers (medics and scouts), the vehicles (horses and elephants) and soldiers (barefoots and snipers).
 * Each of those units share common charactaristics like shooting (vehicles and soldiers), dodging trees (helpers and soldiers) and having lives (vehicles and soldiers).
 * Each of those units interact to enhance the stimulation.
 * 
 * @author (Ramy Elbakari/Nicholos Chan) 
 * @version (0.0.3)
 */
abstract class Units extends Actor
{
    protected boolean side;  // a variable to keep track of which side the unit originated from
    protected int lives;  // the number of lives each unit has
    protected int speed;  // the speed of each unit

    // 2 variables used to store a tree and an arrow
    protected Actor tree;
    protected Arrow arrow;

    // 3 lists used to store vehicles and its sub-classes
    protected List <Horse> horses;
    protected List <Elephant> elephants;
    protected List <Vehicles> vehicles;    

    // 2 lists used to store soldiers that will be extensively used
    protected List <BareFoot> soldiers;  // list used to store soldiers (whether they're all from one side or not)
    protected List <BareFoot> sameSoldiers;  // list used to store soldiers that are all from one side

    // 2 variables that will be used to determine the closest unit to the current unit
    protected double nearestDistance;
    protected double distance=1100;
    
    // 4 variables used to store each type of fighing units
    protected BareFoot bareFoot;
    protected Elephant elephant;
    protected Horse horse;
    protected Vehicles vehicle;

    protected int counter;   // a variable that will be given random number every act and shoot be divisible by 20 (25 for snipers) in order for the unit to shoot
    protected int randomMiss;  // a variable used to store the amount of miss (in pixel) when a unit shoots an arrow (the arrow will go "randomMiss" away from the target)
    protected int cofficient;  // a variable used to store whether the arrow will miss to the right or to the left of the target

    // two variables used to control running certain codes once and running them again under specific requirments (more on that in the barefoot/elephant classes
    protected boolean stop=false;
    protected boolean stop2=true;

    protected HealthIcon icon1,icon2,icon3,icon4,icon5;  // 5 variables for each health icon 

    protected boolean spawnMovement=true;  // a variable to keep track of whether the soldier is undergoing spawn movement or not (a soldier will normally do once he is spawned outside the base)

    protected MainBase base;  // a variable to keep track of which base the unit originated from
     

    /**
     * Gets the side of which the actor is from
     * @return true - left side, false - right side
     */
    protected boolean getSide (){
        return side;  // return which side the unit is from
    }

    /**
     * Makes the target lose a life. Gets called by the arrow class
     */
    protected void loseALife() {}

    /**
     * Gets the lives of this unit.
     * @return - the amount of lives of this unit
     */
    protected int getLives()
    {
        return this.lives;  // return the number of lives the unit has
    }    

    /**
     * Kills the target and removes all its health icons along with the body from the screen.
     * There is an option of whether the target can get revived or not
     * @param revive- true- could be revived, false- could not be revived
     */
    protected void instanceDeath(boolean revive){
        while (lives >0){  // keep looping until the actor loses all his lives
            lives --;  // decrease life by 1
            checkLives(revive);  // update lives (remove a health icon every time)
        }
        getWorld().removeObject (this);  // remove the actor once he loses all his lifes
    }

    /**
     * Checks if the soldier is still making his way to the battlefield
     * @return true- he is, false- he is not
     */
    protected boolean checkSpawnMovement(){
        if (spawnMovement==true){  // if the soldier is still undergoing spawn movement, then
            return true;  // return true

        }
        return false;   // else return false
    }

    /**
     * Checks if the soldier's arrow will hit a tree/soldier from the same side first before hitting his target.
     * @return true- the first thing it hit will be the target, false- it will hit a tree or a soldier from the same side first
     */
    protected boolean checkTree(){
        arrow = new Arrow (this.getRotation(), true);  // generate an arrow on the screen and make it imaginary
        getWorld().addObject (arrow , getX(),getY());  // have it have the same rotation as this unit
        if (arrow.inWay(this)){  // have it cross the screen and check if it hits a tree or a same side soldier before hitting the target, if it hit something before hitting the tree, then 
            getWorld().removeObject (arrow);  // remove that arrow
            return false;  // return false (do not shoot - either friendly fire or won't hit anything but trees)
        }
        getWorld().removeObject (arrow);  // remove that arrow
        return true;  // return true (arrow won't hit anything before hitting target - and therefore shoot)

    }

    /**
     * Checks if the soldier hits an obstacle (tree) or not.
     * @return true - there is an obstacle, false - there isn't an obstacle
     */

    protected boolean checkObstacle(){

        tree = getOneObjectAtOffset(7, 7, Tree.class);  // scan a tree whose 7 pixels away from the actor

        if (tree != null){  // if there is indeed a tree, then
            return true;  // return true
        }
        return false;  // otherwise return false

    }

    /**
     * Calculates the distance between the specified actor and this unit.
     * Gets called by every class for numerous reasons.
     * @param actor- the unit in which the distance between it and this unit will be measured
     */
    protected double calculateNearestActor(Actor actor){
        return Math.hypot(actor.getX() - getX(), actor.getY() - getY());  // calculate the nearest ("specified") actor to this unit using pythagreom theory
    }

    /**
     * Sets the live of the specified actor. Gets called by the elephant class.
     * @param lives - the amount of lives wished to be set
     */
    protected void setLives (int lives){
        this.lives =lives;  // set the lives of the this unit by a specific amount

    }

    /**
     * Checks to see if there any soldier of the specified type in a range
     * of 200 pixels around the unit. Defines the barefoot variable and gives it 
     * the value of the closest soldier to be later used in the shoot method.
     * @return true - there is a soldier, false - there isn't a soldier
     * @param side- the side of the soldier you are looking for true-left side, false -right side
     */
    protected boolean checkCloseSoldiers(boolean side){
        soldiers = getObjectsInRange(200,BareFoot.class);  // scan all the soldiers that are at least 200 pixels away from this unit

        for (int i=0; i< soldiers.size(); i++){  // loop through all the soldiers in the list
            if (soldiers.get(i).getSide()!=side || soldiers.get(i).checkSpawnMovement()){  // eliminate any soldier from the opposite/same side (depends on the type of soldier you are scanning for) or any soldier whose undergoing spawn movement
                soldiers.remove(i);  // remove that soldier from the list
                i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            }else{ // if neither conditions are meet, then the rest of the soldiers are what you are looking for and now it is time to determine which one is closer
                nearestDistance = calculateNearestActor(soldiers.get(i));   // calculate the distance between this unit and the soldier using calculateNearestActor method
                if (nearestDistance < distance){   // if the distance is closer than any other previously calculated distances then, this soldier is the closest
                    distance = nearestDistance;  // the distance will be stored to test the remaining soldiers
                    bareFoot = soldiers.get(i);  // this barefoot will be stored as the closest for now

                }
            }

        }

        if (soldiers.size()!= 0 ){  // if a soldier exists in the specified range (whose on the side you requested)

            distance =1100;   // set the distance back to a huge number so this method can be used again
            return true;  // return true (there is a soldier nearby)
        } else{
            return false;  // otherwise return false
        }

    }

    /**
     * Aims at the defined barefoot target. Gets called by the barefoot and horse classes when aiming at barefoot targets.
     * @return true - a target exsist and is aimed at, false- no target exsist to be aimed at
     */
    protected boolean aim(){
        if (bareFoot!= null){  // if a barefoot exsists in the area
            turnTowards (bareFoot.getX(), bareFoot.getY());   // aim at that barefoot
            return true;  // return true (there is a barefoot in the area)
        }
        return false;   // otherwise return false (there isn't a barefoot)
    }

    /**
     * Updates the health icons being displayed for each unit.
     * If the lives reach 0, then there is an option to be able to be revived or not.
     * @param revive - true- the unit could be revived, false- the unit will not be able to be revived
     */
    protected void checkLives(boolean revive)  // the revive option determines whether the unit could be revived after it dies or not (if true- replace the body with a tombstone for the medic to later revive)
    {
        if (this.lives == 5)   // if this unit has 5 lives, then display all 5 icons
        {
            getWorld().addObject (icon1 , this.getX() + 7, this.getY()-30);
            getWorld().addObject (icon2 , this.getX() - 3, this.getY()-30);
            getWorld().addObject (icon3 , this.getX() - 13, this.getY()-30);
            getWorld().addObject (icon4 , this.getX() - 23, this.getY()-30);
            getWorld().addObject (icon5 , this.getX() - 33, this.getY()-30);
        }
        else if (this.lives == 4)  // same concepts apply for the rest of the lives
        {
            getWorld().addObject (icon1 , this.getX() + 7, this.getY()-30);
            getWorld().addObject (icon2 , this.getX() - 3, this.getY()-30);
            getWorld().addObject (icon3 , this.getX() - 13, this.getY()-30);
            getWorld().addObject (icon4 , this.getX() - 23, this.getY()-30);
            getWorld().removeObject(icon5);  // remove the extra icon from before
        }
        else if(this.lives == 3)
        {
            getWorld().addObject (icon1 , this.getX() + 7, this.getY()-30);
            getWorld().addObject (icon2 , this.getX() - 3, this.getY()-30);
            getWorld().addObject (icon3 , this.getX() - 13, this.getY()-30);
            getWorld().removeObject (icon4);
        }
        else if(this.lives == 2)
        {
            getWorld().addObject (icon1 , this.getX() + 7, this.getY()-30);
            getWorld().addObject (icon2 , this.getX() - 3, this.getY()-30);
            getWorld().removeObject(icon3);
        }
        else if(this.lives == 1)
        {
            getWorld().addObject (icon1 , this.getX() + 7, this.getY()-30);
            getWorld().removeObject(icon2);
        }
        else if(this.lives == 0)  // if the unit has no more lives left, then
        {
            getWorld().removeObject(icon1);  // remove the last icon
            if (revive ==true){  // if revive is true,
                revive();  // then enable the option for the unit to get revived (replace the body by a tombstone, which will later be recovered by the medic)
            }

        }        
    }

    /**
     * Revives the soldier riding the vehicles or if a barefoot dies, he turns to a tombstone waiting for the medics
     * to revive him. Gets called by the checkLives method
     */
    protected void revive(){}
}
