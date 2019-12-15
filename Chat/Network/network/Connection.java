package network;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.*;
import javax.swing.JFileChooser;

public class Connection {

	private final Socket socket;
	private final Thread rxThread;
	private final ConnectionListener eventListener;
	private final BufferedReader in;
	private final BufferedWriter out;
	public static StringBuilder sb = new StringBuilder();

	public Connection(ConnectionListener eventListener, String ipAddr, int port)
			throws IOException, ClassNotFoundException {
		this(eventListener, new Socket(ipAddr, port));

	}

	public Connection(ConnectionListener eventListener, Socket socket) throws IOException, ClassNotFoundException {
		this.eventListener = eventListener;
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
		out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));

		rxThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					eventListener.onConnectionReady(Connection.this);
					while (!rxThread.isInterrupted()) {
						eventListener.onReceiveString(Connection.this, in.readLine());

					}

				} catch (IOException e) {// hier
					eventListener.onException(Connection.this, e);
				} finally {
					eventListener.onDisconnect(Connection.this);
				}
			}
		});
		rxThread.start();
	}

	public synchronized void sendString(String value) {
		try {

			out.write(value + "\r\n");// hier
			out.flush();
		} catch (IOException e) {
			eventListener.onException(Connection.this, e);
			disconnect();
		}
	}

	public synchronized void disconnect() {
		rxThread.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
			eventListener.onException(Connection.this, e);
		}
	}

	public String toString() {
		return "IP_Adr: " + socket.getInetAddress() + " Port: " + socket.getPort();

	}
}
