package org.abimon.omnis.net;

import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;

public class Website {
	String ip;
	
	public Website(String ip){
		if(!ip.startsWith("http") && !ip.startsWith("https"))
			ip = "http://" + ip;
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
		catch(Throwable th){
			th.printStackTrace();
		}
		return "";
	}
	
	public String retrieveContent(Proxy proxy){
		try{
			InputStream http = new URL(ip).openConnection(proxy).getInputStream();
			byte[] data = new byte[http.available()];
			http.read(data);
			http.close();
			return new String(data);
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return "";
	}
}
