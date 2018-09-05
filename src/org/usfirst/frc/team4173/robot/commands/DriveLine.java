/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4173.robot.subsystems.NavX;

import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;

/**
 * Makes the robot drive in a straight line and will adjust itself if it goes off-course
 */
public class DriveLine extends Command {
	private DriveTrain driveTrain;
	private double desiredHeading;
	private NavX navX;
	private double speed;
	private boolean isDone = false;
	
	/**
	 * Will drive in a straight line until another command using the DriveTrain subsystem is called
	 * @param driveTrain The driveTrain subsystem 
	 * @param navX the gyro sensor that will get headings
	 * @param desiredDriveHeading The direction the robot will drive in degrees
	 * @param speed A number to multiply a calculated speed by to make the robot go faster
	 */
	public DriveLine(DriveTrain driveTrain, NavX navX, double desiredDriveHeading, double speed) {
		requires(driveTrain);
		this.driveTrain = driveTrain;
		desiredHeading = desiredDriveHeading;
		this.navX = navX;
		this.speed = speed;
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		//Gather the data from the sensors we need to calculate motor speeds for a straight line
		double currentLeftSpeedRPM = driveTrain.leftUnit.getVelocityRPM();
		double currentRightSpeedRPM = driveTrain.rightUnit.getVelocityRPM();
		double currentHeading = navX.getHeading();
		
		//Calculate the new speeds for the drive motors to maintain a straight line
		double[] newMotorSpeeds = driveTrain.calculateMotorSpeedsForStraightLine(speed,  desiredHeading,  currentLeftSpeedRPM, currentRightSpeedRPM, currentHeading);
	
		//Apply the new speeds to the motors
		driveTrain.driveLeftUnitAtRPM(newMotorSpeeds[0]);
		driveTrain.driveRightUnitAtRPM(newMotorSpeeds[1]);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return isDone;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		isDone = true;
	}
}
