import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;
/**
 * Main base that spawns all troops and keeps track of the properties 
 * of the research facility and storage. This class manages the majority of the statistics of this entire simulation.
 * In terms of all the troops on screen, when to spaen each troop, and even the death amounts, damage values and supply needs. 
 * This class uses the other building child classes as aids to manage this information and spawn the appropriate units
 * at the correct moments. Most of the main variables in the main base class are modifiable by the user of the simulation in the 
 * Start World. The user is able to modify these values to induce different outcomes.
 * 
 * @author Isaac Chang
 * @version 0.0.6
 */
public class MainBase extends Buildings
{
    //instance variables
    //true is left side, false is right side
    private int startBareFootAmount;  // the amount of barefoot that will initially spawn on the screen when the stimulation runs

    private boolean initalSpawnIndicator = false;
    private boolean initialIndicator = false;

    // you can only have one of those units on the screen at a time (true- there is already one-don't spawn another, false-there isn't one-spawn one if possible)
    private boolean medicCheck = false;
    private boolean shipCheck = false;
    private boolean scoutCheck = false;
    private boolean cannonCheck = false;

    private int deaths;  // the amount of deaths
    private int tombstoneOnScreen = 0;  // the amount of tombstone on the screen (from your side)
    private int bareFootOnScreen = 0;  // the amount of soldiers on the screen (from your side)
    private int horseOnScreen = 0;  // the amount of horses on the screen (from your side)
    private int elephantOnScreen = 0;  // the amount of elephants on the screen (from your side)
    private int medicOnScreen = 0;  // the amount of medics on the screen (from your side)
    private int maxHorse = 2;   // a maximum of 2 horses can be on the screen (from your side)
    private int maxElephant = 1;    // a maximum of 1 elephant can be on the screen (from your side)

    //spawn cool down
    private int coolDownTime;  // spawn rate
    private int coolDownCount = 0;  // counts till the spawn rate is reached (spawns more units then resets to 0 to do cylce over again)
    private boolean coolDownCheck = false; // checks to see if the counter reached the spawn rate (true-it did, false-it didn't)

    private int count = 0;  // variable serves as a counter (more on it in the initialTasks method)
    private boolean horseOnTop; // checks to see where your horse is on the screen so that the next horse will be spawn on the other side of the screen (true-top, false-bottom)

    //to reset the damage done by each troop to zero so that horses and elephants have 
    //a chance at spawning
    private boolean firstHorseCheck = false;
    private boolean firstElephantCheck = false;

    //damage done by Barefoot, horse, and elephants will alays start off as zero 
    private int damageBareFoot = 0;
    private int damageHorse = 0;
    private int damageElephant = 0;

    //Note: bareFoot is level 1 research, Horse is level 2, elephants is level 3
    //the amount of supply points needed to spawn each unit
    //is subject to change is necessary
    private int bareFootSupply = 10;
    private int elephantSupply = 30;
    private int horseSupply = 20;
    private int medicSupply = 2;

    //objects that come with the main base
    //are spawn so that main base can easily check the properties of the its storage and research
    StoneWall myStoneWall;
    Storage myStorage;
    Research myResearch;
    Ship storageShip;
    Scout scout;
    Cannon cannon;
    Medic medic;

    /**
     * Creates a Main Base that has the default hitPoints and where the
     * side needs to be specified as well as all other facilities so that they 
     * are one the same side of the main base
     * 
     * not really used, really only here to test simulation in the process of creating it
     * 
     * @param sideIn                the side the main base is on, true for one side, false for the other
     */
    public MainBase(boolean sideIn)
    {
        hitPoints = 1500; //default amount of hit points
        side = sideIn; 
        startBareFootAmount = 6;//subject to change, default value
        coolDownTime = 250;//subject to change, default value
        // instantizing a stone wall on the same side as main base with default hit points
        myStoneWall = new StoneWall(sideIn); 
        //instantize a storage on the same side as main base
        //with default hitPoint
        myStorage = new Storage(sideIn);
        //instantize a research facility in the same side as main base 
        //with default hitPoint
        myResearch = new Research(sideIn); 

        storageShip = new Ship();  // initialize ship
        scout = new Scout(sideIn,this);  // initialize scout
        cannon = new Cannon();  // initialize cannon
        medic = new Medic(sideIn,this);  // initialize medic
    }

