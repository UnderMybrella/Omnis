package org.abimon.omnis.net;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

import org.abimon.omnis.io.Data;

public class Website {
	String ip;

	public Website(String ip){
		if(!ip.startsWith("http") && !ip.startsWith("https"))
			ip = "http://" + ip;
		this.ip = ip;
	}

	public String retrieveContent(){
		return retrieveData(Proxy.NO_PROXY).getAsString();
	}

	public String retrieveContent(Proxy proxy){
		return retrieveData(proxy).getAsString();
	}

	public Data retrieveData() {
		return retrieveData(Proxy.NO_PROXY);
	}

	public Data retrieveData(Proxy proxy) {
		try{
			HttpURLConnection http = (HttpURLConnection) new URL(ip).openConnection(proxy);
			http.setRequestMethod("GET");
			http.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0");

			//http.setRequestProperty("", value);
			return new Data(http.getInputStream());
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return new Data();
	}

	public Data postData(POST post){
		try{
			HttpURLConnection http = (HttpURLConnection) new URL(ip).openConnection();
			http.setDoOutput(true);
			PrintStream out = new PrintStream(http.getOutputStream());
			out.println(post.getPost());

			return new Data(http.getInputStream());
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return new Data();
	}
	
	public static class POST{
		
		private static final String ENCODING = "UTF-8";
		private HashMap<String, String> post;

		/**
		 * Creates a new <code>Post</code> instance.
		 */
		public POST() {
			post = new HashMap<String, String>();
		}

		public POST put(String key, String value) {
			try {
				this.post.put(URLEncoder.encode(key, ENCODING),
						URLEncoder.encode(value, ENCODING));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return this;
		}

		public String getPost() {
			StringBuilder builder = new StringBuilder();
			for (Entry<String, String> entry : post.entrySet()) {
				builder.append(entry.getKey()).append('=').append(entry.getValue())
						.append('&');
			}
			builder.deleteCharAt(builder.length() - 1);
			return new String(builder);
		}

	}
}
