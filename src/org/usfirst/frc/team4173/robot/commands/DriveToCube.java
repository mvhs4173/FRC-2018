package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.Hardware;
import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.CubeDetector;
import org.usfirst.frc.team4173.robot.subsystems.DriveTrain;
import org.usfirst.frc.team4173.robot.subsystems.NavX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class DriveToCube extends Command {
	
	DriveTrain driveTrain;
	NavX navX;
	CubeDetector cubeDetector;
	
	private double pFactor = 0.1;//0.1
	private double dFactor = 0;
	
	double desiredScreenArea = 45.0;
	double allowableError = 5.0;
	
	boolean isDone = false;
	boolean finishedDriving = false;
	
	double lastError = 0.0;
	double lastTime = 0.0;
	
	double startHeading;
	

    public DriveToCube() {
        driveTrain = Hardware.driveTrain;
        requires(driveTrain);
        navX = Hardware.navX;
        cubeDetector = Robot.cubeDetector;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//Make sure the robot is lined up with the cube
    	if (!cubeDetector.cubeDetected()) {
    		isDone = true;
    	}
    	startHeading = -cubeDetector.getHorizontalOffset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double screenArea = cubeDetector.getTargetArea();
    	double error = desiredScreenArea - screenArea;
    	
    	if ((error <= allowableError && !isDone) || !cubeDetector.cubeDetected()) {
    		isDone = true;
    		driveTrain.driveUnitAtFPS(0.0);
    	}
    	
    	SmartDashboard.putBoolean("Driving Finished",  isDone);
    	
    	if (!isDone) {
    		double pNumber = error * pFactor;
    		double dNumber = ((error - lastError)/(Timer.getFPGATimestamp() - lastTime)) * dFactor;
    		
    		double desiredSpeed = pNumber + dNumber;
    		
    		//Once we have desired speed give it to the straight line calculator so the robot will drive straight
    		double motorSpeeds[] = driveTrain.calculateMotorSpeedsForStraightLine(desiredSpeed,  startHeading,  driveTrain.getLeftUnitFPS(), driveTrain.getRightUnitFPS(), -cubeDetector.getHorizontalOffset());
    		
    		driveTrain.driveLeftUnitAtFPS(motorSpeeds[0]);
    		driveTrain.driveRightUnitAtFPS(motorSpeeds[1]);
    	}
    	
    	lastError = error;
    	lastTime = Timer.getFPGATimestamp();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	isDone = true;
    }
}