package org.usfirst.frc.team4173.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;

/**
 * Created by ROBOT12 on 1/12/2018.
 */

public class TwoJointArm {
    private TalonSRX innerArmMotor = null;
    private TalonSRX outerArmMotor = null;

    private Servo armServo1 = null;

    private RogersButton right_bumper;
    private RogersButton left_bumper;

    private DigitalInput innerArmLowerLimit = null;
    private DigitalInput innerArmUpperLimit = null;
    private DigitalInput outerArmLimit = null;

    public double servoPos = 0.5;
    @SuppressWarnings("unused")
	private double outerClicksPerDegree = 9;
    @SuppressWarnings("unused")
	private double innerClicksPerDegree = 9;
    private double outerArmLength = 15.0;
    private double innerArmLength = 7.5;
    private double innerPFactor = 0.007;
    private double outerPFactor = 0.0035;
    private ArmCoordinates armCoordinates;
    public double innerTargetPosition;
    public double outerTargrtPosition;
    private LinearMap innerEncoderToDegree;
    private LinearMap outerEncoderToDegree;

    /**
     * 
     * @param newInnerArmMotor CAN ID
     * @param newOuterArmMotor CAN ID
     * @param newArmServo1 pwm channel 0-9
     * @param limit1 digital channel 0-9
     * @param limit2 digital channel 0-9
     * @param limit3 digital channel 0-9
     */
    public TwoJointArm(int newInnerArmMotor,
                    int newOuterArmMotor,
                    int newArmServo1,
                    int limit1,
                    int limit2,
                    int limit3){
        innerArmMotor = new TalonSRX(newInnerArmMotor);
        outerArmMotor = new TalonSRX(newOuterArmMotor);
        armServo1 = new Servo(newArmServo1);
        right_bumper = new RogersButton();
        left_bumper = new RogersButton();
        innerArmLowerLimit = new DigitalInput(limit1);
        innerArmUpperLimit = new DigitalInput(limit2);
        outerArmLimit = new DigitalInput(limit3);
        armCoordinates = new ArmCoordinates(innerArmLength, outerArmLength);
        innerEncoderToDegree = new LinearMap(0, -92, 805, 0);
        outerEncoderToDegree = new LinearMap(-1584, 0, -794, 90);
    }

    public void innerArmMotion(double power){
        if (innerArmUpperLimit.get() && innerArmLowerLimit.get()){
            innerArmMotor.set(ControlMode.PercentOutput, 0);
        }else if (innerArmLowerLimit.get()){
            innerArmMotor.set(ControlMode.PercentOutput, Math.abs(power));
        } else if (innerArmUpperLimit.get()){
            innerArmMotor.set(ControlMode.PercentOutput, -Math.abs(power));
        } else {
            innerArmMotor.set(ControlMode.PercentOutput, power * .5);
        }
    }

    public void outerArmMotion(double power){
        if (outerArmLimit.get()){
            outerArmMotor.set(ControlMode.PercentOutput, Math.abs(power * .5));
        } else {
            outerArmMotor.set(ControlMode.PercentOutput, power * .4);
        }
    }

    public void armClawMotion(boolean increase, boolean decrease){
        double psMax;
        if (right_bumper.wasJustClicked(increase)){
            servoPos = .585;
        } if (left_bumper.wasJustClicked(decrease)) {
            servoPos = .2;
        }
        // keeps the power from exceeding 100%
        psMax = Math.max(Math.abs(servoPos), Math.abs(servoPos));
        if (psMax > 1.0)
        {
            servoPos /= psMax;
        }
        armServo1.setPosition(servoPos);
    }

    public boolean getInnerArmLowerLimit(){
        return innerArmLowerLimit.get();
    }

    public boolean getInnerArmUpperLimit(){
        return innerArmUpperLimit.get();
    }

    public boolean getOuterArmLimit(){
        return outerArmLimit.get();
    }

    public double getInnerArmMotorPos() {
        return innerArmMotor.getSelectedSensorPosition(0);
    }

    public double getOuterArmMotorPos(){
        return outerArmMotor.getSelectedSensorPosition(0);
    }

    /**
     *
     * @param newTargetPosition in Degrees
     */
    public void setInnerArmPosition(double newTargetPosition){
        double power;
        innerTargetPosition = newTargetPosition;
        //innerArmMotor.setTargetPosition(innerDegreesToEncoder(newTargetPosition));
        power = innerPFactor * getInnerArmError(newTargetPosition);
        if (Math.abs(power) > .75) {
            power = .75*Math.signum(power);
        }
        innerArmMotor.set(ControlMode.PercentOutput, power);

    }

