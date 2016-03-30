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

	LinkedList<Function> onMessage = new LinkedList<Function>();

	private Thread internalThread;

	public Webserver(){
		this(80, false);
	}

	public Webserver(int port, boolean logConnections){
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

	@Override
	public void run() {
		while(true){
			try{
				if(server == null){
					server = new ServerSocket(port);
					if(log != null)
						log.println("Initialising webserver...");
				}

				final Socket client = server.accept();
				new Thread(){
					public void run(){
						while(true){
							try{
								Data data = new Data(client.getInputStream(), false);

								String request = "";
								boolean keepAlive = false;

								for(String s : data.getAsString().split("\n"))
									if(s.startsWith("GET"))
										request = s.substring(5).replace(" HTTP/1.1", "").trim();
								
								if(data.getAsString().contains("keep-alive"))
										keepAlive = true;

								if(log != null)
									log.println(client + " requested " + data.getAsString() + ", " + data.toArray().length + " bytes");
								for(Function func : onMessage){
									try{
										byte[] returnedData = (byte[]) func.invoke(client, data.toArray(), request);
										if(log != null)
											log.println(func.getFunction().getDeclaringClass() + "." + func.getFunction().getName() + " returned " + returnedData.length + " bytes");
										client.getOutputStream().write(returnedData);
										client.getOutputStream().write("\n".getBytes());
									}
									catch(Throwable th){
										if(log != null)
											th.printStackTrace(log);
									}
								}

								Thread.sleep(1000);

								client.getOutputStream().flush();

								if(log != null)
									log.flush();


								if(!keepAlive)
									break;
							}
							catch(Throwable th){
								if(log != null)
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
			}
			catch(Throwable th){
				if(log != null)
					th.printStackTrace(log);
				th.printStackTrace();
			}
		}
	}

	public static byte[] onMessage(Socket client, byte[] fullRequest, String requestedFile){
		return Ludus.getDataUnsafe(requestedFile).toArray();
	}
}
