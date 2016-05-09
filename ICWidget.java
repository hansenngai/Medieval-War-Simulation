import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
/**
 * ICWidget is a Greenfoot Actor that which can be used in a loading scenario or
 * character property scenario. It displays data which can be inputted by the user through 
 * an implemented update method. The maximum value, current value, 
 * and color of the value bar can be specified, however, the default color of the value bar is yellow.
 * <p>
 * It is designed to work in a scenario larger than 600x400 pixels and displays
 * a percentage String value the same color as the energy bar that specifies 
 * the value being displayed, all which is updated through two update method, one which 
 * mutates the valyue and another which mutates the color of the energy display.
 * Because of its size, this class is not meant to be used for multiple players on one screen.
 * Thus there is no method which enables it to "follow" a specified actor.
 * 
 * @author Isaac Chang 
 * @version Version 0.0.3
 */
public class ICWidget extends Actor
{
    //instance variabes
    private GreenfootImage pole;
    private GreenfootImage lightBar;
    private int maxEnergy = 100;
    private int currEnergy = 0;

    private int transparency = 255;
    private boolean transparentCheck = true;

    private final int OVAL_X = 107;
    private final int OVAL_Y = 10;
    private final int OVAL_WIDTH = 30;
    private final int OVAL_HEIGHT = 30;

    private final int BAR_WIDTH = 250;
    private final int BAR_HEIGHT = 347;

    private final int LOAD_BAR_X = 20;
    private final int LOAD_BAR_Y = 35;
    private final int LOAD_BAR_WIDTH = 190;
    private final int LOAD_BAR_HEIGHT = 308;

    private final int WORD_X = 40;
    private final int WORD_Y = 100;
    //currEnergy will always be less than maxEnergy so this is the percent of Height
    //taken away
    private double currEnergyPercent = 1;

    private int blackBarSize;

    private Color iColor1 = Color.YELLOW;
    private Color iBlack = Color.BLACK;

    /**
     * Creates an empty energy bar with a max value of 100 points
     * and a color of yellow
     * 
     */
    public ICWidget()
    {
        //only here to instantiate the images and fonts
        //which can be easily used and implemented in the other constructors

        //creating image to hold imported image of pole
        pole = new GreenfootImage("poleNew3.png");
        //creating imageto hold the image the rest of the drawn sections of the energy bar
        lightBar = new GreenfootImage(BAR_WIDTH, BAR_HEIGHT);
        //setting font for energy bar percentage display
        lightBar.setFont(new Font("Phosphate", Font.PLAIN, 20));

        //to draw and set image of energy bar as image of object
        //uses the default values, will only really come in use if user is instantiating
        //an empty energy bar with default settings
        setEnergy();
    }    
    /**
     * Creates a completely filled energy bar where 
     * the max value is specified by the user and the color of the bar is yellow.
     * Expects maximum energy.
     * 
     * @param inMaxEnergy   Maximum amount of energy of the energy bar
     * 
     */
    public ICWidget(int inMaxEnergy)
    {
        //calling first constructor to instantize images
        this();   
        //setting max energy to max energy value inputted by user
        maxEnergy = inMaxEnergy;
        //updating the image of the energy bar to the full amount
        //this cannot be done after the currEnergy is also set as inMaxEnergy
        //because then the update method wont run because max energy would be the 
        //same as currnt energy and it would not pass the if statement in the first 
        //update method
        update(maxEnergy);
        //setting current energy as max energy
        currEnergy = inMaxEnergy; 
    }

    /**
     * Creates a completely filled energy bar where 
     * the max value and color of the energy bar is specified by the user.
     * Specifications on color is under the update(String color) method documentation.
     * Expects maximum energy and display color.
     * 
     * @param inMaxEnergy   Maximum amount of energy of the energy bar
     * @param inColor       Color of the energy bar
     * 
     */
    public ICWidget(int inMaxEnergy, String inColor)
    {
        //same as above 
        this();   
        //setting color of energy bar
        update(inColor);
        maxEnergy = inMaxEnergy;
        update(inMaxEnergy);
        currEnergy = inMaxEnergy; 
    }

    /**
     * Creates an energy bar where the current and max value of the bar
     * is specified by the user and the color of the bar is yellow.
     * Expects maximum energy, current energy.
     * 
     * @param inMaxEnergy   Maximum amount of energy on the energy bar 
     * @param inCurrEnergy  Current amount of energy on the energy bar
     * 
     */
    public ICWidget(int inMaxEnergy, int inCurrEnergy)
    { 
        //same as above except instead of updating the bar to display the 
        //full amount, update it to show the current energy amount specified
        //by user input
        this();
        maxEnergy = inMaxEnergy;
        update(inCurrEnergy);
        //setting current energy as current energy input by user
        currEnergy = inCurrEnergy;
    }

    /**
     * Creates an energy bar where the current and max value of the bar, 
     * as well as the color of the energy bar is specified by the user.
     * Expects maximum energy, current energy and display color.
     * Specifications on color is under the update(String color) method documentation.
     * 
     * @param inMaxEnergy   Maximum amount of energy of the energy bar
     * @param inCurrEnergy  Current amount of energy on the energy bar
     * @param inColor       Color of the energy bar
     * 
     */
    public ICWidget(int inMaxEnergy, int inCurrEnergy, String inColor)
    { 
        //same as above
        this();
        //setting color of energy bar
        update(inColor);
        maxEnergy = inMaxEnergy;
        update(inCurrEnergy);
        currEnergy = inCurrEnergy; 
    }