    public double getInnerArmError(double newTargetPosition){
        double currentPosition = innerEncoderToDegrees(innerArmMotor.getSelectedSensorPosition(0));
        double targetPosition = newTargetPosition;
        return Degrees.subtract(targetPosition, currentPosition);
    }

    /**
     *
     * @param newTargetPosition in Degrees
     */
    public void setOuterArmPosition(double newTargetPosition){
        double power;
        outerTargrtPosition = newTargetPosition;
        //outerArmMotor.setTargetPosition(outerDegreesToEncoder(newTargetPosition));
        power = outerPFactor * getOuterArmError(newTargetPosition);
        if (Math.abs(power) > 1) {
            power = 1*Math.signum(power);
        }
        outerArmMotor.set(ControlMode.PercentOutput, power);
    }

    public double getOuterArmError(double newTargetPosition){
        double currentPosition = outerEncoderToDegrees(outerArmMotor.getSelectedSensorPosition(0));
        double targetPosition = newTargetPosition;
        return Degrees.subtract(targetPosition, currentPosition);
    }

    public void setArmPosition(double theta, double phi){
        setInnerArmPosition(theta);
        setOuterArmPosition(phi);
    }

    public void setArmPosition(ArmXY xy, String elbowDirection){
        setArmPosition(armCoordinates.xyToAngles(xy, elbowDirection));
    }

    public void setArmPosition(ArmAngles angles){
        setArmPosition(angles.theta, angles.phi);
    }

    public void stopMotors(){
        outerArmMotor.set(ControlMode.PercentOutput, 0);
        innerArmMotor.set(ControlMode.PercentOutput, 0);
    }

    @SuppressWarnings("unused")
	private int innerDegreesToEncoder(double degrees){
        return (int) innerEncoderToDegree.backward(degrees);
    }

    public double innerEncoderToDegrees(double encodeClicks){
        return innerEncoderToDegree.forward(encodeClicks);
    }

    @SuppressWarnings("unused")
	private int outerDegreesToEncoder(double degrees){
        return (int) outerEncoderToDegree.backward(degrees);
    }

    public double outerEncoderToDegrees(double encodeClicks){
        return outerEncoderToDegree.forward(encodeClicks);
    }

    public static class ArmAngles{
        double theta;
        double phi;
        public ArmAngles(double theta, double phi){
            this.theta = theta;
            this.phi = phi;
        }

        public double getTheta(){
            return theta;
        }

        public double getPhi(){
            return phi;
        }
    }

    public static class ArmXY{
        double x;
        double y;
        public ArmXY(double x, double y){
            this.x = x;
            this.y = y;
        }

        public double getX(){
            return x;
        }

        public double getY(){
            return  y;
        }
    }

    private class ArmCoordinates {
        double[] armLengths = new double[2];

        public ArmCoordinates(double innerArm, double outerArm){
            armLengths[0] = innerArm; //inner arm or inner
            armLengths[1] = outerArm; //outer arm or outer
        }

        @SuppressWarnings("unused")
		public ArmXY anglesToXY(ArmAngles angles){
            double x = armLengths[0] * Degrees.sin(angles.theta) + armLengths[1] * Degrees.sin(angles.theta + angles.phi);
            double y = armLengths[0] * Degrees.cos(angles.theta) + armLengths[1] * Degrees.cos(angles.theta + angles.phi);
            return new ArmXY(x, y);
        }

        /**
         *
         * @param xy
         * @param elbowDirection "UP" or "DOWN"
         * @return
         */
        public ArmAngles xyToAngles(ArmXY xy, String elbowDirection){
            double d2 = (xy.x*xy.x) + (xy.y*xy.y);
            double phi = supplementary(Degrees.acos((Math.pow(armLengths[0], 2) + Math.pow(armLengths[1], 2) - d2) / (2 * armLengths[0] * armLengths[1])));
            double B = Degrees.acos((Math.pow(armLengths[0], 2) + d2 - Math.pow(armLengths[1], 2)) / (2 * armLengths[0] * Math.sqrt(d2)));
            double D = Degrees.atan2(xy.x, xy.y);
            double theta;
            boolean reverse = Degrees.cos(D-B) < Degrees.cos(D+B);
            // want elbow pointing up

            if (reverse || elbowDirection == "DOWN") {
                theta = D + B;
                phi = -phi;
            } else {
                theta = D - B;
            }

            return new ArmAngles(theta, phi);
        }

        private double supplementary(double angle){
            return 180 - angle;
        }

        @SuppressWarnings("unused")
		private double complementary(double angle){
            return 90 - angle;
        }
    }
}
