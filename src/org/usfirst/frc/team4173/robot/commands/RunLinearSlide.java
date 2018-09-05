package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.OI;
import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class RunLinearSlide extends Command {
	LinearSlide slide = Robot.linearSlide;
    public RunLinearSlide() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.linearSlide);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    public void execute() {
    	slide.runAtPercentPower(OI.joy.getThrottle());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
