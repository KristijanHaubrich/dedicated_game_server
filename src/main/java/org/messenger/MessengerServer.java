package org.messenger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MessengerServer {
    public static void main(String[] args) throws IOException {

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
            new MessengerServerEchoThread(serverAddress, serverPort).start();

            int clientId = 1;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new MessengerServerHandleClientThread(clientSocket, clientId).start();
                clientId++;
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
