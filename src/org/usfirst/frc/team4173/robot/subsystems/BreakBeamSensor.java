package org.usfirst.frc.team4173.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;

public class BreakBeamSensor {
	private DigitalInput breakBeamSensor;
	public BreakBeamSensor(int portNumber) {
		this.breakBeamSensor = new DigitalInput(portNumber);
		
	}
	public boolean isBeamBroken() {
		return !breakBeamSensor.get();
	}
}
