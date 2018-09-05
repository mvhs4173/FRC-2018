package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.subsystems.BreakBeamSensor;
import org.usfirst.frc.team4173.robot.subsystems.PowerCubeCollector;

/**
 * Collects a power cube
 */
public class CollectPowerCube extends Command {
		private PowerCubeCollector cubeCollector;
		boolean isDone = false;
		private BreakBeamSensor cubeCollectedSensor;
		double collectStartTime = 0;
		boolean cubeIn = false;
		
		
		public CollectPowerCube() {
			requires(Hardware.cubeCollector);
			this.cubeCollector = Hardware.cubeCollector;
			cubeCollectedSensor = Hardware.cubeCollectedSensor;
		}

		// Called repeatedly when this Command is scheduled to run
		@Override
		protected void execute() {
			cubeCollector.intakeCube();
		}

		// Make this return true when this Command no longer needs to run execute()
		@Override
		protected boolean isFinished() {
			return false;
		}
		
		protected void end() {
		}
}
