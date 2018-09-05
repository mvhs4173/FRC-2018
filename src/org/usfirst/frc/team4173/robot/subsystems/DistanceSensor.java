package org.usfirst.frc.team4173.robot.subsystems;
import edu.wpi.first.wpilibj.AnalogInput;

public class DistanceSensor {
		AnalogInput distanceSensor;
		
		private final double voltPer5MM = 0.00488;//How many volts per 5 mm
		
		public DistanceSensor(int port) {
			distanceSensor = new AnalogInput(port);
		}
		
		public double getRawVoltage() {
			return distanceSensor.getVoltage();
		}
		
		/**
		 * Gets the distance in MM
		 * @return Millimeters
		 */
		public double getDistanceMM() {
			return getRawVoltage()/voltPer5MM;
		}
		
		/**
		 * Gets the distance in Cm
		 * @return Centimeters
		 */
		public double getDistanceCM() {
			return getDistanceMM()/10;
		}
		
		public double getDistanceInch() {
			return getDistanceMM()/25.4;
		}
}
