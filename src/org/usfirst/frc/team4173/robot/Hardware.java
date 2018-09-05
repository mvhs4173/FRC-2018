package org.usfirst.frc.team4173.robot;

import org.usfirst.frc.team4173.robot.subsystems.*;
import org.usfirst.frc.team4173.robot.subsystems.DriveUnit.UnitSide;

public class Hardware {
	public static DriveTrain driveTrain;
	public static MotorController rightFrontMotor;
	public static MotorController rightBackMotor;
	public static MotorController leftFrontMotor;
	public static MotorController leftBackMotor;
	public static MotorController leftIntake;
	public static MotorController rightIntake;
	public static Climber climber;
	
	public static DriveUnit leftDriveUnit;
	public static DriveUnit rightDriveUnit;
	public static LinearSlide linearSlide;
	public static HorizontalSlide horizontalSlide;
	public static MagneticLimitSwitch slideLowSwitch;
	public static BreakBeamSensor cubeCollectedSensor;
	
	public static MotorController climbMotor;
	
	public static NavX navX;
	
	public static PowerCubeCollector cubeCollector;
	
	public Hardware() {
		//NOTE: The front motor should be the motor controller that has the encoder attached to it
		//Init the NavX (Gyroscope)
		navX = new NavX();
		
		climbMotor = new MotorController(RobotMap.CLIMB_MOTOR);
		climber = new Climber(climbMotor);
		slideLowSwitch = new MagneticLimitSwitch(RobotMap.SLIDE_LOW_SWITCH);
		
		rightBackMotor = new MotorController(RobotMap.REAR_RIGHT_WHEEL);
		rightFrontMotor = new MotorController(RobotMap.FRONT_RIGHT_WHEEL);
		leftBackMotor = new MotorController(RobotMap.REAR_LEFT_WHEEL);
		leftFrontMotor = new MotorController(RobotMap.FRONT_LEFT_WHEEL);
		
		rightFrontMotor.setMaxAccelerationTime(0.0);
		leftFrontMotor.setMaxAccelerationTime(0.0);
		
		//Power cube collector motors
		leftIntake = new MotorController(RobotMap.COLLECTOR_LEFT_INTAKE);
		rightIntake = new MotorController(RobotMap.COLLECTOR_RIGHT_INTAKE);
		
		//Drive units, one for each side of robot
		leftDriveUnit = new DriveUnit(leftFrontMotor, leftBackMotor, UnitSide.LEFT);
		rightDriveUnit = new DriveUnit(rightFrontMotor, rightBackMotor, UnitSide.RIGHT);
		
		//Linear Slide
		linearSlide = new LinearSlide(RobotMap.SLIDE_MOTOR);
		horizontalSlide = new HorizontalSlide(RobotMap.HORIZONTAL_SLIDE_MOTOR);
		
		driveTrain = new DriveTrain(rightDriveUnit, leftDriveUnit);
		cubeCollector = new PowerCubeCollector(leftIntake, rightIntake);
		
		cubeCollectedSensor = new BreakBeamSensor(1);
	}
}