    /**
     * Creates a Main Base that requires a specified initial 
     * hitPoint and side, as well as all other faicilities so that they are on the same
     * side as the main base and also have specified values for each of their own 
     * unique properties 
     *
     * @param sideIn                    the side the main base is on, true for one side, false for the other
     * @param hitPointsIn               the initial hitPoints the main base will start off with  
     * @param startBareFootAmount       initial amount fo bareFoot soldiers spawn
     * @param coolDownTime              the cool down time between each spawning of units
     * @param startResearchPoint        the initial research points for research facility
     * @param startStoragePoint         the initial storage points for storage facility
     */
    public MainBase(boolean sideIn, int hitPointsIn, int startBareFootAmount, int coolDownTime, int startResearchPoint, int startStoragePoint)
    {
        hitPoints = hitPointsIn;
        side = sideIn;
        this.startBareFootAmount = startBareFootAmount;
        this.coolDownTime = coolDownTime;
        // instantizing a stone wall on the same side as main base with predetermined hit points
        myStoneWall = new StoneWall(sideIn, 100); 
        //instantize a storage on the same side as main base with a predetermined storage point       
        myStorage = new Storage(sideIn, startStoragePoint);
        //instantize a research facility in the same side as main base with a predetermined research point        
        myResearch = new Research(sideIn, startResearchPoint);

        storageShip = new Ship();  // initialize ship
        scout = new Scout(sideIn,this);  // initialize scout
        cannon = new Cannon();  // initialize cannon
        medic = new Medic(sideIn,this);  // initialize medic
    }

    /**
     * Act - do whatever the MainBase wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {        
        initialTasks(); // initial spawns of barefoot and buildings
        spawnShip();  // spawn ships when supply points drop significantly

        //check methods to see what is on the screen
        checkOnScreenBareFoot();
        checkHorseOnScreen();
        checkElephantOnScreen();
        checkTombstoneOnScreen();
        checkMedicOnScreen();

        updateResearch();  // research is based on how much killings the units do- send the info to the research facility to upgrade to new weapons
        //spawn units if cool down is done counting
        if (coolDownCheck == false)
        {
            spawn(); // spawn new units
        }
        //checks if new level is achieved 
        checkNewLevel();
        //cool down counting
        if (coolDownCheck == true)  // if coolDown still didn't reach the spawning rate, then
        {
            coolDownCount++;  // keep counting up
        }
        if (coolDownCount == coolDownTime)  // once it reaches the spawning rate, then
        {
            coolDownCheck = false;  // turn it off (done counting)
            coolDownCount = 0;  // set it back to zero
        }
        if (bareFootOnScreen == 0)  // if no barefoot is on the screen (from your side), then
        {
            spawnBareFoot();  // spawn a new one
        }
        //for cannon at level 4 spawn cannon, only once
        if (myResearch.getResearchLevel() == 4 && cannonCheck == false)  //if research points reach to level 4 and a connan has never been spawned, then
        {
            if (this.side == true) // if this side is on the left, then
            {
                getWorld().addObject(cannon,150,220); // spawn a cannon
            }
            if (this.side == false)  // if it was on the right, then
            {                
                getWorld().addObject(cannon,1090-150,220);  // spawn a cannon
                cannon.setRotation(180);  // and turn it to face the left side (default is facing right side)
            }
            cannonCheck = true;
        }

        //spawning extras

        //Spawning Medics
        //Spawn a medic when there is more than 3 tombstones on the screen
        int count = 0;  // variable will be used to count amount of tombstones in the world (that belongs to you)
        List<Tombstone> TList = getWorld().getObjects(Tombstone.class);  // scan all the tomb stones in the world
        for (Tombstone TL: TList)  // loop through all the tombstones
        {
            if (TL.getSide() == this.side)  // if they are from you side, then
            {
                count++;  // increase the counting variable by 1
            }           
        }
        if (count >=3  &&  medicCheck == false)  // if there is 3 or more tombstones and there is no medic in the world, then
        {
            getWorld().addObject(medic,getX(),getY());  // spawn a new medic
            medicCheck = true;  // there is a medic on the screen
        }
        if (medic.getWorld() == null)  // if there medic doesn't exsist
        {
            medicCheck = false;  // no medic on screen
        }

        //Spawning scouts
        //checking if there are crates, if there are, then spawn a scout to collect them
        count = 0;  // variable will be used to count amount of crates in the world (that belongs to you)
        List<Crate> CList = getWorld().getObjects(Crate.class);  // scan all the crates in the world
        for (Crate CL: CList)  // loop through all the crates
        {
            if (CL.getSide() == this.side)  // if they are from you side, then
            {
                count++;  // increase the counting variable by 1
            }
        }
        if (count == 2 && scoutCheck == false)  // if there is 2 crates (ship drops 2 crates) and there is no scouts out there
        {
            getWorld().addObject(scout,getX(),getY());  // add a scout
            scoutCheck = true;  // scout is in the world
        }
        if (scout.getWorld() == null)  // if scout doesn't exsist
        {
            scoutCheck = false;  // no scout in the world
        }

        if (this.hitPoints <=0){  // if the hit points of this base turn 0, then
            Field place = (Field)getWorld(); // get the current world
            place.goToWin(side);  // use it to access a method that takes the user to the winning screen      
        }
    }    

    /**
     * Carries out the tasks at the beginning of the game such as adding other buildings,
     * and spawning the correct amount fo starting bare foots.
     */
    private void initialTasks()
    {
        //adding the other buildings into the world
        if (initialIndicator == false)  // if this code has never been run before then,
        {
            if (side == true)  // if this side is on the left, then
            {
                // add the three buildings on the left
                getWorld().addObject(myStoneWall, getX()+130, getY()-10);
                getWorld().addObject(myStorage, getX()-13, getY()+180);
                getWorld().addObject(myResearch, getX()-10, getY()-140); 
            }
            else if (side == false)  // if this side is on the right, then
            {
                // add the three buildings on the right
                getWorld().addObject(myStoneWall, getX()-130, getY()-10);
                getWorld().addObject(myStorage, getX()+13, getY()+180);
                getWorld().addObject(myResearch, getX()+10, getY()-140); 

                // set the rotation of all builings to face the left side (default- facing the right side)
                this.setRotation(180);
                myStoneWall.setRotation(180);
                myStorage.setRotation(180);
                myResearch.setRotation(180);
            }
            initialIndicator = true;  // never run this code again
        }     

        //initial spawn of barefoots
        if (initalSpawnIndicator == false)  // if this code has never been run before, then
        {
            if (startBareFootAmount == 0)  // if no soldiers are going to be initially spawned then,
            {
                initalSpawnIndicator = true; //never run this code again
                
            }
            myStorage.setStoragePoint(myStorage.getStoragePoint()+bareFootSupply);  // decrease the storage points
            spawnBareFootRandom();  // spawn a barefoot
            count++;  // increase the counter by one
            if (count == startBareFootAmount)  // if the counter reaches the initial spawn of barefoot
            {
                initalSpawnIndicator = true;  // don't run this code again
            }

        }
        
    }

