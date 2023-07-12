package org.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber)
        ) {
            String serverAddress = serverSocket.getInetAddress().getHostAddress();
            String serverPort = String.valueOf(portNumber);
            new GameServerEchoThread(serverAddress, serverPort).start();

            int clientId = 1;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new GameServerHandleClientThread(clientSocket,clientId).start();
                clientId++;
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
