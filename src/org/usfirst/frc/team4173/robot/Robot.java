/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4173.robot;

import org.usfirst.frc.team4173.robot.commands.*;
import org.usfirst.frc.team4173.robot.subsystems.*;
import org.usfirst.frc.team4173.robot.subsystems.DriveUnit.UnitSide;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide.slidePosition;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.TimedRobot;
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
@SuppressWarnings("unused")
public class Robot extends TimedRobot {
	//public static final ExampleSubsystem kExampleSubsystem
	//		= new ExampleSubsystem();
	public static OI m_oi;
	public static Preferences prefs = Preferences.getInstance();
	static DriveUnit rightUnit = new DriveUnit(RobotMap.FRONT_RIGHT_WHEEL, RobotMap.REAR_RIGHT_WHEEL, UnitSide.RIGHT);
	static DriveUnit leftUnit = new DriveUnit(RobotMap.FRONT_LEFT_WHEEL, RobotMap.REAR_LEFT_WHEEL, UnitSide.LEFT);
	public static DriveTrain drivetrain = new DriveTrain(rightUnit, leftUnit);
	public static LinearSlide linearSlide = new LinearSlide(RobotMap.motor1, RobotMap.DIO0, RobotMap.DIO1, RobotMap.DIO2, RobotMap.DIO3, RobotMap.DIO4);
	public static LinearSlide.slidePosition current;
	REVDigitBoard digitBoard = new REVDigitBoard();
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	double pot = 0;
	
	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		prefs = Preferences.getInstance();
		m_oi = new OI();
		//m_chooser.addDefault("Default Auto", new ExampleCommand());
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", m_chooser);
		current = slidePosition.FLOOR;
		
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
		m_autonomousCommand = m_chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		digitBoard.clear();
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		//comm1.execute();
		
		pot = digitBoard.getPot();
		digitBoard.display(prefs.getString("Message", "Data"));
		SmartDashboard.putNumber("encoder", drivetrain.rightUnit.getEncoderPosition());
		SmartDashboard.putNumber("power", linearSlide.getCurrent());
		SmartDashboard.putNumber("pot", pot);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
