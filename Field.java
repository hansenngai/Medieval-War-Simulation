import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Field class constructs the snipers, mainbases, trees, river and stat display and adds them to the screen.
 * 
 * @author (Ramy Elbakari and Isaac Chang) 
 * @version (0.0.6)
 */
public class Field extends World
{
    //true is left side and team 1
    //false if right side and team 2
    MainBase team1;
    MainBase team2; 
    // Initilizing variables for objects that will be spawned to the world
    StatDisplay display;  // a stat display (at the bottom of the screen)
    River river;  // a river (at the top of the screen)
    // two snipers (one for each side)
    Snipers sniper1;
    Snipers sniper2;
    
    //music
    GreenfootSound music = new GreenfootSound("battle.mp3");

    /**
     * Constructor for objects of class Field. Default start up variables.
     */
    public Field()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(1090, 600, 1,false);
        setPaintOrder(BlackFade.class,StatDisplay.class,HealthIcon.class,StoneWall.class,Tree.class,BareFoot.class,Arrow.class);  // setting the paint order of the world (which actors overlap over which)

        // Creating a instance of the two bases
        team1 = new MainBase(true,100,1,750,0,50);
        team2 = new MainBase(false,100,1,750,0,50);

        // Creating a instance of the display screen and the river
        display = new StatDisplay();
        river = new River();

        // adding all the objects that were initilized on the screen
        addObject(display,545,587);
        addObject(team1,50,378);
        addObject(team2,1040,378);
        addObject(river,545,10);

        // adding four trees on the screen
        addObject (new Tree(), 456, 305);
        addObject (new Tree(), 502, 496);
        addObject (new Tree(), 594, 185);
        addObject (new Tree(), 647, 379);

        // initilizing the two snipers for each base
        sniper1 = new Snipers(true);
        sniper2 = new Snipers(false);
        
        // adding the two snipers on the screen
        addObject(sniper1,150,378);
        addObject(sniper2,1090-150,378);
        sniper2.setRotation(180);  // setting the rotation for the sniper on the right side

    }
    
    /**
     * Constructor for objects of class Field. User must specify specific start-up variables.
     * @param basePoints1- start up base points for team 1 (on the left)
     * @param startBareFoot1- initial number of barefoots to be spawned at the begining of the stimulation for team 1 (on the left)
     * @param spawnTime1- after how many acts should a new barefoot spawn for team 1 (on the left)
     * @param startResearch1- which research level should team 1 (on the left) start with
     * @param startStorage1- how many points should team 1 (on the left) start with
     * @param basePoints2- start up base points for team 2 (on the right)
     * @param startBareFoot2- initial number of barefoots to be spawned at the begining of the stimulation for team 2 (on the right)
     * @param spawnTime2- after how many acts should a new barefoot spawn for team 2 (on the right)
     * @param startResearch2- which research level should team 2 (on the right) start with
     * @param startStorage2- how many points should team 2 (on the right) start with
     * 
     */
    public Field(int basePoints1,int startBareFoot1,int spawnTime1,int startResearch1,int startStorage1,int basePoints2,int startBareFoot2,int spawnTime2,int startResearch2,int startStorage2)
    {    
        // Same concept as the constructor above
        super(1090, 600, 1,false);
        setPaintOrder(BlackFade.class,StatDisplay.class,HealthIcon.class,StoneWall.class,Tree.class,BareFoot.class,Arrow.class);

        // intializing the two bases
        // however this time, the user inputs specifc requirments for start up variables
        team1 = new MainBase(true,basePoints1,startBareFoot1,spawnTime1, startResearch1, startStorage1);
        team2 = new MainBase(false,basePoints2,startBareFoot2,spawnTime2, startResearch2, startStorage2);  
        
        // initializing river and stat display
        display = new StatDisplay();
        river = new River();
        

        addObject(display,545,587);  // adding stat display
        addObject(team1,50,378);   // adding the mainbase for team1 (left side)
        addObject(team2,1040,378);   // adding the mainbase for team2 (right side)
        addObject(river,545,10);   // addding the river

        // adding 4 trees on the screen
        addObject (new Tree(), 456, 305);
        addObject (new Tree(), 502, 496);
        addObject (new Tree(), 594, 185);
        addObject (new Tree(), 647, 379);

        // Initializing two snipers (one sniper for each side)
        sniper1 = new Snipers(true);
        sniper2 = new Snipers(false);

        addObject(sniper1,150,378);  // add the first sniper  (for team on the left)
        addObject(sniper2,1090-150,378);  // add the second sniper  (for team on the right)
        sniper2.setRotation(180);   // rotate the second sniper to appear facing the left side (default: facing the right side)

    }
    
    /**
     * Gets the mainbase for team1 (on the left).
     * @return - the mainbase of the left team
     */
    public MainBase getTeam1()
    {
        return team1;
    }

    /**
     * Gets the mainbase for team2 (on the right).
     * @return - the mainbase of the right team
     */
    public MainBase getTeam2()
    {
        return team2;
    }

    /**
     * Do what the class wants to do when run is pressed
     */
    public void act(){        
        setPaintOrder(StatDisplay.class,HealthIcon.class,StoneWall.class);  // state display is alway on top followed by the health icon then the stonewall
        music.playLoop();  // play music
    }

    /**
     *Plays the background music. Gets called to continue playing the music when it stops by the stopped method
     */
    public void started() {
        music.playLoop();  // play music
    }

    /**
     * Pauses the background music
     */
    public void stopped() {
        music.pause();  // pause music
    }
    
    /**
     * Method to call a new winning world to proclaim the other team won
     * 
     * @param boolean   the side the main base that is calling this method is on
     */
    public void goToWin(boolean side)
    {
         Greenfoot.setWorld (new WinningPage (!side));  // display the winning screen claiming the other side won
         music.stop();  // stop the music when progress to the winning page
    }
}
