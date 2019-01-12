/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4173.robot;

import org.usfirst.frc.team4173.robot.commands.AutoCubePickup;
import org.usfirst.frc.team4173.robot.commands.Autonomous_Left;
import org.usfirst.frc.team4173.robot.commands.Autonomous_Middle;
import org.usfirst.frc.team4173.robot.commands.Autonomous_Right;
import org.usfirst.frc.team4173.robot.commands.DriveToDistance;
import org.usfirst.frc.team4173.robot.commands.TurnToAngle;
import org.usfirst.frc.team4173.robot.subsystems.CoordinateSystem;
import org.usfirst.frc.team4173.robot.subsystems.CubeDetector;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.ColorSide;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.GameObject;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide.slidePosition;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	public static boolean climbSafetyPressed = false;
	
	public static enum AutonomousTarget {
		SWITCH,
		SCALE,
		AUTO_LINE,
		TWO_CUBES_AUTO;
	}
	
	public static ColorSide allianceSwitchColorSide;
	public static ColorSide scaleColorSide;
	
	public static OI oi;

	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();
	SendableChooser<AutonomousTarget> autoTargrt = new SendableChooser<>();
	public static AutonomousTarget target;
	
	public static Preferences prefs;
	public static GameDataRetriever dataRetriever;
	
	public static Hardware hardware;
	
	public static CoordinateSystem coordinateSystem;
	Autonomous_Left leftAutonomous;
	public static CubeDetector cubeDetector;
	
	public static slidePosition current;
	double startingCoordinate[] = {0.0, 0.0};
	boolean autoStarted = false;
	double start = 0;
	
	
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		prefs = Preferences.getInstance();
		hardware = new Hardware();
		
		autoTargrt.setDefaultOption("Switch", AutonomousTarget.SWITCH);
		autoTargrt.addOption("Scale", AutonomousTarget.SCALE);
		autoTargrt.addOption("Line", AutonomousTarget.AUTO_LINE);
		autoTargrt.addOption("Two Cubes",  AutonomousTarget.TWO_CUBES_AUTO);
		
		cubeDetector = new CubeDetector();
		cubeDetector.ledEnabled(false);
		SmartDashboard.putData("Auto Target", autoTargrt);
		
		target = autoTargrt.getSelected();
		
		//Set up the data retriever so we will know what side our color is on
		dataRetriever = new GameDataRetriever();
		
		// chooser.addOption("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", m_chooser);		
		
		coordinateSystem = new CoordinateSystem(startingCoordinate);
		
		//Init the cube collector
		
		oi = new OI();
		
		CameraServer.getInstance().startAutomaticCapture();
		
		m_chooser.addOption("Left", new Autonomous_Left(AutonomousTarget.AUTO_LINE));
		m_chooser.setDefaultOption("Middle", new Autonomous_Middle(AutonomousTarget.AUTO_LINE));
		m_chooser.addOption("Right", new Autonomous_Right(AutonomousTarget.AUTO_LINE));
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		cubeDetector.ledEnabled(false);
		target = autoTargrt.getSelected();
		m_autonomousCommand = m_chooser.getSelected();
		
		Hardware.driveTrain.leftUnit.zeroEncoder();
		Hardware.driveTrain.rightUnit.zeroEncoder();
		
		Hardware.driveTrain.zeroUnitEncoders(); 
		Hardware.navX.setOrigin();
		DriveToDistance drive = new DriveToDistance(Hardware.driveTrain, Hardware.navX, 0.0, prefs.getDouble("Desired Distance",  5.0));
		TurnToAngle turn = new TurnToAngle(Hardware.driveTrain, Hardware.navX, prefs.getDouble("Desired Angle", 90.0));
		AutoCubePickup cubePickup = new AutoCubePickup(true);
		//drive.start();
		//moveSlideToMid.start();
		//retractIntake.start();
		//extendIntake.start();
		//testCourse.start();
		//angleTurnTest(45.0);
		//driveDistance.start();
		//turn.start();
		//(prefs.getDouble("Desired Angle",  90.0));
		
		
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */
		
		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			//m_autonomousCommand.start();
		}
		start = Timer.getFPGATimestamp();
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		cubeDetector.ledEnabled(false);

		ColorSide switchSide = dataRetriever.getGameObjectColorSide(GameObject.ALLIANCE_SWITCH);
		ColorSide scaleSide = dataRetriever.getGameObjectColorSide(GameObject.SCALE);
		ColorSide enemySwitchSide = dataRetriever.getGameObjectColorSide(GameObject.ENEMY_SWITCH);
		
		double distanceTraveled = (Math.abs(Hardware.driveTrain.leftUnit.getFeet()) + Math.abs(Hardware.driveTrain.rightUnit.getFeet()))/2;
		
		if (distanceTraveled <= 12) {
			Hardware.driveTrain.leftUnit.setVelocityFPS(4.5);
			Hardware.driveTrain.rightUnit.setVelocityFPS(4.5);
		}else {
			Hardware.driveTrain.driveUnitAtFPS(0.0);
		}
		
		//angleTurnTest(prefs.getDouble("Desired Angle",  90.0));
		
		
		SmartDashboard.putNumber("Slide Position",  Hardware.linearSlide.getMotorPosition());
		SmartDashboard.putString("Switch Side", switchSide.toString());
		SmartDashboard.putString("Scale Side",  scaleSide.toString());
		SmartDashboard.putNumber("Robot Heading",  Hardware.navX.getHeading());
		
		SmartDashboard.putNumber("Left Unit Speed RPM",  Hardware.driveTrain.leftUnit.getVelocityRPM());
		SmartDashboard.putNumber("Right Unit Speed RPM",  Hardware.driveTrain.rightUnit.getVelocityRPM());
		
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		cubeDetector.ledEnabled(false);
		Hardware.linearSlide.resetEncoder();
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}
	
	//Run this in teleopIni to test the turn to degrees command
	public void angleTurnTest(double angle) {
		TurnToAngle turnToAngle = new TurnToAngle(Hardware.driveTrain, Hardware.navX, angle);
		turnToAngle.start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		//cubeDetector.ledEnabled(false);
		//cubeDetector.enableDriverCamera();
		double currentHeading = Hardware.navX.getHeading();
		double leftSpeedRPM = Hardware.leftDriveUnit.getVelocityFPS();
		double rightSpeedRPM = Hardware.rightDriveUnit.getVelocityFPS();
		
		//Hardware.driveTrain.driveWithJoystick(-OI.joy.getX(), -OI.joy.getY(), OI.joy.getThrottle());
				
		SmartDashboard.putNumber("Slide Position",  Hardware.linearSlide.getMotorPosition());
		SmartDashboard.putNumber("Right Wheel Speed FPS",  rightSpeedRPM);
		SmartDashboard.putNumber("Left Wheel Speed FPS",  leftSpeedRPM);
		SmartDashboard.putNumber("Robot Heading", currentHeading);
		
		SmartDashboard.putNumber("Left Front Current Draw",  Hardware.leftFrontMotor.getAmps());
		SmartDashboard.putNumber("Right Front Current Draw",  Hardware.rightFrontMotor.getAmps());
		SmartDashboard.putNumber("Left Back Current Draw",  Hardware.leftBackMotor.getAmps());
		SmartDashboard.putNumber("Right Back Current Draw",  Hardware.rightBackMotor.getAmps());
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		
	}
}