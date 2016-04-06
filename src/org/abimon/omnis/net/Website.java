package org.abimon.omnis.net;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.abimon.omnis.io.Data;

public class Website {
	String ip;
	String userAgent = "";
	String authorization = "";

	static{
		// FIXME: My JVM doesn't like the certificate. I should go add StartSSL's root certificate to
		// its trust store, and document steps. For now, I'm going to disable SSL certificate checking.

		// Create a trust manager that does not validate certificate chains
		final TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
					}

					@Override
					public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
					}
				}
		};

		try {
			final SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (final Exception e) {
			e.printStackTrace();
		}

		// Create host name verifier that only trusts cardcast
		final HostnameVerifier allHostsValid = new HostnameVerifier() {

			public boolean verify(String hostname, SSLSession session) {
				return"api.cardcastgame.com".equals(hostname);
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}

	public Website(String ip){
		if(!ip.startsWith("http") && !ip.startsWith("https"))
			ip = "http://" + ip;
		this.ip = ip;
	}

	public Website setUserAgent(String userAgent){
		this.userAgent = userAgent;
		return this;
	}

	public Website setAuthorization(String authorization){
		this.authorization = authorization;
		return this;
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
			http.setRequestProperty("User-Agent", userAgent.equals("") ? "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0" : userAgent);
			if(!authorization.equalsIgnoreCase(""))
				http.setRequestProperty("Authorization", authorization);
			http.setUseCaches(false);
			
			//http.setRequestProperty("", value);
			return new Data(http.getInputStream());
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return new Data();
	}


	public Website setHeader(String secretKey) {
		return null;
	}

	public Data postData(POST post){
		try{
			HttpURLConnection http = (HttpURLConnection) new URL(ip).openConnection();
			http.setRequestProperty("User-Agent", userAgent.equals("") ? "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:44.0) Gecko/20100101 Firefox/44.0" : userAgent);
			http.setRequestProperty("Authorization", authorization);
			http.setDoOutput(true);
			OutputStream out = http.getOutputStream();
			out.write(post.getPost().getBytes());
			out.flush();
			out.close();

			return new Data(http.getInputStream());
		}
		catch(Throwable th){
			th.printStackTrace();
		}
		return new Data();
	}

	public static class POST{

		private static String ENCODING = "UTF-8";
		private HashMap<String, String> post;

		/**
		 * Creates a new <code>Post</code> instance.
		 */
		public POST() {
			post = new HashMap<String, String>();
		}

		public POST setEncoding(String encoding){
			ENCODING = encoding;
			return this;
		}
		
		public POST put(String key, String value) {
		try {
				this.post.put(URLEncoder.encode(key, ENCODING), URLEncoder.encode(value, ENCODING));
				//this.post.put(key, value);
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
