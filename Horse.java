import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  // import the list class

/**
 * A horse gets spawned on the screen when the reasearch facility reaches level 2. A horse can span 1 rider. Once the horse spawns, the closest soldier will make his way
 * to ride as long as he can't ride an elephant instead. A horse has 4 lives. A horse moves horizontally on the screen. A hose can only shoot if there is a rider on it.
 * A horse will shoot soldiers from the other base if they are nearby. Soldiers from the other base will purposely shoot at only if they are wandering around and the
 * horse happens to be nearby. Once the horse dies, the soldier riding it will get off with full lives. A horse will not shoot if the arrow will hit the tree with no possibility of hitting the target or if the arrow will hit soldiers
 * from its own base (friendly fire could occur if the horse's aim was off).
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
public class Horse extends Vehicles
{

    
    private int rider;  // the number of riders on the horse (0 or 1)
    

    private boolean moveBackwards=false;   // a variable to keep track of the horse's current movement (true= backwards, false= forwards)

    /**
     * Constructs the horse. Defines the side and base the horse comes from and amount of life the horse will have.
     * Sets the speed of the horse and displays its health icons.
     * @param side- which base the horse will come from true - right side, false- left side
     * @param myBase - which base the horse comes from
     */
    public Horse(boolean side,MainBase myBase){  // determine the side of the horse and which base it comes from
        this.side=side;  // sets the of the horse (true = left (white), false = right (orange))
        if (this.side==false){  // if the side is orange, then
            this.setImage ("orangeEmptyHorse.png");  // the driver of the horse will wear orange (default is white)
            this.setRotation (180);  // set the rotation of the horse to face the right side of the screen
        }

        this.lives = 4;  // set the lives of the horse to be 4
        // display all 4 health icons
        icon1 = new HealthIcon(1,this);
        icon2 = new HealthIcon(2,this);
        icon3 = new HealthIcon(3,this);
        icon4 = new HealthIcon(4,this);
        speed=1;  // set the speed to 1 pixel per act

        base = myBase;   // set the base of which the elephant is coming from
    }

    /**
     * Act - do whatever the Horse wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls how the horse will react in different situations
     */
    public void act() 
    {
        // Add your action code here.
        counter = Greenfoot.getRandomNumber(31)+20;   // random number (20- 50) that detemines when the horse will shoot-changes every act (which makes it EXTREMELY random because the number has to be divisible by 20 for the shooting to occur) 
        randomMiss = Greenfoot.getRandomNumber(11);  // the amount of miss in pixels (the arrow will go a certain number of pixels beside the target)
        cofficient = Greenfoot.getRandomNumber(2);   // variable used to determine whether the soldier will miss (to the right or left) of the target

        if (cofficient ==1){  // if the cofficient is 1, then the miss will be higher (above the soldier) - appears to be to left/right of the soldier depending on the type of soldier (white- to the left of him/ orange- to the right of him)
            randomMiss = -randomMiss;
        }

        counter++;  // increase the counting variable by one - will only affect every random number once

        if (target1Check==true || target.checkSeekingElephant()==true){  // if the first check point is still open or the target changed his direction to go to a nearby elephant instead, then
            chooseSoldiers();  // choose a soldier to ride the horse
        }

        if (target1!= null && target1.getLives()==0 && rider ==0){  // if target1 died while making his way to the horse, then
            target1Check=true;  // open the check point (to choose another solider)
        }
        ride();   // check if there is any nearby soldiers to hop on the horse

        if(rider ==1&& checkCloseSoldiers(!this.side)&& aim() && checkTree()){  // if there is a soldier riding the horse and there is a nearby soldier from the opposite base and there is no tree in between them then,

            shoot();  // shoot at the target (shooting frequency changes every act)
        }   else {  // else
            walk();  // keep walking -unlike the elephant the horse stops to aim and shoot at the target
        } 

        if (this.rider == 1)  // if there is a rider on the horse
        {
            checkLives(true); // update the health icons on the screen to reflect current life of the horse
        }

    }

    /**
     * Checks to see if the specified actor should seek the horse or not.
     * @return true - the soldier should seek the horse false- he shouldn't
     * @param actor- the specific actor that should or should not seek the horse
     */
    public boolean goToHorse(Actor actor){  // same concept applies as the goToElephant method but there can only be one actor going to the horse not 2 like the elephant

        if (actor == target1){
            return true;
        }
        return false;

    }

    /**
     * Soldier jumps on the horse if comes close to it.
     * Soldier no longer on the screen and the image of the horse 
     * changes to the soldier riding it.
     */
    protected void ride(){  // same concept applies as the ride method in the elephant but there can only be one rider for the horse unlike the elephant that 2 people can ride
        if (checkSoldier() && rider==0) {

            if (this.side == false){
                this.setImage ("orangeFullHorse.png");
            }else{
                this.setImage ("FullHorse.png");
            }
            rider++;
            target.instanceDeath(false);
            getWorld().removeObject (target);
        }
    }

    /**
     * Chooses the closest soldier to the horse and defines him in the target variable.
     */
    protected void chooseSoldiers(){   // same concept as the chooseSoldiers in the elephant class but the soldier whose already seeking the elephant does not get taken into account
        soldiers = getWorld().getObjects (BareFoot.class);
        for (int i=0; i< soldiers.size(); i++){
            if (soldiers.get(i).getSide()!=this.side || soldiers.get(i).spotTaken() || soldiers.get(i).checkSeekingElephant()==true){  // eliminate the soldier whose going to the elephant because he will not ride the horse even if he is the closest to it
                soldiers.remove(i);
                i--;
            }else{
                nearestDistance = calculateNearestActor(soldiers.get(i));
                if (nearestDistance < distance){
                    distance = nearestDistance;
                    target = soldiers.get(i);

                }
            }
        }
        distance = 1100;

        if (target!= null){
            target1= target;
            target1Check=false;
        } 

    }

    /**
     * Shoots at the nearby soldiers. Does not work every time it is called.
     * The counter variable should be divisible by 20 for the shooting to take place.
     */
    private void shoot(){  
        if (counter%20==0){   // the counter variable must be divisible by 20 for the shooting to occur (counter changes every act -ranomized)
            getWorld().addObject(new Arrow (getRotation() + randomMiss, this, "horse", base), getX(), getY());  // generate an arrow on the screen with the same rotation as the horse
            Greenfoot.playSound("bow.mp3");
        }

    }    

    /**
     * Controls the movement of the elephant on the screen.
     */
    private void walk(){
        if (getX() ==915){  // if the horse reaches the end of the screen then,
            if (this.side==true){  // if it was from the right side,
                moveBackwards =true;  // move backwards
            } else {  // if it was from the left side,
                moveBackwards =false;   // move forwards
            }
        } else if (getX()== 175){   // if the horse is at the begining of the screen then, 
            if (this.side ==true){  // if it was from the right side,
                moveBackwards =false;  // move forwards
            } else {  // if it was from the left side,
                moveBackwards =true;   // move backwards
            }
        }

        if (this.side ==true){   // if the horse is from the left side, 
            if (moveBackwards ==true){  // if the horse is moving backwards then,

                setRotation (180);  // rotate it 180 degrees to face the other direction
                move (speed);   // move it to that direction
            } else{   // if the horse is moving forwards then,

                setRotation (0);   // don't rotate it
                move (speed);   // move it normally
            }
        } else {   // if the horse is from the right side
            if (moveBackwards ==false){  // if the horse is moving forwards then,

                setRotation (180);  // rotate it 180 degrees to face the other direction  (the original rotation of the png image is 0 degrees)
                move (speed);    // move it to that direction
            } else{  // if the horse is moving backwards then,

                setRotation (0);   // don't rotate it (the original rotation of the png image is already facing the right side)
                move (speed);   // move it to that direction
            }

        }
    }

    /**
     * Checks to see if there any soldier of the specified type in a range
     * of 200 pixels around the horse. Defines the barefoot variable and gives it 
     * the value of the closest soldier to be later used in the shoot method.
     * @return true - there is a soldier, false - there isn't a soldier
     * @param side- the side of the soldier you are looking for true-left side, false -right side
     */
    protected boolean checkCloseSoldiers(boolean side){
        soldiers = getObjectsInRange(350,BareFoot.class);  // scan all the soldiers that are at least 200 pixels away from this unit

        for (int i=0; i< soldiers.size(); i++){  // loop through all the soldiers in the list
            if (soldiers.get(i).getSide()!=side || soldiers.get(i).checkSpawnMovement()){  // eliminate any soldier from the opposite/same side (depends on the type of soldier you are scanning for) or any soldier whose undergoing spawn movement
                soldiers.remove(i);  // remove that soldier from the list
                i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            }else{
                nearestDistance = calculateNearestActor(soldiers.get(i));  // if neither conditions are meet, then the rest of the soldiers are what you are looking for and now it is time to determine which one is closer
                if (nearestDistance < distance){  // calculate the distance between this unit and the soldier using calculateNearestActor method
                    distance = nearestDistance;   // if the distance is closer than any other previously calculated distances then, this soldier is the closest
                    bareFoot = soldiers.get(i);  // the distance will be stored to test the remaining soldiers

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
     * Loses a life. The horse's life decreases by one every time it is called.
     * If the horse's life reaches 0, then the death points get recorded in the mainbase.
     */
    public void loseALife()   // same concept as the loseALife method in the elephant class but the death points will be treated as 5 times that of the soldier (elephant was 10)
    {
        if(this.lives > 0)
        {
            if (this.lives == 1)
            {
                base.setDeaths(base.getDeaths()+5);
            }
            this.lives--;

        }
    }

    /**
     * Spawns the soldier who was riding the horse when the horse dies.
     * Gets called by the checkLives method.
     */
    protected void revive(){
        rider1= new BareFoot(this.side, base);  // defining a barefoot (the one who was riding it)
        getWorld().addObject(rider1,this.getX(),this.getY());  // if the horse dies, then spawn a soldier on the screen (the one who was riding it)
        rider1.revived();  // the barefoot has been revived
    }

    /**
     * Returns whether there is an empty spot on the horse or not
     * @return true - there is an empty spot, false - there isn't an empty spot
     */
    public boolean rider(){
        if (rider>0){   // if there is a soldier riding the horse then,
            return true;   // return true
        }
        return false;   // otherwise return false
    }

    /**
     * Returns the number of riders on the horse
     * @return the number of riders on the horse
     */
    public int numberOfRiders(){
        return rider;  // return the number of soldiers currently riding the elephant

    }

}
