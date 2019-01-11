/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team4173.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.usfirst.frc.team4173.robot.commands.*;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide.slidePosition;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	public static Joystick joy = new Joystick(0);
	public static Joystick launchpad = new Joystick(1);
	public static Button climbUpButton = new JoystickButton(joy, 5),
	climbDownButton = new JoystickButton(joy, 3),
	activateAutoCubePickup = new JoystickButton(launchpad, 10),
	retractIntakeSystem = new JoystickButton(launchpad, 11),
	stopAutoCubePickup = new JoystickButton(launchpad, 17),
	slideFloor = new JoystickButton(launchpad, 5),
	slideSwitch = new JoystickButton(launchpad, 6),
	slideScaleLow = new JoystickButton(launchpad, 7),
	slideScaleMid = new JoystickButton(launchpad, 9),
	takeInCube = new JoystickButton(launchpad, 1),
	ejectCube = new JoystickButton(launchpad, 2),
	slideUp = new JoystickButton(launchpad, 3),
	slideDown = new JoystickButton(launchpad, 4),
	slideOutHook = new JoystickButton(launchpad, 13);
	
	
	
	public OI () {
	
		climbUpButton.whenPressed(new ClimbUp());
		climbUpButton.whenReleased(new StopClimb());
		
		climbDownButton.whenPressed(new ClimbDown());
		climbDownButton.whenReleased(new StopClimb());
		
		takeInCube.whenPressed(new CollectPowerCube());
		takeInCube.whenReleased(new StopCubeCollector());
		
		ejectCube.whenPressed(new EjectPowerCube());
		ejectCube.whenReleased(new StopCubeCollector());
		
		slideFloor.whenPressed(new MoveSlide(slidePosition.FLOOR));
		slideSwitch.whenPressed(new MoveSlide(slidePosition.SWITCH));
		slideScaleLow.whenPressed(new MoveSlide(slidePosition.SCALE_LOW));
		slideScaleMid.whenPressed(new MoveSlide(slidePosition.SCALE_MID));
		
		slideUp.whenPressed(new MoveSlideUp());
		slideDown.whenPressed(new MoveSlideDown());
		
		slideUp.whenReleased(new StopSlide());
		slideDown.whenReleased(new StopSlide());
		
		activateAutoCubePickup.whenPressed(new AutoCubePickup(true));
		stopAutoCubePickup.whenPressed(new StopAutoCubePickup());
	}
}
