package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.*;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide.slidePosition;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveSlide extends Command {
	LinearSlide.slidePosition desiredPosition;
	LinearSlide slide = Hardware.linearSlide;
	boolean isDone = false;
	double slidePfactor = 20.0;
	int allowableError = 200;
	int atPositionCounter = 0;//Counts how many times the slide was in the correct position
	
    public MoveSlide(LinearSlide.slidePosition position) {
    	requires(Hardware.linearSlide);
    	desiredPosition = position;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	slidePfactor = Robot.prefs.getDouble("Slide P Factor",  20.0);
    	if (!isDone) {
	    	int ticks = slide.getMotorPosition();
	    	int desiredTicks = slide.getSlidePositionTicksEquivalent(desiredPosition);
	    	
	    	double error = desiredTicks - ticks;//How far off our target we are
	    	double newMotorSpeed = error * slidePfactor;//New motor speed in RPM
	    	boolean isSpeedNegative = newMotorSpeed < 0;
	    	
	    	newMotorSpeed = Math.sqrt(Math.abs(newMotorSpeed));
	    	
	    	if (isSpeedNegative) {
	    		newMotorSpeed = -newMotorSpeed;
	    	}
	    	
	    	slide.runAtRPM(newMotorSpeed);
	    	SmartDashboard.putNumber("Slide Motor Ticks",  ticks);
	    	
	    	if (Math.abs(error) <= allowableError) {
	    		atPositionCounter++;
	    	}else {
	    		atPositionCounter = 0;
	    	}
	    	
	    	if (atPositionCounter >= 5) {
	    		isDone = true;
	    		slide.stopMotor();
	    	}
	    	
	    	if (desiredPosition == slidePosition.FLOOR && Hardware.slideLowSwitch.isMagnetClose()) {
	    		slide.resetEncoder();
	    	}
	    
	    	SmartDashboard.putNumber("Slide Current",  slide.getCurrent());
	    	
	    	//To prevent the motor from burning out
	    	slide.regulateMotorCurrent();
    	}
    }
    

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isDone;
    }

    // Called once after isFinished returns true
    protected void end() {
    	slide.stopMotor();
    	isDone = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
