package org.abimon.omnis.net;

import java.io.InputStream;
import java.net.URL;

public class Website {
	String ip;
	
	public Website(String ip){
		this.ip = ip;
	}
	
	public String retrieveContent(){
		try{
			InputStream http = new URL(ip).openStream();
			byte[] data = new byte[http.available()];
			http.read(data);
			http.close();
			return new String(data);
		}
		catch(Throwable th){}
		return "";
	}
}
