import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A tree only serves as an obstacle for the soldiers to either walk around or change their direction. Behind every tree, there is an imaginary tree (smaller than the 
 * original tree), if the arrow hits that imaginary tree)then the arrow will get stuck for a few second and disappear (will not pass). Therefore, trees can sometimes
 * save soldiers' lifes. The reason behind imaginary tree is because the arrow sometimes get stuck on the edge of the "box" surrounding the tree (appears as if the arrow
 * stopped in the middle of the air). Thus imaginary tree has its "box" located inside the original tree and nothing seems unusual.
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.4)
 */
public class Tree extends Actor
{

    private boolean addOnce =true;  // a variable used to make sure certain is only run once

    /**
     * Act - do whatever the Tree wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Adds an imaginary tree at the same location as this one.
     */
    public void act() 
    {
        // Add your action code here.
        if (addOnce ==true){  // if the switch is on, then
            getWorld().addObject (new ImaginaryTree(), getX(), getY());  // add an imaginary tree at the same location as this tree
            addOnce =false;  // close the switch (this code will never run again)
        }
        getWorld().setPaintOrder (Ammo.class, Tree.class);  // this tree should overlap the imaginary one

    }    
}
