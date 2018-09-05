package org.usfirst.frc.team4173.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class LinearSlide extends Subsystem {
	TalonSRX motor1;
	MagneticLimitSwitch switch1, switch2, switch3, switch4, switch5;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public LinearSlide(int motorID, int magID, int magID2, int magID3, int magID4, int magID5) {
    	motor1 = new TalonSRX(motorID);
    	motor1.configReverseSoftLimitEnable(false, 10);
    	switch1 = new MagneticLimitSwitch(magID);
    	switch2 = new MagneticLimitSwitch(magID2);
    	switch3 = new MagneticLimitSwitch(magID3);
    	switch4 = new MagneticLimitSwitch(magID4);
    	switch5 = new MagneticLimitSwitch(magID5);
    }
    
    public void runAtPercentPower(double input) {
    	motor1.set(ControlMode.PercentOutput, input);
    }
    
    public double getCurrent() {
    	return motor1.getMotorOutputPercent();
    }
    
    public boolean getStateLower() {
    	return switch1.isMagnetClose();
    }
    
    public boolean getStateUpper() {
    	return switch2.isMagnetClose();
    }
    
    public void stopMotor() {
    	motor1.set(ControlMode.PercentOutput, 0);
    }
    
    public boolean[] getState() {
    	boolean[] bool = {switch1.isMagnetClose(),
    			switch2.isMagnetClose(),
    			switch3.isMagnetClose(),
    			switch4.isMagnetClose(),
    			switch5.isMagnetClose()};
    	return bool;
    }
    
    public enum slidePosition {
    	FLOOR,
    	SWITCH,
    	SCALE_LOW,
    	SCALE_MID,
    	SCALE_HIGH
    }
}