package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.Hardware;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StopAutoCubePickup extends Command {

	boolean isDone = false;
	
    public StopAutoCubePickup() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Hardware.cubeCollector);
    	requires(Hardware.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Hardware.driveTrain.driveUnitAtFPS(0);
    	Hardware.cubeCollector.stopMotors();
    	isDone = true;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
    }
}
