import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The research facility keeps track of research points and research level. When a new research level is reached, a new type of soldier/troop is unlocked. 
 * There are 4 research levels; 1, 2, 3, 4. Reached at 0, 15, 45, and 70 research points respectively. Research level is the main factor in determining 
 * what will be spawned!
 * 
 * @author Hansen Ngai
 * @version 0.0.6
 * 
 * 
 */
public class Research extends Buildings
{
    private int researchLevel; // Research Level  
    private int researchPoints;// Amount of research points
    private int deathCount;// counts amount of deaths
    private int initialPoint; //initial amount of points

    //protected boolean side in superclass    
    /**
     * Creates a research facility for an indicated side.
     * 
     * @param inputSide Indicates which side the research building is for
     */
    public Research(boolean inputSide){
        researchLevel = 1; // Start off as research level 1
        researchPoints = 0; // Start off with no research points
        deathCount = 0; // Start off with no deaths
        this.side = inputSide; // User inputted side
        this.hitPoints = 1000; //can be changed based on main base (Ramy)
    }

    /**
     * Creates a research facility for an indicated side with a predetermined starting research points.
     * 
     * @param inputSide Indicates which side the research building is for.
     * @param startResearchPoints Indicates the value of research points the army starts off with.
     */
    public Research (boolean inputSide, int startResearchPoints){
        researchLevel = 1; // Start off as research level 1
        this.researchPoints = startResearchPoints;// User inputted amount of research points at start
        this.initialPoint = startResearchPoints;// User inputted amount of initial research points
        deathCount = 0; // Start off with no deaths
        this.side = inputSide; // User inputted side 
        this.hitPoints = 1000; //can be changed based main base (Ramy). Set to 80 in main base.
    }

    /**
     * Checks the research points to determine what research level the army is at. 
     */
    private void checkLevel(){
        if (researchPoints >= 50){
            researchLevel = 4; //Research level is 4 if there is more of greater than 50 research points
        }
        else if (researchPoints >= 30){
            researchLevel = 3;//Research level is 3 if there are more or greater than 30 but less than 50 research points
        }
        else if (researchPoints >= 15){
            researchLevel = 2;// Research level is 2 if there are more or greater than 15 but less than 30 research points
        }
        else {
            researchLevel = 1;//Research level is 1 if there are less than 15 research points
        }
    }

    /**
     * Increases research points as a result of the death. (Will usually be as a result of a death)
     * 
     * @param points The Amount of research points to be set.
     */
    public void setResearchPoints(int points)
    {
        researchPoints = points; // Sets research points
    }
    
    /**
     * Gets intital research point
     * 
     * @return int  The initial research points.
     */
    public int getInitialPoint()
    {
        return initialPoint; //returns initial research points
    }
    
    /**
     * Gets the current value of research points.
     * 
     * @return int The current amount of research points.
     */
    public int getResearchPoints(){
        return researchPoints;// Returns the current value of research points
    }

    /**
     * Gets the current research level.
     * 
     * @return int The current research level.
     */
    public int getResearchLevel(){
        return researchLevel; // Returns the current research level
    }

    public void act()
    {
        checkLevel();  // Constantly checks what research level it is at currently.
    }
}
