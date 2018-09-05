package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SafetyReleased extends Command {
	boolean isDone = false;
    public SafetyReleased() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	Robot.climbSafetyPressed = false;
    	isDone = true;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
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
