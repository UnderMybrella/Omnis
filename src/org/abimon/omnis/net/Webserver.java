package org.abimon.omnis.net;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import org.abimon.omnis.io.Data;
import org.abimon.omnis.ludus.Ludus;
import org.abimon.omnis.reflect.Function;
import org.abimon.omnis.util.General;

public class Webserver implements Runnable{

	public static final Function DEFAULT_RETURN = Function.getUnsafe(Webserver.class, "onMessage", Socket.class, byte[].class, String.class);

	PrintStream log = null;
	int port = 80;

	ServerSocket server = null;

	LinkedList<Function> onMessage = new LinkedList<>();
	LinkedList<Function> dataResponse = new LinkedList<>();

	private Thread internalThread;

	public Webserver(){
		this(80, false, true);
	}

	public Webserver(int port){
		this(port, false, true);
	}
	
	public Webserver(boolean closeAfterRequest){
		this(80, false, closeAfterRequest);
	}
	
	public Webserver(boolean logConnections, boolean closeAfterRequest){
		this(80, logConnections, closeAfterRequest);
	}
	
	public Webserver(int port, boolean logConnections){
		this(port, logConnections, true);
	}
	
	public Webserver(int port, boolean logConnections, boolean closeAfterRequest){
		this.port = port;
		if(logConnections){
			try{
				File logs = new File("logs");
				if(!logs.exists())
					logs.mkdir();
				log = new PrintStream(new File(logs, General.formatDate() + ".txt"));
			}
			catch(Throwable th){
				th.printStackTrace();
			}
		}

		internalThread = new Thread(this);
		internalThread.start();
	}

	/**
	 * @param function Function Parameters: Socket, byte[] fullRequest, String requestedFile. Returns: byte[] returnData
	 */
	public void addMessageFunction(Function function){
		onMessage.add(function);
	}

	/**
	 * @param function Function Parameters: Socket, byte[] fullRequest, String requestedFile. Returns: String or null
	 */
	public void addResponseFunction(Function function) { dataResponse.add(function); }

	@Override
	public void run() {
		while (true) {
			try {
				if (server == null) {
					server = new ServerSocket(port);
					if (log != null)
						log.println("Initialising webserver...");
				}

				final Socket client = server.accept();
				new Thread() {
					public void run() {
						while (true) {
							try {
								Data data = null;
								int count = 0;
								while ((data = new Data(client.getInputStream(), false)).size() == 0 && count++ < 100) {
									Thread.sleep(10);
								}

								String request = null;
								boolean keepAlive = false;

								for (String s : data.getAsString().split("\n"))
									if (s.startsWith("GET"))
										request = s.substring(5).replace(" HTTP/1.1", "").trim();

								if (data.getAsString().contains("keep-alive"))
									keepAlive = true;

								if (log != null)
									log.println(client + " requested " + data.getAsString() + ", " + data.toArray().length + " bytes");

								if (request != null) {

									String type = "text/html";
									for (Function func : dataResponse) {
										try {
											String responseType = (String) func.invoke(client, data.toArray(), request);
											if (log != null)
												log.println(func.getFunction().getDeclaringClass() + "." + func.getFunction().getName() + " says that we're returning " + responseType);
											if(responseType != null)
												type = responseType;
										} catch (Throwable th) {
											if (log != null)
												th.printStackTrace(log);
										}
									}

									if (!keepAlive) {
										client.getOutputStream().write(("HTTP/1.1 200 OK\n" +
												"Content-Type: " + type + "; charset=UTF-8\n" +
												"Content-Encoding: UTF-8\n").getBytes());
										client.getOutputStream().write("Connection: close\n\n".getBytes());
									} else {
										client.getOutputStream().write(("HTTP/1.1 200 OK\n" +
												"Content-Type: " + type + "; charset=UTF-8\n" +
												"Content-Encoding: UTF-8\n\n").getBytes());
									}

									for (Function func : onMessage) {
										try {
											byte[] returnedData = (byte[]) func.invoke(client, data.toArray(), request);
											if (log != null)
												log.println(func.getFunction().getDeclaringClass() + "." + func.getFunction().getName() + " returned " + returnedData.length + " bytes");
											if (returnedData == null || returnedData.length == 0)
												continue;
											client.getOutputStream().write(returnedData);
											client.getOutputStream().write("\n".getBytes());
										} catch (Throwable th) {
											if (log != null)
												th.printStackTrace(log);
										}
									}
								}

								client.getOutputStream().flush();

								if (log != null)
									log.flush();

								if (!keepAlive)
									break;
							} catch (Throwable th) {
								if (log != null)
									th.printStackTrace(log);
								th.printStackTrace();
							}
						}
						try {
							client.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
			} catch (Throwable th) {
				if (log != null)
					th.printStackTrace(log);
				th.printStackTrace();
			}
		}
	}

	public static byte[] onMessage(Socket client, byte[] fullRequest, String requestedFile){
		return Ludus.getDataUnsafe(requestedFile).toArray();
	}
}