    /**
     * Spawns the ship when called to drop the supply crates, checks to see if there
     * ther are any other ships on the screen and if storage points of either base is low enough to 
     * need a ship to spawn. If both criteria are met then a ship is spawn. 
     */
    private void spawnShip()
    {
        int count = 0; //counts number of ships on screen
        List<Ship> SList = getWorld().getObjects(Ship.class);  // scan all the ships in the world
        for (Ship SL: SList) // loop through all the ships
        {
            count++;   // increase the counter by 1
        }
        //if ships on screen is zero than spawn a ship
        if (count == 0)  // if there is no ship
        {
            if (myStorage.getStoragePoint() < 10 && shipCheck == false)  // if storage points are below 10 and there is no ship on the screen,
            {
                getWorld().addObject(storageShip,0,30);  // spawn (call) a ship
                Greenfoot.playSound("ship.mp3");  // play the sound that marks the coming of a ship
                shipCheck = true;  // a ship is on the screen
            }
            if (storageShip.getWorld() == null)  // if the ship disappears
            {
                shipCheck = false;  // there is no ship on the screen
            }  
        }
    }

    /**
     * Spawns the respective units; bareFoot, horses, and elephants according to research level, damage done by each, storage points, and 
     * types of troops already on the field. 
     */
    private void spawn()
    {        
        //spawn based on conditions
        if (myResearch.getResearchLevel() == 1)  // if research level is 1
        {                       
                spawnBareFoot();     // spawn barefoot a barefoot (if there is enough storage points)      
            
        }

        if(myResearch.getResearchLevel() == 2)  // if research level is 2
        {

            if (damageHorse >= damageBareFoot && horseOnScreen < maxHorse)  // if horses did more damage than barefoots, then
            {
                spawnHorse();  // spawn a horse
            }
            else 
            {
                spawnBareFoot();  // else spawn a barfoot
            }

        }

        if(myResearch.getResearchLevel() >= 3)  // if research level is 3, then
        {            

            if (damageElephant > damageHorse && elephantOnScreen < maxElephant)  // if damage done by elephant is the highest, 
            {
                spawnElephant();  //spawn an elephant
            }
            else if (damageHorse > damageBareFoot && horseOnScreen < maxHorse) // if damage done by horse is the highest, 
            {
                spawnHorse();    //spawn a horse
            }
            else 
            {
                spawnBareFoot();  // else just spawn another barefoot
            }

        }        
        //updatign number of troops on screen for the protection sequence below since all in the same method
        checkElephantOnScreen();
        checkHorseOnScreen();
        //spawns a horse and an elephant at all times after level 3, however, only happens if there is an abundant amount
        //of resources: storage points is more than 100
        if (myResearch.getResearchLevel() >= 3 && myStorage.getStoragePoint() > 100)
        {
            //once a team gets to level 3 always have a horse on field to be fair
            if (myStorage.getStoragePoint() >= horseSupply && horseOnScreen == 0)
            {
                spawnHorse();
            } 
            //to protect base spawn an elephant
            if (myStorage.getStoragePoint() >= elephantSupply && elephantOnScreen == 0 )
            {
                spawnElephant();
            }
        }      
    }

