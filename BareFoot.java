import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;  // import the list class

/**
 * A barefoot's goal is to attack the opponent's base. However, a maximum of 2 soldiers can attack the oppoenent's base. Therefore, if the barefoot cannot attack the base
 * because there is no space, he will wander around on the screen. His number one priority on the battlefield is to ride a vehicle (because that will give him more life).
 * The elephant is prioritized over the horse (elephant- 5 lives, horse- 4 lives). However, only 2 can ride an elephant. The barefoot will never attempt to move towards
 * the elephant or the horse if there is already closer soldiers to them than he is (will ride before him). Throughout this whole mission, if a soldier meets another
 * soldier from the opposite side, he will shoot at it (his aiming will not be perfect, neither will the other soldier), once the other soldier is dead, he will move
 * on to do whatever he was doing before. However, the soldier will never stop to shoot at another soldier if he is seeking a horse or an elephant. Also, a soldier will
 * not shoot at a vehicle unless he was wandering around and not going to the opponent's base. If a soldier is riding a horse or an elephant and that vehicles dies, he will
 * get off with full life. When the soldier dies, he will turn to a tombstone waiting for a medic to revive him back. A soldier can only be revived once. If a soldier dies
 * while shooting at the opponent's base, he will not be revived. Also, once the soldier gets to the opponent's base, he will not attempt anything, but instead will keep
 * shooting at it until he dies. A soldier has 3 lives throught the stimulation. If the barefoot walks in the opponent's elephant's path, he will die (and could be later
 * revived if he was never revived before). A barefoot will never shoot if his arrow will only hit the tree without any possiblity of hitting the target or if his arrow 
 * will hit soldiers from his base before making its way to the target (friendly fire could occur if the soldier's aim was off). Barefoots will go around trees (not thorugh
 * them)
 * 
 * @author (Ramy Elbakari and Nicholas Chan) 
 * @version (0.0.3)
 */
public class BareFoot extends Soldiers
{

    // the following three variables determine the state of the barefoot on the screen
    private boolean randomWalk=false;  // randomly walking on the screen
    private boolean seekingBase=false;  // walking towards the opponent's base (to shoot at it)
    private boolean seekingElephant=false;  // walking towards an elephant (to ride it)

    private int below=40;  // a maximum of two soldier can attack the opponent's base, the second soldier will stant 40 pixels below the first soldier
    private boolean spotTaken = false;  // variable to keep track of whether the soldier is attacking the opponent's base or not (true= is attacking, false= not attacking)
    private int turnTowardsX=888;  // x-coordinates of location the white soldier will be standing when attacking the base- will be changed if the soldier is wearing orange
    private int turnTowardsY=505;  // y-coordinates of location the soldier (any color) will be standing when attacking the base- will be changed if a soldier is already attacking the base

    private BareFoot bareFoot1, bareFoot2;  // variables to keep track of which two soldiers are going to attack the base
    private boolean revived = false;  // check if the soldier was revived before (a soldier can only be revived once)
    
    /**
     * Constructs the soldier. Defines the side and base the soldier comes from and amount of life the soldier will have.
     * Sets the speed of the soldier and displays its health icons.
     * @param side- which base the soldier will come from true - right side, false- left side
     * @param myBase - which base the soldier comes from
     */
    public BareFoot (boolean side, MainBase myBase){  // determine the side of the barefoot and which base he comes from
        this.side=side; // set the side of the soldier
        if (side==false){  // if the side is false (left side)
            this.setImage("orangeSoldier.png");   // change the picture to a soldier wearing orange          
            turnTowardsX=202;  // x-coordinate of location the orange soldier will be standing when attacking the base

        }
        speed =2;  // set the speed to 2 pixels per act
        this.lives = 3;  // set the lives to 3
        this.base =myBase;  // set the base in which the barefoot comes from
        // display the three health icons of the barefoot
        icon1 = new HealthIcon(1,this);
        icon2 = new HealthIcon(2,this);
        icon3 = new HealthIcon(3,this);

    }

