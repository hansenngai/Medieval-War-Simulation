import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Soldiers class are objects that have the ability to shoot and kill others. It is divided into two sub-classes. The snipers never move away from the base and shoot
 * any soldier from the other side who attempts to attack the base and the barefoot are the fighting combat who go into the battlefield and shoot the opponents's units
 * and attempt to attack the abse.
 * 
 * @author (Ramy Elbakari) 
 * @version (0.0.6)
 */
abstract class Soldiers extends Units
{
    /**
     * A soldier should be able to shoot at a target.
     */
    abstract void shoot();
}
