package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.subsystems.PowerCubeCollector;

/**
 * Stops the cube collector motors
 */
public class StopCubeCollector extends Command {
		private PowerCubeCollector cubeCollector;
		boolean isDone = false;
		
		public StopCubeCollector() {
			requires(Hardware.cubeCollector);
			this.cubeCollector = Hardware.cubeCollector;
		}

		// Called repeatedly when this Command is scheduled to run
		@Override
		protected void execute() {
			cubeCollector.stopMotors();
			isDone = true;
		}

		// Make this return true when this Command no longer needs to run execute()
		@Override
		protected boolean isFinished() {
			return isDone;
		}
}
