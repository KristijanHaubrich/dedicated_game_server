package org.game.components;

import java.util.HashSet;
import java.util.Set;

public class ServerCollector {
    private static final ServerCollector instance = new ServerCollector();
    private final Set<ServerConnection> serverConnections = new HashSet<>();

    private ServerCollector() {
    }

    public static ServerCollector getInstance() {
        return instance;
    }

    public void addConnection(ServerConnection serverConnection) {
        synchronized (serverConnections) {
            serverConnections.add(serverConnection);
        }
    }

    public Set<ServerConnection> getServerConnections() {
        synchronized (serverConnections) {
            return serverConnections;
        }
    }
}