    /**
     * Act - do whatever the BareFoot wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls how the soldier will react in different situations.
     */
    public void act() 
    {

        if (spawnMovement ==false){  // if the soldier isn't coming out the base, then
            counter = Greenfoot.getRandomNumber(31)+20;  // random number (20- 50) that detemines when the soldier will shoot-changes every act (which makes it EXTREMELY random because the number has to be divisible by 20 for the shooting to occur)
            randomMiss = Greenfoot.getRandomNumber(11);  // the amount of miss in pixels (the arrow will go a certain number of pixels beside the target)
            cofficient = Greenfoot.getRandomNumber(2);  // variable used to determine whether the soldier will miss (to the right or left) of the target
            if (cofficient ==1){  // if the cofficient is 1, then the miss will be higher (above the soldier) - appears to be to left/right of the soldier depending on the type of soldier (white- to the left of him/ orange- to the right of him)
                randomMiss = -randomMiss;
            }

            counter++;  // increase the counting variable by one - will only affect every random number once

            movementAroundObstacles(); // checks if there is an obstacle and moves around it

            if (locateNearestElephant() && elephant.goToElephant(this) && elephant.rider()==true){  // the soldier locates the nearest elephant and if that elephant is not taken (no soldiers are already moving towards it nor is it already full), then
                walkToNearestElephant();  // walk to that elephant
                seekingElephant =true;  // set the status of the soldier to seeking an elphant
                // set the other conditions false (not randomly walking or seeking base)
                randomWalk=false;
                seekingBase=false;
            }else if (locateNearestHorse() && horse.goToHorse(this) && horse.rider()==false){  // same concept applies to the horse (similar to the elephant)
                walkToNearestHorse();
                randomWalk=false;
                seekingBase=false;
            }else if (this.spotTaken ==false && this.seekingBase ==false && locateOppVehicle()&& checkTree() && vehicle.numberOfRiders()>0){ // if soldier not shooting at opponent's base and is randomly walking and he located an opponent's vehicle (horse or elephant) and no tree in between them and the vehicle is not empty, then
                shoot();  // shoot at the vehicle -shooting is randomized every act

            }else if (this.spotTaken ==false && checkCloseSoldiers(!this.side) && aim() && checkTree()){  // if the soldier is not shooting at the base and locates an opponent's soldier nearby and no tree seperates between the two then,

                shoot();   // shoot at the soldier -shooting is randomized every act (therefore the winner is randomized every time)
                randomWalk=false;  // set the other conditions false (not randomly walking or seeking base)
                seekingBase=false;

            } else if(goToBase()){  // if no two soldier are shooting at the base or this soldier is the closest to the opponent's base if there isn't any one shooting then,
                walkToOppBase();  // walk to the opponent's base
                randomWalk=false;  // the soldier is not randomly walking
                seekingBase=true;  // but is walking to the base
            } else {  // if none of the above conditions are met then,
                randomWalk();  // the soldier will randomly walk on the screen
                randomWalk=true;  // the soldier's status will be indeed randomly walking on the screen
                seekingBase=false;  // and he is not seeking the base
            }

            checkLives(true);   //Updates the amount of Health Icons following the soldier in accordance to their current amount of lives
        } else {  // else if the soldier is still undergoing spawn movement then,
            spawnMovement();   // this method will tell him how to exit the protective wall and into the battlefield- a soldier will remain inactive until this condition
            // is reached- he will neither die or be affected with any thing in his surroundings

        }

        //getWorld().setPaintOrder (StatDisplay.class, StoneWall.class);  // set the paint order
    }