    /**
     * Spawns a new set of troops not needing any storage points at the beggining of each new 
     * research level, primarily level 2 and level 3.
     */
    private void checkNewLevel()
    {
        if (myResearch.getResearchLevel() == 2 && firstHorseCheck == false)  // if the research level is 2 and this code has never been run before
        {
            //one to ride the horse
            //3 on feet
            myStorage.setStoragePoint(myStorage.getStoragePoint()+(bareFootSupply*3)+horseSupply);  // reduce the storage supply by 3 barefoots and a horse
            for (int i=0 ; i<4 ; i++)  // a loop that spawns a wave of soldiers (3)
            {
                spawnBareFootRandom();  // in random positions around the base
            }
            spawnHorse();  // spawn a horse
            // set all the damages done to 0 - since new level
            damageBareFoot = 0;
            damageHorse = 0;
            damageElephant = 0;
            firstHorseCheck = true;  // do not run this code again
        }

        // same concept applies as the code above
        if (myResearch.getResearchLevel() == 3 && firstElephantCheck == false)
        {
            //two to ride the elephant
            //one to ride the horse
            //3 on feet
            myStorage.setStoragePoint(myStorage.getStoragePoint()+(bareFootSupply*3)+horseSupply+elephantSupply);
            for (int i=0 ; i<3 ; i++)
            {
                spawnBareFootRandom();
            }
            spawnHorse();
            spawnElephant();
            damageBareFoot = 0;
            damageHorse = 0;
            damageElephant = 0;
            firstElephantCheck = true;
        }
    }

    /**
     * Updating research points based on the amounts of deaths recorded in other base.
     */
    private void updateResearch()
    {
        //returning all main bases on screen
        List<MainBase> MList = getWorld().getObjects(MainBase.class);
        for (MainBase MB: MList) // loop through all the mainbases (only 2)
        {
            //if the main base in the list is not itself then that must be the other base
            if (MB != this)
            {                
                //updating research points
                myResearch.setResearchPoints(0);
                myResearch.setResearchPoints(myResearch.getInitialPoint()+MB.getDeaths());    
            }
        }
    }

    /**
     * Counting number of bare foot soldiers on screen on the base's side.
     */
    private void checkOnScreenBareFoot()
    {
        int count = 0;  // counter will be used to count barefoots on the screen (from your side)
        List<BareFoot> BList = getWorld().getObjects(BareFoot.class);  // scan all the barefoots on the screen
        for (BareFoot BF: BList)  // loop through all the barefoots
        {
            if (BF.getSide() == side)  // if the barefoot is from your side
            {                
                count++;    // increase the counter by one             
            }
        }
        bareFootOnScreen = count;  // store the number of barefoot on the screen
    }

    /**
     * Counting number of horses on screen on the base's side.
     */
    private void checkHorseOnScreen()
    {
        // same concpet applies as the checkOnScreenBareFoot method
        int count = 0;
        List<Horse> HList = getWorld().getObjects(Horse.class);
        for (Horse HL: HList)
        {
            if (HL.getSide() == side)
            {                
                count++;                
            }
        }
        horseOnScreen = count;
    }

