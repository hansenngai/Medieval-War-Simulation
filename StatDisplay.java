import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
/**
 * The Stat Display displays the current statistics of the stimulation ranging from and including base hit points, soldiers on screen, spawn rate of soldiers, research
 * points, storage points. It also displays the damage done by foot soldiers, horses and elephants along with the current research level and total deaths. All those
 * statistics are displayed to reflect how each base is doing in the war. The stat diplay is located at the bottom of the stimulation and could be clicked to show
 * all those variables, otherwise it will remain as a small strip at the bottom of the screen to leave room for the actual stimulation.
 * 
 * @author Isaac Chang 
 * @version 0.0.1
 */
public class StatDisplay extends Actor
{
    //instance variables
    private int displayLength = 400;  // length of the big stat display screen
    private int displayWidth = 300;  // width of the big stat display screen

    private int barWidth = 25;  // width of the small stat display screen (has the same lenght as the big one)

    private int leftSideX = 10; // x-coordinate of the left side of the labels on the big stat screen
    private int rightSideX = (displayLength/2)+10;   // x-coordinate of the right side of the labels on the big stat screen

    private boolean open = false; //whether or not the stat window is open (big screen) - true - is open, false - not open
    private boolean change = false;  // controls when to switch between the two modes (big and small)- true-switch, false-don't switch

    private GreenfootImage display;  // greenfoot image that will display the big stat display screen
    private GreenfootImage bar;  // greenfoot image that will display the small stat display screen 

    /**
     * Contructor for the Stat display. Draws the image for the minimized bar only
     */
    public StatDisplay()
    {
        //creating minimized bar image as initial image        
        bar = new GreenfootImage(displayLength,barWidth);  // initializing the image that will store the minimized bar image
        bar.setColor(Color.BLACK);  // set the colour to black
        bar.drawRect(0,0,displayLength,barWidth);   // draw out a rectangle     
        bar.fillRect(0,0,displayLength,barWidth);   // fill it with black 
        bar.setColor(Color.WHITE);   // set the colour to white
        bar.drawRect(0,0,displayLength-1,barWidth-1);  // draw a white outline around it
        bar.drawString("Statistics",(displayLength/2)-20,barWidth/2+5);  // add the label "statistics"
        bar.setTransparency(160);  // make it have some transparency

        display = new GreenfootImage(displayLength,displayWidth);  // initializing the image that will be used to store display the big stat display screen
        setImage(bar); //setting image first as the miniimized bar
    }

    /**
     * Updates display with values taken from both bases.
     */
    private void updateDisplay()
    {
        //getting each base
        Field game = (Field) getWorld();  // variable that will store the current world (will be used to access public methods from the world)                  
        MainBase team1Base = game.getTeam1();  // store the base on the left
        MainBase team2Base = game.getTeam2();  // store the base on the right

        //drawing display with updated values on it
        display.clear();  // clear the previous image

        // draw the big stat display screen
        display.setColor(Color.WHITE);
        display.drawRect(0,0,displayLength,displayWidth);
        display.setColor(Color.BLACK);
        display.fillRect(0,0,displayLength,displayWidth);       
        display.setColor(Color.WHITE);
        display.drawRect(0,0,displayLength-1,displayWidth-1);
        display.drawString("Team 1",((displayLength/2)/2)-20,20);
        display.drawString("Team 2",((displayLength/2)/2)*3-20,20);

        //Team 1 stats (draw labels and the current values of the team on the greenfoot image)
        display.drawString("Base Hit Points: "+team1Base.hitPoints,10,50);
        display.drawString("Soldiers On Screen: "+team1Base.getBareFootOnScreen(),10,70);
        display.drawString("Spawn Rate (in acts): "+team1Base.getCoolDownTime(),10,90);
        display.drawString("Research Points: "+team1Base.myResearch.getResearchPoints(),10,110);
        display.drawString("Storage Points: "+team1Base.myStorage.getStoragePoint(),10,130);

        display.drawString("Damage Done",((displayLength/2)/2)-40,160);
        display.drawString("Foot-Soldier: "+team1Base.getDamageBareFoot(),10,180);
        display.drawString("Horse: "+team1Base.getDamageHorse(),10,200);
        display.drawString("Elephant: "+team1Base.getDamageElephant(),10,220);
        display.drawString("Deaths: "+team1Base.getDeaths(),10,250);
        display.drawString("Research Level: "+team1Base.myResearch.getResearchLevel(),10,270);


        //Team 2 stats  (draw labels and the current values of the team on the greenfoot image)
        display.drawString("Base Hit Points: "+team2Base.hitPoints,rightSideX,50);
        display.drawString("Soldiers On Screen: "+team2Base.getBareFootOnScreen(),rightSideX,70);
        display.drawString("Spawn Rate (in acts): "+team2Base.getCoolDownTime(),rightSideX,90);
        display.drawString("Research Points: "+team2Base.myResearch.getResearchPoints(),rightSideX,110);
        display.drawString("Storage Points: "+team2Base.myStorage.getStoragePoint(),rightSideX,130);

        display.drawString("Damage Done",displayLength/2+((displayLength/2)/2)-40,160);
        display.drawString("Foot-Soldier: "+team2Base.getDamageBareFoot(),rightSideX,180);
        display.drawString("Horse: "+team2Base.getDamageHorse(),rightSideX,200);
        display.drawString("Elephant: "+team2Base.getDamageElephant(),rightSideX,220);
        display.drawString("Deaths: "+team2Base.getDeaths(),rightSideX,250);
        display.drawString("Research Level: "+team2Base.myResearch.getResearchLevel(),rightSideX,270);

        //only set transparency after the fading effect is done
        if (change == false)
        {
            display.setTransparency(180);  // set back transparency (fading caused it to be 0)
        }

    }

    /**
     * Checks to see if this object is clicked and if so, initiate the transformation to expanded
     * or minimzied window
     */
    private void checkClicked()
    {
        if (Greenfoot.mouseClicked(this) == true)  // if clicked
        {
            change = true;   // change the picture to either big or small (depending on what it was before)
        }        
    }

    /**
     * Act - do whatever the StatDisplay wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Displays the stat display depending on 
     */
    public void act() 
    {
        //minimzing or maximizing and setting transparency
        if (change == true) // if the user clicked the the stat display image them
        {
            getImage().setTransparency(getImage().getTransparency()-20);   // decrease transparency (small fading)
            if (open == false && getImage().getTransparency() == 0)  // if the image was the small stat display and fading is complete then, 
            {               
                setImage(display); // set the image of the stat display to show the big version         
                setLocation(getX(),getY()-(displayWidth/2)+(barWidth/2));  // sets its location on the screen (different for each version because of diff sizes)
                open = true;  // the stat display is showing the big stat display screen
                change = false;  // don't run this code again (until the user clicks on the image again)
            }
            else if (open == true && getImage().getTransparency() == 0)  // if the image was the big stat display and fading is complete then, 
            {
                bar.setTransparency(160); // set back the transparency
                setImage(bar);   // set the image of the stat display to show the small version          
                setLocation(getX(),getY()+(displayWidth/2)-(barWidth/2));   // sets its location on the screen (different for each version because of diff sizes)
                open = false;  // the stat display is showing the small stat display screen
                change = false;  // don't run this code again (until the user clicks on the image again)                
            }
        }
        checkClicked();  // check if the user clicked the bar or not
        updateDisplay();  // updates the display stat with current values (in the big screen)
        getWorld().setPaintOrder (StatDisplay.class);  // the stat display should appear over everything
    }    
}