    /**
     * Moves around obstacle while still walking towards the desired location
     * If soldier is randomly walking or reaches the edge of the world then, 
     * ranomly rotates and walks in a different direction.  
     */
    private void movementAroundObstacles(){
        while((checkObstacle()&& seekingBase ==false) || atWorldEdge()){  // if the soldier hits an obstacle while not seeking the base or when the soldier is at the edge of the world then,
            if (randomWalk==true){  // if he is randomly walking, then
                turn (Greenfoot.getRandomNumber(361));  // turn between an angle of 0-360
                move (2);  // move by 2
            } else{  // else if he is seeking an elephant or a horse
                if (getRotation()>180){  // if the soldier is moving to the right, then
                    turn (-15);  // move around (underneath) the object
                } else{ // else (if he is moving the left)
                    turn(15);  // move around (above) the object
                }
                move (1); // move by 1
            }

        }
        while (checkObstacle () && seekingBase ==true){  // if the soldier hits the obstacle while seeking the base
            turn(15);  // move around (above) the object - otherwise glitches occur
            move (speed);  // move by the current speed
        }

    }

    /**
     * Makes the soldier randomly walk around the battlefield
     * Changes direction every once in a while.
     */
    private void randomWalk(){
        if (Greenfoot.getRandomNumber(200)==0){  // chances of barefoot changing direction while he is walking is 1 out of 200
            setRotation (Greenfoot.getRandomNumber(361)-180);  // change rotation between -180 and 180
        }
        move (speed);  // move with the current rotation
    }

    /**
     * Checks to see if the soldier is at the edge of the world
     * @return true- he is, false- he is not
     */
    private boolean atWorldEdge (){
        if (randomWalk ==true){  // if the soldier is randomly walking then,
            if (845 <= getX() +1 || getX() - 1 <= 255 || getWorld().getHeight() <= getY() + 1 || getY() - 1 <= 120){ // if soldier hit boundries (just before the wall of the snipers or below the river, then
                return true;  // return true
            } else {  // if doesn't hit any of those boundries, then
                return false;  // return false
            }

        } else{  // if he is not randomly walking, then the boundries will be different
            if (890 <= getX() +1 || getX() - 1 <= 180 || getWorld().getHeight() <= getY() + 1 || getY() - 1 <= 120){  // the boundries will be below the sniper (because the soldiers will be shooting closer to the base) and the boundries will still be below the river
                return true;  // return true
            } else {    // if doesn't hit any of those boundries, then
                return false;    // return false
            }

        }
    }

    /**
     * Shoots an arrow in the direction he is currently facing. 
     * Does not work every times it is called- counter variable must be
     * divisible by 20 for the shooting to take place.
     * 
     */
    protected void shoot(){
        if (counter%20==0){  // shoot every 20 acts
            getWorld().setPaintOrder(BareFoot.class);  // arrow should appear below soldier
            getWorld().addObject(new Arrow (getRotation() + randomMiss, this, "barefoot", base), getX(), getY());  // generate an arrow with a rotation that is similar or close to (depends on magnitude of the miss) the rotation of the soldier
            Greenfoot.playSound("bow.mp3");  // play sound for the arrow shot
        }

    }

    /**
     * Locates the nearest elephant to this soldier. Elephant must be from the same side as the soldier.
     * Defines the elephant variable to be used in the walkToNearestElephant method and to access methods from the elephant class.
     * @return true - there is an elephant from the same side on the screen,  false - there isn't one
     */
    private boolean locateNearestElephant(){
        elephants = getWorld().getObjects(Elephant.class);   // scan the world for every single elephant
        for (int i=0; i< elephants.size(); i++){ // loop through every elephant to determine which is closer
            if (elephants.get(i).getSide()!=side || elephants.get(i).goToElephant(this)==false){  // remove the elephant who is not your side (can't ride that one) or the one that soldiers are already going to (other soldiers already have a head start)
                elephants.remove(i);  // remove that elephant from the list
                i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            } else{  // if neither conditions are meet, then the rest of the elephants are yours and now it is time to determine which one is closer
                nearestDistance = calculateNearestActor(elephants.get(i));  // calculate the distance between this soldier and the elephant using calculateNearestActor method (which is discussed in the Units class)
                if (nearestDistance < distance){  // if the distance is closer than any other previously calculated distances then, this elephant is the closest
                    distance = nearestDistance;  // the distance will be stored to test the remaining elephants
                    elephant = elephants.get(i);  // the elephant will be stored as the closest elephant (which can be later changed if another elephant is closer)

                }
            }
        }

        if (elephants.size()!= 0){  // if an elephant exists on the screen (whose on your side)

            distance =1100;  // set the distance back to a huge number so this method can be used again
            return true;  // return true (there is an elephant -which is now defined)
        } else{  // if not,
            return false;   // return false
        }

    }

