import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A world to simulate loading and simply saves the values carried over from the Start world
 * to be used in instantiating the new field world for hte simulation.
 * 
 * @author Isaac Chang 
 * @version 0.0.1
 */
public class Loading extends World
{
    //instance variables and objects
    private ICWidget loadBar; // an initilization of the loading widget
    private Author banner;  // text under the widget
    private BlackFade fadeout;  // fade out (screen slowly appears)
    private int numStart;  // variable serves as counter
    private boolean indicator;  // variable to keep track to check when the loading bar finishes
    private GreenfootImage worldImage; // saves the image of the world

    //variables to be carried to the field instance
    private int basePoints1,startBareFoot1, spawnTime1, startResearch1,startStorage1,basePoints2,startBareFoot2,spawnTime2,startResearch2, startStorage2;

    /**
     * Constructor for class Loading.
     * 
     */
    public Loading(int basePoints1,int startBareFoot1,int spawnTime1,int startResearch1,int startStorage1,int basePoints2,int startBareFoot2,int spawnTime2,int startResearch2,int startStorage2)
    {
        super(1090, 600, 1, false); 

        //variables to be moved to new game world     
        this.basePoints1 =basePoints1;
        this.startBareFoot1= startBareFoot1;
        this.spawnTime1= spawnTime1;
        this.startResearch1= startResearch1;
        this.startStorage1 = startStorage1;

        this.basePoints2 =basePoints2;
        this.startBareFoot2= startBareFoot2;
        this.spawnTime2= spawnTime2;
        this.startResearch2= startResearch2;
        this.startStorage2= startStorage2;

        setPaintOrder(BlackFade.class);  // black fade should appear at the top
        worldImage = getBackground();  // save the image of the world
        loadBar = new ICWidget(500,1,"white");  //initialize the loading bar
        banner = new Author("RIHN Productions",30,"white"); //initialize the text under the bar

        addObject(loadBar,545,275);  // add the loading widget on the scree
        addObject(banner,545,500);  // add the text under the loading widget on the screen
        numStart = 0;  // set the counter to 0
        addObject(new BlackFade(true), 545, 300); // add the fade in
        fadeout = new BlackFade(false);  // initilaize the fad out
        indicator = false;  // the loading bar didn't reach a max yet
        
    }

    /**
     * Do whatever the loading class is called to do when the run button is pressed
     */
    public void act()
    {
        checkFull();  // check to see when to put the fade to the world
        numStart++; //increase load bar value
        loadBar.update(numStart);   //updating load bar to simulate loading        
        if (fadeout.getCheckDone() == true)  // checks to see if the loading bar is done, if yes then, 
        {
            goToField();  // go to the war field
        }	
    }

    /**
     * Checks to see if load bar is fully loaded and if so make the 
     * the black fade start fading.
     */
    private void checkFull()
    {
        if (loadBar.getCurrEnergy() == 500)  // if the loading bar reaches a max
        {
            if (indicator == false)  // only add the fade once
            {
                addObject(fadeout, 545, 300);  // add th fade to the world
                indicator = true;  // don't add the fade again
                
                
            }            
        }   
    }

    /**
     * Called to instantiate a new Field world to run the simulation with all the saved values
     * that user inputted
     */
    private void goToField()
    {
        Greenfoot.setWorld(new Field(basePoints1,startBareFoot1, spawnTime1, startResearch1,startStorage1,basePoints2,startBareFoot2,spawnTime2,startResearch2, startStorage2));
    }
}
