import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * An arrow is displayed if a barefoot, sniper, horse or elephant starts shooting. The arrow will appear bigger for the sniper. An arrow will always travel
 * in the direction it was shot. If the arrow hits a tree/soldier, it will get stuck for a few seconds before disappearing. If the arrow hits the edge of 
 * the world, it will disappear. An arrow is the fastest object on the screen.
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.3)
 */
public class Arrow extends Actor
{
    private int speed = 7;  // the speed of the arrow is 7 pixels per act
    private Units target;  // a variable used to store the target of the arrow (first stores the unit that shot the arrow then stores its target later)

    // 2 variablses used to delay the diappearance of the arrow from the tree and soldier
    private int delayTree;
    private int delaySoldier;
    
    private boolean stick= false;  // a variable used to check for whether the arrow should stick on the soldier or not
    private boolean imaginary= false;  // a variable used to check whether the arrow will be used for testing of whether a tree exsists in front of the soldier or not
    private boolean side;  // a variable used to store the side of which the arrow belongs (same side as the soldier who shot it)
    private boolean hitSameSoldier=false;  // a variable that checks for friendly fire (whether the arrow hit a soldier of the same side or not)
    private boolean hitOppSoldier=false;   // a variable that checks for whether the arrow hit an opposite soldier or not
    private String shootingOrigin;  // the type of soldier who shot the arrow
    private MainBase base;   // the base from which the soldier (that originated the arrow) comes from
    private MainBase getBase;  // the base at which the soldiers will hit at

    /**
     * Constructs the arrow. Sets the rotation of the arrow. Defines which actor shot the arrow and which
     * base the shooter of the arrow belongs to. The constructor gets called when a barefoot or a vehicle shoots
     * an arrow.
     * @param rotation - the rotation of the arrow
     * @param target - the actor who shot the arrow
     * @param shootingOrigin- the name of the actor that shot the arrow (e,g. "barefoot", "elephant", etc.)
     * @param base- the base the shooter of the arrow belongs to.
     */
    public Arrow(int rotation, Units target, String shootingOrigin, MainBase base){

        this.setRotation (rotation);  // rotation becomes the rotation of the unit that shot the arrow
        this.target = target;  // the target becomes the unit that shot the arrow
        this.getImage().scale(30,7);  // decrease the size of the arrow when the soldiers or vehicles shoot it (only sniper it is big)
        this.shootingOrigin = shootingOrigin;  // the type of unit that shot the arrow
        this.base =base;  // the base in which the soldier who shot the arrow belonged to

    }

    /**
     * Constructs the arrow. Sets the rotation of the arrow and determine whether the arrow is for shooting
     * or not. The constructor gets called when the sniper shoots the arrow or when an actor wants to test for friendly fire/tree in the way.
     * @param rotation- the rotation of the arrow
     * @param imaginary- true- the arrow should be tested for friendly fire/tree, false- the arrow should be used for actual shooting by the sniper
     */
    public Arrow (int rotation, boolean imaginary){
        this.setRotation (rotation);  // rotation becomes the rotation of the unit that shot the arrow 
        this.imaginary= imaginary;   // determines whether this arrow should be used for testing or not

        if (imaginary ==true){  // if the arrow was imaginary then,  
            getImage().setTransparency (0);  // set it to be completely transparent (no one should be able to see the testing) 
        }
    }

    /**
     * Checks if the arrow hits a tree or a soldier from the same side as the one who shot the arrow.
     * @return true - hits a tree or a soldier from the same side before hitting a soldier from the opposite side, false- hits the soldier of the other side first
     * @param target- the target of the arrow (the soldier from the other side)
     */
    public boolean inWay  (Units target){
        this.target = target;  // the target becomes the target the unit was aiming at

        while(true){  // keep looping until one of the conditions is met
            move (10);   // move by 10
            hitSameSoldier();  // check whether it hits a soldier from the same side
            if (hitOppSoldier==true){  // if it didn't (but instead hit a soldier from the other side), then 
                return false;  // return false (nothing is in the way)
            } 

            if (checkHitTree() ||hitSameSoldier==true){  // if it hits a tree or a soldier from the same side, then
                break; // get out of the loop (will return true soon after)

            }
            if (atWorldEdge()){  // if arrow reaches the edge of the world (didn't hit anything at all)
                return false;     // return false (nothing is in the way)           
            }
        }
        return true;  // return true (when you break out of the loop

    }

    /**
     * Detemines the side of the soldier that the arrow hit first.
     * Gets called by the inWay method.
     */
    private void hitSameSoldier(){
        if (getOneObjectAtOffset(-5,-5,Units.class) != null && (target != getOneObjectAtOffset(-5,-5,Units.class)|| stick==true)){  // if the arrow hits a unit (who is not the same unit that shot it) or if the arrow is sticking on a soldier then, 

            side = target.getSide();  // the side of this arrow becomes the same side as the unit that shot it
            target = (Units) getOneObjectAtOffset(-5,-5,Units.class);  // the target changes to the unit that the arrow hit
            if (side == target.getSide()){  // if they are the same side, then
                hitSameSoldier =true;   // friendly fire has occurred
            } else{   // if not, then
                hitOppSoldier = true;  // the arrow successfully hit the opposite side
            }
        }

    }

