package org.usfirst.frc.team4173.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 *
 */
public class CubeDetector extends Subsystem {
	NetworkTable table;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	public CubeDetector() {
		//Get the network table that the camera is using
		table = NetworkTableInstance.getDefault().getTable("limelight");
		
		//Init the camera
		NetworkTableEntry ledMode = table.getEntry("ledMode");
		NetworkTableEntry camMode = table.getEntry("camMode");
		NetworkTableEntry pipeline = table.getEntry("pipeline");
		
		ledMode.setDouble(1.0);
		camMode.setDouble(1.0);
		pipeline.setDouble(0.0);
		enableCubeDetection();
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    /**
     * Check if the cube is detected
     * @return True if cube is detected
     */
    public boolean cubeDetected() {
    	NetworkTableEntry detected = table.getEntry("tv");
    	double det = detected.getDouble(0);
    	
    	if (det == 0) {
    		return false;
    	}else {
    		return true;
    	}
    }
    
    public void ledEnabled(boolean enabled) {
    	NetworkTableEntry ledMode = table.getEntry("ledMode");
    	if (enabled) {
    		ledMode.setDouble(0.0);
    	}else {
    		ledMode.setDouble(1.0);
    	}
    }
    
   /**
    * Disables vision processing, makes camera only used so driver can see where they are going
    */
    public void enableDriverCamera() {
    	NetworkTableEntry cameraMode = table.getEntry("camMode");
    	
    	cameraMode.setDouble(1.0);
    }
    
    public double getTargetArea() {
    	NetworkTableEntry targetArea = table.getEntry("ta");
    	return targetArea.getDouble(0);
    }
    
    /**
     * Enables vision processing, the camera will look for the power cube
     */
    public void enableCubeDetection() {
    	NetworkTableEntry cameraMode = table.getEntry("camMode");
    	cameraMode.setDouble(0.0);
    }
    
    /**
     * Gets the horizontal offset from the crosshair to the cube in degrees (-27 - 27)
     * @return Degrees offset
     */
    public double getHorizontalOffset() {
    	NetworkTableEntry offsetEntry = table.getEntry("tx");
    	return offsetEntry.getDouble(0);
    }
}

