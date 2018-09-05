/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;

/**
 * Stops the motors of the drivetrain
 */
public class StopDriveMotors extends Command {
	private DriveTrain driveTrain;
	boolean isDone = false;
	
	public StopDriveMotors(DriveTrain driveTrain) {
		requires(driveTrain);
		this.driveTrain = driveTrain;
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		driveTrain.stopDrive();
		isDone = true;
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return isDone;
	}
}