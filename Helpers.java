import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The helpers class adds the soldier or the base to win the war. The helpers is divided into two sub-classes. The medics revive dead soldiers and the scouts
 * collect cargo when the ships throw them to increase storage points.
 * 
 * @author (Nicholas Chan) 
 * @version (0.0.3)
 */
abstract class Helpers extends Units
{
    /**
     * A helper should collect/move towards an object and therefore should have a method
     * that determines which object is closer
     */
    abstract void determineClosestTarget();

    /**
     * A helper should return to base after his mission has been served.
     */
    abstract void returnToBase();
}