    /**
     * Counting number of elephants on screen on the base's side.
     */
    private void checkElephantOnScreen()
    {
        // same concpet applies as the checkOnScreenBareFoot method
        int count = 0;
        List<Elephant> EList = getWorld().getObjects(Elephant.class);
        for (Elephant EL: EList)
        {
            if (EL.getSide() == side)
            {                
                count++;                
            }
        }
        elephantOnScreen = count;
    }

    /**
     * Counting number of tombstones on screen on the base's side.
     */
    private void checkTombstoneOnScreen()
    {
        // same concpet applies as the checkOnScreenBareFoot method
        int count = 0;
        List<Tombstone> TList = getWorld().getObjects(Tombstone.class);
        for (Tombstone TL: TList)
        {
            if (TL.getSide() == side)
            {                
                count++;                
            }
        }
        tombstoneOnScreen = count;
    }

    /**
     * Counting number of medics on screen on the base's side.
     */
    private void checkMedicOnScreen()
    {
        // same concpet applies as the checkOnScreenBareFoot method
        int count = 0;
        List<Medic> MList = getWorld().getObjects(Medic.class);
        for (Medic ML: MList)
        {
            if (ML.getSide() == side)
            {                
                count++;
            }
        }
        medicOnScreen = count;
    }

    /**
     * Spawns a bare foot soldier at a varied x coordinate beside the base, decreasing the supplies it required.
     */
    private void spawnBareFootRandom()
    {
        if (myStorage.getStoragePoint() >= bareFootSupply)  // if there is enough storage points to spawn a barefoot, then
        {
            //specifying side to know where to spawn
            if (side == true)  // if they side is on the left,
            {
                getWorld().addObject(new BareFoot(true, this), getX()+Greenfoot.getRandomNumber(70), getY()+115); // spawn a barefoot (from the left side) (different coordinate every time)
            }
            else if (side == false)
            {
                getWorld().addObject(new BareFoot(false, this), getX()-Greenfoot.getRandomNumber(70), getY()+115);  // spawn a barefoot (from the right side) (different coordinate every time)
            }
            myStorage.setStoragePoint(myStorage.getStoragePoint()-bareFootSupply);  // decrease the storage supply
            coolDownCheck = true;   // spawning is done- start counting again until you reach the spawning rate
        }

    }

    /**
     * Spawns a bare foot soldier beside the base, decreasing the supplies it required.
     */
    private void spawnBareFoot()
    {
        // same exact concept as the spawnBareFootRandom (instead they spawn from the base at a specific coordinate- not random)
        if (myStorage.getStoragePoint() >= bareFootSupply)
        {
            if (side == true)
            {
                getWorld().addObject(new BareFoot(true, this), getX()+40, getY()+115);
            }
            else if (side == false)
            {
                getWorld().addObject(new BareFoot(false, this), getX()-40, getY()+115);
            }
            myStorage.setStoragePoint(myStorage.getStoragePoint()-bareFootSupply);
            coolDownCheck = true;
        }
    }

    /**
     * Spawns a horse soldier on either the top or bottom of the screen
     * depending on whether another horse is already there or not, decreasing the supplies it required.
     */
    private void spawnHorse()
    {
        //seeing if the first horse is on the top or bottom of the screen
        if (horseOnScreen == 1)  // if there is only one horse on the screen, then
        {
            List<Horse> HList = getWorld().getObjects(Horse.class);  // scan all the horses on the screen
            for (Horse HL: HList)  // loop through all the horses
            {
                if (HL.getSide() == side)  // if the horse is on your side
                {
                    if (HL.getY() == 115)  // if the horse has coordinates that are found at the top of the screen
                    {                
                        horseOnTop = true;   // a horse exsists and is at the top of the screen    
                    }
                    else  // if not then, 
                    {
                        horseOnTop = false;  // a horse exsists and is at the bottom of the screen   
                    }
                }
            }
        }
        //spawning horse 
        if (myStorage.getStoragePoint() >= horseSupply)  // if you have enough storage points for a horse, then
        {
            if (side == true) // if this side is on the left,
            {
                if (horseOnScreen == 1)  // if there is one horse on the screen
                {
                    if (horseOnTop == true) // if that horse is at the top, then
                    {
                        getWorld().addObject(new Horse(true,this), 175, 570); // spawn a horse at the bottom

                    }
                    else  // else 
                    {
                        getWorld().addObject(new Horse(true,this), 175, 115);  // spawn a horse at the top

                    }
                }
                else if (horseOnScreen==0)// if this there is no horses to start with 
                {
                    getWorld().addObject(new Horse(true,this), 175, 570);  // spawn one at the bottom (better because it protects the base)

                }                
            }
            else if (side == false) // if this side is on the left then,
            {
                if (horseOnScreen == 1)   // the rest of the code follows the same conecpt as the one above (but the spawning is done from the right side of the screen)
                {
                    if (horseOnTop == true)
                    {
                        getWorld().addObject(new Horse(false,this), 915, 570);

                    }
                    else
                    {
                        getWorld().addObject(new Horse(false,this), 915, 115);

                    }
                }
                else if (horseOnScreen==0) 
                {
                    getWorld().addObject(new Horse(false,this), 915, 115); 

                }                        
            } 
            Greenfoot.playSound("horse.mp3");  // create a sound of a horse
            myStorage.setStoragePoint(myStorage.getStoragePoint()-horseSupply);  // decrease the storage supply
            coolDownCheck = true;  // spawning is done- start counting again until you reach the spawning rate
        }
    }

