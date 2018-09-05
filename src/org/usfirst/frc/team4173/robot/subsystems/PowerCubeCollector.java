package org.usfirst.frc.team4173.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public class PowerCubeCollector extends Subsystem {
	
		private MotorController leftMotor;
		private MotorController rightMotor;
		private int intakeMotorSpeedRPS = 4;
		
		public enum CollectorStatus {
			INTAKE,
			EJECT,
			STOPPED;
		}
		
		//A variable that will hold the current state of the system
		private CollectorStatus state;
		
		public void initDefaultCommand() {
			
		}
		
		/**
		 * A class to control the power cube manipulation system
		 * @param leftMotor The left intake motor of the cube collector
		 * @param rightMotor The right intake motor of the cube collector
		 */
		public PowerCubeCollector(MotorController leftMotor, MotorController rightMotor) {
			this.leftMotor = leftMotor;
			this.rightMotor = rightMotor;
			
			//Set the motors to coast, it is not needed for them to brake and will just use up more power trying to brake
			leftMotor.setBrake(NeutralMode.Coast);
			rightMotor.setBrake(NeutralMode.Coast);
			
			//Indicate the current status of the system
			state = CollectorStatus.STOPPED;
		}
		
		/**
		 * Enables the motors of the system so that they will suck in a power cube
		 */
		public void intakeCube() {
			leftMotor.setVelocityRPS(intakeMotorSpeedRPS);
			rightMotor.setVelocityRPS(-intakeMotorSpeedRPS);
			state = CollectorStatus.INTAKE;
		}
		
		/**
		 * Enables the motors of the system so that they will eject a power cube that it is already holding
		 */
		public void ejectCube() {
			leftMotor.setVelocityRPS(-intakeMotorSpeedRPS);
			rightMotor.setVelocityRPS(intakeMotorSpeedRPS);
			state = CollectorStatus.EJECT;
		}
		
		/**
		 * Causes all motors in the system to stop moving
		 */
		public void stopMotors() {
			leftMotor.setVelocityRPS(0);
			rightMotor.setVelocityRPS(0);
			state = CollectorStatus.STOPPED;
		}
		
		/**
		 * Indicates what the system is currently doing
		 * @return The current state of the system 
		 */
		public CollectorStatus getCurrentState() {
			return state;
		}
}