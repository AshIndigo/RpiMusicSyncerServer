package com.ashindigo.rpi.music.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * The main server class for the music host.
 * @author Ash
 *
 */
public class MusicServerMain {

	static File config = new File(System.getProperty("user.home") + File.separator + "rpiMConfig.conf");
	static Connection connS;
	static Statement stmtS;
	static Connection conn;
	static Statement stmt;
	static ServerSocket sockS;

	public static void main(String[] args) throws Exception {
		//config.createNewFile();
		if (!config.exists()) {
			ServerConfig.createConfig();
		}
		LcdManager.setupLcd();
		
		loadConfig();
		connS = DriverManager.getConnection("jdbc:postgresql://192.168.1.17:5432/users", "xxx", "xxx"); // Enter login details here for the server
		stmtS = connS.createStatement();
		conn = DriverManager.getConnection("jdbc:postgresql://192.168.1.17:5432/music", "xxx", "xxx"); // Enter your server login details here
		stmt = conn.createStatement();
		MusicLogger.log("Database Loaded");
		MusicManager.setupUserFolders();
		MusicManager.scanMusicFolder();
 		sockS = new ServerSocket(22556);
		waitForUserConnection();
	}
	
	/**
	 * Method for simple looping
	 * @throws IOException
	 */
	static void waitForUserConnection() throws IOException {
		new UserThread(sockS.accept()).run();
	}
	
	/**
	 * Reads the config in the users home directory.
	 * @throws IOException
	 */
	public static void loadConfig() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(config));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (!line.startsWith("#")) {
				for (int i = 0; ServerConfig.values().length > i; i++) {
					if (line.split("=")[0].equals(ServerConfig.values()[i].name)) {
						ServerConfig.configMap.put(ServerConfig.values()[i].name, line.split("=")[1]);
					}
				}
			}
		}
		
		br.close();
		MusicLogger.log("Config loaded Location: " + System.getProperty("user.home") + File.pathSeparator + "rpiMConfig.conf");
	}


}
