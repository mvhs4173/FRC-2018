package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team4173.robot.subsystems.CoordinateSystem;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.NavX;

/**
 * Run autonomous from the Left robot starting position
 */
public class TestCoordinates extends CommandGroup {
	
	public enum AutonomousTarget {
		SWITCH,
		SCALE,
		AUTO_LINE;
	}
	
	double startCoordinates[] = {0.0, 0.0};
	
	/**
	 * 
	 * @param switchColorSide The side of our team's switch that has our team color on it
	 * @param scaleColorSide The side of the Scale that has our team color on it
	 */
    public TestCoordinates(DriveTrain driveTrain, NavX navX, CoordinateSystem coordinateSystem) {
    	requires(driveTrain);
    	
    	coordinateSystem.setCurrentPosition(startCoordinates);
    	
    	double firstCoordinate[] = {5.0, 0.0};
    	double secondCoordinate[] = {0.0, 5.0};
    	double thirdCoordinate[] = {5.0, 5.0};
    	double fourthCoordinate[] = {10.0, 0.0};
    	
    	double coordinate5[] = {5.0, 0.0};
    	double coordinate6[] = {0.0, -5.0};
    	double coordinate7[] = {5.0, -5.0};
    	double coordinate8[] = {0.0, 0.0};
    	
    	/*addSequential(new MoveToCoordinate(firstCoordinate, MoveToCoordinate.notSpecified, coordinateSystem, driveTrain, navX));
    	addSequential(new MoveToCoordinate(secondCoordinate, MoveToCoordinate.notSpecified, coordinateSystem, driveTrain, navX));
    	addSequential(new MoveToCoordinate(thirdCoordinate, MoveToCoordinate.notSpecified, coordinateSystem, driveTrain, navX));
    	addSequential(new MoveToCoordinate(fourthCoordinate, MoveToCoordinate.notSpecified, coordinateSystem, driveTrain, navX));
    	
    	addSequential(new MoveToCoordinate(coordinate5, MoveToCoordinate.notSpecified, coordinateSystem, driveTrain, navX));
    	addSequential(new MoveToCoordinate(coordinate6, MoveToCoordinate.notSpecified, coordinateSystem, driveTrain, navX));
    	addSequential(new MoveToCoordinate(coordinate7, MoveToCoordinate.notSpecified, coordinateSystem, driveTrain, navX));
    	addSequential(new MoveToCoordinate(coordinate8, 0.0, coordinateSystem, driveTrain, navX));*/
    }    
}