package org.usfirst.frc.team4173.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;

/*
 * Provides which side of a game object is your color
 */
public class GameDataRetriever {
	
	private String gameData = "";
	
	private DriverStation driverStation;
	
	public enum ColorSide {
		LEFT,
		RIGHT,
		NOT_AVAILABLE;
	}
	
	public enum GameObject {
		ALLIANCE_SWITCH,
		SCALE,
		ENEMY_SWITCH;
	}
	
	public GameDataRetriever() {
		driverStation = DriverStation.getInstance();
	}
	
	/**
	 * Gets which side of the game object is your alliance's color
	 * @param gameObject The object on the field you are getting the color of
	 * @return An enum indicating which side of the game object your color is on
	 */
	public ColorSide getGameObjectColorSide(GameObject gameObject) {
		//Gets the game data from the driver station
		gameData = driverStation.getGameSpecificMessage();
		
		if (gameData == null) {
			gameData = "";
		}
		
		ColorSide result = ColorSide.NOT_AVAILABLE;
		int charIndex = 0;
		
		//Decide which character from the gameData string to get 
		switch (gameObject) {
			case ALLIANCE_SWITCH :
				charIndex = 0;
				break;
			case SCALE :
				charIndex = 1;
				break;
			case ENEMY_SWITCH :
				charIndex = 2;
				break;
		}
		
		
		//Check if anything was received
		if (gameData.length() > 0) {
			//Get the character that corresponds to the game object we are getting the color side for
			char objectSide = gameData.charAt(charIndex);
			
			if (objectSide == 'L') {
				result = ColorSide.LEFT;
			}else {
				result = ColorSide.RIGHT;
			}
		}
		
		return result;
	}
}
