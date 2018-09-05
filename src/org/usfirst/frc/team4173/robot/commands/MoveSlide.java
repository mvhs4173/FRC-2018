package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide.slidePosition;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveSlide extends Command {

	public LinearSlide.slidePosition desiredPos;
	public LinearSlide.slidePosition currentPos;
	LinearSlide slide = Robot.linearSlide;
	
    public MoveSlide(LinearSlide.slidePosition position) {
        // Use requires() here to declare subsystem dependencies
    	desiredPos = position;
        requires(Robot.linearSlide);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch (desiredPos) {
		case FLOOR:
			if(currentPos != slidePosition.FLOOR) {
				slide.runAtPercentPower(-1);
				if(slide.getState()[0]) {
					currentPos = slidePosition.FLOOR;
				}
			}
			break;
		case SWITCH:
			if(currentPos != slidePosition.SWITCH) {
				if(currentPos == slidePosition.FLOOR) {
					slide.runAtPercentPower(1);
				} else {
					slide.runAtPercentPower(-1);
				}
				if(slide.getState()[1]) {
					currentPos = slidePosition.SWITCH;
				}
			}
			break;
		case SCALE_LOW:
			if(currentPos != slidePosition.SCALE_LOW) {
				if((currentPos == slidePosition.FLOOR) || (currentPos == slidePosition.SWITCH)) {
					slide.runAtPercentPower(1);
				} else if((currentPos == slidePosition.SCALE_HIGH) || (currentPos == slidePosition.SCALE_MID)) {
					slide.runAtPercentPower(-1);
				}
				if(slide.getState()[2]) {
					currentPos = slidePosition.SCALE_LOW;
				}
			}
			break;
		case SCALE_MID:
			if(currentPos != slidePosition.SCALE_MID) {
				if(currentPos == slidePosition.SCALE_HIGH) {
					slide.runAtPercentPower(-1);
				} else {
					slide.runAtPercentPower(1);
				}
				if(slide.getState()[3]) {
					currentPos = slidePosition.SCALE_MID;
				}
			}
			break;
		case SCALE_HIGH:
			if(currentPos != slidePosition.SCALE_HIGH) {
				slide.runAtPercentPower(1);
				if(slide.getState()[4]) {
					currentPos = slidePosition.SCALE_HIGH;
				}
			}
			break;
		default:
			break;
    	}
    	SmartDashboard.putString("Current Pos", currentPos.toString());
    	SmartDashboard.putString("Desired Pos", desiredPos.toString());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean bool;
    	if(desiredPos == slidePosition.FLOOR) {
        	bool = slide.getState()[0];
        } else if(desiredPos == slidePosition.SWITCH) {
        	bool = slide.getState()[1];
        } else if(desiredPos == slidePosition.SCALE_LOW) {
        	bool = slide.getState()[2];
        } else if(desiredPos == slidePosition.SCALE_MID) {
        	bool = slide.getState()[3];
        } else if(desiredPos == slidePosition.SCALE_HIGH) {
        	bool = slide.getState()[4];
        } else {
        	bool = false;
        }
        return bool;
    }

    // Called once after isFinished returns true
    protected void end() {
    	slide.stopMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
