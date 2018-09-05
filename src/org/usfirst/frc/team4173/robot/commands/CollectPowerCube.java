package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team4173.robot.subsystems.PowerCubeCollector;

/**
 * Collects a power cube
 */
public class CollectPowerCube extends Command {
		private PowerCubeCollector cubeCollector;
		boolean isDone = false;
		
		public CollectPowerCube(PowerCubeCollector cubeCollector) {
			requires(cubeCollector);
			this.cubeCollector = cubeCollector;
		}

		// Called repeatedly when this Command is scheduled to run
		@Override
		protected void execute() {
			cubeCollector.intakeCube();
			isDone = true;
		}

		// Make this return true when this Command no longer needs to run execute()
		@Override
		protected boolean isFinished() {
			return isDone;
		}
}
