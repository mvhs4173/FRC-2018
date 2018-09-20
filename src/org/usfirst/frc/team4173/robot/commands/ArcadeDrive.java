package org.usfirst.frc.team4173.robot.commands;


import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.RobotMap;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.DriveUnit;
import org.usfirst.frc.team4173.robot.subsystems.DriveUnit.UnitSide;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
@SuppressWarnings("unused")
public class ArcadeDrive extends Command {
	DriveTrain drivetrain = Hardware.driveTrain;
	
    public ArcadeDrive() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Hardware.driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	drivetrain.rightUnit.setVelocityRPM(Robot.prefs.getDouble("Desired RPM", 60));
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