    /**
     * Locates the nearest horse to this soldier. Horse must be from the same side as the soldier.
     * Defines the horse variable to be used in the walkToNearestHorse method and to access methods from the horse class .
     * @return true - there is a horse from the same side on the screen,  false - there isn't one
     */
    private boolean locateNearestHorse(){
        horses = getWorld().getObjects(Horse.class);  // follows the same exact concept as the locateNearestElephant method above but with horses variables instead
        for (int i=0; i< horses.size(); i++){
            if (horses.get(i).getSide()!=side || horses.get(i).goToHorse(this)==false){
                horses.remove(i);
                i--;
            } else{
                nearestDistance = calculateNearestActor(horses.get(i));
                if (nearestDistance < distance){
                    distance = nearestDistance;
                    horse = horses.get(i);

                }
            }
        }

        if (horses.size()!= 0){

            distance =1100;
            return true;
        } else{
            return false;
        }

    }

    /**
     * Walks to the nearest elephant defined in the locateNearestElephant method.
     * Moves with the current speed of the soldier.
     */
    private void walkToNearestElephant(){
        // this method is only called if there is a nearby elephant to the soldier
        turnTowards (elephant.getX(), elephant.getY());  // turn towards the coordinates of that nearest elephant
        move (speed);  // move towards those coordinates
    }

    /**
     * Walks to the nearest horse defined in the locateNearestHorse method.
     * Moves with the current speed of the soldier.
     */
    private void walkToNearestHorse(){
        // this method is only called if there is a nearby horse to the soldier
        turnTowards (horse.getX(), horse.getY());  // turn towards the coordinates of that nearest horse
        move (speed);   // move towards those coordinates
    }

