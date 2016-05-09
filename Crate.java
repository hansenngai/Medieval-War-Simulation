import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The crate serves as a collectable object that increases the storage points of the base. Crate get thrown off the ship and scouts scatter around to collect them.
 * The crate has an animation to it when it gets thrown from the ship to appear 3D. One scout could collect all crates that the ship throws when it comes by.
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
public class Crate extends Actor
{
    private int counter=0;  // a variable used as a counter
    private int size=10;  // a variable used to control the size of the crate
    private boolean increase=true;  // a variable used to indicate whether to increase or decrease the size of the crate
    private boolean changeSize=true;  // a variable used to control whether to change the size of the crate or not  
    private boolean side;   // a variable used to determie which side the crate belongs to
    
   /**
    * Constructs a crate. Sets the size of the crate as well as which side it belongs to.
    * @param side true- left side, false- right side
    */
    public Crate (boolean side){
        this.getImage().scale (10,10);  // scale the image of the crate to a smaller size
        this.side=side;  // set the side to which the crate belongs to.
    }

     /**
     * Act - do whatever the CollectableObjects wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Displays the animation of the crate when it gets thrown out of the ship.
     */
    public void act() 
    {
        
        counter ++;  // increase the counter by 1
        if (changeSize == true){  // if the animation is still going, then
            if (increase==true){  // if the crate's size suppose to increase, then
                size++;  // increase its size by one

            } else {  // else if it is suppose to decrease
                size--;  // decrease its size by one

                if (size ==10){  // if its size reachs 10 again (starting size)
                    changeSize =false;  // stop the animation
                }
            }
            if (counter %3==0){  // every 3 acts, 
                this.setLocation (getX(), getY() +2);  // move the crate 2 pixels down (ship is at the top of the screen throwing crates below it)
            }
        }
        this.getImage().scale (size,size);  // update the size of the crate
        if (size ==80){   // once the crate's size reaches 80, 
            increase =false;  // start decreaseing the size of the crate
        }
    }    
    
    /**
     * Returns which side the crate belongs to.
     * @return true - left side, false- right side
     */
    public boolean getSide(){
        return side;  // return which side the crate belongs to.
    }
}
