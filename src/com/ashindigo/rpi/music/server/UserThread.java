package com.ashindigo.rpi.music.server;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.IOUtils;

/**
 * Offloads the file transfer from the main thread onto a separate one.
 * @author Ash
 *
 */
public class UserThread implements Runnable {

	private Socket sock; // Server Socket
	private Socket sockS; // Client Socket
	
	public UserThread(Socket sock) throws IOException {
		this.sock = sock;
	}

	/**
	 * Transfers the music to the client
	 */
	@Override
	public void run() {
		try {
			MusicLogger.log("Connected! " + sock.getInetAddress());
			sockS = new Socket(sock.getInetAddress(), 25566);
			System.out.println("Established Duel-way connection");
			String username = IOUtils.toString(sockS.getInputStream(), StandardCharsets.UTF_8);
			sockS = new Socket(sock.getInetAddress(), 25566);
			LcdManager.setText(0, username);
			String name = IOUtils.toString(sockS.getInputStream(), StandardCharsets.UTF_8);
			LcdManager.setText(1, name);
			MusicLogger.log("Song Name Requested: " + name.replaceAll("\n", ""));
			DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
			ResultSet rs = MusicServerMain.stmt.executeQuery("select filepath from " + username + " where songname = '" + name.replaceAll("'", "''").replaceAll("\n", "") + "'");
			rs.next();
			FileInputStream fis = new FileInputStream(rs.getString("filepath"));
			byte[] buffer = new byte[4096];
			while (fis.read(buffer) > 0) {
				dos.write(buffer);
			}
			
			fis.close();
			dos.close();	
			MusicLogger.log("File Sent: " + name);
			MusicServerMain.waitForUserConnection();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

}
