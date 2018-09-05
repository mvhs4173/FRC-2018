package org.usfirst.frc.team4173.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4173.robot.subsystems.CoordinateSystem;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.ColorSide;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.GameObject;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide;
import org.usfirst.frc.team4173.robot.subsystems.NavX;
import org.usfirst.frc.team4173.robot.subsystems.PowerCubeCollector;
import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.Robot;

/**
 * Run autonomous from the Left robot starting position
 */
public class Autonomous_Left extends CommandGroup {
	
	
	int interval = 0;
	ColorSide switchColorSide = ColorSide.NOT_AVAILABLE;
	ColorSide scaleColorSide = ColorSide.NOT_AVAILABLE;
	
	double startCoordinates[] = {2.5, 21.5};
	
	/**
	 * 
	 * @param switchColorSide The side of our team's switch that has our team color on it
	 * @param scaleColorSide The side of the Scale that has our team color on it
	 */
    @SuppressWarnings("incomplete-switch")
	public Autonomous_Left(Robot.AutonomousTarget autoTarget) {
    	DriveTrain driveTrain = Hardware.driveTrain;
    	PowerCubeCollector cubeCollector = Hardware.cubeCollector;
    	CoordinateSystem coordinateSystem = Robot.coordinateSystem;
    	requires(driveTrain);
    	requires(cubeCollector);
    	requires(Hardware.linearSlide);
    	
    	coordinateSystem.setCurrentPosition(startCoordinates);
    	
    	//Wait until the FMS gives us the side that our switch and scale is on
    	addSequential(new GetGameObjectColorSides());
    	
    	switchColorSide = Robot.allianceSwitchColorSide;
    	scaleColorSide = Robot.scaleColorSide;
    	
    	double autoLineCoordinates[][] = {{12.0, 24.0, MoveToCoordinate.notSpecified},//Coordinate 0
    										{23.0, 24.0, 0.0}};//Coordinate 1
    	
    	
    	double leftSwitchCoordinates[][] = {{13.5, 24.0, MoveToCoordinate.notSpecified},//Coordinate 0
    										{13.5, 22.5, 90.0}};//Coordinate 1
    	
    	double leftScaleCoordinates[][] = {{22.0, 25.0, MoveToCoordinate.notSpecified},//Coordinate 0
    										{24.0, 20.0, 0.0},//Coordinate 1
    										{21.0, 20.0, 180.0}};//Coordinate 2
    	
    	double rightSwitchCoordinates[][] = {{19.0, 23.0, MoveToCoordinate.notSpecified},//Coordinate 0
    											{19.0, 4.0, MoveToCoordinate.notSpecified},//Coordinate 1
    											{13.5, 4.0, -90.0},//Coordinate 2
    											{13.5, 5.5, -90.0}};//Coordinate 3
    	
    	double rightScaleCoordinates[][] = {{19.0, 23.0, 90.0},//Coordinate 0
    										{19.0, 7.0, 0.0},//Coordinate 1
    										{24.0, 7.0, 0.0},//Coordinate 2
    										{20.0, 7.0, 180.0}};//Coordinate 3
    	//Update the auto target
    	autoTarget = Robot.target;
    	
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
		    		//Get the linear slide moving into position for putting the power cube into the switch
		    		addParallel(new MoveSlide(LinearSlide.slidePosition.SWITCH));
		    		
		    		//Move the robot so it is on the left side of the switch
		    		addSequential(new MoveToCoordinate(leftSwitchCoordinates[0]));
		    		addSequential(new MoveToCoordinate(leftSwitchCoordinates[1]));
		    		
		    		//Eject the power cube from the robot into the switch
		    		addSequential(new EjectPowerCube());
		    		
		    		break;
	    		case SCALE:
	    			//Move near the scale
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[0]));
	    			
	    			addParallel(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
	    			
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[1]));
	    			
	    			addSequential(new EjectPowerCube());
	    			
	    			//Lower slide and move to new position
	    			addParallel(new MoveSlide(LinearSlide.slidePosition.FLOOR));
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[2]));
	    			break;
	    		case TWO_CUBES_AUTO:
	    			//Put a cube into the scale then turn around, find a cube and put it into the switch
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[0]));
	    			
	    			addParallel(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
	    			
	    			addSequential(new MoveToCoordinate(leftScaleCoordinates[1]));
	    			
	    			addSequential(new EjectPowerCube());
	    			
	    			//Turn around and start scanning for a cube to pickup
	    			addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, -179.0));
	    			addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
	    			
	    			addSequential(new AutoCubePickup(false));
	    			addParallel(new MoveSlide(LinearSlide.slidePosition.SWITCH));
	    			//Make sure the robot is facing the switch
	    			addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
	    			addSequential(new EjectPowerCube());
	    			break;
    		}
    	}else if (switchColorSide == ColorSide.RIGHT) {
    		switch(autoTarget) {
    			case SWITCH:
    				//Move around the outside of the switch to the other side
    				addSequential(new MoveToCoordinate(rightSwitchCoordinates[0]));
    				addSequential(new MoveToCoordinate(rightSwitchCoordinates[1]));
    				addSequential(new MoveToCoordinate(rightSwitchCoordinates[2]));
    				
    				//Move the slide into position
    				addParallel(new MoveSlide(LinearSlide.slidePosition.SWITCH));
    				
    				//Move a little closer to the switch then eject the power cube
    				addSequential(new MoveToCoordinate(rightSwitchCoordinates[3]));
    				addSequential(new EjectPowerCube());
    				break;
    			case SCALE:
    				//Move to the scale on the right side
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[0]));
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[1]));
    				
    				//Raise the scale
    				addParallel(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
    				
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[2]));
    				
    				//Eject the cube
    				addSequential(new EjectPowerCube());
    				
    				//Lower the slide and move the robot away from scale
    				addParallel(new MoveSlide(LinearSlide.slidePosition.FLOOR));
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[3]));
    				break;
    			case TWO_CUBES_AUTO:
    				//Move to the scale on the right side
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[0]));
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[1]));
    				
    				//Raise the scale
    				addParallel(new MoveSlide(LinearSlide.slidePosition.SCALE_HIGH));
    				
    				addSequential(new MoveToCoordinate(rightScaleCoordinates[2]));
    				
    				//Eject the cube
    				addSequential(new EjectPowerCube());
    				
    				//Turn around and start scanning for a cube
    				addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
    				addSequential(new MoveSlide(LinearSlide.slidePosition.FLOOR));
    				
    				addSequential(new AutoCubePickup(true));
    				
    				addSequential(new MoveSlide(LinearSlide.slidePosition.SWITCH));
    				addSequential(new TurnToAngle(Hardware.driveTrain, Hardware.navX, 180.0));
    				addSequential(new EjectPowerCube());
    		}
		}
    }    
}