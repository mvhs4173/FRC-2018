package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;

import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.subsystems.BreakBeamSensor;
import org.usfirst.frc.team4173.robot.subsystems.PowerCubeCollector;

/**
 * Ejects a power cube
 */
public class EjectPowerCube extends Command {
		private PowerCubeCollector cubeCollector;
		BreakBeamSensor cubeCollectedSensor;
		boolean isDone = false;
		private double cubeLeftTime;
		boolean timerStarted;
		
		public EjectPowerCube() {
			this.cubeCollector = Hardware.cubeCollector;
			cubeCollectedSensor = Hardware.cubeCollectedSensor;
			requires(cubeCollector);
		}
		
		protected void init() {
		}
		
		// Called repeatedly when this Command is scheduled to run
		@Override
		protected void execute() {
			cubeCollector.ejectCube();
		}

		// Make this return true when this Command no longer needs to run execute()
		@Override
		protected boolean isFinished() {
			return isDone;
		}
		
		protected void end() {
		}
}
