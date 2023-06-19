package org.knockknock.messenger;

import org.knockknock.messenger.ObserverPattern.MessengerObserver;
import org.knockknock.messenger.ObserverPattern.MessengerSubject;
import org.knockknock.multiClientKnockKnock.KKMultiServerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MessengerServer {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java KnockKnockServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            MessengerSubject messengerSubject = new MessengerSubject();
            int counter = 0;
            while(true){
                Socket clientSocket = serverSocket.accept();

                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                if(counter == 0) {
                    out.println("Postali ste podanik.\nPoruke kralja:");
                    MessengerObserver messengerObserver = new MessengerObserver(clientSocket);
                    messengerSubject.sub(messengerObserver);
                    counter++;
                } else{
                    out.println("Želiš li postati kralj?(y/n)");
                    String clientMessage = "";
                    while(clientMessage == ""){
                        clientMessage = in.readLine();
                    }
                    if(clientMessage.equals("y")){
                        messengerSubject.run(clientSocket);
                    }else{
                        out.println("Postao si podanik\nPoruke kralja:");
                        MessengerObserver messengerObserver = new MessengerObserver(clientSocket);
                        messengerSubject.sub(messengerObserver);
                    }
                }

            }
        }
        catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