    /**
     * Spawns an elephant beside the base, decreasing the supplies it required.
     */
    private void spawnElephant()
    {
        if (myStorage.getStoragePoint() >= elephantSupply)  // if you have enough storage points to spawn an elephant then,
        {
            if (side == true)  // if this side is on the left
            {
                getWorld().addObject(new Elephant(true,this), 185, 487); // spawn an elephant from the left
            }
            else if (side == false)  // if this side is on the right
            {
                getWorld().addObject(new Elephant(false,this), 905, 487); // spawn an elephant from the right
            }  
            myStorage.setStoragePoint(myStorage.getStoragePoint()-elephantSupply);  // decrease the storage supply
            Greenfoot.playSound("elephant.mp3");  // create a sound of an elephant
            coolDownCheck = true; // spawning is done- start counting again until you reach the spawning rate
        }
    }

    /**
     * Modifies hitPoints amount to a new amount 
     * 
     * @param hitPoints  the new hitPoints amount that the Main base will now have
     */
    public void setHitPoints(int hitPoints)
    {
        this.hitPoints = hitPoints;
    }

    /**
     * Modifies deaths amount to a new amount 
     * 
     * @param deaths  the new deaths amount that the Main base will now record
     */
    public void setDeaths(int deaths)
    {
        this.deaths = deaths;
    }

    /**
     * Modifies damageBareFoot amount to a new amount 
     * 
     * @param damage  the new damageBareFoot amount that the Main base will now record
     */
    public void setDamageBareFoot(int damage)
    {
        damageBareFoot = damage;
    }

    /**
     * Modifies damageHorse amount to a new amount 
     * 
     * @param damage  the new damageHorse amount that the Main base will now record
     */
    public void setDamageHorse(int damage)
    {
        damageHorse = damage;
    }

    /**
     * Modifies damageElephant amount to a new amount 
     * 
     * @param damage  the new damageElephant amount that the Main base will now record
     */
    public void setDamageElephant(int damage)
    {
        damageElephant = damage;
    }

    /**
     * Returns current hitPoints amount 
     *
     * @return int  the current hit point of the main base
     */
    public int getHitPoints()
    {
        return hitPoints;
    }

    /**
     * Returns current deaths amount 
     *
     * @return int  the current deaths amount
     */
    public int getDeaths()
    {
        return deaths;
    }

    /**
     * Returns swan rate amount 
     *
     * @return int  the spawn rate amount
     */
    public int getCoolDownTime()
    {
        return coolDownTime;
    }

    /**
     * Returns current damageBareFoot amount 
     *
     * @return int  the current damageBareFoot amount
     */
    public int getDamageBareFoot()
    {
        return damageBareFoot;
    }

    /**
     * Returns current damageHorse amount 
     *
     * @return int  the current damageHorse amount
     */
    public int getDamageHorse()
    {
        return damageHorse;
    }

    /**
     * Returns current damageElephant amount 
     *
     * @return int  the current damageElephant amount
     */
    public int getDamageElephant()
    {
        return damageElephant;
    }

    /**
     * Returns current number of barefoots from the mainbase's side is on the screen
     *
     * @return int  the current     number of barefoots from the mainbase's side on the screen
     */
    public int getBareFootOnScreen()
    {
        return bareFootOnScreen;
    }
}
