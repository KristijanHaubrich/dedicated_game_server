package org.game.components;

public class ServerConnection {
    private final String hostname;
    private final int portNumber;

    public ServerConnection(String hostname, int portNumber) {
        this.hostname = hostname;
        this.portNumber = portNumber;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getHostname() {
        return hostname;
    }

    @Override
    public String toString() {
        return hostname + "__" + portNumber;
    }
}
