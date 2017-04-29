package com.ashindigo.rpi.music.server;

public class LcdManager {
	
	static I2CLcd lcd;

	public static void setupLcd() throws Exception {
		I2CLcd lcd = new I2CLcd(2, 16);
		lcd.clear();
	}

	public static void setText(int i, String text) {
		lcd.setText(i, text);
	}
}
