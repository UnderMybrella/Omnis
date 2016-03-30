package org.abimon.omnis.net;

import org.abimon.omnis.io.Data;
import org.abimon.omnis.net.Website.POST;

public class Pastebin {
	String devKey = "";
	String usrKey = "";
	Website pastebin = new Website("pastebin.com/api/api_post.php");


	public Pastebin(String devKey, String username, String password){
		this.devKey = devKey;
		if(!username.equals(""))
			this.usrKey = new Website("http://pastebin.com/api/api_login.php").postData(new POST().put("api_dev_key", devKey).put("api_user_name", username).put("api_user_password", password)).getAsString();
		System.out.println(usrKey);
	}

	public String newPaste(String title, String content){
		POST post = new POST();
		post.put("api_option", "paste");
		post.put("api_dev_key", devKey);
		post.put("api_paste_code", content);
		post.put("api_paste_name", title);
		if(!usrKey.equals(""))
			post.put("api_user_key", usrKey);
		Data data = pastebin.postData(post);
		return data.getAsString();
	}

	public String editPaste(String title, String content){
		if(usrKey.equalsIgnoreCase(""))
			return "Not Logged In";
		POST post = new POST();
		post.put("api_option", "list");
		post.put("api_dev_key", devKey);
		post.put("api_user_key", usrKey);
		
		for(String s : newPaste(title, content).split("\n"))
			System.out.println(s);
		
		return "";
	}

	public String deletePaste(String code){
		if(usrKey.equalsIgnoreCase(""))
			return "Not Logged In";

		POST post = new POST();
		post.put("api_option", "delete");
		post.put("api_dev_key", devKey);
		post.put("api_user_key", usrKey);
		post.put("api_paste_key", code);

		Data data = pastebin.postData(post);

		return data.getAsString();
	}
}