    /**
     * Checks to see if newly inputted current energy is different from current energy.
     * Expects new current Energy.
     * 
     * @param newCurrEnergy Newly inputted value to be diaplyed by the bar
     * @return boolean      True if Energy has changed, false if energy has not changed
     * 
     */
    public boolean update(int newCurrEnergy)
    {
        //So the method will only run if the current energy is changing 
        //save processing power
        if (newCurrEnergy != currEnergy)
        {
            if (newCurrEnergy >= maxEnergy)
            {
                //set current energy to the max energy so that if newCurrEnergy is too
                //high it is still set back to max
                currEnergy  = maxEnergy;
                //call setFullEnergy() method to set image to full energy
                setFullEnergy();
                //return false to show energy as not changed 
                return false;
            }
            else if (newCurrEnergy < maxEnergy && newCurrEnergy >= 0) 
            {
                //set current energy as current energy inputed by user
                currEnergy = newCurrEnergy;    
                //call setEnergy() method to set image to specified current energy level
                setEnergy();
                //return true to show wnergy has changed
                return true;
            }
        }
        //return fale because current energy did not change
        return false;
    }

    /**
     * Sets the current energy bar color to the one
     * specified by the user. Expects String inputs for color.
     * Includes yellow, blue,white, red, green, and gray.
     * Energy bar needs to update to see change in color.
     * 
     * @param color     Color of the energy bar
     * 
     */
    public void update(String color)
    {
        //sets drawing color based on user specifications 
        if (color.equals("yellow"))
        {
            iColor1 = Color.YELLOW;
        }
        else if (color.equals("blue"))
        {
            iColor1 = Color.BLUE;
        }
        else if (color.equals("white"))
        {
            iColor1 = Color.WHITE;
        }
        else if (color.equals("red"))
        {
            iColor1 = Color.RED;
        }
        else if (color.equals("green"))
        {
            iColor1 = Color.GREEN;
        }
        else if (color.equals("gray"))
        {
            iColor1 = Color.GRAY;
        }
    }

    /**
     * Sets the "transparent effect"(bar becomes lighter in color as it becomes filled)
     * on and off. Set true for effect on, set false for effect off.
     * 
     * @param transpCheck   Indicator for whether transparent effect is on or off
     * 
     */
    public void setTransparentCheck(boolean transpCheck)
    {
        transparentCheck  = transpCheck;
    }

    /**
     * Returns current energy value of energy bar.
     * 
     * @return int  Current amount of energy on the energy bar
     * 
     */
    public int getCurrEnergy()
    {
        return currEnergy;
    }

    /**
     * Returns maximum energy value of energy bar.
     * 
     * @return int  Maximum amount of energy on the energy baar
     * 
     */
    public int getMaxEnergy()
    {
        return maxEnergy;
    }

    //private method used to draw image to full energy
    private void setFullEnergy()
    {
        currEnergyPercent = 1; // set to 1 for full display
        double display = currEnergyPercent*100; //set double to calculate percentage
        int currEnergyPercentDisplay = (int) display;
        lightBar.clear(); // clear image
        lightBar.setColor(iColor1); //set specified color
        // rectangle
        lightBar.drawRect(LOAD_BAR_X,LOAD_BAR_Y,LOAD_BAR_WIDTH,LOAD_BAR_HEIGHT);
        lightBar.fillRect(LOAD_BAR_X,LOAD_BAR_Y,LOAD_BAR_WIDTH,LOAD_BAR_HEIGHT);
        lightBar.drawImage(pole,0,0);
        lightBar.setColor(iColor1);
        lightBar.drawString(""+currEnergyPercentDisplay+"%", WORD_X, WORD_Y);
        lightBar.drawOval(OVAL_X,OVAL_Y,OVAL_WIDTH,OVAL_HEIGHT);
        lightBar.fillOval(OVAL_X,OVAL_Y,OVAL_WIDTH,OVAL_HEIGHT);
        lightBar.setTransparency(215);
        this.setImage(lightBar); 
    }

    //private image used to draw the updated version of the 
    //energy bar whenever it is updated
    private void setEnergy()
    {
        // same as above 
        double num = maxEnergy;
        currEnergyPercent = (currEnergy/num);
        double display = currEnergyPercent*100;
        int currEnergyPercentDisplay = (int) display;
        lightBar.clear();
        int process = (int) (currEnergyPercent*LOAD_BAR_HEIGHT);
        blackBarSize =  LOAD_BAR_HEIGHT-process;
        lightBar.setColor(iColor1);
        lightBar.drawRect(LOAD_BAR_X,LOAD_BAR_Y,LOAD_BAR_WIDTH,LOAD_BAR_HEIGHT);
        lightBar.fillRect(LOAD_BAR_X,LOAD_BAR_Y,LOAD_BAR_WIDTH,LOAD_BAR_HEIGHT);
        lightBar.setColor(iBlack);
        lightBar.fillRect(LOAD_BAR_X,LOAD_BAR_Y,LOAD_BAR_WIDTH,blackBarSize);        
        lightBar.drawImage(pole,0,0);
        lightBar.setColor(iColor1);
        lightBar.drawString(""+currEnergyPercentDisplay+"%", WORD_X, WORD_Y);
        if (transparentCheck == true)
        {
            double num2 = currEnergyPercent*40;
            int num3 = (int) num2;
            lightBar.setTransparency(transparency - num3);
        }
        double num2 = currEnergyPercent*30;
        double num3 = 30 - num2;
        int num4 = (int) num3;
        lightBar.drawOval(OVAL_X+num4/2,OVAL_Y+num4/2,OVAL_WIDTH-num4,OVAL_HEIGHT-num4);
        lightBar.fillOval(OVAL_X+num4/2,OVAL_Y+num4/2,OVAL_WIDTH-num4,OVAL_HEIGHT-num4);
        this.setImage(lightBar);
    }

    /**
     * Act - do whatever the ICWidget wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
}