    /**
     * Directs the soldier to the opponent's base. Once the soldier arrives in front of 
     * the opponent's base, he starts shooting. If there is already a soldier shooting
     * at the base, this soldier moves below him and once the first soldier dies, this
     * soldier moves to his place for accurate aiming.
     * 
     */
    private void walkToOppBase(){

        if (spotTaken == false && checkCloseSoldiers(this.side) && bareFoot.spotTaken()){  // if the soldier didn't start attacking the base yet (first condition) AND there is another soldier already attacking it (the last two conditions)

            turnTowardsY= bareFoot.getY()+below;  // turn towards the spot below him
        }

        if (getOneObjectAtOffset (0, -below, BareFoot.class)== null && stop2==true && this.spotTaken ==true){ // if the soldier above him dies while this soldier is attacking the base, then (stop2 variable is to ensure that the following code will be executed once)
            turnTowardsY = 505;  // turn towards the y-coordinate that is above the soldier's current location
            if (this.side==true){  // if the soldier is wearing white, then
                setRotation (getRotation()-10);  // set the rotation to shoot more accurate to the base (since you changed your previous position)
            } else {  // else if the soldier is wearing orange, then
                setRotation (getRotation()+10);  // // set the rotation to shoot more accurate to the base (since you changed your previous position) (different code than the one above because each side is like a reflection of the other)
            }
            stop2=false;  // prevent executing this code again or else the soldier will keeping moving upward (because there will be no soldier above him)
            setLocation (turnTowardsX, turnTowardsY);  // set the new location
        }

        if ((this.getX() >= turnTowardsX-2 && this.getY() >= turnTowardsY-2 && this.side ==true) ||      ((this.getX() == turnTowardsX+2 || this.getX() == turnTowardsX+1 || this.getX() == turnTowardsX) && (this.getY() == turnTowardsY+2 || this.getY() == turnTowardsY+1 || this.getY()+1 == turnTowardsY ||this.getY() == turnTowardsY) && this.side ==false)){
            // if the soldier's position is off by 1 or 2 pixels (because sometimes it is impossible for the soldier to move the desired location because the speed is 2 while the distance required for the soldier to move to meet the requried location is 1

            setLocation (turnTowardsX, turnTowardsY);  // set the soldier's location to the desired location
            if (bareFoot!= null && bareFoot.spotTaken() && this.spotTaken==false){ // if there is a soldier already shooting (barefoot -defined when checkCloseSoldiers method was run above) and this soldier is about to shoot at the base, then
                if (this.side ==true){  // if this soldier is wearing white, then 
                    turnTowards (1040,378); // turn towards the opponent's base (the location the soldier was moving at this whole time is just a location in front of the base (a shooting location))            
                    setRotation(bareFoot.getRotation()+10);  // set the rotation of the soldier to be slighly less than at the base or else his arrows will hit the soldier above him (whose also shooting at the base)
                } else{  // else if the soldier is wearing orange
                    turnTowards (50,378);    // turn towards the opponent's base (has different location than the one above because it is on the other side of the screen)(the location the soldier was moving at this whole time is just a location in front of the base (a shooting location))
                    setRotation(bareFoot.getRotation()-10);  // set the rotation of the soldier to be slighly less than at the base or else his arrows will hit the soldier above him (whose also shooting at the base)
                }
                stop =true;  // prevent executing the code below ever
            }  else if (stop== false){  // else if (this code has never been run before)- also means that there is no soldier already shooting at the base when this soldier arrives
                if (this.side ==true){  // if this soldier is wearing white, then
                    turnTowards (1040,378);  // turn towards the opponent's base
                } else {  // else if this soldier is wearing orange then
                    turnTowards (50,378);  // turn towards the opponent's base (different location because it is on the other side of the screen)
                }
                stop =true;  // prevent the computer to run this code ever again
            }
            this.spotTaken = true;  // the spot has been taken - the soldier is shooting at the base

            shoot();  // shoot every (random act) -discussed more in the act method
        }

        // moving toward target
        if ((getX()!=turnTowardsX && getY()!=turnTowardsY)){  // if the soldier didn't reach his desired location (shooting location), then

            turnTowards(turnTowardsX,turnTowardsY); // keep turning towards that location
            move (speed);  // keep moving towards that location
        } 

    }

    /**
     * Checks to see if the soldier is shooting at the base or not.
     * Gets called by the elephant and horse classes to determine whether the 
     * soldier is willing to go and ride them. A soldier will not ride any vehicle
     * if he is already shooting at the opponent's base.
     * @return true -soldier is shooting at the base, false- soldier is not
     */
    public boolean spotTaken (){
        if (spotTaken ==true){  // if the spot is taken (soldier shooting at the base), then
            return true;  // return true
        } else {  // else
            return false;  // return false
        }

    }

