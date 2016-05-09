import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Ship gets called by any base once this base's hit points reaches a low value. The ship must be neutral and delievers the cargo to both bases.
 * 
 * @author (Ramy Elbakari and Isaac Chang) 
 * @version (0.0.6)
 */
public class Ship extends Actor
{
    private int speed;  // variable that controls the speed of the ship

    /**
     * Constructs the ship. 
     * Sets the speed of the ship.
     */
    public Ship() 
    {
        speed = 1;  // set the speed to one

    }    

    /**
     * Contorls the movement of the ship throught out the stimulation.
     */
    public void act()
    {
        dropSupply();  // drop the supply
        move(speed);   // keep moving
        worldEdge();   // remove ship if at world edge
    }
    
    /**
     * Drops the supply on the shore of the river once the ship is close to each base.
     */
    private void dropSupply()
    {
        // the following two selective states are responsible for throwing the cargo to the left base.
        if (getX() == (1090/2/2))  // once the ship is quarter way, throw cargo  (for the left base)
        {
            getWorld().addObject(new Crate(true),getX(),getY());
        } 
        if (getX() == (1090/2/2)+100)  // once the ship is 100 pixels after quarter the distance, throw another cargo (for the left base)
        {
            getWorld().addObject(new Crate(true),getX(),getY());
        } 

        if (getX() == ((1090/2/2)*3)-100)  // once the ship is close to the second base (the one on the right), throw a cargo
        {
            getWorld().addObject(new Crate(false),getX(),getY());
        }
        if (getX() == ((1090/2/2)*3))   // once it travels a bit (100 pixels), throw another cargo (also for the right base)
        {
            getWorld().addObject(new Crate(false),getX(),getY());
        }
    }

    /**
     * Removes the ship if hits the edge of the world.
     */
    private void worldEdge(){
        if (getX() > 1400){  // if the ship passes the edge of the world
            getWorld(). removeObject (this);  // remove it
        }

    }
}
