package com.ashindigo.rpi.music.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public enum ServerConfig {
	
	DIR("Directory", System.getProperty("user.home") + "/Music/", "The directory that music files are located"),
	;
	
	public String name;
	public String defaultValue;
	public String comment;
	
	public static HashMap<String, String> configMap = new HashMap<String, String>();
	public static File config = new File(System.getProperty("user.home") + "/rpiMConfig.conf");

	ServerConfig(String name, String defaultValue, String comment) {
		this.name = name;
		this.defaultValue = defaultValue;
		this.comment = comment;
	}
	
	public static void createConfig() throws IOException {
		config.createNewFile();
		FileWriter fw = new FileWriter(config);
		for (int i = 0; ServerConfig.values().length > i; i++) {
			String text1 = "# " + ServerConfig.values()[i].name + ":" + ServerConfig.values()[i].comment + " (Default: " + ServerConfig.values()[i].defaultValue + ")"; 
			fw.write(text1);
			fw.write(System.lineSeparator());
			String text2 = ServerConfig.values()[i].name + "=" + ServerConfig.values()[i].defaultValue;
			fw.write(text2);
			fw.write(System.lineSeparator());
			fw.write(System.lineSeparator());
		}
		fw.close();
		MusicLogger.log("New Config Generated");
	}
}
