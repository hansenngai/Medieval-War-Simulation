import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  // import the list class

/**
 * An elephant gets spawned on the screen when the reasearch facility reaches level 3. An elephant can span 2 riders. Once the elephant spawns, the 2 closest soldiers
 * will move towards it to ride it. An elephant will follow move vertically on the screen (guarding the base). An elephant can only shoot if there is at least one rider.
 * Also, an elephant will only shoot if there are soldiers from the other base infront of it (not behind it). An elephant has 5 lives. However, the elephant can take 
 * 16 shots before it starts to lose life. Once the elephant dies, its rider will get off. The elephant can step over the opponent's soldiers if they happen to be on 
 * his path. Soldiers will always prefer to ride the elephant over the horse (unless they are shooting at the opponent's base) because elephants have more lives than 
 * horses. Also, barefoots will purposely shoot at the elephant if they are wandering around and the elephant happens to be nearby. An elephant will not shoot if the 
 * arrow will hit the tree with no possibility of hitting the target or if the arrow will hit soldiers from its own base (friendly fire could occur if the elephant's 
 * aim was off).
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
public class Elephant extends Vehicles
{

    private int riders=0;  // variable to keep track of the number of riders on the elephant

    private int rotation;  // variable used to control the rotation of the elephant (serves as a temporary value while shooting )
    private boolean movingUp;  // variable to keep track of the position of the elephant (true =moving up, false - moving down)
    private int armor;   // variable that stores the amount of armor the elephant will have before starts losing life (when shot)
    private boolean armorCheck =false;  // a boolean that controls when to make the elephant lose life (gets switched on after armor gets destroyed)

    private BareFoot stepOver;  // a variable that stores the actor that the elephant will step on (when a soldier from the other side crosses his path)

    /**
     * Constructs the elephant. Defines the side and base the elephant comes from and amount of life the elephant will have.
     * Sets the speed of the elephant and displays its health icons.
     * @param side- which base the elephant will come from true - right side, false- left side
     * @param myBase - which base the elephant comes from
     */

    public Elephant (boolean side,MainBase myBase){  // determine the side of the elephant and which base it comes from
        this.side = side;  // sets the of the elephant (true = left (white), false = right (orange))
        if (this.side==false){  // if the side is orange, then
            this.setImage("orangeEmptyElephant.png");  // the driver of the elephant will wear orange (default is white)

        }
        this.lives = 5; // the elephant has 5 lives

        // display all 5 health icons of the elephant
        icon1 = new HealthIcon(1,this);
        icon2 = new HealthIcon(2,this);
        icon3 = new HealthIcon(3,this);
        icon4 = new HealthIcon(4,this);
        icon5 = new HealthIcon(5,this);
        armor=16;  // the elephant could bear 16 shots before starts to lose life.

        speed=1;  // the speed of the elephant will be 1 pixel per act

        base = myBase;  // set the base of which the elephant is coming from

    }

    /**
     * Act - do whatever the Elephant wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls how the elephant will react in different situations
     */
    public void act() 
    {

        counter = Greenfoot.getRandomNumber(31)+20; // random number (20- 50) that detemines when the elephant will shoot-changes every act (which makes it EXTREMELY random because the number has to be divisible by 20 for the shooting to occur)
        randomMiss = Greenfoot.getRandomNumber(11);  // the amount of miss in pixels (the arrow will go a certain number of pixels beside the target)
        cofficient = Greenfoot.getRandomNumber(2);  // variable used to determine whether the soldier will miss (to the right or left) of the target

        if (cofficient ==1){  // if the cofficient is 1, then the miss will be higher (above the soldier) - appears to be to left/right of the soldier depending on the type of soldier (white- to the left of him/ orange- to the right of him)
            randomMiss = -randomMiss;
        }

        counter++; // increase the counting variable by one - will only affect every random number once

        if (target1Check==true || target2Check==true){  // if neither checks are passed (neither soldier have yet made it to the elephant ) then,
            chooseSoldiers();  // keep looking for the two closest soldiers 

        }

        if ((target1!= null && target1.getLives()==0 && riders <2)){  // if target one died before making it to the elephant,then
            // open both check points - the reason for opening both check points
            // because the second target will become the first target and now we will have to look for the second target (opening both check points will allow that to happen)
            target1Check= true;
            target2Check= true;
            // and look for the two closest soldiers
            chooseSoldiers();
        } else if ((target2!= null && target2.getLives()==0 &&riders <2)){  // if the second target died before making it to the elephant, then
            chooseSoldiers();  // choose the two closest soldiers
        }
        ride();  // check if there is any nearby soldiers to hop on the elephant

        if(riders >0 && checkCloseSoldiers(!this.side) && checkTree()){  // if the elephant has at least one rider and there are nearby soldiers from the other side and there is no tree in the way, then, 
            shoot();  // shoot- the shooting frequency will be randomized every act
            walk();  // keep walking  - unlike the rest of the units, the elephant does not turn but shoots while it walks
        }   else {  // if no soldiers are located then,

            walk();  // keep walking
        } 

        if (this.riders >0)  // if there is at least one rider, then
        {
            checkLives(true);  //Updates the amount of Health Icons following the elephant in accordance to their current amount of lives
        }
        stepOver();  // step over the opponent's soldiers if they walk in the elephants path
        getWorld().setPaintOrder (HealthIcon.class, Elephant.class);  // health icon to be displayed on top of the elephant

    }    

    /**
     * Checks to see if there any soldier of the specified type in a range
     * of 200 pixels around the elephant. Defines the barefoot variable and gives it 
     * the value of the closest soldier to be later used in the shoot method.
     * @return true - there is a soldier, false - there isn't a soldier
     * @param side- the side of the soldier you are looking for true-left side, false -right side
     */
    protected boolean checkCloseSoldiers(boolean side){
        soldiers = getObjectsInRange(200, BareFoot.class);  // scans all the soldiers that are at leat 200 pixels away from the elephant

        for (int i=0; i< soldiers.size(); i++){  // loops through all the soldiers
            if (soldiers.get(i).getSide()!=side || (soldiers.get(i).getY()>this.getY() && movingUp==true) || (soldiers.get(i).getY()<this.getY() && movingUp==false)){  // if the soldier is not from the specified side or located underneath the elephant, then
                soldiers.remove(i); // remove the soldier from the list
                i--; // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            }else{  // if neither conditions are meet, then the rest of the soldiers are shootable and now it is time to determine which one is closer
                nearestDistance = calculateNearestActor(soldiers.get(i));   // calculate the distance between this soldier and the elephant using calculateNearestActor method (which is discussed in the Units class)
                if (nearestDistance < distance){  // if the distance is closer than any other previously calculated distances then, this soldier is the closest
                    distance = nearestDistance;  // the distance will be stored to test the remaining soldiers
                    bareFoot = soldiers.get(i);  // the soldier will be stored as the closest soldier (which can be later changed if another soldier is closer)

                }
            }

        }

        if (soldiers.size()!= 0){  // if there are soldiers in the range then, 

            distance =1100;  // set the distance back to a huge number so this method can be used again
            return true;  // return true (there is an elephant -which is now defined)
        } else{   // if not,
            return false;  // return false
        }

    }

    /**
     * Soldier jumps on the elephant if comes close to it.
     * Soldier no longer on the screen and the image of the elephant 
     * changes to the soldier riding it.
     */
    protected void ride(){
        if (checkSoldier() && riders != 2) {  // if there is a soldier nearby and there is less than 2 riders then,
            if (riders == 0){   // if there is no riders on the elephant then,
                if (this.side == false){  // if the elephant is from the left side, then
                    this.setImage ("orangeHalfFullElephant.png");  // switch the image of the elephant to a soldier (wearing orange) riding an elephant
                }else{ // else if the elephant if from the right side, then
                    this.setImage ("halfFullElephant.png");   // switch the image of the elephant to a soldier (wearing white) riding an elephant
                }

            } else if (riders ==1){  // else if there is already one soldier riding the elephant then, 
                if (this.side == false){  // if the elephant is from the left side, then
                    this.setImage ("orangeFullElephant.png");   // switch the image of the elephant to two soldiers (wearing orange) riding an elephant
                }else{  // else if the elephant if from the right side, then
                    this.setImage ("fullElephant.png");   // switch the image of the elephant to two soldier (wearing white) riding an elephant
                }

            }
            riders ++;  // increase the number of riders by one
            target.instanceDeath(false);  // set the target's life to 0 (don't make him revived - don't show a barrel instead)
            target.setLives (1);  // set his life back to 1 - to differentiate between the target getting killed while it is making its way to the elephant and when it "dies" because it is riding the elephant.
            // this will cause it to cross the check point (the target made it safely to the elephant)
            getWorld().removeObject (target);  // remove the target from the world
        }
    }    

    /**
     * Chooses the closest soldier to the elephant and defines him in the target variable.
     */
    protected void chooseSoldiers(){
        soldiers = getWorld().getObjects (BareFoot.class);   // scan all the soldiers on the field
        determineClosestTarget();   // determine which soldier (from your side) is closest -that becomes target

        if (target != null&& target1Check ==true){  // if target exsists and the first check point is still open then, 
            target1= target;  // the target becomes target 1
            target1Check=false;  // and close the first check point - cannot redefine target1 until the check point opens again (which will happen if target1 dies before making it to the elephant)
        }
        // the following loop is the same exact as determineClosestTarget except that target1 cannot be target2 again, so remove target1 if scanned
        for (int i=0; i< soldiers.size(); i++){
            nearestDistance = calculateNearestActor(soldiers.get(i));
            if (nearestDistance < distance && soldiers.get(i) != target1){  // if target 1 is scanned, it will not be considered in the calculation for the nearest-therefore leaving you with the SECOND closest target instead
                distance = nearestDistance;
                target = soldiers.get(i);

            }
        }
        if (target != target1){  // if the new target does not equal target1 (different soldier), then
            target2= target;  // the target becomes target2
            target2Check= false;  // the second check point is closed
        }

    }

    /**
     * Checks to see if the specified actor should seek the elephant or not.
     * @return true - the soldier should seek the elephent false- he shouldn't
     * @param actor- the specific actor that should or should not seek the elephant
     */
    public boolean goToElephant(Actor actor){  

        if (actor == target1 || actor == target2){  // if the specified actor in the parameters is either target1 or target2, then
            return true;  // return true (the actor will pursue the elephant to ride it)
        }
        return false;  // otherwise return false

    }    

    /**
     * Shoots at the nearby soldiers. Does not work every time it is called.
     * The counter variable should be divisible by 20 for the shooting to take place.
     */
    private void shoot(){

        if (counter%20==0){   // the counter variable must be divisible by 20 for the shooting to occur (counter changes every act -ranomized)
            rotation =getRotation();  // the elephant does not rotate when shooting, therefore save the current rotation of the elephant
            turnTowards (bareFoot.getX(), bareFoot.getY());   // turn towards the target (rotate)
            getWorld().addObject(new Arrow (getRotation() + randomMiss, this, "elephant", base), getX(), getY());  // shoot an arrow in the direction of the target
            setRotation (rotation);  // set the rotation back to what it was - occurs in one act, therefore seems as if the elephant never rotated
            Greenfoot.playSound("bow.mp3");
        }

    }

    /**
     * Controls the movement of the elephant on the screen.
     */
    private void walk(){
        if (this.side==false){  // if the elephant is from the left side, then
            move (-speed); // move to the left (when reaching out your path)
        }else{  // else if it is from the right, then
            move (speed);  // move to the right (when reaching out your path)
        }
        if (side ==true){  // if the elephant is from the right side, then
            if ((this.getX()>= 348 && stop ==false)|| (this.getY()>=485 && stop ==true)){ // if the elephant reaches its up/down path or suppose to go up (after completing its path of going down) then,
                setRotation (270);  // set the rotation to make the elephant appear to be travelling up
                stop=true; // the code only runs once for now
                stop2=true;  // the second block of code is now open
                movingUp=true;  // the movement of the elephant is now up
            }

            if (this.getY() <=170 && stop2==true){  // if the elephant completes its up path and suppose to go down now, then
                setRotation (90);  // set the rotation to make the elephant appear to be travelling down
                stop2=false;  //  the first block is now open (to turn back to moving upwards)
                movingUp=false;  // the movement of the elephant is now down
            }

        }else {  // else if the elephant is from the left side, then
            if ((this.getX()<= 742 && stop ==false) || (this.getY()>= 485 && stop ==true)){  // same concept as the code above but different coordinates to reflect the other side of the screen
                setRotation (90);
                stop=true;
                stop2=true;
                movingUp=true;
            }

            if (this.getY() <= 170 && stop2==true){
                setRotation (-90);
                stop2=false;
                movingUp=false;
            }

        }
    }

    /**
     * Loses a life. The elephant's life decreases by one every time it is called.
     * If the elephant's life reaches 0, then the death points get recorded in the mainbase.
     */
    public void loseALife() 
    {
        if (armor >0){   // if the armor is still there    
            if (armor == 1)  // if the armor was at one (armor is now destroyed)
            {
                armorCheck = true;  // enable the elephant to lose life
            }
            armor--;   // decrease the armor maintainance by one
        }else if(this.lives > 0 && armorCheck == true)  // if the current life is bigger than 0, then
        {
            if (this.lives == 1)  // if the life is 1, then 
            {
                base.setDeaths(base.getDeaths()+10);  // the elephant will die and therefore record the death points of the elephant in the opponent's base (death points is 10 times that of the soldier-harder to kill)
            }
            this.lives--;  // decrease the life by one
        }
    }

    /**
     * Spawns the soldiers who were riding the elephant when the elephant dies.
     * Gets called by the checkLives method.
     */
    protected void revive(){
        if (this.riders == 1) // if there was only 1 rider when the elephant died
        {
            getWorld().addObject(new BareFoot(this.side, base),this.getX(),this.getY());  // then when the elephant dies, spawn a barefoot (the one who was riding it) -only killed the elephant not the barefoot who was ridding it
        }
        else if (this.riders == 2)  // if there was 2 riders when the elephant died
        {
            rider1= new BareFoot(this.side, base);  // defining 2 barefoots (the ones who were riding it)
            rider2= new BareFoot(this.side, base);
            getWorld().addObject(rider1,this.getX(),this.getY());  // spawn 2 barefoots (the ones who were riding it)
            getWorld().addObject(rider2,this.getX(),this.getY());
            rider1.revived();  // the 2 barefoots have been revived
            rider2.revived();
        }
    }

    /**
     * Step over the soldier who are the same side as this elephant when they cross the elephant's path.
     */
    private void stepOver(){
        soldiers = getObjectsInRange(40, BareFoot.class);   // scan all the soldiers that are at least 40 pixels away

        for (int i=0; i< soldiers.size(); i++){   // loop through every single soldier
            if (soldiers.get(i).getSide()==side){  // eliminate the soldier whose the same side as yours (you will not step on him-the only the opposing soldiers)
                soldiers.remove(i);  // remove from list
                i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            }else{
                nearestDistance = calculateNearestActor(soldiers.get(i));  // calculate the distance between this elephant and the soldier using calculateNearestActor method (which is discussed in the Units class)
                if (nearestDistance < distance){ // if the distance is closer than any other previously calculated distances then, this vehicle is the closest
                    distance = nearestDistance;   // the distance will be stored to test the remaining vehicles
                    stepOver = soldiers.get(i);  // the soldier will be stored as the closest soldier to the elephant (which can be later changed if another soldier is closer)

                }
            }

            if (stepOver!= null && stepOver.getWorld() != null){  // if there was any soldiers closer to the elephant whose lives are greater than 0 then,
                stepOver.instanceDeath(true);  // kill them with the option of being revived- replace them with the tombstone
            }
        }
        distance =1100;  // set the distance back to a huge number so this method can be used again
    }

    /**
     * Returns whether there is an empty spot on the elephant or not
     * @return true - there is an empty spot, false - there isn't an empty spot
     */
    public boolean rider(){
        if (riders<2){  // if there is less than two riders- more soldiers can ride the elephant, then
            return true;   // return true
        }
        return false;  // otherwise return false
    }

    /**
     * Returns the number of riders on the elephant
     * @return the number of riders on the elephant
     */
    public int numberOfRiders(){
        return riders;   // return the number of soldiers currently riding the elephant
    }

}