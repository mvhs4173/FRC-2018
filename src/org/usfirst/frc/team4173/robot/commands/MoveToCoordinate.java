
package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.subsystems.CoordinateSystem;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.NavX;

import edu.wpi.first.wpilibj.command.CommandGroup;
import org.usfirst.frc.team4173.robot.commands.TurnToAngle;
import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.commands.DriveToDistance;

/**
 *
 */
public class MoveToCoordinate extends CommandGroup {
	
	public static final double notSpecified = 404.0; 
	
	/**
	 * Moves the robot to the given coordinate
	 * @param coordinate The coordinate the robot will move to
	 * @param endingAngle The angle that you want the robot to be facing when the command ends, If it doesn't matter what angle it ends at then give it the static variable 'notSpecified'
	 * @param coordinateSystem The coordinate system
	 * @param driveTrain Drivetrain of robot
	 * @param navX The Gyroscope
	 */
    public MoveToCoordinate(double coordinate[]) {
    	DriveTrain driveTrain = Hardware.driveTrain;
    	CoordinateSystem coordinateSystem = Robot.coordinateSystem;
    	NavX navX = Hardware.navX;
    	requires(driveTrain);
    	double angle = 0.0;
    	double distance = 0.0;
    	double endingAngle = coordinate[2];
    	
    	//Get the angle and the distance that the robot needs to travel for it to reach the coordinate
    	double calculatedAngleDistance[] = coordinateSystem.getCoordinateChangeInfo(coordinate);
    	angle = calculatedAngleDistance[0];
    	distance = calculatedAngleDistance[1];
    	
    	addSequential(new TurnToAngle(driveTrain, navX, angle));
    	addSequential(new DriveToDistance(driveTrain, navX, angle, distance));
    	
    	if (endingAngle != 404.0) {
    		addSequential(new TurnToAngle(driveTrain, navX, endingAngle));
    	}
    	
    	coordinateSystem.setCurrentPosition(coordinate);//Tell the coordinate System the robot's new position
    }
}
