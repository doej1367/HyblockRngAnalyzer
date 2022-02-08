package me.hyblockrnganalyzer.util;

public class LobbyStatus {
	private String oldServer;
	private long timestamp;
	private String server;

	public String getServer() {
		return server;
	}

	public void setServer(String newServer) {
		this.oldServer = server;
		timestamp = System.currentTimeMillis();
		this.server = newServer;
	}

	public void setServerChangeFailed(String failedServer) {
		if (failedServer.equalsIgnoreCase(this.server))
			this.server = oldServer;
	}

}
