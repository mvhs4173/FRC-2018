package org.usfirst.frc.team4173.robot.commands;

import org.usfirst.frc.team4173.robot.Robot;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.ColorSide;
import org.usfirst.frc.team4173.robot.subsystems.GameDataRetriever.GameObject;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class GetGameObjectColorSides extends Command {
	
	private boolean isDone = false;
	
    public GetGameObjectColorSides() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Keep asking for the color side until it is given by FMS
    	ColorSide allianceSwitch = Robot.dataRetriever.getGameObjectColorSide(GameObject.ALLIANCE_SWITCH);
    	ColorSide scale = Robot.dataRetriever.getGameObjectColorSide(GameObject.SCALE);
    	
    	if (allianceSwitch != ColorSide.NOT_AVAILABLE && scale != ColorSide.NOT_AVAILABLE) {
    		Robot.allianceSwitchColorSide = allianceSwitch;
    		Robot.scaleColorSide = scale;
    		isDone = true;
    	}
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
    }
}
