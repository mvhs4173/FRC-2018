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
import org.usfirst.frc.team4173.robot.subsystems.Degrees;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;

/**
 * Makes the robot drive in a straight line and will adjust itself if it goes off-course
 */
public class TurnToAngle extends Command {
	private DriveTrain driveTrain;
	private double desiredHeading;
	private NavX navX;
	private boolean isDone = false;
	
	private double allowableError = 1.0;
	private double pFactor = 200.0;
	private double dFactor = 0.5;
	
	private double lastError;//Y axis
	private double lastTime;//X axis
	
	private int numTimesAtAngle = 0;
	
	/**
	 * Turns the robot to a certain angle in degrees
	 * @param driveTrain The drive train the robot uses
	 * @param navX The gyro
	 * @param desiredAngle The angle you want the robot to turn to in Degrees
	 */
	public TurnToAngle(DriveTrain driveTrain, NavX navX, double desiredAngle) {
		requires(driveTrain);
		this.driveTrain = driveTrain;
		this.navX = navX;
		this.desiredHeading = desiredAngle;
		this.lastError = 0.0;
		this.lastTime = 0.0;
	}
	
	protected void init() {
		double currentHeading = navX.getHeading();
		
		//If the robot is already rotated to the desired angle then just end the command now
		if (Math.abs(Degrees.subtract(currentHeading, desiredHeading)) <= allowableError) {
			isDone = true;
		}
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		pFactor = Robot.prefs.getDouble("Turn P Factor", 200);
		dFactor = Robot.prefs.getDouble("Turn D Factor",  0.5);
		if (!isDone) {
			
			double currentHeading = navX.getHeading();
			double error = Degrees.subtract(desiredHeading, currentHeading);
			boolean errorNegative = error < 0;
			double dNumber = 0.0;
			
			//The derivative part of the PD loop
			dNumber = ((error - lastError)/(Timer.getFPGATimestamp() - lastTime)) * dFactor;
			
			SmartDashboard.putNumber("Derivative",  dNumber);
			SmartDashboard.putNumber("Last Error", lastError);
			SmartDashboard.putNumber("Last Time", lastTime);
			
			double newSpeed = Math.sqrt(Math.abs(error) * pFactor);
			
			if (Math.abs(error) > allowableError) {
				newSpeed = Math.max(newSpeed,  3.5);
			}
			
			if (errorNegative) {
				newSpeed = -newSpeed;
			}
			
			newSpeed += dNumber;//Apply the Derivative part of the PD loop
			
			lastError = error;
			
			SmartDashboard.putNumber("Error",  error);
			
			lastTime = Timer.getFPGATimestamp();
			
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
			
			//If the robot has been facing the correct angle for 15 iterations then stop turning
			if (numTimesAtAngle >= 15) {
				isDone = true;
				driveTrain.stopDrive();
			}
			
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
		SmartDashboard.putBoolean("Finished",  true);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {

	}
}