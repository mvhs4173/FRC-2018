package org.usfirst.frc.team4173.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	MotorController climbMotor;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public Climber(MotorController climbMotor) {
    	this.climbMotor = climbMotor;
    }
    
    public void setSpeedRPM(double rpm) {
    	climbMotor.setVelocityRPM(rpm);
    }
}

