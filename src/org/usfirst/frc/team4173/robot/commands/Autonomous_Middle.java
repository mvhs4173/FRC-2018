package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

import org.usfirst.frc.team4173.robot.subsystems.CoordinateSystem;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.ColorSide;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.GameObject;
import org.usfirst.frc.team4173.robot.subsystems.NavX;
import org.usfirst.frc.team4173.robot.subsystems.PowerCubeCollector;
import org.usfirst.frc.team4173.robot.*;

/**
 * Run autonomous from the Left robot starting position
 */
public class Autonomous_Middle extends CommandGroup {
	
	double startCoordinates[] = {2.5, 11.5};
	ColorSide switchColorSide = ColorSide.NOT_AVAILABLE;
	ColorSide scaleColorSide = ColorSide.NOT_AVAILABLE;
	
	/**
	 * 
	 * @param switchColorSide The side of our team's switch that has our team color on it
	 * @param scaleColorSide The side of the Scale that has our team color on it
	 */
    @SuppressWarnings("incomplete-switch")
	public Autonomous_Middle(Robot.AutonomousTarget autoTarget) {
    	DriveTrain driveTrain = Hardware.driveTrain;
    	PowerCubeCollector cubeCollector = Hardware.cubeCollector;
    	CoordinateSystem coordinateSystem = Robot.coordinateSystem;
    	
    	requires(cubeCollector);
    	requires(driveTrain);
    	requires(Hardware.linearSlide);
    	
    	//Wait until the FMS gives us the side that our switch and scale is on
    	addSequential(new GetGameObjectColorSides());
    	
    	switchColorSide = Robot.allianceSwitchColorSide;
    	scaleColorSide = Robot.scaleColorSide;
    	
    	//Update the auto target
    	autoTarget = Robot.target;
    	
    	//In a coordinate it will be a double array with 3 indexes. The first will be the X coordinate, the second the Y coordinate
    	//and the third will be the rotation in degrees that you want the robot to be rotated at when it is finished moving to the coordinate
    	
    	//List of the coordinates to get to the left switch from middle starting position
    	double leftSwitchCoordinates[][] = {{7.5, 18.5, 0.0},//Coordinate 0
    										{10.5, 18.5, 0.0}};//Coordinate 1
    	
    	//List of coordinates to get to the left scale switch from middle starting position
    	double leftScaleCoordinates[][] = {{5.5, 11.5, MoveToCoordinate.notSpecified},//Coordinate 0
    										{5.5, 23.5, MoveToCoordinate.notSpecified},//Coordinate 1
    										{21.0, 20.0, 0.0},//Coordinate 2
    										{24.0, 20.0, 0.0},//Coordinate 3
    										{20.0, 20.0, 180.0}};//Coordinate 4
    	
    	//List of the coordinates to get to the right switch from middle starting position
    	double rightSwitchCoordinates[][] = {{8.5, 8.5, 0.0},//Coordinate 0
    										 {10.5, 8.5, 0.0}};//Coordinate 1
    	
    	//List of the coordinates to get to the right scale from middle starting position
    	double rightScaleCoordinates[][] = {{9.0, 3.5, MoveToCoordinate.notSpecified},//Coordinate 0
    										{22.0, 7.0, 0.0},//Coordinate 1
    										{24.0, 7.0, 0.0},//Coordinate 2
    										{20.0, 7.0, 180.0}};//Coordinate 3
    	
    	
    	
    	coordinateSystem.setCurrentPosition(startCoordinates);
    	
    	if (autoTarget == Robot.AutonomousTarget.AUTO_LINE) {
    		/**If we are just going for the auto line then we will drive
			 * across the auto line and get as close to the scale as we can
			 */
			double autoLineCoordinate1[] = {9.0, 8.0, MoveToCoordinate.notSpecified};//Positioned so that the robot's bumper will be past the auto-line
			
			//Tell the robot to move to the coordinates above
			addSequential(new MoveToCoordinate(autoLineCoordinate1));
    	}
    	
    	//Go for the switch first, this will also get our robot across the auto line in the process
    	if (switchColorSide == ColorSide.LEFT) {
    		switch (autoTarget) {
	    		case SWITCH:
		    		//Position the robot in front of the left side of switch
		    		addSequential(new MoveToCoordinate(leftSwitchCoordinates[0]));
		    		
		    		addSequential(new MoveSlide(LinearSlide.slidePosition.SWITCH));
		    		
		    		addSequential(new MoveToCoordinate(leftSwitchCoordinates[1]));
		    		
		    		addSequential(new EjectPowerCube());
		    		
		    		break;
	    		case SCALE:
	    			//Move to the scale
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[0]));
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[1]));
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[2]));
	    			
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
	    			
	    			//Move under the scale
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[3]));
	    			//Eject the power cube
	    			addSequential(new EjectPowerCube());
	    			//Move away from scale and get robot ready for tele-op
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[4]));
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
	    			break;
	    		case TWO_CUBES_AUTO:
	    			//If the drivers have chosen to do 2 cubes in autonomous then
	    			//The robot will put its current cube into the scale turn around
	    			//pick up another cube and put it into the switch
	    			
	    			//Drive to the scale
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[0]));
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[1]));
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[2]));
	    			
	    			//Raise slide
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
	    			
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[3]));
	    			
	    			//Eject cube
	    			addSequential(new EjectPowerCube());
	    			
	    			//Turn around
	    			addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, -179.0));
	    			
	    			//Lower slide
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
	    			
	    			addSequential(new AutoCubePickup(false));
	    			
	    			//Raise slide
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.SWITCH));
	    			
	    			//Make sure the robot is facing the switch
	    			addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
	    			
	    			addSequential(new EjectPowerCube());
	    			
	    			break;
    		}
    	}else if (switchColorSide == ColorSide.RIGHT) {
    		switch(autoTarget) {
    			case SWITCH:
		    		addSequential(new MoveToCoordinate(rightSwitchCoordinates[0]));
		    		
		    		addSequential(new MoveSlide(LinearSlide.slidePosition.SWITCH));
		    		
		    		addSequential(new MoveToCoordinate(rightSwitchCoordinates[1]));
    		
		    		break;
    			case SCALE:
    				//move to the scale
		    		addSequential(new MoveToCoordinate(rightScaleCoordinates[0]));
		    		addSequential(new MoveToCoordinate(rightScaleCoordinates[1]));
		    		
		    		//Raise the slide
		    		addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
		    		
		    		//move under scale
		    		addSequential(new MoveToCoordinate(rightScaleCoordinates[2]));
		    		
		    		//Eject cube
		    		addSequential(new EjectPowerCube());
		    		
		    		addSequential(new MoveToCoordinate(rightScaleCoordinates[3]));
		    		
		    		addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
		    		break;
    			case TWO_CUBES_AUTO:
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[0]));
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[1]));
    				
    				//Raise slide
    				addSequential(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
    				
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[2]));
    				
    				//eject cube
    				addSequential(new EjectPowerCube());
    				
    				//Turn around
    				addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
    				
    				//Lower slide
    				addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
    				
    				//Pick up the cube
    				addSequential(new AutoCubePickup(true));
    				
    				//Raise slide
    				addSequential(new MoveSlide(LinearSlide.slidePosition.SWITCH));
    				
    				//Make sure the robot is facing the scale
    				addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
    				
    				//eject cube
    				addSequential(new EjectPowerCube());
    				break;
    		}
		}
    }    
}