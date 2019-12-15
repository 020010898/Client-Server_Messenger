package server;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;

import network.*;
import client.Client;



public class Server implements ConnectionListener{ 
	
	public static List<Connection> connections = new LinkedList<>();


	private Server() {
    System.out.println("Server running...");
    try(ServerSocket serverSocket = new ServerSocket(5555)) {
        while (true) {
            try {
            	new Connection(this, serverSocket.accept());         
            }catch (IOException e) {
                System.out.println("TCPConnection exception: " + e );
            }catch (ClassNotFoundException cnfe) {
            	
            }
            
        }
    }catch (IOException e){
        throw new RuntimeException();
    }
}

@Override
	public synchronized void onConnectionReady(Connection tcpConnection) {
    connections.add(tcpConnection);
    sendToAllConnections("Client connected: " + tcpConnection);
    
    
}

@Override
	public synchronized void onReceiveString(Connection tcpConnection, String value) {
    sendToAllConnections(value);
}

@Override
	public synchronized void onDisconnect(Connection tcpConnection) {
    connections.remove(tcpConnection);
    sendToAllConnections("Client disconnected: " + tcpConnection);
    
}

@Override
	public synchronized void onException(Connection tcpConnection, Exception e) {
    System.out.println("TCPConnection exception: " + e);
}


	private void sendToAllConnections(String value) {
    System.out.println(value);
    final int cnt = connections.size();
    for (int i = 0; i <cnt ; i++) connections.get(i).sendString(value);
}
	
	public static void main(String[] args) {
	    new Server();

	}

}

