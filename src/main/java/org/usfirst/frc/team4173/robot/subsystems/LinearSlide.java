package org.usfirst.frc.team4173.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LinearSlide extends Subsystem {
	MotorController liftMotor;
	public final double maxAmps = 35.0;
	int slideFloorPosition = 0;
	int slideSwitchPosition = 7080;//Raw value is negative
	int slideScaleLowPosition = 17832;//Raw value is negative
	int slideScaleMidPosition = 23072;//Raw value is negative
	int slideScaleHighPosition = 0;
	
	double slidePfactor = 0.01;
	int allowableError = 20;//ticks
	
	public slideDirection dir;
	
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    /**
     * A class for controlling the Linear Slide of the robot
     * @param motorId The Id of the motor to control
     * @param lowSwitchId The port that the magnetic limit switch is plugged into for sensing when the slide is at the lowest position
     * @param highSwitchId The port that the magnetic limit switch is plugged into for sensing when the slide is at its highest position
     */
    public LinearSlide(int motorId) {
    	liftMotor = new MotorController(motorId);
    	
    	//Configure the quadrature encoder for the lift motor
    	liftMotor.configQuadEncoder();
    	liftMotor.setMaxAccelerationTime(0.3);
    	
    	liftMotor.setBrake(NeutralMode.Brake);
    }
    
    /**
     * Gets the number of ticks that the given slide position is equivalent to
     * @param position Slide position
     * @return Slide position in ticks
     */
    @SuppressWarnings("incomplete-switch")
	public int getSlidePositionTicksEquivalent(slidePosition position) {
    	int ticks = 0;
    	
    	switch(position) {
    		case FLOOR:
    			ticks = slideFloorPosition;
    			break;
    		case SWITCH:
    			ticks = slideSwitchPosition;
    			break;
    		case SCALE_LOW:
    			ticks = slideScaleLowPosition;
    			break;
    		case SCALE_MID:
    			ticks = slideScaleMidPosition;
    			break;
    		case SCALE_HIGH:
    			ticks = slideScaleHighPosition;
    			break;
    	}
    	
    	return ticks;
    }
    
    /**
     * Run the motor at desired number of rpms
     * @param input The speed of the motor in rpm
     */
    public void runAtRPM(double rpm) {
    	liftMotor.setVelocityRPM(-rpm);
    	
    	if (rpm > 0.0) {
    		dir = slideDirection.UPWARD;
    	} else if (rpm < 0.0) {
    		dir = slideDirection.DOWNWARD;
    	}
    	
    }
    
    public int getMotorPosition() {
    	return -liftMotor.getEncoderPosition();//Make the value negative because in its raw value going up makes the encoder give a negative value
    }
    
   
    
    /**
     * Gives the amperage of the motor
     * @return The current of the motor in amps
     */
    public double getCurrent() {
    	return liftMotor.getAmps();
    }
    
    public void resetEncoder() {
    	liftMotor.resetEncoder();
    }
    
    /**
     * Checks the amperage of the motor and if it is too high then it will stop the motor to 
     * prevent it from burning out
     */
    public void regulateMotorCurrent() {
    	double motorCurrent = getCurrent();
    	
    	//If the amperage that the motor is using is too high then stop the motor
    	if (motorCurrent >= maxAmps) {
    		stopMotor();
    	}
    }
    
    public void stopMotor() {
    	liftMotor.setVelocityRPM(0);
    }
    
    /**
     * Gives the position of the slide
     * @return The position of the slide as an enum
     */
    public slidePosition getSlidePosition() {
    	int encoderTicks = liftMotor.getEncoderPosition();
    	slidePosition position = slidePosition.UNKNOWN;
    	
    	//Determine the position of the scale based on the encoder ticks
    	if (encoderTicks <= slideFloorPosition) {
    		position = slidePosition.FLOOR;
    	}else if (encoderTicks <= slideSwitchPosition && encoderTicks > slideFloorPosition) {
    		position = slidePosition.SWITCH;
    	}else if (encoderTicks <= slideScaleLowPosition && encoderTicks > slideSwitchPosition) {
    		position = slidePosition.SCALE_LOW;
    	}else if (encoderTicks <= slideScaleMidPosition && encoderTicks > slideScaleLowPosition) {
    		position = slidePosition.SCALE_MID;
    	}else if (((encoderTicks <= slideScaleHighPosition || encoderTicks > slideScaleHighPosition) && encoderTicks > slideScaleMidPosition)) {
    		position = slidePosition.SCALE_HIGH;
    	}
    	
    	return position;
    }
    
    public enum slidePosition {
    	FLOOR,
    	SWITCH,
    	SCALE_LOW,
    	SCALE_MID,
    	SCALE_HIGH,
    	UNKNOWN
    }
    
    public enum slideDirection {
    	UPWARD,
    	DOWNWARD
    }
}