    /**
     * Checks to see if the soldier should make his way to the opponent's base.
     * Only two people are allowed to shoot at the base, if there is less than two people shooting
     * and this soldier is the closest to the opponent's base than any other soldier, this soldier should make his way
     * to the opponent's base. Any soldier who is already seeking an elephant will not be considered for this method.
     * This method depends on the nearestSoldierToBase method to determine which soldier is closer to the base.
     * @return true - should walk to opponent's base,  false- should not walk
     */
    private boolean goToBase(){

        // determines whether the soldier should head to the base or not (meaning check if it is one of the 2 closest soldiers to the base)
        sameSoldiers = getWorld().getObjects (BareFoot.class);  // scan the world for all barefoots - will be deduced to same-side soldiers later
        nearestSoldierToBase();  // method that determines the closest soldier (of your side) to the opponent's base
        bareFoot1=bareFoot;  // that soldier becomes bareFoot1

        // the following codes are the same exact as the nearestSoldierToBase except that the soldier can't be bareFoot1 - meaning that it will be the SECOND closest soldier to the opponent's base
        // the explanation of the method is in the method itself
        for (int i=0; i< sameSoldiers.size(); i++){

            if (sameSoldiers.get(i).getSide()!=side || sameSoldiers.get(i).checkSeekingElephant()){  // if bareFoot1 is scanned then, eliminate it from the list. That will leave you with second closest soldier (however the computer will think it is the closest since the true closest soldier is no longer in the list) 
                sameSoldiers.remove(i);
                i--;
            }else{
                nearestDistance = calculateNearestSoldierToBase(sameSoldiers.get(i));
                if (nearestDistance < distance && sameSoldiers.get(i)!= bareFoot1){
                    distance = nearestDistance;
                    bareFoot = sameSoldiers.get(i);

                }
            }

        }
        distance =1100;

        bareFoot2=bareFoot;  // the second closest soldier becomes bareFoot2

        if (this == bareFoot1 || this == bareFoot2){  // if this actor is any of the two closest soldiers, then
            return true;  // return true
        }

        return false;  // if not then return false

    }

    /**
     * Determines which soldier is closer to the opponent's base. 
     * This method depends on the calculateNearestSoldierToBase method
     * to calculate distances between soldiers and the opponent's base.
     * This method also depends on the goToBase method to access different lists and variables.
     */
    private void nearestSoldierToBase(){
        // calculate the closest soldier to the base (will also be used to determine the second closest-more on that in the code above)
        for (int i=0; i< sameSoldiers.size(); i++){ // loop through all the soldiers on the screen
            if (sameSoldiers.get(i).getSide()!=side || sameSoldiers.get(i).checkSeekingElephant()){  // eliminate the opponent's soldiers and your soldiers who are seeking the elephant because neither one of them will attack the opponent's base
                sameSoldiers.remove(i);  // remove that soldier from the list
                i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            }else{  // if neither conditions are meet, then the rest of the soldiers are yours and now it is time to determine which one is closer
                nearestDistance = calculateNearestSoldierToBase(sameSoldiers.get(i));  // calculate the distance between this soldier and the base using calculateNearestSoldierToBase method (which is discussed below)
                if (nearestDistance < distance){  // if the distance is closer than any other previously calculated distances then, this soldier is the closest
                    distance = nearestDistance; // the distance will be stored to test the remaining soldiers
                    bareFoot = sameSoldiers.get(i);  // the soldier will be stored as the closest elephant (which can be later changed if another elephant is closer)

                }
            }

        }
        distance =1100;  // set the distance back to a huge number so this method can be used again
    }

    /**
     * Calculate distances between this soldier and a the opponent's base.
     * Gets called by the nearestSoldierToBase method.
     * @param actor - the soldier in which the distance between it and base will be measured
     */
    private double calculateNearestSoldierToBase(Actor actor){
        return Math.hypot(actor.getX() - turnTowardsX, actor.getY() - turnTowardsY);  // calculate the nearest soldier to the base using pythagreom theory
    }

    /**
     * Adds a tomb stone after the soldier dies. This must be the soldier's first death. Each soldier can only be revived once.
     * However, if the soldier died while shooting at the base, he cannot be revivied. 
     * This method gets called from the checkLives method.
     */
    protected void revive(){
        if(this.revived == false)  // if soldier never been revived before then, 
        {
            if (this.spotTaken==false){  // also if the soldier is not shooting at the base then,

                getWorld().addObject(new Tombstone(this.side), getX(), getY()); //its body will be replaced by a tombstone- which will be revived back by the medics
            }

        }

    }

    /**
     * Marks that this solider cannot be revived again.
     * Gets called from the medics class after the medic has revived the soldier.
     */
    public void revived()
    {

        this.revived = true;  // the soldier has been revived once already
        spawnMovement =false;  // the soldier does not need to undergo spawn movement
    }

