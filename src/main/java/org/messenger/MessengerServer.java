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

        try (
                ServerSocket serverSocket = new ServerSocket(portNumber)
        ){
            String server_address = serverSocket.getInetAddress().getHostAddress();
            String server_port = String.valueOf(portNumber);
            new MessengerServerEchoThread(server_address,server_port).start();
            MessengerSubject messengerSubject = MessengerSubject.getInstance();
            int clientId = 1;
            while(true){
                Socket clientSocket = serverSocket.accept();
                MessengerObserver messengerObserver = new MessengerObserver(clientSocket);
                new MessengerServerHandleClientThread(clientSocket,messengerSubject,messengerObserver,clientId).start();
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
