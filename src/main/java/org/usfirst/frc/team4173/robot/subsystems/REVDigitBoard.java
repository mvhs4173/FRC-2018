package org.usfirst.frc.team4173.robot.subsystems; // might need to change this number

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

import java.util.*;

public class REVDigitBoard {
	/*
	 * DOCUMENTATION::
	 * 
	 * REVDigitBoard() : constructor
	 * void display(String str) : displays the first four characters of the string (only alpha (converted to uppercase), numbers, and spaces)
	 * void display(double batt) : displays a decimal number (like battery voltage) in the form of 12.43 (ten-one-decimal-tenth-hundredth)
	 * void clear() : clears the display
	 * boolean getButtonA() : button A on the board
	 * boolean getButtonB() : button B on the board
	 * double getPot() : potentiometer value
	 */
	
	I2C i2c;
	DigitalInput buttonA, buttonB;
	AnalogInput pot;
	
	int A,B,C,D,E,F,G1,G2,H,J,K,L,M,N,Dp,none;
	int[] ledreg;
	
	byte[][] charreg;
	@SuppressWarnings("rawtypes")
	Map charmap;
	
	@SuppressWarnings("unchecked")
	public
	REVDigitBoard() {
		i2c = new I2C(Port.kMXP, 0x70);
		buttonA = new DigitalInput(19);
		buttonB = new DigitalInput(20);
		pot = new AnalogInput(3);
		
		byte[] osc = new byte[1];
	 	byte[] blink = new byte[1];
	 	byte[] bright = new byte[1];
	 	osc[0] = (byte)0x21;
	 	blink[0] = (byte)0x81;
	 	bright[0] = (byte)0xEF;

		i2c.writeBulk(osc);
		Timer.delay(.01);
		i2c.writeBulk(bright);
		Timer.delay(.01);
		i2c.writeBulk(blink);
		Timer.delay(.01);
		
		ledreg = new int[16];
		
		A = ledreg[0] = (byte)0b00000001;
		B = ledreg[1] = (byte)0b00000010;
		C = ledreg[2] = (byte)0b00000100;
		D = ledreg[3] = (byte)0b00001000;
		E = ledreg[4] = (byte)0b00010000;
		F = ledreg[5] = (byte)0b00100000;
		G1 = ledreg[6] = (byte)0b01000000;
		G2 = ledreg[7] = (byte)0b10000000;
		H = ledreg[8] = (byte)0b00000001;
		J = ledreg[9] = (byte)0b00000010;
		K = ledreg[10] = (byte)0b00000100;
		N = ledreg[11] = (byte)0b00001000;
		M = ledreg[12] = (byte)0b00010000;
		L = ledreg[13] = (byte)0b00100000;
		Dp = ledreg[14] = (byte)0b01000000;
		none = ledreg[15] = (byte)0b00000000;
		
		
		charreg = new byte[48][2]; //charreg is short for character registry
		charmap = new HashMap<Character, Integer>(); 
		/*
		 * the light segment correlation
		 * byte 0 0b|1 |1 |1|1|1|1|1|1|
		 * 		  0b|G2|G1|F|E|D|C|B|A|
		 * byte 1 0b|0|1|1|1|1|1|1|1|
		 * 		  0b| |.|L|M|N|K|J|H|
		 */
		charreg[0][0] = (byte)(A+B+C+D+E+F); charreg[0][1] = (byte)(L+K); //0
		charmap.put('0',0);
		charreg[1][0] = (byte)(B+C); charreg[1][1] = (byte)none; //1
		charmap.put('1',1);
	 	charreg[2][0] = (byte)(G1+G2+E+D+B+A); charreg[2][1] = (byte)none; //2
		charmap.put('2',2);
	 	charreg[3][0] = (byte)(G1+G2+D+C+B+A); charreg[3][1] = (byte)none; //3
		charmap.put('3',3);
	 	charreg[4][0] = (byte)(G1+G2+F+C+B); charreg[4][1] = (byte)none; //4
		charmap.put('4',4);
	 	charreg[5][0] = (byte)(G1+G2+F+D+C+A); charreg[5][1] = (byte)none; //5
		charmap.put('5',5);
	 	charreg[6][0] = (byte)(G1+G2+F+E+D+C+A); charreg[6][1] = (byte)none; //6
		charmap.put('6',6);
	 	charreg[7][0] = (byte)(C+B+A); charreg[7][1] = (byte)none; //7
		charmap.put('7',7);
	 	charreg[8][0] = (byte)(G1+G2+F+E+D+C+B+A); charreg[8][1] = (byte)none; //8
		charmap.put('8',8);
	 	charreg[9][0] = (byte)(G1+G2+F+D+C+B+A); charreg[9][1] = (byte)none; //9
		charmap.put('9',9);
		charreg[10][0] = (byte)none; charreg[10][1] = (byte)Dp; //.
		charmap.put('.',10);
		charreg[11][0] = (byte)0b00000110; charreg[11][1] = (byte)0b01000000;//1.
		charreg[12][0] = (byte)0b11011011; charreg[12][0] = (byte)0b01000000;//2.
		charreg[13][0] = (byte)0b11001111; charreg[13][0] = (byte)0b01000000;//3.
		charreg[14][0] = (byte)0b11100110; charreg[14][0] = (byte)0b01000000;//4.
		charreg[15][0] = (byte)0b11101101; charreg[15][0] = (byte)0b01000000;//5.
		charreg[16][0] = (byte)0b11111101; charreg[16][0] = (byte)0b01000000;//6.
		charreg[17][0] = (byte)0b00000111; charreg[17][0] = (byte)0b01000000;//7.
		charreg[18][0] = (byte)0b11111111; charreg[18][0] = (byte)0b01000000;//8.
		charreg[19][0] = (byte)0b11101111; charreg[19][0] = (byte)0b01000000;//9.

	 	charreg[20][0] = (byte)(G1+G2+F+E+C+B+A); charreg[20][1] = (byte)none; //A
		charmap.put('A',20);
	 	charreg[21][0] = (byte)(G2+D+C+B+A); charreg[21][1] = (byte)(J+M); //B
		charmap.put('B',21);
	 	charreg[22][0] = (byte)(A+D+E+F); charreg[22][1] = (byte)none; //C
		charmap.put('C',22);
	 	charreg[23][0] = (byte)(A+B+C+D); charreg[23][1] = (byte)(J+M); //D
		charmap.put('D',23);
	 	charreg[24][0] = (byte)(G1+G2+F+E+D+A); charreg[24][1] = (byte)none; //E
		charmap.put('E',24);
	 	charreg[25][0] = (byte)(G1+F+E+A); charreg[25][1] = (byte)none; //F
		charmap.put('F',25);
	 	charreg[26][0] = (byte)(D+G2+F+E+C+A); charreg[26][1] = (byte)none; //G
		charmap.put('G',26);
	 	charreg[27][0] = (byte)(G1+G2+F+E+B+C); charreg[27][1] = (byte)none; //H
		charmap.put('H',27);
	 	charreg[28][0] = (byte)(A+D); charreg[28][1] = (byte)(M+J); //I
		charmap.put('I',28);
	 	charreg[29][0] = (byte)(B+D+C+E); charreg[29][1] = (byte)none; //J
		charmap.put('J',29);
	 	charreg[30][0] = (byte)(E+F+G1); charreg[30][1] = (byte)(K+N); //K
		charmap.put('K',30);
	 	charreg[31][0] = (byte)(D+E+F); charreg[31][1] = (byte)none; //L
		charmap.put('L',31);
	 	charreg[32][0] = (byte)(B+C+E+F); charreg[32][1] = (byte)(H+K); //M
		charmap.put('M',32);
	 	charreg[33][0] = (byte)(B+C+E+F); charreg[33][1] = (byte)(H+N); //N
		charmap.put('N',33);
	 	charreg[34][0] = (byte)(A+B+C+D+E+F); charreg[34][1] = (byte)none; //O
		charmap.put('O',34);
	 	charreg[35][0] = (byte)(G1+G2+F+E+B+A); charreg[35][1] = (byte)none; //P
		charmap.put('P',35);
	 	charreg[36][0] = (byte)(A+B+C+D+E+F); charreg[36][1] = (byte)N; //Q
		charmap.put('Q',36);
	 	charreg[37][0] = (byte)(G1+G2+F+E+B+A); charreg[37][1] = (byte)N; //R
		charmap.put('R',37);
	 	charreg[38][0] = (byte)(A+C+D+G2); charreg[38][1] = (byte)H; //S
		charmap.put('S',38);
	 	charreg[39][0] = (byte)A; charreg[39][1] = (byte)(J+M); //T
		charmap.put('T',39);
	 	charreg[40][0] = (byte)(B+C+D+E+F); charreg[40][1] = (byte)none; //U
		charmap.put('U',40);
	 	charreg[41][0] = (byte)(F+E); charreg[41][1] = (byte)(L+K); //V
		charmap.put('V',41);
	 	charreg[42][0] = (byte)(B+C+E+F); charreg[42][1] = (byte)(L+N); //W
		charmap.put('W',42);
	 	charreg[43][0] = (byte)none; charreg[43][1] = (byte)(L+N+K+H); //X
		charmap.put('X',43);
	 	charreg[44][0] = (byte)none; charreg[44][1] = (byte)(H+K+M); //Y
		charmap.put('Y',44);
	 	charreg[45][0] = (byte)(A+D); charreg[45][1] = (byte)(K+L); //Z
		charmap.put('Z',45);
		charreg[46][0] = (byte)none; charreg[46][1] = (byte)none; //space
		charmap.put(' ',46);
		charreg[47][0] = (byte)D; charreg[47][1] = (byte)none; //_
		charmap.put('_', 47);
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void display(String str) { // only displays first 4 chars
		int[] charz = new int[4];
		// uppercase and map it
		str = repeat(' ',Math.max(0, 4-str.length())) + str.toUpperCase(); // pad it to 4 chars
		
		for (int i = 0; i < 4; i++) {
			
			Integer g = (int) charmap.getOrDefault(str.charAt(i), 47);
			if (g == null) {
				g = 46;
			}
			charz[i] = g;
			
		}
		this._display(charz);
	}
	
	public void display(double batt) { // optimized for battery voltage, needs a double like 12.43
		int[] charz = {46,46,46,46};
		// idk how to decimal point
		int ten = (int)(batt/10);
		int one = (int)(batt%10);
		int tenth = (int)((batt*10)%10);
		int hundredth = (int)((batt*100)%10);
		
		if (ten != 0) charz[0] = ten;
		charz[1] = one;
		charz[2] = tenth;
		charz[3] = hundredth;
		
		this._display(charz);
	}
	
	 public void clear() {
		 int[] charz = {46,46,46,46}; // whyy java
		 this._display(charz);
	 }
	 
	 public boolean getButtonA() {
		 return buttonA.get();
	 }
	 public boolean getButtonB() {
		 return buttonB.get();
	 }
	 public double getPot() {
		 return pot.getValue();
	 }
	
////// not supposed to be publicly used..
	
	void _display(int[] charz) {
		byte[] byte1 = new byte[10];
		byte1[0] = (byte)(0b0000111100001111);
 		byte1[2] = charreg[charz[3]][0];
 		byte1[3] = charreg[charz[3]][1];
 		byte1[4] = charreg[charz[2]][0];
 		byte1[5] = charreg[charz[2]][1];
 		byte1[6] = charreg[charz[1]][0];
 		byte1[7] = charreg[charz[1]][1];
 		byte1[8] = charreg[charz[0]][0];
 		byte1[9] = charreg[charz[0]][1];
 		//send the array to the board
 		i2c.writeBulk(byte1);
 		Timer.delay(0.01);
	}
	
	String repeat(char c, int n) {
	    char[] arr = new char[n];
	    Arrays.fill(arr, c);
	    return new String(arr);
	}
	
}


