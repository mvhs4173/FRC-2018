package org.usfirst.frc.team4173.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Pnumatics extends Subsystem {
	Compressor compressor;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    /**
     * The Constructor
     * @param compressorID id of PnumaticsControlModuale
     */
    public Pnumatics(int compressorID) {
    	compressor = new Compressor(0);

    	compressor.setClosedLoopControl(true);
    }
    
    /**
     * 
     * @return true if pressure is below 120PSI 
     */
    public boolean pressureSwitchState() {
    	return compressor.getPressureSwitchValue();
    }
    
    /**
     * 
     * @return Tells if the compressor is on
     */
    public boolean isEnabled() {
    	return compressor.enabled();
    }
    
    /**
     * 
     * @return The current the compressor is consuming in AMPS
     */
    public double compressorCurrent() {
    	return compressor.getCompressorCurrent();
    }
    
    /**
     * 
     * @param PortID port on PCM 0-7
     */
    public void newSolenoid(Solenoid name, int PortID) {
    	name = new Solenoid(PortID);
    }
    
    /**
     * 
     * @param name name of the solenoid to create
     * @param PortID1 Forward Channel 0-7
     * @param PortID2 Reverse Channel 0-7
     */
    public void newDoubleSolenoid(DoubleSolenoid name, int PortID1, int PortID2) {
    	name = new DoubleSolenoid(PortID1, PortID2) ;
    }
    
    /**
     * 
     * @param name name of solenoid to control
     * @param state true opens false closes
     */
    public void setSolenoid(Solenoid name, boolean state) {
		name.set(state);
	}
    
    /**
     * 
     * @param name name of double solenoid to control
     * @param state kOff, kForward, kReverse
     */
    public void setDoubleSolenoid(DoubleSolenoid name, DoubleSolenoid.Value state) {
    	name.set(state);
    }
}

