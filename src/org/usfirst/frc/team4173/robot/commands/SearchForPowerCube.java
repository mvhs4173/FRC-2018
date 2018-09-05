package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.CubeDetector;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.NavX;

/**
 *
 */
public class SearchForPowerCube extends Command {

	CubeDetector cubeDetector;
	DriveTrain driveTrain;
	NavX navX;
	boolean isDone = false;
	boolean searchRight = false;
	
    public SearchForPowerCube(boolean searchRight) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	cubeDetector = Robot.cubeDetector;
    	driveTrain = Hardware.driveTrain;
    	navX = Hardware.navX;
    	requires(driveTrain);
    	this.searchRight = searchRight;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	cubeDetector.enableCubeDetection();
    	cubeDetector.ledEnabled(false);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Check if the robot can see the cube
    	boolean cubeSeen = cubeDetector.cubeDetected();
    	
    	if (!cubeSeen && Math.abs(cubeDetector.getHorizontalOffset()) <= 20) {
	    	//If the cube is not seen then just spin in a circle until it does see it
    		if (searchRight) {
    			driveTrain.turnUnitAtFPS(1);
    		}else {
    			driveTrain.turnUnitAtFPS(-1);
    		}
    	}else {
    		//If the cube is seen
    		driveTrain.turnUnitAtFPS(0);
    		isDone = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	isDone = true;
    }
}