    /**
     * Act - do whatever the LandMine wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     * Controls how the arrow will act if it hits different objects.
     */
    public void act() 
    {
        getWorld().setPaintOrder(BareFoot.class);  // the soldier should always appear on top of the arrow
        
        if (imaginary == false){  // if the arrow is for testing, then do not execute the act method (but instead inWay method will be used)
            if (stick==true){  // if the arrow is sticking on the soldier then, 
                delaySoldier++;  // increase the delay counter

                if (delaySoldier>=60 || target.getLives() ==0){  // if the delay counter reached 60 or the target that the arrow is sticking on dies, then
                    getWorld().removeObject (this);  // remove the arrow from the screen
                }
                if (target.getLives()>0){   // if the soldier still lives then,
                    if (target.getWorld() != null)
                    {
                        setLocation (target.getX(), target.getY());  // this arrow will follow it everywhere
                    }                    
                }
            } else if (checkHitTree()){  // else if the arrow hits the tree, then
                delayTree++;  // increase the delay counter of the tree by one
                if (delayTree >=100){   // if the counter reaches 100 then,
                    getWorld().removeObject (this);  // remove it from the screen

                }
            } else if (atWorldEdge()){  // else if the arrow hits the edge of the world, then
                getWorld().removeObject (this);   // remove that arrow from the screen
            } else if (hitTarget()){   // else if the arrow hits a unit, then

                if (shootingOrigin=="barefoot"){   // if the unit that shot it was a barefoot, then
                    base.setDamageBareFoot(base.getDamageBareFoot()+1);  // increase the damage done by barefoot in your mainbase (by 1)
                } else if (shootingOrigin=="horse"){  // if the unit that shot it was a horse, then
                    base.setDamageHorse (base.getDamageHorse()+1);   // increase the damage done by horse in your mainbase (by 5)
                } else if (shootingOrigin =="elephant"){  // if the unit that shot it was an elephant, then
                    base.setDamageElephant (base.getDamageElephant()+2);   // increase the damage done by elephant in your mainbase (by 2)
                }
                target.loseALife();     //unit loses a life if hit by an arrow
                target.checkLives(true);   // update the health icon of the unit
                if (target.getLives()==0){   // if the unit's life reaches 0, then
                    getWorld().removeObject (target);   // remove that unit
                    getWorld().removeObject (this);    // remove this arrow
                } else{  // else if the unit still has lives, then
                    stick=true;  // make the arrow stick on it for a few seconds
                }
            }else if (hitBuildings()){   // else if the arrow hits the opponent's mainbase then,
                getBase.setHitPoints(getBase.getHitPoints()-1);  // decrease the opponent's mainbase's points by 1
                getWorld().removeObject (this);   // remove this arrow from the screen

            } else{   // if the arrow doesn't hit anything, then
                move (speed);  // keep moving
            }  
        }


    }

    /**
     * Checks if the arrow hit a soldier or a vehicle.
     * @return true- hit a soldier or a vehicle, false- didn't hit any of them
     */
    private boolean hitTarget(){
        if (getOneObjectAtOffset(-5,-5,BareFoot.class) != null && (target != getOneObjectAtOffset(-5,-5,BareFoot.class)|| stick==true)){  // if the arrow hits a soldier (who is not the same unit that shot it) or if the arrow is sticking on a soldier then, 

            target = (BareFoot) getOneObjectAtOffset(-5,-5,BareFoot.class);  // the target becomes the soldier that the arrow just hit
            if (target.checkSpawnMovement()){  // if the soldier was undergoing a spawn movement then,
                return false;  // return false (didn't hit anything), the soldier is still making its way to the battlefield
            }
            return true;  // else return true
        } else if ((getOneObjectAtOffset(0,0,Vehicles.class) != null && (target != getOneObjectAtOffset(0,0,Vehicles.class)|| stick==true))){  // if the arrow hits a vehicle (who is not the same unit that shot it) or if the arrow is sticking on the vehicle then,
            target = (Vehicles) getOneObjectAtOffset(0,0,Vehicles.class);  // the target becomes the vehicle that the arrow just hit
            return true;  // return true (arrow hit a vehicle)
        }
        return false;   // else return false
    }

    /**
     * Checks to see if the arrow hit a tree
     * @return true- the arrow hit a tree, false- didn't hit a tree
     */
    private boolean checkHitTree(){
        if (getOneObjectAtOffset(0,0,ImaginaryTree.class) != null){  // if there is a tree in front of the arrow, then
            getWorld().setPaintOrder(ImaginaryTree.class, Arrow.class);  // the tree should appear in front of the arrow
            return true;  // return true (there is a tree)
        }
        return false;  // otherwise return false
    }

    /**
     * Checks to see if the arrow is at the edge of the world.
     * @return true - it is, false- it is not
     */
    private boolean atWorldEdge (){
        if (getWorld().getWidth() <= getX() +1 || getX() - 1 <= 0 || getWorld().getHeight() <= getY() + 1 || getY() - 1 <= 0){  // if the arrow reaches the edge of the world, then
            return true;  // return true
        } else {
            return false;  // otherwise, return false
        }

    }

    /**
     * Checks to see if the arrow hit the opponent's mainbase.
     * @return true- hits the mainbase, false- didn't hit
     */
    private boolean hitBuildings(){
        getBase = (MainBase) getOneObjectAtOffset(0,0,MainBase.class);  // scan for a base in front of the arrow
        if (getBase != null){    // if there is a base in front of the arrow then,
            return true;  // return true (hit base)
        }
        return false;  // otherwise return false (didn't hit base)
    }
}
