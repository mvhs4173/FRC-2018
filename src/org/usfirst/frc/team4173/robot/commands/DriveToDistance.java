/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4173.robot.subsystems.NavX;
import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;

/**
 * Makes the robot drive in a straight line and will adjust itself if it goes off-course
 */
public class DriveToDistance extends Command {
	private DriveTrain driveTrain;
	private double desiredHeading;
	private NavX navX;
	private double feetToTravel;
	private boolean isDone = false;
	private double allowableError = 1/12;
	private double pFactor = 0.1;
	private double dFactor = 0.5;
	double maxSpeed = 1.0;//ft/s
	boolean encodersReset = false;
	int atDistance = 0;
	
	private double lastDistanceLeft = -1.0;
	private double lastTime = -1.0;
	
	/**
	 * Will drive in a straight line until another command using the DriveTrain subsystem is called
	 * @param driveTrain The driveTrain subsystem 
	 * @param navX the gyro sensor that will get headings
	 * @param desiredDriveHeading The direction the robot will drive in degrees
	 * @param Number of feet for the robot to travel
	 */
	public DriveToDistance(DriveTrain driveTrain, NavX navX, double desiredDriveHeading, double feet) {
		requires(driveTrain);
		this.driveTrain = driveTrain;
		desiredHeading = desiredDriveHeading;
		this.navX = navX;
		this.feetToTravel = feet;
	}
	
	double signedSqrt(double x) {
    	return Math.signum(x) * Math.sqrt(Math.abs(x));
    }

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		pFactor = Robot.prefs.getDouble("Distance P Factor",  0.1);
		dFactor = Robot.prefs.getDouble("Distance D Factor",  0.1);
		
		if (driveTrain.leftUnit.getEncoderPosition() != 0 && !encodersReset) {
			driveTrain.zeroUnitEncoders();
			
			if (driveTrain.leftUnit.getEncoderPosition() == 0) {
				encodersReset = true;
			}
		}else if (driveTrain.leftUnit.getEncoderPosition() == 0) {
			encodersReset = true;
		}
		
		if (encodersReset && isDone == false) {
			//The distance the robot has currently traveled
			double distanceTraveled = (Math.abs(driveTrain.getRightDistanceFeet()) + Math.abs(driveTrain.getLeftDistanceFeet()))/2;
			double distanceLeft = feetToTravel - distanceTraveled;
			
			double speed = distanceLeft * pFactor;//Proportional part of loop
			
			//Derivative part of loop
			double dNumber = ((distanceLeft - lastDistanceLeft)/(Timer.getFPGATimestamp() - lastTime)) * dFactor;
			
			speed += dNumber;//Apply the derivative
			
			//Gather the data from the sensors we need to calculate motor speeds for a straight line
			double currentLeftSpeed = driveTrain.leftUnit.getVelocityFPS();
			double currentRightSpeed = driveTrain.rightUnit.getVelocityFPS();
			double currentHeading = navX.getHeading();
			
			//Calculate the new speeds for the drive motors to maintain a straight line
			double[] newMotorSpeeds = driveTrain.calculateMotorSpeedsForStraightLine(speed,  desiredHeading,  currentLeftSpeed, currentRightSpeed, currentHeading);
			
			SmartDashboard.putNumber("Distance Traveled (Feet)", distanceTraveled);
			//Apply the new speeds to the motors
			
			//Max speed handler
			boolean speed0Negative = newMotorSpeeds[0] < 0;
			boolean speed1Negative = newMotorSpeeds[1] < 0;
			
			newMotorSpeeds[0] = Math.min(Math.abs(newMotorSpeeds[0]), maxSpeed);
			newMotorSpeeds[1] = Math.min(Math.abs(newMotorSpeeds[1]), maxSpeed);
			
			if (speed0Negative) {
				newMotorSpeeds[0] = -newMotorSpeeds[0];
			}
			
			if (speed1Negative) {
				newMotorSpeeds[1] = - newMotorSpeeds[1];
			}
			
			driveTrain.driveLeftUnitAtFPS(0.2);
			driveTrain.driveRightUnitAtFPS(0.2);
			
			SmartDashboard.putNumber("LeftMotorSpeed FPS", driveTrain.leftUnit.getVelocityFPS());
			SmartDashboard.putNumber("RightMotorSpeed FPS",  driveTrain.rightUnit.getVelocityFPS());
			
			
			
			if (distanceLeft <= allowableError) {
				driveTrain.stopDrive();
				isDone = true;
			}
			
			lastDistanceLeft = distanceLeft;
			lastTime = Timer.getFPGATimestamp();
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
		driveTrain.stopDrive();
	}
}
