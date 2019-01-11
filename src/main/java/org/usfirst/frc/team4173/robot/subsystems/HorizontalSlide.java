package org.usfirst.frc.team4173.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class HorizontalSlide extends Subsystem {

	public enum HorizontalSlidePosition {
		IN,
		OUT;
	}
	
	private HorizontalSlidePosition slidePosition;
	
	MotorController slideMotor;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	public HorizontalSlide (int slideMotorId) {
		this.slideMotor = new MotorController(slideMotorId);
		slidePosition = HorizontalSlidePosition.IN;
	}
	
	/*
	 * Set the speed of the motor in RPM
	 */
	public void setSlideMotorSpeed(double rpm) {
		slideMotor.setVelocityRPM(rpm);
	}
	
	/**
	 * Stops the slide motor
	 */
	public void stopSlide() {
		slideMotor.setVelocityRPM(0);
	}
	
	public double getCurrentDraw() {
		return slideMotor.getAmps();
	}
	
	public double getVoltage() {
		return slideMotor.getVoltage();
	}
	
	/*
	 * Gets the position of the motor in ticks
	 */
	public int getEncoderPosition() {
		return slideMotor.getEncoderPosition();
	}
	
	/*
	 * Gets where the slide is
	 */
	public HorizontalSlidePosition getSlidePosition() {
		return slidePosition;
	}
	
	/*
	 * Set what the current position of the slide should be considered as
	 */
	public void setSlidePosition(HorizontalSlidePosition position) {
		slidePosition = position;
	}
	
	public void resetEncoder() {
		slideMotor.resetEncoder();
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

