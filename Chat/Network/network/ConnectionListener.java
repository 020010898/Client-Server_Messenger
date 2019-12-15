package network;

public interface ConnectionListener {

	void onConnectionReady(Connection tcpConnection);

	void onReceiveString(Connection tcpConnection, String value);

	void onDisconnect(Connection tcpConnection);

	void onException(Connection tcpConnection, Exception e);
}