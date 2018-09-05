/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4173.robot.subsystems.NavX;
import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;

/**
 * Makes the robot drive in a straight line and will adjust itself if it goes off-course
 */
public class TurnToAngle extends Command {
	private DriveTrain driveTrain;
	private double desiredHeading;
	private NavX navX;
	private boolean isDone = false;
	private double allowableError = 0.8;
	private double pFactor = Robot.prefs.getDouble("Turn PFactor",  0.25);
	private int numTimesAtAngle = 0;
	
	
	public TurnToAngle(DriveTrain driveTrain, NavX navX, double desiredAngle) {
		requires(driveTrain);
		this.driveTrain = driveTrain;
		this.navX = navX;
		this.desiredHeading = desiredAngle;
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		pFactor = Robot.prefs.getDouble("Turn PFactor",  0.25);
		double currentHeading = navX.getHeading();
		double error = NavX.subtractAngles(desiredHeading, currentHeading);
		boolean errorNegative = error < 0;
		
		
		double newSpeed = Math.sqrt(Math.abs(error) * pFactor);
		
		if (errorNegative) {
			newSpeed = -newSpeed;
		}
		
		if (Math.abs(error) <= allowableError) {
			numTimesAtAngle++;
		}else {
			numTimesAtAngle = 0;
		}
		
		//Determine which direction to rotate the motors
		if (error > 0) {
			driveTrain.driveRightUnitAtRPM(-newSpeed);
			driveTrain.driveLeftUnitAtRPM(newSpeed);
		}else {
			driveTrain.driveRightUnitAtRPM(-newSpeed);
			driveTrain.driveLeftUnitAtRPM(newSpeed);
		}
		
		//If the robot has been facing the correct angle for 20 iterations then stop turning
		if (numTimesAtAngle >= 30) {
			isDone = true;
			driveTrain.stopDrive();
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return isDone;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		driveTrain.stopDrive();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {

	}
}
