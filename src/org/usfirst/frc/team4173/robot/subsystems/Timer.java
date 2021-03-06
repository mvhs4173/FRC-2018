package org.usfirst.frc.team4173.robot.subsystems;

/**
 * Created by ROBOT18 on 11/7/2017.
 */

public class Timer
{
    private double startTime;
    private double seconds;
    private boolean initialized;

    public Timer(){
        initialized = false;
    }
    /**
     *
     * @param seconds how long the timer should run
     */
    public void init(double seconds){
        this.seconds = seconds;
        startTime = (double) System.nanoTime() / 1.0E9;
        initialized = true;
    }

    /**
     *
     * @return true when timer is done
     */
    public boolean isTimerUp(){
        double currentTime = System.nanoTime() / 1.0E9;
        return (!initialized) || (currentTime - startTime >= seconds); //TODO: give error when not initialized
    }

    public void disable(){
        initialized = false;
    }

}
