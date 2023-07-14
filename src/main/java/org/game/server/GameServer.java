package org.game.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

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

            //boolean for ServerTerminalThread,GameServerHandleClientThread and this thread
            AtomicBoolean isServerUp = new AtomicBoolean(true);
            //boolean for GameServerEchoThread
            AtomicBoolean isMainServerThreadUp = new AtomicBoolean(true);

            new ServerTerminalThread(isServerUp).start();
            new GameServerEchoThread(serverAddress, serverPort,isMainServerThreadUp).start();

            int clientId = 1;
            while (isServerUp.get()) {
                Socket clientSocket = serverSocket.accept();
                new GameServerHandleClientThread(clientSocket,clientId,isServerUp).start();
                clientId++;
            }
            //GameServer don't see isServerUp change until accepts one more connection to client so Echo thread needs to be alive until one more client connects
            isMainServerThreadUp.set(false);

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
