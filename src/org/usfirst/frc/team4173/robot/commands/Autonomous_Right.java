package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team4173.robot.subsystems.CoordinateSystem;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.ColorSide;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.GameObject;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide;
import org.usfirst.frc.team4173.robot.subsystems.NavX;
import org.usfirst.frc.team4173.robot.subsystems.PowerCubeCollector;
import org.usfirst.frc.team4173.robot.*;

/**
 * Run autonomous from the Left robot starting position
 */
public class Autonomous_Right extends CommandGroup {
	
	
	
	ColorSide switchColorSide = ColorSide.NOT_AVAILABLE;
	ColorSide scaleColorSide = ColorSide.NOT_AVAILABLE;
	
	double startCoordinates[] = {2.5, 5.5};
	
	/**
	 * 
	 * @param switchColorSide The side of our team's switch that has our team color on it
	 * @param scaleColorSide The side of the Scale that has our team color on it
	 */
    @SuppressWarnings("incomplete-switch")
	public Autonomous_Right(Robot.AutonomousTarget autoTarget) {
    	
    	DriveTrain driveTrain = Hardware.driveTrain;
    	PowerCubeCollector cubeCollector = Hardware.cubeCollector;
    	CoordinateSystem coordinateSystem = Robot.coordinateSystem;
    	requires(driveTrain);
    	requires(cubeCollector);
    	requires(Hardware.linearSlide);
    	
    	//Wait until the FMS gives us the side that our switch and scale is on
    	addSequential(new GetGameObjectColorSides());
    	
    	switchColorSide = Robot.allianceSwitchColorSide;
    	scaleColorSide = Robot.scaleColorSide;
    	
    	//Update the auto target
    	autoTarget = Robot.target;
    	
    	coordinateSystem.setCurrentPosition(startCoordinates);
    	
    	double autoLineCoordinates[][] = {{13.5, 3.5, 0.0},//Coordinate 0
    								{23.5, 3.5, 0.0}};//Coordinate 1
    	
    	double leftSwitchCoordinates[][] = {{20.0, 3.5, -90.0},//Coordinate 0
    										{20.0, 24.0, 180.0},//Coordinate 1
    										{14.0, 24.0, 90.0},//Coordinate 2
    										{14.0, 21.5, 90.0}};//Coordinate 3
    	
    	double leftScaleCoordinates[][] = {{20.0, 3.5, -90.0},//Coordinate 0
    										{20.0, 20.0, 0.0},//Coorindate 1
    										{24.0, 20.0, 0.0},//Coordinate 2
    										{20.0, 20.0, 180.0}};//Coordinate 3
    	
    	double rightSwitchCoordinates[][] = {{14.0, 3.5, -90.0},//Coordinate 0
    											{14.0, 6.0, -90.0}};//Coordinate 1
    	
    	double rightScaleCoordinates[][] = {{18.0, 4.0, MoveToCoordinate.notSpecified},//Coordinate 0
    										{21.0, 7.0, 0.0},//Coordinate 1
    										{24.0, 7.0, 0.0},//Coordinate 2
    										{20.0, 7.0, 180.0}};//Coordinate 3
    	
    	if (autoTarget == Robot.AutonomousTarget.AUTO_LINE) {
    		/**If we are just going for the auto line then we will drive
			 * across the auto line and get as close to the scale as we can
			 */
			//Tell the robot to move to the coordinates above
			addSequential(new MoveToCoordinate(autoLineCoordinates[0]));
			addSequential(new MoveToCoordinate(autoLineCoordinates[1]));
    	}
    	
    	//Go for the switch first, this will also get our robot across the auto line in the process
    	if (switchColorSide == ColorSide.LEFT) {
    		switch (autoTarget) {
	    		case SWITCH:
	    			addSequential(new MoveToCoordinate(leftSwitchCoordinates[0]));
	    			addSequential(new MoveToCoordinate(leftSwitchCoordinates[1]));
	    			addSequential(new MoveToCoordinate(leftSwitchCoordinates[2]));
	    			
	    			//Raise the slide
	    			addParallel(new MoveSlide(LinearSlide.slidePosition.SWITCH));
	    			addSequential(new MoveToCoordinate(leftSwitchCoordinates[3]));
		    		
		    		//Eject the power cube from the robot into the switch
		    		addSequential(new EjectPowerCube());
		    		
		    		break;
	    		case SCALE:
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[0]));
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[1]));
	    			
	    			//Move up the slide
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
	    			
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[2]));
	    			
	    			//Eject the cube
	    			addSequential(new EjectPowerCube());
	    			
	    			//Move away from scale and get robot ready for tele-op
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[3]));
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
	    			break;
	    		case TWO_CUBES_AUTO:
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[0]));
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[1]));
	    			
	    			//Move up the slide
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
	    			
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[2]));
	    			
	    			//Eject the cube
	    			addSequential(new EjectPowerCube());
	    			
	    			//Turn around and look for cube to pickup
	    			addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, -179.0));
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
	    			
	    			addSequential(new AutoCubePickup(false));
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.SWITCH));
	    			
	    			addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
	    			addSequential(new EjectPowerCube());
	    			break;
    		}
    	}else if (switchColorSide == ColorSide.RIGHT) {
    		switch(autoTarget) {
    			case SWITCH:
    				addSequential(new MoveToCoordinate(rightSwitchCoordinates[0]));
    				
    				addParallel(new MoveSlide(LinearSlide.slidePosition.SWITCH));
    				
    				addSequential(new MoveToCoordinate(rightSwitchCoordinates[1]));
    				
    				//Eject the power cube
    				addSequential(new EjectPowerCube());
    				break;
    			case SCALE:
    				//Move to the scale on the right side
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[0]));
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[1]));
    				
    				//Raise the slide
    				addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
    				
    				//Move under the scale
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[2]));
    				
    				//Eject power cube
    				addSequential(new EjectPowerCube());
    				
    				//Move away from the scale and lower the slide
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[3]));
    				//Get away from the scale, lower the slide and get the robot ready for teleop
    				addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
    				break;
    			case TWO_CUBES_AUTO:
    				//Move to the scale on the right side
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[0]));
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[1]));
    				
    				//Raise the slide
    				addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
    				
    				//Move under the scale
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[2]));
    				
    				//Eject power cube
    				addSequential(new EjectPowerCube());
    				
    				//Turn around to get ready to scan for a cube
    				addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
    				addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
    				
    				addSequential(new AutoCubePickup(true));
    				addSequential(new MoveSlide(LinearSlide.slidePosition.SWITCH));
    				
    				//Make sure the robot is facing the switch
    				addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
    				addSequential(new EjectPowerCube());
    				break;
    		}
		}
    }    
}