    /**
     * Loses a life. The soldier's life decreases by one.
     * If the soldier's life reaches 0, a death is recorded in the mainbase class.
     * This method gets called by the arrow class.
     */
    public void loseALife() 
    {
        if(this.lives > 0)  // if soldier's life is bigger than 0, then
        {
            if (this.lives == 1)  // if his life is 1, then
            {
                base.setDeaths(base.getDeaths()+1);  // the death count will be recorded in the opponent's base
            }
            this.lives--;     // decrease the soldier's life by 1      
        }
    }

    /**
     * Checks to see if the soldier is seeking a nearby elephant to ride it.
     * Gets called by BareFoot class and Horse class.
     * @return true - soldier is seeking an elephant, false - is not seeking an elephant
     */
    public boolean checkSeekingElephant(){
        if (seekingElephant ==true){  // if soldier is moving toward an elephant, then
            return true;  // return true
        }
        return false;  // else return false
    }

    /**
     * Locates the nearest opponent's vehicle. Method will be used in the act method.
     * @return true- a vehicle has been located, false - a vehicle has not been located
     */
    public boolean locateOppVehicle(){
        vehicles = getObjectsInRange(100, Vehicles.class);  // scan all the vehicles (elphants or horses) that are at least 100 pixels around the soldier
        for (int i=0; i< vehicles.size(); i++){  // loop through every single vehicle
            if (vehicles.get(i).getSide()==side){  // if the vehicle is the same side as the soldier then,
                vehicles.remove(i);  // eliminate the vehicle from the list because the soldier is scanning the opponent's vehicle (to attack)
                i--;  // decrease the loop because the other elements of the list will be shifted down by one because one element got eliminated
            } else{   // if neither conditions are meet, then the rest of the vehicles are yours and now it is time to determine which one is closer
                nearestDistance = calculateNearestActor(vehicles.get(i));   // calculate the distance between this soldier and the vehicle using calculateNearestActor method (which is discussed in the Units class)
                if (nearestDistance < distance){  // if the distance is closer than any other previously calculated distances then, this vehicle is the closest
                    distance = nearestDistance;  // the distance will be stored to test the remaining vehicles
                    vehicle = vehicles.get(i);  // the elephant will be stored as the closest vehicle (which can be later changed if another vehicle is closer)

                }
            }
        }

        if (vehicles.size()!= 0){  // if a vehicle exists on the screen (whose on your side)
            turnTowards (vehicle.getX(), vehicle.getY());  // turn towards the coordinates of the vehicle
            distance =1100;  // set the distance back to a huge number so this method can be used again
            return true;  // return true
        } else{
            return false;  // if not then return false
        }
    }

    /**
     * Contols the movement of the soldier until he leaves his base.
     * The soldier will not interact with any thing until this method is successfully ahieved.
     */
    private void spawnMovement (){  
        // when a soldier is spawned beside the base, he will move outside the wall, the following code outline his movement (till he gets out of his base's protected wall) 
        if (this.side==true){  // if the soldier is from the left side (white)
            turnTowards (263,324);  // turn towards the coordinates that are just the right of the wall
            speed=1;  // the speed will be decreased to 1
            move (speed);  // move towards the above coordinates

            if (getX() ==263 && getY()==324){  // once he reaches those coordinates, then
                speed=2;  // the speed will be set back to 2
                spawnMovement =false;  // the spawn movement is done
            }
        } else {  // else if the soldier originates from the right side (orange)
            turnTowards (827,324);   // turn towards the coordinates that are just the left of the wall
            speed=1;  // the speed will be decreased to 1
            move (speed);  // move towards the above coordinates

            if (getX() ==827 && getY()==324){  // once he reaches those coordinates, then
                speed=2;   // the speed will be set back to 2
                spawnMovement =false;  // the spawn movement is done
            }
        }
        getWorld().setPaintOrder (StoneWall.class, Snipers.class);  // the soldier will walk underneath the wall and snipers to make it look as if he goes through the door of the wall and into the battle field
    }


}
