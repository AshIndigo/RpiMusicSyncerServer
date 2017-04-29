package com.ashindigo.rpi.music.server;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Manages the music files
 * @author Ash
 *
 */
public class MusicManager {
	
	public static File musicFolder = new File(ServerConfig.configMap.get(ServerConfig.DIR.name));
	public static ArrayList<String> users = new ArrayList<String>();

	/**
	 * Scans the music folder for new music and adds it to the database
	 * @throws ImproperConfigException
	 * @throws SQLException
	 */
	public static void scanMusicFolder() throws ImproperConfigException, SQLException {
		if (musicFolder.isDirectory()) {
			for (int i = 0; musicFolder.listFiles(new AudioFileFilter()).length > i; i++) {
				ResultSet rs = MusicServerMain.stmt.executeQuery("select * from music where songname = '" + musicFolder.listFiles(new AudioFileFilter())[i].getName().replaceAll("'", "''") + "'");
				if (!rs.next()) {
					MusicLogger.log("Music File Added to DB Name: " + musicFolder.listFiles(new AudioFileFilter())[i].getName());
					MusicServerMain.stmt.execute("INSERT INTO music(filepath, songname) values('" + musicFolder.listFiles(new AudioFileFilter())[i].getPath().replaceAll("'", "''") + "','" + musicFolder.listFiles(new AudioFileFilter())[i].getName().replaceAll("'", "''") + "');");
				}
			}
		} else {
			throw new ImproperConfigException("Config Directory setting is a file! Location: " + ServerConfig.configMap.get("Directory"));
		}
	}

	public static void setupUserFolders() throws SQLException {
		ResultSet rs = MusicServerMain.stmtS.executeQuery("select * from users");
		while (rs.next()) {
			
		}
		
	}

}
