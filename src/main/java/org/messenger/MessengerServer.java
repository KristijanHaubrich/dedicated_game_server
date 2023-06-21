package org.messenger;

import org.messenger.observer.pattern.MessengerObserver;
import org.messenger.observer.pattern.MessengerSubject;
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
        MessengerSubject messengerSubject = new MessengerSubject();

        try (ServerSocket serverSocket = new ServerSocket(portNumber)){
            int clientId = 1;
            while(true){
                Socket clientSocket = serverSocket.accept();
                MessengerObserver messengerObserver = new MessengerObserver(clientSocket);
                new MessengerServerThread(clientSocket,messengerSubject,messengerObserver,clientId).start();
                clientId++;
            }

        }
        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
