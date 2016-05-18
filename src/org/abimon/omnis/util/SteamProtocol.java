package org.abimon.omnis.util;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.net.URI;

public class SteamProtocol {
	private static final String base = "steam://";
	
	public static void openSteam(){
		open("");
	}
	
	public static void exitSteam(){
		open("ExitSteam");
	}
	
	public static void addNonSteamGame(){
		open("AddNonSteamGame");
	}
	
	public static void openStore(String steamID){
		open("advertise/" + steamID);
	}
	
	public static void acceptGift(String giftPass){
		open("ackMessage/ackGuestPass/" + giftPass);
	}
	
	public static void openNews(String steamID){
		open("appnews/" + steamID);
	}
	
	public static void backup(String steamID){
		open("backup/" + steamID);
	}
	
	public static void browseMedia(){
		open("browsemedia");
	}
	
	public static void checkSystemRequirements(String steamID){
		open("checksysreq/" + steamID);
	}
	
	public static void defragApp(String steamID){
		open("defrag/" + steamID);
	}
	
	public static void openFriends(){
		open("friends");
	}
	
	public static void openGame(String steamID){
		open("run/" + steamID);
	}
	
	private static void open(String url){
		try{
			boolean headless = GraphicsEnvironment.isHeadless();
			if(headless)
				System.setProperty("java.awt.headless", "false");
			Desktop.getDesktop().browse(URI.create(base + url));
			System.err.println(URI.create(base + url));
			if(headless)
				System.setProperty("java.awt.headless", "true");
		}
		catch(Throwable th){
			th.printStackTrace();
		}
	}
}
