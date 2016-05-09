import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * The Start page of the simulation.
 * Allows the user to input the values they want in the simulation.
 * 
 * @author Isaac Chang
 * @version 0.0.1
 */
public class Start extends World
{
    // variables that will be carried over to the loading page and then to the field page
    private TextField basePoints1,startBareFoot1, spawnTime1, startResearch1,startStorage1,basePoints2,startBareFoot2,spawnTime2,startResearch2, startStorage2;
    
    private int start = 200;  // y-coordinate of the first textfield on the field
    private int spread = 85;  // the distance between each textbox (vertically) 
    private StartButton startButton;  // start button
    private Author title;  // a variable that will store the image of the title of the stimulation (located at the very top)

    //music
    GreenfootSound music = new GreenfootSound("start.mp3");

    /**
     * Constructor for objects of class Start.
     * 
     */
    public Start()
    {    
        super(1090, 600, 1,false);

        startButton = new StartButton();  // initializing a start button
        
        title = new Author("title",20); // initilaizing author variable that will display the title of the stimulation       
        title.setImage("Title.png");  // setting the picture for the title of the stimulation (the one located at the very top)
        
        // initilaizing the textfields for the left side
        basePoints1= new TextField (200,30,Color.GRAY,Color.BLACK,"80");
        startBareFoot1= new TextField (200,30,Color.GRAY,Color.BLACK,"2");
        spawnTime1= new TextField (200,30,Color.GRAY,Color.BLACK,"750");
        startResearch1= new TextField (200,30,Color.GRAY,Color.BLACK,"0");       
        startStorage1= new TextField (200,30,Color.GRAY,Color.BLACK,"50");

        // initilaizing the textfields for the right side
        basePoints2= new TextField (200,30,Color.GRAY,Color.BLACK,"80");
        startBareFoot2= new TextField (200,30,Color.GRAY,Color.BLACK,"2");
        spawnTime2= new TextField (200,30,Color.GRAY,Color.BLACK,"750");
        startResearch2= new TextField (200,30,Color.GRAY,Color.BLACK,"0");       
        startStorage2= new TextField (200,30,Color.GRAY,Color.BLACK,"50");

        // adding the textfields to the world for the left side
        addObject (startButton,1090/2,400);
        addObject(basePoints1,175,start);
        addObject(startBareFoot1,175,start+spread);
        addObject(spawnTime1,175,start+spread*2);
        addObject(startResearch1,175,start+spread*3);
        addObject(startStorage1,175,start+spread*4);

        // adding labels to the world for the left side
        addObject(new Author("Team 1",35,"white"),175,start-75);
        addObject(new Author("Base Health Point",20,"white"),175,start-30);
        addObject(new Author("Initial Soldiers on Field",20,"white"),175,start+spread-30);
        addObject(new Author("Spawn Rate (in runs)",20,"white"),175,start+spread*2-30);
        addObject(new Author("Initial Research Points",20,"white"),175,start+spread*3-30);
        addObject(new Author("Initial Storage Points",20,"white"),175,start+spread*4-30);

        // adding the textfields to the world for the right side
        addObject(new Author("Team 2",35,"white"),1090-175,start-75);
        addObject(basePoints2,1090-175,start);
        addObject(startBareFoot2,1090-175,start+spread);
        addObject(spawnTime2,1090-175,start+spread*2);
        addObject(startResearch2,1090-175,start+spread*3);
        addObject(startStorage2,1090-175,start+spread*4);

        // adding labels to the world for the right side
        addObject(new Author("Base Health Point",20,"white"),1090-175,start-30);
        addObject(new Author("Initial Soldiers on Field",20,"white"),1090-175,start+spread-30);
        addObject(new Author("Spawn Rate (in runs)",20,"white"),1090-175,start+spread*2-30);
        addObject(new Author("Initial Research Points",20,"white"),1090-175,start+spread*3-30);
        addObject(new Author("Initial Storage Points",20,"white"),1090-175,start+spread*4-30);

        addObject(new Author("Makers: Isaac, Ramy, Hansen, Nicholas",27,"black"),1090/2,220);  // add labels for authors of the stimulation
        addObject(new Author("Pierre Elliott Trudeau 12 Cohen ",27,"white"),1090/2,500);   // add labels of the school and teacher
        addObject(title,1090/2,100);  // add picture of the title of the stimulation

    }
    
    /**
     * Instantiate a loading world with the valeus inputted by the user
     */
    public void goToLoading()
    {
        music.stop();  // the music will stop once the user exits this start up page
        // Sets a new world -loading page
        Greenfoot.setWorld(new Loading(Integer.parseInt(basePoints1.getText()),Integer.parseInt(startBareFoot1.getText()),Integer.parseInt(spawnTime1.getText()),Integer.parseInt(startResearch1.getText()),Integer.parseInt(startStorage1.getText()),Integer.parseInt(basePoints2.getText()),Integer.parseInt(startBareFoot2.getText()),Integer.parseInt(spawnTime2.getText()),Integer.parseInt(startResearch2.getText()),Integer.parseInt(startStorage2.getText())));
    }

    /**
     * Plays the background music
     */
    public void act()
    {
        music.playLoop();  // play music
    }

    /**
     * Plays the background music. Gets called to continue playing the music when it stops by the stopped method
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
}
