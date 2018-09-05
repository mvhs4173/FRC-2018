package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide;
import org.usfirst.frc.team4173.robot.subsystems.RogersButton;
import org.usfirst.frc.team4173.robot.subsystems.LinearSlide.slidePosition;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class MoveSlide2 extends Command {

	public LinearSlide.slidePosition desiredPos;
	public LinearSlide.slidePosition currentPos;
	LinearSlide slide = Robot.linearSlide;
	BasicCounter cnt = new BasicCounter();
	RogersButton positionCheck = new RogersButton();
	
    public MoveSlide2(LinearSlide.slidePosition position) {
        // Use requires() here to declare subsystem dependencies
    	desiredPos = position;
    	currentPos = Robot.current;
        requires(Robot.linearSlide);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	currentPos = Robot.current;
    	switch (currentPos) {
		case FLOOR:
			cnt.set(0);
			break;
		case SCALE_HIGH:
			cnt.set(4);
			break;
		case SCALE_LOW:
			cnt.set(2);
			break;
		case SCALE_MID:
			cnt.set(3);
			break;
		case SWITCH:
			cnt.set(1);
			break;
		default:
			break;
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch(desiredPos) {
		case SCALE_HIGH:
			if (cnt.value() < 4) {
				slide.runAtPercentPower(1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.up();
		    	}
			}
			if (cnt.value() == 4) {
				Robot.current = slidePosition.SCALE_HIGH;
			}
			break;
		case SCALE_LOW:
			if (cnt.value() < 2) {
				slide.runAtPercentPower(1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.up();
		    	}
			} else if (cnt.value() > 2) {
				slide.runAtPercentPower(-1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.down();
		    	}
			}
			if (cnt.value() == 2) {
				Robot.current = slidePosition.SCALE_LOW;
			}
			break;
		case SCALE_MID:
			if (cnt.value() < 3) {
				slide.runAtPercentPower(1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.up();
		    	}
			} else if (cnt.value() > 3) {
				slide.runAtPercentPower(-1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.down();
		    	}
			}
			if (cnt.value() == 3) {
				Robot.current = slidePosition.SCALE_MID;
			}
			break;
		case SWITCH:
			if (cnt.value() < 1) {
				slide.runAtPercentPower(1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.up();
		    	}
			} else if (cnt.value() > 1) {
				slide.runAtPercentPower(-1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.down();
		    	}
			}
			if (cnt.value() == 1) {
				Robot.current = slidePosition.SWITCH;
			}
			break;
		case FLOOR:
			if (cnt.value() > 0) {
				slide.runAtPercentPower(-1);
				if (positionCheck.wasJustClicked(slide.getState()[0])) {
		    		cnt.down();
		    	}
			}
			if (cnt.value() == 0) {
				Robot.current = slidePosition.FLOOR;
			}
			break;
		default:
			break;
    	}
    	SmartDashboard.putNumber("Counter", cnt.value());
    	SmartDashboard.putString("Current Pos", currentPos.toString());
    	SmartDashboard.putString("Desired Pos", desiredPos.toString());
    }
    
    public interface ResetableCounter {
    	void reset();
    	int value();
    	void up();
    	void down();
    }
    
    public class BasicCounter implements ResetableCounter {
    	int counterVariable = 0;
    	public void main(String[] args){
    		BasicCounter cnt = new BasicCounter();
    		cnt.up();
    		cnt.down();
    		System.out.printf("The value is %d", cnt.counterVariable); 
    	}

    	public void reset() {
    		this.counterVariable = 0;
    	}
    	
    	public int value() {
    		return this.counterVariable;
    	}

    	public void up() {
    		++this.counterVariable;
    	}

    	public void down() {
    		--this.counterVariable;
    	}

		public void set(int val) {
			counterVariable = val;
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean bool;
    	if(desiredPos == slidePosition.FLOOR) {
        	bool = cnt.value() == 0;
        } else if(desiredPos == slidePosition.SWITCH) {
        	bool = cnt.value() == 1;
        } else if(desiredPos == slidePosition.SCALE_LOW) {
        	bool = cnt.value() == 2;
        } else if(desiredPos == slidePosition.SCALE_MID) {
        	bool = cnt.value() == 3;
        } else if(desiredPos == slidePosition.SCALE_HIGH) {
        	bool = cnt.value() == 4;
        } else {
        	bool = false;
        }
        return bool;
    }

    // Called once after isFinished returns true
    protected void end() {
    	slide.stopMotor();
    	Robot.prefs.putString("Current Slide Position", currentPos.toString());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
